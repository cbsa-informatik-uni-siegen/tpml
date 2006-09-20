package de.unisiegen.tpml.graphics.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Iterator;

import de.unisiegen.tpml.core.util.Environment;


public class EnvironmentRenderer<S, E> extends AbstractRenderer {

	private Environment<S, E>		environment;
	
	private int									bracketSize;
	
	public EnvironmentRenderer() {
		this.bracketSize = AbstractRenderer.fontHeight;
	}

	public void setEnvironment (Environment<S, E> environment) {
		this.environment = environment;
		
		do {
			System.out.print("Environment ");
			Enumeration<S> env = environment.symbols();
			if (env.hasMoreElements()) {
				S s = env.nextElement();
				E e = environment.get(s);
				System.out.print ("<" + s.getClass().getName() + ", " + e.getClass().getName() + ">" );
			}
			
			System.out.println ();
		} while (false);
		
	}
	
	
	public Dimension getNeededSize () {
		Dimension result = new Dimension (2 * this.bracketSize, AbstractRenderer.fontHeight);
		
		int numberOfSymbols = 0;

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
				result.width += AbstractRenderer.expFontMetrics.stringWidth(", ...");
			}
		}
		
		
		return result;
	}
	
	public void renderer (int x, int y, int width, int height, Graphics gc) {
		
		//
		// just render the brackets around the environment
		
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
		Enumeration<S> env = environment.symbols();
		if (env.hasMoreElements()) {
			// get the first element
			S s = env.nextElement();
			E e = environment.get(s);

			// render the symbol
			gc.setColor(AbstractRenderer.keywordColor);
			gc.setFont(AbstractRenderer.keywordFont);
			gc.drawString(s.toString() + ": ", posX, posY);
			posX += AbstractRenderer.keywordFontMetrics.stringWidth(s.toString() + ": ");
			
			// render the entry
			gc.setColor(AbstractRenderer.envColor);
			gc.setFont(AbstractRenderer.envFont);
			gc.drawString(e.toString(), posX, posY);
			
		}
		
	}
}
