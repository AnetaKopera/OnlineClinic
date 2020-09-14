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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;

import java.lang.reflect.Field;
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

            try {

                JsonParser jsonParser = new JsonParser();
                result = requireArguments().getString("search_doctors_result");
                doctorAmount = requireArguments().getInt("search_doctors_amount");

                System.out.println("xde" + result);
                final LinkedHashMap<String, String> parsedDoctors;
                parsedDoctors = jsonParser.parseDoctors(result);
                final Object[] keys = parsedDoctors.keySet().toArray();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        LinearLayout linear = view.findViewById(R.id.fragment_doctors_layout);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_light);
                        Typeface typeface_normal = ResourcesCompat.getFont(getActivity(), R.font.roboto);
                        for (int i = 0; i < Objects.requireNonNull(keys).length; i += 6) {
                            TableRow.LayoutParams btnParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams editParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams imageParams = new TableRow.LayoutParams(243, 243);

                            TableLayout tl = new TableLayout(getActivity());
                            tl.setPadding(25, 25, 0, 25);
                            TableLayout.LayoutParams params_tl = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            params_tl.setMargins(24, 12, 24, 12);

                            tl.setBackgroundResource(R.drawable.edit_text_border);


                            TableRow row1 = new TableRow(getActivity());
                            TableRow.LayoutParams params_row = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                            row1.setLayoutParams(params_row);

                            row1.setGravity(Gravity.CENTER);
                            row1.setWeightSum(4);


                            TableRow row2 = new TableRow(getActivity());
                            row2.setLayoutParams(params_row);
                            row2.setGravity(Gravity.CENTER);
                            row2.setWeightSum(4);

                            Button btn = new Button(getActivity());
                            EditText edit = new EditText(getActivity());


                            final int idDoctor = Integer.valueOf(parsedDoctors.get(keys[i]));
                            final String doctorName = parsedDoctors.get(keys[i + 3]);
                            final String doctorSurname = parsedDoctors.get(keys[i + 4]);
                            final String doctorData = "dr " + doctorName + " " + doctorSurname;
                            final String doctorSpecialization = parsedDoctors.get(keys[i + 1]);
                            final String photoUrl = parsedDoctors.get(keys[i + 2]);

                            ImageView image = new ImageView(getActivity());
                            image.setImageResource(R.drawable.photo1);

                            int resID = getResId(photoUrl, R.drawable.class);
                            image.setImageDrawable(ContextCompat.getDrawable(requireActivity(), resID));

                            final String doctorInformation = doctorData + "\n" + doctorSpecialization;

                            btn.setText("WYBIERZ DOKTORA");
                            btn.setTypeface(typeface);
                            btn.setBackgroundResource(R.drawable.button_style);
                            btn.setTextColor(Color.rgb(0, 0, 0));
                            btn.setGravity(Gravity.CENTER);
                            btn.setTextSize(20);


                            edit.setBackgroundColor(Color.rgb(255, 255, 255));
                            edit.setEnabled(false);
                            edit.setTextColor(Color.rgb(0, 0, 0));
                            edit.setText(doctorInformation);
                            edit.setGravity(Gravity.CENTER);
                            edit.setTypeface(typeface_normal);
                            edit.setTextSize(20);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("Kliknieto w doktora o id: " + idDoctor); /////////////////////////
                                    changeFragment(idDoctor, doctorName, doctorSurname);

                                }
                            });

                            editParams.weight = 3;
                            imageParams.weight = 1;
                            btnParams.weight = 1;

                            row1.addView(edit, editParams);
                            row1.addView(image, imageParams);
                            row2.addView(btn, btnParams);

                            tl.addView(row1);
                            tl.addView(row2);
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

    public int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void changeFragment(int id, String name, String surname) {

        DisplayServices displayServices = new DisplayServices();
        Bundle args = new Bundle();
        args.putInt("doctor_id", id);
        args.putString("doctor_name", name);
        args.putString("doctor_surname", surname);

        displayServices.setArguments(args);

        FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.display_doctors_fragment, displayServices);
        transaction.addToBackStack(null).commit();
    }
}
