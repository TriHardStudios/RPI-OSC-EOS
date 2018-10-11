package org.trihardstudios.RPiOSC.Backend;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is is the log class
 * It will print either to the console and a log file or just a log file
 *
 * @version 1.0.0
 * @author Gregory Bell
 */
public class Log {
    //Colors
    private static final String RESET = "\u001B[0m";
    private static final String ERROR = "\u001B[31m";
    private static final String ABOUT_INFO = "\u001B[32m";
    private static final String INFO = "\u001B[34m";


    public static boolean silent = false;//Are we gonna output to the console? False is yes, true is log only
    private static final String FILE_NAME = "rpi-eos-log.log";

    private static BufferedWriter writer = null;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static LocalDateTime now = LocalDateTime.now();

    public static void start(){
        try{
            writer = new BufferedWriter(new FileWriter(FILE_NAME));
        }catch (Exception G_EX){
            System.out.println(ERROR+ "ERROR: Failed to open log file. Check file permissions?"+RESET);
            return;
        }
        if(!silent)
            System.out.println(INFO+"INFORMATION: Log started at " + dtf.format(now)+RESET);
        try {
            writer.write('[' +dtf.format(now) +']' + "INFORMATION: Log started at " + dtf.format(now));
            writer.flush();

        }catch (IOException IO_EX){

        }

    }

    public static void err(Object dataToPrint){
        if(!silent)
            System.out.println(ERROR + dataToPrint + RESET);
        try {
            writer.append("\n" + '[' + dtf.format(now) + ']' + dataToPrint);
            writer.flush();
        }catch (IOException IO_EX){

        }
    }
    public static void info(Object dataToPrint){
        if(!silent)
            System.out.println(INFO + dataToPrint + RESET);
        try {
            writer.append("\n" + '[' + dtf.format(now) + ']' + dataToPrint);
            writer.flush();
        }catch (IOException IO_EX){

        }
    }
    public static void aboutInfo(Object dataToPrint){
        if(!silent)
            System.out.println(ABOUT_INFO + dataToPrint + RESET);
        try {
            writer.append("\n" + '[' + "ABOUT:" + ']' + dataToPrint);
            writer.flush();
        }catch (IOException IO_EX){

        }
    }

    public static void close(){
        try {
            writer.close();
        }catch (IOException IO_EX){

        }
    }











}
