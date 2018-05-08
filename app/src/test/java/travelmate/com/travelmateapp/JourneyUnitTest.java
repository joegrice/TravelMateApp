package travelmate.com.travelmateapp;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.crypto.ExemptionMechanism;

import static org.mockito.Mockito.*;

import travelmate.com.travelmateapp.helpers.HttpHandler;
import travelmate.com.travelmateapp.models.AsyncResponse;
import travelmate.com.travelmateapp.models.Journey;
import travelmate.com.travelmateapp.tasks.GetJourneyDetailsTask;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JourneyUnitTest {

    @Test
    public void journey_notNull() throws Exception {
        String json = getJsonObject();
        Gson gson = new Gson();
        Journey j = gson.fromJson(json, Journey.class);
        assertNotNull(j);
    }

    @Test
    public void journey_routesLength() throws Exception {
        String json = getJsonObject();
        Gson gson = new Gson();
        Journey j = gson.fromJson(json, Journey.class);
        assertEquals(4, j.routes.size());
    }

    public String getJsonObject() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("Example_Journey.json");
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}