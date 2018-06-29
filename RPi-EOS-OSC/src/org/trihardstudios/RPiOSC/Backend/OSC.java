package org.trihardstudios.RPiOSC.Backend;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.illposed.osc.*;
/**
 * Class for all the OSC with the OSC commands
 * Will contain all commands as well as the IP of the console
 * For EOS Systems only
 *
 *
 */
public class OSC {
    //public static void main(String args[]){}

    //Local Vars
        private String receivedMessage = "";
        private String messageToTransmit ="/eos/";
        private ArrayList<String> lastCmdAdded = new ArrayList<String>();
        private boolean received = false;
        private int attempts = 0;
    //EOF Local Vars

    //OSC vars
    private InetAddress OSCipTX; //The ip address of the console / client. TX is console. RX is client.
    private final int PORT; //The port used for OSC TX
    //EOF OSC vars

    //EOS COMMANDS
    //EOF EOS Commands

    //Classes
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private OSCPortOut tx;
    private OSCPortIn rx;
    private OSCListener debugListener = new OSCListener(){ //What to do when a message is received. For now it sets a var
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            System.out.println("INFORMATION: Message received: " + message.getAddress());
            receivedMessage = message.getAddress();
            //System.out.println(received);

        }
    };
    //EOF Classes

    public OSC(InetAddress tx, int port){
        OSCipTX = tx;
        PORT = port;
        try {
            this.tx = new OSCPortOut(OSCipTX, PORT);
            this.rx = new OSCPortIn(PORT);

        }catch (java.net.SocketException EX_S){
            System.err.println("FATAL: Failed to create transmitter/receiver port at port: " + port);
            System.exit(-5);//Creation Error
        }
        try {
            System.out.println("INFORMATION: Transmitting to ip address " + tx.getHostAddress() + " on port " + PORT);
            System.out.println("INFORMATION: Receiving on port " + rx.getPort());
        }catch (NullPointerException NP_EX){
            System.err.println("FATAL: No IP/Port assigned. Program will now exit");
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
            System.err.println("ERROR: Invalid message: /eos/ping");
        }
        if (!receivedMessage.isEmpty()){
            received = true;
        }

        //System.out.println("INFORMATION: Received data on attempt " + connectTest);
        attempts++;
        rx.stopListening();
        receivedMessage = "";
        return received;

    }

    public void start(){//generic start
        if(!rx.isListening()) {
            System.out.println("INFORMATION: Listener is not running. Starting it now...");
            rx.addListener("/eos/cmd/*", debugListener);//only allows eos messages all others will be denied
            rx.startListening();
            if(Thread.activeCount() <10){//Only starts the runner if we are using less than 10 threads
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            rx.run();
                        }catch (Exception G_EX){
                            System.err.println("ERROR: " + G_EX);
                        }
                    }
                });
            }
            else{
                System.err.println("ERROR: Max threads exceeded, Listener not started");
            }

            System.out.println("INFORMATION: Listening for EOS messages on port " + rx.getPort());
        }
        else
            return;
    }
    public void addListener(String msgToListenFor){//Adds a listener then starts it
        System.out.println("INFORMATION: Starting listener for " +msgToListenFor);
        rx.addListener(msgToListenFor, debugListener);//TODO Change listener
        rx.startListening();
        System.out.println("INFORMATION: Listening for " + msgToListenFor + " on port " + rx.getPort());
    }

    public void stop(){
        if(rx.isListening()){
            System.out.println("INFORMATION: Ending listening thread....");
            System.out.println("INFORMATION: Stopping all listeners...");
            rx.stopListening();
            System.out.println("INFORMATION: Closing port  " + rx.getPort() + ".....");
            rx.close();
            System.out.println("INFORMATION: RX closed. Closing TX...");
            tx.close();
            System.out.println("INFORMATION: TX closed. Session ended.");
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
        System.out.println("INFORMATION: New message: " +messageToTransmit);

    }

    public void send(){
        receivedMessage="";
        try {
            tx.send(new OSCMessage(messageToTransmit));
            System.out.println("INFORMATION: Sent " + messageToTransmit);
        }catch (IOException EX_IO){
            System.err.println("ERROR: Invalid message: " + messageToTransmit);
        }
        if(messageToTransmit.contains("enter")) {
            messageToTransmit = "/eos/";
            for (int i = 0; i< lastCmdAdded.size(); i++)
                lastCmdAdded.remove(i);



        }
       receivedMessage = "";
    }









}
