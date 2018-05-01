package travelmate.com.travelmateapp.models;

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
}