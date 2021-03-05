package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText userEmail,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail=findViewById(R.id.emailLogin);
        userPassword=findViewById(R.id.passwordLogin);


        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void login(View view){

     firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString())
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         startActivity(new Intent(LoginActivity.this,MainActivity.class));

                     }else{
                         String error=task.getException().getMessage();
                         Toast.makeText(LoginActivity.this
                                            ,error, Toast.LENGTH_SHORT).show();
                     }
                 }
             });
    }

    public void signup(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
}