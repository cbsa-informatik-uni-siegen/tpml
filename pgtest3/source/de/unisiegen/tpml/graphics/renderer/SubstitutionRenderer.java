package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;


/**
 * Subclass of the {@link AbstractRenderer} providing the rendering
 * of the substitutions of the type inference view.
 * 
 * @author michael
 *
 */
public class SubstitutionRenderer extends AbstractRenderer {

	/**
	 * The Substitutions that should be rendered.
	 */
	private ArrayList <DefaultTypeSubstitution> defaultTypeSubstitutionList;
	
	/**
	 * Holds informatioin whether the substitution is collapsed.<br>
	 * <br>
	 * Only the first element of the substitution is show. If there
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
	 * the substitution is collpased.
	 */
	private String							collapsedString;
	
	/**
	 * The String shown instaed of the rest of the substitution if it is collapsed. <br>
   * When the mouse is over it the rest will be whown.
	 */
	private static final String	collapsString = ", ...";
  
  /**
   * If the tooltip gets as wide as maxTooltipWidht the tooltip will be broken
   */
  private static final int maxTooltipWidht = 100;
	
  /** 
   * The String isertedt between the singel substitutions in the tootltip
   */
	private static final String betweenTypSubstitutions = ",  ";
	
	/**
	 * The constructor 
	 *
	 */
	public SubstitutionRenderer() {
		this.collapsed 			= false;
		this.collapsedArea	= new Rectangle ();
	}

	/**
	 * Sets the substitution.
	 * @param defaultTypeSubstitutionListP 
	 * 
	 * @param substitution
	 */
	public void setDefaultTypeSubstitutionList (ArrayList <DefaultTypeSubstitution> defaultTypeSubstitutionListP ) {
		this.defaultTypeSubstitutionList = defaultTypeSubstitutionListP ;
		
		// create the string that can be shown in an tooltip 
		// on level above in the CompoundExpression
		this.collapsedString = null;
		if (defaultTypeSubstitutionList.size() > 0)
		{
      // count the chars to break the tooltip if it gets to wide
			int count = 0;
      // html is needed to format the tooltip
			this.collapsedString = "<html>";
			for (int i = 0; i<defaultTypeSubstitutionList.size(); i++)
			{
				DefaultTypeSubstitution thisDetaultTypeSubstitution = defaultTypeSubstitutionList.get(i);
				String tmp = thisDetaultTypeSubstitution.toString();
				count += tmp.length();
				PrettyString ps = thisDetaultTypeSubstitution.toPrettyString();
				this.collapsedString += PrettyStringToHTML.toHTMLString(ps);
				//every but the last gets a ,
				if (i < defaultTypeSubstitutionList.size() - 1)
				{
					this.collapsedString += betweenTypSubstitutions;	
				}
				
				count += betweenTypSubstitutions.length();
				if (count >= maxTooltipWidht)
				{
					this.collapsedString += "<br>";
					//this.collapsedString += "<br>";
					count = 0;
				}
			}
			this.collapsedString += "</html>";
		}
	}
	
	
	/**
	 * Returns whether the substitution was collapsed.
	 * @return  boolean is collapsed 
	 */
	public boolean isCollapsed () {
		return this.collapsed;
	}
	
	/**
	 * Returns the area whre the ", ..." is diplayed.
	 * @return Rectangle of collapsed area
	 */
	public Rectangle getCollapsedArea () {
		return this.collapsedArea;
	}
	
	/**
	 * Returns the information of the substitution that 
	 * are not displayed.
	 * 
	 * @return String of not displayed substitutions
	 */
	public String getCollapsedString () {
		return this.collapsedString;
	}
	
	
	/**
   * Calculates the size, that is needed to propperly render
   * the substitution.
   * 
   * @return The size needed to render the substitution.
   */
  public Dimension getNeededSize () {
  	Dimension result = new Dimension (0, 0);
  	
  	if  ( defaultTypeSubstitutionList.size() == 0 ) 
  	{
      // secure some space when no content is there to be shown
  		result.width += 10;
      result.height = AbstractRenderer.getAbsoluteHeight ();
  	}
  	else 
  	{
      result.height = AbstractRenderer.getAbsoluteHeight ();
  		// get the first element
  		DefaultTypeSubstitution s = defaultTypeSubstitutionList.get(0);
  		
  		result.width += AbstractRenderer.keywordFontMetrics.stringWidth(s.toString());
  		
  		if (defaultTypeSubstitutionList.size() > 1) {
  			// if there is more then only one element in the substitution
  			// the rest will only be displayed ass three dots
  			result.width += AbstractRenderer.expFontMetrics.stringWidth(SubstitutionRenderer.collapsString);
  		}
  		result.width += AbstractRenderer.expFontMetrics.stringWidth("[");
  		result.width += AbstractRenderer.expFontMetrics.stringWidth("]");
  	}
  	return result;
  }
  
  /**
   * Render the substitution to the baseline
   * 
   * @param x  The left position where the substitution should be displayed
   * @param y  The top position where the substitution should be displayed.
   * @param width   The width the renderer is given to render the substitution.
   * @param height  The Height the renderer is given to render the substitution.
   * @param gc  The Graphics used to render 
   */
  //public void renderBase (int x, int y, int width, int height, Graphics gc )
  //{
   // renderer (x, y-(AbstractRenderer.getAbsoluteHeight () /2), width, height, gc); 
 // }

  /**
	 * Renders the substitution.<br>
	 * <br>
	 * The substitution is always rendered as a single line. It will appear
	 * verticaly centered betwean <i>y</i> and <i>(y + height></i>.
	 * 
	 * @param x The left position where the substitution should be displayed
	 * @param y The top position where the substitution should be displayed.
	 * @param width The width the renderer is given to render the substitution.
	 * @param height The Height the renderer is given to render the substitution.
	 * @param gc  The Graphics used to render 
	 */
	public void renderer (int x, int y, int width, int height, Graphics gc) 
  {
		gc.setColor(this.alternativeColor != null ? this.alternativeColor : Color.BLACK);

		int posX = x;

    int posY = y+(AbstractRenderer.getAbsoluteHeight ())/2;

		this.collapsed = false;

     // if ther is enythuing to render
		if (defaultTypeSubstitutionList.size() > 0) 
		{
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.expColor); //if then else
			gc.setFont(AbstractRenderer.expFont);
			
			//Render the "["
			gc.drawString("[", posX, posY);
			posX += AbstractRenderer.expFontMetrics.stringWidth("[");

			// get the first element
			DefaultTypeSubstitution s = defaultTypeSubstitutionList.get(0);

			// render the symbol
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.expColor); //if then else
			gc.setFont(AbstractRenderer.expFont);
			gc.drawString(s.toString(), posX, posY);
			posX += AbstractRenderer.expFontMetrics.stringWidth(s.toString() );
			
      // if ther are more than one element in the list the rest will be a tooltip.
      // the tooltiptext is defined
			if (defaultTypeSubstitutionList.size() > 1) 
			{
				this.collapsed = true;
				gc.drawString(SubstitutionRenderer.collapsString, posX, posY);
				this.collapsedArea.x 			= posX;
				
				posX += AbstractRenderer.expFontMetrics.stringWidth(SubstitutionRenderer.collapsString);
				
				this.collapsedArea.width 	= (posX-collapsedArea.x);
				
				this.collapsedArea.y = posY - fontHeight;
				this.collapsedArea.height = fontHeight;
			}
			
			//Render the "]"
			gc.drawString("]", posX, posY);
			posX += AbstractRenderer.expFontMetrics.stringWidth("]");
			
		}
		
	}
}
