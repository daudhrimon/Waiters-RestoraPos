package com.restorapos.waiters.offlineDb;

import androidx.room.TypeConverter;

import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class VarientConverters {
    @TypeConverter
    public static List<Varientlist> fromvariant(String value) {
        Type listType = new TypeToken<List<Varientlist>>() {
        }.getType();
        List<Varientlist> notifications = new Gson().fromJson(value, listType);
        return notifications;
    }

    @TypeConverter
    public static String listToVariant(List<Varientlist> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
