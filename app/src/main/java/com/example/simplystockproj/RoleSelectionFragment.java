package com.example.simplystockproj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RoleSelectionFragment extends Fragment {
    private int businessId;
    private String businessName;
    private String managerPin;

    public RoleSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_role_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button employeeBtn = view.findViewById(R.id.rsEmployee);
        Button managerBtn = view.findViewById(R.id.rsManager);
        Button backBtn = view.findViewById(R.id.rsBackBtn);

        //Retrieve the values from ExistingBusinessFragment using getArguments()
        if(getArguments() != null){
            businessId = getArguments().getInt("BUSINESS_ID");
            businessName = getArguments().getString("BUSINESS_NAME");
            managerPin = getArguments().getString("MANAGER_PIN");
        }

        employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EmployeeActivity.class);
                intent.putExtra("BUSINESS_ID", businessId);
                intent.putExtra("BUSINESS_NAME", businessName);
                intent.putExtra("USER_ID", ((WelcomingActivity)requireActivity()).getUserId());
                startActivity(intent);
            }
        });

        managerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take the user back to the welcoming menu
                //In Welcoming.java the method replaceFragment() directing the user back to the
                //WelcomingMenuFragment without stacking fragments -> false -> addToBackStack
                ((WelcomingActivity) requireActivity()).replaceFragment(new WelcomeMenuFragment(), false);
            }
        });
    }

    public void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter the managerial PIN.");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPin = input.getText().toString().trim();

                //Check to see if the PIN matches the one in the database
                if(enteredPin.equals(managerPin)){
                    Toast.makeText(requireContext(), "PIN entered correctly", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(requireContext(), ManagerActivity.class);
                    intent.putExtra("BUSINESS_ID", businessId);
                    intent.putExtra("BUSINESS_NAME", businessName);
                    intent.putExtra("USER_ID", ((WelcomingActivity)requireActivity()).getUserId());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(requireContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}