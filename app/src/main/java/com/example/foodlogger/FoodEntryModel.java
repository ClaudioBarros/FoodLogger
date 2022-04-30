package com.example.foodlogger;

import java.time.LocalDate;

public class FoodEntryModel {

    public long mId;
    public FoodModel mFood;
    public float mWeight; //grams
    public LocalDate mDate;

    public FoodEntryModel(){

    }

    public FoodEntryModel(long foodEntryId, FoodModel food, float weight, LocalDate date){
        mId = foodEntryId;
        mFood = food;
        mWeight = weight;
        mDate = date;
    }

}
