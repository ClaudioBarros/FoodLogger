package com.example.foodlogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "foodlogger.db" , null, 1);
    }

    //called on the first access to the database.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create
    }

    //called when the database version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //User
    //only a single user is supported at the moment

    //TODO: User functions

}
