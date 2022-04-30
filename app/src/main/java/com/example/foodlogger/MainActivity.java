package com.example.foodlogger;

import static androidx.navigation.Navigation.findNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    GlobalData globalData;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize main ViewModel class
        globalData = GlobalData.getInstance();

        dbHelper = new DatabaseHelper(this);

        Cursor dbCursor = dbHelper.selectAllFromFoodTable();
        //if database is empty, initialize data from USDA database
        if(dbCursor.getCount() == 0){
            //open USDA database
            USDADatabaseHelper USDADatabaseHelper = new USDADatabaseHelper(this);
            try {
                USDADatabaseHelper.createDatabase();
                USDADatabaseHelper.openDatabase();
            } catch(IOException e){
                e.printStackTrace();
                throw new Error("Error initializing USDA database:" + e.toString());
            }
            dbHelper.initializeFromUSDAData(USDADatabaseHelper);

            USDADatabaseHelper.close();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.diaryFragment, R.id.mealsFragment, R.id.userFragment).build();
        NavController navController = findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}