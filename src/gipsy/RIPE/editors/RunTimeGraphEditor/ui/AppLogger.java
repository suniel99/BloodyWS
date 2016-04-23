/**
 * AppLogger.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.4 $ $Date: 2011/08/22 03:38:51 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: AppLogger.java,v 1.4 2011/08/22 03:38:51 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;

/**
 * A class used to print out custom messages to the System Console or the UI
 * Console Tab.
 * 
 * @author Sleiman Rabah
 * 
 */
public class AppLogger
{

    public static boolean isActivated = true;

    /**
     * Appends a main action message to the UI's printing Console.
     * 
     * @param message
     *            the message to display in the console or to append into the
     *            existing text.
     */
    public static void consoleAction(String message)
    {
        MessageAndErrorConsole.getInstance().append(message + "\n");
    }

    /**
     * Appends a sub action message to the UI's printing Console.
     * 
     * @param message
     *            the message to display in the console or to append into the
     *            existing text.
     */
    public static void consoleSubAction(String message)
    {
        MessageAndErrorConsole.getInstance().append("  " + message + "\n");
    }

    public static void printMsg(String message)
    {
        MessageAndErrorConsole.getInstance().append(message);
    }

    public static void log(String msg)
    {

        if (isActivated)
        {
            System.out.println(msg);
        }
    }

    public static void printGraphElements()
    {
        AppLogger.log("\n\n--Map Content \n\n");

        try
        {
            for (GIPSYTier geoState : GlobalInstance.getInstance()
                    .getGIPSYTiersList())
            {
                // AppLogger.log("State Name: " + geoState.getGeostateName());
                AppLogger.log("		ID: " + geoState.getTierID());
                AppLogger.log("		X: " + geoState.getPosX());
                AppLogger.log("		Y: " + geoState.getPosY());
            }
            for (NodeConnection edge : GlobalInstance.getInstance()
                    .getNodesConnectionList())
            {
                AppLogger.log("Conn Name: " + edge.getName());
                AppLogger.log(" 	ID: " + edge.getEdgeId());
                AppLogger.log("		From : " + edge.getFromTierId());
                AppLogger.log("		To : " + edge.getToTierId());
            }

        }
        catch (Exception e)
        {

            System.out.println("Failed to print graph elements.");
        }

    }
}
