package de.unisiegen.tpml.graphics.renderer;


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
 * Subclass of the {@link AbstractRenderer} providing the rendering the
 * TypeFormulars
 * 
 * @author michael
 */
public class TypeFormularRenderer extends AbstractRenderer
{

  /**
   * the space between two elements of the typeformulalist
   */
  private static final int SPACEBETWEENELEMENTS = AbstractRenderer
      .getAbsoluteHeight () / 3;


  /**
   * the Renderer for the environments
   */
  private EnvironmentRenderer environmentRenderer;


  /**
   * the List of Strings for the tooltip
   */
  private ArrayList < String > collapsedTypeEnvironmentsStrings;


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
   * provides a rectangle arround the entry which should be swapped
   */
  private Rectangle markedArea;


  /**
   * the List of areas for the tooltip
   */
  private ArrayList < Rectangle > collapsedTypeEnvironmentAreas;


  /**
   * The TypeFOrmulars that should be rendered.
   */
  private ArrayList < TypeFormula > typeFormulaList;


  /**
   * the ListenForMouseContainer
   */
  private ToListenForMouseContainer toListenForM;


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


  // their is no nice nail avialble at the unicode (unbelivable) so the nail is
  // rendern manualy
  // private static final String As = "A |- ";
  // private static final String As = "A \u0485 ";
  // private static final String As = "A \u093E ";
  // private static final String As = "A \u0B94 ";

  /**
   * constructor
   */
  public TypeFormularRenderer ()
  {

    this.collapsedTypeEnvironmentAreas = new ArrayList < Rectangle > ();
    this.typeFormularPositions = new ArrayList < Rectangle > ();
    this.expressionPostitions = new ArrayList < Rectangle > ();
    this.typePositions = new ArrayList < Rectangle > ();
    this.leftTypePositions = new ArrayList < Rectangle > ();
    this.rightTypePositions = new ArrayList < Rectangle > ();
    this.aPositions = new ArrayList < Rectangle > ();
    this.typeEquations = new ArrayList < Integer > ();
    this.collapsedTypeEnvironmentsStrings = new ArrayList < String > ();
    this.aStrings = new ArrayList < String > ();
    this.aPrettyStrings = new ArrayList < ArrayList < PrettyString >> ();
  }


  /**
   * Sets the TypeFormulars.
   * 
   * @param typeFormulaListP
   */
  public void setTypeFormulaList ( ArrayList < TypeFormula > typeFormulaListP )
  {
    this.typeFormulaList = typeFormulaListP;
  }


  /**
   * Sets the toListenForMouseContrainer.
   * 
   * @param tlfmcP
   */
  public void setToListenForMoudeContainer ( ToListenForMouseContainer tlfmcP )
  {
    this.toListenForM = tlfmcP;
  }


  /**
   * returns a list of areas where the tooltips should be come the 1. element
   * coresponds to the 1. element int the getCollapStrings
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getCollapsedTypeEnvironmentAreas ()
  {
    return this.collapsedTypeEnvironmentAreas;
  }


  /**
   * get the Strings for tooltips. the 1. string corresponds to the first
   * element of the list of rectangles
   * 
   * @return the collapsedStrings
   */
  public ArrayList < String > getCollapsedTypeEnvironmentsStrings ()
  {
    return this.collapsedTypeEnvironmentsStrings;
  }


  /**
   * get the Strings for tooltips. the 1. string corresponds to the first
   * element of the list of rectangles
   * 
   * @return the collapsedStrings
   */
  public ArrayList < String > getAStrings ()
  {
    return this.aStrings;
  }


  /**
   * get the Strings for tooltips. the 1. string corresponds to the first
   * element of the list of rectangles
   * 
   * @return the collapsedStrings
   */
  public ArrayList < ArrayList < PrettyString >> getAPrettyStrings ()
  {
    return this.aPrettyStrings;
  }


