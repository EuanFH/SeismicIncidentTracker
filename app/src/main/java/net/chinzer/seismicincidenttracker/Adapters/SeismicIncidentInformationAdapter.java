package net.chinzer.seismicincidenttracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.chinzer.seismicincidenttracker.Model.SeismicIncident;
import net.chinzer.seismicincidenttracker.R;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class SeismicIncidentInformationAdapter extends RecyclerView.Adapter<SeismicIncidentInformationAdapter.SeismicIncidentViewHolder>{

    private final LayoutInflater inflater;
    private Map<String, SeismicIncident> seismicIncidentInformation;
    private OnItemClickListener listener;

    class SeismicIncidentViewHolder extends RecyclerView.ViewHolder {
        private final TextView header;
        private final TextView locality;
        private final TextView date;
        private final TextView time;
        private final TextView depth;
        private final TextView magnitude;
        private final TextView severity;


        public SeismicIncidentViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            locality = itemView.findViewById(R.id.locality);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            magnitude = itemView.findViewById(R.id.magnitude);
            severity = itemView.findViewById(R.id.severity);
            depth = itemView.findViewById(R.id.depth);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        Object seismicIncidentInfromationItemKey = seismicIncidentInformation.keySet().toArray()[getAdapterPosition()];
                        listener.onItemClick(seismicIncidentInformation.get(seismicIncidentInfromationItemKey));
                    }
                }
            });
        }
    }

    public SeismicIncidentInformationAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public SeismicIncidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_with_heading, parent, false);
        return new SeismicIncidentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SeismicIncidentViewHolder holder, int position) {
        if (seismicIncidentInformation != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            Object seismicIncidentInfromationItemKey = seismicIncidentInformation.keySet().toArray()[position];
            SeismicIncident current = seismicIncidentInformation.get(seismicIncidentInfromationItemKey);
            holder.header.setText(seismicIncidentInfromationItemKey.toString());
            holder.locality.setText(current.getLocality());
            holder.date.setText(current.getDateTime().format(dateFormatter));
            holder.time.setText(current.getDateTime().format(timeFormatter));
            holder.depth.setText(current.getDepth() + "km");
            holder.magnitude.setText(String.valueOf(current.getMagnitude()));
            holder.severity.setText(String.valueOf(current.getSeverity()));
            switch(current.getSeverity()){
                case("Micro"):
                    holder.magnitude.setBackgroundResource(R.color.micro);
                    break;
                case("Minor"):
                    holder.magnitude.setBackgroundResource(R.color.minor);
                    break;
                case("Light"):
                    holder.magnitude.setBackgroundResource(R.color.light);
                    break;
                case("Moderate"):
                    holder.magnitude.setBackgroundResource(R.color.moderate);
                    break;
                case("Strong"):
                    holder.magnitude.setBackgroundResource(R.color.strong);
                    break;
                case("Major"):
                    holder.magnitude.setBackgroundResource(R.color.major);
                    break;
                case("Great"):
                    holder.magnitude.setBackgroundResource(R.color.great);
                    break;
            }

        }
    }

    public void setSeismicIncidents(Map<String, SeismicIncident> seismicIncidentInfromation){
        this.seismicIncidentInformation = seismicIncidentInfromation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (seismicIncidentInformation != null)
            return seismicIncidentInformation.size();
        else return 0;
    }

    public interface OnItemClickListener{
        void onItemClick(SeismicIncident seismicIncident);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
