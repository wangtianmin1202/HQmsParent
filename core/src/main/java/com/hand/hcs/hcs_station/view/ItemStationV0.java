package com.hand.hcs.hcs_station.view;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * description
 *
 * @author KOCE3G3 2020/04/02 10:18 AM
 */
public class ItemStationV0 implements Serializable {

    private Long stationSequence;

    @Length(max = 80)
    private String stationName;

    public Long getStationSequence() {
        return stationSequence;
    }

    public void setStationSequence(Long stationSequence) {
        this.stationSequence = stationSequence;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
