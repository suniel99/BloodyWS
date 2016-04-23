package gipsy.RIPE.editors.RunTimeGraphEditor;

import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MainFrame;
import gipsy.util.Platform;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;


/**
 * Runtime Graph Editor Starter. A class which starts the program.
 * This class contains the entry point of the application.
 * 
 * Copyright(c) 2010 - 2012, The GIPSY Team.
 * 
 * @author Sleiman Rabah
 * @version $Id: ApplicationStarter.java,v 1.8 2012/06/19 16:59:36 mokhov Exp $
 * @since
 */
public class ApplicationStarter
{
	/**
	 * Single instance of ApplicationStarter class.
	 */
	private static MainFrame soAppFrame;

	/**
	 * Gets a hold a reference to the main UI frame. 
	 * 
	 * @return frame instance used to set as a parent of the different input
	 *         dialog used in this program.
	 */
	public static MainFrame getMainFrame()
	{
		return soAppFrame;
	}

	/**
	 * The application's entry point.
	 * 
	 * @param argv
	 *            For this project, input params are optional
	 */
	public static void main(String[] argv)
	{
		try
		{
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			
			if(Platform.isWindows())
			{
				System.out.println("Assuming default system Windows look and feel.");
			}
			else if(Platform.isMac())
			{
				System.out.println("Assuming default system Mac look and feel.");
			}
			else if(Platform.isUnix())
			{
				System.out.println("Assuming default system Unix/Linux look and feel.");
			}
			else if(Platform.isSolaris())
			{
				System.out.println("Assuming default system Solaris look and feel.");
			}
			else
			{
				System.out.println("Your OS does not seem to be supported. Assuming default look and feel.");
			}
			
			// System look & feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		
		catch(Exception e)
		{
			// Assuming the default look-and-feel after this point whatever it is
			try
			{
				// Cross-platform look & feel.
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch(Exception e1)
			{
				e1.printStackTrace(System.err);
			}
		}

		try
		{
			// Instantiate the program main frame in order to run it.
			soAppFrame = new MainFrame();
		}
		catch(Exception e)
		{
			System.err.print("A problem has been occured when running the application.");
			System.err.print("System Exception: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}

// EOF
