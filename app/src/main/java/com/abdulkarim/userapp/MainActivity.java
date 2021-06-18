package com.abdulkarim.userapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.abdulkarim.userapp.fragments.CartFragment;
import com.abdulkarim.userapp.fragments.FavouriteFragment;
import com.abdulkarim.userapp.fragments.HomeFragment;
import com.abdulkarim.userapp.fragments.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    private boolean isFirstPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        bottomMenu();

    }

    @Override
    public void onBackPressed() {
        if (chipNavigationBar.getSelectedItemId() == R.id.bottom_nav_home){
            if (isFirstPressed) {
                super.onBackPressed();
                //finishAffinity();
            } else {
                isFirstPressed = true;
                showMessage("Press back again to exit");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstPressed = false;
                    }
                }, 1500);
            }

        }else{
            chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);

        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bottom_nav_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.bottom_nav_favourite:
                        fragment = new FavouriteFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        });

    }

}