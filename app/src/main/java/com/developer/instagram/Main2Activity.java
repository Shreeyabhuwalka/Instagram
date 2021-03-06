package com.developer.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.developer.instagram.Fragment.HomeFragment;
import com.developer.instagram.Fragment.NotificationFragment;
import com.developer.instagram.Fragment.ProfileFragment;
import com.developer.instagram.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener(){

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId())
                        {
                            case R.id.nav_home:
                                selectorFragment = new HomeFragment();
                                break;

                            case R.id.nav_heart:
                                selectorFragment = new NotificationFragment();
                                break;
                            case R.id.nav_search:
                                selectorFragment = new SearchFragment();
                                break;
                            case R.id.nav_person:
                                selectorFragment = new ProfileFragment();
                                break;

                            case R.id.nav_addbox:
                                selectorFragment = null;
                                startActivity(new Intent(Main2Activity.this,PostActivity.class));

                                break;
                        }
                        if (selectorFragment!=null)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();
                        }
                        return true;
                    }
                });
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();


    }
}
