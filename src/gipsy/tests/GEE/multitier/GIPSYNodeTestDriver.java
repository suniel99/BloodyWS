package gipsy.tests.GEE.multitier;

import gipsy.Configuration;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.tests.GEE.multitier.GMT.GMTTestConsole;
import gipsy.tests.GEE.multitier.GMT.GMTd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import marf.util.BaseThread;
import marf.util.Debug;

/**
 * This test driver is to test the basic functionality of GISPYNode. It is
 * not a JUnit test, rather, it is a demo.
 * 
 * @author Yi Ji
 * @version $Id: GIPSYNodeTestDriver.java,v 1.7 2012/06/13 14:14:11 mokhov Exp $
 * @since
 */
public class GIPSYNodeTestDriver 
{
	
	static private void usage() {
		System.out.println("usage: configFilename");
	}

	/**
	 * Starting a GIPSY Node application using the configuration file.
	 * 
	 * @param pastrArgs[0] - The GIPSY Node configuration file path.
	 */
	public static void main(String[] pastrArgs)
	{
		if(pastrArgs == null || pastrArgs.length == 0)
		{
			System.err.println("Cannot start GIPSY Node: config file not specified!");
			usage();
			System.exit(1);
		}
		
		System.out.println("Starting up GIPSY node ...\n" +
				"Please note: if a Jini DST is used as the registration DST, " +
				"please ensure that it is always the first Jini DST " +
				"in this machine.");
		System.out.println("Enter command below:");
		
		try 
		{
			SimpleDateFormat oFormat = new SimpleDateFormat("yyyy_MM_dd@kk-mm-ss-SS");
			String strDate = oFormat.format(new Date());
			String logFilename = "log/Node_" + strDate + ".txt";
			
			File oLogDir = new File("log");
			if(!oLogDir.isDirectory())
			{
				oLogDir.mkdir();
			}

			PrintStream oLogStream = new PrintStream(new FileOutputStream(logFilename), true);
			
			PrintStream oOriginalSystemOut = System.out;
			PrintStream oOriginalSystemErr = System.err;
			
			boolean bIsLog2File = false;
			
			// Load the configuration file
			Configuration oNodeConfig = GIPSYNode.loadFromFile(pastrArgs[0]);
			
			// Create a GIPSY Node instance
			GIPSYNode oNode = new GIPSYNode(oNodeConfig);
			
			// Read the console input
			Scanner oConsoleReader = new Scanner(System.in);
			
			// For testing GMT functionality			
			while(oConsoleReader.hasNext())
			{
				String strInput = oConsoleReader.nextLine();
				// Parse the input
				String[] astrCMD = strInput.trim().split("\\s+");
				
				strInput = null;
				
				if(astrCMD[0].equalsIgnoreCase("start"))
				{
					if(astrCMD.length < 3)
					{
						//XXX TODO complete the usage
						System.err.println("Incomplete command, see usage");
						continue;
					}
					
					if(astrCMD[1].equalsIgnoreCase("GMT"))
					{
						if(oNode.getDSTController() == null)
						{
							System.err.println("Unable to start GMT, please set the correct configuration properties");
						}

						// Starts a DST
						Configuration oGMTConfig = GIPSYNode.loadFromFile(astrCMD[2]);
						oGMTConfig.setObjectProperty(GMTWrapper.GMT_NODE, oNode);
						IMultiTierWrapper oTier = oNode.getGMTController().addTier(oGMTConfig);
						BaseThread  oGMTTestConsole;

						if (astrCMD.length >= 4 && astrCMD[3].equals("gmtd")) {
						    oGMTTestConsole = new BaseThread(new GMTd(oTier));
						} else {
						    oGMTTestConsole = new BaseThread(new GMTTestConsole(oTier));
						}

						oGMTTestConsole.start();
						oNode.isRegistered = true;
						oNode.start();
					}
				}
				else if(astrCMD[0].equalsIgnoreCase("register"))
				{
					if(oNode.isRegistered)
					{
						System.err.println("The Node is already registered");
						continue;
					}
					
					oNode.registerNode();
					oNode.start();
				}
				else if(astrCMD[0].equalsIgnoreCase("debug"))
				{
					if(astrCMD.length > 1 && astrCMD[1].equalsIgnoreCase("on"))
					{
						Debug.enableDebug(true);
						System.out.println("Debug is switched on.");
					}
					else if(astrCMD.length > 1 && astrCMD[1].equalsIgnoreCase("off"))
					{
						Debug.enableDebug(false);
						System.out.println("Debug is switched off.");
					}
					else if(Debug.isDebugOn())
					{
						Debug.enableDebug(false);
						System.out.println("Debug is switched off.");
					}
					else if(!Debug.isDebugOn())
					{
						Debug.enableDebug(true);
						System.out.println("Debug is switched on.");
					}
				}
				else if(astrCMD[0].equals("log"))
				{
					if(bIsLog2File)
					{
						bIsLog2File = false;
						System.setOut(oOriginalSystemOut);
						System.setErr(oOriginalSystemErr);
						System.out.println("Switched back to console output.");
					}
					else
					{
						System.out.println("Switch to file output.");
						bIsLog2File = true;
						System.setOut(oLogStream);
						System.setErr(oLogStream);
					}
				}
				else if(astrCMD[0].equalsIgnoreCase("exit"))
				{
					System.out.println("GIPSY Node is shutting down ...");
					System.exit(0);
				}
			}
			oLogStream.flush();
			oLogStream.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
		}
	}
}

// EOF
