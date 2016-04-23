package gipsy.GEE.IDP.DemandGenerator.jms;

import javax.jms.*;
import java.io.Serializable;


public class JMSAsynchListener implements MessageListener  
{
	private Object oResult = null;
	
	public  Object getMessage()
	{
//		System.out.println("get message");
		return oResult;
	}
	
	public synchronized void setMessage(Object oMsg)
	{
		oResult = oMsg;
//		System.out.println("set message");
	}
	
	public void onMessage(Message OMsg)
	{
		
		try
		{
			System.out.println("A message has been received by the listener.");
			if (OMsg instanceof ObjectMessage) 
            {
            	/*
            	 * Although it's been checked before, we want to cast the m to the object
            	 * message within the name "oMessage". 
            	 */
            	ObjectMessage oMessage = (ObjectMessage) OMsg;

            	/*
            	 * We need to have a serializable object from this oMessage.
            	 * we name this object as "oSEntry". 
            	 */
            	Serializable oSEntry = oMessage.getObject();

            	/*
            	 * Although DispatcherEntry is a serializable object, but the object 
            	 * message may not be instance of the DispatcherEntry.After checking 
            	 * that, we want the oObject public parameters inside the oEntry.
            	 */
            	if (oSEntry instanceof JMSDispatcherEntry)
            	{
            		JMSDispatcherEntry oEntry = (JMSDispatcherEntry)oSEntry;
                    setMessage((Object)oEntry.getoDemand());
               }
             }
			//System.out.println("end of message reading.");
		}
		catch (JMSException e )
		{
			System.out.println("<onMessage> Error in receiving messages.");
		}
		catch (Exception e)
		{
			System.out.println("<onMessage> general problem in receiving messages.");
		}
	}//OnMessage ends.
}
