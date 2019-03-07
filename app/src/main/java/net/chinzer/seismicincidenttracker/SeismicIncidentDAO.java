package net.chinzer.seismicincidenttracker;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SeismicIncidentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SeismicIncident seismicIncident);

    @Query("SELECT * FROM seismic_incidents ORDER BY dateTime DESC")
    LiveData<List<SeismicIncident>> getAllSeismicIncidents();

    @Query("DELETE FROM seismic_incidents")
    void deleteAll();
}
