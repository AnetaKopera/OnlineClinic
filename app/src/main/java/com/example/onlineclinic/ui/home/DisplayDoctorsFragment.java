package com.example.onlineclinic.ui.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;

import java.util.LinkedHashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayDoctorsFragment extends Fragment {

    private ProgressDialog pDialog;
    private View view;
    private String result;
    private int doctorAmount;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_doctors, container, false);
        new DisplayDoctorsFragment.ListAllDoctors().execute();
        return view;
    }

    class ListAllDoctors extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.loading_doctors));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

           /* RequestBody postData = new FormBody.Builder()
                    .add("specialization", specialization)
                    .add("doctor", doctor)
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/search_doctor.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();*/
            try {
                /*Response response = client.newCall(request).execute();
                result = Objects.requireNonNull(response.body()).string();
                System.out.println(result);*/

                JsonParser jsonParser = new JsonParser();
                result = requireArguments().getString("search_doctors_result");

                doctorAmount = requireArguments().getInt("search_doctors_amount");


                System.out.println("xde" + result);
                final LinkedHashMap<String, String> parsedDoctors;
                parsedDoctors = jsonParser.parseDoctors(result);
                final Object[] keys = parsedDoctors.keySet().toArray();

                /*for (int i = 0; i < keys.length; i += 5) {
                    System.out.println(" ");
                    for (int j = 0; j < 5; j++) {
                        System.out.println(keys[i + j] + "   " + parsedDoctors.get(keys[i + j]));
                    }

                }*/

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout linear = view.findViewById(R.id.fragment_doctors_layout);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);


                        for (int i = 0; i < Objects.requireNonNull(keys).length; i += 5) {
                            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            Button btn = new Button(getActivity());
                            EditText edit = new EditText(getActivity());

                            final String idDoctor = parsedDoctors.get(keys[i]);
                            final String doctorData = "dr " + parsedDoctors.get(keys[i + 2]) + " " + parsedDoctors.get(keys[i + 3]);
                            final String doctorSpecialization = parsedDoctors.get(keys[i + 1]);


                            final String doctorInformation = doctorData + "\n" + doctorSpecialization;

                            btn.setText("WYBIERZ DOKTORA");
                            btn.setBackgroundColor(Color.rgb(0,0,0));
                            btn.setTextColor(Color.rgb(255, 255, 255));
                            btn.setGravity(Gravity.CENTER);


                            edit.setBackgroundColor(Color.rgb(230, 230, 230));
                            edit.setBackgroundResource(R.drawable.edit_text_border);
                            edit.setEnabled(false);
                            edit.setTextColor(Color.rgb(0, 0, 0));
                            edit.setText(doctorInformation);
                            edit.setGravity(Gravity.CENTER);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("Kliknieto w doktora o id: " + idDoctor);
                                    /*if (UserActivity.getUserId().equals(""))  //User is not logged in
                                    {
                                        Toast.makeText(getActivity(),
                                                R.string.sign_in_first, Toast.LENGTH_LONG).show();
                                    } else    //User is logged in
                                    {
                                        changeFragment(argument, firmData, serviceTime, serviceName, servicePrice);
                                    }*/
                                }
                            });

                            btnParams.setMargins(5, 0, 5, 0);
                            linear.addView(btn, btnParams);

                            editParams.setMargins(5, 0, 5, 10);
                            linear.addView(edit, editParams);


                        }
                    }
                });

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();

        }
    }

    private void changeFragment() {

       /* DisplayDoctorsFragment displayDoctorsFragment = new DisplayDoctorsFragment();
        Bundle args = new Bundle();
        args.putString("search_doctors_result", result);
        args.putInt("search_doctors_amount", amount);

        displayDoctorsFragment.setArguments(args);

        FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.home_fragment, displayDoctorsFragment);
        transaction.addToBackStack(null).commit();*/
    }
}
