package travelmate.com.travelmateapp.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import travelmate.com.travelmateapp.R;

/**
 * Created by joegr on 18/04/2018.
 */

public class NavDrawerListener implements DrawerLayout.DrawerListener {

    private final NavigationView navigationView;
    private final FirebaseAuth mAuth;

    public NavDrawerListener(NavigationView navigationView, FirebaseAuth mAuth) {
        this.navigationView = navigationView;
        this.mAuth = mAuth;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        View navHeader = navigationView.getHeaderView(0);
        TextView navHeaderText = navHeader.findViewById(R.id.nav_header_title);
        navHeaderText.setText(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
