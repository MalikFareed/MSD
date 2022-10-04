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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //connection to Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Records");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        //giving toolbar's reference to default toolbar setter method
        setSupportActionBar(toolbar);

        //enabling BACK button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("MSD"); //this line priority is high than this "toolbar.setTitle("MSDs");"

        }

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){

                }else {

                }

            }
        };
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}