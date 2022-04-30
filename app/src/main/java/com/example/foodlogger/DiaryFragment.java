package com.example.foodlogger;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters

    GlobalData globalData;
    DatabaseHelper dbHelper;

    //Layout Elements
    //Button setDateButton;
    FoodEntryAdapter mAdapter;
    ListView foodEntryListView;



    private String mParam1;
    private String mParam2;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        globalData = GlobalData.getInstance();
        globalData.mCurrentSelectedDate = LocalDate.now();
    }

    private void displayListOfFoodEntries(View view){
        mAdapter = new FoodEntryAdapter(requireContext(), globalData.mFoodEntryList);
        foodEntryListView = (ListView) view.findViewById(R.id.foodEntryListView);
        foodEntryListView.setAdapter(mAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(requireContext());

        //initialize setDateButton
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        Button setDateButton = (Button) view.findViewById((R.id.diary_fragment_date_picker_button));
        setDateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //show date picker dialog
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        TextView selectedDateTextView = (TextView) view.findViewById(R.id.selected_date_text_view);
        if(selectedDateTextView != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
            selectedDateTextView.setText(globalData.mCurrentSelectedDate.format(formatter));
        }

        //initialize add food button
        Button addFoodButton = (Button) view.findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start FoodSelect activity
                Intent intent = new Intent(view.getContext(), FoodSelectActivity.class);
                startActivity(intent);
            }
        });

        globalData.mFoodEntryList = dbHelper.queryFoodEntriesByDate(globalData.mCurrentSelectedDate.toString());

        displayListOfFoodEntries(view);

        foodEntryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                globalData.mDiarySelectedFoodEntry = (FoodEntryModel) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(requireContext(), UpdateOrDeleteFoodEntryActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }
        });

        return view;
        // Inflate the layout for this fragment
    }
}