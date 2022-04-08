package com.example.gatepasssystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView newUser,forgotPassword;
    private EditText editTextTextEmailAddress,editTextTextPassword;
    private Button loginbutton;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newUser = (TextView) findViewById(R.id.newUser);
        newUser.setOnClickListener((View.OnClickListener) this);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener((View.OnClickListener) this);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(this);

        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newUser:
                startActivity(new Intent(this,RegistrationPageActivity.class));
                break;

            case R.id.loginbutton:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,forgot_Password.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextTextEmailAddress.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextTextEmailAddress.setError("Email is required");
            editTextTextEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmailAddress.setError("Enter a valid mail Id");
            editTextTextEmailAddress.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextTextPassword.setError("Password is required");
            editTextTextPassword.requestFocus();
            return;
        }
        if(password.length() < 8){
            editTextTextPassword.setError("Enter a valid password");
            editTextTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.isEmailVerified()){
                    startActivity(new Intent(MainActivity.this,Profile_Activity.class));
                }else{
                    user.sendEmailVerification();
                    startActivity(new Intent(MainActivity.this,Email_Verification.class));
                }
                }else{
                    Toast.makeText(MainActivity.this,"Failed to login Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}