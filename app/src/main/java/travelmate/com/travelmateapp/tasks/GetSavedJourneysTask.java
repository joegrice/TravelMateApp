package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.DbLine;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.models.JourneyStatus;

/**
 * Created by joegr on 25/01/2018.
 */

public class GetSavedJourneysTask extends AsyncTask<Object, Object, Object> {

    private String TAG = AddJourneyActivity.class.getSimpleName();
    public AsyncResponse delegate;

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

        ArrayList<Journey> journeys = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currentJson = jsonArray.getJSONObject(i);
                    Gson gson = new Gson();
                    Journey journey = gson.fromJson(currentJson.toString(), Journey.class);
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

    private void addDisruptedLines(JSONObject json, Journey journey) throws JSONException {
        JSONArray disruptedLinesJsonArray = json.getJSONArray("disruptedLines");
        ArrayList<DbLine> disruptedLines = new ArrayList<>();
        Gson gson = new Gson();
        for (int j = 0; j < disruptedLinesJsonArray.length(); j++) {
            JSONObject dLine = disruptedLinesJsonArray.getJSONObject(j);
            DbLine dbLine = gson.fromJson(dLine.toString(), DbLine.class);
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

