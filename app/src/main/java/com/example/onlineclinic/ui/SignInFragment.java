package com.example.onlineclinic.ui;

import com.example.onlineclinic.UserActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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


public class SignInFragment extends Fragment {
    private ProgressDialog pDialog;

    private EditText inputEmail;
    private EditText inputPassword;

    private LinkedHashMap<String, String> parsedJson = new LinkedHashMap<>();

    private String userId;
    private String noLogout = "false";
    private String clientName;

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        inputEmail = view.findViewById(R.id.plainEmail);
        inputPassword = view.findViewById(R.id.plainPass);

        Button signIn = view.findViewById(R.id.btnSignIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignInFragment.UserLogin().execute();
            }
        });

        CheckBox checkBox = view.findViewById(R.id.noLogoutCheck);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked)
                    noLogout = "true";
                else
                    noLogout = "false";
            }
        });

        return view;
    }

    class UserLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Proszę czekać.\nTrwa logowanie");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String Email = inputEmail.getText().toString();
            String Password = inputPassword.getText().toString();

            if (Email.isEmpty()) {
                Toast.makeText(getActivity(), "Podaj email", Toast.LENGTH_LONG).show();
            } else if (Password.isEmpty()) {
                Toast.makeText(getActivity(), "Podaj hasło", Toast.LENGTH_LONG).show();
            } else {

                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("login", Email)
                        .add("password", Password)
                        .build();

                String url_create_product = "http://10.0.2.2/online_clinic/login.php";
                Request request = new Request.Builder()
                        .url(url_create_product)
                        .post(postData)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    String result = Objects.requireNonNull(response.body()).string();
                    System.out.println(result);
                    JsonParser jsonParser = new JsonParser();

                    parsedJson = jsonParser.parseLogin(result);
                    final Object[] keys = parsedJson.keySet().toArray();
                    userId = parsedJson.get("id");
                    clientName = parsedJson.get("clientName");
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();

            if (parsedJson.isEmpty()) {
                Toast.makeText(getActivity(), "Nieprawidłowy email lub hasło", Toast.LENGTH_LONG).show();
            } else {
                if (noLogout.equals("true")) {
                    SharedPrefs.saveData(getActivity(), "who_is_logged", userId);
                } else {
                    SharedPrefs.saveData(getActivity(), "who_is_logged", "Nobody");
                }

                Intent intent = new Intent(getActivity(), UserActivity.class);

                UserActivity.setUserId(userId);

                startActivity(intent);
                requireActivity().finish();
                Toast.makeText(getActivity(), "Witaj " + clientName + "!", Toast.LENGTH_LONG).show();

            }
        }
    }
}