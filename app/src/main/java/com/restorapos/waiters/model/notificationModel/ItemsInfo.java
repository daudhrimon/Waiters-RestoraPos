package com.restorapos.waiters.model.notificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.restorapos.waiters.model.viewOrderModel.Addonsinfo;
import java.util.List;

public class ItemsInfo {
    @SerializedName("ProductsID")
    @Expose
    private String productsID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("component")
    @Expose
    private String component;
    @SerializedName("destcription")
    @Expose
    private String destcription;
    @SerializedName("itemnotes")
    @Expose
    private String itemnotes;
    @SerializedName("productvat")
    @Expose
    private String productvat;
    @SerializedName("offerIsavailable")
    @Expose
    private String offerIsavailable;
    @SerializedName("offerstartdate")
    @Expose
    private String offerstartdate;
    @SerializedName("OffersRate")
    @Expose
    private String offersRate;
    @SerializedName("offerendate")
    @Expose
    private String offerendate;
    @SerializedName("ProductImage")
    @Expose
    private String productImage;
    @SerializedName("Varientname")
    @Expose
    private String varientname;
    @SerializedName("Varientid")
    @Expose
    private String varientid;
    @SerializedName("Itemqty")
    @Expose
    private String itemqty;
    @SerializedName("addons")
    @Expose
    private Integer addons;
    @SerializedName("addonsinfo")
    @Expose
    private List<Addonsinfo> addonsinfo = null;

    public String getProductsID() {
        return productsID;
    }

    public void setProductsID(String productsID) {
        this.productsID = productsID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDestcription() {
        return destcription;
    }

    public void setDestcription(String destcription) {
        this.destcription = destcription;
    }

    public String getItemnotes() {
        return itemnotes;
    }

    public void setItemnotes(String itemnotes) {
        this.itemnotes = itemnotes;
    }

    public String getProductvat() {
        return productvat;
    }

    public void setProductvat(String productvat) {
        this.productvat = productvat;
    }

    public String getOfferIsavailable() {
        return offerIsavailable;
    }

    public void setOfferIsavailable(String offerIsavailable) {
        this.offerIsavailable = offerIsavailable;
    }

    public String getOfferstartdate() {
        return offerstartdate;
    }

    public void setOfferstartdate(String offerstartdate) {
        this.offerstartdate = offerstartdate;
    }

    public String getOffersRate() {
        return offersRate;
    }

    public void setOffersRate(String offersRate) {
        this.offersRate = offersRate;
    }

    public String getOfferendate() {
        return offerendate;
    }

    public void setOfferendate(String offerendate) {
        this.offerendate = offerendate;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getVarientname() {
        return varientname;
    }

    public void setVarientname(String varientname) {
        this.varientname = varientname;
    }

    public String getVarientid() {
        return varientid;
    }

    public void setVarientid(String varientid) {
        this.varientid = varientid;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public Integer getAddons() {
        return addons;
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
}
