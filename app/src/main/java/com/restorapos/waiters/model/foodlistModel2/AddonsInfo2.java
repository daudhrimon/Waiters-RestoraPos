package com.restorapos.waiters.model.foodlistModel2;

import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddonsInfo2 {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("addonsid")
    @Expose
    private String addonsid;
    @SerializedName("add_on_name")
    @Expose
    private String addOnName;
    @SerializedName("addonsprice")
    @Expose
    private String addonsprice;

    public int addonsquantity = 0;

    public String getAddonsid() {
        return addonsid;
    }

    public void setAddonsid(String addonsid) {
        this.addonsid = addonsid;
    }

    public String getAddOnName() {
        return addOnName;
    }

    public void setAddOnName(String addOnName) {
        this.addOnName = addOnName;
    }

    public String getAddonsprice() {
        return addonsprice;
    }

    public void setAddonsprice(String addonsprice) {
        this.addonsprice = addonsprice;
    }

}
