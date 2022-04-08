package com.example.gatepasssystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class forgot_Password extends AppCompatActivity {

    private EditText editTextTextEmailAddress2;
    private Button resetButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextTextEmailAddress2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        resetButton = (Button) findViewById(R.id.resetButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = editTextTextEmailAddress2.getText().toString().trim();

        if(email.isEmpty()){
            editTextTextEmailAddress2.setError("Email is required");
            editTextTextEmailAddress2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmailAddress2.setError("Enter a valid email");
            editTextTextEmailAddress2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(forgot_Password.this,"Check your mail to reset password",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(forgot_Password.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}