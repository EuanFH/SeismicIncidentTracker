package net.chinzer.seismicincidenttracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SeismicList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshButton;
    private RecyclerView recyclerView;
    private SeismicIncidentViewModel seismicIncidentViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seismic_list, container, false);
        // Inflate the layout for this fragment
        refreshButton = v.findViewById(R.id.swiperefresh);
        refreshButton.setOnRefreshListener(this);
        recyclerView = v.findViewById(R.id.recyclerview);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);

        final SeismicIncidentListAdapter adapter = new SeismicIncidentListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        seismicIncidentViewModel.getAllSeismicIncidents().observe(getViewLifecycleOwner(), new Observer<List<SeismicIncident>>() {
            @Override
            public void onChanged(@Nullable final List<SeismicIncident> seismicIncidents) {
                // Update the cached copy of the words in the adapter.
                adapter.setSeismicIncidents(seismicIncidents);
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
