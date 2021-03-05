package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private EditText userName,userEmail,userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName=findViewById(R.id.name);
        userEmail=findViewById(R.id.email);
        userPassword=findViewById(R.id.passwordLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    public void signup(View view){



            // Drawable customErrorIcon=getResources().getDrawable();
            //  customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

            if(userEmail.getText().toString().matches(emailPattern)){
                if(userPassword.getText().length()>8){


                    firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                        Map<String, Object> userdata = new HashMap<>();
                                        userdata.put("name",userName.getText().toString());

                                        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                                .set(userdata);

                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                                    }
                                    else{
                                        String error=task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                }else{
                    userPassword.setError("Password must be greater than 8!");
                }
            }else{
                userEmail.setError("Invalid email!");

            }

    }
    public void login(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }
}