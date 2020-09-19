package com.example.onlineclinic;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.ui.home.DisplayServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckVisitFragment extends Fragment {

    private View view;
    private EditText token;
    private ProgressDialog pDialog;
    private String checkThisToken;
    private String result;
    private int validate;
    private LinearLayout linear;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_check_visit, container, false);

        token = view.findViewById(R.id.et_token);

        Button btn_checkVisit = view.findViewById(R.id.btn_checkVisit);

        btn_checkVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkThisToken = token.getText().toString().trim();
                token.setText("");
                new CheckVisitFragment.VisitInfo().execute();
            }
        });

        return view;
    }

    class VisitInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Trwa sprawdzanie wizyty");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("token", checkThisToken)
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/checked_visit.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                result = Objects.requireNonNull(response.body()).string();
                //System.out.println(result);
                System.out.println("result: " + result);
                JsonParser jsonParser = new JsonParser();

                validate = jsonParser.getQuerySuccess(result);
                System.out.println("hej: " + validate);
                if (validate == 1) {


                    final LinkedHashMap<String, String> parsedCheckedVisit;
                    parsedCheckedVisit = jsonParser.parseSingleVisit(result);
                    //final Object[] keys = parsedCheckedVisit.keySet().toArray();

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            linear = view.findViewById(R.id.checkedVisit);
                            linear.removeAllViews();

                            //Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_light);
                            Typeface typeface_normal = ResourcesCompat.getFont(getActivity(), R.font.roboto);


                            final EditText edit = new EditText(getActivity());

                            edit.setClickable(true);
                            edit.setFocusable(false);
                            edit.setTypeface(typeface_normal);
                            edit.setBackgroundResource(R.drawable.layout_style);
                            edit.setTextColor(Color.rgb(0, 0, 0));

                            edit.setGravity(Gravity.CENTER);
                            edit.setTextSize(20);
                            //editParams.setMargins(5, 0, 5, 16);

                            //nameDoctor":"Eugeniusz","surnameDoctor":"Maczek","dateVisit":"2020-09-29","hourVisit":"10:30:00","payInAdvance":"N"}

                            final String typeOfService = parsedCheckedVisit.get("typeOfService");
                            final String nameDoctor = parsedCheckedVisit.get("nameDoctor");
                            final String surnameDoctor = parsedCheckedVisit.get("surnameDoctor");
                            final String dateVisit = parsedCheckedVisit.get("dateVisit");
                            final String hourVisit = parsedCheckedVisit.get("hourVisit");
                            final String payInAdvance = parsedCheckedVisit.get("payInAdvance");

                            String text = "Wizyta:\n" + typeOfService + "\n" + dateVisit + "\n" + hourVisit.substring(0,5) + "\nDoktor: " + nameDoctor + " " + surnameDoctor + "\n";
                            if (payInAdvance.equals("Y")) {
                                text += " Zapłacono";
                            } else {
                                text += "Niezapłacono";
                            }
                            edit.setText(text);
                            linear.addView(edit);


                        }

                    });
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            linear = view.findViewById(R.id.checkedVisit);
                            linear.removeAllViews();
                            Toast.makeText(getActivity(), "Błędny token", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
        }
    }


}