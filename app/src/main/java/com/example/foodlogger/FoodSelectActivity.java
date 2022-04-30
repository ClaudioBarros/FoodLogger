package com.example.foodlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class FoodSelectActivity extends AppCompatActivity {

    GlobalData globalData;
    FoodModelAdapter mAdapter;

    EditText editTextFoodName;
    Button searchFoodButton;

    DatabaseHelper dbHelper;

    //UI
    public ListView foodListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_select);

        //initialize DB helper
        dbHelper = new DatabaseHelper(this);

        //initialize global data instance
        globalData = GlobalData.getInstance();

        //create adapter to convert the array of foods
        mAdapter = new FoodModelAdapter(this, globalData.mSelectFoodList);

        //initialize editText element
        editTextFoodName = (EditText) findViewById(R.id.edit_text_food_name);

        //attach the adapter to a listview
        foodListView = (ListView) findViewById(R.id.foodSelectListView);
        foodListView.setAdapter(mAdapter);

        //search Food Button
        searchFoodButton = (Button) this.findViewById(R.id.start_food_search_button);
        searchFoodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String foodName = editTextFoodName.getText().toString();
                if(!foodName.isEmpty()){
                    globalData.mSelectFoodList = dbHelper.queryFoodByName(foodName);
                    mAdapter.clear();
                    mAdapter = new FoodModelAdapter(FoodSelectActivity.this, globalData.mSelectFoodList);
                    foodListView.setAdapter(mAdapter);
                }
            }
        });

        //list view on click listener
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                globalData.mSelectedFood = (FoodModel) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(FoodSelectActivity.this, AddFoodEntryActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }
        });


    }


}