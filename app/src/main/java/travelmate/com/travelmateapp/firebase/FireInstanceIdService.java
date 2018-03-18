package travelmate.com.travelmateapp.firebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import travelmate.com.travelmateapp.tasks.RefreshTokenTask;

/**
 * Created by joegr on 23/02/2018.
 */

public class FireInstanceIdService extends FirebaseInstanceIdService {
    
    private static final String TAG = FireInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            new RefreshTokenTask(this).execute(auth.getCurrentUser().getUid(), refreshedToken);
            Log.d(TAG, "Token Updated.");
        }
    }
}
