package comp261.assig1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * This utility class parses the files, and return the relevant data structure.
 * Internally it uses BufferedReaders instead of Scanners to read in the files,
 * as Scanners are slow.
 * 
 */
public class Parser {

    // read the stop file
    // tab separated stop descriptions
    // stop_id	stop_code	stop_name	stop_desc	stop_lat	stop_lon	zone_id	stop_url	location_type	parent_station	stop_timezone

	public static HashMap<String, Stop> parseStops(File nodeFile){
        HashMap<String, Stop> stops = new HashMap <String, Stop>(); // HashMap to store stops 
		try {
			// make a reader
			BufferedReader br = new BufferedReader(new FileReader(nodeFile));
			br.readLine(); // throw away the top line of the file
			String line;
			// read in each line of the file
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("[\t]");
                if (tokens.length >= 6) {
                    // process the tokens
                    String stopId = tokens[0];
                    String stopName = tokens[2];
                    double lat = Double.valueOf(tokens[4]);
                    double lon = Double.valueOf(tokens[5]);
                    stops.put(stopId, new Stop(new GisPoint(lon, lat), stopName, stopId)); // update map
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }
        return stops; // return full map
    }

    // parse the trip file
    // header: stop_pattern_id,stop_id,stop_sequence,timepoint
    public static HashMap<String, Trip> parseTrips(File tripFile){
        HashMap<String , Trip> trips = new HashMap<String, Trip>(); // Hash Map to store trips 
		try {
			// make a reader
			BufferedReader br = new BufferedReader(new FileReader(tripFile));
			br.readLine(); // throw away the top line of the file.
			String line;
			// read in each line of the file
            while ((line = br.readLine()) != null) {
                // tokenise the line by splitting it at ",".
                String[] tokens = line.split("[,]");
                if (tokens.length >= 4) {
                    // process the tokens
                    String stopPatternId = tokens[0];
                    String stopId = tokens[1];
                    int stopSequence = Integer.parseInt(tokens[2]);
                    String timepoint = tokens[3]; // not used token
                    
                    //Done: Decide how to store the trip data
                    if (stopSequence == 0){ // sequence 0 indicates a new trip (Bus route)
                        trips.put(stopPatternId, new Trip(stopPatternId));
                        trips.get(stopPatternId).addStop(stopId);
                    } else {
                        trips.get(stopPatternId).addStop(stopId); // add to existing trip 
                    }
                } 
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }
        return trips; // return full trip Map. 
    }
}

