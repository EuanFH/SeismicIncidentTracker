package net.chinzer.seismicincidenttracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class SeismicSearch extends Fragment {

    private SeismicIncidentViewModel seismicIncidentViewModel;

    private EditText locality;
    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;
    private EditText startMagnitude;
    private EditText endMagnitude;
    private EditText startDepth;
    private EditText endDepth;
    private Spinner severity;
    private EditText distance;
    private EditText latitude;
    private EditText longitude;
    private Button  searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seismic_search, container, false);
        locality = v.findViewById(R.id.locality);
        startDate = v.findViewById(R.id.startDate);
        endDate = v.findViewById(R.id.endDate);
        startTime = v.findViewById(R.id.startTime);
        endTime = v.findViewById(R.id.endTime);
        startMagnitude = v.findViewById(R.id.startMagnitude);
        endMagnitude = v.findViewById(R.id.endMagnitude);
        startDepth = v.findViewById(R.id.startDepth);
        endDepth = v.findViewById(R.id.endDepth);
        severity = v.findViewById(R.id.severity);
        distance = v.findViewById(R.id.distance);
        latitude = v.findViewById(R.id.latitude);
        longitude = v.findViewById(R.id.longitude);


        searchButton = v.findViewById(R.id.search_button);

        addDateDialog(startDate);
        addDateDialog(endDate);
        addTimeDialog(startTime);
        addTimeDialog(endTime);
        searchButton.setOnClickListener(searchOnClickListener());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);
    }

    private void addDateDialog(EditText dateField){
        //way to get currently selected
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        dateField.setText(String.format("%02d/%02d/%02d", selectedday, selectedmonth, selectedyear));
                    }
                },year, month, day);
                datePicker.show();
            }
        });
    }

    private void addTimeDialog(EditText timeField){
        //way to get currently selected
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                },hour, minute, true);
                timePicker.show();
            }
        });
    }

    private View.OnClickListener searchOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seismicIncidentViewModel.searchSeismicIncidents(new SeismicIncidentsSearch(
                        locality.getText().toString(),
                        startDate.getText().toString(),
                        endDate.getText().toString(),
                        startTime.getText().toString(),
                        endTime.getText().toString(),
                        startMagnitude.getText().toString(),
                        endMagnitude.getText().toString(),
                        startDepth.getText().toString(),
                        endDepth.getText().toString(),
                        severity.getSelectedItem().toString(),
                        latitude.getText().toString(),
                        longitude.getText().toString(),
                        distance.getText().toString()
                ));
                Navigation.findNavController(v).navigate(R.id.action_seismicSearch_to_seismic_incidents);
            }
        };
    }

}
