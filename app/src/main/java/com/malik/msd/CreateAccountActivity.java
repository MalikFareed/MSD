package com.malik.msd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsernameAcct;
    private EditText etEmailAcct;
    private EditText etPasswordAcct;
    private Button btnCreateAccount;
    private ProgressBar createAcctProgress;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();

        etUsernameAcct = findViewById(R.id.etUsernameAcct);
        etEmailAcct = findViewById(R.id.etEmailAcct);
        etPasswordAcct = findViewById(R.id.etPasswordAcct);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        createAcctProgress = findViewById(R.id.createAcctProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}