package com.restorapos.waiters.interfaces;

import android.content.Context;
import android.widget.ImageView;

import com.restorapos.waiters.model.foodlistModel.Foodinfo;

public interface FoodDialogInterface {
    void onFoodItemClick(Context context, Foodinfo foodItem, ImageView selectedMark);
}
