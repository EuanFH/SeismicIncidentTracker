package net.chinzer.seismicincidenttracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class SeismicMap extends Fragment implements OnMapReadyCallback {

    private SeismicIncidentViewModel seismicIncidentViewModel;
    private SeismicIncident seismicIncident;

    private GoogleMap map;

    public SeismicMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.seismic_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);
        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.containsKey("seismicIncident")){
                seismicIncident = bundle.getParcelable("seismicIncident");
            }
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLngBounds unitedkingdom = new LatLngBounds(new LatLng(48.0, -12.0), new LatLng(59.0, 4.0));
        map.setLatLngBoundsForCameraTarget(unitedkingdom);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(unitedkingdom, 20));
        if (seismicIncident == null){
            SeismicIncidentGoogleMapsInfoWindow infoWindow = new SeismicIncidentGoogleMapsInfoWindow(getContext());
            googleMap.setInfoWindowAdapter(infoWindow);
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("seismicIncident", (SeismicIncident)marker.getTag());
                    Navigation.findNavController(getView()).navigate(R.id.action_map_to_seismicItem, bundle);
                }
            });
            multipleSeismicIncidentMap();

        } else {
            specificSeismicIncidentMap();
        }

    }

    public void multipleSeismicIncidentMap(){
        seismicIncidentViewModel.getSeismicIncidents().observe(getViewLifecycleOwner(), new Observer<List<SeismicIncident>>() {
            @Override
            public void onChanged(@Nullable final List<SeismicIncident> seismicIncidents) {
                // Update the cached copy of the words in the adapter.
                for (SeismicIncident seismicIncident : seismicIncidents){
                    int markerVector = 0;
                    switch(seismicIncident.getSeverity()){
                        case("Micro"):
                            markerVector = R.drawable.ic_mapmarker_micro;
                            break;
                        case("Minor"):
                            markerVector = R.drawable.ic_mapmarker_minor;
                            break;
                        case("Light"):
                            markerVector = R.drawable.ic_mapmarker_light;
                            break;
                        case("Moderate"):
                            markerVector = R.drawable.ic_mapmarker_moderate;
                            break;
                        case("Strong"):
                            markerVector = R.drawable.ic_mapmarker_strong;
                            break;
                        case("Major"):
                            markerVector = R.drawable.ic_mapmarker_major_great;
                            break;
                        case("Great"):
                            markerVector = R.drawable.ic_mapmarker_major_great;
                            break;
                    }

                    LatLng latlng = new LatLng(seismicIncident.getLatitude(), seismicIncident.getLongitude());
                    SeismicIncidentGoogleMapsInfoWindow infoWindow = new SeismicIncidentGoogleMapsInfoWindow(getContext());
                    Marker marker = map.addMarker(new MarkerOptions().position(latlng).icon(bitmapDescriptorFromVector(getContext(), markerVector)));
                    marker.setTag(seismicIncident);
                }
            }
        });
    }

    public void specificSeismicIncidentMap(){
        LatLng latlng = new LatLng(seismicIncident.getLatitude(), seismicIncident.getLongitude());
        map.addMarker(new MarkerOptions().position(latlng));
        moveToCurrentLocation(latlng);
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
