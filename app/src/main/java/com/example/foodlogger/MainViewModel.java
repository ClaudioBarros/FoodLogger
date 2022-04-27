package com.example.foodlogger;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    public LocalDate mCurrentSelectedDate;

    public ArrayList<MealModel> mMealFragmentList;


}
