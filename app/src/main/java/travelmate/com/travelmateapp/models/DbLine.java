package travelmate.com.travelmateapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class DbLine {
    public int Id;
    public String Name;
    public String Description;
    public String IsDelayed;

    public DbLine(JSONObject json) throws JSONException {
        Name = json.getString("Name");
        Description = json.getString("Description");
        IsDelayed = json.getString("IsDelayed");
    }
}
