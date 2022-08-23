package com.restorapos.waiters.model.viewOrderModel;

import com.google.gson.annotations.SerializedName;

public class Addonsinfo {

    @SerializedName("addonsName")
    private String addonsName;
    @SerializedName("add_on_id")
    private String addOnId;
    @SerializedName("price")
    private String price;
    @SerializedName("add_on_qty")
    private String addOnQty;

    public String getAddonsName() {
        return addonsName;
    }

    public void setAddonsName(String addonsName) {
        this.addonsName = addonsName;
    }

    public String getAddOnId() {
        return addOnId;
    }

    public void setAddOnId(String addOnId) {
        this.addOnId = addOnId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddOnQty() {
        return addOnQty;
    }

    public void setAddOnQty(String addOnQty) {
        this.addOnQty = addOnQty;
    }

}
