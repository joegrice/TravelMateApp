package travelmate.com.travelmateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import travelmate.com.travelmateapp.adapters.AlternativeRoutesArrayAdapter;
import travelmate.com.travelmateapp.adapters.DisruptedLinesArrayAdapter;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.DbLine;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.models.GRoute;
import travelmate.com.travelmateapp.models.GStep;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

public class SavedJourneyActivity extends AppCompatActivity {

    private TextView progressBarText;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.savedJourneyProgressBar);
        progressBarText = progressBar.findViewById(R.id.progressBarText);

        Intent intent = getIntent();
        Gson gson = new Gson();
        GJourney journey = gson.fromJson(intent.getStringExtra("journeyJson"), GJourney.class);
        displaySavedDetails(journey);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void displaySavedDetails(GJourney journey) {
        setTitle(journey.name);
        TextView status = findViewById(R.id.activity_saved_journey_status);
        status.setText(journey.status);

        if (journey.disruptedLines != null && journey.disruptedLines.size() > 0) {
            TextView disruptedLinesLabel = findViewById(R.id.disruptedLinesLabel);
            disruptedLinesLabel.setText("Disrupted Line(s):");
            disruptedLinesLabel.setVisibility(View.VISIBLE);
            ListView disruptedLinesList = findViewById(R.id.disruptedLinesList);
            disruptedLinesList.setVisibility(View.VISIBLE);
            DisruptedLinesArrayAdapter adapter = new DisruptedLinesArrayAdapter(getApplicationContext(), journey.disruptedLines);
            disruptedLinesList.setAdapter(adapter);
            getAlternativeRoutes(journey);
        } else {
            TextView alternativeRouteLabel = findViewById(R.id.altRoutesLabel);
            alternativeRouteLabel.setText("Route:");
            alternativeRouteLabel.setVisibility(View.VISIBLE);
            AlternativeRoutesArrayAdapter adapter = new AlternativeRoutesArrayAdapter(getApplicationContext(), journey.routes);
            ListView listView = findViewById(R.id.altJourneyList);
            listView.setAdapter(adapter);
        }
    }

    private void getAlternativeRoutes(final GJourney journey) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setText("Finding Alternative Routes...");
        GetJourneyDetailsTask asyncTask = new GetJourneyDetailsTask(new AsyncResponse() {

            @Override
            public void processFinish(Object output) {
                GJourney outputJourney = (GJourney) output;
                ArrayList<GRoute> validRoutes = getValidRoutes(outputJourney.routes, journey.disruptedLines);
                if (!validRoutes.isEmpty()) {
                    TextView alternativeRouteLabel = findViewById(R.id.altRoutesLabel);
                    alternativeRouteLabel.setText("Alternative Route(s):");
                    alternativeRouteLabel.setVisibility(View.VISIBLE);
                    AlternativeRoutesArrayAdapter adapter = new AlternativeRoutesArrayAdapter(getApplicationContext(), validRoutes);
                    ListView listView = findViewById(R.id.altJourneyList);
                    listView.setAdapter(adapter);
                } else {
                    TextView altRoutesText = findViewById(R.id.altRoutesText);
                    altRoutesText.setText("Unable to find a new transit route without disruptions!");
                    altRoutesText.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        asyncTask.execute(getApplicationContext(), journey);
    }

    private ArrayList<GRoute> getValidRoutes(ArrayList<GRoute> routes, ArrayList<DbLine> disruptedLines) {
        ArrayList<GRoute> validRoutes = new ArrayList<>();
        for (GRoute route : routes) {
            boolean containsDelayedLine = false;
            for (GStep step : route.legs.get(0).steps) {
                if (step.transit_details != null && step.transit_details.line != null) {
                    for (DbLine dbLine : disruptedLines) {
                        if (step.transit_details.line.name.equals(dbLine.Name)
                                || step.transit_details.line.short_name.equals(dbLine.Name)) {
                            containsDelayedLine = true;
                            break;
                        }
                    }
                }
            }
            if (!containsDelayedLine) {
                validRoutes.add(route);
            }
        }
        return validRoutes;
    }
}