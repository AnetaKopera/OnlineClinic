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

import com.example.onlineclinic.UserActivity;
import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.MainActivity;
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

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("siema w sign in");
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
                System.out.println("zmiana");
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
            System.out.println("proszec czekac");
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Proszę czekać.\nTrwa logowanie");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("logowanie");
            String Email = inputEmail.getText().toString();
            String Password = inputPassword.getText().toString();

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
                userId = parsedJson.get(Objects.requireNonNull(keys)[0]);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();

            System.out.println("parsed json " + parsedJson);

            if (parsedJson.isEmpty()) {
                Toast.makeText(getActivity(), "Nieprawidłowy email lub hasło", Toast.LENGTH_LONG).show();
            } else {
                // UserAcitivity userCityFragment = new UserAcitivity();

                Bundle args = new Bundle();
                args.putString("USER_ID", userId);
                args.putString("NO_LOGOUT", noLogout);

                System.out.println("userID TO " + userId + " nie wylogowywyuj " + noLogout);
                //userCityFragment.setArguments(args);

                /*FragmentTransaction transaction =
                        Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSupportFragmentManager()).beginTransaction();
                transaction.replace(R.id.fragment_sign_in, userCityFragment);
                transaction.addToBackStack(null).commit();*/
            }
        }
    }
}