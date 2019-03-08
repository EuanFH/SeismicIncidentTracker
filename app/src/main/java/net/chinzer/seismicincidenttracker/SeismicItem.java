package net.chinzer.seismicincidenttracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;

import androidx.fragment.app.Fragment;

public class SeismicItem extends Fragment {

    private SeismicIncident seismicIncident;
    private TextView locality;
    private TextView date;
    private TextView time;
    private TextView magnitude;
    private TextView severity;
    private TextView depth;
    private TextView longitude;
    private TextView latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seismic_item, container, false);
        locality = v.findViewById(R.id.locality);
        date = v.findViewById(R.id.date);
        time = v.findViewById(R.id.time);
        magnitude = v.findViewById(R.id.magnitude);
        severity = v.findViewById(R.id.severity);
        depth = v.findViewById(R.id.depth);
        longitude = v.findViewById(R.id.longitude);
        latitude = v.findViewById(R.id.latitude);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seismicIncident = this.getArguments().getParcelable("seismicIncident");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        locality.setText(seismicIncident.getLocality());
        date.setText(seismicIncident.getDateTime().format(dateFormatter));
        time.setText(seismicIncident.getDateTime().format(timeFormatter));
        magnitude.setText(String.valueOf(seismicIncident.getMagnitude()));
        depth.setText(String.valueOf(seismicIncident.getDepth()));
        longitude.setText(String.valueOf(seismicIncident.getLongitude()));
        latitude.setText(String.valueOf(seismicIncident.getLatitude()));
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

    }
}