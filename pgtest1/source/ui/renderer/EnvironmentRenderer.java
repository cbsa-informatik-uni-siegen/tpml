package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
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
public class EnvironmentRenderer extends AbstractRenderer {

	
	private		Store			store;
	/**
	 * 
	 * @param gc
	 * @param font
	 */
	public EnvironmentRenderer (Store store) {
		this.store	= store;
	}
	
	
	/**
	 * 
	 * @return				The actual needed size for the environment
	 */
	public Dimension getNeededSize () {
		int width = this.envFontMetrics.getHeight() * 2;
		
		width += this.envFontMetrics.getAscent();
		int numElements = 0;
		Enumeration<Location> locs = store.locations();
		while (locs.hasMoreElements()) {
			Location l = locs.nextElement();
			Expression exp = this.store.get(l);
			width += this.envFontMetrics.stringWidth(l + ": " + exp);
			numElements++;
		}
		if (numElements >= 2) {
			width += (numElements-1) * this.envFontMetrics.stringWidth(", ");
		}
		 		
		return new Dimension (width, this.envFontMetrics.getHeight());
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
	public void render(int x, int y, int height, Graphics gc) {
		
		
		int posX = x;
		int posY = y + height / 2 + this.envFontMetrics.getAscent() / 2;

		gc.setFont(this.envFont);
		gc.setColor(Color.BLACK);
		// draw the leading
		// we will not render the '[' here it will be rendered
		// by hand to get it in the right size

		int bracketWidth = height / 10;
		if (bracketWidth > this.envFontMetrics.getAscent ()) {
			bracketWidth = this.envFontMetrics.getAscent ();
		}
		
		gc.setColor(Color.BLACK);
		gc.drawLine (posX + bracketWidth, y, posX, y);
		gc.drawLine (posX, y, posX, y + height - 1);
		gc.drawLine (posX, y + height - 1, posX + bracketWidth, y + height - 1);
		
		posX += bracketWidth + this.envFontMetrics.getAscent() / 2;
		
		Enumeration<Location> locs = this.store.locations();
		
		while (locs.hasMoreElements()) {
			Location l = locs.nextElement();
			
			Expression exp = this.store.get(l);
			// draw the name of the location plus the expression value
			gc.setColor(this.envColor);
			String locString = l.toString(); 
			gc.drawString(locString, posX, posY);
			posX += this.envFontMetrics.stringWidth(locString);

			gc.setColor(new Color(128, 128, 128));
			locString = ": " + exp;
			gc.drawString(locString, posX, posY);
			posX += this.envFontMetrics.stringWidth(locString);
			
			if (locs.hasMoreElements()) {
				gc.drawString(", ", posX, posY);
				posX += this.envFontMetrics.stringWidth(", ");
			}
		}
		posX += bracketWidth + this.envFontMetrics.getAscent() / 2;
		
		gc.setColor(Color.BLACK);
		gc.drawLine (posX - bracketWidth, y, posX, y);
		gc.drawLine (posX, y, posX, y + height - 1);
		gc.drawLine (posX, y + height - 1, posX - bracketWidth, y + height - 1);
		
	}
	
}
