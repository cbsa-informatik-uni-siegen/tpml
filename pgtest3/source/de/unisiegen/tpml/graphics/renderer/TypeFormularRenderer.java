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
//TODO Renderer als Klassenvariable einführen und die Höhe und breite in der
//getNeededSize auch mit deren Größen berechnen

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
	private ArrayList <String> collapsedStrings;
	
	/**
	 * the List of alle Elements of the TypeFormularRenderer with its areas
	 */
	private ArrayList <Rectangle> typeFprmularPostitions;
	
	private ArrayList <Integer> typeEquations;
	
	private int remebmbertoChange=-1; 
	
	private Rectangle markedArea;
	
	/**
	 * the List of areas for the tooltip
	 */
	private ArrayList <Rectangle> collapsedAreas;

	/**
	 * The TypeFOrmulars that should be rendered.
	 */
	private ArrayList <TypeFormula> typeFormulaList;
	
	/**
	 * the ListenForMouseContainer
	 */
	private ToListenForMouseContainer toListenForM;
	
	
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
		
		this.collapsedAreas = new ArrayList<Rectangle>();
		this.typeFprmularPostitions = new ArrayList <Rectangle>(); 
		this.typeEquations = new ArrayList <Integer> ();
		this.collapsedStrings = new ArrayList<String>();
	}

	/**
	 * Sets the TypeFormulars.
	 * @param typeFormulaListP 
	 * 
	 */
	public void setTypeFormulaList (ArrayList <TypeFormula> typeFormulaListP ) 
	{
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
	 * Sets the toListenForMouseContrainer.
	 * @param tlfmcP 
	 * 
	 */
	public void setToListenForMoudeContainer (ToListenForMouseContainer tlfmcP)
	{
		this.toListenForM = tlfmcP;
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
	public ArrayList <Rectangle> getCollapsedAreas ()
	{
		return this.collapsedAreas;
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
	public ArrayList <String> getCollapsedStrings ()
	{
		return collapsedStrings;
	}
	
	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList <Rectangle> getTypeFprmularPostitions ()
	{
		return this.typeFprmularPostitions;
	}
	
	public void markArea (int x, int y, int width, int height, Graphics gc, int i)
	{
		gc.setColor(Color.BLUE);
		gc.drawRect(x, y-height, width, height);
		int toChange =  this.typeEquations.get(i).intValue();
		if (remebmbertoChange == toChange)
		{
			System.out.println("Die sind gleich");
			remebmbertoChange = -1;
			markedArea = null;
		}
		else if (remebmbertoChange == -1)
		{
			remebmbertoChange = toChange;
			System.out.println("neuer Wert gemerkt...");
			markedArea = new Rectangle (x, y-height, width, height);
		}
		else
		{
			System.out.println("Tauschen: "+remebmbertoChange + " mit "+toChange);
			TypeFormula firstElement = typeFormulaList.get(remebmbertoChange);
			TypeFormula secondElement = typeFormulaList.get(toChange);
			typeFormulaList.remove(remebmbertoChange);
			typeFormulaList.add(remebmbertoChange, secondElement);
			typeFormulaList.remove(toChange);
			typeFormulaList.add(toChange, firstElement);
			remebmbertoChange = -1;
			markedArea = null;
			
			
		}	
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
		//int posY = y + height / 2;
		int posY = y + AbstractRenderer.fontHeight/2;
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
			
			prettyStringrenderer = new PrettyStringRenderer();
			typeFprmularPostitions = new ArrayList <Rectangle> ();
			typeEquations = new ArrayList <Integer> ();
			for (int i = 0; i < typeFormulaList.size(); i++)
			{
				t = typeFormulaList.get(i);
				if (t instanceof TypeEquation)
				{
					TypeEquation s = (TypeEquation)t;
					
					//gc.drawString(t.toString(), posX, posY);
					//posX += AbstractRenderer.expFontMetrics.stringWidth(t.toString());
					prettyStringrenderer.setPrettyString(s.toPrettyString());
					Dimension typeEquationSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					ShowBonds bound = new ShowBonds();
					//TODO Wenn es keine Expression ist, dann muss auch was gehen
					
					bound.setExpression(null);
					//TODO wir wollen hier den haben, den auch die Compoundexpression hat
					//ToListenForMouseContainer toListenForM = new ToListenForMouseContainer();
					prettyStringrenderer.render(posX, posY-(typeEquationSize.height / 2) - fontAscent / 2, typeEquationSize.height, gc, bound, toListenForM);
					//TODO Merken wir uns mal den Bereich:
					this.typeFprmularPostitions.add(new Rectangle(posX, posY, typeEquationSize.width, typeEquationSize.height));
					this.typeEquations.add(i);
					posX += typeEquationSize.width;
					
					gc.setColor(expColor);
					//posX += AbstractRenderer.expFontMetrics.stringWidth(t.toString());
					//every but the last line needs an linebreak
					if (i<(typeFormulaList.size()-1))
					{
						posX = x+einrücken;
						//posY += AbstractRenderer.fontHeight;
						posY += typeEquationSize.height;
					}
					
				}
				else if (t instanceof TypeJudgement)
				{
					int oldPosX = posX;
					TypeEnvironment environment = t.getEnvironment();
					Expression expression = t.getExpression();
					Type type = t.getType();
					
					//TODO die entsprechendne Renderer einbinden
					
					environment.symbols();
					environment.identifiers();
					environmentRenderer = new EnvironmentRenderer<Enumeration, Enumeration>();
					environmentRenderer.setEnvironment(environment);
					
					//environment.
					gc.setColor(AbstractRenderer.expColor);
					//gc.drawString(environment.toString(), posX, posY);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(environment.toString());
					
					//System.out.println("Die Größe: "+environmentRenderer.getNeededSize().width); 
					Dimension environmentSize = environmentRenderer.getNeededSize() ;
					environmentRenderer.renderer(posX, posY-(environmentRenderer.getNeededSize().height / 2) - fontAscent / 2, environmentRenderer.getNeededSize().width, environmentRenderer.getNeededSize().height, gc);
				
					posX += environmentRenderer.getNeededSize().width;
					höhe = Math.max(höhe,  environmentRenderer.getNeededSize().height);
					//TODO Umsetzen des Highlightextes...
					String envCollapsedString = environmentRenderer.getCollapsedString();
					if (envCollapsedString != null)
					{
						this.collapsed = true;
						this.collapsedAreas.add(environmentRenderer.getCollapsedArea());
						this.collapsedStrings.add(environmentRenderer.getCollapsedString());
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
					//ToListenForMouseContainer toListenForM = new ToListenForMouseContainer();
					prettyStringrenderer.setPrettyString(expression.toPrettyString());
					Dimension expressionSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					//prettyStringrenderer.render(x, y, height, gc, bound, toListenForM)
					//TODO warum -8
					prettyStringrenderer.render(posX, posY-(expressionSize.height / 2) - fontAscent / 2, expressionSize.height, gc, bound, toListenForM);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(expression.toString());
					posX += expressionSize.width;
					
					gc.setColor(AbstractRenderer.expColor);
					gc.setFont(expFont);
					gc.drawString("::", posX, posY);
					posX += AbstractRenderer.expFontMetrics.stringWidth("::");
					
					//gc.drawString(type.toString(), posX, posY);
					//posX += AbstractRenderer.expFontMetrics.stringWidth(type.toString());
					prettyStringrenderer.setPrettyString(type.toPrettyString());
					Dimension typeSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					prettyStringrenderer.render(posX, posY-(expressionSize.height / 2) - fontAscent / 2, expressionSize.height, gc, bound, toListenForM);
					posX += typeSize.width;
					
					this.typeFprmularPostitions.add(new Rectangle(oldPosX, posY, posX-oldPosX, höhe));
					this.typeEquations.add(i);
					
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
			
			if (markedArea != null)
			{
				gc.drawRect(markedArea.x, markedArea.y, markedArea.width, markedArea.height);
			}
			
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
