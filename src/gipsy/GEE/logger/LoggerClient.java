package gipsy.GEE.logger;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * this class create client connection with server to store the 
 * log messages in the log file
 *
 */
public class LoggerClient
{
	 String serverName = "localhost";
     int port = 1099;
    
   /**
    * creates client connection
    * @param str
    */
   void clientConnection(String str)
   {
	   
	   try
	      {
	     	         
	       Socket client = new Socket(serverName, port);
	       BufferedReader readerObj = new BufferedReader(new InputStreamReader(client.getInputStream()));
	       PrintWriter writerObj = new PrintWriter(client.getOutputStream(), true);
	         
	       try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("clientLogger.log", true)))) 
	       {
	       out.println("[Client Time]: "+getDate()+" " + str);
	       }
	       catch (IOException e)
	       {
	    	   e.printStackTrace();
	       }

	       writerObj.println("[Client Time]: "+getDate()+" " + str);
	       readerObj.close();
	       writerObj.close();
	       client.close();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	   
	   
   }
   /**
    * to get Date time stamp
    * @return string
    */
   
   String getDate()
   {
		
		final Date currentTime = new Date();

     	final SimpleDateFormat sdf =
			        new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");

			// Give it to me in GMT time.
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println("GMT time: " + sdf.format(currentTime).toString());
			
			
		return sdf.format(currentTime).toString();
	 }
   
  
    }