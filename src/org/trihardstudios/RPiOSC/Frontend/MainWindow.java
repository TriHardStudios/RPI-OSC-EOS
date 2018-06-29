package org.trihardstudios.RPiOSC.Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    public static void main(String args[]){
        launch(args);
    }

    public void start(Stage primaryStage){
        try {//FXMLLoader, getIcons can throw errors if they don't find the files needed
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));//creates the FXML loader
            AnchorPane root = fxmlLoader.load();//Creates the window. We are using an AnchorPane as the main window.
            Scene scene = new Scene(root);//Creates the setter
            primaryStage.setScene(scene);//Sets the windows content
            primaryStage.setFullScreen(true);

            primaryStage.setTitle("RPi OSC");//Sets Window title.

            primaryStage.show();//Sets as visable

        }catch (Exception ex){
            ex.printStackTrace();
            System.err.println("FATAL: The GUI failed to generate");
            System.exit(-6);

        }


    }

}
