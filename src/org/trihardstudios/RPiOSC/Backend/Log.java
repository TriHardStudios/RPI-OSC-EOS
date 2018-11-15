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
//-----------------------------------------------------------------
//---------------------------VARS----------------------------------
//-----------------------------------------------------------------

    //---------------------------------
    //--------------COLORS-------------
    //---------------------------------
    private static final String RESET = "\u001B[0m";
    private static final String ERROR = "\u001B[31m";
    private static final String ABOUT_INFO = "\u001B[32m";
    private static final String INFO = "\u001B[34m";

    //---------------------------------
    //------------LOG-CONFIG-----------
    //---------------------------------
    public static boolean silent = false;//Are we gonna output to the console? False is yes, true is log only
    public static boolean logOn = true;//Are we gonna print to the file? True is yes, false is console only
    private static final String FILE_NAME = "rpi-eos-log.log";//File name is final as to premote easy changes

    //---------------------------------
    //----------PRINTER-CONFIG---------
    //---------------------------------
    private static BufferedWriter writer = null;//What writes to the file Will Stay null if log is disabled
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//The format that the date and time are formatted to
    private static LocalDateTime localTime = LocalDateTime.now();//Gets the current time. Runs in a violate thread

//-----------------------------------------------------------------
//--------------------------METHODS--------------------------------
//-----------------------------------------------------------------

    /**
     * Starts the log. This is the first thing called in the program
     *
     *
     */

    public static void start(){
        if(logOn) {
            try {
                writer = new BufferedWriter(new FileWriter(FILE_NAME));
            } catch (Exception G_EX) {
                System.out.println(ERROR + "ERROR: Failed to open log file. Check file permissions?" + RESET);
                logOn = false;//Turns off log to prevent errors
                return;
            }
        }

        if(!silent)
            System.out.println(INFO+"INFORMATION: Log started at " + dateTimeFormatter.format(localTime)+RESET);

        if (logOn) {
            try {
                writer.write('[' + dateTimeFormatter.format(localTime) + ']' + "INFORMATION: Log started at " + dateTimeFormatter.format(localTime));
                writer.flush();

            } catch (IOException IO_EX) {

            }
        }

    }


    public static void close(){
        if(logOn) {
            try {
                writer.close();
            } catch (IOException IO_EX) {

            }
        }
    }




    public static void err(Object dataToPrint){
        if(!silent)
            System.out.println(ERROR + dataToPrint + RESET);

        if(logOn) {
            try {
                writer.append("\n" + '[' + dateTimeFormatter.format(localTime) + ']' + dataToPrint);
                writer.flush();
            } catch (IOException IO_EX) {

            }
        }
    }
    public static void info(Object dataToPrint){
        if(!silent)
            System.out.println(INFO + dataToPrint + RESET);
        if (logOn) {
            try {
                writer.append("\n" + '[' + dateTimeFormatter.format(localTime) + ']' + dataToPrint);
                writer.flush();
            } catch (IOException IO_EX) {

            }
        }
    }
    public static void aboutInfo(Object dataToPrint){
        if(!silent)
            System.out.println(ABOUT_INFO + dataToPrint + RESET);
        if (logOn) {
            try {
                writer.append("\n" + '[' + "ABOUT:" + ']' + dataToPrint);
                writer.flush();
            } catch (IOException IO_EX) {

            }
        }
    }











}
