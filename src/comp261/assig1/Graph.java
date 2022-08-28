package comp261.assig1;

import java.io.File;
import java.util.ArrayList;  
import java.util.HashMap;


public class Graph {
    private HashMap <String, Stop> stops;
    private HashMap <String, Trip> trips;
    private HashMap<Trip, ArrayList<Edge>> tripEdgeMap; // Edges Stored with Trip as the key.
    private Trie trie;


/**
 *  Post parsing Constructor
 * 
 * @param stopMap
 * @param tripMap
 * @param edgeMap
*/
    public Graph(HashMap<String, Stop> stopMap, HashMap<String, Trip> tripMap, HashMap<Trip, ArrayList<Edge>> edgeMap ) {
        this.stops = stopMap;
        this.trips = tripMap;
        this.tripEdgeMap = edgeMap;       
    }
 

    /**
     * Constructor for Parsing which takes in the data through the files its stored in. 
     * 
     * @param stopFile
     * @param tripFile
     */
    public Graph(File stopFile, File tripFile) {
        this.stops = Parser.parseStops(stopFile); // importing data into map of stops with key as stop id
        this.trips = Parser.parseTrips(tripFile); // into map with key as Trip id
        this.tripEdgeMap = buildEdgeData(); // building edges and storing in a map by Trip as key
        buildStopTrie(); 
    }

    // Getters and setters 
    public HashMap<String, Stop> getStops() {
        return this.stops;
    }

    public HashMap<String, Trip> getTrips() {
         return this.trips;
    }

    public HashMap<Trip, ArrayList<Edge>> getEdgeMap(){
        return this.tripEdgeMap;
    }

    public Trie getTrie(){
        return this.trie;
    }

    /**
     * Method to Build edges which will return a hash map of the Trip and 
     * a list of all of the edges related to that trip. 
     */
    public HashMap<Trip, ArrayList<Edge>> buildEdgeData(){
        HashMap<Trip, ArrayList<Edge>> edgeMap= new HashMap<Trip, ArrayList<Edge>>();
        for (Trip trip : trips.values() ){
            ArrayList<String> stopList = trip.getStops();
            ArrayList<Edge> edgeList = new ArrayList<Edge>();
            for (int i = 0; i < stopList.size() - 1; i++ ) {               
                Stop fromStop = stops.get(stopList.get(i));
                Stop toStop = stops.get(stopList.get(i+1));
                String tripId = trip.getTripId();
                if (fromStop != null && toStop != null && tripId != null){ // Avoid creating null edges.
                    edgeList.add(new Edge(fromStop, toStop, tripId));
                }
            }
            edgeMap.put(trip, edgeList);
        }
        return edgeMap;
    }

    /**
     *  Method to build the trie for searching (completion)
     *  The method creates a new Trie Object and builds all the nodes
     *  for the stop names by adding all the stops into it. 
     */
    public void buildStopTrie() {
        this.trie = new Trie();
        for (Stop stop : stops.values()){
            trie.addStops(stop);
        } 
    }
}