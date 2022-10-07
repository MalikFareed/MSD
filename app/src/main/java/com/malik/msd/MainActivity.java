package com.malik.msd;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);

        //giving toolbar's reference to default toolbar setter method
        setSupportActionBar(toolbar);

        //enabling BACK button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("MSD"); //this line priority is high than this "toolbar.setTitle("MSDs");"

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //stores Id of the clicked item(BACK button is also a Menu item)
        int itemId = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if (itemId == R.id.opt_rate) {
            String tag = "RateFragment";
            if (fm.findFragmentByTag(tag) != null)
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //Log.d("Fragment", "Rate fragment already exist!");

            LoadFragment(new RateFragment(), tag);
        } else if (itemId == android.R.id.home) {
            //"android.R.id.home" we get android default items Ids like this.
            //Toast.makeText(this, "Back ", Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        } else if (itemId == R.id.signout) {
            if (user != null && firebaseAuth != null)
                firebaseAuth.signOut();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        } else {
            String tag = "RecordsFragment";
            if (fm.findFragmentByTag(tag) != null)
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //Log.d("Fragment", "Record fragment already exist!");

            LoadFragment(new RecordsFragment(), tag);
        }


        return super.onOptionsItemSelected(item);
    }

    public void LoadFragment(Fragment _fragment, String _tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, _fragment, _tag);
        ft.addToBackStack(_tag);
        ft.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}