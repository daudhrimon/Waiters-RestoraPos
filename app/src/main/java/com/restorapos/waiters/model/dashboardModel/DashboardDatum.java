package com.restorapos.waiters.model.dashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDatum {
    @SerializedName("CategoryID")
    @Expose
    private String categoryID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("categoryimage")
    @Expose
    private String categoryimage;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryimage() {
        return categoryimage;
    }

    public void setCategoryimage(String categoryimage) {
        this.categoryimage = categoryimage;
    }
}
