package travelmate.com.travelmateapp;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import travelmate.com.travelmateapp.adapters.SavedJourneysArrayAdapter;
import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.tasks.GetSavedJourneysTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationHelper navListener = new NavigationHelper(getApplicationContext());
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.getMenu().getItem(1).setChecked(true);

        getSavedJourneys();
    }

    private void getSavedJourneys() {
        GetSavedJourneysTask asyncTask = new GetSavedJourneysTask(new AsyncResponse() {

            @Override
            public void processFinish(Object output) {
                ArrayList<GJourney> journeys = (ArrayList<GJourney>) output;
                SavedJourneysArrayAdapter adapter = new SavedJourneysArrayAdapter(getApplicationContext(), journeys);
                ListView listView = findViewById(R.id.savedJourneysListView);
                listView.setAdapter(adapter);
            }
        });
        asyncTask.execute(getApplicationContext(), mAuth.getCurrentUser().getUid());
    }
}
