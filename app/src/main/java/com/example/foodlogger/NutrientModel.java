package com.example.foodlogger;

public class NutrientModel {
    public long mID;
    public String mName;
    public String mUnit;
    public String mDescription;

    public NutrientModel(long id, String name, String unit, String description) {
        mID = id;
        mName = name;
        mUnit = unit;
        mDescription = description;
    }

    public NutrientModel(){
        mID = 0;
        mName = "";
        mUnit = "";
        mDescription = "";
    }
}
