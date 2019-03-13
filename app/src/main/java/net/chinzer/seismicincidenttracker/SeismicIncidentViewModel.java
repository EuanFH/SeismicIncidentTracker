package net.chinzer.seismicincidenttracker;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class SeismicIncidentViewModel extends AndroidViewModel {
    private SeismicIncidentRepository repository;

    private LiveData<List<SeismicIncident>> currentSeismicIncidents;
    private final MediatorLiveData<List<SeismicIncident>> seismicIncidents;

    public SeismicIncidentViewModel(Application application){
        super(application);
        seismicIncidents = new MediatorLiveData<>();
        repository = new SeismicIncidentRepository(application);
        swapLiveData(repository.getSeismicIncidents());
    }


    public LiveData<List<SeismicIncident>> getSeismicIncidents() {
        return seismicIncidents;
    }

    public void sortSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
        swapLiveData(repository.sortSeismicIncidents(column, ascending));
    }

    public void testSearchSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
        swapLiveData(repository.testSearchSeismicIncidents(column, ascending));
    }

    public void insert(SeismicIncident seismicIncident) {

        repository.insert(seismicIncident);
    }

    private void swapLiveData(LiveData<List<SeismicIncident>> newSeismicIncidents){
        if(currentSeismicIncidents != null){
            seismicIncidents.removeSource(currentSeismicIncidents);
        }
        currentSeismicIncidents = newSeismicIncidents;
        seismicIncidents.addSource(currentSeismicIncidents, seismicIncidents::setValue);
    }

}
