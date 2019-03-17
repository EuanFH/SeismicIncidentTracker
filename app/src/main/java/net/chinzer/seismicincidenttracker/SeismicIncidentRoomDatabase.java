package net.chinzer.seismicincidenttracker;

import android.content.Context;
import android.os.AsyncTask;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {SeismicIncident.class}, version = 1, exportSchema = false)
public abstract class SeismicIncidentRoomDatabase extends RoomDatabase {
    public abstract SeismicIncidentDAO seismicIncidentDAO();
    private static volatile SeismicIncidentRoomDatabase instance;

    static SeismicIncidentRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (SeismicIncidentRoomDatabase.class) {
               if (instance == null) {
                   instance = Room.databaseBuilder(context.getApplicationContext(),
                           SeismicIncidentRoomDatabase.class, "seismic_incidents")
                           .fallbackToDestructiveMigration()
                           .addCallback(roomDatabaseCallback)
                           .build();
               }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(instance).execute();
                }
            };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SeismicIncidentDAO dao;

        PopulateDbAsync(SeismicIncidentRoomDatabase db) {
            dao = db.seismicIncidentDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            dao.deleteAll();
            /*dao.insert(new SeismicIncident(OffsetDateTime.now(), 4, 9.5, "Glasgow", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 3, 9, 0,0,0,0, ZoneOffset.UTC), 1, 9.5, "1", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 3, 1, 0,0,0,0, ZoneOffset.UTC), 2, 9.5, "1", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 2, 1, 1,1,0,0, ZoneOffset.UTC), 3, 9.5, "1", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 2, 1, 2,5,0,0, ZoneOffset.UTC), 4, 9.5, "1", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 2, 4, 5,4,0,0, ZoneOffset.UTC), 5, 9.5, "3", 55.0, -2.0, "https://chinzer.net"));
            dao.insert(new SeismicIncident(OffsetDateTime.of(2019, 4, 9, 0,0,0,0, ZoneOffset.UTC), 6, 9.5, "4", 55.0, -2.0, "https://chinzer.net"));*/



            return null;
        }
    }
}
