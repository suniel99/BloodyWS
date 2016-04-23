package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.Color;

/**
 * A color manager for GIPSY network graph.
 * 
 * @author Sleiman Rabah.
 */
public class GIPSYColors {

	/**
	 * 3.1 Constants declaration for nodes' colors
	 * <p>
	 * <ul>
	 * <li>COLOR_YELLOW
	 * <li>COLOR_GREEN
	 * <li>COLOR_RED
	 * <li>COLOR_ORANGE
	 * <li>COLOR_PINK
	 * </ul>
	 */
	public static final String COLOR_YELLOW = "Yellow";
	public static final String COLOR_GREEN = "Green";
	public static final String COLOR_BLUE = "Blue";
	public static final String COLOR_RED = "Red";
	public static final String COLOR_ORANGE = "Orange";
	public static final String COLOR_PINK = "Pink";
	public static final String COLOR_NAVY = "Navy";
	public static final String COLOR_TEAL = "Teal";
	public static final String COLOR_BROWN = "Brown";
	public static final String COLOR_LIGHTBLUE = "Lightblue";
	public static final String[] GIPSYNodesColorsList = new String[] {
			"Please Select", COLOR_YELLOW, COLOR_BLUE, COLOR_ORANGE,
			COLOR_PINK, COLOR_RED, COLOR_GREEN, COLOR_NAVY, COLOR_BROWN,
			COLOR_LIGHTBLUE, COLOR_TEAL };

	// Teal color.
	public final static Color oTeal = new Color(0, 128, 128);
	public final static Color TEAL = oTeal;

	// Brown color.
	public final static Color oBrown = new Color(205, 51, 51);
	public final static Color BROWN = oBrown;

	// Navy color.
	public final static Color oNavy = new Color(0, 0, 128);
	public final static Color NAVY = oNavy;

	// SGI Lightblue color.
	public final static Color oLightBlue = new Color(125, 158, 192);
	public final static Color LIGHTBLUE = oLightBlue;

	public static Color getColorByName(String strColor) {

		Color oColor = null;

		if (COLOR_YELLOW.equals(strColor)) {
			oColor = Color.yellow;
		} else if (COLOR_BLUE.equals(strColor)) {
			oColor = Color.CYAN;
		} else if (COLOR_GREEN.equals(strColor)) {
			oColor = Color.GREEN;
		} else if (COLOR_ORANGE.equals(strColor)) {
			oColor = Color.orange;
		} else if (COLOR_PINK.equals(strColor)) {
			oColor = Color.PINK;
		} else if (COLOR_RED.equals(strColor)) {
			oColor = Color.RED;
		} else if (COLOR_LIGHTBLUE.equals(strColor)) {
			oColor = GIPSYColors.LIGHTBLUE;
		} else if (COLOR_TEAL.equals(strColor)) {
			oColor = GIPSYColors.TEAL;
		} else if (COLOR_BROWN.equals(strColor)) {
			oColor = GIPSYColors.BROWN;
		} else if (COLOR_NAVY.equals(strColor)) {
			oColor = GIPSYColors.NAVY;
		}

		return oColor;
	}
}
