package net.chinzer.seismicincidenttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{
    private SeismicIncidentViewModel seismicIncidentViewModel;
    private NavController navigationController;
    private BottomNavigationView navigationView;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(this).get(SeismicIncidentViewModel.class);
        setContentView(R.layout.activity_main);
        navigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView = findViewById(R.id.bottomNavigationView);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        NavigationUI.setupActionBarWithNavController(this, navigationController);
        NavigationUI.setupWithNavController(navigationView, navigationController);
    }

    @Override
    public boolean onSupportNavigateUp(){
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

}
