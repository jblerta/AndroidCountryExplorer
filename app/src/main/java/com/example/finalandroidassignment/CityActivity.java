package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CityActivity extends AppCompatActivity implements LocationListener {

    private TextToSpeech textToSpeech;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    protected static String cityID;
    private TextView cityName,cityInfo,firstFragment,secondFragment,txtspeak1,thirdFragment;
    private ImageView cityImage,img1,img2;
    private DocumentSnapshot documentSnapshot;
    private EditText comment;
    double latitude,longitude;
    LinearLayout linearLayout;
    private static final int LOCATION_PERM_REQ = 1234;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        cityName=findViewById(R.id.cityName);
        cityInfo=findViewById(R.id.cityInfo);
        cityImage=findViewById(R.id.imgCity);
        firstFragment=findViewById(R.id.textView5);
        secondFragment=findViewById(R.id.textView6);
        thirdFragment=findViewById(R.id.thirdFragment);
        img1=findViewById(R.id.image2);
        img2=findViewById(R.id.imgCity2);
        txtspeak1=findViewById(R.id.txtspeak1);
//      dialogSignInBtn=signInDialog.findViewById(R.id.signIn_btn);
//  dialogSignUpBtn=signInDialog.findViewById(R.id.signup_btn);
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                  int result= textToSpeech.setLanguage(Locale.ENGLISH);

                  if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                      Log.e("TTS","Language not supported");
                  }else{
                      txtspeak1.setEnabled(true);

                  }

                }else{
                    Log.e("TTS","Initialization failed");

                }
            }
        });



        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        //get id of the city tha has been clicked

        cityID=getIntent().getStringExtra("CITY_ID");


        firebaseFirestore.collection("CITY").document(cityID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshot = task.getResult();



                            Glide.with(getBaseContext()).load(documentSnapshot.get("icon")).apply(new RequestOptions().placeholder(R.drawable.back)).into(cityImage);
                            Glide.with(getBaseContext()).load(documentSnapshot.get("img1")).apply(new RequestOptions().placeholder(R.drawable.back)).into(img1);
                            Glide.with(getBaseContext()).load(documentSnapshot.get("img2")).apply(new RequestOptions().placeholder(R.drawable.back)).into(img2);


                            cityName.setText(documentSnapshot.get("name").toString());
                            cityInfo.setText(documentSnapshot.get("info").toString());
                            firstFragment.setText(documentSnapshot.get("firstFragment").toString());
                            secondFragment.setText(documentSnapshot.get("secondFragment").toString());
                            thirdFragment.setText(documentSnapshot.get("thirdFragment").toString());

                         } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(CityActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    public void speak1(View view){
            speak(cityInfo.getText().toString());
    }

    private void speak(String text){
       // String text=cityInfo.getText().toString();
       // textToSpeech.setPitch(50);
        //textToSpeech.setSpeechRate(50);
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();

    }


    public void openMap(View view){


        firebaseFirestore.collection("CITY").document(cityID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    longitude=Double.parseDouble(documentSnapshot.get("longitude").toString());
                    latitude=Double.parseDouble(documentSnapshot.get("latitude").toString());
                    Intent intent = new Intent(CityActivity.this,MapsActivity.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    startActivity(intent);
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(CityActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void writeComent(View view){

            Intent commentsIntent=new Intent(this,CommentActivity.class);
            commentsIntent.putExtra("CITY__ID",cityID);
            System.out.println("Jk "+cityID);
            startActivity(commentsIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //if (requestCode == LOCATION_PERM_REQ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat
                    .checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //}
        }
    }

    public void gps(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERM_REQ);
        }else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public void edit(View view){
        Intent intent=new Intent(this,EditCity.class);
        getIntent().putExtra("CITY_ID",cityID);
        startActivity(intent);
    }


}