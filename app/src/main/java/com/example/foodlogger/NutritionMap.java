package com.example.foodlogger;

import java.util.HashMap;

public class NutritionMap extends HashMap<String, Float> {

    public NutritionMap(){
        String[] fieldList = Constants.nutritionalInfoFields;

        for(int i = 0; i < fieldList.length; i++){
            put(fieldList[i], 0.0f);
        }
    }
}
