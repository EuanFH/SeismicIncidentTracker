package net.chinzer.seismicincidenttracker;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

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
                //View searchButtonView = this.getActivity().findViewById(R.id.search_button);
                //PopupMenu searchMenu = new PopupMenu(this.getContext(), searchButtonView);
                seismicIncidentViewModel.testSearchSeismicIncidents(currentSortColumn, currentAscending);
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

}