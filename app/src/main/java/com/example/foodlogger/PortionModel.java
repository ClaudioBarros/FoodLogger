package com.example.foodlogger;

public class PortionModel {
    public long mFoodId;
    public String mDescription;
    public float mAmount;
    public float mWeightInGrams;

    public PortionModel(long foodId, String description, float amount, float weightInGrams){
        mFoodId = foodId;
        mDescription = description;
        mAmount = amount;
        mWeightInGrams = weightInGrams;
    }

    public PortionModel(){
        mFoodId = 0;
        mDescription = "";
        mAmount = 0.0f;
        mWeightInGrams = 0.0f;
    }
}
