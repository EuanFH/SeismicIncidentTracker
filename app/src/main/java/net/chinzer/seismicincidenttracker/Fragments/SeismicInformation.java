package net.chinzer.seismicincidenttracker.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.chinzer.seismicincidenttracker.Adapters.SeismicIncidentInformationAdapter;
import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.R;
import net.chinzer.seismicincidenttracker.ViewModels.SeismicIncidentViewModel;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SeismicInformation extends Fragment {

    private SwipeRefreshLayout refreshButton;
    private RecyclerView recyclerView;
    private SeismicIncidentViewModel seismicIncidentViewModel;
    private ConstraintLayout emptyResultsMessage;
    private TextView emptyResultsText;
    private ImageView emptyResultsImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seismic_information, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.recyclerview);
        emptyResultsMessage = view.findViewById(R.id.empty_results_message);
        emptyResultsImage = view.findViewById(R.id.empty_results_message_image);
        emptyResultsText = view.findViewById(R.id.empty_results_message_text);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        seismicIncidentViewModel = ViewModelProviders.of(getActivity()).get(SeismicIncidentViewModel.class);

        final SeismicIncidentInformationAdapter adapter = new SeismicIncidentInformationAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        seismicIncidentViewModel.getSeismicIncidents().observe(getViewLifecycleOwner(), new Observer<List<SeismicIncident>>() {
            @Override
            public void onChanged(@Nullable final List<SeismicIncident> seismicIncidents) {
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
                new LoadAdapterWithSeismicInfromation(adapter, seismicIncidentViewModel, getView()).execute();

            }
        });
    }

    private static class LoadAdapterWithSeismicInfromation extends AsyncTask<Void, Void, Map<String, SeismicIncident>> {

        SeismicIncidentInformationAdapter adapter;
        SeismicIncidentViewModel seismicIncidentViewModel;
        View view;

        public LoadAdapterWithSeismicInfromation(SeismicIncidentInformationAdapter adapter, SeismicIncidentViewModel seismicIncidentViewModel, View view){
            this.adapter = adapter;
            this.seismicIncidentViewModel = seismicIncidentViewModel;
            this.view = view;
        }

        @Override
        protected Map<String, SeismicIncident> doInBackground(Void... params) {
            return seismicIncidentViewModel.getInformationSeismicIncidents();
        }

        @Override
        protected void onPostExecute(Map<String, SeismicIncident> seismicInformation) {
            adapter.setSeismicIncidents(seismicInformation);
            adapter.setOnItemClickListener(new SeismicIncidentInformationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SeismicIncident seismicIncident) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("seismicIncident", seismicIncident);
                    Navigation.findNavController(view).navigate(R.id.action_information_to_seismicItem, bundle);
                }
            });
        }

    }
}
