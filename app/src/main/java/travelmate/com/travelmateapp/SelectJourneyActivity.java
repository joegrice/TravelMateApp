package travelmate.com.travelmateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import travelmate.com.travelmateapp.adapters.JourneyRoutesArrayAdapter;
import travelmate.com.travelmateapp.models.GJourney;

public class SelectJourneyActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_journey);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String routes = intent.getStringExtra("routes");
        Gson gson = new Gson();
        GJourney journey = gson.fromJson(routes, GJourney.class);

        JourneyRoutesArrayAdapter adapter = new JourneyRoutesArrayAdapter(this, mAuth.getCurrentUser().getUid(), journey.routes);
        ListView listView = findViewById(R.id.journeySelectList);
        listView.setAdapter(adapter);
    }
}
