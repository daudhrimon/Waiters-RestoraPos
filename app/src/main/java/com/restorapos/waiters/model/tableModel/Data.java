package com.restorapos.waiters.model.tableModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("tableinfo")
    @Expose
    private List<DataItem> tableinfo = null;

    public List<DataItem> getTableinfo() {
        return tableinfo;
    }

    public void setTableinfo(List<DataItem> tableinfo) {
        this.tableinfo = tableinfo;
    }
}
