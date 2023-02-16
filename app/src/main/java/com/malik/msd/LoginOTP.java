package com.malik.msd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

import utils.MSDApi;

public class LoginOTP extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsernameLoginOTP, etPhoneNumberLoginOTP, etOTPLoginOTP;
    private Button btnNextLoginOTP, btnVerifyLoginOTP;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        etUsernameLoginOTP = findViewById(R.id.etUsernameLoginOTP);
        etPhoneNumberLoginOTP = findViewById(R.id.etPhoneNumberLoginOTP);
        etOTPLoginOTP = findViewById(R.id.etOTPLoginOTP);

        btnNextLoginOTP = findViewById(R.id.btnNextLoginOTP);
        btnVerifyLoginOTP = findViewById(R.id.btnVerifyLoginOTP);

        firebaseAuth = FirebaseAuth.getInstance();

        btnNextLoginOTP.setOnClickListener(this);
        btnVerifyLoginOTP.setOnClickListener(this);

        loadUserData();
    }

    private void loadUserData() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    currentUser = firebaseAuth.getCurrentUser();
                    String currentUserId = currentUser.getUid();

                    collectionReference
                            .whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null)
                                        return;

                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            MSDApi msdApi = MSDApi.getInstance();
                                            msdApi.setUserId(snapshot.getString("userId"));
                                            msdApi.setUsername(snapshot.getString("username"));

                                            startActivity(new Intent(LoginOTP.this, MainActivity.class));
                                            finish();

                                        }
                                    }
                                }
                            });

                } else {
                    //do nothing to stay in same activity.
                }

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextLoginOTP:
                if (!TextUtils.isEmpty(etUsernameLoginOTP.getText().toString())
                        && !TextUtils.isEmpty(etPhoneNumberLoginOTP.getText().toString())) {

                    String phoneNumber = etPhoneNumberLoginOTP.getText().toString().replace(" ", "");
                    String username = etUsernameLoginOTP.getText().toString().replace(" ", "");

                    sendVerificationCode(phoneNumber);

                } else {
                    Toast.makeText(this, "Username and Phone required", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnVerifyLoginOTP:
                if (TextUtils.isEmpty(etOTPLoginOTP.getText().toString())) {
                    Toast.makeText(this, "Enter valid OTP code", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(etOTPLoginOTP.getText().toString());
                }

                break;
        }
    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+92" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;

            etOTPLoginOTP.setEnabled(true);
            btnVerifyLoginOTP.setEnabled(true);
        }
    };

    private void verifyCode(String _code) {
        //matching code sent by firebase with the user's code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, _code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential _credential) {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(_credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginOTP.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(LoginOTP.this, MainActivity.class);
                            i.putExtra("username", etUsernameLoginOTP.getText().toString().replace(" ", ""));
                            i.putExtra("phoneNumber", etPhoneNumberLoginOTP.getText().toString().replace(" ", ""));
                            startActivity(i);

                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        if (currentUser != null) {
            startActivity(new Intent(LoginOTP.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}