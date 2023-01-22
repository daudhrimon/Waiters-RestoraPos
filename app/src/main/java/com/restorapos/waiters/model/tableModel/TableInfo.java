package com.restorapos.waiters.model.tableModel;

import com.google.gson.annotations.SerializedName;

public class TableInfo {
    @SerializedName("tableno")
    private String tableId;

    @SerializedName("tablename")
    private String tableName;
    private String capacity;
    private String available;
    private String Booked;

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getBooked() {
        return Booked;
    }

    public void setBooked(String booked) {
        Booked = booked;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "tableId = '" + tableId + '\'' +
                        ",tableName = '" + tableName + '\'' +
                        "}";
    }
}
