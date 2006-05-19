package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Enumeration;

import common.Store;
import expressions.Expression;
import expressions.Location;

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
	private		Font			font			= null;
	
	private		FontMetrics		fontMetrics		= null;
	
	private		Store			store;
	/**
	 * 
	 * @param gc
	 * @param font
	 */
	public EnvironmentRenderer (Store store) {
		this.store	= store;
	}
	
	public void setFont (Font font, FontMetrics fontMetrics) {
		this.font			= font;
		this.fontMetrics	= fontMetrics;
	}
	
	
	/**
	 * 
	 * @return				The actual needed size for the environment
	 */
	public Dimension getNeededSize () {
		int width = this.fontMetrics.stringWidth("[]");
		
		int numElements = 0;
		Enumeration<Location> locs = store.locations();
		while (locs.hasMoreElements()) {
			Location l = locs.nextElement();
			Expression exp = this.store.get(l);
			width += this.fontMetrics.stringWidth(l + ": " + exp);
			numElements++;
		}
		if (numElements >= 2) {
			width += (numElements-1) * this.fontMetrics.stringWidth(", ");
		}
		 		
		return new Dimension (width, this.fontMetrics.getHeight());
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
	public void render(int x, int y, Graphics gc) {
		
		
		int posX = x;
		int posY = y + this.fontMetrics.getHeight()- this.fontMetrics.getDescent();

		gc.setFont(this.font);
		gc.setColor(Color.BLACK);
		// draw the leading 
		gc.drawString("[", posX, posY);
		posX += this.fontMetrics.stringWidth("[");
		
		Enumeration<Location> locs = this.store.locations();
		while (locs.hasMoreElements()) {
			Location l = locs.nextElement();
			
			Expression exp = this.store.get(l);
			// draw the name of the location plus the expression value
			String locString = l + ": " + exp;
			gc.drawString(locString, posX, posY);
			posX += this.fontMetrics.stringWidth(locString);
			
			if (locs.hasMoreElements()) {
				gc.drawString(", ", posX, posY);
				posX += this.fontMetrics.stringWidth(", ");
			}
		}
		
		gc.drawString ("]", posX, posY);
	}
	
}
