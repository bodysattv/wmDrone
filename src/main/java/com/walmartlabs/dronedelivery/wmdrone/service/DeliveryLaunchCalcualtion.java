package com.walmartlabs.dronedelivery.wmdrone.service;

import com.walmartlabs.dronedelivery.wmdrone.domain.OrderData;
import com.walmartlabs.dronedelivery.wmdrone.util.DeliveryComparator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;


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

    public void tagInput(final List<OrderData> orderList) {

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

        for(OrderData input: orderList){

            //lets set Promoter as default.
            input.setTag(OrderData.Tag.PROMOTER);

            // if an order can not be reached (first possible launching time plus time taken
            // to reach its location) in 1 hours, its nuetral or a detractor.
            if (input.getTimeStamp().plusHours(timePromoterMax).plusMinutes(input.getTimeToLocation()).isBefore(firstLaunchTime)) {
                input.setTag(OrderData.Tag.NUETRAL);
            }
            // if an order can not be reached (first possible launching time plus time taken
            // to reach its location) in 3 hours, its certainly a detractor.
            if (input.getTimeStamp().plusHours(timeNeutralMax).plusMinutes(input.getTimeToLocation()).isBefore(firstLaunchTime)) {
                input.setTag(OrderData.Tag.DETRACTOR);
            }
            //for all orders placed after 10 pm
            if (input.getTimeStamp().isAfter(LocalTime.parse(delStopStr))) {
                input.setTag(OrderData.Tag.DETRACTOR);
            }
            
            
        };

        Collections.sort(orderList, new DeliveryComparator());
        orderList.forEach(System.out::println);

    }

}