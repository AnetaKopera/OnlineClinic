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
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HoursFragment extends Fragment {

    private String currentDate;
    private String selectedDate;

    private int current;
    private int selected;

    private Button next;
    private ProgressDialog pDialog;

    private View view;
    private String selectedHour="";

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hours, container, false);
        new HoursFragment.loadHours().execute();

        /*next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected >= current) {


                } else {
                    Toast.makeText(getActivity(), "Błędna data wizyty", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        return view;
    }

    class loadHours extends AsyncTask<String, String, String> {

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

            RequestBody postData = new FormBody.Builder()
                    .add("timeOfService", Objects.requireNonNull(requireArguments().getString("timeOfService")))
                    .add("dateVisit", Objects.requireNonNull(requireArguments().getString("dateVisit")))
                    .add("idDoctor", Objects.requireNonNull(requireArguments().getString("idDoctor")))
                    .add("idService", Objects.requireNonNull(requireArguments().getString("idService")))
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/hoursList.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();

                System.out.println("result " + result);

                JsonParser jsonParser = new JsonParser();

                final LinkedHashMap<String, String> parsedHours;
                parsedHours = jsonParser.parseHours(result);

                System.out.println(parsedHours);


                final Object[] keys = parsedHours.keySet().toArray();


                final Button next = view.findViewById(R.id.btnNextHours);

                //Generate buttons dynamically based on JSON

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        //LinearLayout linear = view.findViewById(R.id.hours_linear);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_light);


                        //next.setEnabled(false);
                        //next.setTextColor(Color.rgb(150, 150, 150));

                        final RadioGroup radioGroup = view.findViewById(R.id.radio_group_hours);

                        for (int i = 0; i < Objects.requireNonNull(keys).length; i++) {
                            RadioButton rb = new RadioButton(getActivity());
                            String rbText = parsedHours.get(keys[i]);
                            rbText = rbText.substring(0, 5);

                            rb.setText(rbText);
                            rb.setTextColor(Color.rgb(0, 0, 0));
                            rb.setTextSize(20);
                            rb.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                            rb.setTypeface(typeface);

                            //params.setMargins(5, 0, 5, 8);
                            radioGroup.addView(rb);
                        }

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                                if (checkedID != -1) {
                                   // next.setEnabled(true);
                                 //   next.setTextColor(Color.rgb(255, 255, 255));
                                    RadioButton radioButton = view.findViewById(checkedID);
                                    selectedHour = radioButton.getText().toString() + ":00";
                                }
                            }
                        });

                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!selectedHour.equals(""))
                                {
                                    changeFragment();
                                }else{
                                    Toast.makeText(getActivity(), "Wybierz godzinę", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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


      /*  ChooseDateFragment chooseDateFragment = new ChooseDateFragment();
        Bundle args = new Bundle();
        args.putString("idDoctor", idDoctor);
        args.putString("idService", choosedService);
        args.putString("timeOfService", time);

        chooseDateFragment.setArguments(args);

        FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.home_fragment, chooseDateFragment);
        transaction.addToBackStack(null).commit();*/
    }

}
