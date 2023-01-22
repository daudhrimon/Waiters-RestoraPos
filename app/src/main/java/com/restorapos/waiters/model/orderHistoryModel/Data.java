package com.restorapos.waiters.model.orderHistoryModel;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("lastweekamount")
    private String lastweekamount;

    @SerializedName("lastweekorder")
    private String lastweekorder;

    @SerializedName("Overallamount")
    private String overallamount;

    @SerializedName("Overallorder")
    private String overallorder;

    public void setLastweekamount(String lastweekamount) {
        this.lastweekamount = lastweekamount;
    }

    public String getLastweekamount() {
        return lastweekamount;
    }

    public void setLastweekorder(String lastweekorder) {
        this.lastweekorder = lastweekorder;
    }

    public String getLastweekorder() {
        return lastweekorder;
    }

    public void setOverallamount(String overallamount) {
        this.overallamount = overallamount;
    }

    public String getOverallamount() {
        return overallamount;
    }

    public void setOverallorder(String overallorder) {
        this.overallorder = overallorder;
    }

    public String getOverallorder() {
        return overallorder;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "lastweekamount = '" + lastweekamount + '\'' +
                        ",lastweekorder = '" + lastweekorder + '\'' +
                        ",overallamount = '" + overallamount + '\'' +
                        ",overallorder = '" + overallorder + '\'' +
                        "}";
    }
}