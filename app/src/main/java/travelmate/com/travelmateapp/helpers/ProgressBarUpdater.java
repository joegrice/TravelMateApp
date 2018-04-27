package travelmate.com.travelmateapp.helpers;

import android.view.View;
import android.widget.TextView;

import travelmate.com.travelmateapp.R;

public class ProgressBarUpdater {

    private View progressBar;
    private TextView progressBarText;

    public ProgressBarUpdater(View progressBar) {
        this.progressBar = progressBar;
        this.progressBarText = progressBar.findViewById(R.id.progressBarText);
    }

    public void updateProgress(String updateText) {
        progressBarText.setText(updateText);
        updateProgressVisibility();
    }

    public void updateProgressVisibility() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
