package com.easyshopping.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.easyshopping.R;
import com.easyshopping.util.MyClick;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
   public static NavController navController;
    private Bundle intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_user)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        intent = getIntent().getExtras();

        if (intent != null) {

            String key = intent.getString("cart");

            if (key.equalsIgnoreCase("cart")) {
                navController.navigateUp();
                String Goto = intent.getString("GoToTab3");
                Bundle bundle = new Bundle();
                bundle.putString("Goto", Goto);
                navController.navigate(R.id.navigation_cart, bundle);
            }
        }
    }

}