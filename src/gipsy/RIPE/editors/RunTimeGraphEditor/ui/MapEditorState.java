/**
 * MapEditorState.java
 *  
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:40 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MapEditorState.java,v 1.2 2011/08/18 04:17:40 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

/**
 * A class which holds the map editor state's flags. Used to know if the map has
 * been saved/closed, etc.
 * 
 * @author Sleiman Rabah
 * 
 */
public class MapEditorState
{

    private boolean closeCanceled;
    private boolean mapClosed;
    private boolean mapSaved;
    private boolean openCanceled;
    private boolean saveCanceled;

    /**
     * Class Constructor.
     */
    public MapEditorState()
    {
        reset();
    }

    /**
     * Close the map by setting the mapClosed and mapSaved flags to true.
     */
    public void closeMap()
    {
        setMapClosed(true);
        setMapSaved(true);
    }

    /**
     * Gets the closeCanceled flag.
     * 
     * @return the closeCanceled
     */
    public boolean isCloseCanceled()
    {
        return closeCanceled;
    }

    /**
     * Gets the mapClosed flag.
     * 
     * @return mapClosed boolean which indicates if the map is closed or not.
     */
    public boolean isMapClosed()
    {
        return this.mapClosed;
    }

    /**
     * 
     * @return mapSaved which determines whether the map has been saved or not.
     */
    public boolean isMapSaved()
    {
        return mapSaved;
    }

    /**
     * @return the openCanceled which determines whether the load map dialog has
     *         been canceled or not.
     */
    public boolean isOpenCanceled()
    {
        return openCanceled;
    }

    /**
     * @return the saveCanceled which determines whether the save dialog has
     *         been canceled or not.
     */
    public boolean isSaveCanceled()
    {
        return saveCanceled;
    }

    /**
     * Sets the saved and closed flags to false.
     */
    public void openMap()
    {
        setMapSaved(false);
        setMapClosed(false);
    }

    /**
     * Reset the map editor flags.
     */
    public void reset()
    {
        mapClosed = true;
        openCanceled = false;
        setSaveCanceled(false);
        setCloseCanceled(false);
        setMapSaved(false);

    }

    /**
     * @param closeCanceled
     *            the closeCanceled to set
     */
    public void setCloseCanceled(boolean closeCanceled)
    {
        this.closeCanceled = closeCanceled;
    }

    /**
     * Sets the mapClosed flag.
     * 
     * @param mapClosed
     */
    public void setMapClosed(boolean mapClosed)
    {
        this.mapClosed = mapClosed;
    }

    /**
     * Sets the mapSaved flag.
     * 
     * @param mapSaved
     *            a boolean.
     */
    public void setMapSaved(boolean mapSaved)
    {
        this.mapSaved = mapSaved;
    }

    /**
     * @param openCanceled
     *            the openCanceled to set
     */
    public void setOpenCanceled(boolean openCanceled)
    {
        this.openCanceled = openCanceled;
    }

    /**
     * @param saveCanceled
     *            the saveCanceled to set
     */
    public void setSaveCanceled(boolean saveCanceled)
    {
        this.saveCanceled = saveCanceled;
    }

}
