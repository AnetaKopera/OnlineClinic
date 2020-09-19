package com.example.onlineclinic.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.onlineclinic.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class ThankYouFragment extends Fragment {
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        ImageView imageView = view.findViewById(R.id.codeQR);


        QRCodeWriter  qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode("HelatCare_test12345", BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.RGB_565);

            for(int x=0; x<300; x++){
                for(int y=0; y<300; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*String idService = requireArguments().getString("idService");
        String timeOfService = requireArguments().getString("timeOfService");
        String dateVisit = requireArguments().getString("dateVisit");
        String doctorName = requireArguments().getString("doctor_name");
        String doctorSurname = requireArguments().getString("doctor_surname");
        String description = requireArguments().getString("description");
        String price = requireArguments().getString("price");
        String typeOfService = requireArguments().getString("typeOfService");
        String hour = requireArguments().getString("hour");

        final String summaryText = typeOfService + "\n\n" + description + "\n\nData wizyty: " + dateVisit + " " + hour.substring(0, 5) + " \nCzas trwania: " + timeOfService + "min\nCena: " + price + " zł\n\nDoktor: " + doctorName + " " + doctorSurname;


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
        btn_with_pay.setVisibility(View.GONE);

     *//*   btn_without_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payInAdvance = "N";
                makeOrder();
            }
        });
*//*
      *//*  btn_with_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult
                        (new Intent(getActivity(), PayPalMainActivity.class).putExtra("AMOUNT", servicePrice), 808);
            }
        });*/

        return view;
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 808) {
            // payInAdvance = "Y";
            makeOrder();
        }
    }*/

    private void makeOrder() {
        /*MakeOrderFragment makeOrderFragment = new MakeOrderFragment();

        Bundle args = new Bundle();
        args.putString("ServiceID", serviceID);
        args.putString("DateVisit", selectedDate);
        args.putString("HourVisit", selectedHour);
        args.putString("PayInAdvance", payInAdvance);
        args.putString("ID_Worker", workerID);
        args.putString("ID_Client", clientID);

        makeOrderFragment.setArguments(args);

        FragmentTransaction transaction = (Objects.requireNonNull(getActivity()).getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.fragment_summary, makeOrderFragment);
        transaction.addToBackStack(null).commit();*/
    }
}
