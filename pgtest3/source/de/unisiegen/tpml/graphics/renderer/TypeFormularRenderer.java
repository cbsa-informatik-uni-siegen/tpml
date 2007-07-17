package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.types.MonoType;
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
	 * the space between two elements of the typeformulalist
	 */
	private static final int SPACEBETWEENELEMENTS = AbstractRenderer.getAbsoluteHeight ( ) / 3;

	/**
	 * the Renderer for the environments
	 */
	private EnvironmentRenderer environmentRenderer;

	/**
	 * the List of Strings for the tooltip
	 */
	private ArrayList < String > collapsedStrings;

	/**
	 * the List of Strings for the tooltip of the A (not used)
	 */
	private ArrayList < String > aStrings;

	/**
	 * the List of List of ArrayStrings for the tooltip of the As
	 */
	private ArrayList < ArrayList < PrettyString >> aPrettyStrings;

	/**
	 * the List of alle Elements of the TypeFormularRenderer with its areas
	 */
	private ArrayList < Rectangle > typeFormularPositions;

	/**
	 * the List of Areas where the Expressions are (to be found by the outline)
	 */
	private ArrayList < Rectangle > expressionPostitions;

	/**
	 * the List of Areas where the Types are (to be found by the outline)
	 */
	private ArrayList < Rectangle > typePositions;

	/**
	 * the List of Areas where the leftTypes are (to be found by the outline)
	 */
	private ArrayList < Rectangle > leftTypePositions;

	/**
	 * the List of Areas where the rightTypes are (to be found by the outline)
	 */
	private ArrayList < Rectangle > rightTypePositions;

	/**
	 * the List of Areas where the As are (to be found by the outline)
	 */
	private ArrayList < Rectangle > aPositions;

	/**
	 * the typeequations
	 */
	private ArrayList < Integer > typeEquations;

	/**
	 * remembers the entry to swapp by . -1 if no entry is remembered
	 */
	private int remebmberToSwapp = -1;

	/**
	 * provides a rectangle arround the entry which should be swapped
	 */
	private Rectangle markedArea;

	/**
	 * the List of areas for the tooltip
	 */
	private ArrayList < Rectangle > collapsedAreas;

	/**
	 * The TypeFOrmulars that should be rendered.
	 */
	private ArrayList < TypeFormula > typeFormulaList;

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
	private boolean collapsed;

	/**
	 * The rectangle describing the area of the ", ...".<br>
	 * <br>
	 * Can be used to determin where a ToolTip should be displayed.
	 */
	private Rectangle collapsedArea;

	/**
	 * The Arrow renderd between typeenvironment and the expression
	 */
	private static final String arrowString = "  " + "\u22b3" + "  ";

	/**
	 * The String renderd between the expressions and the Types
	 */
	private static final String doubleColon = "  " + "::" + "  ";

	/**
	 * The String renderd for the As "A |- "
	 */
	private static final String As = "A";

	//private static final String As =  "A |- ";
	//private static final String As =  "A \u0485 ";
	//private static final String As =  "A \u093E ";
	//private static final String As =  "A \u0B94 ";

	/**
	 * constructor
	 *
	 */
	public TypeFormularRenderer ( ) {
		this.collapsed = false;
		this.collapsedArea = new Rectangle ( );

		this.collapsedAreas = new ArrayList < Rectangle > ( );
		this.typeFormularPositions = new ArrayList < Rectangle > ( );
		this.expressionPostitions = new ArrayList < Rectangle > ( );
		this.typePositions = new ArrayList < Rectangle > ( );
		this.leftTypePositions = new ArrayList < Rectangle > ( );
		this.rightTypePositions = new ArrayList < Rectangle > ( );
		this.aPositions = new ArrayList < Rectangle > ( );
		this.typeEquations = new ArrayList < Integer > ( );
		this.collapsedStrings = new ArrayList < String > ( );
		this.aStrings = new ArrayList < String > ( );
		this.aPrettyStrings = new ArrayList < ArrayList < PrettyString >> ( );
	}

	/**
	 * Sets the TypeFormulars.
	 * @param typeFormulaListP 
	 * 
	 */
	public void setTypeFormulaList ( ArrayList < TypeFormula > typeFormulaListP ) {
		this.typeFormulaList = typeFormulaListP;
	}

	/**
	 * Sets the toListenForMouseContrainer.
	 * @param tlfmcP 
	 * 
	 */
	public void setToListenForMoudeContainer ( ToListenForMouseContainer tlfmcP ) {
		this.toListenForM = tlfmcP;
	}

	/**
	 * returns a list of areas where the tooltips should be come the 1.
	 * element coresponds to the 1. element int the getCollapStrings
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getCollapsedAreas ( ) {
		return this.collapsedAreas;
	}

	/**
	 * get the Strings for tooltips. the 1. string corresponds to the first 
	 * element of the list of rectangles
	 *
	 * @return the collapsedStrings
	 */
	public ArrayList < String > getCollapsedStrings ( ) {
		return collapsedStrings;
	}

	/**
	 * get the Strings for tooltips. the 1. string corresponds to the first 
	 * element of the list of rectangles
	 *
	 * @return the collapsedStrings
	 */
	public ArrayList < String > getAStrings ( ) {
		return aStrings;
	}

	/**
	 * get the Strings for tooltips. the 1. string corresponds to the first 
	 * element of the list of rectangles
	 *
	 * @return the collapsedStrings
	 */
	public ArrayList < ArrayList < PrettyString >> getAPrettyStrings ( ) {
		return aPrettyStrings;
	}

	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getTypeFormularPositions ( ) {
		return this.typeFormularPositions;
	}

	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getExpressionPositions ( ) {
		return this.expressionPostitions;
	}

	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getTypePositions ( ) {
		return this.typePositions;
	}

	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getLeftTypePositions ( ) {
		return this.leftTypePositions;
	}

	/**
	 * returns a list of areas where typeFormulars are
	 *
	 * @return the list of rectangles with areas
	 */
	public ArrayList < Rectangle > getRightTypePositions ( ) {
		return this.rightTypePositions;
	}

	//	/**
	//	 * mars an entry and remebers it to swapp it with the second marked one. This Method 
	//	 * is only fpr testing. Use the draggNDropp method.
	//	 *
	//	 * @param x				discribes the rect to draw
	//	 * @param y				discribes the rect to draw
	//	 * @param width		discribes the rect to draw
	//	 * @param height	discribes the rect to draw
	//	 * @param gc			the graphicCOmponent to draw
	//	 * @param i				the index of the typeeEquation to remember
	//	 */
	//	public void markArea (int x, int y, int width, int height, Graphics gc, int i)
	//	{
	//		gc.setColor(Color.BLUE);
	//		gc.drawRect(x, y-height, width, height);
	//		int toChange =  this.typeEquations.get(i).intValue();
	//		if (remebmberToSwapp == toChange)
	//		{
	//			testAusgabe("Die sind gleich");
	//			remebmberToSwapp = -1;
	//			markedArea = null;
	//		}
	//		else if (remebmberToSwapp == -1)
	//		{
	//			remebmberToSwapp = toChange;
	//			testAusgabe("neuer Wert gemerkt...");
	//			markedArea = new Rectangle (x, y-height, width, height);
	//		}
	//		else
	//		{
	//			testAusgabe("Tauschen: "+remebmberToSwapp + " mit "+toChange);
	//			TypeFormula firstElement = typeFormulaList.get(remebmberToSwapp);
	//			TypeFormula secondElement = typeFormulaList.get(toChange);
	//			typeFormulaList.remove(remebmberToSwapp);
	//			typeFormulaList.add(remebmberToSwapp, secondElement);
	//			typeFormulaList.remove(toChange);
	//			typeFormulaList.add(toChange, firstElement);
	//			remebmberToSwapp = -1;
	//			markedArea = null;
	//		}	
	//	}

	//	/**
	//	 * another implementation for the dragg'n'dropp. Instead of inserting the element and 
	//	 * adding 1 to the index of all following it swapps the elements
	//	 *
	//	 * @param x index of the first element
	//	 * @param y	index of the second element
	//	 */
	//	public void swapp (int x, int y)
	//	{
	//		TypeFormula firstElement = typeFormulaList.get(x);
	//		TypeFormula secondElement = typeFormulaList.get(y);
	//		
	//		typeFormulaList.set(y, firstElement);
	//		typeFormulaList.set(x, secondElement);
	//	}

	/**
	 * Implementation of the dragg'n'dropp. The second elemnt will be inserted into the list at the position of the
	 * first one and deleted from its old position. Everey index of the following elements will increase by 1
	 * 
	 * @param x
	 *          index of the first element
	 * @param y
	 *          index of the second element
	 */
	public void draggNDropp ( int x, int y ) {
		// remember the second element
		TypeFormula secondElement = typeFormulaList.get ( y );
		// remove it
		typeFormulaList.remove ( y );
		// Inserts it at the position of the first element in the list. Shifts the element
		// currently at that position and any subsequent elements. (adds one to their indices).
		typeFormulaList.add ( x, secondElement );
	}

	/**
	 * Calculates the size, that is needed to propperly render
	 * the TypeFormula.
	 * @param maxWidth
	 * 
	 * @return The size needed to render the environment.
	 */
	public Dimension getNeededSize ( int maxWidth ) {

		//Abfabg komisches Verhalten der GUI
		if ( maxWidth < 0 ) {
			return new Dimension ( 0, 0 );
		}
		//Dimension result = new Dimension (2 * AbstractRenderer.keywordFontMetrics.stringWidth(("{")), AbstractRenderer.getAbsoluteHeight ( ));
		Dimension result = new Dimension ( 0, 0 );

		//the space betrween the entries...
		//result.height += spaceBetweenLines * typeFormulaList.size()-1;

		testAusgabe ( "Das ist ja toll: " + maxWidth );

		{
			//first we need the space for the solve
			int insertSpace = AbstractRenderer.keywordFontMetrics.stringWidth ( "solve " );
			insertSpace += AbstractRenderer.expFontMetrics.stringWidth ( "{" );

			//result.width += insertSpace;

			//Wir brauchen einen prittyStringrenderer, damit wir die breiten herausbekommen
			PrettyStringRenderer prettyStringrenderer;

			//Wir brauchen 4 Breiten, davon das Maximum. Jeden Durchlauf der Forschleife werden diese Werte berechent,
			//und letztlich brauchen wir dann das Maximum dieser maxima. daher merken wir uns dieses Maximum
			int lineWidthMaxAll = insertSpace;

			for ( int i = 0; i < typeFormulaList.size ( ); i++ ) {
				//Wir haben bis zu 4 Breiten, von denen wir dann das Maximum zurückgeben müssen...
				//Eventuell geht aber auch alles in einen Zeile, dann bleieben die anderen eben einrücken...
				int lineWidthTypeFormula = insertSpace;
				int lineWidthEnvironment = insertSpace;
				int lineWidthExpression = insertSpace;
				int lineWidthType = insertSpace;

				int lineHeightTypeFormula = 0;
				int lineHeightEnvironment = 0;
				int lineHeightExpression = 0;
				int lineHeightType = 0;

				//Diese Variable bestimmt, ob umgebrochen werden muss oder nicht...
				int restOfWidth = maxWidth - insertSpace;

				TypeFormula t = typeFormulaList.get ( i );

				if ( t instanceof TypeJudgement ) {
					//get the envirement, expression and type
					TypeEnvironment environment = t.getEnvironment ( );
					Expression expression = t.getExpression ( );
					Type type = t.getType ( );

					environmentRenderer = new EnvironmentRenderer < Enumeration, Enumeration > ( );
					environmentRenderer.setEnvironment ( environment );
					Dimension environmentDim = environmentRenderer.getNeededSize ( );

					//width of envireonment
					//lineWidthEnvironment += AbstractRenderer.keywordFontMetrics.stringWidth(environment.toString());
					lineWidthEnvironment += environmentDim.width;
					lineWidthEnvironment += AbstractRenderer.expFontMetrics.stringWidth ( arrowString );
					restOfWidth -= AbstractRenderer.expFontMetrics.stringWidth ( environment.toString ( ) );
					restOfWidth -= AbstractRenderer.expFontMetrics.stringWidth ( arrowString );

					//height of the environment
					//lineHeightEnvironment = AbstractRenderer.getAbsoluteHeight();
					lineHeightEnvironment = environmentDim.height;

					//width of expression
					prettyStringrenderer = new PrettyStringRenderer ( );
					prettyStringrenderer.setPrettyString ( expression.toPrettyString ( ) );
					//prüfe man die Breite, wenn wir alles hätten
					Dimension expressionSize = prettyStringrenderer.getNeededSizeAll_ ( maxWidth - insertSpace );
					expressionSize.width += AbstractRenderer.keywordFontMetrics.stringWidth ( doubleColon );
					testAusgabe ( "Die Breite: " + expressionSize.width + ", " + ( maxWidth - insertSpace ) );
					//Dimension expressionSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					testAusgabe ( "Wenn für die Expression nicht umgebrochen werden muss: " + expressionSize.width );
					//				Wenn es nicht mehr genug Platz ist für die Expression
					if ( restOfWidth < expressionSize.width ) {
						debug ( "Er bricht um... " );
						//new line, new restOfWidth
						restOfWidth = maxWidth - insertSpace;
						//result.height += AbstractRenderer.getAbsoluteHeight();
						//Wenn der in der nächsten Zeile gerendert werden soll, dann hat er mehr Platz
						expressionSize = prettyStringrenderer.getNeededSizeAll_ ( restOfWidth );
						debug ( "Die Expression hat die Größe: " + expressionSize );
						lineWidthExpression = insertSpace + expressionSize.width;
						lineWidthExpression += AbstractRenderer.keywordFontMetrics.stringWidth ( doubleColon );
						restOfWidth -= expressionSize.width;
						restOfWidth -= AbstractRenderer.keywordFontMetrics.stringWidth ( doubleColon );
						lineHeightExpression = expressionSize.height;
						debug ( "Die höhe, die wir gespeichter haben, die wir gleich aufaddieren werden: "
								+ lineHeightExpression );
					} else //the expression fits in the same line like the environment
					{
						//Wenn wir nicht umgebrochen haben, dann müssen wir die Breite der Environment erhöhen...
						testAusgabe ( "Es wurde nicht umgebrochen, also: " + expressionSize.width );
						lineWidthEnvironment += expressionSize.width;
						lineWidthEnvironment += AbstractRenderer.keywordFontMetrics.stringWidth ( doubleColon );
						restOfWidth -= lineWidthEnvironment;

						//the hight of the Environment could be lager
						lineHeightEnvironment = Math.max ( lineHeightEnvironment, expressionSize.height );
					}
					// Wenn nach dem Doppelpunkt nicht mehr genug Platz für den Typ ist...
					prettyStringrenderer.setPrettyString ( type.toPrettyString ( ) );
					Dimension typeSize = prettyStringrenderer.getNeededSize ( maxWidth - insertSpace );
					if ( restOfWidth < typeSize.width ) {
						//lineWidth = einrücken;
						restOfWidth = maxWidth - insertSpace;
						//result.height += expressionSize.height;
						typeSize = prettyStringrenderer.getNeededSize ( restOfWidth );
						lineWidthType = insertSpace + typeSize.width;
						restOfWidth -= lineWidthType;
						lineHeightType = typeSize.height;
					} else {
						//Wenn noch garnicht umgebrochen wurde, dann ist auch lineWidtnExpression noch einrücken...
						if ( lineWidthExpression == insertSpace ) {
							//Dann müssen wir das auch noch der ersten Zeiel zurechnen...
							lineWidthEnvironment += typeSize.width;
							lineHeightEnvironment = Math.max ( lineHeightEnvironment, typeSize.height );
						} else {
							lineWidthExpression += typeSize.width;
							//lineHeightExpression = Math.max(lineHeightEnvironment, typeSize.height);
							lineHeightExpression = Math.max ( lineHeightExpression, typeSize.height );
						}

					}

				} else if ( t instanceof TypeEquationTypeInference ) {
					prettyStringrenderer = new PrettyStringRenderer ( );
					prettyStringrenderer.setPrettyString ( ( ( TypeEquationTypeInference ) t ).toPrettyString ( ) );

					lineWidthEnvironment += prettyStringrenderer.getNeededSize ( Integer.MAX_VALUE ).width;
					//The width for the A
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( As );
					//The width for the nail + the space arround it (The width of the nail will be as big as the width
					//of the String "--"
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( "--" );
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
					//lineWidthTypeFormula += AbstractRenderer.keywordFontMetrics.stringWidth(t.toString());
					testAusgabe ( t.toString ( ) );
					//lineHeightTypeFormula += AbstractRenderer.getAbsoluteHeight();
					lineHeightTypeFormula += prettyStringrenderer.getNeededSize ( Integer.MAX_VALUE ).height;
				} else if ( t instanceof TypeSubType ) {
					prettyStringrenderer = new PrettyStringRenderer ( );
					prettyStringrenderer.setPrettyString ( ( ( TypeSubType ) t ).toPrettyString ( ) );

					lineWidthEnvironment += prettyStringrenderer.getNeededSize ( Integer.MAX_VALUE ).width;
					//The width for the A
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( As );
					//The width for the nail + the space arround it (The width of the nail will be as big as the width
					//of the String "--"
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( "--" );
					lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
					//lineWidthTypeFormula += AbstractRenderer.keywordFontMetrics.stringWidth(t.toString());
					testAusgabe ( t.toString ( ) );
					//lineHeightTypeFormula += AbstractRenderer.getAbsoluteHeight();
					lineHeightTypeFormula += prettyStringrenderer.getNeededSize ( Integer.MAX_VALUE ).height;
				}

				result.height += lineHeightEnvironment + lineHeightExpression + lineHeightType + lineHeightTypeFormula;
				debug ( "Höhe: " + result.height + ", " + lineHeightEnvironment + ", " + lineHeightExpression + ", "
						+ lineHeightType + ", " + lineHeightTypeFormula );

				int lineWidthMax = realMax ( lineWidthEnvironment, lineWidthExpression, lineWidthType, lineWidthTypeFormula );

				lineWidthMaxAll = Math.max ( lineWidthMax, lineWidthMaxAll );

				testAusgabe ( "Die Maximale Breite ist: " + lineWidthMax + " (" + lineWidthEnvironment + ", "
						+ lineWidthExpression + ", " + lineWidthType + ", " + lineWidthTypeFormula + ")" );
				testAusgabe ( "Maximale Breite aller Zeilen dieses Ausdrucks: " + lineWidthMaxAll );

				result.width = lineWidthMaxAll;
			}

		}

		//we also need the space between the Elements
		result.height += ( ( typeFormulaList.size ( ) - 1 ) * SPACEBETWEENELEMENTS );

		//TODO: Test:
		//result.width = realMax(result.width, maxWidth);

		return result;
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
	public void renderer ( int x, int y, int width, int height, Graphics gc ) {

		//width = Math.max(width, maxWidth);

		gc.setColor ( this.alternativeColor != null ? this.alternativeColor : Color.BLACK );

		gc.drawRect ( x, y, width, height - 1 );
		// the left bracket
		//gc.drawLine (x, y, x + this.bracketSize, y);
		//gc.drawLine (x, y, x, y + height - 1);
		//gc.drawLine (x, y + height - 1, x + this.bracketSize, y + height - 1);

		// the right bracket
		//gc.drawLine (x + width - 1, y, x + width - 1 - this.bracketSize, y);
		//gc.drawLine (x + width - 1, y, x + width - 1, y + height - 1);
		//gc.drawLine (x + width - 1, y + height - 1, x + width - 1 -this.bracketSize, y + height - 1);

		int nochNutzbar = width;

		testAusgabe ( "Noich nutzbar am Anfag: " + nochNutzbar );

		// calculate the vertical center of the available space 
		int posX = x;
		//int posY = y + height / 2;
		int posY = y + AbstractRenderer.getAbsoluteHeight ( ) / 2;
		//int posY = y + AbstractRenderer.fontHeight;
		posY += AbstractRenderer.fontAscent / 2;

		// find the first element in the enumeration if there is one

		this.collapsed = false;
		//Enumeration<S> env = this.environment.symbols();
		if ( typeFormulaList.size ( ) > 0 ) {
			// get the first element
			TypeFormula t = typeFormulaList.get ( 0 );

			//Render the solve {
			//save the einrückung
			int einrücken = posX;
			int spaceToNexEntry = 0;

			gc.setFont ( AbstractRenderer.keywordFont );
			gc.setColor ( this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.keywordColor ); //if then else

			gc.drawString ( "solve", posX, posY );
			posX += AbstractRenderer.keywordFontMetrics.stringWidth ( "solve " );

			gc.setFont ( AbstractRenderer.expFont );
			//gc.setColor(AbstractRenderer.expColor);
			gc.setColor ( this.alternativeColor != null ? this.alternativeColor : AbstractRenderer.expColor ); //if then else			
			gc.drawString ( "{", posX, posY );
			posX += AbstractRenderer.expFontMetrics.stringWidth ( "{" );

			spaceToNexEntry = Math.max ( ( keywordFontMetrics.getHeight ( ) ), expFontMetrics.getHeight ( ) );

			//calculate the einrückung
			//einrücken = AbstractRenderer.keywordFontMetrics.stringWidth("solve {");
			einrücken = posX - einrücken;

			testAusgabe ( "Noich nutzbar wenn das Einrücken abgezogen ist: " + nochNutzbar );

			//prettyStringrenderer = new PrettyStringRenderer();
			typeFormularPositions = new ArrayList < Rectangle > ( );
			typeEquations = new ArrayList < Integer > ( );
			for ( int i = 0; i < typeFormulaList.size ( ); i++ ) {
				expressionPostitions.add ( new Rectangle ( 0, 0, 0, 0 ) );
				typePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
				leftTypePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
				rightTypePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
				aPositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
				aStrings.add ( "" );
				aPrettyStrings.add ( new ArrayList < PrettyString > ( ) );
			}

			//for (int i = typeFormulaList.size()-1; i>=0; i--)
			for ( int i = 0; i < typeFormulaList.size ( ); i++ ) {
				//expressionPostitions.add(new Rectangle(0,0,0,0));
				//typePositions.add(new Rectangle(0,0,0,0));
				//leftTypePositions.add(new Rectangle(0,0,0,0));
				//rightTypePositions.add(new Rectangle(0,0,0,0));
				//aPositions.add(new Rectangle(0,0,0,0));
				//aStrings.add("");
				//aPrettyStrings.add(new ArrayList());

				nochNutzbar = width - einrücken;
				testAusgabe ( "Noich nutzbar wenn das Einrücken abgezogen ist: " + nochNutzbar );
				t = typeFormulaList.get ( i );
				if ( t instanceof TypeEquationTypeInference ) {
					//save the pos X where the render starts to render to find out the size later
					int oldX = posX;
					//Renderer, damit die einzel gerendert werden...
					PrettyStringRenderer typeEquationStringrenderer = new PrettyStringRenderer ( );

					//Typeequations werden einfach hingerendert...
					TypeEquationTypeInference s = ( TypeEquationTypeInference ) t;

					//get the highlightinfos
					ShowBonds bondTypeEquation = new ShowBonds ( );
					bondTypeEquation.load ( s );
					bondTypeEquation.getAnnotations ( );

					//Render the A with the nail. It will be renderd in expColor and keyword Font
					gc.setColor ( AbstractRenderer.expColor );
					gc.setFont ( AbstractRenderer.keywordFont );
					//gc.drawString(As, posX, posY);
					gc.drawString ( As, posX, posY );
					this.aPositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.getAbsoluteHeight ( ),
							AbstractRenderer.expFontMetrics.stringWidth ( As ), AbstractRenderer.getAbsoluteHeight ( ) ) );
					//render the A
					posX += AbstractRenderer.keywordFontMetrics.stringWidth ( As );
					//render the nail and the space arround the nail
					posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

					//The nail as Polygon
					Polygon polygon = new Polygon ( );
					polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
							+ AbstractRenderer.fontHeight / 5 );
					polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
							- AbstractRenderer.fontHeight / 5 );
					polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 + fontDescent );
					polygon.addPoint ( posX + AbstractRenderer.keywordFontMetrics.stringWidth ( "--" ), posY
							- AbstractRenderer.fontHeight / 2 + fontDescent );
					polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 + fontDescent );

					//render it
					gc.drawPolygon ( polygon );

					posX += polygon.getBounds ( ).width;
					posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

					//get the seentyps and provide the String for the Tooltip
					SeenTypes < TypeEquationTypeInference > typeEquSeenTypes = ( ( TypeEquationTypeInference ) t )
							.getSeenTypes ( );
					aStrings.set ( i, typeEquSeenTypes.toString ( ) );

					ArrayList < PrettyString > tmp = new ArrayList < PrettyString > ( );

					for ( int z = 0; z < typeEquSeenTypes.size ( ); z++ ) {
						TypeEquationTypeInference tqti = typeEquSeenTypes.get ( z );
						PrettyString ps = tqti.toPrettyString ( );

						tmp.add ( ps );
					}
					//aPrettyStrings.add(tmp);
					aPrettyStrings.set ( i, tmp );

					//tell the Rendere the PrettyString
					typeEquationStringrenderer.setPrettyString ( s.toPrettyString ( ) );
					//TODO prevent the renderer from linewraping. printig?
					//Dimension typeEquationSize = typeEquationStringrenderer.getNeededSize(width - (posX - oldX));
					Dimension typeEquationSize = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );

					//render the typeFormula
					//typeEquationStringrenderer.render(posX, posY-(typeEquationSize.height / 2) - fontAscent / 2, typeEquationSize.width, typeEquationSize.height, gc, bondTypeEquation, toListenForM);
					typeEquationStringrenderer.renderBase ( posX, posY, typeEquationSize.width, typeEquationSize.height, gc,
							bondTypeEquation, toListenForM );

					//now we need to find out the exact position of the left type and the left one
					MonoType left = s.getLeft ( );
					MonoType right = s.getRight ( );
					typeEquationStringrenderer.setPrettyString ( left.toPrettyString ( ) );
					Dimension leftDim = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );
					typeEquationStringrenderer.setPrettyString ( right.toPrettyString ( ) );
					Dimension rightDim = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );

					//this.typeFormularPositions.add(new Rectangle(posX, posY+AbstractRenderer.fontDescent, typeEquationSize.width, typeEquationSize.height));
					this.typeFormularPositions.add ( new Rectangle ( oldX, posY - AbstractRenderer.fontHeight
							+ AbstractRenderer.fontDescent, width, typeEquationSize.height ) );
					this.leftTypePositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
							+ AbstractRenderer.fontDescent, leftDim.width, leftDim.height ) );
					this.rightTypePositions.set ( i,
							new Rectangle ( ( posX + typeEquationSize.width ) - rightDim.width, posY
									- AbstractRenderer.fontHeight + AbstractRenderer.fontDescent, rightDim.width,
									rightDim.height ) );
					// change Benjamin
					//debug("Folgende sollten die gleiche Y-Koordinate haben, und die gleiche Höhe, sonst ist Unfug!"+typeFormularPositions.get(i)+" - "+leftTypePositions.get(i));

					this.typeEquations.add ( i );
					posX += typeEquationSize.width;

					gc.setColor ( expColor );

					//every but the last line needs an linebreak
					if ( i < ( typeFormulaList.size ( ) - 1 ) ) {
						posX = x + einrücken;
						posY += typeEquationSize.height;
						posY += SPACEBETWEENELEMENTS;
					}
				} else if (t instanceof TypeSubType){
						//save the pos X where the render starts to render to find out the size later
						int oldX = posX;
						//Renderer, damit die einzel gerendert werden...
						PrettyStringRenderer typeEquationStringrenderer = new PrettyStringRenderer ( );

						//Typeequations werden einfach hingerendert...
						TypeSubType subType = ( TypeSubType ) t;

						//get the highlightinfos
						ShowBonds bondTypeEquation = new ShowBonds ( );
						bondTypeEquation.load ( subType );
						bondTypeEquation.getAnnotations ( );

						//Render the A with the nail. It will be renderd in expColor and keyword Font
						gc.setColor ( AbstractRenderer.expColor );
						gc.setFont ( AbstractRenderer.keywordFont );
						//gc.drawString(As, posX, posY);
						gc.drawString ( As, posX, posY );
						this.aPositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.getAbsoluteHeight ( ),
								AbstractRenderer.expFontMetrics.stringWidth ( As ), AbstractRenderer.getAbsoluteHeight ( ) ) );
						//render the A
						posX += AbstractRenderer.keywordFontMetrics.stringWidth ( As );
						//render the nail and the space arround the nail
						posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

						//The nail as Polygon
						Polygon polygon = new Polygon ( );
						polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
								+ AbstractRenderer.fontHeight / 5 );
						polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
								- AbstractRenderer.fontHeight / 5 );
						polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 + fontDescent );
						polygon.addPoint ( posX + AbstractRenderer.keywordFontMetrics.stringWidth ( "--" ), posY
								- AbstractRenderer.fontHeight / 2 + fontDescent );
						polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 + fontDescent );

						//render it
						gc.drawPolygon ( polygon );

						posX += polygon.getBounds ( ).width;
						posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

						/*//get the seentyps and provide the String for the Tooltip
						SeenTypes < TypeEquationTypeInference > typeEquSeenTypes = ( ( TypeEquationTypeInference ) t )
								.getSeenTypes ( );
						aStrings.set ( i, typeEquSeenTypes.toString ( ) );

						ArrayList < PrettyString > tmp = new ArrayList < PrettyString > ( );

						for ( int z = 0; z < typeEquSeenTypes.size ( ); z++ ) {
							TypeEquationTypeInference tqti = typeEquSeenTypes.get ( z );
							PrettyString ps = tqti.toPrettyString ( );

							tmp.add ( ps );
						}
						//aPrettyStrings.add(tmp);
						aPrettyStrings.set ( i, tmp );*/

						//tell the Rendere the PrettyString
						typeEquationStringrenderer.setPrettyString ( subType.toPrettyString ( ) );
						//TODO prevent the renderer from linewraping. printig?
						//Dimension typeEquationSize = typeEquationStringrenderer.getNeededSize(width - (posX - oldX));
						Dimension typeEquationSize = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );

						//render the typeFormula
						//typeEquationStringrenderer.render(posX, posY-(typeEquationSize.height / 2) - fontAscent / 2, typeEquationSize.width, typeEquationSize.height, gc, bondTypeEquation, toListenForM);
						typeEquationStringrenderer.renderBase ( posX, posY, typeEquationSize.width, typeEquationSize.height, gc,
								bondTypeEquation, toListenForM );

						//now we need to find out the exact position of the left type and the left one
						MonoType left = subType.getType ( );
						MonoType right = subType.getType2 ( );
						typeEquationStringrenderer.setPrettyString ( left.toPrettyString ( ) );
						Dimension leftDim = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );
						typeEquationStringrenderer.setPrettyString ( right.toPrettyString ( ) );
						Dimension rightDim = typeEquationStringrenderer.getNeededSize ( Integer.MAX_VALUE );

						//this.typeFormularPositions.add(new Rectangle(posX, posY+AbstractRenderer.fontDescent, typeEquationSize.width, typeEquationSize.height));
						this.typeFormularPositions.add ( new Rectangle ( oldX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, width, typeEquationSize.height ) );
						this.leftTypePositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, leftDim.width, leftDim.height ) );
						this.rightTypePositions.set ( i,
								new Rectangle ( ( posX + typeEquationSize.width ) - rightDim.width, posY
										- AbstractRenderer.fontHeight + AbstractRenderer.fontDescent, rightDim.width,
										rightDim.height ) );
						// change Benjamin
						//debug("Folgende sollten die gleiche Y-Koordinate haben, und die gleiche Höhe, sonst ist Unfug!"+typeFormularPositions.get(i)+" - "+leftTypePositions.get(i));

						this.typeEquations.add ( i );
						posX += typeEquationSize.width;

						gc.setColor ( expColor );

						//every but the last line needs an linebreak
						if ( i < ( typeFormulaList.size ( ) - 1 ) ) {
							posX = x + einrücken;
							posY += typeEquationSize.height;
							posY += SPACEBETWEENELEMENTS;
						}
					}else if ( t instanceof TypeJudgement ) {
				
					//save the position and the width and height of the element
					//int startX = posX;
					int startY = posY;
					int startWidht = 0;
					int startHeight = 0;

					//				Renderer, damit die einzel gerendert werden...
					//PrettyStringRenderer prettyStringrenderer = new PrettyStringRenderer();
					environmentRenderer = new EnvironmentRenderer < Enumeration, Enumeration > ( );
					PrettyStringRenderer expressionRenderer = new PrettyStringRenderer ( );
					PrettyStringRenderer typeRenderer = new PrettyStringRenderer ( );

					int oldPosX = posX;
					TypeEnvironment environment = t.getEnvironment ( );
					Expression expression = t.getExpression ( );
					Type type = t.getType ( );

					environment.symbols ( );
					environment.identifiers ( );

					environmentRenderer.setEnvironment ( environment );

					//environment.
					gc.setColor ( AbstractRenderer.expColor );
					//gc.drawString(environment.toString(), posX, posY);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(environment.toString());

					//testAusgabe("Die Größe: "+environmentRenderer.getNeededSize().width); 
					Dimension environmentSize = environmentRenderer.getNeededSize ( );
					//environmentRenderer.renderer(posX, posY-(environmentRenderer.getNeededSize().height / 2) - fontAscent / 2, environmentRenderer.getNeededSize().width, environmentRenderer.getNeededSize().height, gc);
					environmentRenderer.renderBase ( posX, posY, environmentRenderer.getNeededSize ( ).width,
							environmentRenderer.getNeededSize ( ).height, gc );
					startWidht += environmentRenderer.getNeededSize ( ).width;
					startWidht += AbstractRenderer.expFontMetrics.stringWidth ( arrowString );

					posX += environmentRenderer.getNeededSize ( ).width;
					startWidht += environmentRenderer.getNeededSize ( ).width;
					nochNutzbar -= environmentRenderer.getNeededSize ( ).width;

					testAusgabe ( "Noich nutzbar nach der Environment: " + nochNutzbar );

					//höhe = Math.max(höhe,  environmentRenderer.getNeededSize().height);
					spaceToNexEntry = Math.max ( 0, environmentRenderer.getNeededSize ( ).height );

					String envCollapsedString = environmentRenderer.getCollapsedString ( );
					if ( envCollapsedString != null ) {
						this.collapsed = true;
						this.collapsedAreas.add ( environmentRenderer.getCollapsedArea ( ) );
						this.collapsedStrings.add ( environmentRenderer.getCollapsedString ( ) );
					}

					gc.setColor ( AbstractRenderer.expColor );
					gc.drawString ( arrowString, posX, posY );
					posX += AbstractRenderer.expFontMetrics.stringWidth ( arrowString );
					startWidht += AbstractRenderer.expFontMetrics.stringWidth ( arrowString );

					nochNutzbar -= AbstractRenderer.expFontMetrics.stringWidth ( arrowString );

					testAusgabe ( "Noich nutzbar nach Arrow: " + nochNutzbar );

					//gc.drawString(expression.toString(), posX, posY);
					ShowBonds bound = new ShowBonds ( );
					bound.load ( expression );
					//ToListenForMouseContainer toListenForM = new ToListenForMouseContainer();
					expressionRenderer.setPrettyString ( expression.toPrettyString ( ) );
					Dimension expressionSize = expressionRenderer.getNeededSizeAll_ ( width - einrücken );
					//expressionSize.width += AbstractRenderer.expFontMetrics.stringWidth(doubleColon);
					//prettyStringrenderer.render(x, y, height, gc, bound, toListenForM)
					if ( nochNutzbar < expressionSize.width + AbstractRenderer.expFontMetrics.stringWidth ( doubleColon ) ) //Wenn Expression nicht mehr in die Zeile passt, also gebrochen wird...
					{
						testAusgabe ( "Noich nutzbar weniger als wir für die Expression brauchen, neue Zeile: " + nochNutzbar );
						posX = einrücken;
						nochNutzbar = width - einrücken;

						testAusgabe ( "Noich nutzbar in der neuen Zeile nachedem einrücken abgezogen ist...: " + nochNutzbar );

						expressionSize = expressionRenderer.getNeededSizeAll_ ( nochNutzbar );
						//expressionSize.width += AbstractRenderer.expFontMetrics.stringWidth(doubleColon);
						spaceToNexEntry = Math.max ( spaceToNexEntry, expressionSize.height );
						posY += environmentSize.height;
						startHeight += environmentSize.height;

						//expressionRenderer.render(posX, posY-AbstractRenderer.getAbsoluteHeight(), expressionSize.width, expressionSize.height, gc, bound, toListenForM);
						//expressionRenderer.renderBase(posX, posY, expressionSize.width, expressionSize.height, gc, bound, toListenForM);
						expressionRenderer.render ( posX, posY - AbstractRenderer.fontHeight / 2
								- AbstractRenderer.fontAscent / 2, expressionSize.width, expressionSize.height, gc, bound,
								toListenForM );
						startWidht = Math.max ( startWidht, expressionSize.width );
						startHeight += expressionSize.height;
						expressionPostitions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, expressionSize.width, expressionSize.height ) );

					} else //also die Expression noch in die gleiche Zeile kommt, wie die Environment
					{
						expressionRenderer.render ( posX, posY - AbstractRenderer.fontHeight / 2
								- AbstractRenderer.fontAscent / 2, expressionSize.width, expressionSize.height, gc, bound,
								toListenForM );
						startWidht += expressionSize.width;
						startHeight = Math.max ( startHeight, expressionSize.height );
						expressionPostitions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, expressionSize.width, expressionSize.height ) );
						spaceToNexEntry = Math.max ( spaceToNexEntry, expressionSize.height );
						//expressionRenderer.renderBase(posX, posY, expressionSize.width ,expressionSize.height, gc, bound, toListenForM);
					}
					//prettyStringrenderer.render(posX, posY-(expressionSize.height / 2) - fontAscent / 2, expressionSize.height, gc, bound, toListenForM);
					//posX += AbstractRenderer.keywordFontMetrics.stringWidth(expression.toString());
					posX += expressionSize.width;

					startWidht += expressionSize.width;
					nochNutzbar -= expressionSize.width;

					testAusgabe ( "Noich nutzbar nach Expression : " + nochNutzbar );

					gc.setColor ( AbstractRenderer.expColor );
					gc.setFont ( expFont );
					gc.drawString ( doubleColon, posX, posY );
					posX += AbstractRenderer.expFontMetrics.stringWidth ( doubleColon );
					startWidht += AbstractRenderer.expFontMetrics.stringWidth ( doubleColon );
					nochNutzbar -= AbstractRenderer.expFontMetrics.stringWidth ( doubleColon );

					testAusgabe ( "Noich nutzbar nach :: : " + nochNutzbar );

					//gc.drawString(type.toString(), posX, posY);
					//posX += AbstractRenderer.expFontMetrics.stringWidth(type.toString());
					typeRenderer.setPrettyString ( type.toPrettyString ( ) );
					ShowBonds bondType = new ShowBonds ( );
					bondType.load ( type );

					//Dimension typeSize = prettyStringrenderer.getNeededSize(Integer.MAX_VALUE);
					Dimension typeSize = typeRenderer.getNeededSize ( width - einrücken );

					if ( nochNutzbar < typeSize.width ) {
						testAusgabe ( "Noich nutzbar < Typsize, neue Zeile: " + nochNutzbar );
						posX = einrücken;
						nochNutzbar = width - einrücken;

						testAusgabe ( "Noich nutzbar in der neuen Zeile: " + nochNutzbar );

						//posY += Math.max(höhe, expressionSize.height);
						posY += expressionSize.height;
						//startHeight += expressionSize.height;

						//typeRenderer.render(posX, posY-(typeSize.height / 2) - fontAscent / 2, typeSize.width ,typeSize.height, gc, bondType, toListenForM);
						typeRenderer.renderBase ( posX, posY, typeSize.width, typeSize.height, gc, bondType, toListenForM );
						startWidht = Math.max ( startWidht, typeSize.width );
						startHeight += typeSize.height;
						typePositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, typeSize.width, typeSize.height ) );
						posX += typeSize.width;

						//this.typeFprmularPostitions.add(new Rectangle(oldPosX, posY, posX-oldPosX, spaceToNexEntry));
						this.typeFormularPositions.add ( new Rectangle ( oldPosX, startY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, startWidht, startHeight ) );
						debug ( "Das rectangle für den Quatsch: " + new Rectangle ( oldPosX, posY, startWidht, startHeight ) );
						debug ( "Anzahl der Einträge in der List: " + typeFormularPositions.size ( ) );
						//this.typeFprmularPostitions.add(new Rectangle(startX, startY, startWidht, startHeight));
						this.typeEquations.add ( i );

						spaceToNexEntry = typeSize.height;
					} else {
						//typeRenderer.render(posX, posY-(typeSize.height / 2) - fontAscent / 2, typeSize.width ,typeSize.height, gc, bondType, toListenForM);
						typeRenderer.renderBase ( posX, posY, typeSize.width, typeSize.height, gc, bondType, toListenForM );
						startWidht += typeSize.width;
						startHeight = Math.max ( startHeight, typeSize.height );
						typePositions.set ( i, new Rectangle ( posX, posY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, typeSize.width, typeSize.height ) );

						posX += typeSize.width;

						//this.typeFprmularPostitions.add(new Rectangle(startX, startY, startWidht, startHeight));
						this.typeFormularPositions.add ( new Rectangle ( oldPosX, startY - AbstractRenderer.fontHeight
								+ AbstractRenderer.fontDescent, startWidht, startHeight ) );
						//debug("Das rectangle für den Quatsch: "+new Rectangle(oldPosX, posY, startWidht, startHeight));
						//debug("Anzahl der Einträge in der List: "+typeFormularPositions.size());
						this.typeEquations.add ( i );
					}

					//everey line but the last needs a line braek
					if ( i < ( typeFormulaList.size ( ) - 1 ) ) {
						posX = x + einrücken;
						posY += spaceToNexEntry;
						posY += SPACEBETWEENELEMENTS;
					}
				}
			}

			gc.setColor ( expColor );
			gc.setFont ( AbstractRenderer.expFont );
			gc.drawString ( "}", posX, posY );
			posX += AbstractRenderer.expFontMetrics.stringWidth ( "}" );

			if ( markedArea != null ) {
				gc.drawRect ( markedArea.x, markedArea.y, markedArea.width, markedArea.height );
			}
		}
	}

	/**
	 * @param vals
	 * @return the max of all given vals;
	 */
	private static int realMax ( int... vals ) {
		int result = vals[0];

		//element 0 is allready the result, start with 1
		for ( int i = 1; i < vals.length; i++ ) {
			result = Math.max ( result, vals[i] );
		}
		return result;
	}

	/**
	 * @return the aPositions
	 */
	public ArrayList < Rectangle > getAPositions ( ) {
		return this.aPositions;
	}

	private static void debug ( String s ) {
	//Debug.out.println(s, Debug.MICHAEL);
	}

	private static void testAusgabe ( String s ) {
	//System.out.println(s);
	}

	private static void testAusgabe ( ) {
	//System.out.println();
	}

	private static void testAusgabeL ( String s ) {
	//System.out.print(s);
	}

}
