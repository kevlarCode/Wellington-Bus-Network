package comp261.assig1;
/* Simple data structure to represent a Geographical Information System (GIS) point. 
This way the screen Point2D is kept separate from the point in the model 
*/

public class GisPoint {
    public double lat;
    public double lon;

    public GisPoint(double lon, double lat) {
        this.lat = lat;
        this.lon = lon;
    }

    public void add(double lon, double lat) {
        this.lat += lat;
        this.lon += lon;
    }

    public void add(GisPoint point) {
        this.lat += point.lat;
        this.lon += point.lon;
    }

    public void subtract(double lon, double lat) {
        this.lat -= lat;
        this.lon -= lon;
    }

    public void subtract(GisPoint point) {
        this.lat -= point.lat;
        this.lon -= point.lon;
    }

    //to string
    public String toString() {
        return "(" + lon + ", " + lat + ")";
    }

    public double distance(double lon2, double lat2) {
        return Math.sqrt(Math.pow(lon2-lon,2) + Math.pow(lat2-lat,2));
    }

    public double distance(GisPoint loc) {
        return Math.sqrt(Math.pow(loc.lon-lon,2) + Math.pow(loc.lat-lat,2));
    }

}



