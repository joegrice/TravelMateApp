package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.GJourney;

/**
 * Created by joegr on 19/03/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class SelectJourneyTask extends AsyncTask<Object, Object, Object> {

    private AsyncResponse delegate;

    public SelectJourneyTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        Context context = (Context) objects[0];
        String uid = (String) objects[1];
        String route = (String) objects[2];
        GJourney userJourney = (GJourney) objects[3];
        HttpHandler handler = new HttpHandler();
        String params = "uid=" + uid + "&name=" + userJourney.name + "&route=" + route
                + "&startlocation=" + userJourney.from + "&endlocation=" + userJourney.to
                + "&time=" + userJourney.time + "&period=" + userJourney.period;
        String url = context.getString(R.string.server_url) + "/api/journey/select?" + params;
        handler.makeServiceCall("PUT", url);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}
