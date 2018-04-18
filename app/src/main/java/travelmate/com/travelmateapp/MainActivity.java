package travelmate.com.travelmateapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import travelmate.com.travelmateapp.adapters.SavedJourneysArrayAdapter;
import travelmate.com.travelmateapp.helpers.NavDrawerListener;
import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.tasks.GetSavedJourneysTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        NavDrawerListener navDrawerListener = new NavDrawerListener(navigationView, mAuth);
        mDrawerLayout.addDrawerListener(navDrawerListener);
        NavigationHelper navListener = new NavigationHelper(getApplicationContext(), mDrawerLayout);
        navigationView.setNavigationItemSelectedListener(navListener);

        getSavedJourneys();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
