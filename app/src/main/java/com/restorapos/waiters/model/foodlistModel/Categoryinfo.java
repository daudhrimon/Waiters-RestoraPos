package com.restorapos.waiters.model.foodlistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Categoryinfo implements Serializable {
    @SerializedName("CategoryID")
    @Expose
    private String categoryID;
    @SerializedName("Name")
    @Expose
    private String name;

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
}
