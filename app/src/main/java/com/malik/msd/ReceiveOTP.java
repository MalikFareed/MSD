package com.malik.msd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ReceiveOTP extends AppCompatActivity {

    private EditText etEnterOTPReceiveOTP;
    private Button btnVerifyOTPReceiveOTP;
    String username, phoneNumber;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_otp);

        username = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        etEnterOTPReceiveOTP = findViewById(R.id.etEnterOTPReceiveOTP);
        btnVerifyOTPReceiveOTP = findViewById(R.id.btnVerifyOTPReceiveOTP);

        mAuth = FirebaseAuth.getInstance();

        initiateOTP(phoneNumber);

    }

    private void initiateOTP(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

//       PhoneAuthProvider.verifyPhoneNumber(
//               phoneNumber,
//               60,
//               TimeUnit.SECONDS,
//               this,
//               new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                   @Override
//                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                       super.onCodeSent(s, forceResendingToken);
//                   }
//
//                   @Override
//                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                   }
//
//                   @Override
//                   public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                   }
//               }
//               );

    }
}