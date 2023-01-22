package com.restorapos.waiters.model.appModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppRequestBody {
    @SerializedName("Data")
    @Expose
    private List<AppFoodInfo> appFoodInfo;

    @SerializedName("VAT")
    @Expose
    private String vat;
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("TableId")
    @Expose
    private String TableId;
    @SerializedName("Total")
    @Expose
    private double Total;

    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;

    @SerializedName("TypeID")
    @Expose
    private String TypeID;

    @SerializedName("ServiceCharge")
    @Expose
    private String ServiceCharge;

    @SerializedName("Discount")
    @Expose
    private String Discount;
    @SerializedName("CustomerNote")
    @Expose
    private String CustomerNote;

    @SerializedName("Grandtotal")
    @Expose
    private double Grandtotal;

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public List<AppFoodInfo> getAppFoodInfo() {
        return appFoodInfo;
    }

    public void setAppFoodInfo(List<AppFoodInfo> appFoodInfo) {
        this.appFoodInfo = appFoodInfo;
    }

    public String getTableId() {
        return TableId;
    }

    public void setTableId(String tableId) {
        TableId = tableId;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCustomerNote() {
        return CustomerNote;
    }

    public void setCustomerNote(String customerNote) {
        CustomerNote = customerNote;
    }

    public double getGrandtotal() {
        return Grandtotal;
    }

    public void setGrandtotal(double grandtotal) {
        Grandtotal = grandtotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
