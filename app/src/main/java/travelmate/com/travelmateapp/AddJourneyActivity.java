package travelmate.com.travelmateapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

public class AddJourneyActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            NavigationHelper navigationHelper = new NavigationHelper(AddJourneyActivity.this);
            navigationHelper.onBottomNavigationViewClick(item);
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        Button searchButton = findViewById(R.id.button_JourneySearch);
        searchButton.setOnClickListener(this);
    }

    public void addJourney() {
        EditText startLocation = findViewById(R.id.editText_StartLocation);
        String startLocationString = startLocation.getText().toString();
        EditText endLocation = findViewById(R.id.editText_EndLocation);
        String endLocationString = endLocation.getText().toString();

        if (startLocation.length() > 0 && endLocation.length() > 0) {
            GetJourneyDetailsTask asyncTask = new GetJourneyDetailsTask(new AsyncResponse() {

                @Override
                public void processFinish(Object output) {
                    ArrayList<Journey> journeyArrayList = (ArrayList<Journey>) output;
                    //spinner.setVisibility(View.GONE);
                }
            });
            asyncTask.execute(getApplicationContext(), startLocationString, endLocationString);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_JourneySearch:
                addJourney();
                break;
        }
    }
}
