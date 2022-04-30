package com.example.foodlogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodModel {
    public long mID;
    public String mName;
    public String mDescription;
    public NutritionMap mNutritionMap;
    //public ArrayList<PortionModel> mPortions;

    public FoodModel(
            long id,
            String name,
            String description,
            NutritionMap nutritionMap
    ){
        mID = id;
        mName = name;
        mDescription = description;
        mNutritionMap = nutritionMap;
    }

    public FoodModel(long id, String name){
        mID = id;
        mName = name;
        mNutritionMap = new NutritionMap();
    }

    public FoodModel(long id, String name, String description){
        mID = id;
        mName = name;
        mDescription = description;
        mNutritionMap = new NutritionMap();
    }

    public FoodModel(){
        mID = 0;
        mName = "";
        mDescription = "";
        mNutritionMap = new NutritionMap();
    }

}
