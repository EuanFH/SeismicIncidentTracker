package net.chinzer.seismicincidenttracker;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public abstract class FragmentSortSearchActionBar extends Fragment {

    private SeismicIncidentViewModel seismicIncidentViewModel;
    private SeismicIncidentColumnName currentSortColumn = SeismicIncidentColumnName.DATETIME;
    private int currentSortColumnItem = R.id.date_time;
    private boolean currentAscending = false;


    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_sort_actionbar, menu);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {

            case R.id.search_button:

                EditText startDate;
                EditText endDate;
                EditText startTime;
                EditText endTime;
                EditText startMagnitude;
                EditText endMagnitude;
                EditText startDepth;
                EditText endDepth;
                //View searchButtonView = this.getActivity().findViewById(R.id.search_button);
                //PopupMenu searchMenu = new PopupMenu(this.getContext(), searchButtonView);
                //seismicIncidentViewModel.testSearchSeismicIncidents(currentSortColumn, currentAscending);
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View popupMenuView = layoutInflater.inflate(R.layout.popup_search,null);

                PopupWindow popupWindow = new PopupWindow(popupMenuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                startDate = popupMenuView.findViewById(R.id.startDate);
                endDate = popupMenuView.findViewById(R.id.endDate);
                startTime = popupMenuView.findViewById(R.id.startTime);
                endTime = popupMenuView.findViewById(R.id.endTime);

                addDateDialog(startDate);
                addDateDialog(endDate);
                addTimeDialog(startTime);
                addTimeDialog(endTime);

                popupWindow.setFocusable(true);
                popupWindow.setElevation(40);
                popupWindow.update();
                getActivity().findViewById(R.id.fragmentContainer).setForeground(getResources().getDrawable( R.drawable.window_dim ));
                fadeInForeground(getActivity().findViewById(R.id.fragmentContainer));

                popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
                return true;

            case R.id.sort_button:
                View sortButtonView = this.getActivity().findViewById(R.id.sort_button);
                PopupMenu sortMenu = new PopupMenu(this.getContext(), sortButtonView);
                sortMenu.inflate(R.menu.sort);
                //make this better by adding @string in the future
                MenuItem sortByButton = sortMenu.getMenu().findItem(R.id.sort_by);
                sortByButton.setTitle(String.format("Sort By %s", currentSortColumn.getHumanReadableColumnName()));
                MenuItem ascendingDescendingButton;
                if(currentAscending){
                    ascendingDescendingButton = sortMenu.getMenu().findItem(R.id.ascending);
                }
                else{
                    ascendingDescendingButton = sortMenu.getMenu().findItem(R.id.descending);
                }
                ascendingDescendingButton.setEnabled(false);
                //might want to split this into seprate class
                sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.sort_by:
                                //make this better by adding @string in the future
                                item.setTitle("Sort By");
                                MenuItem columnCurrentlyUsed = sortMenu.getMenu().findItem(currentSortColumnItem);
                                columnCurrentlyUsed.setEnabled(false);
                                break;
                            case R.id.ascending:
                                currentAscending = true;
                                break;

                            case R.id.descending:
                                currentAscending = false;
                                break;

                            case R.id.date_time:
                                currentSortColumn = SeismicIncidentColumnName.DATETIME;
                                currentSortColumnItem = item.getItemId();
                                break;

                            case R.id.depth:
                                currentSortColumn = SeismicIncidentColumnName.DEPTH;
                                currentSortColumnItem = item.getItemId();
                                break;

                            case R.id.magnitude:
                                currentSortColumn = SeismicIncidentColumnName.MAGNITUDE;
                                currentSortColumnItem = item.getItemId();
                                break;

                            case R.id.severity:
                                currentSortColumn = SeismicIncidentColumnName.SEVERITY;
                                currentSortColumnItem = item.getItemId();
                                break;

                            case R.id.locality:
                                currentSortColumn = SeismicIncidentColumnName.LOCALITY;
                                currentSortColumnItem = item.getItemId();
                                break;
                        }
                        seismicIncidentViewModel.sortSeismicIncidents(currentSortColumn, currentAscending);
                        return true;
                    }
                });
                sortMenu.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void fadeInForeground(View view) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view.getForeground(), "alpha", 0, 120);
        animator.setDuration(500);
        animator.start();
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

}