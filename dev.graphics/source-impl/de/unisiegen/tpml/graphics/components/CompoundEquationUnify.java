package de.unisiegen.tpml.graphics.components;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;


/**
 * this class renders the coumpoundexpression of the Unify
 * 
 * @version $Id$
 */
public class CompoundEquationUnify extends JComponent
{

  /**
   * TODO
   */
  private static final long serialVersionUID = -8168542936162077008L;


  /**
   * Renderer that is used to render the expressions
   */
  private PrettyStringRenderer renderer;


  /**
   * The size of the Typformulars.
   */
  private Dimension equationSize;


  /**
   * The size of the Subistitutions.
   */
  private Dimension substitutionSize;


  /**
   * saves the position where the mouse starts the dragging
   */
  // private int mousePositionPressedX ;
  /**
   * saves the position where the mouse starts the dragging
   */
  // private int mousePositionPressedY ;
  private boolean dragndropeabled = true;


  /**
   * the element where the mouse starts dragging
   */
  private int rectPressed = -1;


  /**
   * the element where the mouse ends dragging
   */
  private int rectReleased = -1;


  /**
   * The current list of DefaultTypeSubsititutions that are rendered.
   */
  // private ArrayList < TypeSubstitution > defaultTypeSubstitutionList;
  private TypeSubstitutionList defaultTypeSubstitutionList;


  /**
   * the current Typformulars
   */
  // private ArrayList < TypeEquation > typeEquationList;
  private TypeEquationList typeEquationList;


  /**
   * The width of the braces around the expression and the environment.
   */
  private int spaceInFrontOf;


  /**
   * Whether the expression should be wrapped if there is not enough space to
   * render it in on line.<br>
   * Actualy this is only used with the result of the BigStepGUI.
   */
  private boolean noLineWrapping;


  /**
   * If this color is given all colors of the {@link AbstractRenderer} are
   * ignored and only this color is used.
   */
  private Color alternativeColor;


  /**
   * The arrow symbol that is used between the environment and the expression
   * when used within the
   * {@link de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent}
   */
  // private static String arrowStr = " \u22b3 ";
  /**
   * Initialises the CompoundExpression with the default values.<br>
   * <br>
   * The braces have a size of 10 pixes, no underlining and the color of the
   * {@link AbstractRenderer} are ignored.
   */
  private ShowBonds bonds;


  /**
   * the list of points where the mouseovereffect will be react
   */
  private ToListenForMouseContainer toListenForMouse;


  /**
   * true if the user is dragging a typformula
   */
  protected boolean dragged = false;


  /**
   * the string the renderer will show beside the mousepointer
   */
  private String draggedString = ""; //$NON-NLS-1$


  /**
   * the position beside the mousepointer where the renderer will render the
   * draggedString
   */
  // private int draggedX = 0;
  /**
   * the position beside the mousepointer where the renderer will render the
   * draggedString
   */
  // private int draggedY = 0;
  /**
   * the position beside the mousepointer where the renderer will render the
   * draggedString
   */
  private Point draggedBesideMousePointer;


