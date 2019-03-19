package net.chinzer.seismicincidenttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

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
        startPerodicRssUpdate();
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

    private void startPerodicRssUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Seismic Incidents";
            String description = "Reports Latest Seismic Incidents";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Seismic Incidents", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(new PeriodicWorkRequest.Builder(SeismicIncidentUpdateWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES).build());
    }
}
