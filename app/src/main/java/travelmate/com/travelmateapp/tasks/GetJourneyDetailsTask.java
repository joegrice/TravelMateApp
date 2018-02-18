package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Instruction;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.models.Leg;
import travelmate.com.travelmateapp.models.Step;

/**
 * Created by joegr on 25/01/2018.
 */

public class GetJourneyDetailsTask extends AsyncTask<Object, Object, Object> {

    private String TAG = AddJourneyActivity.class.getSimpleName();
    public AsyncResponse delegate = null;

    public GetJourneyDetailsTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpHandler sh = new HttpHandler();
        ArrayList<Journey> journeys = new ArrayList<>();
        Context context = (Context) objects[0];
        String locationString = "startlocation=" + encodeUrl(String.valueOf(objects[1])) + "&endlocation=" + encodeUrl(String.valueOf(objects[2]));
        String url = context.getString(R.string.server_url) + "/api/journey/search?" + locationString;
        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray journeysArr = jsonObj.getJSONArray("Journeys");

                for (int i = 0; i < journeysArr.length(); i++) {
                    JSONObject item = journeysArr.getJSONObject(i);
                    Journey newJourney = createJourney(item);
                    journeys.add(newJourney);
                }
            } catch (final JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
        return journeys;
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        try {
            encodedUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedUrl;
    }

    private Journey createJourney(JSONObject journeyJson) throws JSONException {
        Journey journey = new Journey();
        journey.startDateTime = journeyJson.getString("StartDateTime");
        journey.arrivalDateTime = journeyJson.getString("ArrivalDateTime");
        journey.duration = journeyJson.getInt("Duration");
        journey.legs = createLegs(journeyJson.getJSONArray("Legs"));
        return journey;
    }

    private ArrayList<Leg> createLegs(JSONArray legsJson) throws JSONException {
        ArrayList<Leg> legs = new ArrayList<>();
        for (int i = 0; i < legsJson.length(); i++) {
            JSONObject item = legsJson.getJSONObject(i);
            Leg leg = new Leg();
            leg.duration = item.getInt("Duration");
            leg.instruction = createInstruction(item.getJSONObject("Instruction"));
            legs.add(leg);
        }
        return legs;
    }

    private Instruction createInstruction(JSONObject instructionJson) throws JSONException {
        Instruction instruction = new Instruction();
        instruction.detailed = instructionJson.getString("Detailed");
        instruction.summary = instructionJson.getString("Summary");
        instruction.steps = createSteps(instructionJson.getJSONArray("Steps"));
        return instruction;
    }

    private ArrayList<Step> createSteps(JSONArray stepsJson) throws JSONException {
        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 0; i < stepsJson.length(); i++) {
            JSONObject item = stepsJson.getJSONObject(i);
            Step step = new Step();
            step.distance = item.getInt("Distance");
            step.streetName = item.getString("StreetName");
            step.description = item.getString("Description");
            step.descriptionHeading = item.getString("DescriptionHeading");
            step.turnDirection = item.getString("TurnDirection");
            steps.add(step);
        }
        return steps;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}

