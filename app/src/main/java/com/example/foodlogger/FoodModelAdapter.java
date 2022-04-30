package com.example.foodlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class FoodModelAdapter extends ArrayAdapter<FoodModel> {

    public FoodModelAdapter(@NonNull Context context,  @NonNull List<FoodModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position
        FoodModel food = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }
        TextView foodName = (TextView) convertView.findViewById(R.id.textViewFoodItemName);
        foodName.setText(food.mName);
        return convertView;
    }
}
