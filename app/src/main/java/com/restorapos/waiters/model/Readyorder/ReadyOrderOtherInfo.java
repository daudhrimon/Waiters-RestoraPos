package com.restorapos.waiters.model.Readyorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReadyOrderOtherInfo {
    @SerializedName("totalorder")
    @Expose
    private Integer totalorder;
    @SerializedName("orderinfo")
    @Expose
    private List<ReadyorderData> readyOrderData = null;

    public Integer getTotalorder() {
        return totalorder;
    }

    public void setTotalorder(Integer totalorder) {
        this.totalorder = totalorder;
    }

    public List<ReadyorderData> getReadyOrderData() {
        return readyOrderData;
    }

    public void setReadyOrderData(List<ReadyorderData> readyOrderData) {
        this.readyOrderData = readyOrderData;
    }
}
