package com.restorapos.waiters.model.notificationModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderinfoItem {

    @SerializedName("amount")
    private String amount;

    @SerializedName("orderid")
    private String orderid;

    @SerializedName("customer")
    private String customer;

    @SerializedName("iteminfo")
    private List<ItemsInfo> iteminfo;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public List<ItemsInfo> getIteminfo() {
        return iteminfo;
    }

    public void setIteminfo(List<ItemsInfo> iteminfo) {
        this.iteminfo = iteminfo;
    }

    @Override
    public String toString() {
        return
                "OrderinfoItem{" +
                        "amount = '" + amount + '\'' +
                        ",orderid = '" + orderid + '\'' +
                        ",customer = '" + customer + '\'' +
                        "}";
    }
}