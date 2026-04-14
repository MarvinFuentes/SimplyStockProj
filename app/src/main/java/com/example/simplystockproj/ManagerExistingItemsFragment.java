package com.example.simplystockproj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ManagerExistingItemsFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ExistingItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_existing_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView existingItemListView = view.findViewById(R.id.meiListView);
        Button checkInBtn = view.findViewById(R.id.meiCheckInBtn);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();
        adapter = new ExistingItemAdapter();

        String bizId = String.valueOf(((ManagerActivity) requireActivity()). getBusinessId());

        //Query to get all of the saved items from 'items' table
        //I use the business ID to keep it all organized
        cursor = db.rawQuery("SELECT item_id, category, description, availability, image_uri FROM items WHERE business_id = ?",
                new String[]{bizId}
        );

        while(cursor.moveToNext()){
            int itemId = cursor.getInt(0);
            String category = cursor.getString(1);
            String description = cursor.getString(2);
            int availability = cursor.getInt(3);
            String imageUri = cursor.getString(4);

            adapter.addItem(new ExistingItem(itemId, category, description, availability, imageUri, 0));
        }

        cursor.close();
        db.close();

        existingItemListView.setAdapter(adapter);

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add more quantity to the inventory from the existing items list
                boolean updateAnyItem = false;

                for(int i = 0; i < adapter.getCount(); i++){
                    ExistingItem item = (ExistingItem) adapter.getItem(i);
                    int amountToAdd = item.getAmountToAdd();

                    if(amountToAdd > 0){
                        int newAvailability = item.getAvailability() + amountToAdd;

                        boolean updated = dbHelper.updateItemAvailability(item.getItemId(), newAvailability);

                        if(updated){
                            item.setAvailability(newAvailability);
                            item.setAmountToAdd(0);
                            updateAnyItem = true;
                        }
                    }
                }

                if(updateAnyItem){
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Inventory checked in successfully.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(requireContext(), "Enter an amount for at least one item.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Custom adapter
    class ExistingItemAdapter extends BaseAdapter {
        //Each Item equals a row
        ArrayList<ExistingItem> items = new ArrayList<>();

        //The amount of rows that should be displayed.
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExistingItemView existingItemView;

            if (convertView == null) {
                existingItemView = new ExistingItemView(requireContext());
            } else {
                existingItemView = (ExistingItemView) convertView;
            }

            ExistingItem existingItem = items.get(position);
            existingItemView.setCategory(existingItem.getCategory());
            existingItemView.setDescription(existingItem.getDescription());
            existingItemView.setAvailability(existingItem.getAvailability());
            existingItemView.setImage(existingItem.getImageUri());

            if(existingItem.getAmountToAdd() == 0){
                existingItemView.setAmountEntered("");
            }
            else{
                existingItemView.setAmountEntered(String.valueOf(existingItem.getAmountToAdd()));
            }

            existingItemView.getAmountEditText().addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String amountText = s.toString().trim();

                    if(amountText.isEmpty()){
                        existingItem.setAmountToAdd(0);
                    }
                    else{
                        try{
                            existingItem.setAmountToAdd(Integer.parseInt(amountText));
                        }
                        catch(NumberFormatException e){
                            existingItem.setAmountToAdd(0);
                        }
                    }
                }
            });

            return existingItemView;
        }

        public void addItem(ExistingItem item){
            items.add(item);
        }
    }
}