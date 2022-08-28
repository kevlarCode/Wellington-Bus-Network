package comp261.assig1;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.*;

public class GraphController {

    // names from the items defined in the FXML file
    @FXML private TextField searchText; 
    @FXML private Button load;
    @FXML private Button quit;
    @FXML private Button up;
    @FXML private Button down;
    @FXML private Button left;
    @FXML private Button right;
    @FXML private Canvas mapCanvas;
    @FXML private Label nodeDisplay;
    @FXML private TextArea tripText;

    // These are use to map the nodes to the location on screen
    private Double scale = 5000.0; //5000 gives 1 pixel ~ 2 meter
    private static final double ratioLatLon = 0.73; // in Wellington ratio of latitude to longitude
    private GisPoint mapOrigin = new GisPoint(174.77, -41.3); // Lon Lat for Wellington
 
    private static int stopSize = 5; // drawing size of stops
    private static int moveDistance = 100; // 100 pixels
    private static double zoomFactor = 1.1; // zoom in/out factor


    private ArrayList<Stop> highlightNodes = new ArrayList<Stop>(); // highlighted nodes
    private ArrayList<Trip> tripstoDraw = new ArrayList<Trip> (); // highlisgted trips 
    private boolean enterPressed = false; // boolean to differentiate between search and search key methods.

    public double dragStartX = 0; // holds data for dragging on the canvas
    public double dragStartY = 0;

    // map model to screen using scale and origin
    private Point2D model2Screen (GisPoint modelPoint) {
        return new Point2D(model2ScreenX(modelPoint.lon), model2ScreenY(modelPoint.lat));
    }
    private double model2ScreenX (double modelLon) {
      return (modelLon - mapOrigin.lon) * (scale*ratioLatLon) + mapCanvas.getWidth()/2; 
    }
    // the getHeight at the start is to flip the Y axis for drawing as JavaFX draws from the top left with Y down.
    private double model2ScreenY (double modelLat) {
      return mapCanvas.getHeight()-((modelLat - mapOrigin.lat)* scale + mapCanvas.getHeight()/2);
    }

    // map screen to model using scale and origin
    private double getScreen2ModelX(Point2D screenPoint) {
        return (((screenPoint.getX()-mapCanvas.getWidth()/2)/(scale*ratioLatLon)) + mapOrigin.lon);
    }
    private double getScreen2ModelY(Point2D screenPoint) {
        return ((((mapCanvas.getHeight()-screenPoint.getY())-mapCanvas.getHeight()/2)/scale) + mapOrigin.lat);
    }
    
    private GisPoint getScreen2Model(Point2D screenPoint) {
        return new GisPoint(getScreen2ModelX(screenPoint), getScreen2ModelY(screenPoint));
    }   

    /* handle the load button being pressed connected using FXML*/
    public void handleLoad(ActionEvent event) {
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        System.out.println("Handling event " + event.getEventType());
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access
        File defaultNodePath = new File("data");
        if(!defaultNodePath.canRead()) {
            defaultNodePath = new File("C:/");
        }
        fileChooser.setInitialDirectory(defaultNodePath);
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setTitle("Open Stop File");
        File stopFile = fileChooser.showOpenDialog(stage);

        fileChooser.setTitle("Open Stop Pattern File");
        File tripFile = fileChooser.showOpenDialog(stage);

        Main.graph=new Graph(stopFile,tripFile);
        drawGraph();
        event.consume(); // this prevents other handlers from being called
    }

    public void handleQuit(ActionEvent event) {
        System.out.println("Quitting with event " + event.getEventType()); 
        event.consume();  
        System.exit(0); 
    }

    public void handleZoomin(ActionEvent event) {
        System.out.println("Zoom in event " + event.getEventType()); 
        // Todo: zoom in
        scale *= zoomFactor;
        drawGraph();
        event.consume();  
    }

    
    /** 
     *  This method hadles the event of the user scrolling on the mouse
     * The method allows an alternate method to zooming in on the canvas through
     * the mouse scroll rather than just the zoom in button.
     */
    public void mouseScroll(ScrollEvent event ){
        System.out.println("Mouse Scroll:" + event.getEventType());
        if(scale + event.getDeltaY()/10 > 0){
        scale += event.getDeltaY()/10;
        event.consume();
        drawGraph();
        }
    }

