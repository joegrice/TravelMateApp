package travelmate.com.travelmateapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import travelmate.com.travelmateapp.helpers.NavDrawerListener;
import travelmate.com.travelmateapp.helpers.NavigationHelper;
import travelmate.com.travelmateapp.helpers.TimePickerFragment;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

public class AddJourneyActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        Button searchButton = findViewById(R.id.button_JourneySearch);
        searchButton.setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.add_journey_drawer_layout);
        NavDrawerListener navDrawerListener = new NavDrawerListener(navigationView, mAuth);
        mDrawerLayout.addDrawerListener(navDrawerListener);
        NavigationHelper navListener = new NavigationHelper(getApplicationContext(), mDrawerLayout);
        navigationView.setNavigationItemSelectedListener(navListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeKeyboard();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void addJourney() {
        final Journey userJourney = new Journey();
        EditText name = findViewById(R.id.editText_name);
        userJourney.name = name.getText().toString();
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
                    Journey outputJourney = (Journey) output;
                    userJourney.routes = outputJourney.routes;
                    moveToSelectPage(userJourney);
                }
            });
            asyncTask.execute(getApplicationContext(), userJourney);
        }
    }

    private void moveToSelectPage(Journey journey) {
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
