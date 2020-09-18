package com.example.onlineclinic.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;
import com.example.onlineclinic.UserActivity;

import java.util.LinkedHashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpFragment extends Fragment {
    private int SUCCESS;

    private ProgressDialog pDialog;

    private EditText inputName;
    private EditText inputSurname;
    private EditText inputEmail;
    private EditText inputPassword;

    private String name;
    private String surname;
    private String Email;
    private String Password;

    private int userId;

    //private LinkedHashMap<String, String> parsedJson = new LinkedHashMap<>();

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        inputName = view.findViewById(R.id.plainName);
        inputSurname = view.findViewById(R.id.plainSurname);
        inputEmail = view.findViewById(R.id.plainEmail);
        inputPassword = view.findViewById(R.id.plainPassword);

        Button signUp = view.findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignUpFragment.CreateNewUser().execute();
            }
        });

        return view;
    }

    class CreateNewUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Proszę czekać trwa tworzenie konta");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            name = inputName.getText().toString();
            surname = inputSurname.getText().toString();
            Email = inputEmail.getText().toString();
            Password = inputPassword.getText().toString();
            System.out.println(name + " " + surname + " " + Email + " " + Password);

            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("name", name)
                    .add("surname", surname)
                    .add("email", Email)
                    .add("password", Password)
                    .build();

            String url_create_product = "http://10.0.2.2/online_clinic/signup.php";
            Request request = new Request.Builder()
                    .url(url_create_product)
                    .post(postData)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();
                JsonParser jsonParser = new JsonParser();


                final LinkedHashMap<String, String> parsedSignUp;
                parsedSignUp = jsonParser.parseSignUp(result);
                final Object[] keys = parsedSignUp.keySet().toArray();

                SUCCESS = Integer.parseInt(Objects.requireNonNull(parsedSignUp.get("success")));
                userId = Integer.parseInt(Objects.requireNonNull(parsedSignUp.get("id")));

            } catch (Exception e) {
                SUCCESS = 0;
                System.out.println("Error: " + e);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            requireActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (SUCCESS == 1) {
                        //SharedPrefs.saveData(getActivity(), "who_is_logged", userId);

                        SharedPrefs.saveData(getActivity(), "who_is_logged", "Nobody");
                        Intent intent = new Intent(getActivity(), UserActivity.class);

                        UserActivity.setUserId(String.valueOf(userId));

                        startActivity(intent);
                        requireActivity().finish();

                        Toast.makeText(getActivity(), "Witaj " + name.trim() + "!", Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getActivity(), "Nie udało się założyć konta.\nSpróbuj ponownie", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}