    public void handleZoomout(ActionEvent event) {
        System.out.println("Zoom out event " + event.getEventType()); 
        // Todo: zoom out
        scale *= 1.0/zoomFactor;
        drawGraph();
        event.consume();  
    }

    public void handleUp(ActionEvent event) {
        System.out.println("Move up event " + event.getEventType()); 
        // Todo: move up
        mapOrigin.add(0, moveDistance / scale);
        drawGraph(); 
        event.consume();  
    }

    public void handleDown(ActionEvent event) {
        System.out.println("Move Down event " + event.getEventType()); 
        // Todo: move down
        mapOrigin.subtract(0, moveDistance/scale);
        drawGraph();
        event.consume();  
    }

    public void handleLeft(ActionEvent event) {
        System.out.println("Move Left event " + event.getEventType()); 
        // Todo: move left
        mapOrigin.subtract(moveDistance/scale, 0);
        drawGraph();
        event.consume();  
    }


    /** 
     * Method to handle if the user clicks the mouse for dragging on the canvas. 
    */
    public void handleMousePressed(MouseEvent event){
        System.out.println("Mouse pressed event " + event.getEventType());
        dragStartX = event.getX();
        dragStartY = event.getY();
        event.consume();
    }

    /** 
     * Method changes the mapOrigin of the canvas to allow user to drag on the canvas
     * 
     */
    public void handleMouseDrag(MouseEvent event){
        System.out.println("Mouse drag event " + event.getEventType());
        double dx = event.getX() - dragStartX;
        double dy = event.getY() - dragStartY;
        dragStartX = event.getX();
        dragStartY = event.getY();
        mapOrigin.lon -= dx/(scale*ratioLatLon);
        mapOrigin.lat += dy/scale;
        drawGraph();
        event.consume();
    }

    public void handleRight(ActionEvent event) {
        System.out.println("Move Right event " + event.getEventType()); 
        // Todo: move right
        mapOrigin.add(moveDistance/scale, 0);
        drawGraph();
        event.consume();  
    }

    /** 
     *  This is the handle search function which only is called when the user presses enter  
     * on the search bar.
     * In order for the user to find a match for the stop node, the user will have to search the exact 
     * name of the stop node.
     */
    public void handleSearch(ActionEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText();
        highlightNodes.clear();
        tripstoDraw.clear();
        enterPressed = true; // boolean is turned to true so only this function is used.
        for (Stop stop : Main.graph.getStops().values()){ 
            if (stop.getName().equalsIgnoreCase(search)){ // checks search aganist the stops 
                nodeDisplay.setText(stop.getName());
                highlightNodes.add(stop); 
                for (Trip trip : Main.graph.getTrips().values()){
                    if (trip.getStops().contains(stop.getId())){
                        this.tripstoDraw.add(trip); 
                    }
                }
                drawGraph();
                event.consume();
                return;
            }
        }
        tripText.setText("Stop : " + search + " not Found");
        drawGraph();
        event.consume();  
    }  

