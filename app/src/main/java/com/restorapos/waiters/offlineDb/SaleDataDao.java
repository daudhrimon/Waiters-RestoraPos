package com.restorapos.waiters.offlineDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import java.util.List;

@Dao
public interface SaleDataDao {
    /**
     * Unit List
     **/
    @Insert
    void insertFood(Foodinfo foodinfo);

    @Query("SELECT * FROM Foodinfo ORDER BY productName DESC")
    List<Foodinfo> getAllUnit();

    @Delete
    void delete(Foodinfo unitListItem);

    @Update
    void updateFood(Foodinfo unitListItem);

    @Query("DELETE FROM Foodinfo")
    void deleteFoodTable();
}
