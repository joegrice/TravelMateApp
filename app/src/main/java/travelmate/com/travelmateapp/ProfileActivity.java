package travelmate.com.travelmateapp;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import travelmate.com.travelmateapp.helpers.NavigationHelper;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationHelper navListener = new NavigationHelper(getApplicationContext());
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
