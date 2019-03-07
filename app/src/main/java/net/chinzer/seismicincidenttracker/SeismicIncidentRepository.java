package net.chinzer.seismicincidenttracker;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SeismicIncidentRepository {

   private SeismicIncidentDAO seismicIncidentDAO;
   private LiveData<List<SeismicIncident>> allSeismicIncidents;

   public SeismicIncidentRepository(Application application) {
       SeismicIncidentRoomDatabase db = SeismicIncidentRoomDatabase.getDatabase(application);
       seismicIncidentDAO = db.seismicIncidentDAO();
       allSeismicIncidents = seismicIncidentDAO.getAllSeismicIncidents();
   }

   public LiveData<List<SeismicIncident>> getAllSeismicIncidents() {
       return allSeismicIncidents;
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


