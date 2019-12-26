
package com.walmartlabs.dronedelivery.wmdrone.domain;

import java.time.LocalTime;

public class InputData {

    private String id;
    private String location;
    private Integer timeToLocation;
    private String timeStampStr;
    private LocalTime timeStamp;
    private String tag;

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTimeToLocation() {
        return timeToLocation;
    }

    public void setTimeToLocation(Integer timeToLocation) {
        this.timeToLocation = timeToLocation;
    }

    public LocalTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStampStr() {
        return timeStampStr;
    }

    public void setTimeStampStr(final String timeStampStr) {
        this.timeStampStr = timeStampStr;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }


    public String toString() {
        return ( id + " " + timeToLocation + " " + timeStampStr);
}
}