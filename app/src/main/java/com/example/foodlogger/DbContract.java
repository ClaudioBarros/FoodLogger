package com.example.foodlogger;

import android.provider.BaseColumns;

import java.util.Map;

public class DbContract {

    private DbContract(){
       //restrict instantiation
    }

    public static class UserData implements BaseColumns{
        public static final String TABLE_NAME = "user_data";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_KCAL_GOAL = "kcal_goal";
        public static final String COLUMN_NAME_PROTEIN_GOAL = "protein_goal";
        public static final String COLUMN_NAME_CARB_GOAL = "carb_goal";
        public static final String COLUMN_NAME_FAT_GOAL= "fat_goal";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + UserData.TABLE_NAME + " (" +
                UserData._ID + " INTEGER PRIMARY KEY," +
                UserData.COLUMN_NAME_HEIGHT + " REAL," +
                UserData.COLUMN_NAME_NAME+ " TEXT," +
                UserData.COLUMN_NAME_KCAL_GOAL + " INTEGER," +
                UserData.COLUMN_NAME_PROTEIN_GOAL + " INTEGER," +
                UserData.COLUMN_NAME_CARB_GOAL + " INTEGER," +
                UserData.COLUMN_NAME_FAT_GOAL + " INTEGER)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + UserData.TABLE_NAME;
    }

    public static class User_Weight implements BaseColumns{
        public static final String TABLE_NAME = "user_weight";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_UNIT = "unit";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + User_Weight.TABLE_NAME + " (" +
                User_Weight._ID + " INTEGER PRIMARY KEY," +
                User_Weight.COLUMN_NAME_USER_ID + " INTEGER," +
                User_Weight.COLUMN_NAME_DATE_TIME + "TEXT," +
                User_Weight.COLUMN_NAME_WEIGHT + " INTEGER," +
                User_Weight.COLUMN_NAME_UNIT + " TEXT," +
                "FOREIGN KEY(" + User_Weight.COLUMN_NAME_USER_ID + ") REFERENCES " +
                        UserData.TABLE_NAME + "(" + UserData._ID + ")" + ")";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + User_Weight.TABLE_NAME;
    }

    public static class Food implements BaseColumns{
        public static final String TABLE_NAME  = "food";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_MANUFAC_NAME = "manufac_name";
        public static final String COLUMN_NAME_SRC_DB_ID = "src_db_id";
        public static final String COLUMN_NAME_DATABASE_TAG = "database_tag";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                Food._ID + " INTEGER PRIMARY KEY," +
                Food.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                Food.COLUMN_NAME_DESCRIPTION + " TEXT," +
                Food.COLUMN_NAME_MANUFAC_NAME + " TEXT," +
                Food.COLUMN_NAME_SRC_DB_ID + " INTEGER NOT NULL," +
                Food.COLUMN_NAME_DATABASE_TAG + " TEXT NOT NULL)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Food.TABLE_NAME;
    }

    public static class Nutrient implements BaseColumns{
        public static final String TABLE_NAME = "nutrient";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TAGNAME = "tagname";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                Nutrient._ID + " INTEGER PRIMARY KEY," +
                Nutrient.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                Nutrient.COLUMN_NAME_TAGNAME + " TEXT," +
                Nutrient.COLUMN_NAME_UNIT + " TEXT NOT NULL," +
                Nutrient.COLUMN_NAME_DESCRIPTION + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Nutrient.TABLE_NAME;
    }

    public static class FoodEntry implements BaseColumns{
        public static final String TABLE_NAME = "food_entry";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_WEIGHT = "weight"; //grams

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                FoodEntry._ID + " INTEGER PRIMARY KEY," +
                FoodEntry.COLUMN_NAME_FOOD_ID + " INTEGER NOT NULL," +
                FoodEntry.COLUMN_NAME_DATE + " TEXT NOT NULL," +
                FoodEntry.COLUMN_NAME_WEIGHT + " REAL NOT NULL)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;
    }

    public static class Meal implements BaseColumns{
        public static final String TABLE_NAME = "meal";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                Meal._ID+ " INTEGER PRIMARY KEY," +
                Meal.COLUMN_NAME_NAME+ " TEXT NOT NULL," +
                Meal.COLUMN_NAME_DESCRIPTION + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Meal.TABLE_NAME;
    }

    public static class Portion implements BaseColumns{
        public static final String TABLE_NAME = "portion";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_GRAMS = "grams";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                Portion._ID + " INTEGER PRIMARY KEY," +
                Portion.COLUMN_NAME_FOOD_ID + " INTEGER NOT NULL," +
                Portion.COLUMN_NAME_AMOUNT+ " REAL NOT NULL," +
                Portion.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL," +
                Portion.COLUMN_NAME_GRAMS + " REAL NOT NULL, " +
                "FOREIGN KEY(" + Portion.COLUMN_NAME_FOOD_ID + ") REFERENCES " +
                    Food.TABLE_NAME + "(" + Food._ID + ")" + ")";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Portion.TABLE_NAME;
    }

    public static class Food_Nutrient implements BaseColumns{
        public static final String TABLE_NAME = "food_nutrient";

        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_NUTRIENT_ID = "nutrient_id";
        public static final String COLUMN_NAME_AMOUNT = "amount"; //amount per 100g

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                Food_Nutrient.COLUMN_NAME_FOOD_ID + " INTEGER NOT NULL," +
                Food_Nutrient.COLUMN_NAME_NUTRIENT_ID + " INTEGER NOT NULL," +
                Food_Nutrient.COLUMN_NAME_AMOUNT + " FLOAT NOT NULL," +
                "FOREIGN KEY(" + Food_Nutrient.COLUMN_NAME_FOOD_ID + ") REFERENCES " +
                    Food.TABLE_NAME + "(" + Food._ID + ")" + "," +
                "FOREIGN KEY(" + Food_Nutrient.COLUMN_NAME_NUTRIENT_ID + ") REFERENCES " +
                    Nutrient.TABLE_NAME + "(" + Nutrient._ID + ")" + ")";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Food_Nutrient.TABLE_NAME;
    }

    public static class Meal_Food implements BaseColumns{
        public static final String TABLE_NAME = "meal_food";

        //public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_MEAL_ID = "meal_id";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_PORTION_ID = "portion_id";
        public static final String COLUMN_NAME_PORTION_AMOUNT = "portion_amount";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + Meal_Food.TABLE_NAME + " (" +
                        Meal_Food.COLUMN_NAME_MEAL_ID + " INTEGER NOT NULL," +
                        Meal_Food.COLUMN_NAME_FOOD_ID + " INTEGER NOT NULL," +
                        Meal_Food.COLUMN_NAME_PORTION_ID + " INTEGER NOT NULL," +
                        Meal_Food.COLUMN_NAME_PORTION_AMOUNT + " INTEGER NOT NULL," +
                        "FOREIGN KEY(" + Meal_Food.COLUMN_NAME_MEAL_ID + ") REFERENCES " +
                            Meal.TABLE_NAME + "(" + Meal._ID + ")" + "," +
                        "FOREIGN KEY(" + Meal_Food.COLUMN_NAME_FOOD_ID + ") REFERENCES " +
                            Food.TABLE_NAME + "(" + Food._ID + ")" + "," +
                        "FOREIGN KEY(" + Meal_Food.COLUMN_NAME_PORTION_ID + ") REFERENCES " +
                            Portion.TABLE_NAME + "(" + Portion._ID + ")" + ")";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Meal_Food.TABLE_NAME;
    }

}
