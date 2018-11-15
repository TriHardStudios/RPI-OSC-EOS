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
        final String VERSION_NUM = "0.1.0";
        try {
            args[0].toLowerCase();//Makes the string lower case so we can decrease the sizes of the if statements
            //s means console off so the console will be disabled
            if (args[0].contains("-s"))
                Log.silent = true;
            //loff means log off so the log will be disabled
            if (args[0].contains("-loff"))
                Log.logOn=false;
            //ts means total silence so both the log and console will be disabled
            if (args[0].contains("-ts")){
                Log.silent = true;
                Log.logOn=false;
            }
            //If invalid commands are entered it will silently fail and just fail every check but it shouldn't
            //cause any issues with the execution of the program.
            Log.start();
        }catch (ArrayIndexOutOfBoundsException AOOB_EX){
            Log.start();//This statement is only reachable if the user doesn't enter any commands.
            Log.err("ERROR: No arguments");
        }//This error is expected, if the user doesn't enter any arguments

        //Printing the credits
        Log.aboutInfo("Raspberry Pi EOS Controller ver: " + VERSION_NUM);
        Log.aboutInfo("by TriHard Studios");

        //Legal
        Log.aboutInfo("\n\n\nTHIS PROGRAM IS PROVIDED AS IS AND IS UNDER NO WARRANTY EXPRESSED OR IMPLIED. BY USING THIS PROGRAM YOU ACCEPT ALL RISKS \nAND TRIHARD STUDIOS IS NOT RESPONSIBLE FOR ANYTHING DONE WITH THIS PROGRAM \nTHIS PROGRAM NOt ENDORSEd IN ANY WAY, SHAPE, OR FORM BY ETC");
        Log.aboutInfo("Copyright 2017-2018 TriHard Studio \nEOS is a registered trademark of ETC ");


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
