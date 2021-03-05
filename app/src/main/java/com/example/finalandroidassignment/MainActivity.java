package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

RecyclerView gridView;
    private Button logoutBtn,newCityBtn,loginBtn,signupBtn;
    FirebaseUser currentUser;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView=findViewById(R.id.recycler_view);
        logoutBtn=findViewById(R.id.logout);
        newCityBtn=findViewById(R.id.newCity);
        loginBtn=findViewById(R.id.signin);
        signupBtn=findViewById(R.id.signup);


        // show City's
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        layoutManager=new GridLayoutManager(MainActivity.this,2);
        final CityHomeAdapter cityHomeAdapter=new CityHomeAdapter(DBqueries.cityModelList);
        gridView.setLayoutManager(layoutManager);

        gridView.setAdapter(cityHomeAdapter);
        gridView.hasFixedSize();


        if(DBqueries.cityModelList.size()==0){

            DBqueries.getCityData(cityHomeAdapter,this);

        }else{
            cityHomeAdapter.notifyDataSetChanged();
        }





    }


    //Current User
    @Override
    protected void onStart() {
        super.onStart();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        //check if user is logged in or not to display or hide specific buttons
      if(currentUser!=null){
            logoutBtn.setVisibility(View.VISIBLE);
            newCityBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            signupBtn.setVisibility(View.GONE);
        }
        else{
            logoutBtn.setVisibility(View.GONE);
            newCityBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
            signupBtn.setVisibility(View.VISIBLE);
        }

    }







    public void SignIn(View view) {
        Intent addIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(addIntent);
    }

    public void Signup(View view) {
        Intent addIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(addIntent);
    }


    public void add(View view){
        Intent addIntent = new Intent(MainActivity.this, AddCityActivity.class);
        startActivity(addIntent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent main = new Intent(MainActivity.this, MainActivity.class);
        startActivity(main);
    }


  /*  MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem=item;

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        });


        currentUser= FirebaseAuth.getInstance().getCurrentUser();

     //   if (currentUser == null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();

                    switch (id) {
                        case R.id.nav_home:

                            invalidateOptionsMenu();
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            break;
                        case R.id.nav_edit:

                             startActivity(new Intent(MainActivity.this,EditCity.class));
                            break;
                        case R.id.nav_add:
                            startActivity(new Intent(MainActivity.this,AddCityActivity.class));
                            break;
                            case R.id.nav_logout: {
                            FirebaseAuth.getInstance().signOut();
                            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(registerIntent);
                            finish();
                        }
                    }
                }
            });
            return true;
       /* } else {
         //   signInDialog.show();
            return false;
        }*/

    }










