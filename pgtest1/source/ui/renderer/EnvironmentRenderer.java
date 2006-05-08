package ui.renderer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;

/**
 *
 * Class providing support to render an environment
 * @author marcell
 * @version $Id$
 */
public class EnvironmentRenderer {

	
	/**
	 * Font that should be used to render the stuff
	 */
	private		Font			font;
	
	/**
	 * 
	 * @param gc
	 * @param font
	 */
	public EnvironmentRenderer (Font font) {
		this.font	= font;
	}
	
	
	/**
	 * Calculates the "best" size needed for the Environment.
	 * <br>
	 * The returned Dimension may not fit the hint size.
	 * 
	 * @param environment	The environment containing the key-value-pairs 
	 * @param hint			A size the gui "wants" to spend for the environment
	 * @return				The actual needed size for the environment
	 */
	public Dimension getNeededSize (HashMap environment, Dimension hint) {
	
		return new Dimension (0, 0);
	}
	
	/**
	 * Renders the Environment at the position given by (x, y).
	 * <br>
	 * It is guarenteed that the size needed for the rendering is exactly the
	 * Size calculated by {@link #getNeededSize(HashMap, Dimension)}.
	 * 
	 * @param	x			The horizontal coordinate to render
	 * @param	y			The vertical coordinate to render
	 * @param 	environment	The environment that should be rendered.
	 * @param	gc			The graphics context needed to render the content
	 */
	public void render(int x, int y, HashMap environment, Graphics2D gc) {
		
	}
	
}
