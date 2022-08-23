package com.restorapos.waiters.model.foodlistModel2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Foodinfo2 {
    @SerializedName("ProductsID")
    @Expose
    private String productsID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductImage")
    @Expose
    private String productImage;
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
    @SerializedName("OffersRate")
    @Expose
    private String offersRate;
    @SerializedName("offerIsavailable")
    @Expose
    private String offerIsavailable;
    @SerializedName("offerstartdate")
    @Expose
    private String offerstartdate;
    @SerializedName("offerendate")
    @Expose
    private String offerendate;
    @SerializedName("variantid")
    @Expose
    private String variantid;
    @SerializedName("variantName")
    @Expose
    private String variantName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("totalvariant")
    @Expose
    private String totalvariant;
    @SerializedName("varientlist")
    @Expose
    private List<Varientlist> varientlist = null;
    @SerializedName("addons")
    @Expose
    private Integer addons;

    private List<AddonsInfo2> addonsinfo = null;

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getOffersRate() {
        return offersRate;
    }

    public void setOffersRate(String offersRate) {
        this.offersRate = offersRate;
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

    public String getOfferendate() {
        return offerendate;
    }

    public void setOfferendate(String offerendate) {
        this.offerendate = offerendate;
    }

    public String getVariantid() {
        return variantid;
    }

    public void setVariantid(String variantid) {
        this.variantid = variantid;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalvariant() {
        return totalvariant;
    }

    public void setTotalvariant(String totalvariant) {
        this.totalvariant = totalvariant;
    }

    public List<Varientlist> getVarientlist() {
        return varientlist;
    }

    public void setVarientlist(List<Varientlist> varientlist) {
        this.varientlist = varientlist;
    }

    public Integer getAddons() {
        return addons;
    }

    public void setAddons(Integer addons) {
        this.addons = addons;
    }

    public List<AddonsInfo2> getAddonsinfo() {
        return addonsinfo;
    }

    public void setAddonsinfo(List<AddonsInfo2> addonsinfo) {
        this.addonsinfo = addonsinfo;
    }
}
