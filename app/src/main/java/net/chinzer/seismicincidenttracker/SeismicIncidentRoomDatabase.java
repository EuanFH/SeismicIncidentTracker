package net.chinzer.seismicincidenttracker;

import android.content.Context;
import android.os.AsyncTask;

import java.time.OffsetDateTime;

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
            dao.insert(new SeismicIncident(OffsetDateTime.now(), 4, 9.5, "Glasgow", 55.0, -2.0));
            return null;
        }
    }
}
