package travelmate.com.travelmateapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.models.GJourney;

/**
 * Created by joegr on 09/04/2018.
 */

public class SavedJourneysArrayAdapter extends ArrayAdapter<GJourney> {

    private Context context;
    private ArrayList<GJourney> journeys = new ArrayList<>();

    public SavedJourneysArrayAdapter(Context context, ArrayList<GJourney> journeys) {
        super(context, 0, journeys);
        this.context = context;
        this.journeys = journeys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.saved_journey_list_item, parent, false);
        }

        // Get the data item for this position
        final GJourney journey = journeys.get(position);

        // Lookup view for data population
        TextView from = convertView.findViewById(R.id.saved_journey_from);
        TextView to = convertView.findViewById(R.id.saved_journey_to);
        TextView time = convertView.findViewById(R.id.saved_journey_time);
        TextView period = convertView.findViewById(R.id.saved_journey_period);
        TextView status = convertView.findViewById(R.id.saved_journey_status);

        // Populate the data into the template view using the data object
        from.setText(journey.from);
        to.setText(journey.to);
        time.setText(journey.time);
        period.setText(journey.period);
        status.setText(journey.status);

        // Return the completed view to render on screen
        return convertView;
    }
}
