package com.walmartlabs.dronedelivery.wmdrone.service;

import com.walmartlabs.dronedelivery.wmdrone.domain.OrderData;
import com.walmartlabs.dronedelivery.wmdrone.exception.BadInputFileException;
import com.walmartlabs.dronedelivery.wmdrone.util.DeliveryComparator;
import com.walmartlabs.dronedelivery.wmdrone.util.FileReadWriteUtil;
import com.walmartlabs.dronedelivery.wmdrone.util.InputFileParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * This class has calls to calculate sequence of delivery to optimize NPS.
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

    @Autowired
    private InputFileParser parserService;

    /**
     * This is entry method of the class where it gets the order list and write the
     * calculate sequence and resulatant NPS in a file.
     * 
     * @param orderList
     * @param filePath
     * @throws IOException
     */
    public String generateOptimizedSequence(final String filePath) throws BadInputFileException, IOException {

        String outputFilePath = "";
        try {
            outputFilePath = parserService.getOutputFileName(filePath);
            final List<OrderData> inputList = parserService
                    .convertToOrderDataList(FileReadWriteUtil.readFromInputFile(filePath));

            inputList.forEach(System.out::println);

            // first tag all orders to its possible categories.
            final LocalTime firstLaunchTime = tagInput(inputList);

            // resequence the order delivery priority for NPS maximization
            resequenceOrders(inputList, firstLaunchTime);

            // calculate lauch time
            calculateLaunchTime(inputList, firstLaunchTime);

            // generate the output file
            FileReadWriteUtil.generateOutput(inputList, outputFilePath);

        } catch (final BadInputFileException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return outputFilePath;

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
            // calculate the launch time for the next delivery (last + to and fro for this
            // delivery time)
            launchTime = launchTime.plusMinutes(2 * input.getTimeToLocation());
        }

    }

    /**
     * solve on the order delivery sequence for the NPS maximization
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