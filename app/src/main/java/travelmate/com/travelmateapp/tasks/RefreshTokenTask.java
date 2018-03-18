package travelmate.com.travelmateapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.helpers.HttpHandler;

/**
 * Created by joegr on 03/03/2018.
 */

public class RefreshTokenTask extends AsyncTask<String, String, String> {

    private Context context;

    public RefreshTokenTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpHandler httpHandler = new HttpHandler();
        String uid = strings[0];
        String token = strings[1];
        String url = context.getString(R.string.server_url) + "/api/account/refreshtoken?uid=" + uid + "&token=" + token;
        httpHandler.makeServiceCall("PUT", url);
        return token;
    }
}
