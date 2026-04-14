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
import android.widget.TextView;

import java.util.ArrayList;

public class ConsumptionUserFragment extends Fragment {
    Database dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consumption_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView userListView = view.findViewById(R.id.consUserlistView);

        dbHelper = new Database(requireContext());
        db = dbHelper.getReadableDatabase();
        adapter = new UserAdapter();

        String bizId = String.valueOf(((ManagerActivity) requireActivity()).getBusinessId());

        cursor = db.rawQuery( "SELECT DISTINCT users.id, users.first_name, users.last_name "
                                + "FROM checkouts "
                                + "JOIN users ON checkouts.user_id = users.id "
                                + "WHERE checkouts.business_id = ?",
                                new String[]{bizId});

        while (cursor.moveToNext()){
            int userId = cursor.getInt(0);
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);

            adapter.userIds.add(userId);
            adapter.userNames.add(firstName + " " + lastName);
        }

        cursor.close();
        db.close();

        userListView.setAdapter(adapter);
        }

    class UserAdapter extends BaseAdapter{
        ArrayList<Integer> userIds = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();

        @Override
        public int getCount(){
            return userNames.size();
        }

        @Override
        public Object getItem(int position){
            return userNames.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                convertView = inflater.inflate(R.layout.consumption_user_row, parent, false);
            }

            TextView nameText = convertView.findViewById(R.id.curUserName);
            nameText.setText(userNames.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Take the selected user info (ID & Name) and send it to the next fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", userIds.get(position));
                    bundle.putString("USER_NAME", userNames.get(position));

                    ConsumptionDateFragment fragment = new ConsumptionDateFragment();
                    fragment.setArguments(bundle);

                    ((ManagerActivity) requireActivity()).replaceFragment(fragment, true);
                }
            });

            return convertView;
        }
    }
}