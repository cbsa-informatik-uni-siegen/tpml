package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typeinference.TypeEquation;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.components.ShowBonds;


/**
 * Subclass of the {@link AbstractRenderer} providing the rendering
 * the TypeFormulars
 * 
 * @author michael
 *
 */
public class TypeFormularRenderer extends AbstractRenderer {
	
	/**
	 * the Renderer for the environments
	 */
	private EnvironmentRenderer environmentRenderer;
	/**
	 * the Renderer for the expressions and...
	 */
	private PrettyStringRenderer prettyStringrenderer;
	
	/**
	 * the List of Strings for the tooltip
	 */
	private ArrayList <String> CollapStrings;
	
	/**
	 * the List of areas for the tooltip
	 */
	private ArrayList <Rectangle> CollapAreas;

	/**
	 * The TypeFOrmulars that should be rendered.
	 */
	private ArrayList <TypeFormula> typeFormulaList;
	
	
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
	//private static final String	collapsString = ", ...";
	
	/**
	 * 
	 */
	private static final String	arrowString = "\u22b3";
	
	/**
	 * 
	 *
	 */
	public TypeFormularRenderer() {
		//this.bracketSize 		= AbstractRenderer.fontDescent;
		this.collapsed 			= false;
		this.collapsedArea	= new Rectangle ();
		
		this.CollapAreas = new ArrayList<Rectangle>();
		this.CollapStrings = new ArrayList<String>();
	}

