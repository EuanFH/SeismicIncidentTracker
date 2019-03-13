package net.chinzer.seismicincidenttracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SeismicList extends FragmentSortSearchActionBar implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshButton;
    private RecyclerView recyclerView;
    private SeismicIncidentViewModel seismicIncidentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seismic_list, container, false);
        // Inflate the layout for this fragment
        refreshButton = view.findViewById(R.id.swiperefresh);
        refreshButton.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.recyclerview);
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
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        //new Thread(new Task(urlSource)).start();
        new Thread(new Task()).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        @Override
        public void run()
        {
            try{
                for (SeismicIncident seismicIncident : Rss.seismicIncidents()){
                    seismicIncidentViewModel.insert(seismicIncident);
                }
            }
            catch(Exception e){
                Log.e("MyTag", e.getMessage());
                e.printStackTrace();
            }
            refreshButton.setRefreshing(false);
        }

    }

}
