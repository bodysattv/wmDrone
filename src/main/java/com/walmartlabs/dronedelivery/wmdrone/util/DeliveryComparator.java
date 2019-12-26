package com.walmartlabs.dronedelivery.wmdrone.util;

import java.util.Comparator;

import com.walmartlabs.dronedelivery.wmdrone.domain.InputData;

public class DeliveryComparator implements Comparator<InputData> {
    @Override
    public int compare(final InputData o1, final InputData o2) {

        int result = o1.getTimeToLocation().compareTo(o2.getTimeToLocation());
        if(result == 0) {
            result =  o1.getTimeStamp().compareTo(o2.getTimeStamp());
        }
       return result;
    }
}