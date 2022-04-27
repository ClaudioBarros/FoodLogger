package com.example.foodlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "foodlogger.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called on the first access to the database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.UserData.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.User_Weight.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Food.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.FoodEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Meal.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Portion.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Food_Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Meal_Food.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void initializeFromUSDAData(USDADatabaseHelper usdaHelper){
        SQLiteDatabase usdaDb = usdaHelper.getReadableDatabase();
        SQLiteDatabase appDb = this.getWritableDatabase();


        //Mappings between USDA Database IDs and our database IDs
        //in the format <usdaId, appId>
        HashMap<Long, Long> foodIdMap = new HashMap<Long, Long>();
        HashMap<Long, Long> nutrientIdMap = new HashMap<Long, Long>();

        //retrieve food table data from USDA Database and insert into the app's Database
        {

            String tableName = "food";
            String idColumn = "id";
            String nameColumn = "long_desc";
            String manufacNameColumn = "manufac_name";

            String[] projection = {
                    idColumn,
                    nameColumn,
                    manufacNameColumn
            };

            Cursor cursor = usdaDb.query(tableName, projection, null, null, null, null, null);

            while (cursor.moveToNext()) {
                //USDA id will only be used to retrieve other information from tables with foreign keys
                long usdaId = cursor.getLong(cursor.getColumnIndexOrThrow(idColumn));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
                String manufacName = cursor.getString(cursor.getColumnIndexOrThrow(manufacNameColumn));
                String databaseTag = "USDA";

                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Food.COLUMN_NAME_NAME, foodName);
                values.put(DatabaseContract.Food.COLUMN_NAME_DESCRIPTION, "");
                values.put(DatabaseContract.Food.COLUMN_NAME_MANUFAC_NAME, manufacName);
                values.put(DatabaseContract.Food.COLUMN_NAME_SRC_DB_ID, usdaId);
                values.put(DatabaseContract.Food.COLUMN_NAME_DATABASE_TAG, databaseTag);

                long newRowId = appDb.insert(DatabaseContract.Food.TABLE_NAME, null, values);
                if(newRowId < 0){
                    Log.e("DATABASE ERROR", "Error inserting item into the \"" +
                            DatabaseContract.Food.TABLE_NAME + "\" table");
                    continue;
                }
                foodIdMap.put(usdaId, newRowId);
            }
            cursor.close();
        }

        //retrieve nutrient table data from USDA Database and insert into the app's database;
        {
            String tableName = "nutrient";
            String idColumn = "id";
            String nameColumn = "name";
            String unitColumn = "units";

            String[] projection = {
                    idColumn,
                    unitColumn,
                    nameColumn
            };

            Cursor cursor = usdaDb.query(tableName, projection, null, null, null, null, null);

            while (cursor.moveToNext()) {
                //USDA id will only be used to retrieve other information from tables with foreign keys
                long usdaID = cursor.getLong(cursor.getColumnIndexOrThrow(idColumn));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(unitColumn));

                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Nutrient.COLUMN_NAME_NAME, name);
                values.put(DatabaseContract.Nutrient.COLUMN_NAME_DESCRIPTION, "");
                values.put(DatabaseContract.Nutrient.COLUMN_NAME_UNIT, unit);

                long newRowId = appDb.insert(DatabaseContract.Nutrient.TABLE_NAME, null, values);
                nutrientIdMap.put(usdaID, newRowId);
            }
            cursor.close();
        }

        //retrieve <food, nutrient, amount> information from the usda database
        {
            String tableName = "nutrition";
            String foodIdColumn = "food_id";
            String nutrientIdColumn = "nutrient_id";
            String amountColumn = "amount";

            String[]  projection = {
                    foodIdColumn,
                    nutrientIdColumn,
                    amountColumn
            };

            Cursor cursor = usdaDb.query(tableName, projection, null, null, null, null, null);

            while (cursor.moveToNext()) {

                long usdaFoodId = cursor.getLong(cursor.getColumnIndexOrThrow(foodIdColumn));
                long usdaNutrientId = cursor.getLong(cursor.getColumnIndexOrThrow(nutrientIdColumn));
                float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(amountColumn));

                long appFoodId = foodIdMap.get(usdaFoodId);
                long appNutrientId = nutrientIdMap.get(usdaNutrientId);

                // add the same <food, nutrient> relationship present in the USDA Database
                // but using our database IDs as FKs
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Food_Nutrient.COLUMN_NAME_FOOD_ID, appFoodId);
                values.put(DatabaseContract.Food_Nutrient.COLUMN_NAME_NUTRIENT_ID, appNutrientId);
                values.put(DatabaseContract.Food_Nutrient.COLUMN_NAME_AMOUNT, amount);

                appDb.insert(DatabaseContract.Food_Nutrient.TABLE_NAME, null, values);
            }
            cursor.close();
        }

        //retrieve food portion information from usda database
        {
            String tableName = "weight";
            String foodIdColumn = "food_id";
            String descriptionColumn = "description";
            String amountColumn = "amount";
            String weightInGramsColumn = "gm_weight";

            String[]  projection = {
                    foodIdColumn,
                    descriptionColumn,
                    amountColumn,
                    weightInGramsColumn
            };

            Cursor cursor = usdaDb.query(tableName, projection, null, null, null, null, null);

            while (cursor.moveToNext()) {
                long usdaFoodId = cursor.getLong(cursor.getColumnIndexOrThrow(foodIdColumn));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(descriptionColumn));
                float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(amountColumn));
                float weightInGrams = cursor.getFloat(cursor.getColumnIndexOrThrow(weightInGramsColumn));

                long appFoodId = foodIdMap.get(usdaFoodId);
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Portion.COLUMN_NAME_FOOD_ID, appFoodId);
                values.put(DatabaseContract.Portion.COLUMN_NAME_AMOUNT, amount);
                values.put(DatabaseContract.Portion.COLUMN_NAME_DESCRIPTION, description);
                values.put(DatabaseContract.Portion.COLUMN_NAME_GRAMS, weightInGrams);

                appDb.insert(DatabaseContract.Portion.TABLE_NAME, null, values);
            }
            cursor.close();

        }
    }

    // ----- UserData ------

    //INSERT
    public long insertIntoUserData(SQLiteOpenHelper dbHelper, String name, String height, String kcalGoal,
                                   String proteinGoal, String carbGoal, String fatGoal ) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserData.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.UserData.COLUMN_NAME_HEIGHT, height);
        values.put(DatabaseContract.UserData.COLUMN_NAME_KCAL_GOAL, kcalGoal);
        values.put(DatabaseContract.UserData.COLUMN_NAME_PROTEIN_GOAL, proteinGoal);
        values.put(DatabaseContract.UserData.COLUMN_NAME_CARB_GOAL, carbGoal);
        values.put(DatabaseContract.UserData.COLUMN_NAME_FAT_GOAL, fatGoal);

        //returns -1 if the insert operation fails, and the ID if it succeeds
        return db.insert(DatabaseContract.UserData.TABLE_NAME, null, values);
    }

    //QUERY
    //@param projection list of which columns to get
    //@param filter parameters

    //DELETE

    //UPDATE

    //QUERY

    //--------------------- FoodEntry -----------------------


 }
