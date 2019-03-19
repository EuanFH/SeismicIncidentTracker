package net.chinzer.seismicincidenttracker;

import android.app.Application;
import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SeismicIncidentUpdateRefreshWorker extends Worker {
    public SeismicIncidentUpdateRefreshWorker(Context appContext, WorkerParameters workerParameters){
        super(appContext, workerParameters);
    }

    public Result doWork(){
        Application application = (Application) getApplicationContext();
        SeismicIncidentRepository repository = new SeismicIncidentRepository(application);
        try{
            for (SeismicIncident seismicIncident : Rss.seismicIncidents()){
                repository.insert(seismicIncident);
            }
        }
        catch(Exception e){
            return Result.failure();
        }
        return Result.success();
    }
}
