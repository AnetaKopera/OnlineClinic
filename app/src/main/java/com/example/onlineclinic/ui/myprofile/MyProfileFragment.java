package com.example.onlineclinic.ui.myprofile;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class MyProfileFragment extends Fragment {
    private ProgressDialog pDialog;
    private View view;

    private String userName;
    private String userSurname;
    private String userMail;

    private EditText showName;
    private EditText showSurname;
    private EditText showMail;

    private Button btn;
    private String ID;

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ID = UserActivity.getUserId();

        showName = view.findViewById(R.id.showName);
        showSurname = view.findViewById(R.id.showSurname);
        showMail = view.findViewById(R.id.showMail);

        btn = view.findViewById(R.id.profile_edit_button);

        Button pass = view.findViewById(R.id.profile_edit_password);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                FragmentTransaction transaction =
                        Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
                transaction.replace(R.id.fragment_my_profile, changePasswordFragment);
                transaction.addToBackStack(null).commit();
            }
        });

        new MyProfileFragment.ListUserProfile().execute();

        return view;
    }

    class ListUserProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Proszę czekać trwa ładowanie");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String ID = UserActivity.getUserId();

            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("idUser", ID)
                    .build();

            String url_user = "http://10.0.2.2/online_clinic/user_profile.php";
            Request request = new Request.Builder()
                    .url(url_user)
                    .post(postData)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();


                JsonParser jsonParser = new JsonParser();
                final LinkedHashMap<String, String> parsedUserProfile;
                parsedUserProfile = jsonParser.parseUserProfile(result);
                final Object[] keys = parsedUserProfile.keySet().toArray();

                userSurname = parsedUserProfile.get(keys[0]);
                userName = parsedUserProfile.get(keys[1]);
                userMail = parsedUserProfile.get(keys[2]);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showName.setText(userName);
                        showSurname.setText(userSurname);
                        showMail.setText(userMail);

                        showName.setTextColor(Color.rgb(0, 0, 0));
                        showSurname.setTextColor(Color.rgb(0, 0, 0));
                        showMail.setTextColor(Color.rgb(0, 0, 0));

                        showName.setTextSize(21);
                        showSurname.setTextSize(21);
                        showMail.setTextSize(21);

                        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto);

                        showName.setTypeface(typeface);
                        showSurname.setTypeface(typeface);
                        showMail.setTypeface(typeface);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showName.setEnabled(true);
                                showSurname.setEnabled(true);
                                showMail.setEnabled(true);

                                btn.setText("Zapisz");

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showName.setEnabled(false);
                                        showSurname.setEnabled(false);
                                        showMail.setEnabled(false);

                                        new MyProfileFragment.UpdateUserProfile().execute();

                                        MyProfileFragment myProfileFragment = new MyProfileFragment();

                                        FragmentTransaction transaction =
                                                Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
                                        transaction.replace(R.id.fragment_my_profile, myProfileFragment);
                                        transaction.addToBackStack(null).commit();
                                    }
                                });
                            }
                        });
                    }
                });
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            return null;
        }

        protected void onPostExecute(String s) {
            pDialog.dismiss();
        }

    }

    class UpdateUserProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Proszę czekać");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String Name = showName.getText().toString();
            String Surname = showSurname.getText().toString();
            String Mail = showMail.getText().toString();

            String ID = UserActivity.getUserId();

            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("id", ID)
                    .add("surname", Surname)
                    .add("name", Name)
                    .add("email", Mail)
                    .build();

            String url_update = "http://10.0.2.2/online_clinic/update_user_profile.php";
            Request request = new Request.Builder()
                    .url(url_update)
                    .post(postData)
                    .build();

            try {
                client.newCall(request).execute();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            return null;

        }

        protected void onPostExecute(String s) {
            pDialog.dismiss();
        }
    }
}