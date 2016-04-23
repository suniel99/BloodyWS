/**
 * VertexFontTransformer.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: VertexFontTransformer.java,v 1.2 2011/08/18 04:17:44 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import java.awt.Font;
import java.io.Serializable;

import org.apache.commons.collections15.Transformer;

/**
 * A class used to change the text's format/font displayed on inside the graph
 * nodes.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:44 $
 */
public class VertexFontTransformer<V> implements Transformer<V, Font>,
        Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = -6275206384711964822L;
    protected boolean bold = false;

    /**
     * Set the bold attribute.
     * 
     * @param bold
     *            boolean indicating whether the font must be bold or not.
     */
    public void setBold(boolean bold)
    {
        this.bold = bold;
    }

    /**
     * Used to transform the font displayed inside a node.
     * 
     * @param v
     *            which is a generic graph node.
     * @return a java font object.
     */
    public Font transform(V v)
    {
        if (bold)
            return new Font("Helvetica", Font.BOLD, 16);
        else
            return new Font("Helvetica", Font.PLAIN, 12);
    }
}