  /**
   * returns a list of areas where typeFormulars are
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getTypeFormularPositions ()
  {
    return this.typeFormularPositions;
  }


  /**
   * returns a list of areas where typeFormulars are
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getExpressionPositions ()
  {
    return this.expressionPostitions;
  }


  /**
   * returns a list of areas where typeFormulars are
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getTypePositions ()
  {
    return this.typePositions;
  }


  /**
   * returns a list of areas where typeFormulars are
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getLeftTypePositions ()
  {
    return this.leftTypePositions;
  }


  /**
   * returns a list of areas where typeFormulars are
   * 
   * @return the list of rectangles with areas
   */
  public ArrayList < Rectangle > getRightTypePositions ()
  {
    return this.rightTypePositions;
  }


  /**
   * Implementation of the dragg'n'dropp. The second elemnt will be inserted
   * into the list at the position of the first one and deleted from its old
   * position. Everey index of the following elements will increase by 1
   * 
   * @param x index of the first element
   * @param y index of the second element
   */
  public void draggNDropp ( int x, int y )
  {
    // remember the second element
    TypeFormula secondElement = this.typeFormulaList.get ( y );
    // remove it
    this.typeFormulaList.remove ( y );
    // Inserts it at the position of the first element in the list. Shifts the
    // element
    // currently at that position and any subsequent elements. (adds one to
    // their indices).
    this.typeFormulaList.add ( x, secondElement );
    reorganizeAreas ();
  }


  /**
   * resets the saved areas. They must be rebuild to garante assure the areas
   * for the tooltiptexts are correct.
   */
  private void reorganizeAreas ()
  {
    this.collapsedTypeEnvironmentAreas = new ArrayList < Rectangle > ();
    this.collapsedTypeEnvironmentsStrings = new ArrayList < String > ();
    this.aPositions = new ArrayList < Rectangle > ();
    this.aStrings = new ArrayList < String > ();
    this.typePositions = new ArrayList < Rectangle > ();
    this.typeFormularPositions = new ArrayList < Rectangle > ();
    this.leftTypePositions = new ArrayList < Rectangle > ();
    this.rightTypePositions = new ArrayList < Rectangle > ();
  }


