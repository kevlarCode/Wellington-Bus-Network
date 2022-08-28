package comp261.assig1;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  @author Kevin P. Lee
 */

public class Main extends Application {

    // save the graph datastructure here as it is easy to get to using Main.graph
    public static Graph graph;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // load the strings for language support
        // currently en_NZ and mi_NZ are supported
        Locale locale = new Locale("en", "NZ");
        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", locale);

        // load the fxml file to set up the GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapView.fxml"),bundle);
        Parent root = loader.load();

        primaryStage.setTitle(bundle.getString("title")); // set the title of the window from the bundle
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        // So you do not have to load the files every time you are testing make
        // PATH_TO_DATA_FOLDER something like "data/"
        graph = new Graph(  new File("data/stops.txt"), 
                            new File("data/stop_patterns.txt"));
        
        //force the GraphController to draw the graph after loading
        ((GraphController)loader.getController()).drawGraph();

        
    }
    public static void main(String[] args) {
        launch(args);
    }
}
