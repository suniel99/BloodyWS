package gipsy.tests.GEE.simulator;

import javax.swing.filechooser.FileFilter;
import java.io.File;


/**
 * This class is the filter for DGT profile files.
 * 
 * @author Emil Vassev
 * @version $Id: ProfileFilter.java,v 1.4 2009/09/07 01:07:49 mokhov Exp $
 * @since
 */
public class ProfileFilter
extends FileFilter 
{
	/**
	 * This method determines the accepted files - those with an extension of "dgt". 
	 */
	public boolean accept(File poFileOrDirectory) 
	{
		// accepts directories
		if(poFileOrDirectory.isDirectory())
		{
			return true;
		}

		// gets the extension of the file
		String strExtension = getExtension(poFileOrDirectory);

		// accepts files with a "dgt" extension
		if(strExtension.equals(GlobalDef.SHORT_PROFILE_EXTENSION))
		{
			return true;
		}

		return false;
	}

	/**
	 * Returns the description of the accepted files.
	 * XXX: incomplete?
	 */
	public String getDescription() 
	{
		return "DGT profile files";
	}

	/**
	 * Gets the extension of the file, in lower case.
	 * @throws NullPointerException if argument is null
	 */
	private String getExtension(File poFile) 
	{
		String strName = poFile.getName();
		
		int i = strName.lastIndexOf('.');
		
		if(i > 0 && i < strName.length() - 1)
		{
			return strName.substring(i + 1).toLowerCase();
		}
		
		return "";
	}
}

// EOF
