package org.trihardstudios.RPiOSC.Backend.OSCTest;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import org.trihardstudios.RPiOSC.Backend.Log;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This is the class that handles all outgoing communication with the console
 * This will also route more specif communication to the proper class
 *
 * @company TriHard Studios
 * @author Gregory Bell
 * @version 0.0.1
 */
class OSCTx {
    //-----------------------------------------------
    //------------CLASSES----------------------------
    //-----------------------------------------------
    static InetAddress ip;
    static OSCPortOut OSCTx;//The OSCOld master class will set this var and if there are any errors it will defualt back to null so it can be set again

//-----------------------------------------------------------------------------
//-------------------------METHODS---------------------------------------------
//-----------------------------------------------------------------------------

    /**
     * Creates the TX and starts the class
     * @param IP The ip to transmit to
     * @param PORT The port to transmit to
     * @throws UnknownHostException If the IP is invalid
     * @throws SocketException If the data given is invalid
     * @throws NullPointerException If the creation failed
     */

    static void createTX(String IP, final int PORT)throws UnknownHostException, SocketException, NullPointerException {
         ip= InetAddress.getByName(IP);
        //System.out.println(ip);
        OSCTx = new OSCPortOut(ip, PORT );
        Log.info("INFORMATION: TX created at " +OSCTx.getSocket().getInetAddress()+":"+OSCTx.getPort());
    }

    /**
     * Sends messages to the IP defined in createTX
     * @param address The address of the device (/eos only enables eos consoles to recieve)
     * @param arguments Any more data that needs to be transmitted (anything else after the address)
     */

    static void send(String address, ArrayList arguments){
        OSCMessage message;
        try{
            Log.info("INFORMATION: Creating OSC packet");
            message = new OSCMessage(address, arguments);
            Log.info("INFORMATION: OSCOld packet created ");
            //TODO Add dispatch conditions end with return
            OSCTx.send(message);
            Log.info("INFORMATION: OSCOld packet sent: " + message);


        }catch (Exception G_EX){
            Log.err("ERROR: Invalid address and/or arguments: " + address + " " + arguments);
            return;
        }


    }

    /**
     * Closes the TX
     */

    static void close(){
        Log.info("INFORMATION: Closing TX...");
        OSCTx.close();
        Log.info("INFORMATION: TX closed.");
    }

}

