package com.techwonders.doday.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.techwonders.doday.model.FoodExpense;

import java.util.List;

@Dao
public interface FoodExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FoodExpense foodExpense);

    @Update
    void update(FoodExpense foodExpense);

    @Delete
    void delete(FoodExpense foodExpense);

    @Query("DELETE FROM my_food_expense")
    void deleteAllFoodExpense();

    @Query("DELETE FROM my_food_expense WHERE id=:item_id")
    void deleteFoodExpense(int item_id);

    @Query("SELECT * FROM my_food_expense WHERE status=0 ORDER BY timestamp DESC")
    LiveData<List<FoodExpense>> getAllFoodExpense();

    @Query("UPDATE my_food_expense SET status=:newStatus WHERE status=:status")
    void updateStatus(int status, int newStatus);

}
