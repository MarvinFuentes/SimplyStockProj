package com.example.simplystockproj;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewBusinessFragment extends Fragment {
    Database dbHelper;

    public NewBusinessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_business, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new Database(requireContext());

        Button backButton = view.findViewById(R.id.nbBackBtn);
        Button newBusiness = view.findViewById(R.id.nbNewBusinessBtn);

        EditText businessNameText = view.findViewById(R.id.nbBusinessNameText);
        EditText businessPinText = view.findViewById(R.id.nbBusinessPin);
        EditText businessManagerAccessText = view.findViewById(R.id.nbManagerPin);
        EditText businessCityText = view.findViewById(R.id.nbCityName);
        EditText businessStateText = view.findViewById(R.id.nbState);

        newBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessName = businessNameText.getText().toString().trim();
                String businessPin = businessPinText.getText().toString().trim();
                String managerPin = businessManagerAccessText.getText().toString().trim();
                String cityName = businessCityText.getText().toString().trim();
                String stateName = businessStateText.getText().toString().trim();

                if(businessName.isEmpty()){
                    businessNameText.setError("Business name is required");
                    return;
                }
                if(cityName.isEmpty()){
                    businessCityText.setError("City is required");
                    return;
                }
                if(stateName.isEmpty()){
                    businessStateText.setError("State is required");
                    return;
                }

                //Pin requirements - This pin is for access to the business or organization
                if(businessPin.isEmpty()){
                    businessCityText.setError("Pin is required");
                    return;
                }
                if(businessPin.length() < 4){
                    businessPinText.setError("Pin must be at least 4 digits");
                    return;
                }
                if(!businessPin.matches("\\d+")){
                    businessPinText.setError("Pin can only contain digits");
                    return;
                }

                //Pin requirements - This pin is for access to manager tools
                if(managerPin.isEmpty()){
                    businessCityText.setError("Pin is required");
                    return;
                }
                if(managerPin.length() < 4){
                    businessPinText.setError("Pin must be at least 4 digits");
                    return;
                }
                if(!managerPin.matches("\\d+")){
                    businessPinText.setError("Pin can only contain digits");
                    return;
                }

                // Check to see if the state the user entered is a valid state
                // This feature is a maybe. I might add it on later

                int ownerId = ((WelcomingActivity) requireActivity()).getUserId();

                if(dbHelper.createNewBusiness(ownerId, businessName, businessPin, managerPin, cityName, stateName)){
                    Toast.makeText(requireContext(), "Business created successfully!", Toast.LENGTH_SHORT).show();
                    //Take the user back to the welcoming page to select their newly created business
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    Toast.makeText(requireContext(), "Business creation failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take the user back to the welcoming page
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}