package org.trihardstudios.RPiOSC.Frontend;

import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.trihardstudios.RPiOSC.Backend.OSC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainWindowController extends MainWindow {
    //Classes
    OSC osc;
    //EOF Classes

    //-----------------------------------------------
    //-----------THERE IS A LARGE AMOUNT OF VARS-----
    //-----------------------------------------------
    //Local Vars
    InetAddress IP;
    int faderPage =1;



    //FXML Vars
    //Tabpane
    @FXML
    Tab keypad;

    @FXML
    Tab faders;

    @FXML
    Tab cues;
    //EOF Tabpane

    //For CMD Pad
    @FXML
    TextField cmdLine;
    //Row 1
    @FXML
    Button plus;
    @FXML
    Button thru;
    @FXML
    Button minus;
    //Row 2
    @FXML
    Button one;
    @FXML
    Button two;
    @FXML
    Button three;
    //Row 3
    @FXML
    Button four;
    @FXML
    Button five;
    @FXML
    Button six;
    //row 4
    @FXML
    Button seven;
    @FXML
    Button eight;
    @FXML
    Button nine;
    //row 5
    @FXML
    Button period;
    @FXML
    Button zero;
    @FXML
    Button clear;
    //Control Buttons
    @FXML
    Button out;
    @FXML
    Button full;
    @FXML
    Button at;
    @FXML
    Button record;
    @FXML
    Button enter;




    //For welcome
    @FXML
    TextField consoleIP;

    @FXML
    Text status;

    @FXML
    Button connect;

    @FXML
    Button disconnect;

    @FXML
    Button exit;
    //EOF Welcome

    //Misc Functions
    private void syntaxChecker(){
        if(cmdLine.getText().isEmpty())
            osc.buildMessage("cmd");
               

    }
    //FXML Functions
    //Welcome

    @FXML
    protected void onConnect(ActionEvent ae){
        if(consoleIP.getText().isEmpty())
            System.err.println("ERROR: Invalid IP");
        else {

            System.out.println("INFORMATION: Attempting connection...");
            status.setText("Attempting connection...");
            status.setStyle("-fx-text-fill: #e2df00");
            try {
                IP = InetAddress.getByName(consoleIP.getText());

            } catch (UnknownHostException UH_EX) {
                System.err.println("ERROR: Invalid IP");
                status.setText("");
                status.setStyle("-fx-text-fill: #C80003");
                status.setText("Invalid IP");
                return;//Breaks
            }
            osc = new OSC(IP, 7001);
            int attempts = 0;
            boolean connectS = false;//Connection Successful
            while (!connectS && attempts < 10) {
                if (osc.test()) {
                    connectS = true;
                    break;
                } else
                    attempts++;


            }
            if (connectS) {
                status.setText("");
                status.setStyle("-fx-text-fill: #00c015");
                status.setText("Connection Successful.");
                System.out.println("INFORMATION: Connection Successful.");
                keypad.setDisable(false);
                faders.setDisable(false);
                cues.setDisable(false);
                System.out.println("INFORMATION: Tabs unlocked.");
                osc.start();
                consoleIP.setEditable(false);
                connect.setDisable(true);

            } else if (consoleIP.getText().contains("127.0.0.1")) {
                status.setText("");
                status.setStyle("-fx-text-fill: #00c015");
                status.setText("Developer Override");
                System.out.println("WARNING: Developer Override");
                keypad.setDisable(false);
                faders.setDisable(false);
                cues.setDisable(false);
                System.out.println("INFORMATION: Tabs unlocked.");
                osc.start();
                consoleIP.setEditable(false);
                connect.setDisable(true);

            } else {
                status.setText("");
                status.setStyle("-fx-text-fill: #c80003");
                status.setText("Connection Timed out.");
                System.err.println("ERROR: Connection Timed out.");
                keypad.setDisable(true);
                faders.setDisable(true);
                cues.setDisable(true);
                osc = null;
                System.gc();
                return;

            }
        }





    }

    @FXML
    protected void onDisconnect(ActionEvent ae){
        if(!(osc == null))
            osc.stop();
        status.setText("Disconnected");
        keypad.setDisable(true);
        faders.setDisable(true);
        cues.setDisable(true);
        consoleIP.setEditable(true);
        connect.setDisable(false);
        osc = null;
        System.gc();

    }

    @FXML
    protected void onExit(ActionEvent ae){
        if(!(osc==null))
            osc.stop();
        System.out.println("INFORMATION: Exiting...");
        System.exit(0);


    }

    //CMD pad
    //row 1
    @FXML
    protected void onPlus (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " +");
        osc.buildMessage("+");

    }
    @FXML
    protected void onThru (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " Thru");
        osc.buildMessage("Thru");

    }

    @FXML
    protected void onMinus (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " -");
        osc.buildMessage("-");
    }

    //Row 2

    @FXML
    protected void onOne (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 1");
        osc.buildMessage("1");

    }
    @FXML
    protected void onTwo (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 2");
        osc.buildMessage("2");

    }
    @FXML
    protected void onThree (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 3");
        osc.buildMessage("3");

    }
    //Row 4
    @FXML
    protected void onFour (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 4");
        osc.buildMessage("4");

    }
    @FXML
    protected void onFive (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 5");
        osc.buildMessage("5");

    }
    @FXML
    protected void onSix (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 6");
        osc.buildMessage("6");

    }
    //Row 5
    @FXML
    protected void onSeven (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 7");
        osc.buildMessage("7");

    }
    @FXML
    protected void onEight (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 8");
        osc.buildMessage("8");

    }
    @FXML
    protected void onNine (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 9");
        osc.buildMessage("9");

    }
    //Row 6
    @FXML
    protected void onPeriod (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " Cell");
        osc.buildMessage("Cell");

    }
    @FXML
    protected void onZero (ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " 0");
        osc.buildMessage("0");

    }
    @FXML
    protected void onClear (ActionEvent ae){//Beef Rague Cheese Suflay Errors live here.
        try {
            if (cmdLine.getText().contains("*") || cmdLine.getText().isEmpty())
                cmdLine.setText("");
            else {
                String strToRemove = cmdLine.getText().substring(cmdLine.getText().lastIndexOf(' '));
                cmdLine.setText(cmdLine.getText().replace(strToRemove, ""));
            }
            osc.buildMessage("clear");
        }catch (StringIndexOutOfBoundsException SIOOB_EX){
            System.err.println("ERROR: " + SIOOB_EX);
            cmdLine.setText("");
            osc.buildMessage("clearAll");
        }

    }

    //Control Buttons
    @FXML
    protected void onOut(ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " @ Out*");
        osc.buildMessage("@");
        osc.buildMessage("Out");
        osc.buildMessage("enter");
        osc.send();
    }

    @FXML
    protected void onFull(ActionEvent ae){
       syntaxChecker();
       cmdLine.setText(cmdLine.getText() + " @ Full");
       osc.buildMessage("@");
       osc.buildMessage("Full");

    }
    @FXML
    protected void onAt(ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + " @");
        osc.buildMessage("@");
    }
    @FXML
    protected void onRecord(ActionEvent ae){
        syntaxChecker();
        if(!cmdLine.getText().contains("*")) {
            osc.buildMessage("enter");
            cmdLine.setText(cmdLine.getText() + "*");
            osc.send();
        }
        cmdLine.setText("Record Next Cue? (Enter to confirm)*");
        //TODO NEXT CUE
        cmdLine.setText("");

    }
    @FXML
    protected void onEnter(ActionEvent ae){
        syntaxChecker();
        cmdLine.setText(cmdLine.getText() + "*");
        osc.buildMessage("enter");
        osc.send();
    }



}
