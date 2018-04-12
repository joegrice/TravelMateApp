package travelmate.com.travelmateapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.helpers.TimePickerFragment;
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void addJourney() {
        final GJourney userJourney = new GJourney();
        EditText startLocation = findViewById(R.id.editText_StartLocation);
        userJourney.from = startLocation.getText().toString();
        EditText endLocation = findViewById(R.id.editText_EndLocation);
        userJourney.to = endLocation.getText().toString();
        TextView time = findViewById(R.id.journey_set_time_value);
        userJourney.time = time.getText().toString();
        Spinner spinner = findViewById(R.id.period_spinner);
        userJourney.period = spinner.getSelectedItem().toString();


        if (startLocation.length() > 0 && endLocation.length() > 0) {
            GetJourneyDetailsTask asyncTask = new GetJourneyDetailsTask(new AsyncResponse() {

                @Override
                public void processFinish(Object output) {
                    GJourney outputJourney = (GJourney) output;
                    userJourney.routes = outputJourney.routes;
                    moveToSelectPage(userJourney);
                }
            });
            asyncTask.execute(getApplicationContext(), userJourney);
        }

    }

    private void moveToSelectPage(GJourney journey) {
        Intent intent = new Intent(this, SelectJourneyActivity.class);
        Gson gson = new Gson();
        String userJourney = gson.toJson(journey);
        intent.putExtra("userJourney", userJourney);
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
