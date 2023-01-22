package com.restorapos.waiters.offlineDb;

import androidx.room.TypeConverter;

import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Addonsinfo> fromString(String value) {
        Type listType = new TypeToken<List<Addonsinfo>>() {
        }.getType();
        List<Addonsinfo> notifications = new Gson().fromJson(value, listType);
        return notifications;
    }

    @TypeConverter
    public static String listToString(List<Addonsinfo> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Varientlist> fromvariant(String value) {
        Type listType = new TypeToken<List<Addonsinfo>>() {
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
