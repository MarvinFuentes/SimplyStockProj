package com.example.simplystockproj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EmployeeInventoryFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView itemListView = view.findViewById(R.id.eiListView);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();
        adapter = new ItemAdapter();

        String businessId = String.valueOf(((EmployeeActivity) requireActivity()).getBusinessId());

        //Query to get all of the saved items from 'items' table
        //I use the business ID to keep it all organized
        cursor = db.rawQuery( "SELECT item_id, category, description, availability, image_uri, low_stock "
                                + "FROM items WHERE business_id = ? "
                                + "ORDER BY category ASC, description ASC",
                                new String[]{businessId});

        while(cursor.moveToNext()){
            int itemId = cursor.getInt(0);
            String category = cursor.getString(1);
            String description = cursor.getString(2);
            int availability = cursor.getInt(3);
            String imageUri = cursor.getString(4);
            int lowStock = cursor.getInt(5);


            adapter.addItem(new Item(itemId, category, description, availability, imageUri, lowStock));
        }

        cursor.close();
        db.close();

        itemListView.setAdapter(adapter);
    }

    //Custom adapter
    class ItemAdapter extends BaseAdapter{
        //Each Item equals a row
        ArrayList<Item> items = new ArrayList<Item>();

        //The amount of rows that should be displayed.
        @Override
        public int getCount(){
            return items.size();
        }

        @Override
        public Object getItem(int position){
            return items.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ItemView itemView;

            if(convertView == null){
                itemView = new ItemView(requireContext());
            }
            else{
                itemView = (ItemView) convertView;
            }

            Item item = items.get(position);
            itemView.setCategory(item.getCategory());
            itemView.setDescription(item.getDescription());
            itemView.setAvailability(item.getAvailability(), item.getLowStock());
            itemView.setImage(item.getImageUri());
            itemView.getAddButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add the item to the cart
                    ArrayList<CartItem> cartItems = ((EmployeeActivity) requireActivity()).getCartItems();

                    boolean ifInCart = false;

                    for(CartItem cartItem : cartItems){
                        if(cartItem.getItemId() == item.getItemId()){
                            if(cartItem.getQuantitySelected() < item.getAvailability()){
                                cartItem.setQuantitySelected(cartItem.getQuantitySelected() + 1);
                                Toast.makeText(requireContext(), "Item quantity updated in cart", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(requireContext(), "Cannot exceed available quantity", Toast.LENGTH_SHORT).show();
                            }
                            ifInCart = true;
                            break;
                        }
                    }

                    if(!ifInCart){
                        if(item.getAvailability() > 0){
                            cartItems.add(new CartItem(item.getItemId(), item.getCategory(), item.getDescription(), item.getAvailability(), item.getImageUri(), 1));
                            Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(requireContext(), "Item is out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            return itemView;
        }

        public void addItem(Item item){
            items.add(item);
        }
    }
}