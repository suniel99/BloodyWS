package gipsy.util;

import sun.reflect.Reflection;

/**
 * A tool to get the information of a method call.
 * 
 * @author Yi Ji
 * @version $Id: Trace.java,v 1.1 2010/12/29 19:21:02 ji_yi Exp $
 */
public class Trace 
{
	/**
	 * Get the simple name of the caller class.
	 * @return
	 */
	public static String getCallerClassName()
	{
		return Reflection.getCallerClass(3).getSimpleName();
	}
	
	/**
	 * Get the simple name of the enclosing/current class.
	 * @return
	 */
	public static String getEnclosingClassName()
	{
		return Reflection.getCallerClass(2).getSimpleName();
	}
	
	/**
	 * Get the name of the caller method
	 * @return
	 */
	public static String getCallerMethodName()
	{
		return Thread.currentThread().getStackTrace()[3].getMethodName();
	}
	
	/**
	 * Get the name of the enclosing/current method
	 * @return
	 */
	public static String getEnclosingMethodName()
	{
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
}
