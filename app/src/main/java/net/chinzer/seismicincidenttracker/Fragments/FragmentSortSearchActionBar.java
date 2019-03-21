package net.chinzer.seismicincidenttracker.Fragments;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import net.chinzer.seismicincidenttracker.Model.SeismicIncidentColumnName;
import net.chinzer.seismicincidenttracker.R;
import net.chinzer.seismicincidenttracker.ViewModels.SeismicIncidentViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public abstract class FragmentSortSearchActionBar extends Fragment {

    private SeismicIncidentViewModel seismicIncidentViewModel;

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_sort_actionbar, menu);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);
        super.onCreateOptionsMenu(menu, menuInflater);
        if(seismicIncidentViewModel.getCurrentSearch() == null){
            MenuItem clear_button = menu.findItem(R.id.clear_button);
            clear_button.setVisible(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.clear_button:
                seismicIncidentViewModel.resetSeismicIncidents();
                item.setVisible(false);
                return true;

            case R.id.search_button:
                Navigation.findNavController(getView()).navigate(R.id.action_seismic_incidents_to_seismicSearch);
                return true;

            case R.id.sort_button:
                View sortButtonView = this.getActivity().findViewById(R.id.sort_button);
                PopupMenu sortMenu = new PopupMenu(this.getContext(), sortButtonView);
                sortMenu.inflate(R.menu.sort);
                //make this better by adding @string in the future
                MenuItem sortByButton = sortMenu.getMenu().findItem(R.id.sort_by);
                sortByButton.setTitle(String.format("Sort By %s", seismicIncidentViewModel.getCurrentSortColumn().getHumanReadableColumnName()));
                MenuItem ascendingDescendingButton;
                if(seismicIncidentViewModel.isCurrentAscending()){
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
                                MenuItem columnCurrentlyUsed = sortMenu.getMenu().findItem(seismicIncidentViewModel.getCurrentSortColumnItem());
                                columnCurrentlyUsed.setEnabled(false);
                                break;
                            case R.id.ascending:
                                seismicIncidentViewModel.setCurrentAscending(true);
                                break;

                            case R.id.descending:
                                seismicIncidentViewModel.setCurrentAscending(false);
                                break;

                            case R.id.date_time:
                                seismicIncidentViewModel.setCurrentSortColumn(SeismicIncidentColumnName.DATETIME);
                                seismicIncidentViewModel.setCurrentSortColumnItem(item.getItemId());
                                break;

                            case R.id.depth:
                                seismicIncidentViewModel.setCurrentSortColumn(SeismicIncidentColumnName.DEPTH);
                                seismicIncidentViewModel.setCurrentSortColumnItem(item.getItemId());
                                break;

                            case R.id.magnitude:
                                seismicIncidentViewModel.setCurrentSortColumn(SeismicIncidentColumnName.MAGNITUDE);
                                seismicIncidentViewModel.setCurrentSortColumnItem(item.getItemId());
                                break;

                            case R.id.severity:
                                seismicIncidentViewModel.setCurrentSortColumn(SeismicIncidentColumnName.SEVERITY);
                                seismicIncidentViewModel.setCurrentSortColumnItem(item.getItemId());
                                break;

                            case R.id.locality:
                                seismicIncidentViewModel.setCurrentSortColumn(SeismicIncidentColumnName.LOCALITY);
                                seismicIncidentViewModel.setCurrentSortColumnItem(item.getItemId());
                                break;
                        }
                        seismicIncidentViewModel.sortSeismicIncidents();
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