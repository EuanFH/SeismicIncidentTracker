package net.chinzer.seismicincidenttracker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import net.chinzer.seismicincidenttracker.Adapters.SeismicIncidentListAdapter;
import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.R;
import net.chinzer.seismicincidenttracker.ViewModels.SeismicIncidentViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;

public class SeismicList extends FragmentSortSearchActionBar implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshButton;
    private RecyclerView recyclerView;
    private SeismicIncidentViewModel seismicIncidentViewModel;
    private ConstraintLayout emptyResultsMessage;
    private TextView emptyResultsText;
    private ImageView emptyResultsImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seismic_list, container, false);
        // Inflate the layout for this fragment
        refreshButton = view.findViewById(R.id.swiperefresh);
        refreshButton.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.recyclerview);
        emptyResultsMessage = view.findViewById(R.id.empty_results_message);
        emptyResultsImage = view.findViewById(R.id.empty_results_message_image);
        emptyResultsText = view.findViewById(R.id.empty_results_message_text);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);

        final SeismicIncidentListAdapter adapter = new SeismicIncidentListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        seismicIncidentViewModel.getSeismicIncidents().observe(getViewLifecycleOwner(), new Observer<List<SeismicIncident>>() {
            @Override
            public void onChanged(@Nullable final List<SeismicIncident> seismicIncidents) {
                // Update the cached copy of the words in the adapter.
                if(seismicIncidents.size() == 0){
                    if(seismicIncidentViewModel.getCurrentSearch() != null){
                        emptyResultsText.setText(getResources().getString(R.string.empty_results_search));
                        emptyResultsImage.setImageResource(R.drawable.ic_search_black_24dp);
                    }
                    else{
                        emptyResultsText.setText(getResources().getString(R.string.empty_results_nosearch));
                        emptyResultsImage.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                    }
                    recyclerView.setVisibility(View.GONE);
                    emptyResultsMessage.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyResultsMessage.setVisibility(View.GONE);
                }
                adapter.setSeismicIncidents(seismicIncidents);
                adapter.setOnItemClickListener(new SeismicIncidentListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(SeismicIncident seismicIncident) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("seismicIncident", seismicIncident);
                        Navigation.findNavController(getView()).navigate(R.id.action_seismic_incidents_to_seismicItem, bundle);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        seismicIncidentViewModel.refreshFromRss().observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if(workInfo != null){
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        refreshButton.setRefreshing(false);
                    }
                    if(workInfo.getState() == WorkInfo.State.FAILED){
                        refreshButton.setRefreshing(false);
                        Snackbar snackbar = Snackbar.make(getView(), "Failed To Retrieve Seismic Incidents",Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                        snackbar.show();
                    }
                }
            }
        });
    }
}
