package gipsy.GEE.logger;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.io.*;

import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * this class create connection with client to store the 
 * log messages in the log file
 *
 */
public class LoggerServer extends Thread
{
   private ServerSocket serverSocket;
   
   /**
    * constructor that recevies portno
    */
   public LoggerServer(int port_no) throws IOException
   {
      serverSocket = new ServerSocket(port_no);
     
   }

   public void run()
   {
      while(true)
      {
    	  String str;
    	  try
         {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
           
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
                 
            
            PrintWriter printWriter = new PrintWriter(server.getOutputStream(), true);

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            while ((str = bufferReader.readLine()) != null) {
              System.out.println("The message: " + str);
            
           
            
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("serverLogger.log", true)))) {
                out.println("[Server Time]: "+getDate()+" " + str);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
            }
            printWriter.close();
            bufferReader.close();
            server.close();
            
         }
    	 catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }
    	 catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   
   String getDate()
     {
		
		 final Date currentTime = new Date();

		 final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");

		 sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		 System.out.println("GMT time: " + sdf.format(currentTime).toString());
			
			
		 return sdf.format(currentTime).toString();
	 }
   
   public static void main(String [] args)
   {
      int port = 1099;
      try
      {
         Thread t = new LoggerServer(port);
         t.start();
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}