  /**
   * Calculates the size, that is needed to propperly render the TypeFormula.
   * 
   * @param maxWidth
   * @return The size needed to render the environment.
   */
  @SuppressWarnings ( "unchecked" )
  public Dimension getNeededSize ( int maxWidth )
  {

    // fix a problem of the gui that the components are very small (-x)
    if ( maxWidth < 0 )
    {
      return new Dimension ( 0, 0 );
    }
    Dimension result = new Dimension ( 0, 0 );

    {
      // first we need the space for the solve
      int insertSpace = AbstractRenderer.keywordFontMetrics
          .stringWidth ( "solve " );
      insertSpace += AbstractRenderer.expFontMetrics.stringWidth ( "{" );

      // the prettyStingRender is needed here to find out the needed space
      PrettyStringRenderer prettyStringrenderer;

      // Wir brauchen 4 Breiten, davon das Maximum. Jeden Durchlauf der
      // Forschleife werden diese Werte berechent,
      // und letztlich brauchen wir dann das Maximum dieser maxima. daher merken
      // wir uns dieses Maximum

      // we need 4 widths, but for the result we need the max of these 4 widths.
      // With everey pass of the
      // for iteration these values will be calculated. The result is the max of
      // these maxes.
      int lineWidthMaxAll = insertSpace;

      for ( int i = 0 ; i < this.typeFormulaList.size () ; i++ )
      {
        // here are the 4 widths. Maybe evereything fits in one line.
        int lineWidthTypeFormula = insertSpace;
        int lineWidthEnvironment = insertSpace;
        int lineWidthExpression = insertSpace;
        int lineWidthType = insertSpace;

        int lineHeightTypeFormula = 0;
        int lineHeightEnvironment = 0;
        int lineHeightExpression = 0;
        int lineHeightType = 0;

        // the rest of the width of the line is stored to decide to break or not
        // to break
        int restOfWidth = maxWidth - insertSpace;

        TypeFormula t = this.typeFormulaList.get ( i );

        if ( t instanceof TypeJudgement )
        {
          // get the envirement, expression and type
          TypeEnvironment environment = t.getEnvironment ();
          Expression expression = t.getExpression ();
          Type type = t.getType ();

          this.environmentRenderer = new EnvironmentRenderer < Enumeration, Enumeration > ();
          this.environmentRenderer.setEnvironment ( environment );
          Dimension environmentDim = this.environmentRenderer.getNeededSize ();

          // width of envireonment
          lineWidthEnvironment += environmentDim.width;
          lineWidthEnvironment += AbstractRenderer.expFontMetrics
              .stringWidth ( arrowString );
          restOfWidth -= AbstractRenderer.expFontMetrics
              .stringWidth ( environment.toString () );
          restOfWidth -= AbstractRenderer.expFontMetrics
              .stringWidth ( arrowString );

          // height of the environment
          lineHeightEnvironment = environmentDim.height;

          // width of expression
          prettyStringrenderer = new PrettyStringRenderer ();
          prettyStringrenderer.setPrettyString ( expression.toPrettyString () );
          Dimension expressionSize = prettyStringrenderer
              .getNeededSizeAll_ ( maxWidth - insertSpace );
          expressionSize.width += AbstractRenderer.keywordFontMetrics
              .stringWidth ( doubleColon );
          // if there is not enough space for the expression
          if ( restOfWidth < expressionSize.width )
          {
            // new line, new restOfWidth
            restOfWidth = maxWidth - insertSpace;
            expressionSize = prettyStringrenderer
                .getNeededSizeAll_ ( restOfWidth );
            lineWidthExpression = insertSpace + expressionSize.width;
            lineWidthExpression += AbstractRenderer.keywordFontMetrics
                .stringWidth ( doubleColon );
            restOfWidth -= expressionSize.width;
            restOfWidth -= AbstractRenderer.keywordFontMetrics
                .stringWidth ( doubleColon );
            lineHeightExpression = expressionSize.height;
          }
          else
          // the expression fits in the same line like the environment
          {
            // if it is not warped to the next line the width of the environment
            // will increse...
            lineWidthEnvironment += expressionSize.width;
            lineWidthEnvironment += AbstractRenderer.keywordFontMetrics
                .stringWidth ( doubleColon );
            restOfWidth -= lineWidthEnvironment;

            // the hight of the Environment could be lager
            lineHeightEnvironment = Math.max ( lineHeightEnvironment,
                expressionSize.height );
          }
          // if there is not enought space after the ::
          prettyStringrenderer.setPrettyString ( type.toPrettyString () );
          Dimension typeSize = prettyStringrenderer.getNeededSize ( maxWidth
              - insertSpace );
          if ( restOfWidth < typeSize.width )
          {
            restOfWidth = maxWidth - insertSpace;
            typeSize = prettyStringrenderer.getNeededSize ( restOfWidth );
            lineWidthType = insertSpace + typeSize.width;
            restOfWidth -= lineWidthType;
            lineHeightType = typeSize.height;
          }
          else
          {
            // if it has never warped the widthof the expression is unchanges
            // and the width will increase
            if ( lineWidthExpression == insertSpace )
            {
              lineWidthEnvironment += typeSize.width;
              lineHeightEnvironment = Math.max ( lineHeightEnvironment,
                  typeSize.height );
            }
            else
            {
              lineWidthExpression += typeSize.width;
              lineHeightExpression = Math.max ( lineHeightExpression,
                  typeSize.height );
            }
          }
        }
        else if ( t instanceof TypeEquationTypeInference )
        {
          prettyStringrenderer = new PrettyStringRenderer ();
          prettyStringrenderer
              .setPrettyString ( ( ( TypeEquationTypeInference ) t )
                  .toPrettyString () );

          lineWidthEnvironment += prettyStringrenderer
              .getNeededSize ( Integer.MAX_VALUE ).width;
          // The width for the A
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( As );
          // The width for the nail + the space arround it (The width of the
          // nail will be as big as the width
          // of the String "--"
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( "--" );
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
          lineHeightTypeFormula += prettyStringrenderer
              .getNeededSize ( Integer.MAX_VALUE ).height;
        }
        else if ( t instanceof TypeSubType )
        {
          prettyStringrenderer = new PrettyStringRenderer ();
          prettyStringrenderer.setPrettyString ( ( ( TypeSubType ) t )
              .toPrettyString () );

          lineWidthEnvironment += prettyStringrenderer
              .getNeededSize ( Integer.MAX_VALUE ).width;
          // The width for the A
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( As );
          // The width for the nail + the space arround it (The width of the
          // nail will be as big as the width
          // of the String "--"
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( "--" );
          lineWidthEnvironment += keywordFontMetrics.stringWidth ( " " );
          lineHeightTypeFormula += prettyStringrenderer
              .getNeededSize ( Integer.MAX_VALUE ).height;
        }

        result.height += lineHeightEnvironment + lineHeightExpression
            + lineHeightType + lineHeightTypeFormula;

        int lineWidthMax = realMax ( lineWidthEnvironment, lineWidthExpression,
            lineWidthType, lineWidthTypeFormula );

        lineWidthMaxAll = Math.max ( lineWidthMax, lineWidthMaxAll );

        result.width = lineWidthMaxAll;
      }
    }

    // we also need the space between the Elements
    result.height += ( ( this.typeFormulaList.size () - 1 ) * SPACEBETWEENELEMENTS );

    return result;
  }


