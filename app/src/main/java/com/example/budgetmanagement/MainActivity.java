package com.example.budgetmanagement;

import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.budgetmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private Toolbar actionbar;
    private ActivityMainBinding binding;
    public static Resources resources;
    static Application applicationInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationInstance = this.getApplication();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resources = getResources();

        actionbar = findViewById(R.id.toolBar);
        setSupportActionBar(actionbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_statistics, R.id.navigation_coming, R.id.navigation_history, R.id.navigation_settings)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navBar = findViewById(R.id.nav_view);
        TextView fragmentName = findViewById(R.id.fragmentName);

        actionbar.setNavigationOnClickListener(v -> onBackPressed());

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            int destination = navDestination.getId();
            if(destination == R.id.addNewComing) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText("Nowa transackcja");
            } else if (destination == R.id.categoryList) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText("Kategorie");
            } else if (destination == R.id.filterFragment) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText("Sortowanie");
            } else if (destination == R.id.editComingElement) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText("Edycja");
            } else if (destination == R.id.addNewCategoryElement) {
                navBar.setVisibility(View.GONE);
                fragmentName.setText("Nowa kategoria");
            } else if (destination == R.id.navigation_statistics) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText("Statystyki");
            } else if (destination == R.id.navigation_coming) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText("Transakcje");
            } else if (destination == R.id.navigation_settings) {
                navBar.setVisibility(View.VISIBLE);
                fragmentName.setText("Ustawienia");
            } else {
                navBar.setVisibility(View.VISIBLE);
            }
        });
    }
}