package travelmate.com.travelmateapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import travelmate.com.travelmateapp.MainActivity;
import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.models.GRoute;
import travelmate.com.travelmateapp.models.GStep;
import travelmate.com.travelmateapp.tasks.SelectJourneyTask;

/**
 * Created by joegr on 18/03/2018.
 */

public class JourneyRoutesArrayAdapter extends ArrayAdapter<GRoute> {

    private Context context;
    private String TAG;
    private GJourney userJourney;
    private String uid;
    private ArrayList<GRoute> routes = new ArrayList<>();

    public JourneyRoutesArrayAdapter(Context context, String uid, GJourney userJourney) {
        super(context, 0, userJourney.routes);
        this.context = context;
        this.uid = uid;
        this.routes = userJourney.routes;
        TAG = context.getClass().getSimpleName();
        this.userJourney = userJourney;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.journey_list_item, parent, false);
        }

        // Get the data item for this position
        final GRoute route = routes.get(position);

        // Attach the click event handler
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String routeJson = gson.toJson(route);
                userJourney.from = route.legs.get(0).start_address;
                userJourney.to = route.legs.get(route.legs.size() - 1).end_address;
                SelectJourneyTask selectJourneyTask = new SelectJourneyTask(new AsyncResponse() {

                    @Override
                    public void processFinish(Object output) {
                        Intent viewJourneys = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(viewJourneys);
                    }
                });
                selectJourneyTask.execute(context, uid, routeJson, userJourney);
            }
        });

        // Lookup view for data population
        LinearLayout linearLayout = convertView.findViewById(R.id.journey_list_item_linear);
        linearLayout.removeAllViews();

        // Populate inner LinearLayout
        for (GStep step : route.legs.get(0).steps) {
            View innerLayout = LayoutInflater.from(context).inflate(R.layout.journey_list_item_linear_item, null);
            TextView stepText = innerLayout.findViewById(R.id.linear_step);
            TextView instruction = innerLayout.findViewById(R.id.linear_instruction);
            TextView name = innerLayout.findViewById(R.id.linear_name);
            LinearLayout nameLayout = innerLayout.findViewById(R.id.linear_name_layout);
            TextView shortName = innerLayout.findViewById(R.id.linear_shortname);
            LinearLayout shortNameLayout = innerLayout.findViewById(R.id.linear_shortname_layout);

            int stepNum = route.legs.get(0).steps.indexOf(step);
            stepText.setText("Step: " + ++stepNum);
            instruction.setText(step.html_instructions);
            if (step.transit_details != null && step.transit_details.line != null) {
                if (step.transit_details.line.name != null
                        && !step.transit_details.line.name.equals("null")) {
                    name.setText(step.transit_details.line.name);
                } else {
                    nameLayout.setVisibility(View.GONE);
                }
                if (step.transit_details.line.short_name != null
                        && !step.transit_details.line.short_name.equals("null")) {
                    shortName.setText(step.transit_details.line.short_name);
                } else {
                    shortNameLayout.setVisibility(View.GONE);
                }
            } else {
                nameLayout.setVisibility(View.GONE);
                shortNameLayout.setVisibility(View.GONE);
            }
            linearLayout.addView(innerLayout);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
