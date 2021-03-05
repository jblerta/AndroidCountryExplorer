package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditCity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 123;
    private Button btnImg, upload,upload2,upload3;
    public ImageView img1;
    protected static String cityID;
    private EditText name, info, firstFragment, secondFragment,thirdFragment, longitude, latitude;
    private Uri filePath;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);
        img1 = findViewById(R.id.image1);

        cityID=getIntent().getStringExtra("CITY_ID");
        btnImg = findViewById(R.id.img1Btn);
        upload = findViewById(R.id.upload);
        name=findViewById(R.id.name_city);
        info=findViewById(R.id.info_city);
        firstFragment=findViewById(R.id.firstFr);
        secondFragment=findViewById(R.id.secondFr);
        thirdFragment=findViewById(R.id.thirdFr);
        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);

        upload2=findViewById(R.id.upload2);
        upload3=findViewById(R.id.upload3);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //select image
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage1();
            }
        });

        upload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage2();
            }
        });

    }

    public void add(View view){
        Map<String, Object> cityData=new HashMap<>();
        cityData.put("name",name.getText().toString());
        cityData.put("info",info.getText().toString());
        cityData.put("firstFragment",firstFragment.getText().toString());
        cityData.put("secondFragment",secondFragment.getText().toString());
        cityData.put("thirdFragment",thirdFragment.getText().toString());
        cityData.put("longitude",longitude.getText().toString());
        cityData.put("latitude",latitude.getText().toString());
        firebaseFirestore.collection("CITY").document(name.getText().toString().toUpperCase()).update(cityData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditCity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                }
                else{
                    String error=task.getException().getMessage();
                    Toast.makeText(EditCity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Select Image method
    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {// Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), filePath);
                img1.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    //upload first image
    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            name.getText().toString()+"_"
                                    + UUID.randomUUID().toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downloadURL = "https://" + task.getResult().getEncodedAuthority()
                                    + task.getResult().getEncodedPath()
                                    + "?alt=media&token="
                                    + task.getResult().getQueryParameters("token").get(0);
                            Toast.makeText(EditCity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                            //save your downloadURL
                            firebaseFirestore.collection("CITY").document(name.getText().toString().toUpperCase())
                                    .update(
                                            "icon",downloadURL
                                    );
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(EditCity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                }
            });

        }

    }

    //upload second image
    private void uploadImage1() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            name.getText().toString()+"_"
                                    + UUID.randomUUID().toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downloadURL = "https://" + task.getResult().getEncodedAuthority()
                                    + task.getResult().getEncodedPath()
                                    + "?alt=media&token="
                                    + task.getResult().getQueryParameters("token").get(0);
                            Toast.makeText(EditCity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                            //save your downloadURL
                            firebaseFirestore.collection("CITY").document(name.getText().toString().toUpperCase())
                                    .update(
                                            "img1",downloadURL
                                    );
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(EditCity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                }
            });

        }

    }

    //upload third image
    private void uploadImage2() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            name.getText().toString()+"_"
                                    + UUID.randomUUID().toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downloadURL = "https://" + task.getResult().getEncodedAuthority()
                                    + task.getResult().getEncodedPath()
                                    + "?alt=media&token="
                                    + task.getResult().getQueryParameters("token").get(0);
                            Toast.makeText(EditCity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                            //save your downloadURL
                            firebaseFirestore.collection("CITY").document(name.getText().toString().toUpperCase())
                                    .update(
                                            "img2",downloadURL
                                    );
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(EditCity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(
                        UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                }
            });

        }

    }
}