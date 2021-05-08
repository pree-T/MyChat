package com.example.mychat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView signup;
    EditText lemail,lpassword;
    TextView signin;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        signup=findViewById(R.id.signup1);
        lemail=findViewById(R.id.email);
        lpassword=findViewById(R.id.pass);
        signin=findViewById(R.id.signin1);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=lemail.getText().toString().trim();
                String password=lpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login.this,"Enter Valid Data",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.matches(emailPattern)))
                {
                    lemail.setError("Invalid Email");
                    Toast.makeText(Login.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6)
                {
                    lpassword.setError("Password length smaller than 6 characters");
                    Toast.makeText(Login.this,"Please enter valid password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                startActivity( new Intent(Login.this,Home.class));
                            }
                            else
                            {
                                Toast.makeText(Login.this,"Error in Login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

                });

        signup.setOnClickListener(v -> startActivity(new Intent(Login.this, Registration.class)));
    }
    @Override
    public void onBackPressed(){

        moveTaskToBack(true);

    }
}