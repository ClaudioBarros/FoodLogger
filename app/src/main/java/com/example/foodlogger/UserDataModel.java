package com.example.foodlogger;

public class UserDataModel {
    public String mName;
    public float mHeight;
    public int mKCalGoal;
    public int mProteinGoal;
    public int mCarbGoal;
    public int mFatGoal;

    public UserDataModel(
            String name,
            float height,
            int kCalGoal,
            int proteinGoal,
            int carbGoal,
            int fatGoal){

        mName = name;
        mHeight = height;
        mKCalGoal = kCalGoal;
        mProteinGoal = proteinGoal;
        mCarbGoal = carbGoal;
        mFatGoal = fatGoal;
    }

    public UserDataModel(){
        mName = "";
        mHeight = 0.0f;
        mKCalGoal = 0;
        mProteinGoal = 0;
        mCarbGoal = 0;
        mFatGoal = 0;
    }
}
