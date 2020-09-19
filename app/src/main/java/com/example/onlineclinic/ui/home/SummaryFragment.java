package com.example.onlineclinic.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.PayPal.PayPalMainActivity;
import com.example.onlineclinic.R;
import com.example.onlineclinic.UserActivity;


public class SummaryFragment extends Fragment {
    private View view;
    private String payInAdvance;
    private String price;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary, container, false);

        String timeOfService = requireArguments().getString("timeOfService");
        String dateVisit = requireArguments().getString("dateVisit");
        String doctorName = requireArguments().getString("doctor_name");
        String doctorSurname = requireArguments().getString("doctor_surname");
        String description = requireArguments().getString("description");
        price = requireArguments().getString("price");
        String typeOfService = requireArguments().getString("typeOfService");
        String hour = requireArguments().getString("hour");

        final String summaryText = typeOfService + "\n\n" + description + "\n\nData wizyty: " + dateVisit + " " + hour.substring(0, 5) + " \nCzas trwania: " + timeOfService + " min\nCena: " + price + " zł\n\nDoktor: " + doctorName + " " + doctorSurname;


        LinearLayout summary = view.findViewById(R.id.summary_linear);

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.roboto);

        EditText editText = new EditText(getActivity());
        editText.setText(summaryText);
        editText.setPadding(10, 10, 10, 10);
        editText.setBackgroundResource(R.drawable.layout_style);
        editText.setEnabled(false);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setTypeface(typeface);
        editText.setTextSize(20);
        editText.setTextColor(Color.rgb(0, 0, 0));
        summary.addView(editText);

        Button btn_without_pay = view.findViewById(R.id.btn_Without_Pay);
        Button btn_with_pay = view.findViewById(R.id.btn_With_Pay);

        if (UserActivity.getUserId().equals("")) {
            btn_with_pay.setVisibility(View.GONE);
        }


        btn_without_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payInAdvance = "N";
                makeOrder();
            }
        });

        btn_with_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), PayPalMainActivity.class).putExtra("AMOUNT", price), 808);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 808) {
            payInAdvance = "Y";
            makeOrder();
        }
    }

    private void makeOrder() {
        ThankYouFragment thankYouFragment = new ThankYouFragment();

        Bundle args = new Bundle();
        args.putString("idDoctor", requireArguments().getString("idDoctor"));
        args.putString("idService", requireArguments().getString("idService"));
        args.putString("timeOfService", requireArguments().getString("timeOfService"));
        args.putString("dateVisit", requireArguments().getString("dateVisit"));
        args.putString("doctor_name", requireArguments().getString("doctor_name"));
        args.putString("doctor_surname", requireArguments().getString("doctor_surname"));
        args.putString("description", requireArguments().getString("description"));
        args.putString("price", requireArguments().getString("price"));
        args.putString("typeOfService", requireArguments().getString("typeOfService"));
        args.putString("hour", requireArguments().getString("hour"));
        args.putString("payInAdvance", payInAdvance);

        thankYouFragment.setArguments(args);

        FragmentTransaction transaction = (requireActivity().getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.fragment_summary, thankYouFragment);
        transaction.addToBackStack(null).commit();
    }
}
