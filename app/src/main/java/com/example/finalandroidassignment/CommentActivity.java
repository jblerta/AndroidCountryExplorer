package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    protected static String cityID;
    private Window window;
    private Dialog signInDialog;
    private FirebaseFirestore firebaseFirestore;
    private EditText comment;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView=findViewById(R.id.recycler_view);
        comment=findViewById(R.id.commentTxt);

        firebaseFirestore=FirebaseFirestore.getInstance();
        cityID=getIntent().getStringExtra("CITY__ID");
        System.out.println("kl "+cityID);

        signInDialog=new Dialog(CommentActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);




        LinearLayoutManager layoutManager=new LinearLayoutManager(CommentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);



        layoutManager=new LinearLayoutManager(CommentActivity.this);
        CommentsAdapter commentsAdapter=new CommentsAdapter(DBqueries.commentModelList);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(commentsAdapter);


        if(DBqueries.commentModelList.size()==0){

            DBqueries.getComment(commentsAdapter,this);

        }else{
            commentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();

    }

    public void signIn(View view){
        signInDialog.dismiss();
        startActivity(new Intent(CommentActivity.this,LoginActivity.class));
    }

    public void signup(View view){
        signInDialog.dismiss();
        startActivity(new Intent(CommentActivity.this,RegisterActivity.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void writeComment(View view){
        if (currentUser != null) {

            // String name=firebaseFirestore.collection("USERS").document(currentUser.getUid()).get().getResult().getString("name");
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());


            Map<String, Object> commentData = new HashMap<>();
            commentData.put("comment", comment.getText().toString());
            commentData.put("userName", currentUser.getEmail());
            commentData.put("date", date);
            firebaseFirestore.collection("CITY").document(cityID).collection("COMMENTS").document().set(commentData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(CommentActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            signInDialog.show();
        }
    }
}