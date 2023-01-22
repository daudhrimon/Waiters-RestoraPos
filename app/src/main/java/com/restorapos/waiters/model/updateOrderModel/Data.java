package com.restorapos.waiters.model.updateOrderModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("orderid")
    private String orderid;

    @SerializedName("vat")
    private String vat;

    @SerializedName("iteminfo")
    private List<IteminfoItem> iteminfo;

    @SerializedName("discount")
    private String discount;

    @SerializedName("Grandtotal")
    private String grandtotal;

    @SerializedName("Servicecharge")
    private String servicecharge;
    @SerializedName("Table")
    private String table;
    @SerializedName("customername")
    private String customername;

    public String getCustomername() {
        return customername;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getVat() {
        return vat;
    }

    public void setIteminfo(List<IteminfoItem> iteminfo) {
        this.iteminfo = iteminfo;
    }

    public List<IteminfoItem> getIteminfo() {
        return iteminfo;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setServicecharge(String servicecharge) {
        this.servicecharge = servicecharge;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "orderid = '" + orderid + '\'' +
                        ",vat = '" + vat + '\'' +
                        ",iteminfo = '" + iteminfo + '\'' +
                        ",discount = '" + discount + '\'' +
                        ",grandtotal = '" + grandtotal + '\'' +
                        ",servicecharge = '" + servicecharge + '\'' +
                        "}";
    }
}