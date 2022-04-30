package com.example.foodlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateFoodActivity extends AppCompatActivity {

    GlobalData globalData;
    DatabaseHelper dbHelper;

    EditText weightET; //grams
    Button updateButton;
    TextView foodNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        //init globalData
        globalData = GlobalData.getInstance();

        //init database helper
        dbHelper = new DatabaseHelper(this);

        //init UI elements
        weightET = (EditText) findViewById(R.id.editTextUpdateFoodWeight);

        foodNameTV = (TextView) findViewById(R.id.textViewUpdateFoodName);
        foodNameTV.setText(globalData.mDiarySelectedFoodEntry.mFood.mName);

        updateButton = (Button) findViewById(R.id.updateFoodEntryButton);
        updateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                float weight = Float.parseFloat(weightET.getText().toString());
                if(weight <= 0){
                    return;
                }
                dbHelper.updateFoodEntryById(
                        globalData.mDiarySelectedFoodEntry.mId,
                        weight);
            }
        });
    }
}