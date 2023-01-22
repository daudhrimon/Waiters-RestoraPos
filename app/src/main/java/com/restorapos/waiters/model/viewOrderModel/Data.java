package com.restorapos.waiters.model.viewOrderModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("CustomerPhone")
    private String customerPhone;

    @SerializedName("TableName")
    private String tableName;

    @SerializedName("service_charge")
    private String serviceCharge;

    @SerializedName("CustomerEmail")
    private String customerEmail;

    @SerializedName("iteminfo")
    private List<IteminfoItem> iteminfo;

    @SerializedName("Subtotal")
    private String subtotal;

    @SerializedName("VAT")
    private String vAT;

    @SerializedName("discount")
    private String discount;

    @SerializedName("order_total")
    private String orderTotal;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("orderdate")
    private String orderdate;

    @SerializedName("CustomerType")
    private String customerType;

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setIteminfo(List<IteminfoItem> iteminfo) {
        this.iteminfo = iteminfo;
    }

    public List<IteminfoItem> getIteminfo() {
        return iteminfo;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setVAT(String vAT) {
        this.vAT = vAT;
    }

    public String getVAT() {
        return vAT;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerType() {
        return customerType;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "customerPhone = '" + customerPhone + '\'' +
                        ",tableName = '" + tableName + '\'' +
                        ",service_charge = '" + serviceCharge + '\'' +
                        ",customerEmail = '" + customerEmail + '\'' +
                        ",iteminfo = '" + iteminfo + '\'' +
                        ",subtotal = '" + subtotal + '\'' +
                        ",vAT = '" + vAT + '\'' +
                        ",discount = '" + discount + '\'' +
                        ",order_total = '" + orderTotal + '\'' +
                        ",customerName = '" + customerName + '\'' +
                        ",orderdate = '" + orderdate + '\'' +
                        ",customerType = '" + customerType + '\'' +
                        "}";
    }
}