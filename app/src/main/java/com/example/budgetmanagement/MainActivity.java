package com.example.budgetmanagement;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.budgetmanagement.database.ViewModels.FilterViewModel;
import com.example.budgetmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FilterViewModel filterViewModel;
    public static Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resources = getResources();

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_statistics, R.id.navigation_incoming, R.id.navigation_history, R.id.navigation_category)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navBar = findViewById(R.id.nav_view);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {

            if(navDestination.getId() == R.id.addNewComing ||
                    navDestination.getId() == R.id.addNewHistory ||
                    navDestination.getId() == R.id.filterFragment) {
                navBar.setVisibility(View.INVISIBLE);
            } else {
                navBar.setVisibility(View.VISIBLE);

            }
        });
    }
}