  /**
   * the constructor
   */
  public CompoundEquationUnify ()
  {
    super ();
    this.bonds = new ShowBonds ();
    this.substitutionSize = new Dimension ( 0, 0 );
    this.equationSize = new Dimension ( 0, 0 );
    this.toListenForMouse = new ToListenForMouseContainer ();
    this.alternativeColor = null;
    this.spaceInFrontOf = 10;
    this.draggedBesideMousePointer = new Point ( 0, 0 );
    // this.underlineExpression = null ;
    CompoundEquationUnify.this.setDoubleBuffered ( true );
    // the MouseMotionListner only implements the text shown next to the
    // mouspointer while dragging
    // and changing the mousePointer
    addMouseMotionListener ( new MouseMotionAdapter ()
    {

      @Override
      public void mouseMoved ( MouseEvent event )
      {
        // Resets the Cursor to the default Cursor after
        // changing the Cursor to the Dragging-Cursor
        Cursor c = new Cursor ( Cursor.DEFAULT_CURSOR );
        getParent ().getParent ().setCursor ( c );
        handleMouseMoved ( event );
      }


      @Override
      public void mouseDragged ( MouseEvent event )
      {
        if ( CompoundEquationUnify.this.isDragndropeabled () )
        {
          // if the User started dragging on an Typformula
          if ( CompoundEquationUnify.this.dragged )
          {
            // be shure the Component repaints the Component to render the
            // draggedString
            repaint ();
            // change the Cursor to the Hand
            Cursor c = new Cursor ( Cursor.HAND_CURSOR );
            getParent ().getParent ().setCursor ( c );
            // tell the renderer where the draggedString will be renderd
            // CompoundExpressionTypeInference.this.draggedX = event.getX() + 5;
            // CompoundExpressionTypeInference.this.draggedY = event.getY() + 5;
            CompoundEquationUnify.this.setDraggedBesideMousePointer ( event
                .getPoint () );
            if ( event.getX () >= getWidth () - 50 )
            {
              // CompoundExpressionTypeInference.this.draggedX = event.getX() -
              // (50 - (getWidth() - event.getX()));
              CompoundEquationUnify.this.getDraggedBesideMousePointer ().x = event
                  .getX ()
                  - ( 50 - ( getWidth () - event.getX () ) );
            }
            if ( event.getY () <= 10 )
            {
              // CompoundExpressionTypeInference.this.draggedY = event.getY() +
              // 15;
              CompoundEquationUnify.this.getDraggedBesideMousePointer ().y = event
                  .getY () + 15;
            }
          }
        }

      }
    } );

    addMouseListener ( new MouseAdapter ()
    {

      @Override
      public final void mouseClicked ( MouseEvent pMouseEvent )
      {
        handelMouseClicked ( pMouseEvent );
      }


      @Override
      public void mouseExited ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        // tell the toListenForMouse that the mose is gone
        resetMouseContainer ();
      }


      @Override
      public void mousePressed ( MouseEvent event )
      {
        handelMousePressed ( event );
      }


      @Override
      public void mouseReleased ( MouseEvent event )
      {
        handelMouseReleased ( event );
      }
    } );

  }


  /**
   * handels the MouseReleased event. If the mouse is released the Points there
   * the mouse was pressed and released are compared. If the adreas are
   * different and the the second component is isert at the first position, all
   * positions of all comonents after are increment by one.
   * 
   * @param event the MouseEvent
   */
  protected void handelMouseReleased ( MouseEvent event )
  {
    /*
     * // point whre the mouse was released Point mouseReleased = event.getPoint
     * (); // resette ArrayList < Rectangle > rects = this.typeFormularRenderer
     * .getTypeFormularPositions (); for ( int i = 0 ; i < rects.size () ; i++ ) {
     * if ( isIn ( rects.get ( i ), mouseReleased ) ) { // set the rectReleased
     * this.rectReleased = i; } } // if we have a dragg'n'drop if (
     * isDragndropeabled () && CompoundEquationUnify.this.dragged && (
     * this.rectPressed != this.rectReleased ) && ( this.rectPressed != -1 ) && (
     * this.rectReleased != -1 ) ) {
     * CompoundEquationUnify.this.typeFormularRenderer.draggNDropp (
     * this.rectReleased, this.rectPressed ); }
     * CompoundEquationUnify.this.dragged = false;
     */
    Point mouseReleased = event.getPoint ();
    repaint ();
  }


  /**
   * handels the mousePressed event. Remembers the point where the mouse was
   * pressed and sets the String that will be shown next to the mousepointer
   * while dragging.
   * 
   * @param event
   */
  protected void handelMousePressed ( MouseEvent event )
  {
    /*
     * if ( isDragndropeabled () ) { // remember the position. If the user
     * dragges thes point to another, // they will be switched Point
     * mousePressedPosition = event.getPoint (); // look up if there is a
     * typeformula at the point mousePosition. // if ther is no typformular,
     * ther will be no dragg'n'drop ArrayList < Rectangle > rects =
     * CompoundEquationUnify.this.typeFormularRenderer .getTypeFormularPositions
     * (); for ( int i = 0 ; i < rects.size () ; i++ ) { if ( isIn ( rects.get (
     * i ), mousePressedPosition ) ) { // set the rectPressed this.rectPressed =
     * i; // set the String shown next to the mouse while dragging
     * CompoundEquationUnify.this.draggedString =
     * CompoundEquationUnify.this.typeEquationList .get ( i ).toString (); // if
     * the String is to large shorter it if (
     * CompoundEquationUnify.this.draggedString.length () > 13 ) {
     * CompoundEquationUnify.this.draggedString =
     * CompoundEquationUnify.this.draggedString .substring ( 0, 10 ) + "...";
     * //$NON-NLS-1$ } CompoundEquationUnify.this.dragged = true; } } }
     */
  }


  /**
   * Handels the MosueEvents for the outline. The {@link CompoundEquationUnify}
   * provides the positions of the different Components.
   * 
   * @param pMouseEvent
   */
  protected void handelMouseClicked ( MouseEvent pMouseEvent )
  {
    /*
     * Point pos = pMouseEvent.getPoint (); ArrayList < Rectangle > leftType =
     * CompoundEquationUnify.this.typeFormularRenderer .getLeftTypePositions ();
     * ArrayList < Rectangle > rightType =
     * CompoundEquationUnify.this.typeFormularRenderer .getRightTypePositions
     * (); ArrayList < Rectangle > expressionPositions =
     * CompoundEquationUnify.this.typeFormularRenderer .getExpressionPositions
     * (); ArrayList < Rectangle > typePositions =
     * CompoundEquationUnify.this.typeFormularRenderer .getTypePositions ();
     * Outline outline = ( ( TypeInferenceView ) CompoundEquationUnify.this
     * .getParent ().getParent ().getParent ().getParent ().getParent ()
     * .getParent () ).getOutline (); for ( int i = 0 ; i <
     * CompoundEquationUnify.this.typeEquationList .size () ; i++ ) {
     * TypeFormula t = CompoundEquationUnify.this.typeEquationList .get ( i );
     * if ( t instanceof TypeJudgement ) { TypeJudgement typeJudgement = (
     * TypeJudgement ) t; // Expression if ( isIn ( expressionPositions.get ( i ),
     * pos ) ) { outline.load ( typeJudgement.getExpression (),
     * Outline.ExecuteMouseClick.TYPEINFERENCE ); } // Type else if ( isIn (
     * typePositions.get ( i ), pos ) ) { outline.load ( typeJudgement.getType
     * (), Outline.ExecuteMouseClick.TYPEINFERENCE ); } } else if ( t instanceof
     * TypeEquation ) { TypeEquation typeEquation = ( TypeEquation ) t; // Left
     * type if ( isIn ( leftType.get ( i ), pos ) ) { outline.load (
     * typeEquation.getLeft (), Outline.ExecuteMouseClick.TYPEINFERENCE ); } //
     * Right type else if ( isIn ( rightType.get ( i ), pos ) ) { outline.load (
     * typeEquation.getRight (), Outline.ExecuteMouseClick.TYPEINFERENCE ); } }
     * else if ( t instanceof TypeSubType ) { TypeSubType subType = (
     * TypeSubType ) t; // Left type if ( isIn ( leftType.get ( i ), pos ) ) {
     * outline.load ( subType.getType (),
     * Outline.ExecuteMouseClick.TYPEINFERENCE ); } // Right type else if ( isIn (
     * rightType.get ( i ), pos ) ) { outline.load ( subType.getType2 (),
     * Outline.ExecuteMouseClick.TYPEINFERENCE ); } } }
     */
  }


  /**
   * Sets an alternative color.<br>
   * <br>
   * Both renderers, the {@link PrettyStringRenderer} and the
   * {@link EnvironmentRenderer}, are updated with this color.
   * 
   * @param color The alternative color.
   */
  public void setAlternativeColor ( Color color )
  {
    this.alternativeColor = color;
    if ( this.renderer != null )
    {
      this.renderer.setAlternativeColor ( color );
    }
    /*
     * if ( this.substitutionRenderer != null ) {
     * this.substitutionRenderer.setAlternativeColor ( color ); }
     */
  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ()
  {
    if ( this.renderer != null )
    {
      this.renderer.checkLinewraps ();
    }
  }


  /**
   * Handles whether a ToolTip should be displayed for the environment, if some
   * parts of it are collapsed.
   * 
   * @param event
   */
  void handleMouseMoved ( MouseEvent event )
  {
    /*
     * // the point where the mosue is Point pointMousePosition = event.getPoint
     * (); // first, we do not want to have an tooltip setToolTipText ( null ); //
     * find out if the mosue is over an A ArrayList < Rectangle >
     * rectsOfAPositions = this.typeFormularRenderer .getAPositions (); for (
     * int i = 0 ; i < rectsOfAPositions.size () ; i++ ) { Rectangle
     * rectOfActualA = rectsOfAPositions.get ( i ); if ( isIn ( rectOfActualA,
     * pointMousePosition ) ) // if (x >= r.x && x <= // r.x+r.width && y >= //
     * r.y && y <= // r.y+r.width) { // genereate the TooltioText String
     * genreateTooltip = ""; //$NON-NLS-1$ // get the Infos about the tooltip
     * from the typeFormularRenderer ArrayList < ArrayList < PrettyString >>
     * list = this.typeFormularRenderer .getAPrettyStrings (); ArrayList <
     * PrettyString > prettyStrings = list.get ( i ); // build up the html
     * genreateTooltip += ( "<html>" ); //$NON-NLS-1$ for ( int l = 0 ; l <
     * prettyStrings.size () ; l++ ) { genreateTooltip +=
     * PrettyStringToHTML.toHTMLString ( prettyStrings .get ( l ) ); if ( l < (
     * prettyStrings.size () - 1 ) ) { genreateTooltip += " <br> ";
     * //$NON-NLS-1$ } } if ( prettyStrings.size () == 0 ) { genreateTooltip += ( "<font
     * size=+1>\u00D8</font>" ); //$NON-NLS-1$ } genreateTooltip += ( "</html>" );
     * //$NON-NLS-1$ setToolTipText ( genreateTooltip ); } } // tell the
     * PrettyStringRenderer where the mouse pointer is
     * this.toListenForMouse.setHereIam ( pointMousePosition ); // first, we do
     * not want to mark anything, we are waiting for mouse pointer // is over
     * one bounded id this.toListenForMouse.setMark ( false );
     * CompoundEquationUnify.this.repaint (); // note if to mark or not to mark
     * boolean mark = false; // walk throu the postions where to mark for ( int
     * t = 0 ; t < this.toListenForMouse.size () ; t++ ) { // get position of
     * pointer, these are rectangles. These positions are made // by the
     * PrettyStringRenderer Rectangle r = this.toListenForMouse.get ( t ); int
     * pX = r.x; int pX1 = r.x + r.width; int pY = r.y; int pY1 = r.y +
     * r.height; // fnde out if pointer is on one of the chars to mark if ( (
     * event.getX () >= pX ) && ( event.getX () <= pX1 ) && ( event.getY () >=
     * pY ) && ( event.getY () <= pY1 ) ) // if ( ( event.getX ( ) >= pX ) && (
     * event.getX ( ) <= pX1 ) ) { // just note it mark = true; } } // if the
     * pointer is on one of the bounded chars if ( mark ) { // we want to habe
     * marked this.toListenForMouse.setMark ( true );
     * CompoundEquationUnify.this.repaint (); } else { // we do not want to see
     * anything marked resetMouseContainer (); } if ( (
     * this.substitutionRenderer != null ) &&
     * this.substitutionRenderer.isCollapsed () ) { Rectangle r =
     * this.substitutionRenderer.getCollapsedArea (); if ( isIn ( r,
     * pointMousePosition ) ) { setToolTipText (
     * this.substitutionRenderer.getCollapsedString () ); } } // tooltip for the
     * single typeenvironments of every item if ( this.typeFormularRenderer !=
     * null ) { ArrayList < Rectangle > rectsOfCollapsedAreasPositions =
     * this.typeFormularRenderer .getCollapsedTypeEnvironmentAreas (); for ( int
     * i = 0 ; i < rectsOfCollapsedAreasPositions.size () ; i++ ) { Rectangle r =
     * rectsOfCollapsedAreasPositions.get ( i ); if ( isIn ( r,
     * pointMousePosition ) ) { setToolTipText ( this.typeFormularRenderer
     * .getCollapsedTypeEnvironmentsStrings ().get ( i ) ); } } }
     */
  }


  /**
   * reset the MouseContainer
   */
  void resetMouseContainer ()
  {
    this.toListenForMouse.reset ();
    this.toListenForMouse.setMark ( false );
    this.repaint ();
  }


  /**
   * Sets whether the expression should be wrapped or not.
   * 
   * @param pNoLineWrapping
   */
  public void setNoLineWrapping ( boolean pNoLineWrapping )
  {
    this.noLineWrapping = pNoLineWrapping;
  }


  /**
   * Sets the TypeSubsittutioins that should be rendered.
   * 
   * @param defaultTypeSubstitutionListP
   */
  public void setDefaultTypeSubstitutionList (
      TypeSubstitutionList defaultTypeSubstitutionListP )
  {
    /*
     * // check if we have a new environment if (
     * this.defaultTypeSubstitutionList != defaultTypeSubstitutionListP ) { //
     * update to the new environment this.defaultTypeSubstitutionList =
     * defaultTypeSubstitutionListP; // check what to do with the renderer if (
     * this.defaultTypeSubstitutionList == null ) { this.substitutionRenderer =
     * null; } else { if ( this.substitutionRenderer == null ) {
     * this.substitutionRenderer = new SubstitutionRenderer ();
     * this.substitutionRenderer .setAlternativeColor ( this.alternativeColor ); }
     * this.substitutionRenderer .setDefaultTypeSubstitutionList (
     * this.defaultTypeSubstitutionList ); } // be sure to schedule a repaint }
     */
    if ( this.defaultTypeSubstitutionList != defaultTypeSubstitutionListP )
    {
      this.defaultTypeSubstitutionList = defaultTypeSubstitutionListP;
      if ( this.defaultTypeSubstitutionList == null )
      {
        this.renderer = null;
      }
      else
      {
        if ( this.renderer == null )
        {
          this.renderer = new PrettyStringRenderer ();
          this.renderer.setAlternativeColor ( this.alternativeColor );
        }
        this.renderer.setPrettyString ( this.defaultTypeSubstitutionList
            .toPrettyString () );
      }
    }
    repaint ();
  }


  /**
   * Sets the environment that should be rendered.
   * 
   * @param typeEquationListP the type equation list
   */
  public void setTypeEquationList ( TypeEquationList typeEquationListP )
  {
    /*
     * // check if we have a new environment if ( this.typeEquationList !=
     * typeFormulaListP ) { // update to the new environment
     * this.typeEquationList = typeFormulaListP; // check what to do with the
     * renderer if ( this.typeEquationList == null ) { this.typeFormularRenderer =
     * null; } else { if ( this.typeFormularRenderer == null ) {
     * this.typeFormularRenderer = new TypeFormularRenderer ();
     * this.typeFormularRenderer .setToListenForMoudeContainer (
     * this.toListenForMouse ); this.typeFormularRenderer .setAlternativeColor (
     * this.alternativeColor ); } this.typeFormularRenderer.setTypeFormulaList (
     * this.typeEquationList ); } // be sure to schedule a repaint }
     */
    if ( this.typeEquationList != null )
    {
      this.typeEquationList = typeEquationListP;
      if ( this.typeEquationList == null
          && this.defaultTypeSubstitutionList == null )
      {
        this.renderer = null;
      }
      else
      {
        if ( this.renderer == null )
        {
          this.renderer = new PrettyStringRenderer ();
          this.renderer.setAlternativeColor ( this.alternativeColor );
        }
        this.renderer
            .setPrettyString ( this.typeEquationList.toPrettyString () );
      }
    }
    repaint ();
  }


  /**
   * Calculates the size needed to propperly render the compoundExpression
   * 
   * @param pMaxWidth
   * @return Dimension
   */
  public Dimension getNeededSize ( int pMaxWidth )
  {

    int maxWidth = pMaxWidth;
    Dimension result = new Dimension ( 0, 0 );
    // to guaranty that no line wrapping should be performed
    // set the maxWidth = MAX_INT
    if ( this.noLineWrapping )
    {
      maxWidth = Integer.MAX_VALUE;
    }
    // check whether there is Substitution...
    if ( ( this.defaultTypeSubstitutionList != null )
    // && ( this.defaultTypeSubstitutionList.size () > 0 ) )
        && ( this.defaultTypeSubstitutionList.getFirst () != null ) )
    {
      /*
       * TODO: cu - test this code
       */
      this.renderer.setPrettyString ( this.defaultTypeSubstitutionList
          .toPrettyString () );
      // The dimension the rendere needs to render the Substitutions
      // this.substitutionSize = this.substitutionRenderer.getNeededSize ();
      this.substitutionSize = this.renderer.getNeededSize ( maxWidth );
      // The higth is simpel
      result.height = this.substitutionSize.height;
      result.width = this.substitutionSize.width;
      result.width += this.spaceInFrontOf;
    }
    // check whether there are type equations...
    if ( ( this.typeEquationList != null )
    // && ( this.typeEquationList.size () > 0 ) )
        && ( this.typeEquationList.getFirst () != null ) )
    {
      /*
       * TODO: cu - test this code
       */
      this.renderer.setPrettyString ( this.typeEquationList.toPrettyString () );
      this.equationSize = this.renderer.getNeededSize ( maxWidth );
      result.width += Math.max ( this.equationSize.width + this.spaceInFrontOf,
          result.width );
      result.height = this.equationSize.height + this.substitutionSize.height;
      // TODO printing...
      /*
       * this.typeFormulaSize = this.typeFormularRenderer .getNeededSize (
       * maxWidth ); result.width += Math.max ( this.typeFormulaSize.width +
       * this.spaceInFrontOf, result.width ); result.height =
       * this.typeFormulaSize.height + this.substitutionSize.height;
       */

    }
    return result;
  }


  /**
   * The actualy rendering method.
   * 
   * @param gc The Graphics object that will be used to render the stuff.
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {
    // TODO test the different coponents of the renderer:
    // gc.setColor(Color.ORANGE);
    // gc.drawRect(0,0,neededSize.width-1, neededSize.height-1);
    // gc.setColor(Color.CYAN);
    // gc.drawRect(0,0,substitutionSize.width, substitutionSize.height);
    //		
    // gc.setColor(Color.RED);
    // for (int i = 0; i <
    // this.typeFormularRenderer.getTypeFormularPositions().size(); i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getTypeFormularPositions().get(i),
    // gc);
    // }
    //
    // gc.setColor(Color.GREEN);
    // for (int i = 0; i <
    // this.typeFormularRenderer.getLeftTypePositions().size(); i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getLeftTypePositions().get(i),
    // gc);
    // }
    //
    // gc.setColor(Color.GREEN);
    // for (int i = 0; i <
    // this.typeFormularRenderer.getRightTypePositions().size(); i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getRightTypePositions().get(i),
    // gc);
    // }
    //
    // gc.setColor(Color.YELLOW);
    // for (int i = 0; i < this.typeFormularRenderer.getTypePositions().size();
    // i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getTypePositions().get(i), gc);
    // }
    //
    // gc.setColor(Color.BLUE);
    // for (int i = 0; i <
    // this.typeFormularRenderer.getExpressionPositions().size(); i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getExpressionPositions().get(i),
    // gc);
    // }
    //
    // gc.setColor(Color.PINK);
    // for (int i = 0; i <
    // this.typeFormularRenderer.getCollapsedTypeEnvironmentAreas().size(); i++)
    // {
    // drawRectAngle(this.typeFormularRenderer.getCollapsedTypeEnvironmentAreas().get(i),
    // gc);
    // }

    // testAusgabe ( "paintComponent wurde aufgerufen..." ) ;
    // TODO Only for test to make yompoundexpression visible
    // it also displays how often the exptresso is rednerd while srolling...

    // --------------------------------
    // Color [] test = new Color [10];
    // test[0] = Color.yellow;
    // test[1] = Color.red;
    // test[2] = Color.green;
    // test[3] = Color.cyan;
    // test[4] = Color.green;
    // test[5] = Color.lightGray;
    // test[6] = Color.blue;
    // test[7] = Color.gray;
    // test[8] = Color.magenta;
    // test[9] = Color.orange;
    // double get = Math.random();
    // int getR = (int) (get*10);
    // gc.setColor (test[getR]);
    // TODO Workaround printing: if this ist not done nothing is readabel in PDF

    gc.setColor ( Color.WHITE );
    gc.fillRect ( -1, -1, getWidth () + 5, getHeight () + 5 );

    // --------------------------------
    // make sure that we have an expression renderer
    // if ( this.expressionRenderer == null )
    // {
    // return ;
    // }
    // assuming the size of the component will suffice, no testing
    // of any sizes will happen.
    /*
     * just to get reminded: no environment: expression storeenvironment:
     * (expression [env]) typeenvironment: [env] |> expression
     */
    int posX = 0;
    int posY = 0;

    if ( this.defaultTypeSubstitutionList != TypeSubstitutionList.EMPTY_LIST )
    {
      posX += this.spaceInFrontOf;
      this.renderer.render ( posX, posY, this.substitutionSize.width,
          getHeight (), gc, this.bonds, this.toListenForMouse );
      posY += this.substitutionSize.height;
    }

    if ( !this.typeEquationList.isEmpty () )
    {
      this.renderer.render ( posX, posY, this.equationSize.width, getHeight (),
          gc, this.bonds, this.toListenForMouse );
    }
    /*
     * int posX = 0; int posY = 0; // if there is an substitution render it now
     * if ( ( this.defaultTypeSubstitutionList != null ) && (
     * this.defaultTypeSubstitutionList.size () > 0 ) ) { posX +=
     * this.spaceInFrontOf; this.substitutionRenderer.renderer ( posX, posY,
     * this.substitutionSize.width, getHeight (), gc ); posY +=
     * this.substitutionSize.height; } // else if
     * (this.defaultTypeSubstitutionList instanceof TypeEnvironment) // { // //
     * draw the environment first // this.substitutionRenderer.renderer(posX,
     * posY, // this.substitutionSize.width, getHeight(), gc); // posX +=
     * this.substitutionSize.width; // // draw the arrow character in the
     * vertical center // int centerV = getHeight() / 2; // centerV +=
     * AbstractRenderer.getTextFontMetrics().getAscent() / 2; //
     * gc.setFont(AbstractRenderer.getTextFont()); //
     * gc.setColor(AbstractRenderer.getTextColor()); //
     * gc.drawString(CompoundExpressionTypeInference.arrowStr, posX, centerV); //
     * posX += //
     * AbstractRenderer.getTextFontMetrics().stringWidth(CompoundExpressionTypeInference.arrowStr); // //
     * draw the expression at the last position. //
     * this.expressionRenderer.render(posX, posY, getWidth(), getHeight(), gc, //
     * this.bonds, this.toListenForMouse); // } if ( this.typeEquationList !=
     * null ) { // this.typeFormularRenderer.renderer( posX, posY, //
     * this.typeFormulaSize.width, getHeight (), gc) ;
     * this.typeFormularRenderer.renderer ( posX, posY,
     * this.typeFormulaSize.width, this.typeFormulaSize.height, gc ); }
     */

    // last render the string besinde the mousepointer
    if ( this.dragged )
    {
      gc.drawString ( this.draggedString, this.draggedBesideMousePointer.x,
          this.draggedBesideMousePointer.y );
    }
  }


  /**
   * checs if the point is in the Rectangle
   * 
   * @param r the Rectangle
   * @param p the Point
   * @return TODO
   */
  private static boolean isIn ( Rectangle r, Point p )
  {
    int x = p.x;
    int y = p.y;
    if ( ( x >= r.x ) && ( x <= r.x + r.width ) && ( y >= r.y )
        && ( y <= r.y + r.height ) )
    {
      // System.out.println("der Punkg ist in dem Rect: "+r+ " - "+p);
      return true;
    }
    return false;
  }


  // /**
  // * TODO TESTMETHODE
  // *
  // * @param r
  // * @param g
  // */
  // public void drawRectAngle(Rectangle r, Graphics g)
  // {
  // g.drawRect(r.x, r.y, r.width, r.height);
  // // Rectangle rect = new Rectangle()
  // }

  /**
   * @return the draggedBesideMousePointer
   */
  public Point getDraggedBesideMousePointer ()
  {
    return this.draggedBesideMousePointer;
  }


  /**
   * @param pDraggedBesideMousePointer the draggedBesideMousePointer to set
   */
  public void setDraggedBesideMousePointer ( Point pDraggedBesideMousePointer )
  {
    this.draggedBesideMousePointer = pDraggedBesideMousePointer;
  }


  /**
   * @return the dragndropeabled
   */
  public boolean isDragndropeabled ()
  {
    return this.dragndropeabled;
  }


  /**
   * sets the ability to disable the dragg'n'drop. This happens if the node is
   * prooven. The user should not be able to switch expressions of one node
   * after if is prooven.
   * 
   * @param pDragndropeabled the dragndropeabled to set
   */
  public void setDragndropeabled ( boolean pDragndropeabled )
  {
    this.dragndropeabled = pDragndropeabled;
  }
}
