//
// Name                 Euan Haahr
// Student ID           S1716375
// Programme of Study   Bsc Computing
//
package net.chinzer.seismicincidenttracker.Workers;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import net.chinzer.seismicincidenttracker.Converters.DateTimeTypeConverters;
import net.chinzer.seismicincidenttracker.R;
import net.chinzer.seismicincidenttracker.Database.Rss;
import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.Database.SeismicIncidentRepository;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SeismicIncidentUpdateWorker extends Worker {
    public SeismicIncidentUpdateWorker(Context appContext, WorkerParameters workerParameters){
        super(appContext, workerParameters);
    }

    public Worker.Result doWork(){
        Application application = (Application) getApplicationContext();
        SeismicIncidentRepository repository = new SeismicIncidentRepository(application);
        SeismicIncident seismicIncidentNotify = null;
        try{
            List<SeismicIncident> seismicIncidents = Rss.seismicIncidents();
            if(!seismicIncidents.isEmpty()){
                SeismicIncident newestSeismicIncident = seismicIncidents.get(0);
                SeismicIncident fromDB = repository.getSeismicIncident(newestSeismicIncident.getDateTime(), newestSeismicIncident.getLatitude(), newestSeismicIncident.getLongitude());
                if(fromDB == null){
                    seismicIncidentNotify = newestSeismicIncident;
                }
            }
            for (SeismicIncident seismicIncident : seismicIncidents){
                repository.insert(seismicIncident);
            }
        }
        catch(Exception e){
            return Result.failure();
        }
        if(seismicIncidentNotify != null){
            Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext(), "Seismic Incidents");
            Notification notification = notificationBuilder
                    .setContentTitle("New Seismic Incident")
                    .setSmallIcon(R.drawable.ic_earthquake)
                    .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(
                            String.format("%s\nSeverity: %s\nMagnitude: %s\n\uD83D\uDCC5%s\n\uD83D\uDD52%s",
                                    seismicIncidentNotify.getLocality(),
                                    seismicIncidentNotify.getSeverity(),
                                    seismicIncidentNotify.getMagnitude(),
                                    DateTimeTypeConverters.fromOffsetDateTimeToUserInputDate(seismicIncidentNotify.getDateTime()),
                                    DateTimeTypeConverters.fromOffsetTimeToUserInputTime(seismicIncidentNotify.getDateTime())
                            )
                    ))
                    .build();
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
        return Result.success();
    }
}
