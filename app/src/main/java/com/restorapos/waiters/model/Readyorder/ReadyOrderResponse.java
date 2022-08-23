package com.restorapos.waiters.model.Readyorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadyOrderResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ReadyOrderOtherInfo readyOrderOtherInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReadyOrderOtherInfo getReadyOrderOtherInfo() {
        return readyOrderOtherInfo;
    }

    public void setReadyOrderOtherInfo(ReadyOrderOtherInfo readyOrderOtherInfo) {
        this.readyOrderOtherInfo = readyOrderOtherInfo;
    }
}
