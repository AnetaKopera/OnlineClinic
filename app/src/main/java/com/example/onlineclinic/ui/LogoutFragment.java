package com.example.onlineclinic.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.onlineclinic.MainActivity;
import com.example.onlineclinic.UserActivity;

public class LogoutFragment extends Fragment
{
    public View onCreateView
            (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        UserActivity.setUserId("");

        SharedPrefs.saveData(getActivity(),"who_is_logged","Nobody");

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
        Toast.makeText(getActivity(), "Wylogowano", Toast.LENGTH_SHORT).show();

        return null;
    }
}