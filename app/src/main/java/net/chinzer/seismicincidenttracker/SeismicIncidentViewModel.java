package net.chinzer.seismicincidenttracker;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SeismicIncidentViewModel extends AndroidViewModel {
    private SeismicIncidentRepository repository;

    private LiveData<List<SeismicIncident>> allSeismicIncidents;

    public SeismicIncidentViewModel(Application application){
        super(application);
        repository = new SeismicIncidentRepository(application);
        allSeismicIncidents = repository.getAllSeismicIncidents();
    }


    public LiveData<List<SeismicIncident>> getAllSeismicIncidents() {
        return allSeismicIncidents;
    }

    public void insert(SeismicIncident seismicIncident) { repository.insert(seismicIncident);}

}
