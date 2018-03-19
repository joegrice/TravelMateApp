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
import travelmate.com.travelmateapp.models.GRoute;
import travelmate.com.travelmateapp.models.GStep;

/**
 * Created by joegr on 18/03/2018.
 */

public class JourneyRoutesArrayAdapter extends ArrayAdapter<GRoute> {

    private Context context;
    private String TAG;
    private ArrayList<GRoute> routes = new ArrayList<>();

    public JourneyRoutesArrayAdapter(Context context, ArrayList<GRoute> routes) {
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

        final GRoute route = routes.get(position);

        // Attach the click event handler
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: SEND CHOICE TO SERVER
                // TODO: SEND USER TO SAVED JOURNEYS PAGE
            }
        });

        // Lookup view for data population
        LinearLayout linearLayout = convertView.findViewById(R.id.journey_list_item_linear);
        linearLayout.removeAllViews();

        // Populate inner LinearLayout
        for (GStep step : route.legs.get(0).steps) {
            View innerLayout = LayoutInflater.from(context).inflate(R.layout.journey_list_item_linear_item, null);
            TextView instruction = innerLayout.findViewById(R.id.linear_instruction);
            TextView name = innerLayout.findViewById(R.id.linear_name);
            TextView shortName = innerLayout.findViewById(R.id.linear_shortname);

            instruction.setText(step.html_instructions);
            if (step.transit_details != null && step.transit_details.line != null) {
                name.setText(step.transit_details.line.name);
                if (step.transit_details.line.short_name != null) {
                    shortName.setText(step.transit_details.line.short_name);
                }
            }
            linearLayout.addView(innerLayout);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
