package com.restorapos.waiters.model.foodlistModel2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Varientlist {

    @SerializedName("multivariantid")
    @Expose
    private String multivariantid;
    @SerializedName("multivariantName")
    @Expose
    private String multivariantName;
    @SerializedName("multivariantPrice")
    @Expose
    private String multivariantPrice;

    public String getMultivariantid() {
        return multivariantid;
    }

    public void setMultivariantid(String multivariantid) {
        this.multivariantid = multivariantid;
    }

    public String getMultivariantName() {
        return multivariantName;
    }

    public void setMultivariantName(String multivariantName) {
        this.multivariantName = multivariantName;
    }

    public String getMultivariantPrice() {
        return multivariantPrice;
    }

    public void setMultivariantPrice(String multivariantPrice) {
        this.multivariantPrice = multivariantPrice;
    }
}
