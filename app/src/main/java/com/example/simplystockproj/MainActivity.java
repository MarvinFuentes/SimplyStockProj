package com.example.simplystockproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button signUpBtn, logInBtn;
    TextView forgotPassword;
    EditText emailText, passwordText;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new Database(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpBtn = (Button) findViewById(R.id.mSignUpBtn);
        logInBtn = (Button) findViewById(R.id.mLogInBtn);

        emailText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passwordText = (EditText) findViewById(R.id.editTextTextPassword);

        forgotPassword = (TextView) findViewById(R.id.forgotPassBtn);

        // Allowing the user to create an account first if they do not have an existing one.
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Check to see if their credentials match what the database has.
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                boolean ifEmailIsValid = emailCheck(email);
                boolean ifPasswordIsValid = passwordCheck(password);

                if(ifEmailIsValid && ifPasswordIsValid){
                    checkLogin(email, password);
                }
            }
        });

        // Send the user to the forgot password page to successfully change their password.
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean emailCheck(String email){
        if(email.isEmpty()){
            emailText.setError("Email is required");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    public boolean passwordCheck(String password){
        if(password.isEmpty()){
            passwordText.setError("Password is required");
            return false;
        }
        return true;
    }

    public void checkLogin(String email, String password){
        User user = dbHelper.getUserByEmail(email);

        if(user == null){
            emailText.setError("No account found with this email");
            return;
        }

        if(!user.getPassword().equals(password)){
            passwordText.setError("Incorrect password");
            return;
        }

        Intent intent = new Intent(MainActivity.this, WelcomingActivity.class);
        intent.putExtra("USER_ID", user.getId());
        startActivity(intent);
    }
}