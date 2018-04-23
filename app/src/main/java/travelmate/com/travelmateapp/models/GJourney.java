package travelmate.com.travelmateapp.models;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

/**
 * Created by joegr on 18/03/2018.
 */

public class GJourney
{
    public String name;
    public String from;
    public String to;
    public String time;
    public String period;
    public String status;
    public ArrayList<GRoute> routes;
    public ArrayList<DbLine> disruptedLines;
}