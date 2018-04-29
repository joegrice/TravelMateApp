package travelmate.com.travelmateapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import travelmate.com.travelmateapp.R;
import travelmate.com.travelmateapp.models.DbLine;

/**
 * Created by joegr on 18/03/2018.
 */

public class DisruptedLinesArrayAdapter extends ArrayAdapter<DbLine> {

    private Context context;
    private ArrayList<DbLine> disruptedLines;

    public DisruptedLinesArrayAdapter(Context context, ArrayList<DbLine> disruptedLines) {
        super(context, 0, disruptedLines);
        this.context = context;
        this.disruptedLines = disruptedLines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.disrupted_line_list_item, parent, false);
        }

        // Get the data item for this position
        final DbLine disruptedLine = disruptedLines.get(position);

        // Lookup view for data population
        TextView desc = convertView.findViewById(R.id.disrupted_line_desc);

        // Populate the data into the template view using the data object
        desc.setText(disruptedLine.Description);

        // Return the completed view to render on screen
        return convertView;
    }
}
