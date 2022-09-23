package com.malik.msd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;


public class RateFragment extends Fragment {

    DatePicker dp;
    RadioButton rb;

    public RateFragment() {
        // Required empty public constructor
    }

    public static RateFragment newInstance(String param1, String param2) {
        RateFragment fragment = new RateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_rate, container, false);

        dp = rootView.findViewById(R.id.datePicker);
        rb = rootView.findViewById(R.id.rbEvening);

        String date = String.valueOf(dp.getDayOfMonth())+ "/" + String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear());
        Log.d("Day", date);


        return rootView;
    }

}