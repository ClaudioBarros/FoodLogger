package com.example.foodlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateOrDeleteFoodEntryActivity extends AppCompatActivity {

    GlobalData globalData;
    DatabaseHelper dbHelper;

    TextView foodNameTV;

    TextView kcalTV;
    TextView proteinTV;
    TextView carbTV;
    TextView fatTV;

    TextView kcalValueTV;
    TextView proteinValueTV;
    TextView carbValueTV;
    TextView fatValueTV;

    Button updateButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delete_food_entry);

        GlobalData globalData = GlobalData.getInstance();
        dbHelper = new DatabaseHelper(this);

        FoodModel food = globalData.mDiarySelectedFoodEntry.mFood;

        // weight must be divided by 100 since all nutrient values are relative to 100g of food
        float weight_factor = globalData.mDiarySelectedFoodEntry.mWeight / 100.0f;

        foodNameTV = (TextView) findViewById(R.id.foodEntryNameTV);
        foodNameTV.setText(food.mName);

        float kCalAmount = food.mNutritionMap.map.get("kCal") * weight_factor;
        float proteinAmount = food.mNutritionMap.map.get("Protein") * weight_factor;
        float carbAmount = food.mNutritionMap.map.get("Carbohydrates") * weight_factor;
        float fatAmount = food.mNutritionMap.map.get("Fat") * weight_factor;

        /*
        kcalTV = (TextView) findViewById(R.id.kcalTV);
        proteinTV = (TextView) findViewById(R.id.proteinTV);
        carbTV = (TextView) findViewById(R.id.carbTV);
        fatTV = (TextView) findViewById(R.id.fatTV);
         */

        kcalValueTV = (TextView) findViewById(R.id.kcalValueTV);
        kcalValueTV.setText(Float.toString(kCalAmount));

        proteinValueTV = (TextView) findViewById(R.id.proteinValueTV);
        proteinValueTV.setText(Float.toString(proteinAmount));

        carbValueTV = (TextView) findViewById(R.id.carbValueTV);
        carbValueTV.setText(Float.toString(carbAmount));

        fatValueTV = (TextView) findViewById(R.id.fatValueTV);
        fatValueTV.setText(Float.toString(fatAmount));

        updateButton = (Button) findViewById(R.id.updateEntryButton);
        updateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UpdateFoodActivity.class);
                startActivity(intent);
            }
        });


        deleteButton = (Button) findViewById(R.id.deleteEntryButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int numDeletedTables = dbHelper.deleteFoodEntryById(globalData.mDiarySelectedFoodEntry.mId);
                if(numDeletedTables == 1){
                    Toast.makeText(UpdateOrDeleteFoodEntryActivity.this,
                            "Item deleted",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(UpdateOrDeleteFoodEntryActivity.this,
                            "Error deleting item",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }
}