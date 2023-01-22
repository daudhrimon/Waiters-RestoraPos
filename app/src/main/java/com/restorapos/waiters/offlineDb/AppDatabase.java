package com.restorapos.waiters.offlineDb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.foodlistModel.Varientlist;

@Database(entities = {Foodinfo.class, Addonsinfo.class, Varientlist.class}, version = 1, exportSchema = false)

@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SaleDataDao taskDao();
}
