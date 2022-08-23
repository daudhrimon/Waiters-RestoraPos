package com.restorapos.waiters.model.appModel;

import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AppFoodInfo {
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("variantName")
    @Expose
    private String variantName;
    @SerializedName("productvat")
    @Expose
    private String productvat;
    @SerializedName("selected")
    @Expose
    private String selected;
    @SerializedName("addons")
    @Expose
    private String addons;
    @SerializedName("addonsinfo")
    @Expose
    private List<Addonsinfo> addonsinfo = null;

    public int quantity =0;
    public double total=0;
    public int addonsqty=0;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddons() {
        return addons;
    }

    public void setAddons(String addons) {
        this.addons = addons;
    }

    public List<Addonsinfo> getAddonsinfo() {
        return addonsinfo;
    }

    public void setAddonsinfo(List<Addonsinfo> addonsinfo) {
        this.addonsinfo = addonsinfo;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getProductvat() {
        return productvat;
    }

    public void setProductvat(String productvat) {
        this.productvat = productvat;
    }

    public int getAddonsqty() {
        return addonsqty;
    }

    public void setAddonsqty(int addonsqty) {
        this.addonsqty = addonsqty;
    }
}
