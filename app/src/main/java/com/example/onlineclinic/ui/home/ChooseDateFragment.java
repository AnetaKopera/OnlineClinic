package com.example.onlineclinic.ui.home;


import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.R;
import com.example.onlineclinic.ui.home.HoursFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChooseDateFragment extends Fragment {

    private String currentDate;
    private String selectedDate;

    private int current;
    private int selected;

    private Button next;

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_date, container, false);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        currentDate = formatter.format(date);
        selectedDate = currentDate;


        current = Integer.parseInt(currentDate.substring(0, 4) + currentDate.substring(5, 7) + currentDate.substring(8));
        selected = Integer.parseInt(selectedDate.substring(0, 4) + selectedDate.substring(5, 7) + selectedDate.substring(8));

        next = view.findViewById(R.id.btnNext);

        Calendar c = Calendar.getInstance();
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setMinDate(c.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int selected_year, int selected_month, int selected_day) {

                String dayOfMonth;
                String month;

                if (selected_day < 10) {
                    dayOfMonth = "0" + selected_day;
                } else {
                    dayOfMonth = "" + selected_day;
                }

                if (selected_month < 9) {
                    month = "0" + (selected_month + 1);
                } else {
                    month = "" + (selected_month + 1);
                }

                selectedDate = selected_year + "-" + month + "-" + dayOfMonth;

                current = Integer.parseInt(currentDate.substring(0, 4) + currentDate.substring(5, 7) + currentDate.substring(8));
                selected = Integer.parseInt(selectedDate.substring(0, 4) + selectedDate.substring(5, 7) + selectedDate.substring(8));

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected >= current) {
                    new ChooseDateFragment.isAvailableDate().execute();

                } else {
                    Toast.makeText(getActivity(), "Błędna data wizyty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    class isAvailableDate extends AsyncTask<String, String, String> {
        private int amount;

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("timeOfService", requireArguments().getString("timeOfService"))
                    .add("dateVisit", selectedDate)
                    .add("idDoctor", requireArguments().getString("idDoctor"))
                    .build();

            String url_services = "http://10.0.2.2/online_clinic/hoursAmount.php";
            Request request = new Request.Builder()
                    .url(url_services)
                    .post(postData)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();

                JsonParser jsonParser = new JsonParser();

                amount = jsonParser.getQueryAmount(result);

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            return null;
        }

        protected void onPostExecute(String result) {
            if (amount != 0) {

                HoursFragment hoursFragment = new HoursFragment();
                Bundle args = new Bundle();
                args.putString("idDoctor", requireArguments().getString("idDoctor"));
                args.putString("idService", requireArguments().getString("idService"));
                args.putString("timeOfService",  requireArguments().getString("timeOfService"));
                args.putString("dateVisit",  selectedDate);

                hoursFragment.setArguments(args);

                FragmentTransaction transaction = Objects.requireNonNull(requireActivity().getSupportFragmentManager()).beginTransaction();
                transaction.replace(R.id.home_fragment, hoursFragment);
                transaction.addToBackStack(null).commit();
            } else {
                Toast.makeText(getActivity(), "Brak dostępnych terminów w ten dzień", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
