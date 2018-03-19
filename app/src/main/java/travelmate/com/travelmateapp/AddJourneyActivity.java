package travelmate.com.travelmateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.GJourney;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

public class AddJourneyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        Button searchButton = findViewById(R.id.button_JourneySearch);
        searchButton.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationHelper navListener = new NavigationHelper(this);
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.getMenu().getItem(0).setChecked(true);
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
                    GJourney journey = (GJourney) output;
                    moveToSelectPage(journey);
                }
            });
            asyncTask.execute(getApplicationContext(), startLocationString, endLocationString);
        }
    }

    private void moveToSelectPage(GJourney journey) {
        Intent intent = new Intent(this, SelectJourneyActivity.class);
        Gson gson = new Gson();
        String routes = gson.toJson(journey);
        intent.putExtra("routes", routes);
        this.startActivity(intent);
        finish();
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
