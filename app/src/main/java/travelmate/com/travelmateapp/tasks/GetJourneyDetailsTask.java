package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Journey;

/**
 * Created by joegr on 25/01/2018.
 */

public class GetJourneyDetailsTask extends AsyncTask<Object, Object, Object> {

    private String TAG = AddJourneyActivity.class.getSimpleName();
    public AsyncResponse delegate;

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
        Journey journey = new Journey();
        Context context = (Context) objects[0];
        Journey userJourney = (Journey) objects[1];
        String locationString = "startlocation=" + handler.encodeUrl(userJourney.from) +
                "&endlocation=" + handler.encodeUrl(userJourney.to) + "&time=" + handler.encodeUrl(userJourney.time);
        String url = context.getString(R.string.server_url) + "/api/journey/search?" + locationString;
        String jsonStr = handler.makeServiceCall("GET", url);

        if (jsonStr != null) {
            Gson gson = new Gson();
            journey = gson.fromJson(jsonStr, Journey.class);
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

