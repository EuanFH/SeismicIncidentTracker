package net.chinzer.seismicincidenttracker.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.R;

import java.time.format.DateTimeFormatter;

public class SeismicIncidentGoogleMapsInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public SeismicIncidentGoogleMapsInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_info_window, null);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        TextView locality = view.findViewById(R.id.locality);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);
        TextView magnitude = view.findViewById(R.id.magnitude);
        TextView severity = view.findViewById(R.id.severity);
        TextView depth = view.findViewById(R.id.depth);
        SeismicIncident seismicIncident = (SeismicIncident) marker.getTag();

        locality.setText(seismicIncident.getLocality());
        date.setText(seismicIncident.getDateTime().format(dateFormatter));
        time.setText(seismicIncident.getDateTime().format(timeFormatter));
        depth.setText(seismicIncident.getDepth() + "km");
        magnitude.setText(String.valueOf(seismicIncident.getMagnitude()));
        severity.setText(String.valueOf(seismicIncident.getSeverity()));
        switch(seismicIncident.getSeverity()){
            case("Micro"):
                magnitude.setBackgroundResource(R.color.micro);
                break;
            case("Minor"):
                magnitude.setBackgroundResource(R.color.minor);
                break;
            case("Light"):
                magnitude.setBackgroundResource(R.color.light);
                break;
            case("Moderate"):
                magnitude.setBackgroundResource(R.color.moderate);
                break;
            case("Strong"):
                magnitude.setBackgroundResource(R.color.strong);
                break;
            case("Major"):
                magnitude.setBackgroundResource(R.color.major);
                break;
            case("Great"):
                magnitude.setBackgroundResource(R.color.great);
                break;
        }

        return view;
    }
}
