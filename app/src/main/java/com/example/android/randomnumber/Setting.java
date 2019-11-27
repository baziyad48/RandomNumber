package com.example.android.randomnumber;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class Setting extends Fragment {
    //inisialisasi untuk default preferences jika tidak ada
    private final String INTERVAL = "INTERVAL";
    private final String JACKPOT = "JACKPOT";
    private final long DEFAULT_INTERVAL = 500;
    private final int DEFAULT_JACKPOT = 8;
    //untuk mengambil dan menaruh nilai di edit text
    private EditText interval, jackpot;
    private SharedPreferences mPreferences;

    public Setting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //ambil edit text sesuai id nya
        interval = getActivity().findViewById(R.id.et_interval);
        jackpot = getActivity().findViewById(R.id.et_jackpot);
        //mengambil preferences
        mPreferences = getActivity().getSharedPreferences(getActivity().getApplication().toString(), MODE_PRIVATE);
        long intervalVal = mPreferences.getLong(INTERVAL, DEFAULT_INTERVAL);
        int jackpotVal = mPreferences.getInt(JACKPOT, DEFAULT_JACKPOT);
        //inisialisasi variabel interval dan jackpot dari shared preferences
        interval.setText(String.valueOf(intervalVal));
        jackpot.setText(String.valueOf(jackpotVal));
        //mengubah dan menyimpan interval
        interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                long millis = Long.parseLong(String.valueOf(interval.getText().toString().isEmpty() ? DEFAULT_INTERVAL : interval.getText().toString()));
                preferencesEditor.putLong(INTERVAL, millis);
                preferencesEditor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //mengubah dan menyimpan interval
        jackpot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putInt(JACKPOT, Integer.parseInt(String.valueOf(jackpot.getText().toString().isEmpty() ? 7 : jackpot.getText().toString())));
                preferencesEditor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
