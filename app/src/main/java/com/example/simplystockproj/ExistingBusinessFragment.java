package com.example.simplystockproj;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ExistingBusinessFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_existing_business, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout businessListLayout = view.findViewById(R.id.existingBusinessLinearLayout);
        Button backButton = view.findViewById(R.id.ebBackBtn);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();

        //Query to get all of the saved businesses from 'organizations' table.
        cursor = db.rawQuery("SELECT business_id, business_name, city, state, pin, manager_pin FROM organizations", null);

        //Loop through all of the organizations. Display the name, city, and state.
        while(cursor.moveToNext()){
            int businessId = cursor.getInt(0);
            String businessName = cursor.getString(1);
            String businessCity = cursor.getString(2);
            String businessState = cursor.getString(3);
            String businessPin = cursor.getString(4);
            String managerPin = cursor.getString(5);

            //Display the Business name with the city and state.
            TextView textView = new TextView(requireContext());
            textView.setText(businessName + "\n" + businessCity + ", " + businessState);
            textView.setTextSize(22);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 28, 0, 28);

            LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            textView.setLayoutParams(parameters);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBusinessPinDialog(businessId, businessName, businessPin, managerPin);
                }
            });

            businessListLayout.addView(textView);
        }

        cursor.close();
        db.close();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take the user back to the welcoming menu
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void showBusinessPinDialog(int businessId, String businessName, String businessPin, String managerPin){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter the business PIN.");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPin = input.getText().toString().trim();

                if(enteredPin.equals(businessPin)){
                    Toast.makeText(requireContext(), "PIN entered correctly", Toast.LENGTH_SHORT).show();

                    //Pass the arguments when the user clicks on a existing business
                    //Store the ID, name, & Pin of the business in keys -> 'BUSINESS_ID'
                    Bundle bundle = new Bundle();
                    bundle.putInt("BUSINESS_ID", businessId);
                    bundle.putString("BUSINESS_NAME", businessName);
                    bundle.putString("PIN", businessPin);
                    bundle.putString("MANAGER_PIN", managerPin);

                    //Role selection fragment
                    //Attach the bundle of arguments to RoleSelectionFragment using .setArguments()
                    RoleSelectionFragment fragment = new RoleSelectionFragment();
                    fragment.setArguments(bundle);

                    //Load the new RoleSelectionFragment along with the data of the business
                    ((WelcomingActivity) requireActivity()).replaceFragment(fragment, true);
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