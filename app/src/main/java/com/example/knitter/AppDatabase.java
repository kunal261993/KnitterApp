package com.example.knitter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PostTable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao taskDao();
}