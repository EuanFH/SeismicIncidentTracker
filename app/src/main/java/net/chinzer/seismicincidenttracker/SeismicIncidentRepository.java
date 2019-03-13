package net.chinzer.seismicincidenttracker;

import android.app.Application;
import android.os.AsyncTask;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class SeismicIncidentRepository {

   private SeismicIncidentDAO seismicIncidentDAO;

   public SeismicIncidentRepository(Application application) {
       SeismicIncidentRoomDatabase db = SeismicIncidentRoomDatabase.getDatabase(application);
       seismicIncidentDAO = db.seismicIncidentDAO();
   }

   //this can probably be refactored too tired right now
   public LiveData<List<SeismicIncident>> getSeismicIncidents() {
       return sortSeismicIncidents(SeismicIncidentColumnName.DATETIME, false);
   }

   public LiveData<List<SeismicIncident>> sortSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
       return seismicIncidentDAO.seismicIncidentsQuery(new SeismicIncidentQueryBuilder().orderBy(column, ascending).compile());
   }

   public LiveData<List<SeismicIncident>> testSearchSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
        return seismicIncidentDAO.seismicIncidentsQuery(new SeismicIncidentQueryBuilder().whereDay(OffsetDateTime.of(2019, 3, 9, 0,0,0,0, ZoneOffset.UTC), null).orderBy(column, ascending).compile());
   }

   public void insert (SeismicIncident seismicIncident) {
       new insertAsyncTask(seismicIncidentDAO).execute(seismicIncident);
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


