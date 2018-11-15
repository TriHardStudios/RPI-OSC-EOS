package org.trihardstudios.RPiOSC.Backend;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.*;
import com.illposed.osc.*;
import org.trihardstudios.RPiOSC.Backend.OSCTest.OSC;

/**
 * Class for all the OSCTest with the OSCTest commands
 * Will contain all commands as well as the IP of the console
 * For EOS Systems only
 *
 *
 */

//CLEAN THIS FILE//
    //TODO Rewrite class to be static
public class OSCOld {
    //Local Vars
        private String receivedMessage = "";
        private String cmdLineRAW = null;
        private String messageToTransmit ="/eos/";
        private ArrayList<String> lastCmdAdded = new ArrayList<String>();
        private boolean received = false;
        private int attempts = 0;
    //EOF Local Vars

    //OSCTest vars
    private final InetAddress OSCipTX; //The ip address of the console / client. TX is console. RX is client.
    private final int PORT; //The port used for OSCTest TX/RX
    //EOF OSCTest vars

    //Classes
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private OSCPortOut tx;
    private OSCPortIn rx;
    private OSCListener debugListener = new OSCListener(){ //What to do when a message is received. For now it sets a var
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            Log.info("INFORMATION: Message received: " + message.getAddress());
            receivedMessage = message.getAddress();
            //System.out.println(received);

        }
    };
    private OSCListener cmdListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            cmdLineRAW = message.getAddress();
            cmdLineRAW = cmdLineRAW.substring(cmdLineRAW.indexOf('"'), cmdLineRAW.lastIndexOf('"'));
            Log.info("INFORMATION: New cmd line data received: "+ cmdLineRAW);
        }
    };

    //EOF Classes
public static void main(String args[])throws UnknownHostException {
    Log.start();
    // OSCOld oscOld = new OSCOld(InetAddress.getByName("10.0.0.1"), 7001);
    OSC.setData(7001,"10.0.0.12", "127.0.0.1");
    OSC.start();
}



    public OSCOld(InetAddress tx, int port){
        OSCipTX = tx;
        PORT = port;
        try {
            this.tx = new OSCPortOut(OSCipTX, PORT);
            this.rx = new OSCPortIn(PORT, InetAddress.getLocalHost());

        }catch (Exception ex){
            Log.err("FATAL: Failed to create transmitter/receiver port at port: " + port);
            System.exit(-5);//Creation Error
        }
        try {
            Log.info("INFORMATION: Transmitting to ip address " + tx.getHostAddress() + " on port " + PORT);
            Log.info("INFORMATION: Receiving on port " + rx.getPort() + " " + rx.getSocket().getLocalAddress());
        }catch (NullPointerException NP_EX){
            Log.err("FATAL: No IP/Port assigned. Program will now exit");
            System.exit(-7);
        }
    }

    public boolean test(){
        if(attempts == 0)
            rx.addListener("/eos/*", debugListener);
        rx.startListening();
        try {
            tx.send(new OSCMessage("/eos/ping")); //this won't though an error
        }catch (IOException EX_IO){
            Log.err("ERROR: Invalid message: /eos/ping");
        }
        if (!receivedMessage.isEmpty()){
            received = true;
        }

        attempts++;
        rx.stopListening();
        receivedMessage = "";
        return received;

    }

    public void start(){

        if(!rx.isListening()) {
            Log.info("INFORMATION: Listener is not running. Starting it now...");
            rx.addListener("/eos/*", debugListener);//only allows eos messages all others will be denied
            rx.addListener("/eos/out/cmd" , cmdListener);
            rx.startListening();
            if(Thread.activeCount() <10){//Only starts the runner if we are using less than 10 threads
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            rx.run();
                        }catch (Exception G_EX){
                            Log.err("ERROR: " + G_EX);
                        }
                    }
                });
            }
            else{
                Log.err("ERROR: Max threads exceeded, Listener not started");
            }

            Log.info("INFORMATION: Listening for EOS messages on port " + rx.getPort());
        }
        else
            return;
    }

    public TextField updateCmcLine(TextField cmdLine, String stringToAdd ){
        if (tx.getSocket().getLocalAddress().toString() == "127.0.0.1"){
            Log.info("WARNING: No syntax checking");
            if(stringToAdd == "enter")
                cmdLine.setText(cmdLine.getText() + "*");

            cmdLine.setText(cmdLine.getText() + " " + stringToAdd);
            buildMessage(stringToAdd);
            return cmdLine;

        }
        else{
            buildMessage("key");//Tells EOS that we are pressing a virtual key
            buildMessage(stringToAdd);
            send();
            buildMessage("clearAll");
        }
        if (cmdLineRAW != null){//This might miss some updates. Might want to create deamon
            cmdLine.setText(cmdLineRAW);
            cmdLineRAW = null;
        }
        return cmdLine;

    }

    public void stop(){
        if(rx.isListening()){
            Log.info("INFORMATION: Ending listening thread....");
            Log.info("INFORMATION: Stopping all listeners...");
            rx.stopListening();
            Log.info("INFORMATION: Closing port  " + rx.getPort() + ".....");
            rx.close();
            Log.info("INFORMATION: RX closed. Closing TX...");
            tx.close();
            Log.info("INFORMATION: TX closed. Session ended.");
        }
    }

    public void buildMessage(String str){
        if(str == "clear") {
            String strToRemove = messageToTransmit.substring(messageToTransmit.lastIndexOf(lastCmdAdded.get(lastCmdAdded.size()-1)));
            messageToTransmit = messageToTransmit.replace(strToRemove, "");
            lastCmdAdded.remove(lastCmdAdded.size()-1);
        } else if (str =="clearAll") {
            messageToTransmit = "/eos/";
            for (int i = 0; i< lastCmdAdded.size(); i++)
                lastCmdAdded.remove(i);
        } else{
            messageToTransmit += str + "/";
            lastCmdAdded.add(str+"/");
        }
        Log.info("INFORMATION: New message: " +messageToTransmit);

    }

    public void send(){
        receivedMessage="";
        try {
            tx.send(new OSCMessage(messageToTransmit));
            Log.info("INFORMATION: Sent " + messageToTransmit);
        }catch (IOException EX_IO){
            Log.err("ERROR: Invalid message: " + messageToTransmit);
        }
        if(messageToTransmit.contains("enter")) {
            buildMessage("clearAll");
        }
       receivedMessage = "";
    }









}
