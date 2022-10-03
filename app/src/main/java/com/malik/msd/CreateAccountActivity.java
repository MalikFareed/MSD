package com.malik.msd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private CollectionReference collectionReference = db.collection("Users");

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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //user already logged in
                } else {
                    //no user yet
                }
            }
        };

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etUsernameAcct.getText().toString())
                        && !TextUtils.isEmpty(etEmailAcct.getText().toString())
                        && !TextUtils.isEmpty(etPasswordAcct.getText().toString())) {

                    String username = etUsernameAcct.getText().toString().trim();
                    String email = etEmailAcct.getText().toString().trim();
                    String password = etPasswordAcct.getText().toString().trim();

                    createUserEmailAccount(username, email, password);


                } else {
                    Toast.makeText(CreateAccountActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void createUserEmailAccount(String _username, String _email, String _password) {
        if (!TextUtils.isEmpty(_username) && !TextUtils.isEmpty(_email)  && !TextUtils.isEmpty(_password) ) {
            createAcctProgress.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //new user created, take user to next activity
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();

                                //create User Map to add User in firestore collection(Table)
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", _username);

                                //saving to firestore database
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (Objects.requireNonNull(task).getResult().exists()){
                                                            createAcctProgress.setVisibility(View.INVISIBLE);

                                                            String name = task.getResult().getString("username");

                                                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);

                                                            intent.putExtra("username", name);
                                                            intent.putExtra("userId", currentUserId);
                                                            startActivity(intent);

                                                        }else {
                                                            createAcctProgress.setVisibility(View.INVISIBLE);

                                                        }

                                                    }
                                                });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            } else {
                                //something went wrong
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else {
            Toast.makeText(this, "Empty fields found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}