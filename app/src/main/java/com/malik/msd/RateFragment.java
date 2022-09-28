package com.malik.msd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RateFragment extends Fragment {

    DatePicker dp;
    RadioButton rbMorning, rbEvening;
    EditText etRate, etMann, etSair;
    TextView tvAmount;
    Button btnSave;

    List<Record> records;

    FragmentManager fragment;

    public RateFragment() {
        // Required empty public constructor
    }

    public static RateFragment newInstance(String param1, String param2) {
        RateFragment fragment = new RateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        records = new ArrayList<Record>();

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)    {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_rate, container, false);

        dp = rootView.findViewById(R.id.datePicker);
        rbMorning = rootView.findViewById(R.id.rbMorning);
        rbEvening = rootView.findViewById(R.id.rbEvening);
        etRate = rootView.findViewById(R.id.etRate);
        etMann = rootView.findViewById(R.id.etMann);
        etSair = rootView.findViewById(R.id.etSair);
        tvAmount = rootView.findViewById(R.id.tvAmount);
        btnSave = rootView.findViewById(R.id.btnSave);

        init();

        return rootView;
    }

    private void init(){

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SaveRecord();

                } catch (Exception e) {
                    Toast.makeText(getActivity(),"!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String strRate = editable.toString();
                if (strRate == null || strRate.equals("")){
                    strRate = "0";
                }
                double rate = Integer.parseInt(strRate);

                try {
                    double amount = GetAmount(rate, Double.parseDouble(etMann.getText().toString()), Double.parseDouble(etSair.getText().toString()));
                    tvAmount.setText("Amount: " + amount);
                }
                catch (Exception e){ return; }


            }
        });

        etMann.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String strMann = editable.toString();

                if (strMann == null || strMann.equals("")){
                    strMann = "0";
                }
                double mann = Integer.parseInt(strMann);

                try {
                    double amount = GetAmount(Double.parseDouble(etRate.getText().toString()),mann, Double.parseDouble(etSair.getText().toString()));
                    tvAmount.setText("Amount: " + amount);
                }
                catch (Exception e){ return; }
            }

        });

        etSair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String strSair = editable.toString();

                if (strSair == null || strSair.equals("")){
                    strSair = "0";
                }
                double sair = Integer.parseInt(strSair);

                try {
                    double amount = GetAmount(Double.parseDouble(etRate.getText().toString()),Double.parseDouble(etMann.getText().toString()), sair);
                    tvAmount.setText("Amount: " + amount);
                }
                catch (Exception e){ return; }
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

    private void SaveRecord() throws ParseException {
        String date = GetFormattedDate(String.valueOf(dp.getDayOfMonth())+ "/" + String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear()));
        String  time = "Morning";
        if (rbEvening.isChecked())
            time = "Evening";

        try {
            if (etRate != null && etMann != null && etSair != null){
                records.add(new Record(
                        date,
                        time,
                        Double.parseDouble(etRate.getText().toString()),
                        Double.parseDouble(etMann.getText().toString()),
                        Double.parseDouble(etSair.getText().toString())
                ));
                ShowAlertDialog();
            }
            else
            {
                Toast.makeText(this.getActivity(), "Pls Enter values.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            Toast.makeText(this.getActivity(), "Parse Exception: Invalid values", Toast.LENGTH_SHORT).show();
        }
    }

    private void ResetViews() {

        etRate.setText(null);
        etMann.setText(null);
        etSair.setText(null);

        if (rbEvening.isChecked()) {
            rbMorning.setChecked(true);
            dp.updateDate(dp.getYear(), dp.getMonth(), dp.getDayOfMonth() + 1);
        }
        else
            rbEvening.setChecked(true);

    }

    private double GetAmount(double _rate,double _mann,double _sair){
        double kgAmount = _rate/40;
        Log.d("KgAmt: ",String.valueOf(kgAmount));
        double totalKg = (_mann*40) + _sair;

        return kgAmount * totalKg;

    }

    private void ShowAlertDialog(){
        if (records != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
            alert.setTitle("Data");

            String message = "Date: "+records.get(0).getDate()+"\n Time: "+records.get(0).getTime();
            alert.setMessage(message);

            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();
                    ResetViews();
                }
            });

            alert.create().show();
        }
        else {
            Toast.makeText(this.getContext(), "No data to save!", Toast.LENGTH_SHORT).show();
        }
    }



}