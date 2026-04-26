package com.example.simplystockproj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static String daily(String dateKey){
        try{
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

            Date date = input.parse(dateKey);
            return output.format(date);
        }catch(Exception e){
            return dateKey;
        }
    }

    public static String weekly(String weekKey){
        try{
            String [] parts = weekKey.split("-W");
            int year = Integer.parseInt(parts[0]);
            int week = Integer.parseInt(parts[1]);

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.WEEK_OF_YEAR, week);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            Date startDate = cal.getTime();

            cal.add(Calendar.DAY_OF_WEEK, 6);
            Date endDate = cal.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

            return sdf.format(startDate) + "  |  " + sdf.format(endDate);
        }catch(Exception e){
            return weekKey;
        }
    }

    public static String monthly(String monthKey){
        try{
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

            Date date = input.parse(monthKey);
            return output.format(date);
        }catch(Exception e){
            return monthKey;
        }
    }
}
