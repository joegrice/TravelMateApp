package travelmate.com.travelmateapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.SavedJourneyActivity;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.models.JourneyStatus;
import travelmate.com.travelmateapp.models.ViewHolder;
import travelmate.com.travelmateapp.tasks.DeleteJourneyTask;

/**
 * Created by joegr on 09/04/2018.
 */

public class SavedJourneysArrayAdapter extends ArrayAdapter<Journey> {

    private Context context;
    private ArrayList<Journey> journeys;
    private final ViewBinderHelper binderHelper;

    public SavedJourneysArrayAdapter(Context context, ArrayList<Journey> journeys) {
        super(context, 0, journeys);
        this.context = context;
        this.journeys = journeys;
        binderHelper = new ViewBinderHelper();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.saved_journey_list_item, parent, false);

            holder = new ViewHolder();
            holder.swipeLayout = convertView.findViewById(R.id.saved_journey_list_item_swipe_layout);
            holder.frontView = convertView.findViewById(R.id.front_layout);
            holder.deleteView = convertView.findViewById(R.id.delete_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        final Journey journey = journeys.get(position);

        // Attach the click event handler
        if (journey != null) {
            binderHelper.bind(holder.swipeLayout, journey.name);

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteJourneyTask deleteJourneyTask = new DeleteJourneyTask(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            remove(journey);
                        }
                    });
                    deleteJourneyTask.execute(context, journey.id);
                }
            });
            holder.frontView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    String journeyJson = gson.toJson(journey);
                    Intent savedJourney = new Intent(context, SavedJourneyActivity.class);
                    savedJourney.putExtra("journeyJson", journeyJson);
                    context.startActivity(savedJourney);
                }
            });
        }

        // Lookup view for data population
        TextView name = convertView.findViewById(R.id.saved_journey_name);
        TextView from = convertView.findViewById(R.id.saved_journey_from);
        TextView to = convertView.findViewById(R.id.saved_journey_to);
        TextView time = convertView.findViewById(R.id.saved_journey_time);
        TextView period = convertView.findViewById(R.id.saved_journey_period);
        TextView status = convertView.findViewById(R.id.saved_journey_status);

        // Populate the data into the template view using the data object
        name.setText(journey.name);
        from.setText(journey.from);
        to.setText(journey.to);
        time.setText(journey.time);
        period.setText(journey.period);
        status.setText(journey.status);
        if (journey.status.equals(JourneyStatus.Delayed)) {
            status.setTextColor(Color.RED);
        } else if (journey.status.equals(JourneyStatus.GoodService)) {
            status.setTextColor(Color.parseColor("#8BC34A"));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Restore open/close state when the orientation is changed.
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Restore open/close state when the orientation is changed.}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
}
