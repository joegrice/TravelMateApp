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
        HttpHandler handler = new HttpHandler();
        GJourney journey = new GJourney();
        Context context = (Context) objects[0];
        GJourney userJourney = (GJourney) objects[1];
        String locationString = "startlocation=" + handler.encodeUrl(userJourney.from) +
                "&endlocation=" + handler.encodeUrl(userJourney.to) + "&time=" + handler.encodeUrl(userJourney.time);
        String url = context.getString(R.string.server_url) + "/api/journey/search?" + locationString;
        String jsonStr = handler.makeServiceCall("GET", url);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                journey = new GJourney(jsonObj);
            } catch (final JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
        return journey;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}

