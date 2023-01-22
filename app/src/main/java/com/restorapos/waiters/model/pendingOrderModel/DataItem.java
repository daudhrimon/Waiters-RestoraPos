package com.restorapos.waiters.model.pendingOrderModel;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("TableName")
    private String tableName;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("TotalAmount")
    private String totalAmount;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("tokenno")
    private String tokenno;

    @SerializedName("OrderDate")
    private String orderDate;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "tableName = '" + tableName + '\'' +
                        ",customerName = '" + customerName + '\'' +
                        ",totalAmount = '" + totalAmount + '\'' +
                        ",order_id = '" + orderId + '\'' +
                        ",orderDate = '" + orderDate + '\'' +
                        "}";
    }
}