package com.example.budgetmanagement;

import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.budgetmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public static Resources resources;
    static Application applicationInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationInstance = this.getApplication();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resources = getResources();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        getSupportActionBar().setElevation(0);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_statistics, R.id.navigation_coming, R.id.navigation_category, R.id.navigation_settings)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navBar = findViewById(R.id.nav_view);
        TextView fragmentName = getSupportActionBar().getCustomView().findViewById(R.id.fragmentName);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            int destination = navDestination.getId();
            if(destination == R.id.addNewComing) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.fragment_name_new_transactions);
            } else if (destination == R.id.editComingElement) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.fragment_name_edit);
            } else if (destination == R.id.addNewCategoryElement) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.fragment_name_new_category);
            } else if (destination == R.id.comingElementDetails) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.details);
            } else if (destination == R.id.navigation_settings) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText(R.string.fragment_name_settings);
            } else if (destination == R.id.periodComparatorFragment) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.fragment_name_statistics);
            } else if (destination == R.id.periodStatisticsFragment) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText(R.string.fragment_name_statistics);
            } else if (destination == R.id.navigation_statistics) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText(R.string.fragment_name_statistics);
            } else if (destination == R.id.navigation_coming) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText(R.string.fragment_name_transactions);
            } else if (destination == R.id.navigation_category) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText(R.string.fragment_name_categories);
            } else {
                navBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}