package com.restorapos.waiters.model.Readyorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadyorderData {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_people")
    @Expose
    private String totalPeople;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("TableName")
    @Expose
    private String tableName;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(String totalPeople) {
        this.totalPeople = totalPeople;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
