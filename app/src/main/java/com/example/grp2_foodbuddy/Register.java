package com.example.grp2_foodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText email, password, conpassword;
    Button signupAccount;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);

        signupAccount = findViewById(R.id.signupAccount);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        signupAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {

        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();
        String conpasswordVal = conpassword.getText().toString();

        if (!emailVal.matches(emailPattern)) {
            email.setError("Invalid Email");
        } else if (passwordVal.isEmpty() || passwordVal.length() < 6) {
            password.setError("Ensure password has length of more than 6");
        } else if (!passwordVal.equals(conpasswordVal)) {
            conpassword.setError("Password Mismatch");
        } else {
            mAuth.createUserWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendUserToDashboard();
                        Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "" + task.getException(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void sendUserToDashboard() {
        Intent intent = new Intent(Register.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}