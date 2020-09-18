package com.example.onlineclinic.ui.home;

import com.example.onlineclinic.UserActivity;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
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

public class DisplayServices extends Fragment {

    private ProgressDialog pDialog;
    private View view;
    private String idDoctor;
    private String choosedService;
    private String time;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_services, container, false);
        new DisplayServices.ListAllServices().execute();
        return view;
    }

    class ListAllServices extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.loading_services));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            idDoctor = String.valueOf(requireArguments().getInt("doctor_id"));


            RequestBody postData = new FormBody.Builder()
                    .add("idDoctor", idDoctor)
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/search_services.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();

                JsonParser jsonParser = new JsonParser();
                ;
                final LinkedHashMap<String, String> parsedServices;
                parsedServices = jsonParser.parseDoctors(result);
                final Object[] keys = parsedServices.keySet().toArray();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout linear = view.findViewById(R.id.fragment_services_layout);

                        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto);
                        Typeface typeface_bold = ResourcesCompat.getFont(getActivity(), R.font.roboto_bold);
                        Typeface typeface_light = ResourcesCompat.getFont(getActivity(), R.font.roboto_light);

                        for (int i = 0; i < Objects.requireNonNull(keys).length; i += 6) {

                            TableRow.LayoutParams btnParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams editParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                            TableLayout tl = new TableLayout(getActivity());
                            tl.setPadding(25, 25, 25, 25);
                            TableLayout.LayoutParams params_tl = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            params_tl.setMargins(24, 12, 24, 12);

                            tl.setBackgroundResource(R.drawable.layout_style);


                            TableRow row1 = new TableRow(getActivity());
                            TableRow.LayoutParams params_row = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                            row1.setLayoutParams(params_row);

                            row1.setGravity(Gravity.CENTER);
                            row1.setWeightSum(4);


                            TableRow row2 = new TableRow(getActivity());
                            row2.setLayoutParams(params_row);
                            row2.setGravity(Gravity.CENTER);
                            row2.setWeightSum(4);

                            TableRow row3 = new TableRow(getActivity());
                            row3.setLayoutParams(params_row);
                            row3.setGravity(Gravity.CENTER);
                            row3.setWeightSum(4);


                            Button btn = new Button(getActivity());
                            EditText edit = new EditText(getActivity());
                            EditText edit1 = new EditText(getActivity());

                            final String idService = parsedServices.get(keys[i]);
                            final String typeOfService = parsedServices.get(keys[i + 1]);
                            final String description = parsedServices.get(keys[i + 2]);
                            final String price = parsedServices.get(keys[i + 3]);
                            final String timeOfService = parsedServices.get(keys[i + 4]);
                            // final String idClinic = parsedServices.get(keys[i + 5]);

                            final String serviceInformation = description + "\n" + "Cena " + price + " zł\n" + "Czas wizyty " + timeOfService + " min.";


                            btn.setText("WYBIERZ WIZYTĘ");
                            btn.setTypeface(typeface_light);
                            btn.setTextSize(20);
                            btn.setBackgroundResource(R.drawable.button_style);
                            btn.setTextColor(Color.rgb(0, 0, 0));
                            btn.setGravity(Gravity.CENTER);


                            edit1.setBackgroundColor(Color.rgb(228, 240, 241));
                            edit1.setEnabled(false);
                            edit1.setTextColor(Color.rgb(0, 0, 0));
                            edit1.setText(typeOfService);
                            edit1.setGravity(Gravity.CENTER);
                            edit1.setTypeface(typeface_bold);
                            edit1.setTextSize(20);

                            edit.setBackgroundColor(Color.rgb(228, 240, 241));
                            edit.setEnabled(false);
                            edit.setTextColor(Color.rgb(0, 0, 0));
                            edit.setText(serviceInformation);
                            edit.setGravity(Gravity.CENTER);
                            edit.setTypeface(typeface);
                            edit.setTextSize(20);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("Kliknieto w serwis o id: " + idService);
                                    choosedService = idService;
                                    time =  timeOfService;
                                    changeFragment();
                                }
                            });

                            btnParams.setMargins(200, 10, 200, 10);
                            editParams.setMargins(50, 0, 50, 0);

                            editParams.weight = 3;
                            btnParams.weight = 3;

                            row1.addView(edit1, editParams);
                            row2.addView(edit, editParams);
                            row3.addView(btn, btnParams);

                            tl.addView(row1);
                            tl.addView(row2);
                            tl.addView(row3);
                            linear.addView(tl, params_tl);
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
        //tescik
        /*if (UserActivity.getUserId().equals(""))  //User is not logged in
        {
            Toast.makeText(getActivity(),"Niezalogowany", Toast.LENGTH_LONG).show();
        } else    //User is logged in
        {
            Toast.makeText(getActivity(),"ZALOGOWANY", Toast.LENGTH_LONG).show();
            //changeFragment(argument, firmData, serviceTime, serviceName, servicePrice);
        }*/
        ChooseDateFragment chooseDateFragment = new ChooseDateFragment();
        Bundle args = new Bundle();
        args.putString("idDoctor", idDoctor);
        args.putString("idService", choosedService);
        args.putString("timeOfService", time);

        chooseDateFragment.setArguments(args);

        FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.home_fragment, chooseDateFragment);
        transaction.addToBackStack(null).commit();
    }
}
