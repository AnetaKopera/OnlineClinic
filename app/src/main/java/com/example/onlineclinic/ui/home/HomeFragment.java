package com.example.onlineclinic.ui.home;

import android.app.ProgressDialog;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private ProgressDialog pDialog;
    private View view;
    private String doctor;
    private String specialization;

    private Button btn_search;
    private int amount;
    private String result;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        view = inflater.inflate(R.layout.fragment_home, container, false);

        final EditText et_doctor = view.findViewById(R.id.et_doctor);
        final EditText et_specialization = view.findViewById(R.id.et_specialization);

        btn_search = view.findViewById(R.id.btn_SearchDoctor);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctor = String.valueOf(et_doctor.getText());
                specialization = String.valueOf(et_specialization.getText());

                new HomeFragment.ListAllDoctors().execute();
            }
        });

        return view;
    }


    class ListAllDoctors extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.wait_doctors));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("specialization", specialization)
                    .add("doctor", doctor)
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/search_doctor.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                result = Objects.requireNonNull(response.body()).string();
                System.out.println(result);

                JsonParser jsonParser = new JsonParser();

                amount = jsonParser.getQueryAmount(result);

                System.out.println("Query amount= " + amount);


                /*final LinkedHashMap<String, String> parsedDoctors;
                parsedDoctors = jsonParser.parseDoctors(result);
                final Object[] keys = parsedDoctors.keySet().toArray();

                for (int i = 0; i < keys.length; i += 5) {
                    System.out.println(" ");
                    for (int j = 0; j < 5; j++) {
                        System.out.println(keys[i + j] + "   " + parsedDoctors.get(keys[i + j]));
                    }

                }*/
/*
                //Generate buttons dynamically based on JSON

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout linear = view.findViewById(R.id.services_linear);

                        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.oregano);

                        if (parsedServices.isEmpty()) {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                    (LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                            Button btn = new Button(getActivity());
                            btn.setText(R.string.no_services);
                            btn.setBackgroundResource(R.drawable.gradient_1);
                            btn.setTextColor(Color.rgb(255, 255, 255));
                            btn.setTypeface(typeface);

                            params.setMargins(10, 3, 10, 3);
                            linear.addView(btn, params);
                        } else {

                            for (int i = 0; i < Objects.requireNonNull(keys).length; i += 5) {
                                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams
                                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);

                                LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams
                                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);

                                Button btn = new Button(getActivity());
                                EditText edit = new EditText(getActivity());

                                final String argument = parsedServices.get(keys[i]);
                                final String firmData = " " + parsedServices.get(keys[i + 2]) + "\n"
                                        + " " + getResources().getString(R.string.price) + " " + parsedServices.get(keys[i + 3]) + " PLN \n"
                                        + " " + getResources().getString(R.string.time) + " " + parsedServices.get(keys[i + 4]) + " MIN";
                                final String serviceTime = parsedServices.get(keys[i + 4]);
                                final String serviceName = parsedServices.get(keys[i + 1]);
                                final String servicePrice = parsedServices.get(keys[i + 3]);

                                btn.setText(serviceName);
                                btn.setBackgroundResource(R.drawable.gradient_buttons);
                                btn.setTextColor(Color.rgb(255, 255, 255));
                                btn.setGravity(Gravity.CENTER);
                                btn.setTypeface(typeface);

                                edit.setBackgroundColor(Color.rgb(230, 230, 230));
                                edit.setEnabled(false);
                                edit.setTextColor(Color.rgb(0, 0, 0));
                                edit.setText(firmData);
                                edit.setGravity(Gravity.CENTER);
                                edit.setTypeface(typeface);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (UserActivity.getUserId().equals(""))  //User is not logged in
                                        {
                                            Toast.makeText(getActivity(),
                                                    R.string.sign_in_first, Toast.LENGTH_LONG).show();
                                        } else    //User is logged in
                                        {
                                            changeFragment(argument, firmData, serviceTime, serviceName, servicePrice);
                                        }
                                    }
                                });

                                btnParams.setMargins(5, 0, 5, 0);
                                linear.addView(btn, btnParams);

                                editParams.setMargins(5, 0, 5, 10);
                                linear.addView(edit, editParams);
                            }
                        }
                    }
                });*/
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if (amount == 0) {
                Toast.makeText(getActivity(), "Nie znaleziono pasującego doktora!\nSpróbuj ponownie.", Toast.LENGTH_LONG).show();
            } else {
                changeFragment();
            }
        }
    }

    private void changeFragment() {

        DisplayDoctorsFragment displayDoctorsFragment = new DisplayDoctorsFragment();
        Bundle args = new Bundle();
        args.putString("search_doctors_result", result);
        args.putInt("search_doctors_amount", amount);

        displayDoctorsFragment.setArguments(args);

        FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.home_fragment, displayDoctorsFragment);
        transaction.addToBackStack(null).commit();
    }
}