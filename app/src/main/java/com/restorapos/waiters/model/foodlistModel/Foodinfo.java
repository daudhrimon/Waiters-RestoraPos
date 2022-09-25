package com.restorapos.waiters.model.foodlistModel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.restorapos.waiters.offlineDb.Converters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Foodinfo implements Serializable {

    public int quantitys = 0;
    public String itemNote;
    public String addOnsName;
    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("ProductsID")
    @Expose
    private String productId;
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
    private String totalvariant;
    @SerializedName("addons")
    @Expose
    private Integer addons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TypeConverters(Converters.class)
    @SerializedName("addonsinfo")
    @Expose
    private List<Addonsinfo> addonsinfo = null;
    public int quantity = 0;
    public double addOnsTotal = 0;

    @TypeConverters(Converters.class)
    @SerializedName("varientlist")
    @Expose
    private List<Varientlist> varientlist = null;

    public int getQuantitys() {
        return quantitys;
    }

    public void setQuantitys(int quantitys) {
        this.quantitys = quantitys;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public String getAddOnsName() {
        return addOnsName;
    }

    public void setAddOnsName(String addOnsName) {
        this.addOnsName = addOnsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAddOnsTotal() {
        return addOnsTotal;
    }

    public void setAddOnsTotal(double addOnsTotal) {
        this.addOnsTotal = addOnsTotal;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}
