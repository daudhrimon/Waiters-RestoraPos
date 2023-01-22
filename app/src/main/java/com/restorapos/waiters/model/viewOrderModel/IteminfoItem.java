package com.restorapos.waiters.model.viewOrderModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IteminfoItem {

    @SerializedName("ProductsID")
    private String productsID;

    @SerializedName("Varientid")
    private String varientid;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("price")
    private String price;
    @SerializedName("status")
    private String status;

    @SerializedName("Itemqty")
    private String itemqty;

    @SerializedName("Varientname")
    private String varientname;

    @SerializedName("Itemtotal")
    private String itemtotal;
    @SerializedName("addons")
    private Integer addons;
    @SerializedName("addonsinfo")
    private List<Addonsinfo> addonsinfo = null;

    public Integer getAddons() {
        return addons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddons(Integer addons) {
        this.addons = addons;
    }

    public List<Addonsinfo> getAddonsinfo() {
        return addonsinfo;
    }

    public void setAddonsinfo(List<Addonsinfo> addonsinfo) {
        this.addonsinfo = addonsinfo;
    }

    public void setProductsID(String productsID) {
        this.productsID = productsID;
    }

    public String getProductsID() {
        return productsID;
    }

    public void setVarientid(String varientid) {
        this.varientid = varientid;
    }

    public String getVarientid() {
        return varientid;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setVarientname(String varientname) {
        this.varientname = varientname;
    }

    public String getVarientname() {
        return varientname;
    }

    public void setItemtotal(String itemtotal) {
        this.itemtotal = itemtotal;
    }

    public String getItemtotal() {
        return itemtotal;
    }

    @Override
    public String toString() {
        return
                "IteminfoItem{" +
                        "productsID = '" + productsID + '\'' +
                        ",varientid = '" + varientid + '\'' +
                        ",productName = '" + productName + '\'' +
                        ",price = '" + price + '\'' +
                        ",itemqty = '" + itemqty + '\'' +
                        ",varientname = '" + varientname + '\'' +
                        ",itemtotal = '" + itemtotal + '\'' +
                        "}";
    }
}