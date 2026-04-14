package com.example.simplystockproj;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WelcomingActivity extends AppCompatActivity {
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcoming);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("USER_ID", -1);
        if(userId == -1){
            finish();
            return;
        }

        //load the menu the first time only
        if(savedInstanceState == null){
            replaceFragment(new WelcomeMenuFragment(), false);
        }
    }

    //getUserId() will always help whenever I need to fetch the user's ID
    public int getUserId(){
        return userId;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment);

        //Users are allowed to press back and return to the previous fragment
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        //Replaces the fragment permanently (returns to the last activity)
        fragmentTransaction.commit();
    }
}