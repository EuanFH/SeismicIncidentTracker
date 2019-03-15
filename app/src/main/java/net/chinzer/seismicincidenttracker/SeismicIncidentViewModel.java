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

    private boolean currentAscending = false;
    private int currentSortColumnItem = R.id.date_time;
    private SeismicIncidentColumnName currentSortColumn = SeismicIncidentColumnName.DATETIME;

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

    public void searchSeismicIncidents(SeismicIncidentsSearch newSearch){
        swapLiveData(repository.searchSeismicIncidents(newSearch, currentSortColumn, currentAscending));
    }

    public void insert(SeismicIncident seismicIncident) {

        repository.insert(seismicIncident);
    }

    public boolean isCurrentAscending() {
        return currentAscending;
    }

    public void setCurrentAscending(boolean currentAscending) {
        this.currentAscending = currentAscending;
    }

    public int getCurrentSortColumnItem() {
        return currentSortColumnItem;
    }

    public void setCurrentSortColumnItem(int currentSortColumnItem) {
        this.currentSortColumnItem = currentSortColumnItem;
    }

    public SeismicIncidentColumnName getCurrentSortColumn() {
        return currentSortColumn;
    }

    public void setCurrentSortColumn(SeismicIncidentColumnName currentSortColumn) {
        this.currentSortColumn = currentSortColumn;
    }

    private void swapLiveData(LiveData<List<SeismicIncident>> newSeismicIncidents){
        if(currentSeismicIncidents != null){
            seismicIncidents.removeSource(currentSeismicIncidents);
        }
        currentSeismicIncidents = newSeismicIncidents;
        seismicIncidents.addSource(currentSeismicIncidents, seismicIncidents::setValue);
    }

}
