package org.trihardstudios.RPiOSC.Backend.OSCTest;
import org.trihardstudios.RPiOSC.Backend.Log;
import java.util.ArrayList;

/**
 * Replacement class for the old OSCOld class that was becoming very cluttered and not working well at all
 * Performs the sames tasks as old one but will be far more organised
 * TODO MORE INFO
 * @company TriHard Studios
 * @author Gregory Bell
 * @version 0.0.1
 *
 */

public class OSC {
//----------------------------------------------------------------------------
//---------------------VARS---------------------------------------------------
//----------------------------------------------------------------------------
    static boolean running = false; //If the programing is started or not. If it fails at any point in the way it will default back to false which will nullify everything
    static boolean devOverride = false;//If the program is in dev override mode which changes the communication style
    static boolean ready = false;
    static volatile boolean firstTest = false;
    //--------------------------------------------------
    //--------------------OSCOld-VARS----------------------
    //--------------------------------------------------

    private static int port; //The port used for comunitcation between the two devices
    private static String txIp;//The IP to transmit to. If it is a localhost IP (127.x.x.1) then the program will go into dev override mode.
    private static String rxIp;//The IP to receive on. If it isn't explicitly defined then it will cause erratic behaviour as th
    static ArrayList<String> args;

//-----------------------------------------------------------------------------
//-------------------------METHODS---------------------------------------------
//-----------------------------------------------------------------------------



    public static void start(){
        if (running)
            return;
        try{
            running = true;
            Log.info("INFORMATION: Creating TX at " + txIp+":"+port);
            OSCTx.createTX(txIp, port);
            Log.info("INFORMATION: Creating RX at " + rxIp+":"+port);
            OSCRx.createRX(rxIp, port);
            if (OSCTx.OSCTx.getSocket().getInetAddress() == OSCRx.OSCRx.getSocket().getLocalAddress() || OSCTx.OSCTx.getSocket().getLocalAddress().toString().contains("127."))//If the txIP and the rxIP are the same or if the tx is a local host address then dev mode is enabled
                devOverride = true;



        }catch (Exception G_EX){
            Log.err("ERROR: Failed to start OSC Backend: " + G_EX);
            nullify();//Nulls everything
            return;//Breaks out of the creation as it will prevent further errors as the rest of the class isn't desinged to handle null pointers
        }
        if(running)
            Log.info("INFORMATION: RX and TX created successfully");

        if(devOverride) {
            Log.info("WARNING: Developer override has been enabled. Consult the documentation for what this entails.");
            ready = true;//When ready is true the everything will be unlocked
            return;
        }
        //Todo add a timer to time 30 secs
        //THIS IS TEMPORARY
        try {
            int counter = 0;
            args.add("/ping");//Adds the ping command
            Log.info("INFORMATION: Attempting Connection...");
            OSCRx.startListening("/eos");
            while (!firstTest && counter <= 25) {//This will cause an infinite loop if the proper data is not received
                OSCTx.send("/eos", args);
            }
        }catch (NullPointerException NP_EX){
            Log.err("ERROR: Failed to send data. Resetting");
            try{
                stop();
            }catch (Exception G_EX){
                //ERRORS are expected.. We are just swallowing them
            }
            nullify();//Resets everything
            return;

        }
        if (firstTest){
            Log.info("INFORMATION: Connection Successful!");
            ready = true;
        }
        else {
            Log.err("ERROR: No data received. Resetting.");
            OSCTx.close();
            OSCRx.close();
            nullify();//Resets everything
        }



    }

    public static void stop(){
        OSCTx.close();
        OSCRx.close();
        nullify();//Resets everything
    }

    /**
     * This sets the data from any thing that calls it
     * @param port the port
     * @param txIp the transmitting IP
     * @param rxIp the receiving IP
     */


    public static void setData(int port, String txIp, String rxIp){
        if(!running) {//As to prevent the addresses to be changed while the program is runnning
            OSC.port = port;
            OSC.txIp = txIp;
            OSC.rxIp = rxIp;
        }

    }


    //-----------------------------------------------
    //----------------GETTERS------------------------
    //-----------------------------------------------

    public static boolean getDevOverride() {
        return devOverride;
    }

    public static boolean isReady() {
        return ready;
    }
    //------------------------------------------------
    //----------------MISC-FUNCTS---------------------
    //------------------------------------------------



    private static void nullify(){
        OSCTx.OSCTx = null;//Setting the TX to null to allow for another try
        OSCRx.OSCRx = null;//Setting the RX to null to allow for another try
        running = false;
        ready = false;
    }







}
