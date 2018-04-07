package travelmate.com.travelmateapp.firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by joegr on 23/02/2018.
 */

public class FireMessagingService extends FirebaseMessagingService {

    private static final String TAG = FireMessagingService.class.getSimpleName();

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
