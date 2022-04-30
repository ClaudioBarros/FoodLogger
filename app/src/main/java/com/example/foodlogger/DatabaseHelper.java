package com.example.foodlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
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
        db.execSQL(DbContract.UserData.SQL_CREATE_TABLE);
        db.execSQL(DbContract.User_Weight.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Food.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DbContract.FoodEntry.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Meal.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Portion.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Food_Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Meal_Food.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.User_Weight.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Food.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Nutrient.SQL_DELETE_TABLE);
        db.execSQL(DbContract.FoodEntry.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Meal.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Portion.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Food_Nutrient.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Meal_Food.SQL_DELETE_TABLE);

        db.execSQL(DbContract.UserData.SQL_CREATE_TABLE);
        db.execSQL(DbContract.User_Weight.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Food.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DbContract.FoodEntry.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Meal.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Portion.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Food_Nutrient.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Meal_Food.SQL_CREATE_TABLE);
    }

    public void initializeFromUSDAData(USDADatabaseHelper usdaHelper){

        SQLiteDatabase appDb = this.getWritableDatabase();

        SQLiteDatabase usdaDb = usdaHelper.getReadableDatabase();

        //Mappings between USDA Database IDs and our database IDs
        //in the format <usdaId, appId>
        HashMap<Long, Long> foodIdMap = new HashMap<Long, Long>();
        HashMap<Long, Long> nutrientIdMap = new HashMap<Long, Long>();

        ArrayList<Long> foodIdList = new ArrayList<>();
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

            int counter = 0;
            while (cursor.moveToNext()) {
                //USDA id will only be used to retrieve other information from tables with foreign keys
                long usdaId = cursor.getLong(cursor.getColumnIndexOrThrow(idColumn));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
                String manufacName = cursor.getString(cursor.getColumnIndexOrThrow(manufacNameColumn));
                String databaseTag = "USDA";

                ContentValues values = new ContentValues();
                values.put(DbContract.Food.COLUMN_NAME_NAME, foodName);
                values.put(DbContract.Food.COLUMN_NAME_DESCRIPTION, "");
                values.put(DbContract.Food.COLUMN_NAME_MANUFAC_NAME, manufacName);
                values.put(DbContract.Food.COLUMN_NAME_SRC_DB_ID, usdaId);
                values.put(DbContract.Food.COLUMN_NAME_DATABASE_TAG, databaseTag);

                long newRowId = appDb.insert(DbContract.Food.TABLE_NAME, null, values);
                Log.i("INFO", String.format("FOOD ADDED TO DATABASE - COUNTER = %dFOOD ID = %d", counter, newRowId));
                if(newRowId < 0){
                    Log.e("DATABASE ERROR", "Error inserting item into the \"" +
                            DbContract.Food.TABLE_NAME + "\" table");
                    continue;
                }
                foodIdMap.put(usdaId, newRowId);
                foodIdList.add(usdaId);
                counter++;
                if(counter == 50){
                    break;
                }
            }
            cursor.close();
        }

        //retrieve nutrient table data from USDA Database and insert into the app's database;
        {
            String tableName = "nutrient";
            String idColumn = "id";
            String nameColumn = "name";
            String tagnameColumn = "tagname";
            String unitColumn = "units";

            String[] projection = {
                    idColumn,
                    unitColumn,
                    tagnameColumn,
                    nameColumn
            };

            Cursor cursor = usdaDb.query(tableName, projection, null, null, null, null, null);

            while (cursor.moveToNext()) {
                //USDA id will only be used to retrieve other information from tables with foreign keys
                long usdaID = cursor.getLong(cursor.getColumnIndexOrThrow(idColumn));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
                String tagname = cursor.getString(cursor.getColumnIndexOrThrow(tagnameColumn));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(unitColumn));


                ContentValues values = new ContentValues();
                values.put(DbContract.Nutrient.COLUMN_NAME_NAME, name);
                values.put(DbContract.Nutrient.COLUMN_NAME_TAGNAME, name);
                values.put(DbContract.Nutrient.COLUMN_NAME_DESCRIPTION, "");
                values.put(DbContract.Nutrient.COLUMN_NAME_UNIT, unit);

                long newRowId = appDb.insert(DbContract.Nutrient.TABLE_NAME, null, values);
                nutrientIdMap.put(usdaID, newRowId);
            }
            cursor.close();
        }

        //retrieve <food, nutrient, amount> information from the usda database
        for(int i = 0; i < 50; i++) {
            String tableName = "nutrition";
            String foodIdColumn = "food_id";
            String nutrientIdColumn = "nutrient_id";
            String amountColumn = "amount";

            String[]  projection = {
                    foodIdColumn,
                    nutrientIdColumn,
                    amountColumn
            };

            //WHERE DATE = LIKE date;
            String filter = foodIdColumn + " = ?";
            String[] filterArgs = {foodIdList.get(i).toString()};

            Cursor cursor = usdaDb.query(tableName, projection, filter, filterArgs, null, null, null);

            while (cursor.moveToNext()) {

                long usdaFoodId = cursor.getLong(cursor.getColumnIndexOrThrow(foodIdColumn));
                long usdaNutrientId = cursor.getLong(cursor.getColumnIndexOrThrow(nutrientIdColumn));
                float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(amountColumn));

                Long appFoodId = foodIdMap.get(usdaFoodId);
                Long appNutrientId = nutrientIdMap.get(usdaNutrientId);

                // add the same <food, nutrient> relationship present in the USDA Database
                // but using our database IDs as FKs
                ContentValues values = new ContentValues();
                values.put(DbContract.Food_Nutrient.COLUMN_NAME_FOOD_ID, appFoodId);
                values.put(DbContract.Food_Nutrient.COLUMN_NAME_NUTRIENT_ID, appNutrientId);
                values.put(DbContract.Food_Nutrient.COLUMN_NAME_AMOUNT, amount);

                appDb.insert(DbContract.Food_Nutrient.TABLE_NAME, null, values);
            }
            cursor.close();
        }

        //retrieve food portion information from usda database
        /*
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
                values.put(DbContract.Portion.COLUMN_NAME_FOOD_ID, appFoodId);
                values.put(DbContract.Portion.COLUMN_NAME_AMOUNT, amount);
                values.put(DbContract.Portion.COLUMN_NAME_DESCRIPTION, description);
                values.put(DbContract.Portion.COLUMN_NAME_GRAMS, weightInGrams);

                appDb.insert(DbContract.Portion.TABLE_NAME, null, values);
            }
            cursor.close();
        }
        */
    }

    public HashMap<String, Long> buildNutrientTagToIdMap(){

        SQLiteDatabase appDb = this.getWritableDatabase();

        HashMap<String, Long> map = new HashMap<String, Long>();
        //retrieve reduced nutrient list from nutrient table
        String[] projection = {
                DbContract.Nutrient._ID,
                DbContract.Nutrient.COLUMN_NAME_TAGNAME,
        };

        StringBuilder queryFilterString = new StringBuilder();
        String[] filterArgs = new String[Constants.nutritionalInfoFields.length];
        for(int i = 0; i < Constants.nutritionalInfoFields.length; i++){
            if(i != 0) {
                queryFilterString.append(" OR ");
            }
            queryFilterString.append(DbContract.Nutrient.COLUMN_NAME_TAGNAME + "= ?");
            filterArgs[i] = Constants.nutrientNameToTagnameMap.get(Constants.nutritionalInfoFields[i]);
        }

        //WHERE tagname = nutrient_tagname;
        String filter = queryFilterString.toString();

        Cursor cursor = appDb.query(
                DbContract.Nutrient.TABLE_NAME,
                projection,
                filter,
                filterArgs,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            long nutId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Nutrient._ID));
            String nutTagname = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Nutrient.COLUMN_NAME_TAGNAME));
            map.put(nutTagname, nutId);
        }
        cursor.close();

        return map;
    }

    public NutritionMap buildFoodNutritionMap(long foodId){
        SQLiteDatabase appDb = this.getWritableDatabase();

        NutritionMap nutritionMap = new NutritionMap();
        HashMap<String, Long>  nutrientTagToId = buildNutrientTagToIdMap();

        for(int i = 0; i < Constants.nutritionalInfoFields.length; i++){
            String nutrientField = Constants.nutritionalInfoFields[i];
            String nutrientTagname = Constants.nutrientNameToTagnameMap.get(nutrientField);
            if(nutrientTagname == null){
                continue;
            }

            Long nutrientId = nutrientTagToId.get(nutrientTagname);

            //retrieve nutrient amount from Food_Nutrition table
            String[] projection = {
                    DbContract.Food_Nutrient.COLUMN_NAME_AMOUNT,
            };

            //WHERE food_name LIKE %name%;
            String filter =
                    DbContract.Food_Nutrient.COLUMN_NAME_FOOD_ID + "= ? AND " +
                            DbContract.Food_Nutrient.COLUMN_NAME_NUTRIENT_ID + " = ?";
            String[] filterArgs = {
                    String.valueOf(foodId),
                    String.valueOf(nutrientId)
            };

            Cursor amountCursor = appDb.query(
                    DbContract.Food_Nutrient.TABLE_NAME,
                    projection,
                    filter,
                    filterArgs,
                    null,
                    null,
                    null
            );

            if(amountCursor.getCount() == 0){
                amountCursor.close();
                continue;
            }

            //per 100g
            float nutrientAmount = amountCursor.getFloat(amountCursor.getColumnIndexOrThrow(DbContract.Food_Nutrient.COLUMN_NAME_AMOUNT));

            if(nutritionMap.map.containsKey(nutrientField)) {
                nutritionMap.map.put(nutrientField, nutrientAmount);
            }

            amountCursor.close();
        }

        return nutritionMap;
    }

    public ArrayList<PortionModel> buildFoodPortionData(long foodId){

        SQLiteDatabase appDb = this.getWritableDatabase();

        ArrayList<PortionModel> portionList = new ArrayList<PortionModel>();

        String[] projection = {
                DbContract.Portion._ID,
                DbContract.Portion.COLUMN_NAME_DESCRIPTION,
                DbContract.Portion.COLUMN_NAME_AMOUNT,
                DbContract.Portion.COLUMN_NAME_GRAMS
        };

        //WHERE food_id = foodId;
        String filter =
                DbContract.Portion.COLUMN_NAME_FOOD_ID + "= ?";

        String[] filterArgs = {
                String.valueOf(foodId),
        };

        Cursor portionCursor = appDb.query(
                DbContract.Portion.TABLE_NAME,
                projection,
                filter,
                filterArgs,
                null,
                null,
                null
        );

        if(portionCursor.getCount() == 0){
            portionCursor.close();
            return null;
        }

        while(portionCursor.moveToNext()){
            long portionId = portionCursor.getLong(portionCursor.getColumnIndexOrThrow(DbContract.Portion._ID));
            String portionDesc = portionCursor.getString(portionCursor.getColumnIndexOrThrow(DbContract.Portion.COLUMN_NAME_DESCRIPTION));
            float portionAmount = portionCursor.getFloat(portionCursor.getColumnIndexOrThrow(DbContract.Portion.COLUMN_NAME_AMOUNT));
            float portionGrams = portionCursor.getFloat(portionCursor.getColumnIndexOrThrow(DbContract.Portion.COLUMN_NAME_GRAMS));

            portionList.add(new PortionModel(portionId, foodId, portionDesc, portionAmount, portionGrams));
        }

        return portionList;
    }

    //INSERT
    public long addFoodEntry(long foodId, float weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.FoodEntry.COLUMN_NAME_FOOD_ID, foodId);
        values.put(DbContract.FoodEntry.COLUMN_NAME_DATE, date);
        values.put(DbContract.FoodEntry.COLUMN_NAME_WEIGHT, weight);

        //returns -1 if the insert operation fails, and the ID if it succeeds
        return db.insert(DbContract.FoodEntry.TABLE_NAME, null, values);
    }

    //QUERY
    //@param projection list of which columns to get
    //@param filter parameters

    public FoodModel queryFoodById(long id){
        SQLiteDatabase appDb = this.getWritableDatabase();

        String[] projection = {
                DbContract.Food._ID,
                DbContract.Food.COLUMN_NAME_NAME,
        };

        //WHERE food_name LIKE %name%;
        String filter = DbContract.Food._ID + " = ?";
        String[] filterArgs = {Long.toString(id)};

        Cursor cursor = appDb.query(
                DbContract.Food.TABLE_NAME,
                projection,
                filter,
                filterArgs,
                null,
                null,
                null
        );

        if(cursor.moveToNext()){
            long foodId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Food._ID));
            String foodName = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Food.COLUMN_NAME_NAME));
            return new FoodModel(foodId, foodName);
        }

        return null;
    }

    public ArrayList<FoodModel> queryFoodByName(String name){
        SQLiteDatabase appDb = this.getWritableDatabase();

        ArrayList<FoodModel> foodList = new ArrayList<FoodModel>();

        String[] projection = {
                DbContract.Food._ID,
                DbContract.Food.COLUMN_NAME_NAME,
        };

        //WHERE food_name LIKE %name%;
        String filter = DbContract.Food.COLUMN_NAME_NAME + " LIKE ?";
        String[] filterArgs = {"%" + name + "%"};

        Cursor cursor = appDb.query(
                DbContract.Food.TABLE_NAME,
                projection,
                filter,
                filterArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long foodId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Food._ID));
            String foodName = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Food.COLUMN_NAME_NAME));
            foodList.add(new FoodModel(foodId, foodName));
        }
        cursor.close();

        return foodList;
    }

    public ArrayList<FoodModel> searchFoodByName(String name){
        ArrayList<FoodModel> foodList = queryFoodByName(name);
        //for each food, get nutrition and portion data
        for (FoodModel f : foodList) {
            long foodId = f.mID;

            //search food from nutrition table
            f.mNutritionMap = buildFoodNutritionMap(foodId);
        }
        return foodList;
    }

    public Cursor selectAllFromFoodTable(){
        SQLiteDatabase appDb = this.getWritableDatabase();

        String[] projection = {
                DbContract.Food._ID,
        };

        return  appDb.query(
                DbContract.Food.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    public FoodModel buildCompleteFoodModelbyId(long id){
        FoodModel food = queryFoodById(id);
        food.mNutritionMap = buildFoodNutritionMap(id);
        return food;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<FoodEntryModel> queryFoodEntriesByDate(String date) {
        SQLiteDatabase appDb = this.getWritableDatabase();

        ArrayList<FoodEntryModel> list = new ArrayList<FoodEntryModel>();

        String[] projection = {
                DbContract.FoodEntry._ID,
                DbContract.FoodEntry.COLUMN_NAME_FOOD_ID,
                DbContract.FoodEntry.COLUMN_NAME_DATE,
                DbContract.FoodEntry.COLUMN_NAME_WEIGHT
        };

        //WHERE DATE = LIKE date;
        String filter =
                DbContract.FoodEntry.COLUMN_NAME_DATE + " LIKE ?";

        String[] filterArgs = {date};

        Cursor cursor = appDb.query(
                DbContract.FoodEntry.TABLE_NAME,
                projection,
                filter,
                filterArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long foodEntryId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.FoodEntry._ID));
            long foodId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.FoodEntry.COLUMN_NAME_FOOD_ID));
            long weight = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.FoodEntry.COLUMN_NAME_WEIGHT));

            FoodModel food = queryFoodById(foodId);
            food.mNutritionMap = buildFoodNutritionMap(foodId);

            list.add(new FoodEntryModel(foodEntryId, food, weight, LocalDate.parse(date)));
        }

        cursor.close();

        return list;
    }


    //DELETE

    //returns number of rows affected in the database
    public int deleteFoodEntryById(long id) {
        SQLiteDatabase appDb = this.getWritableDatabase();
        String selection = DbContract.FoodEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        return appDb.delete(DbContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
    }

    //UPDATE

    //returns number of rows affected in the database
    public int updateFoodEntryById(long foodEntryID, float weight) {
        SQLiteDatabase appDb = this.getWritableDatabase();

        String weightStr = Float.toString(weight);
        ContentValues values = new ContentValues();
        values.put(DbContract.FoodEntry.COLUMN_NAME_WEIGHT, weightStr);

        String selection = DbContract.FoodEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(foodEntryID)};

        return appDb.update(
                DbContract.FoodEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    //QUERY

    //--------------------- FoodEntry -----------------------


 }
