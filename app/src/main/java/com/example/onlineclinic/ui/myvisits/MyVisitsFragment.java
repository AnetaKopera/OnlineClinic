package com.example.onlineclinic.ui.myvisits;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MyVisitsFragment extends Fragment {

    private View view;
    private ProgressDialog pDialog;
    private String result;
    private int validate;
    private LinearLayout linear;
    private int amountVisits = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_visits, container, false);

        new MyVisitsFragment.VisitInfo().execute();

        return view;
    }

    class VisitInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Trwa wyszukiwanie wizyt");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            String id = UserActivity.getUserId();

            RequestBody postData = new FormBody.Builder()
                    .add("id", id)
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/actual_visits.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                result = Objects.requireNonNull(response.body()).string();
                System.out.println(result);
                System.out.println("result: " + result);
                JsonParser jsonParser = new JsonParser();

                validate = jsonParser.getQuerySuccess(result);

                if (validate == 1) {


                    final LinkedHashMap<String, String> parsedVisits;
                    parsedVisits = jsonParser.parseVisits(result);
                    final Object[] keys = parsedVisits.keySet().toArray();
                    amountVisits = jsonParser.getQueryAmount(result);

                    System.out.println("papap " + parsedVisits);

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 16, 0, 16);
                            linear = view.findViewById(R.id.linear_actual_visits);
                            linear.removeAllViews();

                            Typeface typeface_normal = ResourcesCompat.getFont(getActivity(), R.font.roboto);


                            for (int i = 0; i < amountVisits; i++) {
                                final TextView edit = new TextView(getActivity());

                                edit.setClickable(true);
                                edit.setFocusable(false);
                                edit.setTypeface(typeface_normal);
                                edit.setBackgroundResource(R.drawable.layout_style);
                                edit.setTextColor(Color.rgb(0, 0, 0));
                                edit.setTextIsSelectable(true);

                                edit.setGravity(Gravity.CENTER);
                                edit.setTextSize(20);

                                final String typeOfService = parsedVisits.get(keys[i * 7]);
                                final String nameDoctor = parsedVisits.get(keys[i * 7 + 1]);
                                final String surnameDoctor = parsedVisits.get(keys[i * 7 + 2]);
                                final String dateVisit = parsedVisits.get(keys[i * 7 + 3]);
                                final String hourVisit = parsedVisits.get(keys[i * 7 + 4]);
                                final String payInAdvance = parsedVisits.get(keys[i * 7 + 5]);
                                final String token = parsedVisits.get(keys[i * 7 + 6]);

                                String text = "\nWizyta:\n" + typeOfService + "\n" + dateVisit + "\n" + hourVisit.substring(0, 5) + "\nDoktor: " + nameDoctor + " " + surnameDoctor;
                                if (payInAdvance.equals("Y")) {
                                    text += "\nZapłacono";
                                }

                                text += "\nToken: " + token + "\n";
                                edit.setText(text);
                                linear.addView(edit, params);
                            }

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
            if (amountVisits == 0) {
                Toast.makeText(getActivity(), "Nie masz jeszcze żadnych wizyt", Toast.LENGTH_SHORT).show();
            }
        }
    }
}