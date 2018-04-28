package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.jar.Attributes;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.DbLine;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.models.GLeg;
import travelmate.com.travelmateapp.models.GLine;
import travelmate.com.travelmateapp.models.GRoute;
import travelmate.com.travelmateapp.models.GStep;
import travelmate.com.travelmateapp.models.GTransitDetails;
import travelmate.com.travelmateapp.models.JourneyStatus;

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
        HttpHandler handler = new HttpHandler();
        String paramString = "uid=" + handler.encodeUrl(uid);
        String url = context.getString(R.string.server_url) + "/api/journey/saved?" + paramString;
        String jsonStr = handler.makeServiceCall("GET", url);

        ArrayList<GJourney> journeys = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currentJson = jsonArray.getJSONObject(i);
                    GJourney journey = new GJourney(currentJson);
                    if (journey.status.equals(JourneyStatus.Delayed)) {
                        addDisruptedLines(currentJson, journey);
                    }
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

    private void addDisruptedLines(JSONObject json, GJourney journey) throws JSONException {
        JSONArray disruptedLinesJsonArray = json.getJSONArray("disruptedLines");
        ArrayList<DbLine> disruptedLines = new ArrayList<>();
        for (int j = 0; j < disruptedLinesJsonArray.length(); j++) {
            JSONObject dLine = disruptedLinesJsonArray.getJSONObject(j);
            DbLine dbLine = new DbLine(dLine);
            disruptedLines.add(dbLine);
        }
        journey.disruptedLines = disruptedLines;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}

