package com.malik.msd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Entry;
import utils.MSDApi;

public class RateFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RateFragmentActivity";

    private DatePicker dp;
    private RadioButton rbMorning, rbEvening;
    private EditText etRate, etMann, etSair;
    private TextView tvAmount;
    private Button btnAdd, btnUpload;
    ProgressBar fragRateProgress;

    private List<Entry> entryList;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //connection to Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Records");

    public RateFragment() {
        // Required empty public constructor
    }

    public static RateFragment newInstance(String param1, String param2) {
        RateFragment fragment = new RateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        entryList = new ArrayList<Entry>();

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rate, container, false);

        dp = rootView.findViewById(R.id.datePicker);
        rbMorning = rootView.findViewById(R.id.rbMorning);
        rbEvening = rootView.findViewById(R.id.rbEvening);
        etRate = rootView.findViewById(R.id.etRate);
        etMann = rootView.findViewById(R.id.etMann);
        etSair = rootView.findViewById(R.id.etSair);
        tvAmount = rootView.findViewById(R.id.tvAmount);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        btnUpload = rootView.findViewById(R.id.btnUpload);
        fragRateProgress = rootView.findViewById(R.id.fragRateProgress);

        etSair.setFilters(new InputFilter[]{new InputFilterMinMax("0", "39")});

        Main();

        return rootView;
    }

    private void Main() {

        toggleUploadButton();

        btnAdd.setOnClickListener(this);

        btnUpload.setOnClickListener(this);

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {    }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rate = etRate.getText().toString();
                String mann = etMann.getText().toString();
                String sair = etSair.getText().toString();
                if (rate.matches("") || mann.matches("") || sair.matches("")) {
                    tvAmount.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strRate = editable.toString();
                if (strRate == null || strRate.equals("")) {
                    strRate = "0";
                }
                double rate = Double.parseDouble(strRate);

                try {
                    double amount = GetAmount(rate, Double.parseDouble(etMann.getText().toString()), Double.parseDouble(etSair.getText().toString()));
                    tvAmount.setText(String.valueOf(amount));
                } catch (Exception e) {
                    return;
                }


            }
        });

        etMann.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rate = etRate.getText().toString();
                String mann = etMann.getText().toString();
                String sair = etSair.getText().toString();
                if (rate.matches("") || mann.matches("") || sair.matches("")) {
                    tvAmount.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strMann = editable.toString();

                if (strMann == null || strMann.equals("")) {
                    strMann = "0";
                }
                double mann = Double.parseDouble(strMann);

                try {
                    double amount = GetAmount(Double.parseDouble(etRate.getText().toString()), mann, Double.parseDouble(etSair.getText().toString()));
                    tvAmount.setText(String.valueOf(amount));
                } catch (Exception e) {
                    return;
                }
            }

        });

        etSair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rate = etRate.getText().toString();
                String mann = etMann.getText().toString();
                String sair = etSair.getText().toString();
                if (rate.matches("") || mann.matches("") || sair.matches("")) {
                    tvAmount.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strSair = editable.toString();

                if (strSair == null || strSair.equals("")) {
                    strSair = "0";
                }
                double sair = Double.parseDouble(strSair);

                try {
                    double amount = GetAmount(Double.parseDouble(etRate.getText().toString()), Double.parseDouble(etMann.getText().toString()), sair);
                    tvAmount.setText(String.valueOf(amount));
                } catch (Exception e) {
                    return;
                }
            }

        });
    }

    private String GetFormattedDate(String _date) throws ParseException {
        String stringDate = String.valueOf(_date);
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        String pattern = "dd/MM/yyyy";
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);

    }

    private boolean AddEntry(String _date, String _time, double _rate, double _mann, double _sair, double _amount,
                             String _userId, Timestamp _timeAdded, String _username) {

        return entryList.add(new Entry(_date, _time, _rate, _mann, _sair, _amount, _userId, _timeAdded, _username));
    }

    private void ResetViews() {

        etRate.setText(null);
        etMann.setText(null);
        etSair.setText(null);

        if (rbEvening.isChecked()) {
            rbMorning.setChecked(true);
            dp.updateDate(dp.getYear(), dp.getMonth(), dp.getDayOfMonth() + 1);
        } else
            rbEvening.setChecked(true);

        etRate.requestFocus();
    }

    private double GetAmount(double _rate, double _mann, double _sair) {
        double kgAmount = _rate / 40;
        double totalKg = (_mann * 40) + _sair;

        return kgAmount * totalKg;

    }

    private void ShowAlertDialog() {
        if (entryList != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
            alert.setTitle("Message");

            String message = entryList.size() + " new entry(s) will be uploaded";
            alert.setMessage(message);

            alert.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fragRateProgress.setVisibility(View.VISIBLE);

                    boolean uploaded = uploadData(entryList);
                    if (uploaded) {
                        entryList.clear();
                        toggleUploadButton();
                        Toast.makeText(getActivity(), counter + " File(s) uploaded successfully", Toast.LENGTH_SHORT).show();
                        counter = 0;
                    } else {
                        Toast.makeText(getActivity(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.setNegativeButton("Cancel", null);

            alert.setNeutralButton("View", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

            alert.create().show();
        } else {
            Toast.makeText(this.getContext(), "No entry found!", Toast.LENGTH_SHORT).show();
        }
    }

    int counter = 0;

    private boolean uploadData(List<Entry> entryList) {

        for (Entry entry : entryList) {
            collectionReference.add(entry)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            counter += 1;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                            Toast.makeText(getActivity(), "failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        fragRateProgress.setVisibility(View.INVISIBLE);
        return true;
    }

    private void toggleUploadButton() {
        if (entryList.size() > 0) {
            btnUpload.setEnabled(true);
        } else
            btnUpload.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                try {
                    double rate, mann, sair, amount;
                    if (!(etRate.getText().toString().matches("") || etMann.getText().toString().matches("") || etSair.getText().toString().matches(""))) {
                        rate = Double.parseDouble(etRate.getText().toString());
                        mann = Double.parseDouble(etMann.getText().toString());
                        sair = Double.parseDouble(etSair.getText().toString());
                        amount = Double.parseDouble(tvAmount.getText().toString());

                    } else {
                        Toast.makeText(getActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String date = GetFormattedDate(String.valueOf(dp.getDayOfMonth()) + "/" + String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear()));

                    String time = "Morning";
                    if (rbEvening.isChecked())
                        time = "Evening";


                    if (AddEntry(date, time, rate, mann, sair, amount, currentUserId, new Timestamp(new Date()), currentUserName)) {
                        Toast.makeText(getActivity(), "Successfully added", Toast.LENGTH_SHORT).show();
                        toggleUploadButton();
                        ResetViews();
                    } else {
                        Toast.makeText(getActivity(), "Failed to add entry", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Invalid values found!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnUpload:
                ShowAlertDialog();
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        MSDApi msdApi = MSDApi.getInstance();

        user = firebaseAuth.getCurrentUser();

        currentUserName = msdApi.getUsername();
        currentUserId = msdApi.getUserId();

        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}