package com.restorapos.waiters.model.notificationModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("orderinfo")
    private List<OrderinfoItem> orderinfo;

    public void setOrderinfo(List<OrderinfoItem> orderinfo) {
        this.orderinfo = orderinfo;
    }

    public List<OrderinfoItem> getOrderinfo() {
        return orderinfo;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "orderinfo = '" + orderinfo + '\'' +
                        "}";
    }
}