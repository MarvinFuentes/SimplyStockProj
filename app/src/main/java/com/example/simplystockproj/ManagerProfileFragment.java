package com.example.simplystockproj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ManagerProfileFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    private TextView selectedProfileView;
    private int selectedUserId = -1;

    public ManagerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout profileListLayout = view.findViewById(R.id.existingProfileLinearLayout);

        Button deleteBtn = view.findViewById(R.id.mpDeleteBtn);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();

        //Query to get all of the saved profiles from 'users' table.
        cursor = db.rawQuery("SELECT id, first_name, last_name, email FROM users", null);

        //Loop through all of the profiles. Display the first name, last name, and email of the users.
        while(cursor.moveToNext()){
            int userId = cursor.getInt(0);
            String userFName = cursor.getString(1);
            String userLName = cursor.getString(2);
            String userEmail = cursor.getString(3);

            //Display all of the users names and email addresses
            TextView textView = new TextView(requireContext());
            textView.setText(userFName + " " + userLName + "\n" + userEmail);
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
                    //Remove old highlight if any
                    if(selectedProfileView != null){
                        selectedProfileView.setBackground(null);
                    }

                    //Highlight the selected profile
                    selectedProfileView = (TextView) v;
                    selectedUserId = userId;

                    selectedProfileView.setBackgroundResource(android.R.color.darker_gray);
                }
            });

            profileListLayout.addView(textView);
        }

        cursor.close();
        db.close();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUserId == -1){
                    Toast.makeText(requireContext(), "Select a profile first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedUserId == ((ManagerActivity) requireActivity()).getUserId()){
                    Toast.makeText(requireContext(), "Cannot delete your own profile", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean deleted = dbHelper.deleteUser(selectedUserId);

                if(deleted){
                    Toast.makeText(requireContext(), "Profile deleted!", Toast.LENGTH_SHORT).show();
                    //Refresh the Profile Management Fragment to reflect the deleted profile.
                    ((ManagerActivity) requireActivity()).replaceFragment(new ManagerProfileFragment(), false);
                }
                else{
                    Toast.makeText(requireContext(), "Failed to delete profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}