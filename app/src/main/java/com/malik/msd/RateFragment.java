package com.malik.msd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import java.util.Date;
import java.util.List;

public class RateFragment extends Fragment {

    DatePicker dp;
    RadioButton rbMorning, rbEvening;
    EditText etRate, etMann, etSair;
    TextView tvAmount;
    Button btnAdd, btnUpload;

    List<Entry> entryList;

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
        entryList = new ArrayList<Entry>();

        super.onCreate(savedInstanceState);

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

        etSair.setFilters(new InputFilter[]{new InputFilterMinMax("0", "39")});

        Main();

        return rootView;
    }

    private void Main() {

        entryList.add(new Entry("01/12/21", "Morning", 1000, 1, 1));
        entryList.add(new Entry("01/12/2021", "Evening", 2000, 12, 21));
        entryList.add(new Entry("02/12/2021", "Evening", 7500, 3, 31));
        entryList.add(new Entry("02/12/21", "Morning", 7000, 1, 1));

        toggleUploadButton();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double rate, mann, sair;
                    if (!(etRate.getText().toString().matches("") || etMann.getText().toString().matches("") || etSair.getText().toString().matches(""))) {
                        rate = Double.parseDouble(etRate.getText().toString());
                        mann = Double.parseDouble(etMann.getText().toString());
                        sair = Double.parseDouble(etSair.getText().toString());

                    } else {
                        Toast.makeText(getActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String date = GetFormattedDate(String.valueOf(dp.getDayOfMonth()) + "/" + String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear()));
                    String time = "Morning";
                    if (rbEvening.isChecked())
                        time = "Evening";


                    if (AddEntry(date, time, rate, mann, sair)) {
                        Toast.makeText(getActivity(), "Successfully added", Toast.LENGTH_SHORT).show();
                        toggleUploadButton();
                        ResetViews();
                    } else {
                        Toast.makeText(getActivity(), "Failed to add entry", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Invalid values found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialog();
            }
        });

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rate = etRate.getText().toString();
                String mann = etMann.getText().toString();
                String sair = etSair.getText().toString();
                if (rate.matches("") || mann.matches("") || sair.matches("")) {
                    tvAmount.setText("Amount: 0.0");
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
                    tvAmount.setText("Amount: " + amount);
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
                    tvAmount.setText("Amount: 0.0");
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
                    tvAmount.setText("Amount: " + amount);
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
                    tvAmount.setText("Amount: 0.0");
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
                    tvAmount.setText("Amount: " + amount);
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

    private boolean AddEntry(String _date, String _time, double _rate, double _mann, double _sair) {
        return entryList.add(new Entry(_date, _time, _rate, _mann, _sair));
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
                    boolean uploaded = uploadData(entryList);
                    if (uploaded) {
                        entryList.clear();
                        toggleUploadButton();
                        Toast.makeText(getActivity(), "Upload successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Upload failed!.", Toast.LENGTH_SHORT).show();
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

    private boolean uploadData(List<Entry> entryList) {

        return true;
    }

    private void toggleUploadButton() {
        if (entryList.size() > 0) {
            btnUpload.setEnabled(true);
        } else
            btnUpload.setEnabled(false);
    }


}