package com.example.onlineclinic.ui.myprofile;

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

import com.example.onlineclinic.R;
import com.example.onlineclinic.UserActivity;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordFragment extends Fragment {
    private ProgressDialog pDialog;
    private int validate;

    private EditText plainOldPass;
    private EditText plainNewPass;
    private EditText plainConfirm;

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        plainOldPass = view.findViewById(R.id.plainOldPass);
        plainNewPass = view.findViewById(R.id.plainNewPass);
        plainConfirm = view.findViewById(R.id.plainConfirm);

        Button change = view.findViewById(R.id.pass_change_button);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePasswordFragment.changePassword().execute();
            }
        });

        return view;
    }

    class changePassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Trwa aktualizowanie profilu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String oldPassword = plainOldPass.getText().toString();
            String newPassword = plainNewPass.getText().toString();
            String confirmPassword = plainConfirm.getText().toString();

            String ID = UserActivity.getUserId();

            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("id", ID)
                    .add("password_old", oldPassword)
                    .add("password_new", newPassword)
                    .add("password_new2", confirmPassword)
                    .build();

            String url_pass = "http://10.0.2.2/online_clinic/update_password.php";
            Request request = new Request.Builder()
                    .url(url_pass)
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

        protected void onPostExecute(String s) {
            requireActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (validate == 0) {
                        Toast.makeText(getActivity(), "Złe hasło", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Hasło zmienione", Toast.LENGTH_LONG).show();

                        plainOldPass.setText("");
                        plainNewPass.setText("");
                        plainConfirm.setText("");
                    }

                }
            });

            pDialog.dismiss();
        }
    }
}
