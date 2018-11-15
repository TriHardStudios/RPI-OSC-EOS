package org.trihardstudios.RPiOSC.Backend.OSCTest;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCListener;
import org.trihardstudios.RPiOSC.Backend.Log;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * This is the class the will handle any incoming communitcation with the console.
 * It takes the message then dispatch it to where it should be
 *
 * @company TriHard Studios
 * @author Gregory Bell
 * @version 0.0.1
 */

class OSCRx {
//-------------------------------------------------------------------
//---------------------------CLASSES---------------------------------
//-------------------------------------------------------------------
    static OSCPortIn OSCRx;//The OSCOld master class will set this var and if there are any errors it will defualt back to null so it can be set again

    //---------------------------------------------------------------
    //-------------------LISTENERS-----------------------------------
    //---------------------------------------------------------------
    /**
     * Takes the message recieved then dispatches it to where it goes
     * Ping is the only thing that is handled internally everything else will be dispatched to where it goes
     */
    private static OSCListener primaryListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            //todo add conditions for dispatch
            if(message.getAddress().contains("ping"))
                OSC.firstTest = true;


        }
    };
    /**
     * This will take what ever listener is running and run it in the background
     */

    private static volatile Runnable backgroundListener;




//-----------------------------------------------------------------------------
//-------------------------METHODS---------------------------------------------
//-----------------------------------------------------------------------------

    /**
     * Creates the RX and starts the class
     * @param IP The ip to recieve on
     * @param PORT The port to listen on
     * @throws UnknownHostException If the IP is invalid
     * @throws SocketException If the data given is invalid
     * @throws NullPointerException If the creation failed
     */

    static void createRX(String IP, final int PORT)throws UnknownHostException, SocketException, NullPointerException {
        OSCRx = new OSCPortIn(PORT, /*InetAddress.getByName(IP)*/ InetAddress.getLocalHost());
        Log.info("INFORMATION: Created RX at " + OSCRx.getSocket().getInetAddress() + ":" + OSCRx.getPort());
        backgroundListener = new Runnable() {
            public void run() {
                Log.info("INFORMATION: Background listener started.");
                try {
                    OSCRx.run();
                }catch (Exception G_EX){
                    //Errors Will be suppressed because they are expected..
                }
                Log.info("INFORMATION: Background listener exited");
            }
        };
        Log.info("INFORMATION: Background Listener created and is ready to start.");

    }


    static void startListening(String address){
        OSCRx.addListener(address, primaryListener);
        OSCRx.startListening();
        Log.info("INFORMATION: Listening for " +address + " messages.");
        new Thread(backgroundListener).start();
    }



    static void close(){
        if(OSCRx.isListening()){
            Log.info("INFORMATION: Stopping listening thread..");
            OSCRx.stopListening();
        }
        Log.info("INFORMATION: Closing RX...");
        OSCRx.close();
        Log.info("INFORMATION: RX Closed.");

    }


}
