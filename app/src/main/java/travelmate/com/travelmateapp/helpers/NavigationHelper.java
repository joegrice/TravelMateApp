package travelmate.com.travelmateapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import travelmate.com.travelmateapp.AddJourneyActivity;
import travelmate.com.travelmateapp.MainActivity;
import travelmate.com.travelmateapp.ProfileActivity;
import travelmate.com.travelmateapp.R;

/**
 * Created by joegr on 28/01/2018.
 */

public class NavigationHelper  implements BottomNavigationView.OnNavigationItemSelectedListener {

    Context context;

    public NavigationHelper(Context context) {
        this.context = context;
    }

    public void onBottomNavigationViewClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_add:
                context.startActivity(new Intent(context, AddJourneyActivity.class));
                break;
            case R.id.action_nav_view:
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            case R.id.action_nav_profile:
                context.startActivity(new Intent(context, ProfileActivity.class));
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onBottomNavigationViewClick(item);
        return false;
    }
}