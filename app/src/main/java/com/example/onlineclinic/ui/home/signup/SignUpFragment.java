package com.example.onlineclinic.ui.home.signup;
/*
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
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineclinic.R;
import com.example.onlineclinic.JsonParser;
import com.example.onlineclinic.ui.home.HomeFragment;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment
{
    private int SUCCESS ;

    private ProgressDialog pDialog;

    private EditText inputName;
    private EditText inputSecondName;
    private EditText inputSurname;
    private EditText inputEmail;
    private EditText inputPassword;

    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        inputName = view.findViewById(R.id.plainName);
        inputSecondName = view.findViewById(R.id.plainSecondName);
        inputSurname = view.findViewById(R.id.plainSurname);
        inputEmail = view.findViewById(R.id.plainMail);
        inputPassword = view.findViewById(R.id.plainPassword);

        Button signUp = view.findViewById(R.id.buttonSignUp);

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // creating new user in background thread
                new SignUpFragment.CreateNewUser().execute();
            }
        });

        return view;
    }

    class CreateNewUser extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.wait_sign_up));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {

            String name = inputName.getText().toString();
            String secondName = inputSecondName.getText().toString();
            String surname = inputSurname.getText().toString();
            String Email = inputEmail.getText().toString();
            String Password = inputPassword.getText().toString();

            OkHttpClient client = new OkHttpClient();

            RequestBody postData = new FormBody.Builder()
                    .add("name", name)
                    .add("name2", secondName)
                    .add("surname", surname)
                    .add("email", Email)
                    .add("password", Password)
                    .build();

            String url_create_product = "http://10.0.2.2/bayb/add_new_user.php";
            Request request = new Request.Builder()
                    .url(url_create_product)
                    .post(postData)
                    .build();
            try
            {
                Response response = client.newCall(request).execute();
                String result = Objects.requireNonNull(response.body()).string();
                JsonParser jsonParser = new JsonParser();
                final String parsedSignUp;
                parsedSignUp = jsonParser.parseSignUp(result);

                if(parsedSignUp.equals("1"))
                {
                    SUCCESS = 1;
                }
            }
            catch (Exception e)
            {
                SUCCESS = 0;
                System.out.println("Error: " + e);
            }
            return null;
        }
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (SUCCESS == 1)
                    {
                        //Display success message
                        Toast.makeText(getActivity(),
                                R.string.user_added, Toast.LENGTH_LONG).show();

                        HomeFragment homeFragment = new HomeFragment();

                        FragmentTransaction transaction =
                                Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSupportFragmentManager()).beginTransaction();
                        transaction.replace(R.id.fragment_sign_up, homeFragment);
                        transaction.addToBackStack(null).commit();

                    }
                    else
                    {
                        Toast.makeText(getActivity(),
                                R.string.some_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
*/