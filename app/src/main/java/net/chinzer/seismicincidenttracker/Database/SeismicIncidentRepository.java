package net.chinzer.seismicincidenttracker.Database;

import android.app.Application;
import android.os.AsyncTask;

import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.Model.SeismicIncidentColumnName;
import net.chinzer.seismicincidenttracker.Model.SeismicIncidentsSearch;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

public class SeismicIncidentRepository {

   private SeismicIncidentDAO seismicIncidentDAO;

   public SeismicIncidentRepository(Application application) {
       SeismicIncidentRoomDatabase db = SeismicIncidentRoomDatabase.getDatabase(application);
       seismicIncidentDAO = db.seismicIncidentDAO();
   }

   public SeismicIncident getSeismicIncident(OffsetDateTime dateTime, double latitude, double longitude){
       return seismicIncidentDAO.getSeismicIncident(dateTime, latitude, longitude);
   }

   public LiveData<List<SeismicIncident>> getSeismicIncidents() {
       return sortSeismicIncidents(SeismicIncidentColumnName.DATETIME, false);
   }

   public LiveData<List<SeismicIncident>> sortSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
       return seismicIncidentDAO.seismicIncidentsQuery(new SeismicIncidentQueryBuilder().orderBy(column, ascending).compile());
   }

   public LiveData<List<SeismicIncident>> searchSeismicIncidents(SeismicIncidentsSearch search, SeismicIncidentColumnName column, boolean ascending){
       SeismicIncidentQueryBuilder query = new SeismicIncidentQueryBuilder();
       addSearchWheres(search, query);
       return seismicIncidentDAO.seismicIncidentsQuery(query.orderBy(column, ascending).compile());
   }

   public Map <String, SeismicIncident> getInformation(SeismicIncidentsSearch search) {
       Map <String, SeismicIncident> informationSeismicIncidents = new LinkedHashMap<String, SeismicIncident>();

       informationSeismicIncidents.put("Northerly", getMaxMinOfColumn(SeismicIncidentColumnName.LATITUDE, false, search));
       informationSeismicIncidents.put("Southerly", getMaxMinOfColumn(SeismicIncidentColumnName.LATITUDE, true, search));
       informationSeismicIncidents.put("Easterly", getMaxMinOfColumn(SeismicIncidentColumnName.LONGITUDE, false, search));
       informationSeismicIncidents.put("Westerly", getMaxMinOfColumn(SeismicIncidentColumnName.LONGITUDE, true, search));
       informationSeismicIncidents.put("Largest", getMaxMinOfColumn(SeismicIncidentColumnName.MAGNITUDE, false, search));
       informationSeismicIncidents.put("Smallest", getMaxMinOfColumn(SeismicIncidentColumnName.MAGNITUDE, true, search));
       informationSeismicIncidents.put("Deepest", getMaxMinOfColumn(SeismicIncidentColumnName.DEPTH, false, search));
       informationSeismicIncidents.put("Shallowest", getMaxMinOfColumn(SeismicIncidentColumnName.DEPTH, true, search));

       return informationSeismicIncidents;
    }

   public void insert (SeismicIncident seismicIncident) {
       new insertAsyncTask(seismicIncidentDAO).execute(seismicIncident);
   }

   private SeismicIncident getMaxMinOfColumn(SeismicIncidentColumnName columnName, boolean min, SeismicIncidentsSearch search){
       SeismicIncidentQueryBuilder query = new SeismicIncidentQueryBuilder();
       query = addSearchWheres(search, query);
       return seismicIncidentDAO.seismicIncidentsQueryOneValue(query.orderBy(columnName,min).limit(1).compile());
   }

   private static SeismicIncidentQueryBuilder addSearchWheres(SeismicIncidentsSearch search, SeismicIncidentQueryBuilder query){
       if(search != null) {
           if (search.getLocality() != null) {
               query = query.whereLocality(search.getLocality());
           }

           if (search.getStartDate() != null) {
               if (search.getEndDate() == null) {
                   query = query.whereDay(search.getStartDate());
               } else {
                   query = query.whereDay(search.getStartDate(), search.getEndDate());
               }
           }

           if (search.getStartTime() != null) {
               if (search.getEndTime() == null) {
                   query = query.whereTime(search.getStartTime());
               } else {
                   query = query.whereTime(search.getStartTime(), search.getEndTime());
               }
           }

           if (search.getStartMagnitude() != null) {
               if (search.getEndMagnitude() == null) {
                   query = query.whereMagnitude(search.getStartMagnitude());
               } else {
                   query = query.whereMagnitude(search.getStartMagnitude(), search.getEndMagnitude());
               }
           }

           if (search.getStartDepth() != null) {
               if (search.getEndDepth() == null) {
                   query = query.whereDepth(search.getStartDepth());
               } else {
                   query = query.whereDepth(search.getStartDepth(), search.getEndDepth());
               }
           }

           if (search.getSeverity() != null) {
               query = query.whereSeverity(search.getSeverity());
           }
       }
       return query;
    }

   private static class insertAsyncTask extends AsyncTask<SeismicIncident, Void, Void> {

       private SeismicIncidentDAO asyncTaskDao;

       insertAsyncTask(SeismicIncidentDAO dao) {
           asyncTaskDao = dao;
       }

       @Override
       protected Void doInBackground(final SeismicIncident... params) {
           asyncTaskDao.insert(params[0]);
           return null;
       }
   }
}


