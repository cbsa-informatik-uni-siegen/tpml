package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Enumeration;

import common.Environment;
import expressions.Expression;
import expressions.Location;

/**
 *
 * Class providing support to render an environment
 * @author marcell
 * @version $Id$
 */
public class EnvironmentRenderer<S, E> extends AbstractRenderer {

	
//	private		Store				store;
	
	private 	Environment<S, E>	environment;	
	/**
	 * 
	 */
	public EnvironmentRenderer (Environment<S, E> environment) {
		this.environment = environment;
	}
	
	
	/**
	 * 
	 * @return				The actual needed size for the environment
	 */
	public Dimension getNeededSize () {
		int width = AbstractRenderer.envFontMetrics.getHeight() * 2;
		
		width += AbstractRenderer.envFontMetrics.getAscent();
		int numElements = 0;
		Enumeration<S> symbols = environment.symbols();
		while (symbols.hasMoreElements()) {
			S s = symbols.nextElement();
			E e = this.environment.get(s);
			width += AbstractRenderer.envFontMetrics.stringWidth(s + ": " + e);
			numElements++;
		}
		if (numElements >= 2) {
			width += (numElements-1) * AbstractRenderer.envFontMetrics.stringWidth(", ");
		}
		 		
		return new Dimension (width, AbstractRenderer.envFontMetrics.getHeight());
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
	 * @return	Returns the xpos behind the bracket
	 */
	public int render(int x, int y, int height, Graphics gc) {
		
		
		int posX = x;
		int posY = y + height / 2 + AbstractRenderer.envFontMetrics.getAscent() / 2;

		gc.setFont(AbstractRenderer.envFont);
		gc.setColor(Color.BLACK);
		// draw the leading
		// we will not render the '[' here it will be rendered
		// by hand to get it in the right size

		int bracketWidth = height / 10;
		if (bracketWidth > AbstractRenderer.envFontMetrics.getAscent ()) {
			bracketWidth = AbstractRenderer.envFontMetrics.getAscent ();
		}
		
		gc.setColor(Color.BLACK);
		gc.drawLine (posX + bracketWidth, y, posX, y);
		gc.drawLine (posX, y, posX, y + height - 1);
		gc.drawLine (posX, y + height - 1, posX + bracketWidth, y + height - 1);
		
		posX += bracketWidth + AbstractRenderer.envFontMetrics.getAscent() / 2;
		
		Enumeration<S> symbols = this.environment.symbols();
		
		while (symbols.hasMoreElements()) {
			S s = symbols.nextElement();
			E e = this.environment.get (s);
			// draw the name of the location plus the expression value
			gc.setColor(AbstractRenderer.envColor);
			String locString = s.toString(); 
			gc.drawString(locString, posX, posY);
			posX += AbstractRenderer.envFontMetrics.stringWidth(locString);

			gc.setColor(new Color(128, 128, 128));
			locString = ": " + e;
			gc.drawString(locString, posX, posY);
			posX += AbstractRenderer.envFontMetrics.stringWidth(locString);
			
			if (symbols.hasMoreElements()) {
				gc.drawString(", ", posX, posY);
				posX += AbstractRenderer.envFontMetrics.stringWidth(", ");
			}
		}
		posX += bracketWidth + AbstractRenderer.envFontMetrics.getAscent() / 2;
		
		gc.setColor(Color.BLACK);
		gc.drawLine (posX - bracketWidth, y, posX, y);
		gc.drawLine (posX, y, posX, y + height - 1);
		gc.drawLine (posX, y + height - 1, posX - bracketWidth, y + height - 1);
		
		return posX;
	}
	
}