	/**
	 * Sets the TypeFormulars.
	 * @param typeFormulaListP 
	 * 
	 */
	public void setTypeFormulaList (ArrayList <TypeFormula> typeFormulaListP ) {
		this.typeFormulaList = typeFormulaListP ;
		
		// create the string that can be shown in an tooltip 
		// on level above in the CompoundExpression
		//Enumeration<S> env = environment.symbols();
		
		this.collapsedString = null;
		
		//TODO das wird kompliziert, da viele collapsedStrings nötig werden...
		//get every item from list, check if enviroment...
		//int countEnviroments...
		//for (int i = 0; i>typeFormulaList.size(); i++)
		//{
//			
//		}
//		
//		if (typeFormulaList.size() > 0) {
//			
//			this.collapsedString = typeFormulaList.get(0).toString();
//			
//			for (int i = 1 ; i < typeFormulaList.size(); i++)
//			{				
//				this.collapsedString += ", " + typeFormulaList.get(i).toString();
//			}
//		}
	}
	
	
	/**
	 * Calculates the size, that is needed to propperly render
	 * the TypeFormula.
	 * @param y //TODO mal sehen, ob wir das brauchen...
	 * 
	 * @return The size needed to render the environment.
	 */
	public Dimension getNeededSize (int y) {
		Dimension result = new Dimension (2 * AbstractRenderer.keywordFontMetrics.stringWidth(("{")), AbstractRenderer.fontHeight);
		
		//Enumeration<S> env = this.environment.symbols();
		
		if  ( typeFormulaList.size() == 0 ) 
		{
			// secure some space between the two brackets when
			// no content is there to be shown
			result.width += 5;
		}
		else 
		{
			//first we will render the solve
			int einrücken = AbstractRenderer.keywordFontMetrics.stringWidth("solve {");
			result.width += einrücken;
			
			
			// get the first element
			TypeFormula t = typeFormulaList.get(0);

			// if there is more then only one element in the environment
			// the same will happen and the hight will be counted...
			result.height = typeFormulaList.size() * AbstractRenderer.fontHeight;
			for (int i = 0; i < typeFormulaList.size(); i++)
			{
				int lineWidth = einrücken;

				t = typeFormulaList.get(i);
				if (t instanceof TypeEquation)
				{
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth(t.toString());
				}
				else
				// if (t instanceof TypeJudgement)
				{
					TypeEnvironment environment = t.getEnvironment();
					Expression expression = t.getExpression();
					Type type = t.getType();
					// lineWidth = lineWidth + 2*AbstractRenderer.keywordFontMetrics.stringWidth("[");
					// TODO TExt in Box...
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth(environment.toString());
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth(arrowString);
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth(expression.toString());
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth("::");
					lineWidth = lineWidth + AbstractRenderer.keywordFontMetrics.stringWidth(type.toString());

				}
				testAusgabe("Die Momentane Breite ist: " + result.width);
				testAusgabe("Die aktuelle Zeile ist: " + lineWidth);
				if (lineWidth > result.width)
				{
					result.width = lineWidth;
				}
				testAusgabe("Jetzt ist die Breite: " + result.width);

			}
				// result.width += AbstractRenderer.expFontMetrics.stringWidth(TypeFormularRenderer.collapsString);

		}
		//TODO Test
		//result.height = y / 2 + ((AbstractRenderer.fontHeight)*typeFormulaList.size());
		
		return result;
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
	 * returns a list of areas where the tooltips should be come the 1.
	 * element coresponds to the 1. element int the getCollapStrings
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList <Rectangle> getCollapAreas ()
	{
		return this.CollapAreas;
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
	 * get the Strings for tooltips. the 1. string corresponds to the first 
	 * element of the list of rectangles
	 *
	 * @return
	 */
	public ArrayList <String> getCollapStrings ()
	{
		return CollapStrings;
	}
	
	
	/**
	 * Renders the Typeformulas.<br>
	 * <br>
	 * The Typeformulas are rendered line by lien. They will appear
	 * starting <i>y</i> and ending <i>(y + height></i>.
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
		
		//TODO was soll denn das hier nochmal
		gc.setColor(this.alternativeColor != null ? this.alternativeColor : Color.BLACK);
		
		// the left bracket
		//gc.drawLine (x, y, x + this.bracketSize, y);
		//gc.drawLine (x, y, x, y + height - 1);
		//gc.drawLine (x, y + height - 1, x + this.bracketSize, y + height - 1);
		
		// the right bracket
		//gc.drawLine (x + width - 1, y, x + width - 1 - this.bracketSize, y);
		//gc.drawLine (x + width - 1, y, x + width - 1, y + height - 1);
		//gc.drawLine (x + width - 1, y + height - 1, x + width - 1 -this.bracketSize, y + height - 1);

		// calculate the vertical center of the available space 
		int posX = x ;
		int posY = y + height / 2;
		//int posY = y + AbstractRenderer.fontHeight;
		posY += AbstractRenderer.fontAscent  / 2;
		
		// find the first element in the enumeration if there is one
		
		this.collapsed = false;
		//Enumeration<S> env = this.environment.symbols();
		if (typeFormulaList.size() > 0) {
			// get the first element
			TypeFormula t = typeFormulaList.get(0);
			
			//Render the solve {
			//save the einrückung
			int einrücken=posX;
			int höhe = 0;
			
			gc.setFont(AbstractRenderer.keywordFont);
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.keywordColor); //if then else
			
			gc.drawString("solve", posX, posY);
			posX += AbstractRenderer.keywordFontMetrics.stringWidth("solve ");
			
			gc.setFont(AbstractRenderer.expFont);
			//gc.setColor(AbstractRenderer.expColor);
			gc.setColor(this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.expColor); //if then else			
			gc.drawString("{", posX, posY);
			posX += AbstractRenderer.expFontMetrics.stringWidth("{");

			höhe = Math.max(( keywordFontMetrics.getHeight()), expFontMetrics.getHeight());
			
			
			//calculate the einrückung
				//einrücken = AbstractRenderer.keywordFontMetrics.stringWidth("solve {");
			einrücken = posX-einrücken;
			
			for (int i = 0; i < typeFormulaList.size(); i++)
			{
				t = typeFormulaList.get(i);
				if (t instanceof TypeEquation)
				{
					gc.drawString(t.toString(), posX, posY);
					gc.setColor(expColor);
					posX += AbstractRenderer.expFontMetrics.stringWidth(t.toString());
					//every but the last line needs an linebreak
					if (i<(typeFormulaList.size()-1))
					{
						posX = x+einrücken;
						posY += AbstractRenderer.fontHeight;
					}
					
				}
				else if (t instanceof TypeJudgement)
				{
					TypeEnvironment environment = t.getEnvironment();
					Expression expression = t.getExpression();
					Type type = t.getType();
					
					//TODO die entsprechendne Renderer einbinden
					prettyStringrenderer = new PrettyStringRenderer();
					environment.symbols();
					environment.identifiers();
					environmentRenderer = new EnvironmentRenderer<Enumeration, Enumeration>();
					environmentRenderer.setEnvironment(environment);
					
					//environment.
					gc.setColor(AbstractRenderer.expColor);
					//gc.drawString(environment.toString(), posX, posY);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(environment.toString());
					
					//System.out.println("Die Größe: "+environmentRenderer.getNeededSize().width); 
					
					environmentRenderer.renderer(posX, posY-(environmentRenderer.getNeededSize().height / 2) - fontAscent / 2, environmentRenderer.getNeededSize().width, environmentRenderer.getNeededSize().height, gc);
				
					posX += environmentRenderer.getNeededSize().width;
					höhe = Math.max(höhe,  environmentRenderer.getNeededSize().height);
					//TODO Umsetzen des Highlightextes...
					String envCollapsedString = environmentRenderer.getCollapsedString();
					if (envCollapsedString != null)
					{
						this.collapsed = true;
						this.CollapAreas.add(environmentRenderer.getCollapsedArea());
						this.CollapStrings.add(environmentRenderer.getCollapsedString());
					}
				
					//gc.drawString("]", posX, posY);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth("]");
					
					gc.setColor(AbstractRenderer.expColor);
					gc.drawString(arrowString, posX, posY);
					posX += AbstractRenderer.expFontMetrics.stringWidth(arrowString);
					
					//gc.drawString(expression.toString(), posX, posY);
					prettyStringrenderer = new PrettyStringRenderer ();
					ShowBonds bound = new ShowBonds();
					bound.setExpression(expression);
					ToListenForMouseContainer toListenForM = new ToListenForMouseContainer();
					prettyStringrenderer.setPrettyString(expression.toPrettyString());
					Dimension expressionSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					//prettyStringrenderer.render(x, y, height, gc, bound, toListenForM)
					//TODO warum -8
					prettyStringrenderer.render(posX, posY-(expressionSize.height / 2) - fontAscent / 2, expressionSize.height, gc, bound, toListenForM);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(expression.toString());
					posX += expressionSize.width;
					
					gc.setColor(AbstractRenderer.expColor);
					gc.drawString("::", posX, posY);
					posX += AbstractRenderer.expFontMetrics.stringWidth("::");
					
					//gc.drawString(type.toString(), posX, posY);
					//posX += AbstractRenderer.expFontMetrics.stringWidth(type.toString());
					prettyStringrenderer.setPrettyString(type.toPrettyString());
					Dimension typeSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					prettyStringrenderer.render(posX, posY-(expressionSize.height / 2) - fontAscent / 2, expressionSize.height, gc, bound, toListenForM);
					posX += typeSize.width;
					
					//everey line but the last needs a line braek
					if (i<(typeFormulaList.size()-1))
					{
						//System.out.println("Liste hat Elemente: "+typeFormulaList.size());
						//System.out.println("Wir sind bei Element: "+i);
						posX = x+einrücken;
						
						//posY += Math.max(AbstractRenderer.fontHeight, expressionSize.height);
						posY += Math.max(höhe, expressionSize.height);
					}	
				}
			}
			
			gc.setColor(expColor);
			gc.setFont(AbstractRenderer.expFont);
			gc.drawString("}", posX, posY);
			posX += AbstractRenderer.expFontMetrics.stringWidth("}");
			
		}
		
	}
	
	
	 private static void testAusgabe (String s)
	  {
	  	//System.out.println(s);
	  }
	  private static void testAusgabe ()
	  {
	  	//System.out.println();
	  }
	  private static void testAusgabeL (String s)
	  {
	  	//System.out.print(s);
	  }
}
