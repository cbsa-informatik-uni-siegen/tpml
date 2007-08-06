package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;

import de.unisiegen.tpml.core.util.Environment;


/**
 * Subclass of the {@link AbstractRenderer} providing the rendering
 * of an environment.
 * 
 * @author marcell
 * @author michael
 *
 * @param <S>
 * @param <E>
 */
public class EnvironmentRenderer<S, E> extends AbstractRenderer {

	/**
	 * The environment that should be rendered.
	 */
	private Environment<S, E>		environment;
	
	/**
	 * The width of the brackets around the environment in pixels.
	 */
	private int									bracketSize;
	
	/**
	 * Holds informatioin whether the environment is collapsed.<br>
	 * <br>
	 * Only the first element of the environment is show. If there
	 * are more than one element, they will only be shown as ", ...".
	 * Than the collapsed flag is <i>true</i> else it is <i>false</i>.
	 */
	private boolean							collapsed;
	
	/**
	 * The rectangle describing the area of the ", ...".<br>
	 * <br>
	 * Can be used to determin where a ToolTip should be displayed.
	 */
	private Rectangle						collapsedArea;
	
	/**
	 * Containing all the informations that are not shown. When
	 * the environment is collpased.
	 */
	private String							collapsedString;
	
	/**
	 * 
	 */
	private static final String	collapsString = ", ...";
	
	/**
	 * 
	 *
	 */
	public EnvironmentRenderer() {
		//this.bracketSize 		= AbstractRenderer.fontDescent;
		//let the bracket be as width as 
		this.bracketSize 		= AbstractRenderer.expFontMetrics.stringWidth("[");
		this.collapsed 			= false;
		this.collapsedArea	= new Rectangle ();
	}

	/**
	 * Sets the environment.
	 * 
	 * @param pEnvironment
	 */
	public void setEnvironment (Environment<S, E> pEnvironment) {
		this.environment = pEnvironment;
		
		// create the string that can be shown in an tooltip 
		// on level above in the CompoundExpression
		Enumeration<S> env = pEnvironment.symbols();

		this.collapsedString = null;
		if (env.hasMoreElements()) {
			S s = env.nextElement();
			E e = pEnvironment.get(s);
			this.collapsedString = s.toString() + ": " + e.toString();
			
			while (env.hasMoreElements()) {
				s = env.nextElement();
				e = pEnvironment.get(s);
				
				this.collapsedString += ", " + s.toString() + ": " + e.toString();
			}
		}
	}
	
	
	/**
	 * Returns whether the environment was collapsed.
	 * @return
	 */
	public boolean isCollapsed () {
		return this.collapsed;
	}
	
	/**
	 * Returns the area whre the ", ..." is diplayed.
	 * @return
	 */
	public Rectangle getCollapsedArea () {
		return this.collapsedArea;
	}
	
	/**
	 * Returns the information of the environment that 
	 * are not displayed.
	 * 
	 * @return
	 */
	public String getCollapsedString () {
		return this.collapsedString;
	}
  
  /**
   * Calculates the size, that is needed to propperly render
   * the environment.
   * 
   * @return The size needed to render the environment.
   */
  public Dimension getNeededSize () {
  	Dimension result = new Dimension (2 * this.bracketSize, AbstractRenderer.getAbsoluteHeight ( ));
  	
  	Enumeration<S> env = this.environment.symbols();
  	
  	if (!env.hasMoreElements()) {
  		// secure some space between the two brackets when
  		// no content is there to be shown
  		result.width += this.bracketSize;
  	}
  	else {
  		// get the first element
  		S s = env.nextElement();
  		E e = this.environment.get(s);
  		
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

//  public void renderBase (int x, int y, int width, int height, Graphics gc )
//  {
//    renderer (x, y-(height / 2) - fontAscent / 2, width, height, gc); 
//  }
	
	
	/**
	 * Renders the environment.<br>
	 * <br>
	 * The environment is always rendered as a single line. It will appear
	 * verticaly centered betwean <i>y</i> and <i>(y + height></i>.
	 * 
	 * @param x The left position where the environment should be displayed
	 * @param y The top position where the environment should be displayed.
	 * @param width The width the renderer is given to render the environment.
	 * @param height The Height the renderer is given to render the envionment.
	 * @param gc
	 */
	public void renderer (int x, int y, int width, int height, Graphics gc) {
		
		//
		// just render the brackets around the environment
		
		gc.setColor(this.alternativeColor != null ? this.alternativeColor : Color.BLACK);
		
		// the left bracket
		int posX = x;
		//int posY = y + height / 2;
		//posY += AbstractRenderer.fontAscent  / 2;
		int posY = y + AbstractRenderer.fontAscent;
		// if the hight is not bigger then the fonhight normal [ ] are used
		// or else the bracket will bew renderd manually. Till now the environments are shorted
		// with three dots (...) so they are never warped. To ignor the bigger size made by an 
		// expression (height <= AbstractRenderer.getAbsoluteHeight()) replaced by if (true)
		if (true)
		{
			gc.setFont(expFont);
			gc.drawString("[", x, posY);
			posX += expFontMetrics.stringWidth("[");
			
			//the right one
			gc.drawString("]", x+width-expFontMetrics.stringWidth("]"), posY);
			
		}
		else
		{
			gc.drawLine (x, y, x + this.bracketSize, y);
			gc.drawLine (x, y, x, y + height - 1);
			gc.drawLine (x, y + height - 1, x + this.bracketSize, y + height - 1);
			
			// the right bracket
			gc.drawLine (x + width - 1, y, x + width - 1 - this.bracketSize, y);
			gc.drawLine (x + width - 1, y, x + width - 1, y + height - 1);
			gc.drawLine (x + width - 1, y + height - 1, x + width - 1 -this.bracketSize, y + height - 1);
			posX += this.bracketSize;
		}
		

		// calculate the vertical center of the available space 
		
		
		
		// find the first element in the enumeration if there is one
		
		this.collapsed = false;
		Enumeration<S> env = this.environment.symbols();
		if (env.hasMoreElements()) {
			// get the first element
			S s = env.nextElement();
			E e = this.environment.get(s);

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
				this.collapsedArea.x = posX;
				gc.drawString(EnvironmentRenderer.collapsString, posX, posY);
				posX += AbstractRenderer.envFontMetrics.stringWidth(collapsString);
				this.collapsedArea.width 	= (posX - this.collapsedArea.x);
				
				//this.collapsedArea.y = posY-(AbstractRenderer.getAbsoluteHeight()-2);
				this.collapsedArea.y = posY-(AbstractRenderer.fontAscent);
				this.collapsedArea.height = AbstractRenderer.getAbsoluteHeight();
			}
			
		}
		
	}
}
