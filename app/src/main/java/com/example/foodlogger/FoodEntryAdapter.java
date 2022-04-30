package com.example.foodlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class FoodEntryAdapter extends ArrayAdapter<FoodEntryModel> {

    public FoodEntryAdapter(@NonNull Context context, @NonNull List<FoodEntryModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position
        FoodEntryModel foodEntry = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food_entry, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.textViewFoodEntryName);
        nameTextView.setText(foodEntry.mFood.mName);

        TextView weightTextView = (TextView) convertView.findViewById(R.id.textViewFoodWeight);
        String weightString = String.format("%.2f", foodEntry.mWeight) + " g";

        weightTextView.setText(weightString);

        return convertView;
    }
}
