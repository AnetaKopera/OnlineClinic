package com.example.onlineclinic;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);
        ImageView img = view.findViewById(R.id.map);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.google.com/maps/place/Drewnowska+58,+90-001+Łódź/@51.7807603,19.4454322,17z/data=!3m1!4b1!4m5!3m4!1s0x471bcac4fa42bffd:0x3a7a0e19d3b484dd!8m2!3d51.7807603!4d19.4476209"));
                startActivity(intent);
            }
        });
        return view;
    }

}