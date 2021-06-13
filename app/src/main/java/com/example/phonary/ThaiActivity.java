package com.example.phonary;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ThaiActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private Button btnChangeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thai);

        // Bind views
        bottomNavigationView = findViewById(R.id.nav_view);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        // Set onClick Listeners
        btnChangeLanguage.setOnClickListener(this);

        // Initiate Navigation Controller
        initNavController();
    }

    private void initNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_vowels:
                navController.navigate(R.id.frag_letters);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                bottomNavigationView.getMenu().getItem(1).setChecked(false);
                break;
            case R.id.nav_phonemes:
                navController.navigate(R.id.frag_phonemes);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                break;
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangeLanguage:
                Toast.makeText(this, "Thai is the only supported language now. We are sorry for this inconvenience.", Toast.LENGTH_SHORT).show();
        }
    }
}