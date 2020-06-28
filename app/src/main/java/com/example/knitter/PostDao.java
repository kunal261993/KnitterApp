package com.example.knitter;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PostDao
{
    @Query("SELECT * FROM PostTable")
    List<PostTable> getPosts();

    @Delete
    void deleteAlarm(PostTable postTable);

    @Insert
    void insert(PostTable postTable);

    @Update()
    void updateAlarm(PostTable postTable);
}