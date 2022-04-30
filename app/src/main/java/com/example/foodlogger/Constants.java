package com.example.foodlogger;

import java.util.HashMap;

public final class Constants {

    private Constants(){
        //restrict instantiation
    }

    public static final String[] nutritionalInfoFields = {
            "kCal",
            "Carbohydrates",
            "Protein",
            "Fat",
            "Saturated Fat",
            "Polyunsaturated Fat",
            "Monounsaturated Fat",
            "Trans Fat",
            "Cholesterol",
            "Sodium",
            "Potassium",
            "Fiber",
            "Sugars",
            "Vitamin A",
            "Vitamin C",
            "Calcium",
            "Iron",
    };

    //Maps a nutrient name to it's name on the database
    //in the format <nutrient_name, db_nutrient_name>
    public static final HashMap<String, String> nutrientNameToTagnameMap;
    static{
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("kCal", "ENERC_KCAL");
        tmp.put("Carbohydrates", "CHOCDF");
        tmp.put("Protein", "PROCNT");
        tmp.put("Fat", "FAT");
        tmp.put("Saturated Fat", "FASAT" );
        tmp.put("Polyunsaturated Fat", "FAPU" );
        tmp.put("Monounsaturated Fat", "FAMS");
        tmp.put("Trans Fat", "FATRN" );
        tmp.put("Cholesterol", "CHOLE" );
        tmp.put("Sodium", "NA");
        tmp.put("Potassium", "K");
        tmp.put("Fiber", "FIBTG" );
        tmp.put("Sugars", "SUGAR");
        tmp.put("Vitamin A", "VITA_RAE" );
        tmp.put("Vitamin C", "VITC");
        tmp.put("Calcium", "CA");
        tmp.put("Iron", "FE");

        nutrientNameToTagnameMap = tmp;
    }

    public static class MealTypes{
        public static final String MEAL_TYPE_BREAKFAST = "Breakfast";
        public static final String MEAL_TYPE_LUNCH = "Lunch";
        public static final String MEAL_TYPE_DINNER = "Dinner";
        public static final String MEAL_TYPE_OTHER = "Other";
    }


}
