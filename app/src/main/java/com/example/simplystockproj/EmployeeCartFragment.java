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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EmployeeCartFragment extends Fragment {
    Database dbHelper;
    CartItemAdapter adapter;

    public EmployeeCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView cartItemListView = view.findViewById(R.id.ecListView);

        dbHelper = new Database(requireContext());

        ArrayList<CartItem> cartItems = ((EmployeeActivity) requireActivity()).getCartItems();

        adapter = new CartItemAdapter(cartItems);
        cartItemListView.setAdapter(adapter);

        Button checkOutBtn = view.findViewById(R.id.ecConfirmBtn);

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CartItem> cartItems = ((EmployeeActivity) requireActivity()).getCartItems();

                if(cartItems.isEmpty()){
                    Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = ((EmployeeActivity) requireActivity()).getUserId();
                int bizId = ((EmployeeActivity) requireActivity()).getBusinessId();

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                //With this bool I make sure all checkout records and inventory update successfully.
                boolean allUpdatesSuccessful = true;

                for(CartItem cartItem : cartItems) {
                    boolean checkoutSaved = dbHelper.createCheckout(
                            userId,
                            bizId,
                            cartItem.getItemId(),
                            cartItem.getQuantitySelected(),
                            date
                    );

                    int newAvailability = cartItem.getAvailability() - cartItem.getQuantitySelected();

                    boolean itemUpdated = dbHelper.updateItemAvailability(cartItem.getItemId(), newAvailability);

                    if (!checkoutSaved || !itemUpdated) {
                        allUpdatesSuccessful = false;
                        break;
                    }
                }

                if(allUpdatesSuccessful){
                    cartItems.clear();
                    //Refresh the listView, since the data changed
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Checkout complete", Toast.LENGTH_SHORT).show();

                    //Take back the user to the newly updated inventory
                    ((EmployeeActivity) requireActivity()).replaceFragment(new EmployeeInventoryFragment(), false);
                }
                else{
                    Toast.makeText(requireContext(), "Checkout failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Custom adapter
    class CartItemAdapter extends BaseAdapter{
        ArrayList<CartItem> items;

        public CartItemAdapter(ArrayList<CartItem> items){
            this.items = items;
        }

        //This method helps Android Studio know how many rows are being displayed
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

        //This method is called for every row that needs to get displayed
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartItemView cartItemView;

            if(convertView == null){
                cartItemView =new CartItemView(requireContext());
            }
            else{
                cartItemView = (CartItemView) convertView;
            }

            CartItem cartItem = items.get(position);
            cartItemView.setCategory(cartItem.getCategory());
            cartItemView.setDescription(cartItem.getDescription());
            cartItemView.setQuantity(cartItem.getQuantitySelected());
            cartItemView.setImage(cartItem.getImageUri());

            cartItemView.getAddBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cartItem.getQuantitySelected() < cartItem.getAvailability()){
                        cartItem.setQuantitySelected(cartItem.getQuantitySelected() + 1);
                        //notifies the ListView the data inside of the adapter has changed.
                        //It refreshes the list rows
                        notifyDataSetChanged();
                    }
                }
            });

            cartItemView.getSubBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cartItem.getQuantitySelected() > 1){
                        cartItem.setQuantitySelected(cartItem.getQuantitySelected() - 1);
                    }
                    else{
                        items.remove(position);
                    }
                    //notifies the ListView the data inside of the adapter has changed.
                    //It refreshes the list rows
                    notifyDataSetChanged();
                }
            });

            return cartItemView;
        }
    }
}