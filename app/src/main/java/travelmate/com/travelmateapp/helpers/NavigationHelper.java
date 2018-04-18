package travelmate.com.travelmateapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.LoginActivity;
import travelmate.com.travelmateapp.MainActivity;
import travelmate.com.travelmateapp.R;

/**
 * Created by joegr on 28/01/2018.
 */

public class NavigationHelper implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout mDrawerLayout;

    public NavigationHelper(Context context, DrawerLayout mDrawerLayout) {
        this.context = context;
        this.mDrawerLayout = mDrawerLayout;
    }

    public void onBottomNavigationViewClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_add:
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                context.startActivity(new Intent(context, AddJourneyActivity.class));
                break;
            case R.id.action_nav_view:
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            case R.id.action_nav_logout:
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onBottomNavigationViewClick(item);
        return true;
    }
}