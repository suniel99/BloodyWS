package gipsy.tests.GEE.multitier.GMT;

import gipsy.Configuration;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.tests.GEE.multitier.GMT.GMTCommand.CommandId;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * This JFrame based simple console is used for testing the functionality of GMT.
 * It is designed to be used with gipsy.tests.junit.GEE.multitier.GIPSYNodeTestDriver
 * 
 * XXX: this ought to move to RIPE; surgically without a loss of history.
 * 
 * @author Yi Ji
 * @version $Id: GMTTestConsole.java,v 1.11 2012/06/13 14:14:15 mokhov Exp $
 * @since TODO
 */
public class GMTTestConsole 
extends JFrame 
implements Runnable
{ 
	/**
	 * Generated by Eclipse
	 */
	private static final long serialVersionUID = 7558611022263719180L;
	
	private PipedInputStream oPipedInOut; 
	//private PipedInputStream oPipedInErr; 
	private PipedOutputStream oPipedOutOut; 
	//private PipedOutputStream oPipedOutErr; 
	
	private PrintStream oPrintOut;
	private PrintStream oPrintErr;
	
	private JTextArea oTextArea = new JTextArea();
	private KeyInputProcessor oKeyInputProcessor = new KeyInputProcessor();
	private KeyboardListener oKeyboardListener = new KeyboardListener();
	
	private GMTWrapper oGMT = null;
	private Thread oGMTThread = null;
	
	private static final String USAGE = "" +
			"Note: [] indicates the argument is optional\n" +
			"  allocate NodeIndex DST JiniDST.config [number of instances to start]\n" +
			"  allocate NodeIndex DGT sDGT.config DSTIndexAtGMT [number of instances to start]\n" +
			"  allocate NodeIndex DWT DWT.config DSTIndexAtGMT [number of instances to start]\n" +
			"  set policy 0\n" +
			"  load commands commandFile\n" +
			"  resume\n" +
			"  exit";
	
	public GMTTestConsole(IMultiTierWrapper poGMT) throws IOException, MultiTierException
	{ 
		this.oGMT = (GMTWrapper)poGMT;

		this.oPipedInOut = new PipedInputStream(); 
		this.oPipedOutOut = new PipedOutputStream(oPipedInOut); 
		this.oPrintOut = new PrintStream(oPipedOutOut, true);
		this.oGMT.setOut(this.oPrintOut); 
		
		// To save a reader thread, we use the same stream.
		this.oPrintErr = this.oPrintOut;
		this.oGMT.setErr(this.oPrintErr); 
		
		// Add a scrolling text area 
		this.oTextArea.setEditable(false); 
		this.oTextArea.setRows(20); 
		this.oTextArea.setColumns(50); 
		
		this.oTextArea.addKeyListener(this.oKeyboardListener);
		
		addWindowListener
		(
			new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e)
				{
			        synchronized(oGMTThread)
			        {
			        	try
			        	{
				        	oGMT.stopTier();
				        	oGMTThread.interrupt();
				        	System.out.println("GMT thread stopped: " + oGMTThread);
			        	}
			        	catch(Exception oException)
			        	{
			        		oException.printStackTrace(System.err);
			        	}
			        }
				}
			}
		);
		
		getContentPane().add(new JScrollPane(oTextArea), BorderLayout.CENTER); 
		pack(); 
		
		// Create reader threads 
		Thread oOutReader = new ReaderThread(oPipedInOut);
		oOutReader.start();
		
		this.oKeyInputProcessor.start();
		while(!oOutReader.isAlive() || !this.oKeyInputProcessor.isAlive())
		{
			Thread.yield();
		}
		
		// Start GMT
		this.oGMT.startTier();
		this.oGMTThread = new Thread(this.oGMT);
		this.oGMTThread.start();
		
		this.setTitle("GMT Console");
		this.setVisible(true);
	}
	
	/**
	 * 
	 * Execute the commands.
	 * 
	 * Only one instance of this thread should be running, otherwise
	 * the shared pipied stream would throw 
	 * "java.io.IOException: Write end dead"
	 * when the thread terminates.
	 * 
	 * @author Yi Ji
	 */
	private class KeyInputProcessor extends Thread
	{
		private ConcurrentLinkedQueue<String> oCommands = new ConcurrentLinkedQueue<String>();
		private boolean bIsTerminated = false;
		
		private KeyInputProcessor()
		{
			bIsTerminated = false;
		}
		
		private void setKeyboardInput(String pstrInput)
		{
			this.oCommands.add(pstrInput);
		}

		@SuppressWarnings("unused")
        private void terminate()
		{
			bIsTerminated = true;
			synchronized(this)
			{
				this.notify();
			}
		}
		
        // TODO use GMTCommandParser
		public void run() 
		{
			while(!this.bIsTerminated)
			{
				ICommand command = null;

				synchronized(this)
				{
					try 
					{
						this.wait();
						if(this.bIsTerminated)
						{
							return;
						}
					} 
					catch (InterruptedException oInterrupted) 
					{
						oInterrupted.printStackTrace(System.err);
						break;
					}
					
					String strInput = null;
					
					while(!this.bIsTerminated && (strInput = this.oCommands.poll()) != null)
					{
						String[] strCMD = strInput.trim().split("\\s+");
						
						if(strCMD.length == 0) 
						{
							oPrintErr.println("--Illegal command, see usage:" + USAGE);
							continue;
						}
						
						if(strCMD[0].equalsIgnoreCase("allocate"))
						{
							try 
							{
								int iNodeIndex = Integer.parseInt(strCMD[1]);
								TierIdentity oTierIdentity = TierIdentity.valueOf(strCMD[2]);
								Configuration oTierConfig = GIPSYNode.loadFromFile(strCMD[3]);
								DSTRegistration oDataDST = null;
								int iNumOfInstances = 1;
								String strConnectTo = "";
								
								if(oTierIdentity == TierIdentity.DST)
								{
									if(strCMD.length == 5)
									{
										iNumOfInstances = Integer.parseInt(strCMD[4]);
									}
								}
								else
								if(oTierIdentity == TierIdentity.DGT || oTierIdentity == TierIdentity.DWT)
								{
									if(strCMD.length < 5)
									{
										oPrintErr.println("--Illegal command, see usage:");
										continue;
									}
									
									if(strCMD.length == 6)
									{
										iNumOfInstances = Integer.parseInt(strCMD[5]);
									}
									
									int iDSTIndex = Integer.parseInt(strCMD[4]);
									
									oDataDST = oGMT.getInfoKeeper()
									.getDSTRegistrations(0, Integer.MAX_VALUE)
									.get(iDSTIndex);
									
									if(oDataDST == null 
											|| oDataDST.getActiveConnectionCount() >= oDataDST.getMaxActiveConnection())
									{
										appendTextArea("--The DST chozen is not available for this " 
												+ oTierIdentity.toString() + ": max " +
												oDataDST.getMaxActiveConnection() + " connections allowed.\n");
										continue;
									}
									
									Configuration oTAConfig = oDataDST.getTAConfig();
									
									if(oTierIdentity == TierIdentity.DGT)
									{
										oTierConfig.setObjectProperty(DGTWrapper.TA_CONFIG, oTAConfig);
									}
									else
									{
										oTierConfig.setObjectProperty(DWTWrapper.TA_CONFIG, oTAConfig);
									}
									
									strConnectTo = " connecting to DST (index " + iDSTIndex + ")";
								}
								
								appendTextArea("--Allocating " + iNumOfInstances +  " " + 
										oTierIdentity.toString() + "(s) in Node " + iNodeIndex +
										 " using " + strCMD[3] + strConnectTo + 
										" ...\n");

							    Object[] args = {iNodeIndex, oTierIdentity, oTierConfig,
							                        oDataDST, iNumOfInstances};
								command = GMTCommandSimpleFactory.createGMTCommand
								            (CommandId.GMT_ALLOCATE, oGMT, args);
								command.execute();
								appendTextArea("--Tier allocation finished!\n");
							} 
							catch (NumberFormatException oNumberFormatException)
							{
								oPrintErr.println("--Illegal command, see usage:" + USAGE);
							}
							catch (IndexOutOfBoundsException oIndexException)
							{
								oPrintErr.println("--DST index out of bounds!");
							}
							catch (Exception e) 
							{
								e.printStackTrace(oPrintErr);
							}
						}
						else if(strCMD[0].equalsIgnoreCase("deallocate"))
						{
							int iNodeIndex = Integer.parseInt(strCMD[1]);
							TierIdentity oTierIdentity = TierIdentity.valueOf(strCMD[2]);
							int iNumOfTierIDs = strCMD.length -3;
							String[] astrTierIDs = new String[iNumOfTierIDs];
							
							for(int i = 0; i<astrTierIDs.length; i++)
							{
								astrTierIDs[i] = strCMD[i+3];
							}
							try
							{
							    Object[] args = {iNodeIndex, oTierIdentity, astrTierIDs};
							    command = GMTCommandSimpleFactory.createGMTCommand(
                                                                    CommandId.GMT_DEALLOCATE, 
                                                                    oGMT, 
                                                                    args
                                                                );
							    command.execute();
								appendTextArea("--Tier deallocation finished!\n");
							}
							catch(MultiTierException oDeallocationException)
							{
								appendTextArea("--Tier deallocation failed!\n");
							}
							
						}
						else if(strCMD[0].trim().equalsIgnoreCase("exit"))
						{
							System.exit(0);
						}
						else if(strCMD[0].trim().equalsIgnoreCase("set"))
						{
							if(strCMD.length < 3)
							{
								oPrintErr.println("--Illegal command, see usage:" + USAGE);
								continue;
							}
							
							int iPolicy = 0;
							try
							{
								iPolicy = Integer.parseInt(strCMD[2]);
								String msg = this.getSetPolicyString(iPolicy);
								if (oGMT.isValidPolicyNumber(iPolicy)) {
								    oPrintOut.println(msg);
								    oGMT.iRecoverPolicy = iPolicy;
								} else {
								    oPrintErr.println(msg);
								}
								
							}
							catch (NumberFormatException oNumberFormatException)
							{
								oPrintErr.println("--Illegal command, see usage:" + USAGE);
							}
							
						}
						else if(strCMD[0].trim().equalsIgnoreCase("resume"))
						{
							synchronized(oGMT)
							{
								oGMT.notify();
							}
						}
						else if(strCMD[0].equals("load"))
						{
							if(strCMD.length < 2)
							{
								oPrintErr.println("--Illegal command, see usage:" + USAGE);
								continue;
							}
							else
							{
								new CommandLoader(strCMD[1]).start();
							}
						}
						else
						{
							oPrintErr.println("--Illegal command, see usage:" + USAGE);
						}
					}
				}
			}
		}

		/**
		 * Returns the log message for the set command.
		 * @param iPolicy
		 * @return
		 */
		@Deprecated
        private String getSetPolicyString(int iPolicy) {
            String result = null;
            switch (iPolicy) {
                case GMTWrapper.LET_IT_BE:
                    result = "--Policy set: LET_IT_BE.";
                    break;
                case GMTWrapper.TRY_NEXT_UNTIL_THE_END:
                    result = "--Policy set: TRY_NEXT_UNTIL_THE_END.";
                    break;
                case GMTWrapper.TRY_NEXT_AND_WRAP_AROUND:
                    result = "--Policy set: TRY_NEXT_AND_WRAP_AROUND.";
                    break;
                case GMTWrapper.IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END:
                    result = "--Policy set: IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END.";
                    break;
                case GMTWrapper.IF_CRASH_THEN_RESTART:
                    result = "--Policy set: IF_CRASH_THEN_RESTART.";
                    break;
                default:
                    result = "--Wrong policy number specified!";
                    
            }

            return result;
        }

    }

	private class CommandLoader extends Thread
	{
		private String strFileName;
		
		private CommandLoader(String pstrFileName)
		{
			this.strFileName = pstrFileName;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() 
		{
			try
			{
				BufferedReader oReader = new BufferedReader(new FileReader(this.strFileName));
				String strLoadedCMD = null;
				
				while((strLoadedCMD = oReader.readLine()) != null)
				{
					if(strLoadedCMD.trim().isEmpty())
					{
						continue;
					}
					oTextArea.append(strLoadedCMD + "\n");
					oKeyInputProcessor.setKeyboardInput(strLoadedCMD);
				}
				synchronized(oKeyInputProcessor)
				{
					oKeyInputProcessor.notify();
				}
			}
			catch (IOException oException)
			{
				oException.printStackTrace(System.err);
			} 
		}
		
		
	}
	
	private class KeyboardListener implements KeyListener
	{
		private String strInput = "";
		private WeakHashMap<Integer, String> oCMDCache =  new WeakHashMap<Integer, String>();
		private Integer iCMDHistory = 0;
		
		@Override
		public void keyTyped(KeyEvent poKeyEvent) 
		{
			char cKey = poKeyEvent.getKeyChar();
			
			if(cKey == '\n')
			{
				oKeyInputProcessor.setKeyboardInput(strInput);
				
				int iSize = oCMDCache.size();
				oCMDCache.put(iSize, strInput);
				strInput = strInput + cKey;
				appendTextArea(poKeyEvent.getKeyChar() + "");
				
				synchronized(oKeyInputProcessor)
				{
					oKeyInputProcessor.notify();
				}
				
				strInput = "";
				iCMDHistory = 0;
			}
			else if(cKey == '\b')
			{
				if(strInput.length() == 0)
				{
					return;
				}
				strInput = strInput.substring(0, strInput.length()-1);
				try 
				{
					oTextArea.setText(oTextArea.getText(0, oTextArea.getText().length() - 1));
				} 
				catch (BadLocationException e) 
				{
					e.printStackTrace();
				}
			}
			// XXX Verify if it is already not action key
			else if(!poKeyEvent.isActionKey())
			{
				strInput = strInput + cKey;
				appendTextArea(poKeyEvent.getKeyChar()+"");
			}
			
			poKeyEvent.consume();
		}

		@Override
		public void keyPressed(KeyEvent poKeyEvent) 
		{
			int iKeyCode = poKeyEvent.getKeyCode();
			
			if(iKeyCode == KeyEvent.VK_DOWN)
			{
				try 
				{
				    oTextArea.setText(oTextArea.getText(0, oTextArea.getText().length() - strInput.length()));
				} 
				catch (BadLocationException e) {
					e.printStackTrace();
				} 
                catch (Exception e) {
                    if (strInput == null) {
                        System.err.println("[ERROR] strInput shouldn't be null.");
                    }
                }
				
                synchronized (iCMDHistory) {
    				if(iCMDHistory >= 0 && iCMDHistory < this.oCMDCache.size())
    				{

    					int iIndex = this.oCMDCache.size() - iCMDHistory - 1;
    					strInput = this.oCMDCache.get(iIndex);
    					iCMDHistory--;
    					appendTextArea(strInput);
    				} 
    				else
    				{
    					strInput = "";
    					
					    iCMDHistory = this.oCMDCache.size() - 1;
					}
				}
				
			}
			else if(iKeyCode == KeyEvent.VK_UP)
			{
				try 
				{
                    oTextArea.setText(oTextArea.getText(0, oTextArea.getText().length() - strInput.length()));
				} 
				catch (BadLocationException e) {
					e.printStackTrace();
				}
                catch (Exception e) {
                    if (strInput == null) {
                        System.err.println("[ERROR] strInput shouldn't be null.");
                    }
                }
				
                synchronized (iCMDHistory) {
    				if(iCMDHistory < this.oCMDCache.size())
    				{
    					int iIndex = this.oCMDCache.size() - iCMDHistory - 1;
    					strInput = this.oCMDCache.get(iIndex);
    					iCMDHistory++;
    					appendTextArea(strInput);
    				}
    				else
    				{
    					strInput = "";
    					iCMDHistory = 0;   
                    }
				}
			}
			else if(iKeyCode == KeyEvent.VK_LEFT)
			{
				
			}
			else if(iKeyCode == KeyEvent.VK_RIGHT)
			{
				
			}
			else
			{
				if(poKeyEvent.getModifiers()==Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) 
				{
					JTextArea  oTextArea = (JTextArea)poKeyEvent.getSource();

					if(poKeyEvent.getKeyCode()==KeyEvent.VK_C) 
					{
						oTextArea.copy();
					}
//					else if(poKeyEvent.getKeyCode()==KeyEvent.VK_X)
//					{
//						oTextArea.cut();
//					} 
//					else if(poKeyEvent.getKeyCode()==KeyEvent.VK_V) 
//					{
//						oTextArea.paste();
//					}
				}
			
			}
			poKeyEvent.consume();
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			
		}
		
	}
	
	private class ReaderThread 
	extends Thread 
	{ 
		PipedInputStream oPipedIn;
		
		ReaderThread(PipedInputStream poPipedIn) 
		{ 
			this.oPipedIn = poPipedIn; 
		} 
		
		public void run() 
		{ 
			final byte[] atBuf = new byte[1024]; 

			try
			{ 
				while(true) 
				{ 
					final int iLen = oPipedIn.read(atBuf); 
					
					if(iLen == -1) 
					{ 
						break; 
					}

					if(iLen != 0)
					{
						appendTextArea(new String(atBuf, 0, iLen));
					}
					
					Thread.yield();
				} 
			} 
			catch(IOException oException) 
			{ 
				oException.printStackTrace(System.err);
			} 
		} 
	}
	
	private void appendTextArea(String pstrText)
	{
		this.oTextArea.append(pstrText); 

		// Make sure the last line is always visible 
		this.oTextArea.setCaretPosition(oTextArea.getDocument().getLength()); 
//		// Keep the text area down to a certain character size 
//		int iIdealSize = 1000; 
//		int iMaxExcess = 500; 
//		int iExcess = oTextArea.getDocument().getLength() - iIdealSize; 
//		if (iExcess >= iMaxExcess) 
//		{ 
//			oTextArea.replaceRange("", 0, iExcess); 
//		}
	}
	
	@Override
	public void run() 
	{
	    //*/
	} 
} 
// EOF
