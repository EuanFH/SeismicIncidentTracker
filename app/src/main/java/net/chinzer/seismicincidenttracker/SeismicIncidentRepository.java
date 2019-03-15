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

   public LiveData<List<SeismicIncident>> getSeismicIncidents() {
       return sortSeismicIncidents(SeismicIncidentColumnName.DATETIME, false);
   }

   public LiveData<List<SeismicIncident>> sortSeismicIncidents(SeismicIncidentColumnName column, boolean ascending){
       return seismicIncidentDAO.seismicIncidentsQuery(new SeismicIncidentQueryBuilder().orderBy(column, ascending).compile());
   }

   public LiveData<List<SeismicIncident>> searchSeismicIncidents(SeismicIncidentsSearch search, SeismicIncidentColumnName column, boolean ascending){
       SeismicIncidentQueryBuilder query = new SeismicIncidentQueryBuilder();

       if(search.getLocality() != null){
           query = query.whereLocality(search.getLocality());
       }

       if(search.getStartDate() != null){
           if(search.getEndDate() == null){
               query = query.whereDay(search.getStartDate());
           }
           else{
               query = query.whereDay(search.getStartDate(), search.getEndDate());
           }
       }

       if(search.getStartTime() != null){
           if(search.getEndTime() == null){
               query = query.whereTime(search.getStartTime());
           }
           else{
               query = query.whereTime(search.getStartTime(), search.getEndTime());
           }
       }

       if(search.getStartMagnitude() != null){
           if(search.getEndMagnitude() == null){
               query = query.whereMagnitude(search.getStartMagnitude());
           }
           else{
               query = query.whereMagnitude(search.getStartMagnitude(), search.getEndMagnitude());
           }
       }

       if(search.getStartDepth() != null){
           if(search.getEndDepth() == null){
               query = query.whereDepth(search.getStartDepth());
           }
           else{
               query = query.whereDepth(search.getStartDepth(), search.getEndDepth());
           }
       }

       if(search.getSeverity() != null){
           query = query.whereSeverity(search.getSeverity());
       }

       if(search.getDistance() != null & search.getLatitude() != null & search.getLongitude() != null){
           query = query.whereInRadias(search.getLatitude(), search.getLongitude(), search.getDistance());
       }
       return seismicIncidentDAO.seismicIncidentsQuery(query.orderBy(column, ascending).compile());
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


