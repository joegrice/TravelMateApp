package travelmate.com.travelmateapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.models.Route;
import travelmate.com.travelmateapp.models.Step;

/**
 * Created by joegr on 18/03/2018.
 */

public class AlternativeRoutesArrayAdapter extends ArrayAdapter<Route> {

    private Context context;
    private String TAG;
    private ArrayList<Route> routes;

    public AlternativeRoutesArrayAdapter(Context context, ArrayList<Route> routes) {
        super(context, 0, routes);
        this.context = context;
        this.routes = routes;
        TAG = context.getClass().getSimpleName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.journey_list_item, parent, false);
        }

        // Get the data item for this position
        final Route route = routes.get(position);

        // Lookup view for data population
        LinearLayout linearLayout = convertView.findViewById(R.id.journey_list_item_linear);
        linearLayout.removeAllViews();

        // Populate inner LinearLayout
        for (Step step : route.legs.get(0).steps) {
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
