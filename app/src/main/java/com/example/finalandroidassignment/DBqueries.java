package com.example.finalandroidassignment;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CityModel> cityModelList = new ArrayList<>();
    public static List<CommentModel> commentModelList = new ArrayList<>();


    public static void getCityData(final CityHomeAdapter cityHomeAdapter, final Context context) {
        firebaseFirestore.collection("CITY").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                cityModelList.add(new CityModel(documentSnapshot.getId(), documentSnapshot.get("name").toString(), documentSnapshot.get("icon").toString()));

                            }
                            cityHomeAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

        public static void getComment(final CommentsAdapter commentsAdapter, final Context context) {
            firebaseFirestore.collection("CITY").document(CommentActivity.cityID).collection("COMMENTS").orderBy("date", Query.Direction.ASCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    System.out.println("jkjk+ "+CityActivity.cityID);
                                    commentModelList.add(new CommentModel(documentSnapshot.get("userName").toString(), documentSnapshot.get("comment").toString(), documentSnapshot.get("date").toString()));
                                }
                                commentsAdapter.notifyDataSetChanged();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


        }
    }
