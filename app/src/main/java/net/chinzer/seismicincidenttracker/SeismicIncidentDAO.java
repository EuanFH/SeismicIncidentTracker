package net.chinzer.seismicincidenttracker;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

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

    @RawQuery (observedEntities = SeismicIncident.class)
    LiveData<List<SeismicIncident>> seismicIncidentsQuery(SupportSQLiteQuery query);

    @RawQuery (observedEntities = SeismicIncident.class)
    SeismicIncident seismicIncidentsQueryOneValue(SupportSQLiteQuery query);

    @Query("DELETE FROM seismic_incidents")
    void deleteAll();
}
