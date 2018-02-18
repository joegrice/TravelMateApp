package travelmate.com.travelmateapp.models;

import java.util.ArrayList;

/**
 * Created by joegr on 25/01/2018.
 */

public class Journey {
    public String startDateTime;
    public int duration;
    public String arrivalDateTime;
    public ArrayList<Leg> legs;
    public ArrayList<Line> lines;
}
