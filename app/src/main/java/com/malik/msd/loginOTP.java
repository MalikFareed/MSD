package com.malik.msd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginOTP extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsernameLoginOTP, etPhoneNumberLoginOTP, etOTPLoginOTP;
    private Button btnNextLoginOTP, btnVerifyLoginOTP;

    FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        btnNextLoginOTP.setOnClickListener(this);
        btnVerifyLoginOTP.setOnClickListener(this);
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
                if (!TextUtils.isEmpty(etOTPLoginOTP.getText().toString() )) {
                    Toast.makeText(this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(etOTPLoginOTP.getText().toString());
                }

                break;
        }
    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+92" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null){
                verifyCode(code);
//                Intent i = new Intent(loginOTP.this, ReceiveOTP.class);
//                i.putExtra("username", etUsernameLoginOTP.getText().toString().replace(" ", ""));
//                i.putExtra("phoneNumber", etPhoneNumberLoginOTP.getText().toString().replace(" ", ""));
//                startActivity(i);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(loginOTP.this, "Verification failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
        }
    };

    private void verifyCode(String _code) {
        //matching code sent by firebase with the user's code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, _code);
        signinByCredentials(credential);
    }

    private void signinByCredentials(PhoneAuthCredential _credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(_credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(loginOTP.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}