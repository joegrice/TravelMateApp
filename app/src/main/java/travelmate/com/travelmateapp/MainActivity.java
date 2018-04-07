package travelmate.com.travelmateapp;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import travelmate.com.travelmateapp.helpers.NavigationHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationHelper navListener = new NavigationHelper(this);
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.getMenu().getItem(1).setChecked(true);
    }
}
