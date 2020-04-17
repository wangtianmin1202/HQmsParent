package com.hand.spc.repository.dto;

import java.util.Date;
import java.util.List;

public class BaseCalcResultVO {

    private List<MessageR> messageList;
    private String messageType;
    private Date messageCreationDate;

    public List<MessageR> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageR> messageList) {
        this.messageList = messageList;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Date getMessageCreationDate() {
        if (messageCreationDate != null) {
            return (Date) messageCreationDate.clone();
        } else {
            return null;
        }
    }

    public void setMessageCreationDate(Date messageCreationDate) {
        if (messageCreationDate == null) {
            this.messageCreationDate = null;
        } else {
            this.messageCreationDate = (Date) messageCreationDate.clone();
        }
    }

}
