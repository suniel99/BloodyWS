package gipsy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import gipsy.lang.GIPSYContext;
import gipsy.util.NotImplementedException;


/**
 * <p>Contains serializable configuration of this GIPSY instance
 * and its components. For static and run-time configuration
 * management. It is serializable such that it can be saved and
 * restored (e.g. in a fail-over restart) or transmitted over the
 * network to update the configuration parameters of other nodes.
 * </p>
 * 
 * @author Serguei Mokhov
 * @since July 22, 2008
 * @version $Id: Configuration.java,v 1.13 2012/04/09 01:28:24 mokhov Exp $
 * 
 * @see java.util.Properties
 */
public class Configuration
implements Serializable
{
	/**
	 * The settings container.
	 */
	protected Properties oConfigurationSettings = null;
	
	/**
	 * XXX 
	 */
	private static final long serialVersionUID = 123L;

	public static final String CONFIGURATION_ROOT_PATH_KEY = "ca.concordia.cse.gipsy.config.root.path";
	public static final String JAVA_SECURITY_POLICY_KEY = "java.security.policy";

	
	/*
	 * -------------
	 * Constructors.
	 * -------------
	 */
	
	/**
	 * Empty configuration.
	 */
	public Configuration()
	{
		super();
		this.oConfigurationSettings = new Properties();
		this.initializeDefaultSettings();
	}

	/**
	 * Pre-determined configuration.
	 * @param poDefaults the default settings
	 */
	public Configuration(Properties poDefaults)
	{
		super();
		this.oConfigurationSettings = new Properties(poDefaults);
		this.initializeDefaultSettings();
	}

	/*
	 * -----------
	 * Common API.
	 * -----------
	 */
	
	/**
	 * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(String pstrKey, String pstrDefaultValue)
	{
		return this.oConfigurationSettings.getProperty(pstrKey, pstrDefaultValue);
	}

	/**
	 * The String associated with pstrKey or NULL if not such
	 * key is found.
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getProperty(String pstrKey)
	{
		return this.oConfigurationSettings.getProperty(pstrKey);
	}

	/**
	 * @see {@link java.util.Hashtable#get(Object)}
	 */
	public Object getObjectProperty(String pstrKey)
	{
		return this.oConfigurationSettings.get(pstrKey);
	}
	
	
	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public synchronized Object setProperty(String pstrKey, String pstrValue)
	{
		return this.oConfigurationSettings.setProperty(pstrKey, pstrValue);
	}

	/**
	 * @see {@link java.util.Hashtable#put(Object, Object)}
	 */
	public Object setObjectProperty(String pstrKey, Object poValue)
	{
		return this.oConfigurationSettings.put(pstrKey, poValue);
	}
	
	/**
	 * @param poContext
	 * @return
	 */
	public synchronized Object setProperty(GIPSYContext poContext)
	{
		throw new NotImplementedException("setProperty(GIPSYContext)");
		//return this.oConfigurationSettings.setProperty(poContext.g);
	}

	/**
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	public synchronized Object remove(String pstrPropertyName)
	{
		return this.oConfigurationSettings.remove(pstrPropertyName);
	}

	/**
	 * @see java.util.Properties#list(java.io.PrintStream)
	 */
	public void list(PrintStream poOut)
	{
		this.oConfigurationSettings.list(poOut);
	}

	/**
	 * @see java.util.Properties#list(java.io.PrintWriter)
	 */
	public void list(PrintWriter poOut)
	{
		this.oConfigurationSettings.list(poOut);
	}

	/**
	 * Load properties from an input stream and add them into this configuration.
	 * 
	 * @see java.util.Properties#load(java.io.InputStream)
	 */
	public synchronized void load(InputStream poIn)
	throws IOException
	{
		this.oConfigurationSettings.load(poIn);
	}

	/**
	 * @see java.util.Properties#loadFromXML(java.io.InputStream)
	 */
	public synchronized void loadFromXML(InputStream poIn)
	throws IOException, InvalidPropertiesFormatException
	{
		this.oConfigurationSettings.loadFromXML(poIn);
	}

	/**
	 * @see java.util.Properties#propertyNames()
	 */
	public Enumeration<?> propertyNames()
	{
		return this.oConfigurationSettings.propertyNames();
	}

	/**
	 * @see java.util.Hashtable#values()
	 */
	public Collection<Object> propertyValues()
	{
		return this.oConfigurationSettings.values();
	}

	/**
	 * @see java.util.Properties#store(java.io.OutputStream, java.lang.String)
	 */
	public synchronized void store(OutputStream poOut, String pstrComments)
	throws IOException
	{
		this.oConfigurationSettings.store(poOut, pstrComments);
	}

	/**
	 * @see java.util.Properties#storeToXML(java.io.OutputStream, java.lang.String, java.lang.String)
	 */
	public synchronized void storeToXML(OutputStream poOut, String pstrComment, String pstrEncoding)
	throws IOException
	{
		this.oConfigurationSettings.storeToXML(poOut, pstrComment, pstrEncoding);
	}

	/**
	 * @see java.util.Properties#storeToXML(java.io.OutputStream, java.lang.String)
	 */
	public synchronized void storeToXML(OutputStream poOut, String pstrComment)
	throws IOException
	{
		this.oConfigurationSettings.storeToXML(poOut, pstrComment);
	}

	/**
	 * @see java.util.Hashtable#clear()
	 */
	public synchronized void clear()
	{
		this.oConfigurationSettings.clear();
	}

	/**
	 * XXX: review.
	 * @see java.util.Hashtable#clone()
	 */
	public synchronized Object clone()
	{
		Configuration oNewConfig = new Configuration();
		oNewConfig.setConfigurationSettings((Properties) this.oConfigurationSettings.clone());
		return oNewConfig;
	}

	/**
	 * @see java.util.Hashtable#size()
	 */
	public synchronized int size()
	{
		return this.oConfigurationSettings.size();
	}

	/**
	 * @see java.util.Hashtable#toString()
	 */
	public synchronized String toString()
	{
		return this.oConfigurationSettings.toString();
	}


	/*
	 * --------------------
	 * Getters and setters.
	 * --------------------
	 */
	
	/**
	 * @return
	 */
	public Properties getConfigurationSettings()
	{
		return this.oConfigurationSettings;
	}

	/**
	 * @param configurationSettings
	 */
	public void setConfigurationSettings(Properties configurationSettings)
	{
		this.oConfigurationSettings = configurationSettings;
	}
	
	/**
	 * Currently only get the absolute root path of all the configuration files.
	 * May be extended to load all default settings.
	 * @return
	 */
	private void initializeDefaultSettings()
	{
		// Get the absolute path of the gipsy.GEE.IDP.config folder
		String strClassFileName = getClass().getSimpleName() + ".class";
    	String strPath = getClass().getResource(strClassFileName).getPath();
    	strPath = strPath.substring(1).replaceFirst(strClassFileName, "");
    	// XXX: forcing leading "/" in Linux
    	//strPath = strPath + "GEE/IDP/config/";
    	strPath = "/" + strPath + "GEE/IDP/config/";
    	// Set the path to the corresponding property.
    	this.oConfigurationSettings.setProperty(CONFIGURATION_ROOT_PATH_KEY, strPath);
	}


	/**
	 *
	 * @see java.util.Hashtable#hashCode()
	 */
	public int hashCode() 
	{
		return this.oConfigurationSettings.hashCode();
	}

	/**
	 * @see java.util.Hashtable#equals(Object)
	 */
	public boolean equals(Object pObject) 
	{
		if(pObject instanceof Configuration)
		{
			return this.oConfigurationSettings.equals(((Configuration)pObject).oConfigurationSettings);
		}
		else
		{
			return false;
		}
	}
}

// EOF
