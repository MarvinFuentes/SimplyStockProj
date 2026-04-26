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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConsumptionDateFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    DateAdapter adapter;

    String filterType = "daily";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consumption_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView dateListView = view.findViewById(R.id.consDateListView);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();
        adapter = new DateAdapter();

        String query;
        int userId = -1;
        String userName = "";

        if(getArguments() != null){
            userId = getArguments().getInt("USER_ID");
            userName = getArguments().getString("USER_NAME");
            filterType = getArguments().getString("FILTER_TYPE", "daily");
        }

        String bizId = String.valueOf(((ManagerActivity) requireActivity()).getBusinessId());

        if(filterType.equals("monthly")){
            query =   "SELECT DISTINCT substr(date, 1, 7) "
                    + "FROM checkouts "
                    + "WHERE business_id = ? AND user_id = ? "
                    + "ORDER BY date DESC";
        }
        else if(filterType.equals("weekly")){
            query =   "SELECT DISTINCT strftime('%Y-W%W', date) "
                    + "FROM checkouts "
                    + "WHERE business_id = ? AND user_id = ? "
                    + "ORDER BY date DESC";
        }
        else{
            query =   "SELECT DISTINCT date "
                    + "FROM checkouts "
                    + "WHERE business_id = ? AND user_id = ? "
                    + "ORDER BY date DESC";
        }
        cursor = db.rawQuery(query, new String[]{bizId, String.valueOf(userId)});

        while(cursor.moveToNext()){
            String checkoutDate = cursor.getString(0);

            adapter.userIds.add(userId);
            adapter.userNames.add(userName);
            adapter.userDates.add(checkoutDate);
        }

        cursor.close();
        db.close();

        dateListView.setAdapter(adapter);
    }

    class DateAdapter extends BaseAdapter{
        ArrayList<Integer> userIds = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        ArrayList<String> userDates = new ArrayList<>();

        @Override
        public int getCount(){
            return userDates.size();
        }

        @Override
        public Object getItem(int position){
            return userDates.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                convertView = inflater.inflate(R.layout.consumption_date_row, parent, false);
            }

            TextView dateText = convertView.findViewById(R.id.cdrDates);
            String actualDate = userDates.get(position);

            if(filterType.equals("weekly")){
                dateText.setText(DateFormatter.weekly(actualDate));
            }
            else if(filterType.equals("monthly")){
                dateText.setText(DateFormatter.monthly(actualDate));
            }
            else{
                dateText.setText(DateFormatter.daily(actualDate));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Now I send the date,userId, userName, and businessId to the Chart fragment.
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", userIds.get(position));
                    bundle.putString("USER_NAME", userNames.get(position));
                    bundle.putString("CHECKOUT_DATE", userDates.get(position));
                    bundle.putString("FILTER_TYPE", filterType);
                    bundle.putInt("BUSINESS_ID", ((ManagerActivity) requireActivity()).getBusinessId());

                    ConsumptionChartFragment fragment = new ConsumptionChartFragment();
                    fragment.setArguments(bundle);

                    ((ManagerActivity) requireActivity()).replaceFragment(fragment, true);
                }
            });

            return convertView;
        }
    }
}