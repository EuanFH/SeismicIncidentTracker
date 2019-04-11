//
// Name                 Euan Haahr
// Student ID           S1716375
// Programme of Study   Bsc Computing
//
package net.chinzer.seismicincidenttracker.Database;

import net.chinzer.seismicincidenttracker.Converters.DateTimeTypeConverters;
import net.chinzer.seismicincidenttracker.Model.SeismicIncident;

import java.time.OffsetDateTime;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
@TypeConverters({DateTimeTypeConverters.class})
public interface SeismicIncidentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SeismicIncident seismicIncident);

    @Query("SELECT * FROM seismic_incidents WHERE dateTime = :dateTime AND latitude = :latitude AND longitude = :longitude LIMIT 1")
    SeismicIncident getSeismicIncident(OffsetDateTime dateTime, double latitude, double longitude);

    @RawQuery (observedEntities = SeismicIncident.class)
    LiveData<List<SeismicIncident>> seismicIncidentsQuery(SupportSQLiteQuery query);

    @RawQuery (observedEntities = SeismicIncident.class)
    SeismicIncident seismicIncidentsQueryOneValue(SupportSQLiteQuery query);

    @Query("DELETE FROM seismic_incidents")
    void deleteAll();
}
