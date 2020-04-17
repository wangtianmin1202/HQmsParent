package com.hand.spc.utils;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = -638488203753911569L;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T rows;

    private boolean success = true;
    
    @JsonInclude(Include.NON_NULL)
    private int total;

    public ResponseData() {}

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(T list) {
        this(true);
        setRows(list);
    }

    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getRows() {
        return rows;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
