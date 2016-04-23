package gipsy.GEE.IDP.DemandGenerator.jms;

import gipsy.GEE.GEEException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;

import java.io.IOException;

import marf.util.BaseThread;
//import net.jini.id.Uuid;
//import java.util.UUID;
//import java.util.Hashtable;


/**
 * JMS-based Demand Generator implementation.
 * @author Amir Pourteymour
 * @author Serguei Mokhov
 * @author Yi Ji
 * @version $Id: JMSDemandGenerator.java,v 1.10 2010/12/20 03:44:36 ji_yi Exp $
 * @since
 */
public class JMSDemandGenerator
extends gipsy.GEE.IDP.DemandGenerator.DemandGenerator
{
//**** Separative symbols definition

	private static int iNumDG =1;
	private static int iCurrNumber = 0;
	//Changed: sResults
	private static String strResults = "";
	//private static UUID oUniqueID [] = new UUID[iNumDG];
	//Changed: oUniqueID
	private static DemandSignature saoSignature [] = new DemandSignature[iNumDG]; 

	//private static String someID="";
	
	//public String U = "";
	//private static UUID oUniqueID [] = new UUID[intCurrNumber];
	private	static IDemandDispatcher oDemandDispatcher = null;

	static class DDCommunicator
	extends BaseThread  
	{
		/**
		 * Method run() - inherited from Runnable
		 * The purpose of the thread is to keep the service alive for some time.
		 */
		public void run()
		//public  synchronized  void runner ()
		{
			try
			{
				//DemandController oRController = new DemandController();
			 //	Hashtable ResultTable = null ;
				 
				// WorkResult oResult ; working properly
				DemandController oResult; 
				
//				for (int i=0; i<iNumDG; i++)
//				{
//					while (oUniqueID[i] == null)
//						;
					//oResult = (WorkResult)oDemandDispatcher.readResult(oUniqueID[i]);
					//Synchronous --> false 
				    //DemandJMS oJMS = new DemandJMS(false);
				    //Asynchronous --> true
					
					ITransportAgent oJTA = new JMSTransportAgent();
					//write condition in printing synchr/Asynch message selection later.
					System.out.println("Synchronous Models");
					
				    /*
				     * Receiving the result from the Queue 2.
				     * In here, after using oJMS as an instance of DemandJMS, we try to execute the readResult()
				     * method to read the result of the executed demand. The result will go to the oResult.
				     */
				    //oJMS.jmsConnect("GIPSY/Queue2");

					//oJMS.jmsConnect("GIPSY/Topic2");
					//synchronized (this)
					//{
					
						System.out.println("Use Queue2 to connect.");
					    // commented to check the error point.
						//synchronized(this)
						Object o = null;
							try
							{
					    		System.out.println("An attempt to read a result...");
								while (o==null)
							    {
							    	//o = oJMS.readDemand();
									//System.out.println("Unique Identifier " + oUniqueID[iNumDG].valueOf(iNumDG));
									//System.out.println("Generator looks for demand # : " + oUniqueID[0]);
									o = oJTA.getResult(saoSignature[0]);
							    	Thread.sleep(20);
							    	
							    	if (o != null)
							    	{
							    		
							    		if (!(o instanceof DemandController))
							    		//if (!(o instanceof WorkResult)) working properly
							    		 
							    		{
								    		System.out.println("Incorrect receipt: " + o.toString());
							    			o = null;
							    		}
							    		else if(o instanceof DemandController)
							    		//else if(o instanceof WorkResult) working properly
							    		{
							    			System.out.println("the received object is an instance of WorkResult.");
							    		}
							    		
							    		
							    	}	
							    	if(o==null)
							    	{
							    		System.out.println("There is no object in this queue.");
							    	}
							    }
							}
						
							catch (NullPointerException ere)
							{								
								System.out.println("DDCommunicator.run()->"+ ere.getMessage());
								System.out.println("DDCommunicator.run()->"+ere.getCause().toString());
							}
				
					   	System.out.println("The result is received.");
					   	//////////////////////////////////////////
					   	//Object o = oJMS.readResult() ; 
					   	//ResultTable = (Hashtable) o;
					   //	oResult = (WorkResult)o ; //change this line :D - Working properly
					   	oResult = (DemandController)o;
					  
					   	//ResultTable = oRController.perform();
					   	
					   	/////////////////////////////////////////
						//oResult = (WorkResult)oJMS.readResult();
						
						System.out.println("Working Result has computed the results.");
						//oJTA.;
						System.out.println("<oJMS>Connection is closed.");
						//printOut("Received result, ID: " + oUniqueID[i].toString());

						synchronized(strResults)
							{
							//  sResults = sResults + oResult.GetResult() + PrintResult.CR + PrintResult.LF + PrintResult.CR + PrintResult.LF;
							strResults = strResults + oResult.getResult() + PrintResult.CR + PrintResult.LF + PrintResult.CR + PrintResult.LF;
							
							}//end of Synchronized.
   				    	System.out.println("Getting the result and writing it to sResult." + strResults);
			   	}		 
				catch (NullPointerException ex)
				{
					System.err.println("*********caught by NullPointerException****")   ;
					System.err.println(ex.getMessage());
					System.err.println(ex.getCause().toString());
				}
			
				catch (Exception ex)
				{
						System.err.println("object can not be converted in DDCommunicator.run() ");
						PrintResult.printOutError(ex.getMessage());
						PrintResult.printOutError(ex.getCause().toString());
				}
		}
	}

	/**
	 * The method readStringFromKeyboard() reads a string from the keyboard. 
	 * The keyboard reading terminates when "Return" is pressed.
	 * XXX: move to a utility class.
	 */
	private static String readStringFromKeyboard()
	throws IOException
	{
		// Changed: originally sBuffer
		StringBuffer strBuffer = new StringBuffer(0); 
		char ch;
		/*
		 * Reads the input stream into a string buffer
		 * The terminal symbol is Carriage Return
		 */
		strBuffer.setLength(0);
		while ((ch=(char)System.in.read()) != '\n')
			if ((ch != 10) && (ch != 13))
				strBuffer.append(ch); 
        
		return strBuffer.toString();
	}
	
	public void generateDemand(String pstrGenerationMode)
	{
		System.out.println(pstrGenerationMode+ " mode is requested.");
		try
		{
			if (pstrGenerationMode.compareToIgnoreCase("local")==0)
			{
				Task oPi = new Task();
				strResults = "Local Pi calculation" + PrintResult.CR + PrintResult.LF + PrintResult.CR + PrintResult.LF;
	
				PrintResult.lnTimeStart = System.currentTimeMillis();
				for (int i=0; i<iNumDG; i++)
				{
					PrintResult.printOut("Calculating Pi " + (i+1) + " time ... ");
					strResults = strResults + "New Pi calculation: " + PrintResult.CR + PrintResult.LF + oPi.execute (1000) + PrintResult.CR + PrintResult.LF + PrintResult.CR + PrintResult.LF;
				}
				PrintResult.lnTimeEnd = System.currentTimeMillis();
			}
			else
			{
				//*******************************
				//Remotely Execution-->
				
//				System.out.println("");
				DDCommunicator oDG = null;
				//WorkDemand oDemand = null;
				String strDemandName;
	
				/*
				 * ******************************************************************************************
				 * using "GIPSY/Queue1" to connect and send the demand to the worker.The result will return *
				 * later in a different queue.                                                              *     
				 * ******************************************************************************************
				 */
				
				System.out.println("iNumDG(*) = " + iNumDG);
				PrintResult.lnTimeStart = System.currentTimeMillis();
				
				for (int i=0; i<iNumDG; i++)
				{
					System.out.println("iNumDG(**) = " + iNumDG);
					oDemandDispatcher = new JMSDemandDispatcher("","");

					System.out.println("Queue1 destination (*) is selected to send the demand to worker.") ;
					//successful trace 

					iCurrNumber ++;
					strDemandName = "Demand_ " + iCurrNumber;
					DemandController oDemand = new DemandController(strDemandName);

					//GeneratorDC.dController(sDemandName);
					
					//oDemand = new WorkDemand(sDemandName); working properly
					System.out.println("00000000000000000000000000000000000");
					System.out.println("Demand :" +oDemand.getSignature() + "is attached to " + oDemand.strCreatorAddress  );
					/** 
					 * After executing the writeDemand within the oDemand, we use the oDemandDispatcher which
					 * itself is an instance of DemandJMS.
					 * It will simply send the demand to execute and as the return, we have a unique ID which
					 * is attached to each demand.
					 */
					System.out.println("current place before i "); 
					//oUniqueID[i] = oDemandDispatcher.writeDemand(oDemand); working previously
					
					//oUniqueID[i] = oDemandDispatcher.writeDemand(oDemand); working properly
				

					saoSignature[i] = oDemandDispatcher.writeDemand(oDemand);

					//this.U=oDemandDispatcher.writeDemand(oDemand);

					System.out.println("(*)sending the demand to the JMS application server with the ID : " + saoSignature[i]);

					//PrintResult.printOut("Dispatched demand; i: "+i+ ", ID: " + oUniqueID[i].toString());
					
				
					//if (i==0)  
					
					{
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						System.out.println("one step before the start of the thread ");
						oDG = new DDCommunicator();
						oDG.run();
					
						//oDG.runner();
					}
				//	else 
					{
					//	System.out.println("i is not zero in here.");
					}
					
				
					//temp. position to test something.should move above.
					//writeOutFile();
					//oDemandDispatcher.jmsClose();
					System.out.println("<oDemandDispatcher> connection is closed.");
				}
				System.out.println("iNumDG(***) = " + iNumDG);
				PrintResult.printOut("DG has dispatched " + iNumDG + " demands.");
				oDG.join (); 
				PrintResult.lnTimeEnd = System.currentTimeMillis();
			}
			PrintResult.printOut("DG has finished its work.");
			PrintResult.writeOutFile(strResults);
		}
		catch (NullPointerException ex)
		{
			System.out.println("*********caught by NullPointerException****")   ;
//			System.out.println(ex.getMessage());
			System.out.println("***********************");
//			System.out.println(ex.getCause().toString());
			System.out.println("***********************");
//			System.out.println(ex.getLocalizedMessage().toString());
			System.out.println("***********************");
		}
		catch (Exception e)
		{
			System.out.println("error in here.");
			System.out.println(e.getCause().toString());
			PrintResult.printOutError(e.getMessage());
		}
	}

	/**
	 * Main method  - the entry point of the the program.
	 */
	public static void main(String[] argv)
	{
		try
		{
			String strExecution;
			
			if(argv.length == 0)
			{
				//Asking the user to execute the demand locally or remotely.
				System.out.print("Enter the kind of pi calculation 'local' or 'remote': ");
				strExecution = readStringFromKeyboard();
			}
			else		  
			{
				strExecution = argv[0];
			}
			
			PrintResult.printOut("Demand Generator is running ...");
			
			
			//Local Execution-->
			JMSDemandGenerator oDemandGenerator = new JMSDemandGenerator();
			System.out.println("Before sending to demand generator") ; 
			oDemandGenerator.generateDemand(strExecution);
			System.out.println("After sending to demand generator");
		}
		catch(IOException e)
		{
			System.out.println("IOEXCEPTIOM" + e.getLocalizedMessage().toString());
		}
	}

	
	// IDemandGenerator API

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandGenerator.IDemandGenerator#eval(gipsy.GIPC.intensional.SimpleNode, gipsy.lang.GIPSYContext[], int)
	 */
	@Override
	public GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent)
	throws GEEException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandGenerator.IDemandGenerator#execute(gipsy.storage.Dictionary, gipsy.lang.GIPSYContext)
	 */
	@Override
	public GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags)
	{
		return null;
	}
}

// EOF
