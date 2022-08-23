package com.restorapos.waiters.model.customerModel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CustomerFullListResponse {
    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("data")
    private List<CustomerFullList> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<CustomerFullList> getData() {
        return data;
    }

    public void setData(List<CustomerFullList> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
