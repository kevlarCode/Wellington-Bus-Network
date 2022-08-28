package comp261.assig1;

/**
 *  Representation of the edges of the Maps which stores the
 */

public class Edge {
    private Stop fromStop;
    private Stop toStop;
    private String tripId;

    //Constructor - takes in from stop, to stop and the trip Id. 
    public Edge (Stop from, Stop to, String tripId) {
        this.fromStop = from;
        this.toStop = to;
        this.tripId = tripId;
    }

    //getters and setters
    public Stop getFromStop() {  
        return fromStop;
    }
  
    public Stop getToStop(){ 
        return toStop;
    }  

    public String getTripId() {
        return tripId;
    }
}
