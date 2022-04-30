package com.example.foodlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddFoodEntryActivity extends AppCompatActivity {

    GlobalData globalData;
    DatabaseHelper dbHelper;

    EditText weightEditText; //grams
    Button addFoodEntryButton;
    TextView addFoodNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_entry);

        //init globalData
        globalData = GlobalData.getInstance();

        //init database helper
        dbHelper = new DatabaseHelper(this);

        //init UI elements
        weightEditText = (EditText) findViewById(R.id.editTextFoodWeight);

        addFoodNameTextView = (TextView) findViewById(R.id.textViewAddFoodName);
        addFoodNameTextView.setText(globalData.mSelectedFood.mName);

        addFoodEntryButton = (Button) findViewById(R.id.addFoodEntryButton);
        addFoodEntryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                float weight = Float.parseFloat(weightEditText.getText().toString());
                if(weight <= 0){
                    return;
                }
                dbHelper.addFoodEntry(globalData.mSelectedFood.mID, weight, globalData.mCurrentSelectedDate.toString());
            }
        });

    }
}