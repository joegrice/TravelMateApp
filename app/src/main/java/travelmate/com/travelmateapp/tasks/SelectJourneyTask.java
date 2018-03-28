package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;

/**
 * Created by joegr on 19/03/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class SelectJourneyTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private String uid;
    private String route;
    private String startLocation;
    private String endLocation;

    public SelectJourneyTask(Context context, String uid, String route, String startLocation, String endLocation) {
        this.context = context;
        this.uid = uid;
        this.route = route;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpHandler handler = new HttpHandler();
        String params = "uid=" + uid + "&route=" + route
                + "&startlocation=" + startLocation
                + "&endlocation=" + endLocation;
        String url = context.getString(R.string.server_url) + "/api/journey/select?" + params;
        handler.makeServiceCall("PUT", url);
        return null;
    }
}
