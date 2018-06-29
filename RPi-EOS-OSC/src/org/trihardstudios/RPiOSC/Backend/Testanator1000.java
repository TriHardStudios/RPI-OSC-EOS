package org.trihardstudios.RPiOSC.Backend;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
public class Testanator1000 {

    public static void main(String args[]){
        InetAddress ip;
        Scanner sc = new Scanner(System.in);
        String usrIn ="";
        System.out.print("Enter IP Address to transmit to: ");
        usrIn=sc.nextLine();
        try {
            ip = InetAddress.getByName(usrIn);
        }catch (UnknownHostException EX_UH){
            System.err.println("ERROR: Invalid IP defaulting to loopback");
            ip = null;


        }
        OSC osc = new OSC(ip, 7001);

        osc.test();
        osc.start();
        System.out.print("Enter Command: ");
        usrIn ="";
       while(true){
           usrIn = sc.next();
           osc.buildMessage(usrIn);
           if(usrIn.contains("enter")){
               break;
           }

        }
        while(true){}

    }


}
