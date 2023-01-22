package com.restorapos.waiters.model.foodlistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("PcategoryID")
    @Expose
    private String pcategoryID;
    @SerializedName("Restaurantvat")
    @Expose
    private String restaurantvat;
    @SerializedName("VAT")
    @Expose
    private String vat;
    @SerializedName("categoryinfo")
    @Expose
    private List<Categoryinfo> categoryinfo = null;
    @SerializedName("foodinfo")
    @Expose
    private List<Foodinfo> foodinfo = null;

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getPcategoryID() {
        return pcategoryID;
    }

    public void setPcategoryID(String pcategoryID) {
        this.pcategoryID = pcategoryID;
    }

    public String getRestaurantvat() {
        return restaurantvat;
    }

    public void setRestaurantvat(String restaurantvat) {
        this.restaurantvat = restaurantvat;
    }

    public List<Categoryinfo> getCategoryinfo() {
        return categoryinfo;
    }

    public void setCategoryinfo(List<Categoryinfo> categoryinfo) {
        this.categoryinfo = categoryinfo;
    }

    public List<Foodinfo> getFoodinfo() {
        return foodinfo;
    }

    public void setFoodinfo(List<Foodinfo> foodinfo) {
        this.foodinfo = foodinfo;
    }
}
