package com.example.grp2_foodbuddy;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button newAccount;
    EditText email, password;
    Button loginAccount;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    User current_user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        newAccount = findViewById(R.id.newAccount);
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passwordLog);

        loginAccount = findViewById(R.id.loginAccount);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        newAccount.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Register.class)));
        loginAccount.setOnClickListener(v -> PerformLogin());
    }
    private void PerformLogin() {
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        if (!emailVal.matches(emailPattern)) {
            email.setError("Invalid Email");
        } else if (passwordVal.isEmpty() || passwordVal.length() < 6) {
            password.setError("Ensure password has length of more than 6");
        }
        else {
            mAuth.signInWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendUserToDashboard();
                    Toast.makeText(MainActivity.this, "Welcome to FoodBuddy!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "User not registered", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void sendUserToDashboard() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        String emailVal = email.getText().toString();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username", emailVal);
        startActivity(intent);
    }
}