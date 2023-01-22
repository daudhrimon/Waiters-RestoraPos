package com.restorapos.waiters.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FoodinfoItem {

    public int quantitys = 0;
    @SerializedName("addOnsName")
    private String addOnsName;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("addons")
    private String addons;

    @SerializedName("offerstartdate")
    private String offerstartdate;

    @SerializedName("ProductImage")
    private String productImage;

    @SerializedName("productvat")
    private String productvat;

    @SerializedName("OffersRate")
    private String offersRate;

    @SerializedName("destcription")
    private String destcription;

    @SerializedName("offerIsavailable")
    private String offerIsavailable;

    @SerializedName("ProductsID")
    private String productsID;

    @SerializedName("addonsinfo")
    private List<AddonsinfoItem> addonsinfo;

    @SerializedName("component")
    private String component;

    @SerializedName("price")
    private String price;

    @SerializedName("offerendate")
    private String offerendate;

    @SerializedName("variantid")
    private String variantid;

    @SerializedName("variantName")
    private String variantName;

    @SerializedName("addOnsTotal")
    private String addOnsTotal;

    @SerializedName("itemnotes")
    private String itemnotes;

    public void setAddOnsName(String addOnsName) {
        this.addOnsName = addOnsName;
    }

    public String getAddOnsName() {
        return addOnsName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setAddons(String addons) {
        this.addons = addons;
    }

    public String getAddons() {
        return addons;
    }

    public void setOfferstartdate(String offerstartdate) {
        this.offerstartdate = offerstartdate;
    }

    public String getOfferstartdate() {
        return offerstartdate;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductvat(String productvat) {
        this.productvat = productvat;
    }

    public String getProductvat() {
        return productvat;
    }

    public void setOffersRate(String offersRate) {
        this.offersRate = offersRate;
    }

    public String getOffersRate() {
        return offersRate;
    }

    public void setDestcription(String destcription) {
        this.destcription = destcription;
    }

    public String getDestcription() {
        return destcription;
    }

    public void setOfferIsavailable(String offerIsavailable) {
        this.offerIsavailable = offerIsavailable;
    }

    public String getOfferIsavailable() {
        return offerIsavailable;
    }

    public void setProductsID(String productsID) {
        this.productsID = productsID;
    }

    public String getProductsID() {
        return productsID;
    }

    public void setAddonsinfo(List<AddonsinfoItem> addonsinfo) {
        this.addonsinfo = addonsinfo;
    }

    public List<AddonsinfoItem> getAddonsinfo() {
        return addonsinfo;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setOfferendate(String offerendate) {
        this.offerendate = offerendate;
    }

    public String getOfferendate() {
        return offerendate;
    }

    public void setVariantid(String variantid) {
        this.variantid = variantid;
    }

    public String getVariantid() {
        return variantid;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setAddOnsTotal(String addOnsTotal) {
        this.addOnsTotal = addOnsTotal;
    }

    public String getAddOnsTotal() {
        return addOnsTotal;
    }

    public void setItemnotes(String itemnotes) {
        this.itemnotes = itemnotes;
    }

    public String getItemnotes() {
        return itemnotes;
    }

    @Override
    public String toString() {
        return
                "FoodinfoItem{" +
                        "addOnsName = '" + addOnsName + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",productName = '" + productName + '\'' +
                        ",addons = '" + addons + '\'' +
                        ",offerstartdate = '" + offerstartdate + '\'' +
                        ",productImage = '" + productImage + '\'' +
                        ",productvat = '" + productvat + '\'' +
                        ",offersRate = '" + offersRate + '\'' +
                        ",destcription = '" + destcription + '\'' +
                        ",offerIsavailable = '" + offerIsavailable + '\'' +
                        ",productsID = '" + productsID + '\'' +
                        ",addonsinfo = '" + addonsinfo + '\'' +
                        ",component = '" + component + '\'' +
                        ",price = '" + price + '\'' +
                        ",offerendate = '" + offerendate + '\'' +
                        ",variantid = '" + variantid + '\'' +
                        ",variantName = '" + variantName + '\'' +
                        ",addOnsTotal = '" + addOnsTotal + '\'' +
                        ",itemnotes = '" + itemnotes + '\'' +
                        "}";
    }
}