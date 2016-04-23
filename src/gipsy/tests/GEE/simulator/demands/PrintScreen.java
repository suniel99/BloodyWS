package gipsy.tests.GEE.simulator.demands;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;


/**
 * XXX. 
 * @author Emil Vassev
 * @version $Id: PrintScreen.java,v 1.7 2009/09/29 19:10:35 mokhov Exp $
 * @since
 */
public class PrintScreen
implements Serializable  
{
	/*
	 * Constants
	 */

	/**
	 * XXX: fix. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * XXX.
	 */
	private String strCreatorAddress = "";
	
	/**
	 * XXX.
	 */
	private String strExecutorAddress = "";
	
	/**
	 * XXX.
	 */
	private SerializedImage oScreenShot = null;

	/**
	 * XXX.
	 */
	private transient BufferedImage oImage = null;

	/**
	 * XXX.
	 */
	public PrintScreen()
	{
		this(null);
	}

	/**
	 * XXX.
	 * @param pstrHostAddress XXX
	 */
	public PrintScreen(String pstrHostAddress)
	{
		this.strCreatorAddress = pstrHostAddress;
	}

	/**
	 * XXX
	 * @param pstrHostAddress XXX
	 */
	public void takeScreenShot(String pstrHostAddress)
	{
		try
		{
			this.strExecutorAddress = pstrHostAddress; 

			//**** Get the current screen size
			Toolkit oToolkit = Toolkit.getDefaultToolkit();
			Dimension oScreenSize = oToolkit.getScreenSize();
			Rectangle oScreenRect = new Rectangle(oScreenSize);

			//**** create screen shot
			Robot oRobot = new Robot();
			this.oImage = oRobot.createScreenCapture(oScreenRect);

			this.oScreenShot = new SerializedImage(this.oImage);
		}
		catch(Exception ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * XXX.
	 * @param pstrFileName XXX
	 */
	public void saveImageToFile(String pstrFileName)
	{
		String strFileName;
		this.oImage = this.oScreenShot.getBufferedImage();

		if(pstrFileName.compareTo("") == 0)
		{
			strFileName = "screenshot_" + this.strExecutorAddress.replace('.', '_') + ".png";
		}
		else
		{
			strFileName = pstrFileName;
		}
		
		try
		{
			//**** save the captured image to a png file
			ImageIO.write(this.oImage, "png", new File(strFileName));
		}
		catch(IOException ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * XXX.
	 * @param pstrDirName XXX
	 * @param pstrPrefix XXX
	 */
	public void saveImageToFile(String pstrDirName, String pstrPrefix)
	{
		String strFileName;
		this.oImage = this.oScreenShot.getBufferedImage();

		strFileName
			= pstrDirName
			+ "/" + pstrPrefix
			+ "_screenshot_"
			+ this.strExecutorAddress.replace('.', '_')
			+ ".png";
		
		try
		{
			//**** save the captured image to a png file
			ImageIO.write(this.oImage, "png", new File(strFileName));
		}
		catch(IOException ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * XXX.
	 * @return XXX
	 */
	public String getCreatorAddress()
	{
		return this.strCreatorAddress;
	}

	/**
	 * XXX.
	 * @return XXX
	 */
	public String getExecutorAddress()
	{
		return this.strExecutorAddress;
	}

	/**
	 * XXX.
	 * @return XXX
	 */
	public BufferedImage getScreenShot()
	{
		return this.oScreenShot.getBufferedImage();
	}
}

// EOF
