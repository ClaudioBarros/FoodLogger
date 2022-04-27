package com.example.foodlogger;

import java.util.HashMap;
import java.util.Map;

public class FoodModel {
    public long mID;
    public String mName;
    public String mDescription;
    public String mManufacName;
    public long mSrcDatabaseId;
    public String mDatabaseTag;
    public NutritionMap mNutritionMap;
    public HashMap<String, Float> mPortionMap; //<portion name, portion amount in grams>

    public FoodModel(long id, String name, String description, long srcDatabaseId, String databaseTag, NutritionMap nutritionMap, HashMap<String, Float> portionMap){
        mID = id;
        mName = name;
        mDescription = description;
        mSrcDatabaseId = srcDatabaseId;
        mDatabaseTag = databaseTag;
        mNutritionMap = nutritionMap;
        mPortionMap = portionMap;
    }

    public FoodModel(long id, String name, String description, long srcDatabaseId, String databaseTag){
        mID = id;
        mName = name;
        mDescription = description;
        mSrcDatabaseId = srcDatabaseId;
        mDatabaseTag = databaseTag;
        mNutritionMap = new NutritionMap();
        mPortionMap = new HashMap<String, Float>();
    }

    public FoodModel(){
        mID = 0;
        mName = "";
        mDescription = "";
        mDatabaseTag = "";
        mNutritionMap = new NutritionMap();
        mPortionMap = new HashMap<String, Float>();
    }
}
