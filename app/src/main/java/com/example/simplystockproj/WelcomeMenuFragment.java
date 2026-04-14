package com.example.simplystockproj;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeMenuFragment extends Fragment {

    public WelcomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button existingBusinessBtn = view.findViewById(R.id.existingBusinessBtn);
        Button newBusinessBtn = view.findViewById(R.id.newBusinessBtn);

        existingBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Existing business Fragment
                ((WelcomingActivity) requireActivity()).replaceFragment(new ExistingBusinessFragment(), true);
            }
        });

        newBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New business fragment
                ((WelcomingActivity) requireActivity()).replaceFragment(new NewBusinessFragment(), true);
            }
        });
    }
}