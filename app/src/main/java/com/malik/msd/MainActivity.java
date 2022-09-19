package com.malik.msd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        //giving our toolbar's reference to use in app
        setSupportActionBar(toolbar);
        //getting our toolbar to perform actions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enabling BACK button

        toolbar.setTitle("MSD");
    }
}