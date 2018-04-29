package travelmate.com.travelmateapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by joegr on 18/03/2018.
 */

public class Journey {
    public int id;
    public String name;
    public String from;
    public String to;
    public String time;
    public String period;
    public String status;
    public ArrayList<Route> routes;
    public ArrayList<DbLine> disruptedLines;

    public Journey() {
    }

    public Journey(JSONObject journeyJson) throws JSONException {
        id = journeyJson.getInt("id");
        name = journeyJson.getString("name");
        from = journeyJson.getString("from");
        to = journeyJson.getString("to");
        time = journeyJson.getString("time");
        period = journeyJson.getString("period");
        status = journeyJson.getString("status");
        routes = createRoutes(journeyJson.getJSONArray("routes"));
    }

    private ArrayList<Route> createRoutes(JSONArray routesJson) throws JSONException {
        ArrayList<Route> routes = new ArrayList<>();
        for (int i = 0; i < routesJson.length(); i++) {
            JSONObject item = routesJson.getJSONObject(i);
            Route route = new Route();
            route.legs = createLegs(item.getJSONArray("legs"));
            routes.add(route);
        }
        return routes;
    }

    private ArrayList<Leg> createLegs(JSONArray legsJson) throws JSONException {
        ArrayList<Leg> legs = new ArrayList<>();
        for (int i = 0; i < legsJson.length(); i++) {
            JSONObject item = legsJson.getJSONObject(i);
            Leg leg = new Leg();
            leg.start_address = item.getString("start_address");
            leg.end_address = item.getString("end_address");
            leg.steps = createSteps(item.getJSONArray("steps"));
            legs.add(leg);
        }
        return legs;
    }

    private ArrayList<Step> createSteps(JSONArray stepsJson) throws JSONException {
        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 0; i < stepsJson.length(); i++) {
            JSONObject item = stepsJson.getJSONObject(i);
            Step step = new Step();
            step.html_instructions = item.getString("html_instructions");
            JSONObject duration = item.getJSONObject("duration");
            step.duration = new TextValue(duration.getString("text"));
            if (!item.isNull("transit_details")) {
                step.transit_details = createTransitDetails(item.getJSONObject("transit_details"));
            }
            steps.add(step);
        }
        return steps;
    }

    private TransitDetails createTransitDetails(JSONObject transit_details) throws JSONException {
        TransitDetails transitDetails = new TransitDetails();
        JSONObject lineJson = transit_details.getJSONObject("line");
        Line line = new Line();
        line.name = lineJson.getString("name");
        line.short_name = lineJson.getString("short_name");
        transitDetails.line = line;
        return transitDetails;
    }
}