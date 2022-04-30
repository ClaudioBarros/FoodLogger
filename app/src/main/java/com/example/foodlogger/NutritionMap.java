package com.example.foodlogger;

import java.util.HashMap;

public class NutritionMap{

    HashMap<String, Float> map = new HashMap<String, Float>();

    {
        String[] fieldList = Constants.nutritionalInfoFields;
        for(int i = 0; i < fieldList.length; i++){
            map.put(fieldList[i], 0.0f);
        }
    }

    public NutritionMap(){

    }

}
