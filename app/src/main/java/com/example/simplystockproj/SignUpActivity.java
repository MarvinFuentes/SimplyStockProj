package com.example.simplystockproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {
    Button signUpBtn, backBtn;
    EditText firstNameText, lastNameText, emailText, passwordText, confPasswordText;
    Database dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        dbHelper = new Database(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpBtn = (Button) findViewById(R.id.suSignUpBtn);
        backBtn = (Button) findViewById(R.id.suBackBtn);

        firstNameText = (EditText) findViewById(R.id.sEditTextText);
        lastNameText = (EditText) findViewById(R.id.sEditTextText2);
        emailText = (EditText) findViewById(R.id.sEditTextTextEmailAddress2);
        passwordText = (EditText) findViewById(R.id.sEditTextTextPassword3);
        confPasswordText = (EditText) findViewById(R.id.sEditTextTextPassword2);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameText.getText().toString().trim();
                String lastName = lastNameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String confPass = confPasswordText.getText().toString().trim();

                //Required First and Last name
                if(firstName.isEmpty()){
                    firstNameText.setError("First name is required");
                    return;
                }
                if(lastName.isEmpty()){
                    lastNameText.setError("Last name is required");
                    return;
                }

                //Email validation
                if(email.isEmpty()){
                    emailText.setError("Email is required");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Enter a valid email");
                    return;
                }

                //Password Requirements
                if(password.isEmpty()){
                    passwordText.setError("Password is required");
                    return;
                }
                if(password.length() < 8){
                    passwordText.setError("Password must be at least 8 characters");
                    return;
                }
                if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%?&]).{8,}$")){
                    passwordText.setError("Password must include at least 1 uppercase, 1 lowercase, 1 digit, and 1 symbol: #@$!%?&");
                    return;
                }

                //Confirming the password matches
                if(confPass.isEmpty()){
                    confPasswordText.setError("Confirm your password");
                    return;
                }
                if(!password.equals(confPass)){
                    confPasswordText.setError("Password does not match");
                    return;
                }

                //Check to see if the email already exists in the database
                if(dbHelper.ifEmailExists(email)){
                    emailText.setError("An account with this email already exists");
                    return;
                }

                if(dbHelper.createNewUser(firstName, lastName, email, password)){
                    Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Sign up failed. Try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}