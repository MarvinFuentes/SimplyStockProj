package com.example.simplystockproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {
    private int userId;
    private int businessId;
    private String businessName;
    private ArrayList<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getIntExtra("USER_ID", -1);
        businessId = getIntent().getIntExtra("BUSINESS_ID", -1);
        businessName = getIntent().getStringExtra("BUSINESS_NAME");

        if(userId == -1 || businessId == -1 || businessName == null){
            finish();
            return;
        }

        //load the menu the first time only
        if(savedInstanceState == null){
            replaceFragment(new EmployeeInventoryFragment(), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.eHomeBtn){
            replaceFragment(new EmployeeInventoryFragment(), false);
        }
        else if(itemId == R.id.eWelcomingBtn){
            Intent intent = new Intent(EmployeeActivity.this, WelcomingActivity.class);
            intent.putExtra("USER_ID", getIntent().getIntExtra("USER_ID", -1));
            startActivity(intent);
            finish();
        }
        else if(itemId == R.id.eCartBtn){
            //take the user to the cart menu
            replaceFragment(new EmployeeCartFragment(), true);
        }
        else if(itemId == R.id.eLogOutBtn){
            Intent intent = new Intent(EmployeeActivity.this, MainActivity.class);
            //This line ensures the activity stack is cleared and prevents users
            //from using the back button and returning
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public int getUserId() {
        return userId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView3, fragment);

        //Users are allowed to press back and return to the previous fragment
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        //Replaces the fragment permanently (returns to the last activity)
        fragmentTransaction.commit();
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }
}