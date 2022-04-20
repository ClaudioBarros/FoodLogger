package com.example.foodlogger;

import java.util.Map;

public class FoodModel {
    public String mName;
    public String mDescription;
    public NutritionMap mNutritionMap;
    public Map<String, Float> mPortionMap; //<portion name, portion amount in grams>

    public FoodModel(String name, String description, NutritionMap nutritionMap, Map<String, Float> portionMap){
        mName = name;
        mDescription = description;
        mNutritionMap = nutritionMap;
        mPortionMap = portionMap;
    }
}
