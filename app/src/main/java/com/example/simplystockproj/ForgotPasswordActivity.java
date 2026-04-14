package com.example.simplystockproj;

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

public class ForgotPasswordActivity extends AppCompatActivity {
    Button resetPassBtn, backBtn;
    EditText emailText, newPassText, newConfPassText;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        dbHelper = new Database(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resetPassBtn = (Button) findViewById(R.id.fpResetPasswordBtn);
        backBtn = (Button) findViewById(R.id.fpBackBtn);

        emailText = (EditText) findViewById(R.id.fpEditTextTextEmailAddress2);
        newPassText = (EditText) findViewById(R.id.fpEditTextTextPassword4);
        newConfPassText = (EditText) findViewById(R.id.fpEditTextTextPassword5);

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String password = newPassText.getText().toString().trim();
                String confPassword = newConfPassText.getText().toString().trim();

                //Email validation
                if(email.isEmpty()){
                    emailText.setError("Email is required");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Enter a valid email");
                    return;
                }

                //Check to see if the email is truly linked to a user
                if(!dbHelper.ifEmailExists(email)){
                    emailText.setError("No account found with this email");
                    return;
                }

                //Password Requirements
                if(password.isEmpty()){
                    newPassText.setError("Password is required");
                    return;
                }
                if(password.length() < 8){
                    newPassText.setError("Password must be at least 8 characters");
                    return;
                }
                if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%?&]).{8,}$")){
                    newPassText.setError("Password must include at least 1 uppercase, 1 lowercase, 1 digit, and 1 symbol: #@$!%?&");
                    return;
                }

                //Check to see if the old password matches the new password the user wants to create
                User user = dbHelper.getUserByEmail(email);
                if(user != null && user.getPassword().equals(password)){
                    newPassText.setError("New password must be different");
                    return;
                }

                //Confirming the new password matches
                if(confPassword.isEmpty()){
                    newConfPassText.setError("Confirm your password");
                    return;
                }
                //Check to see if new password is matching with confirming password.
                if(!password.equals(confPassword)){
                    newConfPassText.setError("Password does not match");
                    return;
                }

                if(dbHelper.updatePassword(email, password)){
                    Toast.makeText(ForgotPasswordActivity.this, "Password updated", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Update failed. Try again later", Toast.LENGTH_LONG).show();
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