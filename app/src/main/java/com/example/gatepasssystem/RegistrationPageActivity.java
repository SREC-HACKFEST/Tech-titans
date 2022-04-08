package com.example.gatepasssystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView registerUserbutton;
    private EditText nameUser,rollNumberUser,departmentUser,emailAddressUser,passwordUser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        mAuth = FirebaseAuth.getInstance();

        registerUserbutton = (Button) findViewById(R.id.registerUserbutton);
        registerUserbutton.setOnClickListener(this);

        nameUser = (EditText) findViewById(R.id.nameUser);
        rollNumberUser = (EditText) findViewById(R.id.rollNumberUser);
        departmentUser = (EditText) findViewById(R.id.departmentUser);
        emailAddressUser = (EditText) findViewById(R.id.emailAddressUser);
        passwordUser = (EditText) findViewById(R.id.passwordUser);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerUserbutton:
                registerUsercheck();
                break;
        }
    }

    private void registerUsercheck(){
        String fullname = nameUser.getText().toString().trim();
        String rollno = rollNumberUser.getText().toString().trim();
        String department = departmentUser.getText().toString().trim();
        String email = emailAddressUser.getText().toString().trim();
        String password = passwordUser.getText().toString().trim();

        if(fullname.isEmpty()){
            nameUser.setError("Full Name is required");
            nameUser.requestFocus();
            return;
        }

        if(rollno.isEmpty()){
            rollNumberUser.setError("Roll Number is required");
            rollNumberUser.requestFocus();
            return;
        }

        if(department.isEmpty()){
            departmentUser.setError("Department is required");
            departmentUser.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailAddressUser.setError("Email address is required");
            emailAddressUser.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAddressUser.setError("Enter a valid email address");
            emailAddressUser.requestFocus();
            return;
        }

        if(password.length() < 8){
            passwordUser.setError("Password should contain more than 7 characters");
            passwordUser.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(fullname,rollno,department,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegistrationPageActivity.this,"Successfully Registered",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(RegistrationPageActivity.this,"Failed to Register",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegistrationPageActivity.this,"Failed to Register",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}