package com.example.foodlogger;

public class FoodNutritionModel {
    public long mFoodId;
    public long mNutrientId;
    public float mAmount;

    public FoodNutritionModel(long foodId, long nutrientId, float amount){
        mFoodId = foodId;
        mNutrientId = nutrientId;
        mAmount = amount;
    }
}
