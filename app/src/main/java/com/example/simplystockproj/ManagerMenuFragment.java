package com.example.simplystockproj;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ManagerMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button profileManagementBtn = view.findViewById(R.id.mmProfileManagement);
        Button createNewItemBtn = view.findViewById(R.id.mmNewItem);
        Button existingItemBtn = view.findViewById(R.id.mmExistingItems);
        Button compAnalyticsBtn = view.findViewById(R.id.mmConsAnalytics);
        Button roleSwapBtn = view.findViewById(R.id.mmChangeRole);

        profileManagementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Existing Profile Fragment
                ((ManagerActivity) requireActivity()).replaceFragment(new ManagerProfileFragment(), true);
            }
        });

        createNewItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Item Fragment
                ((ManagerActivity) requireActivity()).replaceFragment(new ManagerNewItemFragment(), true);
            }
        });

        existingItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Existing Items Fragment
                ((ManagerActivity) requireActivity()).replaceFragment(new ManagerExistingItemsFragment(), true);
            }
        });

        compAnalyticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consumption analytics -First the manger chooses the user -Then they select the date they want to check.
                ((ManagerActivity) requireActivity()).replaceFragment(new ConsumptionUserFragment(), true);
            }
        });

        roleSwapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((ManagerActivity) requireActivity()).replaceFragment(new EmployeeInventoryFragment(), true);
                Intent intent = new Intent(requireContext(), EmployeeActivity.class);
                intent.putExtra("BUSINESS_ID", ((ManagerActivity) requireActivity()).getBusinessId());
                intent.putExtra("BUSINESS_NAME", ((ManagerActivity) requireActivity()).getBusinessName());
                intent.putExtra("USER_ID", ((ManagerActivity) requireActivity()).getUserId());

                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}