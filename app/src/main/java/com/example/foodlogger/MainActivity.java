package com.example.foodlogger;

import static androidx.navigation.Navigation.findNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    MainViewModel mModel;

    USDADatabaseHelper mUSDADatabaseHelper;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize main ViewModel class
        mModel = new ViewModelProvider(this).get(MainViewModel.class);

        //open USDA database
        mUSDADatabaseHelper = new USDADatabaseHelper(this);
        try {
            mUSDADatabaseHelper.createDatabase();
            mUSDADatabaseHelper.openDatabase();
        } catch(IOException e){
            e.printStackTrace();
            throw new Error("Error initializing USDA database:" + e.toString());
        }

        //open app database and initialize with USDA data
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.initializeFromUSDAData(mUSDADatabaseHelper);
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