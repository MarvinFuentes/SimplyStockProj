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
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConsumptionChartFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String filterType = "daily";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consumption_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userDateText = view.findViewById(R.id.ccUserDateTextView);
        AnyChartView anyChartView = view.findViewById(R.id.ccAnyChartView);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();

        int userId = -1;
        int businessId = -1;
        String userName = "";
        String checkoutDate = "";

        if(getArguments() != null){
            userId = getArguments().getInt("USER_ID");
            businessId = getArguments().getInt("BUSINESS_ID");
            userName = getArguments().getString("USER_NAME");
            checkoutDate = getArguments().getString("CHECKOUT_DATE");
            filterType = getArguments().getString("FILTER_TYPE", "daily");
        }

        String displayDate;

        if(filterType.equals("weekly")){
            displayDate = DateFormatter.weekly(checkoutDate);
        }
        else if(filterType.equals("monthly")){
            displayDate = DateFormatter.monthly(checkoutDate);
        }
        else{
            displayDate = DateFormatter.daily(checkoutDate);
        }

        userDateText.setText(userName + "  |  " + displayDate);

        List<DataEntry> data = new ArrayList<>();

        String query;

        if(filterType.equals("weekly")){
            query = "SELECT items.description, SUM(checkouts.quantity) "
                    + "FROM checkouts "
                    + "JOIN items ON checkouts.item_id = items.item_id "
                    + "WHERE checkouts.business_id = ? "
                    + "AND checkouts.user_id = ? "
                    + "AND strftime('%Y-W%W', checkouts.date) = ? "
                    + "GROUP BY items.description";
        }
        else if(filterType.equals("monthly")){
            query = "SELECT items.description, SUM(checkouts.quantity) "
                    + "FROM checkouts "
                    + "JOIN items ON checkouts.item_id = items.item_id "
                    + "WHERE checkouts.business_id = ? "
                    + "AND checkouts.user_id = ? "
                    + "AND substr(checkouts.date, 1, 7) = ? "
                    + "GROUP BY items.description";
        }
        else{
            query = "SELECT items.description, SUM(checkouts.quantity) "
                    + "FROM checkouts "
                    + "JOIN items ON checkouts.item_id = items.item_id "
                    + "WHERE checkouts.business_id = ? "
                    + "AND checkouts.user_id = ? "
                    + "AND checkouts.date = ? "
                    + "GROUP BY items.description";
        }

        cursor = db.rawQuery(query, new String[]{String.valueOf(businessId), String.valueOf(userId), checkoutDate});

        while (cursor.moveToNext()){
            String itemDescription = cursor.getString(0);
            int totalQty = cursor.getInt(1);

            data.add(new ValueDataEntry(itemDescription, totalQty));
        }

        cursor.close();
        db.close();

        Cartesian cartesian = AnyChart.bar();

        cartesian.data(data);
        cartesian.title("Items Checked Out");
        cartesian.title().fontColor("#000000");

        cartesian.yScale().minimum(0d);

        //Labels on the bars
        cartesian.labels(true);
        cartesian.labels().fontColor("#000000");

        //Axis titles
        cartesian.yAxis(0).title("Quantity");
        cartesian.xAxis(0).title("Items");

        //Axis text color
        cartesian.yAxis(0).labels().fontColor("#000000");
        cartesian.xAxis(0).labels().fontColor("#000000");

        //Axis Titles color
        cartesian.yAxis(0).title().fontColor("#000000");
        cartesian.xAxis(0).title().fontColor("#000000");

        //The simply stock color for the bars
        cartesian.palette(new String[]{"#0770B3"});

        //Enables simple chart animation provided by Anychart library
        cartesian.animation(true);

        anyChartView.setChart(cartesian);
    }
}