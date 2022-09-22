package com.malik.msd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
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
        FragmentManager fm = getSupportFragmentManager();

        if (itemId == R.id.opt_rate){
            String tag = "RateFragment";
            if (fm.findFragmentByTag(tag) != null)
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //Log.d("Fragment", "Rate fragment already exist!");

            LoadFragment(new RateFragment(), tag);
        }
        else if (itemId == android.R.id.home){
            //"android.R.id.home" we get android default items Ids like this.
            Toast.makeText(this, "Back ", Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        }else {
            String tag = "RecordsFragment";
            if (fm.findFragmentByTag(tag) != null)
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //Log.d("Fragment", "Record fragment already exist!");

            LoadFragment(new RecodsFragment(), tag);
        }


        return super.onOptionsItemSelected(item);
    }

    public void LoadFragment(Fragment _fragment, String _tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, _fragment, _tag);
        ft.addToBackStack(_tag);
        ft.commit();
        //Log.d("Fragment", "onOptionsItemSelected: "+getSupportFragmentManager().getBackStackEntryCount());
        //Log.d("Fragment ","<<<<<<<<<<<<<<<<<<<<<<<<<<<<  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");




    }

    @Override
    public void onBackPressed() {
        Log.d("Fragments List", "onOptionsItemSelected: "+getSupportFragmentManager().getBackStackEntryCount());
        super.onBackPressed();
    }
}