package org.trihardstudios.RPiOSC.Frontend;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.trihardstudios.RPiOSC.Backend.OSCOld;
import org.trihardstudios.RPiOSC.Backend.RemotePowerButton;
import org.trihardstudios.RPiOSC.Backend.Log;
import org.trihardstudios.RPiOSC.Backend.OSCTest.OSC;
public class MainWindowController extends MainWindow {
    //Classes
    static OSCOld osc =null; //Making this var might fix some issues with duplicate classes being created
    //EOF Classes

    //-----------------------------------------------
    //-----------THERE IS A LARGE AMOUNT OF VARS-----
    //-----------------------------------------------
    //Local Vars
    int faderPage =1;
    boolean firstFaderOpen = true;



    //FXML Vars
    //Faderspane
    //Set 1
    @FXML
    Slider Fader1;
    @FXML
    Slider Fader2;
    @FXML
    Slider Fader3;
    @FXML
    Slider Fader4;
    @FXML
    Slider Fader5;
    @FXML
    Text Fader1Label;
    @FXML
    Text Fader2Label;
    @FXML
    Text Fader3Label;
    @FXML
    Text Fader4Label;
    @FXML
    Text Fader5Label;




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
    TextField localIP;

    @FXML
    Text status;

    @FXML
    Button connect;

    @FXML
    Button disconnect;

    @FXML
    Button exit;

    @FXML
    Button PowerOn;
    //EOF Welcome

    //Misc Functions
    private void syntaxChecker(){
        if(cmdLine.getText().contains("*"))
            cmdLine.clear();
        if(cmdLine.getText().isEmpty())
            osc.buildMessage("cmd");



    }
    //FXML Functions
    //Welcome

    @FXML
    protected void onConnect(ActionEvent ae){
        if(consoleIP.getText().isEmpty() || localIP.getText().isEmpty()){
            Log.err("ERROR: Invalid IP");
            return;

        }
        OSC.setData(7001,consoleIP.getText(), localIP.getText());
        OSC.start();

        if(OSC.isReady() && OSC.getDevOverride()) {
            status.setText("");
            status.setStyle("-fx-text-fill: #00c015");
            status.setText("Developer Override");
            keypad.setDisable(false);
            faders.setDisable(false);
            cues.setDisable(false);
            Log.info("INFORMATION: Tabs unlocked.");
            consoleIP.setEditable(false);
            localIP.setEditable(false);
            connect.setDisable(true);
            return;
        }
        if(OSC.isReady() && !(OSC.getDevOverride())) {
            status.setText("");
            status.setStyle("-fx-text-fill: #00c015");
            status.setText("Connection Successful.");
            Log.info("INFORMATION: Connection Successful.");
            keypad.setDisable(false);
            faders.setDisable(false);
            cues.setDisable(false);
            Log.info("INFORMATION: Tabs unlocked.");
            consoleIP.setEditable(false);
            localIP.setEditable(false);
            connect.setDisable(true);
            return;
        }

        status.setText("");
        status.setStyle("-fx-text-fill: #c80003");
        status.setText("Connection Timed out.");
        Log.err("ERROR: Connection Timed out.");
        keypad.setDisable(true);
        faders.setDisable(true);
        cues.setDisable(true);


    }


    @FXML
    protected void onDisconnect(ActionEvent ae){
        if(OSC.isReady())
            OSC.stop();
        status.setText("Disconnected");
        keypad.setDisable(true);
        faders.setDisable(true);
        cues.setDisable(true);
        consoleIP.setEditable(true);
        localIP.setEditable(true);
        connect.setDisable(false);


    }

    @FXML
    protected void onExit(ActionEvent ae){
        if(OSC.isReady())
            OSC.stop();
        Log.info("INFORMATION: Exiting...");
        Log.info("INFORMATION: Closing Log..");
        Log.close();
        System.exit(0);


    }

    @FXML
    protected void onPowerOn(ActionEvent ae){
        RemotePowerButton.start();
        RemotePowerButton.send();
        status.setText("Sent Power Packet");
        Log.info("INFORMATION: Sent Power Packet");
    }

    //CMD pad
    //row 1
    @FXML
    protected void onPlus (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "+");
    }

    @FXML
    protected void onThru (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "Thru");


    }

    @FXML
    protected void onMinus (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "-");

    }

    //Row 2

    @FXML
    protected void onOne (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "1");


    }

    @FXML
    protected void onTwo (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "2");


    }

    @FXML
    protected void onThree (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "3");


    }

    //Row 4
    @FXML
    protected void onFour (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "4");


    }

    @FXML
    protected void onFive (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "5");


    }

    @FXML
    protected void onSix (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "6");


    }

    //Row 5
    @FXML
    protected void onSeven (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "7");


    }

    @FXML
    protected void onEight (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "8");


    }

    @FXML
    protected void onNine (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "9");


    }

    //Row 6
    @FXML
    protected void onPeriod (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, ".");


    }

    @FXML
    protected void onZero (ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "0");


    }

    @FXML
    protected void onClear (ActionEvent ae){//Beef Rague Cheese Suflay Errors live here. //THIS has NOT been converted to the new version..
        try {
            if (cmdLine.getText().contains("*") || cmdLine.getText().isEmpty())
                cmdLine.setText("");
            else {
                String strToRemove = cmdLine.getText().substring(cmdLine.getText().lastIndexOf(' '));
                cmdLine.setText(cmdLine.getText().replace(strToRemove, ""));
            }
            osc.buildMessage("clear");
        }catch (StringIndexOutOfBoundsException SIOOB_EX){
            //Log.err("ERROR: " + SIOOB_EX);
            cmdLine.setText("");
            osc.buildMessage("clearAll");
        }

    }

    //Control Buttons
    @FXML
    protected void onOut(ActionEvent ae){
        cmdLine = cmdLine = osc.updateCmcLine(cmdLine, "out");

    }

    @FXML
    protected void onFull(ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "full");


    }

    @FXML
    protected void onAt(ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "at");

    }

    @FXML
    protected void onRecord(ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "record");


    }

    @FXML
    protected void onEnter(ActionEvent ae){
        cmdLine = osc.updateCmcLine(cmdLine, "enter");

    }




    //Faders
    @FXML
    protected void updateFaders(ActionEvent ae){
        if(firstFaderOpen){
            for(int i = 1; i < 5; i++)
            osc.buildMessage("fader/" + i +"/config/10");
            osc.send();
            osc.buildMessage("clearAll");

        }

    }



}
