package com.example.foodlogger;

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

    public static class MealTypes{
        public static final String MEAL_TYPE_BREAKFAST = "Breakfast";
        public static final String MEAL_TYPE_LUNCH = "Lunch";
        public static final String MEAL_TYPE_DINNER = "Dinner";
        public static final String MEAL_TYPE_OTHER = "Other";
    }
}
