package com.example.todoList;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;


@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);


    @Query("UPDATE item_table SET name=:name,description=:description,date=:date,image=:image WHERE id = :id")
    void update(int id, String name,String description, Date date, byte [] image);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item_table")
    void deleteAllItems();

    @Query("SELECT * FROM item_table ORDER BY date DESC ")
    List<Item> getAllItems();



}
