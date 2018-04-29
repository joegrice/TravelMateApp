package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;

/**
 * Created by joegr on 19/03/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class DeleteJourneyTask extends AsyncTask<Object, Object, Object> {

    private AsyncResponse delegate;

    public DeleteJourneyTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        Context context = (Context) objects[0];
        int journeyId = (int) objects[1];
        HttpHandler handler = new HttpHandler();
        String params = "id=" + journeyId;
        String url = context.getString(R.string.server_url) + "/api/journey/delete?" + params;
        handler.makeServiceCall("DELETE", url);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}
