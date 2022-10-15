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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginOTP extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsernameLoginOTP, etPhoneNumberLoginOTP;
    private Button btnNextLoginOTP;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mAuth = FirebaseAuth.getInstance();

        btnNextLoginOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextLoginOTP:
                if (!TextUtils.isEmpty(etUsernameLoginOTP.getText().toString())
                        && !TextUtils.isEmpty(etPhoneNumberLoginOTP.getText().toString())) {

                    String phoneNumber = etPhoneNumberLoginOTP.getText().toString().replace(" ", "");
                    String username = etUsernameLoginOTP.getText().toString().replace(" ", "");

                    sendVerificationCode(phoneNumber, username);



                } else {
                    Toast.makeText(this, "Username and Phone required", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void sendVerificationCode(String phoneNumber, String username) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
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
            if (code != null){
                Intent i = new Intent(loginOTP.this, ReceiveOTP.class);
                i.putExtra("username", username);
                i.putExtra("phoneNumber", phoneNumber);
                startActivity(i);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(loginOTP.this, "Verification failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };
}