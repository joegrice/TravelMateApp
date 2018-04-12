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
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.models.GLeg;
import travelmate.com.travelmateapp.models.GLine;
import travelmate.com.travelmateapp.models.GRoute;
import travelmate.com.travelmateapp.models.GStep;
import travelmate.com.travelmateapp.models.GTransitDetails;

/**
 * Created by joegr on 25/01/2018.
 */

public class GetSavedJourneysTask extends AsyncTask<Object, Object, Object> {

    private String TAG = AddJourneyActivity.class.getSimpleName();
    public AsyncResponse delegate = null;

    public GetSavedJourneysTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Context context = (Context) objects[0];
        String uid = (String) objects[1];
        String paramString = "uid=" + encodeUrl(uid);
        String url = context.getString(R.string.server_url) + "/api/journey/saved?" + paramString;
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall("GET", url);

        ArrayList<GJourney> journeys = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currentJson = jsonArray.getJSONObject(i);
                    GJourney journey = createJourney(currentJson);
                    journey.from = currentJson.getString("from");
                    journey.to = currentJson.getString("to");
                    journey.time = currentJson.getString("time");
                    journey.period = currentJson.getString("period");
                    journeys.add(journey);
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

    private GJourney createJourney(JSONObject journeyJson) throws JSONException {
        GJourney journey = new GJourney();
        journey.routes = createRoutes(journeyJson.getJSONArray("routes"));
        return journey;
    }

    private ArrayList<GRoute> createRoutes(JSONArray routesJson) throws JSONException {
        ArrayList<GRoute> routes = new ArrayList<>();
        for (int i = 0; i < routesJson.length(); i++) {
            JSONObject item = routesJson.getJSONObject(i);
            GRoute route = new GRoute();
            route.legs = createLegs(item.getJSONArray("legs"));
            routes.add(route);
        }
        return routes;
    }

    private ArrayList<GLeg> createLegs(JSONArray legsJson) throws JSONException {
        ArrayList<GLeg> legs = new ArrayList<>();
        for (int i = 0; i < legsJson.length(); i++) {
            JSONObject item = legsJson.getJSONObject(i);
            GLeg leg = new GLeg();
            leg.start_address = item.getString("start_address");
            leg.end_address = item.getString("end_address");
            leg.steps = createSteps(item.getJSONArray("steps"));
            legs.add(leg);
        }
        return legs;
    }

    private ArrayList<GStep> createSteps(JSONArray stepsJson) throws JSONException {
        ArrayList<GStep> steps = new ArrayList<>();
        for (int i = 0; i < stepsJson.length(); i++) {
            JSONObject item = stepsJson.getJSONObject(i);
            GStep step = new GStep();
            step.html_instructions = item.getString("html_instructions");
            if (!item.isNull("transit_details")) {
                step.transit_details = createTransitDetails(item.getJSONObject("transit_details"));
            }
            steps.add(step);
        }
        return steps;
    }

    private GTransitDetails createTransitDetails(JSONObject transit_details) throws JSONException {
        GTransitDetails transitDetails = new GTransitDetails();
        JSONObject lineJson = transit_details.getJSONObject("line");
        GLine line = new GLine();
        line.name = lineJson.getString("name");
        line.short_name = lineJson.getString("short_name");
        transitDetails.line = line;
        return transitDetails;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}

