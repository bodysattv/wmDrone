package com.walmartlabs.dronedelivery.wmdrone.service;

import com.walmartlabs.dronedelivery.wmdrone.domain.OrderData;
import com.walmartlabs.dronedelivery.wmdrone.util.DeliveryComparator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * This class has calls to calculate sequence of delivery to optimize NPR.
 * 
 */

@Service
public class DeliveryLaunchCalcualtion {

    @Value("${delivery.time.start}")
    private String delStartStr;

    @Value("${delivery.time.stop}")
    private String delStopStr;

    @Value("${delivery.interval.promoter.max}")
    private Integer timePromoterMax;

    @Value("${delivery.interval.neutral.max}")
    private Integer timeNeutralMax;

    /**
     * This is entry method of the class where it gets the order list and write the
     * calculate sequence and resulatant NPR in a file.
     * 
     * @param orderList
     * @param filePath
     * @throws IOException
     */
    public void generateOptimizedSequence(final List<OrderData> orderList, final String filePath) throws IOException {

        // first tag all orders to its possible categories.
        final LocalTime firstLaunchTime = tagInput(orderList);

        // solve on the order delivery sequence for the NPR maximization
        resequenceOrders(orderList, firstLaunchTime);

        // calculate lauch time
        calculateLaunchTime(orderList, firstLaunchTime);

        // generate the output file
        generateOutput(orderList, filePath);

    }

    /**
     * generate the output file
     * 
     * @param orderList
     * @param fileName
     * @throws IOException
     */
    private void generateOutput(final List<OrderData> orderList, final String fileName) throws IOException {
        Integer numberOfPromoters = 0;
        Integer numberOfDertactors = 0;
        final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (final OrderData input : orderList) {
            final String outputLine = new StringBuilder(input.getId()).append(" ")
                    .append(input.getLaunchTime().format(DateTimeFormatter.ISO_LOCAL_TIME)).toString();

            if(input.getTag().equals(OrderData.Tag.PROMOTER)){
                numberOfPromoters += 1;
            }else if(input.getTag().equals(OrderData.Tag.DETRACTOR)){
                numberOfDertactors += 1;
            }


            writer.write(outputLine + "\n");

        }
        //write NPS calculation
        Integer npsValue = (numberOfPromoters-numberOfDertactors)*100/orderList.size();
        String npsLine = new StringBuilder("NPS ").append(npsValue).toString();
        writer.write(npsLine);

        writer.close();

    }


    /**
     * calculate lauch time for each order in the list
     * 
     * @param orderList
     * @param firstLaunchTime
     */
    private void calculateLaunchTime(final List<OrderData> orderList, final LocalTime firstLaunchTime) {

        LocalTime launchTime = firstLaunchTime;
        for (final OrderData input : orderList) {
            input.setLaunchTime(launchTime);
            //calculate the launch time for the next delivery (last + to and fro for this delivery time)
            launchTime.plusMinutes(2 * input.getTimeToLocation());
        }

    }

    /**
     * solve on the order delivery sequence for the NPR maximization
     * 
     * @param orderList
     * @param firstLaunchTime
     */
    private void resequenceOrders(final List<OrderData> orderList, final LocalTime firstLaunchTime) {
        Collections.sort(orderList, new DeliveryComparator());
        orderList.forEach(System.out::println);

    }

    /**
     * temporary tags to all orders in its likelihood of categories.
     * 
     * @param orderList
     * @return
     */
    public LocalTime tagInput(final List<OrderData> orderList) {

        final LocalTime firstOrderTime = orderList.get(0).getTimeStamp();
        final LocalTime lastOrderTime = orderList.get(orderList.size() - 1).getTimeStamp();

        // finding first delivery launch time
        // 1. by default it is 6AM (same day if last entry is before 6am or next day if
        // first entry is after 10 pm)
        LocalTime firstLaunchTime = LocalTime.parse(delStartStr);
        // unless last order is in between the delivery time.
        if (lastOrderTime.isAfter(firstLaunchTime) && !lastOrderTime.isAfter(LocalTime.parse(delStopStr))) {
            firstLaunchTime = lastOrderTime;
        }

        for (final OrderData input : orderList) {

            // lets set Promoter as default.
            input.setTag(OrderData.Tag.PROMOTER);

            // if an order can not be reached (first possible launching time plus time taken
            // to reach its location) in 1 hours, its nuetral or a detractor.
            if (input.getTimeStamp().plusHours(timePromoterMax).plusMinutes(input.getTimeToLocation())
                    .isBefore(firstLaunchTime)) {
                input.setTag(OrderData.Tag.NUETRAL);
            }
            // if an order can not be reached (first possible launching time plus time taken
            // to reach its location) in 3 hours, its certainly a detractor.
            if (input.getTimeStamp().plusHours(timeNeutralMax).plusMinutes(input.getTimeToLocation())
                    .isBefore(firstLaunchTime)) {
                input.setTag(OrderData.Tag.DETRACTOR);
            }
            // for all orders placed after 10 pm
            if (input.getTimeStamp().isAfter(LocalTime.parse(delStopStr))) {
                input.setTag(OrderData.Tag.DETRACTOR);
            }

        }
        ;

        return firstLaunchTime;

    }

}