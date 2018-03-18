package travelmate.com.travelmateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import travelmate.com.travelmateapp.helpers.NavigationHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationHelper navListener = new NavigationHelper(this);
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
