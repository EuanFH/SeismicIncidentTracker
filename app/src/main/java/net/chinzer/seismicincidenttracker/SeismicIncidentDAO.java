package net.chinzer.seismicincidenttracker;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

@Dao
@TypeConverters({DateTimeTypeConverters.class})
public interface SeismicIncidentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SeismicIncident seismicIncident);

    @Query("SELECT * FROM seismic_incidents ORDER BY dateTime DESC")
    LiveData<List<SeismicIncident>> getAllSeismicIncidents();

    @Query("SELECT * FROM seismic_incidents WHERE date(dateTime) = date(:dateOfIncidents) ORDER BY dateTime DESC")
    List<SeismicIncident> getDateSpecificSeismicIncidents(OffsetDateTime dateOfIncidents);

    @Query("SELECT * FROM seismic_incidents WHERE date(dateTime) BETWEEN date(:startDateOfIncidents) AND date(:endDateOfIncidents) ORDER BY dateTime DESC")
    List<SeismicIncident> getDateRangeSpecificSeismicIncidents(OffsetDateTime startDateOfIncidents, OffsetDateTime endDateOfIncidents);

    //old
    //@Query("SELECT * FROM seismic_incidents WHERE dateTime BETWEEN :startDateTimeOfIncidents AND :endDateTimeOfIncidents ORDER BY dateTime DESC")
    //List<SeismicIncident> getDateTimeRangeSpecificSeismicIncidents(OffsetDateTime startDateTimeOfIncidents, OffsetDateTime endDateTimeOfIncidents);

    @Query("SELECT * FROM seismic_incidents WHERE date(dateTime) = date(:dateOfIncidents) AND time(dateTime) BETWEEN :startTimeOfIncidents AND :endTimeOfIncidents ORDER BY dateTime DESC")
    List<SeismicIncident> getDateTimeRangeSpecificSeismicIncidents(OffsetDateTime dateOfIncidents, OffsetTime startTimeOfIncidents, OffsetTime endTimeOfIncidents);

    //untested
    //@Query("SELECT * FROM seismic_incidents WHERE date(dateTime) BETWEEN :startDateOfIncidents AND :endDateOfIncidents AND time(dateTime) BETWEEN :startTimeOfIncidents AND :endTimeOfIncidents  ORDER BY dateTime DESC")
    //List<SeismicIncident> getDateRangeTimeRangeSpecificSeismicIncidents(OffsetDateTime startDateOfIncidents, OffsetDateTime endDateOfIncidents, OffsetTime startTimeOfIncidents, OffsetTime endTimeOfIncidents);

    @Query("DELETE FROM seismic_incidents")
    void deleteAll();
}
