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

    }

    //called when the database version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //User
    //only a single user is supported at the moment

    //TODO: User functions

    //Food
    public void addFood(){

    }

    public FoodModel getFood(){

    }

    public void updateFood(){

    }

    public void deleteFood(){

    }

    //Meal
    public void addMeal(){

    }

    public MealModel getMeal(){

    }

    public void updateMeal(){

    }

    public void deleteMeal(){

    }

    //
    public void
}
