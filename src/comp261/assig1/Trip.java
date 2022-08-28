package comp261.assig1;
import java.util.ArrayList;

public class Trip {
    // Fields:
    private String tripId;
    private ArrayList<String> stops;


    // Constructor - only takes in the trip Id.
    public Trip (String stopPatternID) {
        this.tripId = stopPatternID;
        this.stops = new ArrayList<String>();
    }

    // Getters and setters 
    public String getTripId() {
        return tripId;
    }

    public ArrayList<String> getStops(){
        return stops; 
    }

    public void addStop(String stopId) {
        stops.add(stopId);
    }
}
