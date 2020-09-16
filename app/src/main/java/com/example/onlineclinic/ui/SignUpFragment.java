package com.example.onlineclinic.ui;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;

import java.util.LinkedHashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpFragment extends Fragment {
    private ProgressDialog pDialog;

    private EditText inputEmail;
    private EditText inputPassword;

    private LinkedHashMap<String, String> parsedJson = new LinkedHashMap<>();

    private String userId;
    private String noLogout = "false";

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        return view;
    }

  }