    /** 
     * This method handles the seach when the user enters any keys in the search bar
     *  This method implements the tris and uses tri to search for the Stop nodes and provides 
     * the user with search related options in the Triptext area.
     */
    public void handleSearchKey(KeyEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText();
        if (this.enterPressed) { return; } // if user presses enter than only allows searchKey to be used

        if (search.isBlank()){
            tripText.setText(""); // reset
            highlightNodes.clear();
            tripstoDraw.clear();
            drawGraph();
            return;
        }

        highlightNodes.clear();
        tripstoDraw.clear();
        ArrayList<Stop> stopsFound = Main.graph.getTrie().getAll(search); // list of search relataed stops 
        String text = "";
        for (Stop stop : stopsFound) {
            highlightNodes.add(stop);
            nodeDisplay.setText(stop.getName());
            text += stop.getName() + "\n"; 
            if (stopsFound.size() == 1 ) { // only highlight trip when one match is found
                for (Trip trip : Main.graph.getTrips().values()){
                    if (trip.getStops().contains(stop.getId())){
                        this.tripstoDraw.add(trip);
                    }
                }
            }
        }
        tripText.setText(text);

        drawGraph();
        event.consume();  
    }  


/* handle mouse clicks on the canvas
   select the node closest to the click */
    public void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse click event " + event.getEventType());
        Point2D screenPoint = new Point2D(event.getX(), event.getY());
        double x = getScreen2ModelX(screenPoint);
        double y = getScreen2ModelY(screenPoint);
        this.enterPressed = false; // When mouse Clicked set back to false
        highlightClosestStop(x,y);
        event.consume();
    }

    //find the Closest stop to the lon,lat postion
    public void highlightClosestStop(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Stop closestStop = null;
        for (Stop node : Main.graph.getStops().values()){
            double distance = node.getPoint().distance(lon, lat);
            if (distance < minDist) {
                minDist = distance;
                closestStop = node; // set closest node
            }
        }
        if (closestStop != null) {
            highlightNodes.clear();
            highlightNodes.add(closestStop);
            nodeDisplay.setText(closestStop.getName());
            addHighlightednodes(closestStop);
            String tripT = "";  // printing out Trips with stops. 
            for (Trip trip : tripstoDraw){
                tripT += "Trip ID : " + trip.getTripId() + "   stops: " + trip.getStops() + "\n";
            }
            tripText.setText(tripT);
            drawGraph();
        }
    }

    /**
     *  Method adds the given stop that the user has clicked on and adds the edges to be drawn 
     */
    public void addHighlightednodes(Stop node){
        String stopId = node.getId();
        tripstoDraw.clear();
        for (Trip trip : Main.graph.getTrips().values()){
            if (trip.getStops().contains(stopId)){
                this.tripstoDraw.add(trip);
            }
        }
    }

/*
Drawing the graph on the canvas
*/
    public void drawCircle(double x, double y, int radius) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.fillOval(x-radius/2, y-radius/2, radius, radius);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        mapCanvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }
    

    // This will draw the graph in the graphics context of the mapcanvas
    public void drawGraph() {
        Graph graph = Main.graph;
        if(graph == null) {
            return;
        }
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        // Todo:  store node list so we can use nodes to find edge end points.
        for (Stop node : Main.graph.getStops().values()) {
            if (highlightNodes.contains(node)) {
                gc.setFill(Color.RED);
                this.stopSize = 7; 
            } else {
                gc.setFill(Color.BLUE);
                this.stopSize = 5;
            }
            Point2D screenPoint = model2Screen(node.getPoint());
            drawCircle(screenPoint.getX(), screenPoint.getY(), stopSize);
        }

        //draw all edges
        for (ArrayList<Edge> edgeList : Main.graph.getEdgeMap().values()){
            for (Edge edge : edgeList){
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1);
                Point2D startPoint = model2Screen(edge.getFromStop().getPoint());
                Point2D endPoint = model2Screen(edge.getToStop().getPoint());            
                drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
            }
        }

        // Draw all trips that are in the trips to be drawn list
        Random r = new Random();
        for (Trip trip : tripstoDraw) {
            drawTrip(trip, Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255))); // draw trips in random colours
        }
    }

    /**
     * This the mathod that draw all the edges in a given trip in the colour passed.
     * 
     */
    public void drawTrip(Trip trip, Color col) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.setStroke(col);
        gc.setLineWidth(3);
        for (Edge edge : Main.graph.getEdgeMap().get(trip)){
            Point2D startPoint = model2Screen(edge.getFromStop().getPoint());
            Point2D endPoint = model2Screen(edge.getToStop().getPoint());
            drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
    }

}