  /**
   * Renders the Typeformulas.<br>
   * <br>
   * The Typeformulas are rendered line by lien. They will appear starting <i>y</i>
   * and ending <i>(y + height></i>.
   * 
   * @param x The left position where the environment should be displayed
   * @param y The top position where the environment should be displayed.
   * @param width The width the renderer is given to render the environment.
   * @param height The Height the renderer is given to render the envionment.
   * @param gc
   */
  @SuppressWarnings ( "unchecked" )
  public void renderer ( int x, int y, int width, int height, Graphics gc )
  {
    // TODO for debugging
    // gc.setColor(this.alternativeColor != null ? this.alternativeColor :
    // Color.BLACK);
    // gc.drawRect(x, y, width, height - 1);

    int remainingSpace = width;

    // calculate the vertical position to start
    int posX = x;
    int posY = y + AbstractRenderer.fontAscent;

    if ( this.typeFormulaList.size () > 0 )
    {
      // get the first element
      TypeFormula t = this.typeFormulaList.get ( 0 );

      // save the space needed to indent (einreucken)
      int indentSpace = posX;
      int spaceToNexEntry = 0;

      gc.setFont ( AbstractRenderer.keywordFont );
      gc.setColor ( this.alternativeColor != null ? this.alternativeColor
          : AbstractRenderer.keywordColor ); // if then else

      gc.drawString ( "solve", posX, posY );
      posX += AbstractRenderer.keywordFontMetrics.stringWidth ( "solve " );

      gc.setFont ( AbstractRenderer.expFont );
      gc.setColor ( this.alternativeColor != null ? this.alternativeColor
          : AbstractRenderer.expColor ); // if then else
      gc.drawString ( "{", posX, posY );
      posX += AbstractRenderer.expFontMetrics.stringWidth ( "{" );

      spaceToNexEntry = Math.max ( ( keywordFontMetrics.getHeight () ),
          expFontMetrics.getHeight () );

      // calculate the indentSpace
      indentSpace = posX - indentSpace;

      this.typeFormularPositions = new ArrayList < Rectangle > ();
      this.typeEquations = new ArrayList < Integer > ();
      for ( int i = 0 ; i < this.typeFormulaList.size () ; i++ )
      {
        this.expressionPostitions.add ( new Rectangle ( 0, 0, 0, 0 ) );
        this.typePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
        this.leftTypePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
        this.rightTypePositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
        this.aPositions.add ( new Rectangle ( 0, 0, 0, 0 ) );
        this.aStrings.add ( "" );
        this.aPrettyStrings.add ( new ArrayList < PrettyString > () );
      }

      for ( int i = 0 ; i < this.typeFormulaList.size () ; i++ )
      {
        remainingSpace = width - indentSpace;

        t = this.typeFormulaList.get ( i );
        if ( t instanceof TypeEquationTypeInference )
        {
          // save the posX where the render starts to render to find out the
          // size later
          int oldX = posX;

          // the renderer for the typeEquations
          PrettyStringRenderer typeEquationStringrenderer = new PrettyStringRenderer ();

          TypeEquationTypeInference s = ( TypeEquationTypeInference ) t;

          // get the highlight informations
          ShowBonds bondTypeEquation = new ShowBonds ();
          bondTypeEquation.load ( s );
          bondTypeEquation.getAnnotations ();

          // Render the A with the nail. It will be renderd in expColor and
          // keyword Font
          gc.setColor ( AbstractRenderer.expColor );
          gc.setFont ( AbstractRenderer.keywordFont );
          gc.drawString ( As, posX, posY );
          this.aPositions.set ( i, new Rectangle ( posX, posY
              - AbstractRenderer.getAbsoluteHeight (),
              AbstractRenderer.expFontMetrics.stringWidth ( As ),
              AbstractRenderer.getAbsoluteHeight () ) );
          // render the A
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( As );
          // render the nail and the space arround the nail
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

          // The nail as Polygon
          Polygon polygon = new Polygon ();
          polygon.addPoint ( posX,
              ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
                  + AbstractRenderer.fontHeight / 5 );
          polygon.addPoint ( posX,
              ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
                  - AbstractRenderer.fontHeight / 5 );
          polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2
              + fontDescent );
          polygon.addPoint ( posX
              + AbstractRenderer.keywordFontMetrics.stringWidth ( "--" ), posY
              - AbstractRenderer.fontHeight / 2 + fontDescent );
          polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2
              + fontDescent );

          // render it
          gc.drawPolygon ( polygon );

          posX += polygon.getBounds ().width;
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

          // get the seentyps and provide the String for the Tooltip
          SeenTypes < TypeEquationTypeInference > typeEquSeenTypes = ( ( TypeEquationTypeInference ) t )
              .getSeenTypes ();
          this.aStrings.set ( i, typeEquSeenTypes.toString () );

          ArrayList < PrettyString > tmp = new ArrayList < PrettyString > ();

          for ( int z = 0 ; z < typeEquSeenTypes.size () ; z++ )
          {
            TypeEquationTypeInference tqti = typeEquSeenTypes.get ( z );
            PrettyString ps = tqti.toPrettyString ();

            tmp.add ( ps );
          }
          this.aPrettyStrings.set ( i, tmp );

          // tell the Rendere the PrettyString
          typeEquationStringrenderer.setPrettyString ( s.toPrettyString () );
          // TODO prevent the renderer from linewraping. printig?
          // Dimension typeEquationSize =
          // typeEquationStringrenderer.getNeededSize(width - (posX - oldX));
          Dimension typeEquationSize = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );

          // render the typeFormula
          typeEquationStringrenderer.render ( posX, posY
              - AbstractRenderer.fontAscent, typeEquationSize.width,
              typeEquationSize.height, gc, bondTypeEquation, this.toListenForM );

          // now we need to find out the exact position of the left type and the
          // left one
          MonoType left = s.getLeft ();
          MonoType right = s.getRight ();
          typeEquationStringrenderer.setPrettyString ( left.toPrettyString () );
          Dimension leftDim = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );
          typeEquationStringrenderer.setPrettyString ( right.toPrettyString () );
          Dimension rightDim = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );

          this.typeFormularPositions.add ( new Rectangle ( oldX, posY
              - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              width, typeEquationSize.height ) );
          this.leftTypePositions.set ( i, new Rectangle ( posX, posY
              - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              leftDim.width, leftDim.height ) );
          this.rightTypePositions.set ( i, new Rectangle (
              ( posX + typeEquationSize.width ) - rightDim.width, posY
                  - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              rightDim.width, rightDim.height ) );

          this.typeEquations.add ( new Integer ( i ) );
          posX += typeEquationSize.width;

          gc.setColor ( expColor );

          // every but the last line needs an linebreak
          if ( i < ( this.typeFormulaList.size () - 1 ) )
          {
            posX = x + indentSpace;
            posY += typeEquationSize.height;
            posY += SPACEBETWEENELEMENTS;
          }
        }
        else if ( t instanceof TypeSubType )
        {
          // save the pos X where the render starts to render to find out the
          // size later
          int oldX = posX;

          // the renderer for the TypeEquations
          PrettyStringRenderer typeEquationStringrenderer = new PrettyStringRenderer ();
          TypeSubType subType = ( TypeSubType ) t;

          // get the highlightinfos
          ShowBonds bondTypeEquation = new ShowBonds ();
          bondTypeEquation.load ( subType );
          bondTypeEquation.getAnnotations ();

          // Render the A with the nail. It will be renderd in expColor and
          // keyword Font
          gc.setColor ( AbstractRenderer.expColor );
          gc.setFont ( AbstractRenderer.keywordFont );
          gc.drawString ( As, posX, posY );
          this.aPositions.set ( i, new Rectangle ( posX, posY
              - AbstractRenderer.getAbsoluteHeight (),
              AbstractRenderer.expFontMetrics.stringWidth ( As ),
              AbstractRenderer.getAbsoluteHeight () ) );

          // render the A
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( As );

          // render the nail and the space arround the nail
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

          // the nail as Polygon
          Polygon polygon = new Polygon ();
          polygon.addPoint ( posX,
              ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
                  + AbstractRenderer.fontHeight / 5 );
          polygon.addPoint ( posX,
              ( posY - AbstractRenderer.fontHeight / 2 + fontDescent )
                  - AbstractRenderer.fontHeight / 5 );
          polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2
              + fontDescent );
          polygon.addPoint ( posX
              + AbstractRenderer.keywordFontMetrics.stringWidth ( "--" ), posY
              - AbstractRenderer.fontHeight / 2 + fontDescent );
          polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2
              + fontDescent );

          // render it
          gc.drawPolygon ( polygon );

          posX += polygon.getBounds ().width;
          posX += AbstractRenderer.keywordFontMetrics.stringWidth ( " " );

          // tell the Rendere the PrettyString
          typeEquationStringrenderer.setPrettyString ( subType
              .toPrettyString () );
          // TODO prevent the renderer from linewraping. printig?
          // Dimension typeEquationSize =
          // typeEquationStringrenderer.getNeededSize(width - (posX - oldX));
          Dimension typeEquationSize = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );

          // render the typeFormula
          typeEquationStringrenderer.render ( posX, posY
              - AbstractRenderer.fontAscent, typeEquationSize.width,
              typeEquationSize.height, gc, bondTypeEquation, this.toListenForM );

          // now we need to find out the exact position of the left type and the
          // left one
          MonoType left = subType.getType ();
          MonoType right = subType.getType2 ();
          typeEquationStringrenderer.setPrettyString ( left.toPrettyString () );
          Dimension leftDim = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );
          typeEquationStringrenderer.setPrettyString ( right.toPrettyString () );
          Dimension rightDim = typeEquationStringrenderer
              .getNeededSize ( Integer.MAX_VALUE );

          this.typeFormularPositions.add ( new Rectangle ( oldX, posY
              - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              width, typeEquationSize.height ) );
          this.leftTypePositions.set ( i, new Rectangle ( posX, posY
              - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              leftDim.width, leftDim.height ) );
          this.rightTypePositions.set ( i, new Rectangle (
              ( posX + typeEquationSize.width ) - rightDim.width, posY
                  - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
              rightDim.width, rightDim.height ) );

          this.typeEquations.add ( new Integer ( i ) );
          posX += typeEquationSize.width;

          gc.setColor ( expColor );

          // every but the last line needs an linebreak
          if ( i < ( this.typeFormulaList.size () - 1 ) )
          {
            posX = x + indentSpace;
            posY += typeEquationSize.height;
            posY += SPACEBETWEENELEMENTS;
          }
        }
        else if ( t instanceof TypeJudgement )
        {

          // save the position and the width and height of the element
          int startY = posY;
          int startWidht = 0;
          int startHeight = 0;

          this.environmentRenderer = new EnvironmentRenderer < Enumeration, Enumeration > ();
          PrettyStringRenderer expressionRenderer = new PrettyStringRenderer ();
          PrettyStringRenderer typeRenderer = new PrettyStringRenderer ();

          int oldPosX = posX;
          TypeEnvironment environment = t.getEnvironment ();
          Expression expression = t.getExpression ();
          Type type = t.getType ();

          environment.symbols ();
          environment.identifiers ();

          this.environmentRenderer.setEnvironment ( environment );

          gc.setColor ( AbstractRenderer.expColor );

          Dimension environmentSize = this.environmentRenderer.getNeededSize ();
          this.environmentRenderer.renderer ( posX, posY
              - AbstractRenderer.fontAscent, this.environmentRenderer
              .getNeededSize ().width, this.environmentRenderer
              .getNeededSize ().height, gc );

          startWidht += this.environmentRenderer.getNeededSize ().width;
          startWidht += AbstractRenderer.expFontMetrics
              .stringWidth ( arrowString );

          posX += this.environmentRenderer.getNeededSize ().width;
          startWidht += this.environmentRenderer.getNeededSize ().width;
          remainingSpace -= this.environmentRenderer.getNeededSize ().width;

          spaceToNexEntry = Math.max ( 0, this.environmentRenderer
              .getNeededSize ().height );

          String envCollapsedString = this.environmentRenderer
              .getCollapsedString ();
          if ( envCollapsedString != null )
          {
            this.collapsedTypeEnvironmentAreas.add ( this.environmentRenderer
                .getCollapsedArea () );
            this.collapsedTypeEnvironmentsStrings
                .add ( this.environmentRenderer.getCollapsedString () );
          }

          gc.setColor ( AbstractRenderer.expColor );
          gc.drawString ( arrowString, posX, posY );
          posX += AbstractRenderer.expFontMetrics.stringWidth ( arrowString );
          startWidht += AbstractRenderer.expFontMetrics
              .stringWidth ( arrowString );

          remainingSpace -= AbstractRenderer.expFontMetrics
              .stringWidth ( arrowString );

          ShowBonds bound = new ShowBonds ();
          bound.load ( expression );

          expressionRenderer.setPrettyString ( expression.toPrettyString () );
          Dimension expressionSize = expressionRenderer
              .getNeededSizeAll_ ( width - indentSpace );

          // if the expression dose not fit in the line
          if ( remainingSpace < expressionSize.width
              + AbstractRenderer.expFontMetrics.stringWidth ( doubleColon ) )
          {
            posX = indentSpace;
            remainingSpace = width - indentSpace;

            expressionSize = expressionRenderer
                .getNeededSizeAll_ ( remainingSpace );

            spaceToNexEntry = Math
                .max ( spaceToNexEntry, expressionSize.height );
            posY += environmentSize.height;
            startHeight += environmentSize.height;

            expressionRenderer.render ( posX, posY
                - AbstractRenderer.fontAscent, expressionSize.width,
                expressionSize.height, gc, bound, this.toListenForM );
            startWidht = Math.max ( startWidht, expressionSize.width );
            startHeight += expressionSize.height;
            this.expressionPostitions.set ( i, new Rectangle ( posX, posY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                expressionSize.width, expressionSize.height ) );
          }
          else
          // the expressions fits in the same line as the environment
          {
            expressionRenderer.render ( posX, posY
                - AbstractRenderer.fontAscent, expressionSize.width,
                expressionSize.height, gc, bound, this.toListenForM );
            startWidht += expressionSize.width;
            startHeight = Math.max ( startHeight, expressionSize.height );
            this.expressionPostitions.set ( i, new Rectangle ( posX, posY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                expressionSize.width, expressionSize.height ) );
            spaceToNexEntry = Math
                .max ( spaceToNexEntry, expressionSize.height );
          }
          posX += expressionSize.width;

          startWidht += expressionSize.width;
          remainingSpace -= expressionSize.width;

          gc.setColor ( AbstractRenderer.expColor );
          gc.setFont ( expFont );
          gc.drawString ( doubleColon, posX, posY );
          posX += AbstractRenderer.expFontMetrics.stringWidth ( doubleColon );
          startWidht += AbstractRenderer.expFontMetrics
              .stringWidth ( doubleColon );
          remainingSpace -= AbstractRenderer.expFontMetrics
              .stringWidth ( doubleColon );

          typeRenderer.setPrettyString ( type.toPrettyString () );
          ShowBonds bondType = new ShowBonds ();
          bondType.load ( type );

          Dimension typeSize = typeRenderer
              .getNeededSize ( width - indentSpace );

          if ( remainingSpace < typeSize.width )
          {
            posX = indentSpace;
            remainingSpace = width - indentSpace;

            posY += expressionSize.height;

            typeRenderer.render ( posX, posY - fontAscent, typeSize.width,
                typeSize.height, gc, bondType, this.toListenForM );

            startWidht = Math.max ( startWidht, typeSize.width );
            startHeight += typeSize.height;
            this.typePositions.set ( i, new Rectangle ( posX, posY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                typeSize.width, typeSize.height ) );
            posX += typeSize.width;

            this.typeFormularPositions.add ( new Rectangle ( oldPosX, startY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                startWidht, startHeight ) );

            this.typeEquations.add ( new Integer ( i ) );

            spaceToNexEntry = typeSize.height;
          }
          else
          {
            typeRenderer.render ( posX, posY - AbstractRenderer.fontAscent,
                typeSize.width, typeSize.height, gc, bondType,
                this.toListenForM );

            startWidht += typeSize.width;
            startHeight = Math.max ( startHeight, typeSize.height );
            this.typePositions.set ( i, new Rectangle ( posX, posY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                typeSize.width, typeSize.height ) );

            posX += typeSize.width;

            this.typeFormularPositions.add ( new Rectangle ( oldPosX, startY
                - AbstractRenderer.fontHeight + AbstractRenderer.fontDescent,
                startWidht, startHeight ) );

            this.typeEquations.add ( new Integer ( i ) );
          }

          // everey line but the last needs a line braek
          if ( i < ( this.typeFormulaList.size () - 1 ) )
          {
            posX = x + indentSpace;
            posY += spaceToNexEntry;
            posY += SPACEBETWEENELEMENTS;
          }
        }
      }

      gc.setColor ( expColor );
      gc.setFont ( AbstractRenderer.expFont );
      gc.drawString ( "}", posX, posY );
      posX += AbstractRenderer.expFontMetrics.stringWidth ( "}" );

      if ( this.markedArea != null )
      {
        gc.drawRect ( this.markedArea.x, this.markedArea.y,
            this.markedArea.width, this.markedArea.height );
      }
    }
  }


  /**
   * @param vals
   * @return the max of all given vals;
   */
  private static int realMax ( int ... vals )
  {
    int result = vals [ 0 ];

    // element 0 is allready the result, start with 1
    for ( int i = 1 ; i < vals.length ; i++ )
    {
      result = Math.max ( result, vals [ i ] );
    }
    return result;
  }


  /**
   * @return the aPositions
   */
  public ArrayList < Rectangle > getAPositions ()
  {
    return this.aPositions;
  }

}
