package com.restorapos.waiters.model.foodlistModel2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data2 {
    @SerializedName("PcategoryID")
    @Expose
    private String pcategoryID;
    @SerializedName("Restaurantvat")
    @Expose
    private String restaurantvat;
    @SerializedName("categoryinfo")
    @Expose
    private List<Categoryinfo2> categoryinfo = null;
    @SerializedName("foodinfo")
    @Expose
    private List<Foodinfo2> foodinfo = null;

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

    public List<Categoryinfo2> getCategoryinfo() {
        return categoryinfo;
    }

    public void setCategoryinfo(List<Categoryinfo2> categoryinfo) {
        this.categoryinfo = categoryinfo;
    }

    public List<Foodinfo2> getFoodinfo() {
        return foodinfo;
    }

    public void setFoodinfo(List<Foodinfo2> foodinfo) {
        this.foodinfo = foodinfo;
    }

}
