package com.restorapos.waiters.interfaces;

import com.restorapos.waiters.model.foodlistModel.Foodinfo;

public interface SumInterface {
    void addedSum(Foodinfo foodinfo);

    void divideSum(Foodinfo foodinfo);

    void deleteSum(Foodinfo foodinfo, int pos);
}
