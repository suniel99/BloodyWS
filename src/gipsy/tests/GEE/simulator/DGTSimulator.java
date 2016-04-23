package gipsy.tests.GEE.simulator;

import gipsy.Configuration;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * This class is a modified version of the original DGT class written by Emil Vassev.
 * Modifications are made in this class to make DGT integrate with DGTController 
 * and behave as a separate thread.
 * 
 * @author Emil Vassev
 * @author Yi Ji
 * @version $Id: DGTSimulator.java,v 1.22 2012/03/31 00:08:42 mokhov Exp $
 * @since
 */
public class DGTSimulator
extends DGTWrapper
{
	/**
	 * XXX
	 */
	private ResultProcessor oThreadResultProcessor;
	
	/**
	 * XXX
	 */
	private ResultReceiver oThreadResultReceiver;
	
	/**
	 * XXX
	 */
	private DemandSender oThreadDemandSender;
	
	
	private static final String TESTER_PARAMETER = "gipsy.tests.GEE.simulator.tester.parameter";
	private static final String TEST_MODE = "gipsy.tests.GEE.simulator.mode";
	private static final String TESTER_NUMBER = "gipsy.tests.GEE.simulator.tester.number";
	private static final String DEMAND_PAYLOAD = "gipsy.tests.GEE.simulator.demand.payload";
	
	public DGTSimulator()
	throws GEEException
	{
		super();
	}

	/**
	 * Create an instance based on configuration
	 * 
	 * @param poConfiguration - TA configuration used by the dispatcher.
	 */
	public DGTSimulator(Configuration poConfiguration)
	throws GEEException
	{
		super(poConfiguration);
	}
	
	@Override
	public void run() 
	{
		try
		{	
			GlobalDef.siMode = 0;
			
			try
			{
				GlobalDef.siMode = Integer.parseInt(this.oConfiguration.getProperty(TEST_MODE));
			}
			catch(Exception oExp)
			{
				GlobalDef.siMode = 0;
			}
			
			GlobalDef.reset();
			
			// Create a TA instance using the configuration
			Configuration oTAConfig = (Configuration)this.oConfiguration.getObjectProperty(DGTWrapper.TA_CONFIG);
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			
			// Create a DemandDispatcher instance using the configuration
			String strImplClassName = this.oConfiguration.getProperty(DGTWrapper.DEMAND_DISPATCHER_IMPL);
			Class<?> oImplClass = Class.forName(strImplClassName);
			Class<?>[] aoParamTypes = new Class[] {ITransportAgent.class};
			Constructor<?> oImplConstructor = oImplClass.getConstructor(aoParamTypes);
			Object[] aoArgs = new Object[]{oTA};
			
			GlobalDef.soDemandDispatcher = (IDemandDispatcher)oImplConstructor.newInstance(aoArgs);
			((DemandDispatcher)GlobalDef.soDemandDispatcher).setTAExceptionHandler(this.oTAExceptionHandler);
			
			GlobalDef.soStatisticsUpdator.start();
			GlobalDef.soDGTDialog.setVisible(true);
			
			// Extract the demand payload if any.
			String strPayload = this.oConfiguration.getProperty(DEMAND_PAYLOAD);
			strPayload = strPayload.trim();
			
			int iDemandPayloadSize = 0;
			
			if(strPayload.contains("*"))
			{
				iDemandPayloadSize = 1;
				
				String[] astrParams = strPayload.split("\\*");
				for(int i = 0; i<astrParams.length; i++)
				{
					iDemandPayloadSize = iDemandPayloadSize * Integer.parseInt(astrParams[i].trim());
				}
			}
			else
			{
				try
				{
					iDemandPayloadSize = Integer.parseInt(strPayload);
				}
				catch (Exception oExp)
				{
					iDemandPayloadSize = 0;
				}
			}
			
			GlobalDef.satDemandPayload = new byte[iDemandPayloadSize];
			
			System.out.println("Demand payload prepared: " + GlobalDef.satDemandPayload.length);
			
			if
			(
				GlobalDef.siMode == GlobalDef.OLD_MODE
				|| GlobalDef.siMode == GlobalDef.ASYN_CONTROL_MODE
			)
			{
				this.oThreadDemandSender = new DemandSender();
				this.oThreadResultReceiver = new ResultReceiver();
				//this.oThreadResultProcessor = new ResultProcessor();

				//this.oThreadDemandSender.setPriority(Thread.MIN_PRIORITY);
				//this.oThreadResultReceiver.setPriority(Thread.MIN_PRIORITY);
				//this.oThreadResultProcessor.setPriority(Thread.MIN_PRIORITY);
				
				this.oThreadDemandSender.start();
				this.oThreadResultReceiver.start();
				//this.oThreadResultProcessor.start();

				this.oThreadDemandSender.join();
				this.oThreadResultReceiver.join();
				//this.oThreadResultProcessor.join();
			}
			else if(GlobalDef.siMode == GlobalDef.RESPONSE_TEST_MODE)
			{
				int iNumOfRTTester = 0;
				
				try
				{
					iNumOfRTTester = 
						Integer.parseInt(this.oConfiguration.getProperty(TESTER_NUMBER));
				}
				catch (Exception oExp)
				{
					oExp.printStackTrace(System.err);
					iNumOfRTTester = 0;
				}
				
				System.out.println("Number of Testers = " + iNumOfRTTester);
				
				DemandResponseTimeTester[] oTesters = 
					new DemandResponseTimeTester[iNumOfRTTester];
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i] = new DemandResponseTimeTester();
				}
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i].start();
				}
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i].join();
				}
			}
			else if(GlobalDef.siMode == GlobalDef.SPACE_SCALABILITY_TEST)
			{
				
				
				int iTesterNum = 0;
				
				try
				{
					iTesterNum = Integer.parseInt(this.oConfiguration.getProperty(TESTER_NUMBER));
				}
				catch(Exception oException)
				{
					oException.printStackTrace(System.err);
					return;
				}
				
				DSTSpaceScalabilityTester[] oTesters = new DSTSpaceScalabilityTester[iTesterNum];
				
				
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i] = new DSTSpaceScalabilityTester();
					oTesters[i].iTesterID = i;
				}
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i].start();
				}
				
				for(int i = 0; i<oTesters.length; i++)
				{
					oTesters[i].join();
				}
				
				Iterator<Entry<Long, Long>> oIterator = DSTSpaceScalabilityTester.oRecord.entrySet().iterator();
				
				PrintWriter oWriter = new PrintWriter(
						new FileOutputStream("test1result_" + System.currentTimeMillis() + ".csv", true));
				
				while(oIterator.hasNext())
				{
					Entry<Long, Long> oReport = oIterator.next();
					oWriter.println(oReport.getValue() + ", " + oReport.getKey());
					oWriter.flush();
				}
				oWriter.close();
				
				//Runtime.getRuntime().exec("cmd /c start shutdown -f -s");
			}
			else if(GlobalDef.siMode == GlobalDef.SPACE_TIME_TEST)
			{
				int iBatchSize = 100;
				
				try
				{
					iBatchSize = 
						Integer.parseInt(this.oConfiguration.getProperty(TESTER_PARAMETER));
				}
				catch (Exception oExp)
				{
					oExp.printStackTrace(System.err);
					iBatchSize = 100;
				}
				
				SpaceTimeTester oTester = new SpaceTimeTester(iBatchSize);
				
				oTester.start();
				
				oTester.join();
				
				//Runtime.getRuntime().exec("cmd /c start shutdown -f -s");
			}
			System.out.println("All the threads have finished successfully.");
			
			//System.exit(0);
		}
		catch(Exception ex)
		{
			ex.printStackTrace(System.err);
			GlobalDef.handleCriticalException(ex);	
		}
	}

	@Override
	public void startTier() 
	{
		new Thread(this).start();
	}

	@Override
	public void stopTier() {
		// TODO Auto-generated method stub
		
	}
}

// EOF
