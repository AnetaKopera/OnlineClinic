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
                et_doctor.setText("");
                et_specialization.setText("");

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
                //System.out.println(result);
                JsonParser jsonParser = new JsonParser();

                amount = jsonParser.getQueryAmount(result);
                //System.out.println("Query amount= " + amount);

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