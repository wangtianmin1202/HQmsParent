package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;

/**
 * description
 *
 * @author KOCDZX0 2020/03/13 10:16 AM
 */
public class EcrDetailsVO implements Serializable {

    private Long wmsOnhand;

    private Long mesOhand;

    private Date wmsLastUpdateDate;

    private Date mesLastUpdate;

    public Long getWmsOnhand() {
        return wmsOnhand;
    }

    public void setWmsOnhand(Long wmsOnhand) {
        this.wmsOnhand = wmsOnhand;
    }

    public Long getMesOhand() {
        return mesOhand;
    }

    public void setMesOhand(Long mesOhand) {
        this.mesOhand = mesOhand;
    }

    public Date getWmsLastUpdateDate() {
        return wmsLastUpdateDate;
    }

    public void setWmsLastUpdateDate(Date wmsLastUpdateDate) {
        this.wmsLastUpdateDate = wmsLastUpdateDate;
    }

    public Date getMesLastUpdate() {
        return mesLastUpdate;
    }

    public void setMesLastUpdate(Date mesLastUpdate) {
        this.mesLastUpdate = mesLastUpdate;
    }
}
