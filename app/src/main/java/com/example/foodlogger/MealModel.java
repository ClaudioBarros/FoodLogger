package com.example.foodlogger;

import java.util.ArrayList;

public class MealModel {

    public String mMealName;
    public String mMealDescription;
    public ArrayList<FoodLog> mFoodLogList;

    public MealModel(String mealName, String mealDescription, String foodLogList){
        mMealName = mealName;
        mMealDescription = mealDescription;
    }
}
