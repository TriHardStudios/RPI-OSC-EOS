package org.trihardstudios.RPiOSC.Backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//Loads mac adress from a file defined at startup.
public class RemotePowerButton {
    private static final String PATH = "mac.thd";
    private static String ip, mac;
    private static InetAddress address;


    private static BufferedReader fileIn = null;
    private static DatagramPacket packet = null;


    public static void start(){
        Log.info("INFORMATION: Igniting remote connection...");
        if (fileIn == null){
            Log.info("INFORMATION: Reading data from file...");
            read();
        }
        if (packet == null){
            try {
                Log.info("INFORMATION: Creating packet....");
                byte[] macBytes = getBytes(mac);
                byte[] packetBytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++)
                    packetBytes[i] = (byte) 0xff;
                for (int i = 6; i < packetBytes.length; i += macBytes.length)
                    System.arraycopy(macBytes, 0, packetBytes, i, macBytes.length);
                address = InetAddress.getByName(ip);
                packet = new DatagramPacket(packetBytes, packetBytes.length, address, 9);
            }catch (Exception G_EX){
                Log.err("ERROR: Failed to create packet " + G_EX);
                packet = null;
                return;//BREAK
            }
        }

    }


    private static void read(){
        try{
            fileIn  = new BufferedReader(new FileReader(PATH));
        }catch (FileNotFoundException FNF_EX){
            Log.err("FATAL: Could not locate file. Run repair.sh");
            return;//Breaks
        }
        try {
            mac = fileIn.readLine();
            ip = fileIn.readLine();
        }catch (IOException IO_EX){
            Log.err("ERROR: An error occurred while reading the file");
            return;
        }
        Log.info("INFORMATION: Broadcast IP: " + ip + " Mac Address: " +mac);



    }

    private static byte[] getBytes(String str){
        byte[] bytes  = new byte[6];
        String[] hex = str.split(":");
        if (hex.length != 6){
            Log.err("FATAL: Invalid MAC address");
            return bytes;
        }
        for (int i =0; i < bytes.length; i++)
            bytes[i] = (byte) Integer.parseInt(hex[i], 16);
        return bytes;

    }

    public static void send(){
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
        }catch (Exception G_EX){
            Log.err("ERROR: Failed to send packet " + G_EX);
        }
    }


}
