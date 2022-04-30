package com.example.foodlogger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class GlobalData {

    //date picker fragment
    public LocalDate mCurrentSelectedDate;
    //select food screen selected food
    FoodModel mSelectedFood;

    //diary food entry selected food
    FoodEntryModel mDiarySelectedFoodEntry;

    //Arrays
    ArrayList<FoodModel> mSelectFoodList = new ArrayList<FoodModel>();
    ArrayList<FoodEntryModel> mFoodEntryList = new ArrayList<FoodEntryModel>();

    private static final GlobalData data = new GlobalData();

    public static GlobalData getInstance(){return data;}

}
