package com.malik.msd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        //giving toolbar's reference to method
        setSupportActionBar(toolbar);

        //enabling BACK button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("MSD"); //this line priority is high than this "toolbar.setTitle("MSDs");"

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //stores Id of the clicked item(BACK button is also a Menu item)
        int itemId = item.getItemId();

        if (itemId == R.id.opt_rate){
            //Toast.makeText(this, "Rate Clicked", Toast.LENGTH_SHORT).show();
            LoadFragemnt(new RateFragment());
        }
        else if (itemId == android.R.id.home){
            //"android.R.id.home" we get android default items Ids like this.
            Toast.makeText(this, "Back ", Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        }else {
            //Toast.makeText(this, "View records Clicked", Toast.LENGTH_SHORT).show();
            LoadFragemnt(new RecodsFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    public void LoadFragemnt(Fragment _fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, _fragment);
        ft.commit();
    }
}