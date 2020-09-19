package com.example.onlineclinic.ui.home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.onlineclinic.R;
import com.example.onlineclinic.UserActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ThankYouFragment extends Fragment {
    private View view;
    private ImageView imageView;
    private ProgressDialog pDialog;

    private String idDoctor;
    private String idService;
    private String dateVisit;
    private String payInAdvance;
    private String hour;
    private String token;

    private String idClient;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        imageView = view.findViewById(R.id.codeQR);
        Button btn_home = view.findViewById(R.id.btn_back_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        idDoctor = requireArguments().getString("idDoctor");
        idService = requireArguments().getString("idService");
        dateVisit = requireArguments().getString("dateVisit");
        payInAdvance = requireArguments().getString("payInAdvance");
        hour = requireArguments().getString("hour");

        token = generateClientToken();

        if (UserActivity.getUserId().equals("")) {
            idClient = "6";
        } else {
            idClient = UserActivity.getUserId();
        }

        new ThankYouFragment.MakeOrder().execute();

        return view;
    }

    private String generateClientToken() {
        String token = "HealthCare_";

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long randomNumber = timestamp.getTime();
        token += String.valueOf(randomNumber);

        return token;

    }

    private void generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);

            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MakeOrder extends AsyncTask<String, String, String> {

        private int validate = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Trwa zamawianie wizyty");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("idService", idService)
                    .add("dateVisit", dateVisit)
                    .add("hourVisit", hour)
                    .add("payInAdvance", payInAdvance)
                    .add("idDoctor", idDoctor)
                    .add("idClient", idClient)
                    .add("clientToken", token)
                    .build();

            String url_make_order = "http://10.0.2.2/online_clinic/order_visit.php";
            Request request = new Request.Builder()
                    .url(url_make_order)
                    .post(postData)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();

                JSONObject jsonObject = new JSONObject(result);
                validate = jsonObject.getInt("success");


            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();

            if (validate == 1) {
                generateQRCode(token);
                TextView Token = view.findViewById(R.id.tv_token);
                Token.setText(token);

                TextView textView = view.findViewById(R.id.textView_thank_you_2);

                if (UserActivity.getUserId().equals("")) {
                    textView.setText("Jesteś niezalogowany zrób zrzut ekranu by zachować kod QR lub zapisz token");
                } else {
                    textView.setText("Podgląd wizyty znajduje się\nw zakładce \"Aktualne wizyty\"");
                }

            } else {
                System.out.println("Nie udało się zamówić");
            }
        }
    }

}
