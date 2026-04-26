package com.example.simplystockproj;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
        cursor = db.rawQuery( "SELECT item_id, category, description, availability, image_uri, url, low_stock "
                                + "FROM items WHERE business_id = ? "
                                + "ORDER BY category ASC, description ASC",
                                new String[]{bizId});

        while(cursor.moveToNext()){
            int itemId = cursor.getInt(0);
            String category = cursor.getString(1);
            String description = cursor.getString(2);
            int availability = cursor.getInt(3);
            String imageUri = cursor.getString(4);
            String url = cursor.getString(5);
            int lowStock = cursor.getInt(6);

            adapter.addItem(new ExistingItem(itemId, category, description, url, availability, imageUri, 0, lowStock));
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
            final int currentPosition = position;
            final ExistingItemView existingItemView;

            if (convertView == null) {
                existingItemView = new ExistingItemView(requireContext());
            } else {
                existingItemView = (ExistingItemView) convertView;
            }

            final ExistingItem existingItem = items.get(currentPosition);

            existingItemView.setCategory(existingItem.getCategory());
            existingItemView.setDescription(existingItem.getDescription());
            existingItemView.setAvailability(existingItem.getAvailability(), existingItem.getLowStock());
            existingItemView.setImage(existingItem.getImageUri());
            existingItemView.setUrl(existingItem.getUrl());
            existingItem.setLowStock(existingItem.getLowStock());

            existingItemView.getDeleteBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Delete Item");

                    //Had to create a separate dialog XML files because I was having issues setting the message text color to black.
                    //Instead of fighting the default styling, I created a custom layout and inflated it here.
                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_item, null);
                    builder.setView(dialogView);

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean deleted = dbHelper.deleteItem(existingItem.getItemId());

                            if(deleted){
                                items.remove(currentPosition);
                                notifyDataSetChanged();
                                Toast.makeText(requireContext(), "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(requireContext(), "Failed to delete item.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }
            });

            existingItemView.getEditBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Edit Item Details");

                    //Had to create a separate XML file due to the same issues I ran into with delete dialog.
                    //I was also having trouble with the ExitText hints not displaying properly,
                    //so I opted to use an XML layout to have better control over the inputs
                    //and provide a clearer UI for editing item details.
                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_item, null);

                    EditText categoryInputText = dialogView.findViewById(R.id.deiCategoryInput);
                    EditText descriptionInputText = dialogView.findViewById(R.id.deiDescriptionInput);
                    EditText urlInputText = dialogView.findViewById(R.id.deiUrlInput);
                    EditText lowStockText = dialogView.findViewById(R.id.deiLowStockInput);

                    //Pre-fill the fields with the existing item data so the user can edit instead of retyping everything.
                    categoryInputText.setText(existingItem.getCategory());
                    descriptionInputText.setText(existingItem.getDescription());
                    urlInputText.setText(existingItem.getUrl());
                    lowStockText.setText(String.valueOf(existingItem.getLowStock()));

                    builder.setView(dialogView);

                    //MUST set the PositiveButton to null so Android does NOT auto close the dialog.
                    //The default behavior kept closing dialog even if validation fails.
                    builder.setPositiveButton("Save", null);

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    //Create dialog instance so I can manually control button behavior
                    AlertDialog dialog = builder.create();

                    //This runs after dialog is shown - buttons exist at this point
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            //Safely grab the Save button
                            Button saveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                            saveBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String newCategory = categoryInputText.getText().toString().trim();
                                    String newDescription = descriptionInputText.getText().toString().trim();
                                    String newUrl = urlInputText.getText().toString().trim();
                                    String newLowStock = lowStockText.getText().toString().trim();

                                    if(newCategory.isEmpty()){
                                        categoryInputText.setError("Category is required");
                                        return;
                                    }

                                    if(newDescription.isEmpty()){
                                        descriptionInputText.setError("Description is required");
                                        return;
                                    }

                                    if(!newUrl.isEmpty() && !Patterns.WEB_URL.matcher(newUrl).matches()){
                                        urlInputText.setError("Enter a valid URL");
                                        return;
                                    }

                                    if(newLowStock.isEmpty()){
                                        lowStockText.setError("Must Enter quantity");
                                        return;
                                    }

                                    if(!newLowStock.matches("\\d+")){
                                        lowStockText.setError("Only digits can be entered");
                                        return;
                                    }

                                    boolean updated = dbHelper.updateItemInfo(existingItem.getItemId(), newCategory, newDescription, newUrl, Integer.parseInt(newLowStock));

                                    if (updated) {
                                        existingItem.setCategory(newCategory);
                                        existingItem.setDescription(newDescription);
                                        existingItem.setUrl(newUrl);
                                        existingItem.setLowStock(Integer.parseInt(newLowStock));

                                        notifyDataSetChanged(); //Refresh list
                                        Toast.makeText(requireContext(), "Item update successfully.", Toast.LENGTH_SHORT).show();

                                        dialog.dismiss(); //Only close dialog if update is successful
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to update item.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    //Show dialog not builder
                    dialog.show();
                }
            });

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