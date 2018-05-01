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
import travelmate.com.travelmateapp.helpers.ProgressBarUpdater;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.DbLine;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.models.Route;
import travelmate.com.travelmateapp.models.Step;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

public class SavedJourneyActivity extends AppCompatActivity {

    private ProgressBarUpdater progressBarUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View progressBar = findViewById(R.id.savedJourneyProgressBar);
        progressBarUpdater = new ProgressBarUpdater(progressBar);

        Intent intent = getIntent();
        Gson gson = new Gson();
        Journey journey = gson.fromJson(intent.getStringExtra("journeyJson"), Journey.class);
        setTitle(journey.name);
        displaySavedDetails(journey);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void displaySavedDetails(Journey journey) {
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

    private void getAlternativeRoutes(final Journey journey) {
        progressBarUpdater.updateProgress("Finding Alternative Routes...");
        GetJourneyDetailsTask asyncTask = new GetJourneyDetailsTask(new AsyncResponse() {

            @Override
            public void processFinish(Object output) {
                Journey outputJourney = (Journey) output;
                ArrayList<Route> validRoutes = getValidRoutes(outputJourney.routes, journey.disruptedLines);
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
                progressBarUpdater.updateProgressVisibility();
            }
        });

        asyncTask.execute(getApplicationContext(), journey);
    }

    private ArrayList<Route> getValidRoutes(ArrayList<Route> routes, ArrayList<DbLine> disruptedLines) {
        ArrayList<Route> validRoutes = new ArrayList<>();
        for (Route route : routes) {
            boolean containsDelayedLine = false;
            for (Step step : route.legs.get(0).steps) {
                if (step.transit_details != null && step.transit_details.line != null) {
                    for (DbLine dbLine : disruptedLines) {
                        if (step.transit_details.line.name.equals(dbLine.Name)
                                || (step.transit_details.line.short_name != null
                                && step.transit_details.line.short_name.equals(dbLine.Name))) {
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