package org.trihardstudios.RPiOSC.Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.trihardstudios.RPiOSC.Backend.Log;

public class MainWindow extends Application {
    public static void main(String args[]){
        try {
            if (args[0].contains("-s"))
                Log.silent = true;
            Log.start();
        }catch (ArrayIndexOutOfBoundsException AOOB_EX){
            Log.start();//This statement is only reachable if the user doesn't enter any commands.
            Log.err("ERROR: No arguments");
        }//This error is expected, if the user doesn't enter any arguments
        launch();
    }

    /**
     * Creates the applications GUI then hands contol over to the MainWindowController
     * It creates the GUI from the FXML file MainWindow.fxml
     * @param primaryStage
     */

    public void start(Stage primaryStage){
        try {//FXMLLoader, getIcons can throw errors if they don't find the files needed
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));//creates the FXML loader
            AnchorPane root = fxmlLoader.load();//Creates the window. We are using an AnchorPane as the main window.
            Scene scene = new Scene(root);//Creates the setter
            primaryStage.setScene(scene);//Sets the windows content
            primaryStage.setFullScreen(true);
            primaryStage.getIcons().add(new Image("org/trihardstudios/RPiOSC/Frontend/Logo.png"));

            primaryStage.setTitle("RPi OSCTest");//Sets Window title.

            primaryStage.show();//Sets as visable

        }catch (Exception ex){
            Log.err("FATAL: " + ex);
            System.exit(-6);

        }


    }

}
