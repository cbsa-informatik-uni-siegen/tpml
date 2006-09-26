package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;

import de.unisiegen.tpml.core.util.Environment;


public class EnvironmentRenderer<S, E> extends AbstractRenderer {

	private Environment<S, E>		environment;
	
	private int									bracketSize;
	
	private boolean							collapsed;
	
	private Rectangle						collapsedArea;
	
	private String							collapsedString;
	
	private static final String	collapsString = ", ...";
	
	public EnvironmentRenderer() {
		this.bracketSize 		= AbstractRenderer.fontDescent;
		this.collapsed 			= false;
		this.collapsedArea	= new Rectangle ();
	}

	public void setEnvironment (Environment<S, E> environment) {
		this.environment = environment;
		
		// create the string that can be shown in an tooltip 
		// on level above in the CompoundExpression
		Enumeration<S> env = environment.symbols();

		this.collapsedString = null;
		if (env.hasMoreElements()) {
			S s = env.nextElement();
			E e = environment.get(s);
			this.collapsedString = s.toString() + ": " + e.toString();
			
			while (env.hasMoreElements()) {
				s = env.nextElement();
				e = environment.get(s);
				
				this.collapsedString += ", " + s.toString() + ": " + e.toString();
			}
		}
	}
	
	
	public Dimension getNeededSize () {
		Dimension result = new Dimension (2 * this.bracketSize, AbstractRenderer.fontHeight);
		
		Enumeration<S> env = environment.symbols();
		
		if (!env.hasMoreElements()) {
			// secure some space between the two brackets when
			// no content is there to be shown
			result.width += this.bracketSize;
		}
		else {
			// get the first element
			S s = env.nextElement();
			E e = environment.get(s);
			
			result.width += AbstractRenderer.keywordFontMetrics.stringWidth(s.toString() + ": ");
			result.width += AbstractRenderer.envFontMetrics.stringWidth(e.toString());
			
			if (env.hasMoreElements()) {
				// if there is more then only one element in the environment
				// the rest will only be displayed ass three dots
				result.width += AbstractRenderer.expFontMetrics.stringWidth(EnvironmentRenderer.collapsString);
			}
		}
		
		
		return result;
	}
	
	public boolean isCollapsed () {
		return this.collapsed;
	}
	
	public Rectangle getCollapsedArea () {
		return this.collapsedArea;
	}
	
	public String getCollapsedString () {
		return this.collapsedString;
	}
	
	public void renderer (int x, int y, int width, int height, Graphics gc) {
		
		//
		// just render the brackets around the environment
		
		gc.setColor(this.alternativeColor != null ? this.alternativeColor : Color.BLACK);
		
		// the left bracket
		gc.drawLine (x, y, x + this.bracketSize, y);
		gc.drawLine (x, y, x, y + height - 1);
		gc.drawLine (x, y + height - 1, x + this.bracketSize, y + height - 1);
		
		// the right bracket
		gc.drawLine (x + width - 1, y, x + width - 1 - this.bracketSize, y);
		gc.drawLine (x + width - 1, y, x + width - 1, y + height - 1);
		gc.drawLine (x + width - 1, y + height - 1, x + width - 1 -this.bracketSize, y + height - 1);

		// calculate the vertical center of the available space 
		int posX = x + this.bracketSize;
		int posY = y + height / 2;
		posY += AbstractRenderer.fontAscent  / 2;
		
		// find the first element in the enumeration if there is one
		
		this.collapsed = false;
		Enumeration<S> env = environment.symbols();
		if (env.hasMoreElements()) {
			// get the first element
			S s = env.nextElement();
			E e = environment.get(s);

			// render the symbol
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.keywordColor);
			gc.setFont(AbstractRenderer.keywordFont);
			gc.drawString(s.toString() + ": ", posX, posY);
			posX += AbstractRenderer.keywordFontMetrics.stringWidth(s.toString() + ": ");
			
			// render the entry
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.envColor);
			gc.setFont(AbstractRenderer.envFont);
			gc.drawString(e.toString(), posX, posY);
			posX += AbstractRenderer.envFontMetrics.stringWidth(e.toString());
			
			if (env.hasMoreElements()) {
				this.collapsed = true;
				gc.drawString(EnvironmentRenderer.collapsString, posX, posY);
				
				this.collapsedArea.x 			= x;
				this.collapsedArea.width 	= width; 
			}
			
		}
		
	}
}
