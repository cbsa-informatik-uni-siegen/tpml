package de.unisiegen.tpml.graphics.components ;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.graphics.outline.Outline;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringToHTML;
import de.unisiegen.tpml.graphics.renderer.SubstitutionRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;
import de.unisiegen.tpml.graphics.renderer.TypeFormularRenderer;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceView;


/**
 * this class renders the coumpoundexpression of the TypeInference
 */
public class CompoundExpressionTypeInference extends JComponent
{
  /**
   * 
   */
  private static final long serialVersionUID = - 7653329118052555176L ;


  /**
   * Renderer that is used to render the expressions
   */
  private PrettyStringRenderer expressionRenderer ;


  /**
   * Renderer that is used to render the TypeFormulars
   */
  private TypeFormularRenderer typeFormularRenderer ;

  /**
   * The size of the Typformulars.
   */
  private Dimension typeFormulaSize ;


  /**
   * The size of the Subistitutions.
   */
  private Dimension substitutionSize ;


  /**
   * saves the position where the mouse starts the dragging
   */
  private int mousePositionPressedX ;


  /**
   * saves the position where the mouse starts the dragging
   */
  private int mousePositionPressedY ;


  /**
   * The current list of DefaultTypeSubsititutions that are rendered.
   */
  private ArrayList < DefaultTypeSubstitution > defaultTypeSubstitutionList ;


  /**
   * the current Typformulars
   */
  private ArrayList < TypeFormula > typeFormulaList ;


  /**
   * Renderer that is used to render the substitutions.
   */
  private SubstitutionRenderer substitutionRenderer ;


  /**
   * The width of the braces around the expression and the environment.
   */
  private int braceSize ;


  /**
   * Whether the expression should be wrapped if there is not enough space to
   * render it in on line.<br>
   * Actualy this is only used with the result of the BigStepGUI.
   */
  private boolean noLineWrapping ;


  /**
   * If this color is given all colors of the {@link AbstractRenderer} are
   * ignored and only this color is used.
   */
  private Color alternativeColor ;


  /**
   * The arrow symbol that is used between the environment and the expression
   * when used within the
   * {@link de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent}
   */
  private static String arrowStr = " \u22b3 " ;


  /**
   * Initialises the CompoundExpression with the default values.<br>
   * <br>
   * The braces have a size of 10 pixes, no underlining and the color of the
   * {@link AbstractRenderer} are ignored.
   */
  private ShowBonds bonds ;


  /**
   * the list of points where the mouseovereffect will be react
   */
  private ToListenForMouseContainer toListenForMouse ;


  /**
   * true if the user is dragging a typformula
   */
  protected boolean dragged = false ;


  /**
   * the string the renderer will show beside the mousepointer
   */
  private String draggedString = "" ;


  /**
   * the position besind the mousepointer where the renderer will render the
   * draggedString
   */
  private int draggedX = 0 ;


  /**
   * the position besind the mousepointer where the renderer will render the
   * draggedString
   */
  private int draggedY = 0 ;


  /**
   * the constructor
   */
  public CompoundExpressionTypeInference ( )
  {
    super ( ) ;
    this.bonds = new ShowBonds ( ) ;
    this.toListenForMouse = new ToListenForMouseContainer ( ) ;
    this.alternativeColor = null ;
    this.braceSize = 10 ;
    //this.underlineExpression = null ;
    CompoundExpressionTypeInference.this.setDoubleBuffered ( true ) ;
    this.addMouseMotionListener ( new MouseMotionAdapter ( )
    {
      @ Override
      public void mouseMoved ( MouseEvent event )
      {
        // Resets the Cursor to the default Cursor after
        // changing the Cursor to the Dragging-Cursor
        Cursor c = new Cursor ( Cursor.DEFAULT_CURSOR ) ;
        getParent ( ).getParent ( ).setCursor ( c ) ;
        handleMouseMoved ( event ) ;
      }


      @ Override
      public void mouseDragged ( MouseEvent event )
      {
        testAusgabe ( "Die Maus wurde gedragged..." ) ;
        // if the User started dragging on an Typformula
        if ( dragged )
        {
          // be shure the Component repaints the Component to render the
          // draggedString
          repaint ( ) ;
          // change the Cursor to the Hand
          Cursor c = new Cursor ( Cursor.HAND_CURSOR ) ;
          getParent ( ).getParent ( ).setCursor ( c ) ;
          // tell the renderer where the draggedString will be renderd
          draggedX = event.getX ( ) + 5 ;
          draggedY = event.getY ( ) + 5 ;
          if ( event.getX ( ) >= getWidth ( ) - 50 )
          {
            draggedX = event.getX ( )
                - ( 50 - ( getWidth ( ) - event.getX ( ) ) ) ;
          }
          if ( event.getY ( ) <= 10 )
          {
            draggedY = event.getY ( ) + 15 ;
          }
        }
      }
    } ) ;
    this.addMouseListener ( new MouseAdapter ( )
    {
      public final void mouseClicked ( MouseEvent pMouseEvent )
      {
        int posX = pMouseEvent.getX ( ) ;
        int posY = pMouseEvent.getY ( ) ;
        ArrayList < Rectangle > rects = typeFormularRenderer
            .getTypeFormularPositions ( ) ;
        ArrayList < Rectangle > leftType = typeFormularRenderer
            .getLeftTypePositions ( ) ;
        ArrayList < Rectangle > rightType = typeFormularRenderer
            .getRightTypePositions ( ) ;
        ArrayList < Rectangle > expressionPositions = typeFormularRenderer
            .getExpressionPositions ( ) ;
        ArrayList < Rectangle > typePositions = typeFormularRenderer
            .getTypePositions ( ) ;
        Outline outline = ( ( TypeInferenceView ) CompoundExpressionTypeInference.this
            .getParent ( ).getParent ( ).getParent ( ).getParent ( )
            .getParent ( ).getParent ( ) ).getOutline ( ) ;
        for ( int i = 0 ; i < CompoundExpressionTypeInference.this.typeFormulaList
            .size ( ) ; i ++ )
        {
          TypeFormula t = CompoundExpressionTypeInference.this.typeFormulaList
              .get ( i ) ;
          if ( t instanceof TypeJudgement )
          {
            TypeJudgement typeJudgement = ( TypeJudgement ) t ;
            // Expression
            if ( posX >= expressionPositions.get ( i ).x
                && posX <= expressionPositions.get ( i ).x
                    + expressionPositions.get ( i ).width
                && posY >= expressionPositions.get ( i ).y
                    - expressionPositions.get ( i ).height
                && posY <= expressionPositions.get ( i ).y )
            {
              outline.loadPrettyPrintable ( typeJudgement.getExpression ( ) ,
                  Outline.ExecuteMouseClick.TYPEINFERENCE ) ;
            }
            // Type
            else if ( posX >= typePositions.get ( i ).x
                && posX <= typePositions.get ( i ).x
                    + typePositions.get ( i ).width
                && posY >= typePositions.get ( i ).y
                    - typePositions.get ( i ).height
                && posY <= typePositions.get ( i ).y )
            {
              outline.loadPrettyPrintable ( typeJudgement.getType ( ) ,
                  Outline.ExecuteMouseClick.TYPEINFERENCE ) ;
            }
          }
          else if ( t instanceof TypeEquationTypeInference )
          {
            TypeEquationTypeInference typeEquation = ( TypeEquationTypeInference ) t ;
            // Left type
            if ( posX >= leftType.get ( i ).x
                && posX <= leftType.get ( i ).x + leftType.get ( i ).width
                && posY >= leftType.get ( i ).y - leftType.get ( i ).height
                && posY <= leftType.get ( i ).y )
            {
              outline.loadPrettyPrintable ( typeEquation.getLeft ( ) ,
                  Outline.ExecuteMouseClick.TYPEINFERENCE ) ;
            }
            // Right type
            else if ( posX >= rightType.get ( i ).x
                && posX <= rightType.get ( i ).x + rightType.get ( i ).width
                && posY >= rightType.get ( i ).y - rightType.get ( i ).height
                && posY <= rightType.get ( i ).y )
            {
              outline.loadPrettyPrintable ( typeEquation.getRight ( ) ,
                  Outline.ExecuteMouseClick.TYPEINFERENCE ) ;
            }
          }
        }
        /*
         * for (int i = 0; i<rects.size(); i++) { if (posX >= rects.get(i).x &&
         * posX <= rects.get(i).x+rects.get(i).width && posY >=
         * rects.get(i).y-rects.get(i).height && posY <= rects.get(i).y) {
         * Outline outline = (( TypeInferenceView )
         * CompoundExpressionTypeInference.this.getParent ( ) .getParent (
         * ).getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
         * .getOutline ( ) ; TypeFormula t =
         * CompoundExpressionTypeInference.this.typeFormulaList.get ( i ); if (
         * t instanceof TypeJudgement ) { TypeJudgement typeJudgement = (
         * TypeJudgement ) t ; if (posX >= expressionPositions.get(i).x && posX <=
         * expressionPositions.get(i).x+expressionPositions.get(i).width) {
         * outline.loadPrettyPrintable (typeJudgement.getExpression ( ) ,
         * Outline.ExecuteMouseClick.TYPEINFERENCE ) ; } if (posX >=
         * typePositions.get(i).x && posX <=
         * typePositions.get(i).x+typePositions.get(i).width) {
         * outline.loadPrettyPrintable (typeJudgement.getType ( ),
         * Outline.ExecuteMouseClick.TYPEINFERENCE ) ; } } else if ( t
         * instanceof TypeEquationTypeInference ) { TypeEquationTypeInference
         * typeEquation = ( TypeEquationTypeInference ) t ; if (posX >=
         * leftType.get(i).x && posX <= leftType.get(i).x+leftType.get(i).width) {
         * outline.loadPrettyPrintable ( typeEquation.getLeft ( ) ,
         * Outline.ExecuteMouseClick.TYPEINFERENCE ) ; } if (posX >=
         * rightType.get(i).x && posX <=
         * rightType.get(i).x+rightType.get(i).width) {
         * outline.loadPrettyPrintable ( typeEquation.getRight ( ) ,
         * Outline.ExecuteMouseClick.TYPEINFERENCE ) ; } } } }
         */
      }
    } ) ;
    this.addMouseListener ( new MouseAdapter ( )
    {
    	int rectPressed;
    	int rectReleased;
      @ Override
      public void mouseExited ( MouseEvent e )
      {
      	//tell the toListenForMouse that the mose is gone
        toListenForMouse.reset ( ) ;
        toListenForMouse.setMark ( false ) ;
        CompoundExpressionTypeInference.this.repaint ( ) ;
      }


      @ Override
      public void mousePressed ( MouseEvent event )
      {
        testAusgabe ( "Die Maus wurde gedrückt..." ) ;
        // remember the position. If the user dragges thes point to another,
        // they will be switched
        mousePositionPressedX = event.getX ( ) ;
        mousePositionPressedY = event.getY ( ) ;
        // look up if there is a typeformula at the point mousePosition.
        // if ther is no typformular, ther will be no dragg'n'drop
        ArrayList < Rectangle > rects = typeFormularRenderer.getTypeFormularPositions ( ) ;
        for ( int i = 0 ; i < rects.size ( ) ; i ++ )
        {
          if ( mousePositionPressedX >= rects.get ( i ).x
              && mousePositionPressedX <= rects.get ( i ).x + rects.get ( i ).width
              && mousePositionPressedY >= rects.get ( i ).y - rects.get ( i ).height
              && mousePositionPressedY <= rects.get ( i ).y )
          {
            testAusgabe ( "" + i + ". Bereicht: " + rects.get ( i ).toString ( ) ) ;
            rectPressed=i;
            draggedString = typeFormulaList.get ( i ).toString ( ) ;
            if ( draggedString.length ( ) > 13 )
            {
              draggedString = draggedString.substring ( 0 , 10 ) + "..." ;
            }
            dragged = true ;
          }
        }
      }


      @ Override
      public void mouseReleased ( MouseEvent event )
      {
        testAusgabe ( "Die Maus wurde losgelassen..." ) ;
        int mousePositionReleasedX = event.getX ( ) ;
        int mousePositionReleasedY = event.getY ( ) ;
        // if the point there the mouse was pressed is the same the mouse was
        // released, ther is no dragg and dropp and the typeformula will only be
        // marked
//        if ( mousePositionReleasedX == mousePositionPressedX && mousePositionReleasedY == mousePositionPressedY )
//        {
//          ArrayList < Rectangle > rects = typeFormularRenderer
//              .getTypeFormularPositions ( ) ;
//          for ( int i = 0 ; i < rects.size ( ) ; i ++ )
//          {
//            testAusgabe ( "" + i + ". Bereicht: " + rects.get ( i ).toString ( ) ) ;
//          }
//          for ( int i = 0 ; i < rects.size ( ) ; i ++ )
//          {
//            if ( mousePositionReleasedX >= rects.get ( i ).x
//                && mousePositionReleasedX <= rects.get ( i ).x + rects.get ( i ).width
//                && mousePositionReleasedY >= rects.get ( i ).y - rects.get ( i ).height
//                && mousePositionReleasedY <= rects.get ( i ).y )
//            {
//            	//TODO test: These lines drwa a rect arround the single Elements 
//              //Graphics gc = getGraphics ( ) ;
//              //typeFormularRenderer.markArea ( rects.get ( i ).x , rects.get ( i ).y , rects.get ( i ).width , rects.get ( i ).height , gc , i ) ;
//            }
//          }
//        }
        // if the point of pressed and released are unequal
        // the typformulas at the two points will be switched
//        else
        {
          // resetten
          testAusgabe ( "Dragg and Drop implementieren" ) ;
          ArrayList < Rectangle > rects = typeFormularRenderer.getTypeFormularPositions ( ) ;
          for ( int i = 0 ; i < rects.size ( ) ; i ++ )
          {
            if ( mousePositionReleasedX >= rects.get ( i ).x
                && mousePositionReleasedX <= rects.get ( i ).x + rects.get ( i ).width
                && mousePositionReleasedY >= rects.get ( i ).y - rects.get ( i ).height
                && mousePositionReleasedY <= rects.get ( i ).y )
            {
              testAusgabe ( "" + i + ". Bereicht: "+ rects.get ( i ).toString ( ) ) ;
              //Graphics gc = getGraphics ( ) ;
              rectReleased=i;
              //typeFormularRenderer.markArea ( rects.get ( i ).x , rects.get ( i ).y , rects.get ( i ).width ,rects.get ( i ).height , gc , i ) ;
            }
//            else if ( mousePositionPressedX >= rects.get ( i ).x
//                && mousePositionPressedX <= rects.get ( i ).x + rects.get ( i ).width
//                && mousePositionPressedY >= rects.get ( i ).y - rects.get ( i ).height
//                && mousePositionPressedY <= rects.get ( i ).y )
//            {
//              System.out.println ( "" + i + ". Bereicht: "
//                  + rects.get ( i ).toString ( ) ) ;
//              Graphics gc = getGraphics ( ) ;
//              //typeFormularRenderer.markArea ( rects.get ( i ).x , rects.get ( i ).y , rects.get ( i ).width , rects.get ( i ).height , gc , i ) ;
//            }
          }
        }
        
//        ArrayList < Rectangle > rects = typeFormularRenderer.getTypeFormularPositions ( ) ;
        if (rectPressed != rectReleased)
        {
        	typeFormularRenderer.draggNDropp (rectReleased, rectPressed) ;
        	
        }
        dragged = false ;
        repaint ( ) ;
      }
    } ) ;
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
    this.alternativeColor = color ;
    if ( this.expressionRenderer != null )
    {
      this.expressionRenderer.setAlternativeColor ( color ) ;
    }
    if ( this.substitutionRenderer != null )
    {
      this.substitutionRenderer.setAlternativeColor ( color ) ;
    }
  }


//  /**
//   * Sets the expression, that should be underlined.
//   * 
//   * @param underlineExpression
//   */
//  public void setUnderlineExpression_alt ( Expression underlineExpression )
//  {
//    boolean needsRepaint = this.underlineExpression != underlineExpression ;
//    this.underlineExpression = underlineExpression ;
//    if ( this.expressionRenderer != null )
//    {
//      this.expressionRenderer
//          .setUndelinePrettyPrintable ( this.underlineExpression ) ;
//    }
//    if ( needsRepaint )
//    {
//      repaint ( ) ;
//    }
//  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ( )
  {
    if ( this.expressionRenderer != null )
    {
      this.expressionRenderer.checkLinewraps ( ) ;
    }
  }


  /**
   * Handles whether a ToolTip should be displayed for the environment, if some
   * parts of it are collapsed.
   * 
   * @param event
   */
  private void handleMouseMoved ( MouseEvent event )
  {
    //first, we do not want to have an tooltip
    setToolTipText ( null ) ;

    //find out if the mosue is over an A
    ArrayList <Rectangle> rs = typeFormularRenderer.getAPositions();
    
    for (int i = 0; i < rs.size(); i++)
    {
      Rectangle r = rs.get(i);
      //the simple unfoprmatted version
      //      Point p = event.getPoint();
      //      if (isIn (r, p))
      //      {
      //        setToolTipText(typeFormularRenderer.getAStrings().get(i));
      //      }
      Point p = event.getPoint();
      if (isIn(r, p)) //if (x >= r.x && x <= r.x+r.width && y >= r.y && y <= r.y+r.width)
      {
        //genereate the TooltioText
        String genreateTooltip = "";

        //get the Infos about the tooltip from the typeFormularRenderer
        ArrayList <ArrayList> list = typeFormularRenderer.getAPrettyStrings();
        ArrayList <PrettyString> prettyStrings = list.get(i);

        //build up the html
        genreateTooltip += ("<html>");

        for (int l = 0; l < prettyStrings.size(); l++)
        {
          genreateTooltip += PrettyStringToHTML.toHTMLString(prettyStrings.get(l));
          if (l < (prettyStrings.size() - 1))
          {
            genreateTooltip += " <br> ";
          }
        }
        
        if (prettyStrings.size() == 0)
        {
        	genreateTooltip += ("<font size=+1>\u00D8</font>");
        }

        genreateTooltip += ("</html>");
        setToolTipText(genreateTooltip);
      }
    }
    
    //tell the PrettyStringRenderer where the mouse pointer is
    toListenForMouse.setHereIam ( event.getX ( ) , event.getY ( ) ) ;

    //first, we do not want to mark anything, we are waiting for mouse pointer is over one bounded id
    toListenForMouse.setMark ( false ) ;
    CompoundExpressionTypeInference.this.repaint ( ) ;
    
    //note if to mark or not to mark
    boolean mark = false;
    
    //walk throu the postions where to mark
    for ( int t = 0 ; t < toListenForMouse.size ( ) ; t++)
    {
      //get position of pointer, these are rectangles. These positions are made by the PrettyStringRenderer
      Rectangle r =  toListenForMouse.get ( t ) ;
      int pX = r.x;
      int pX1 = r.x+r.width ;
      int pY = r.y;
      int pY1 = r.y+r.height;
    
      // fnde out if pointer is on one of the chars to mark
      if ((event.getX() >= pX) && (event.getX() <= pX1) && (event.getY() >= pY) && (event.getY() <= pY1)) 
      //if ( ( event.getX ( ) >= pX ) && ( event.getX ( ) <= pX1 ) )
      {
        //just note it
        mark = true;
      }
    }
    
    //if the pointer is on one of the bounded chars
    if (mark)
    {
      //we want to habe marked
      toListenForMouse.setMark ( true ) ;
      CompoundExpressionTypeInference.this.repaint ( ) ;
    }
    else
    {
      //we do not want to see anything marked
     toListenForMouse.setMark ( false ) ;
     toListenForMouse.reset();
     CompoundExpressionTypeInference.this.repaint ( ) ;
    }

    if ( this.substitutionRenderer != null && this.substitutionRenderer.isCollapsed ( ) )
    {
      Rectangle r = this.substitutionRenderer.getCollapsedArea ( ) ;
      testAusgabe("Die Grenzen r: "+ r.x+" - "+(r.x+r.width)+", "+r.y+"-"+(r.y+r.height));
      testAusgabe("Die Maus:"+event.getX ( )+", "+event.getY ( ));
      if ( event.getX ( ) >= r.x && event.getX ( ) <= r.x + r.width && event.getY() >= r.y && event.getY() <= r.y+r.height )
      {
        setToolTipText ( this.substitutionRenderer.getCollapsedString ( ) ) ;
        testAusgabe(this.substitutionRenderer.getCollapsedString ( ));
      }
      else
      {
        //setToolTipText ( null ) ;
      }
    }
    
    //TOOLTIPText für die einzelnen Dinger...
    //if (this.typeFormularRenderer != null && this.typeFormularRenderer.isCollapsed ( ) )
    if (this.typeFormularRenderer != null  )
    {
      //setToolTipText ( null ) ;
          
      rs = this.typeFormularRenderer.getCollapsedAreas();
      
      for (int i = 0; i<rs.size(); i++)
      {
        Rectangle r = rs.get(i);
        testAusgabe("Die Grenzen r: "+ r.x+" - "+(r.x+r.width)+", "+r.y+"-"+(r.y+r.height));
        testAusgabe("Die Maus:"+event.getX ( )+", "+event.getY ( ));
        //if ( event.getX ( ) >= r.x && event.getX ( ) <= r.x + r.width )
        if ( event.getX ( ) >= r.x && event.getX ( ) <= r.x + r.width && event.getY() >= r.y && event.getY() <= r.y+r.height )
        {
          testAusgabe("ja, diesen hier!"+i);
          setToolTipText ( this.typeFormularRenderer.getCollapsedStrings().get(i) ) ;
          testAusgabe(getToolTipText());
        } 
      }   
    }    

  }


  /**
   * Sets whether the expression should be wrapped or not.
   * 
   * @param noLineWrapping
   */
  public void setNoLineWrapping ( boolean noLineWrapping )
  {
    this.noLineWrapping = noLineWrapping ;
  }


  /**
   * Sets the TypeSubsittutioins that should be rendered.
   * 
   * @param defaultTypeSubstitutionListP
   */
  public void setDefaultTypeSubstitutionList (
      ArrayList < DefaultTypeSubstitution > defaultTypeSubstitutionListP )
  {
    // testAusgabe("neue Liste gesetzt: "+ defaultTypeSubstitutionListP.size());
    // check if we have a new environment
    if ( this.defaultTypeSubstitutionList != defaultTypeSubstitutionListP )
    {
      // update to the new environment
      this.defaultTypeSubstitutionList = defaultTypeSubstitutionListP ;
      // check what to do with the renderer
      if ( this.defaultTypeSubstitutionList == null )
      {
        this.substitutionRenderer = null ;
      }
      else
      {
        if ( this.substitutionRenderer == null )
        {
          this.substitutionRenderer = new SubstitutionRenderer ( ) ;
          this.substitutionRenderer
              .setAlternativeColor ( this.alternativeColor ) ;
        }
        this.substitutionRenderer
            .setDefaultTypeSubstitutionList ( this.defaultTypeSubstitutionList ) ;
      }
      // be sure to schedule a repaint
      repaint ( ) ;
    }
  }


  /**
   * Sets the environment taht should be rendered.
   * 
   * @param typeFormulaListP
   */
  public void setTypeFormulaList ( ArrayList < TypeFormula > typeFormulaListP )
  {
    // testAusgabe("neue Liste gesetzt: "+ typeFormulaListP.size());
    // check if we have a new environment
    if ( this.typeFormulaList != typeFormulaListP )
    {
      // update to the new environment
      this.typeFormulaList = typeFormulaListP ;
      // check what to do with the renderer
      if ( this.typeFormulaList == null )
      {
        this.typeFormularRenderer = null ;
      }
      else
      {
        if ( this.typeFormularRenderer == null )
        {
          this.typeFormularRenderer = new TypeFormularRenderer ( ) ;
          this.typeFormularRenderer
              .setToListenForMoudeContainer ( toListenForMouse ) ;
          this.typeFormularRenderer
              .setAlternativeColor ( this.alternativeColor ) ;
        }
        this.typeFormularRenderer.setTypeFormulaList ( this.typeFormulaList ) ;
      }
      // be sure to schedule a repaint
      repaint ( ) ;
    }
  }


  /**
   * Calculates the size needed to propperly render the compoundExpression
   * 
   * @param maxWidth
   * @return Dimension
   */
  public Dimension getNeededSize ( int maxWidth )
  {
    Dimension result = new Dimension ( 0 , 0 ) ;
    // to guaranty that no line wrapping should be performed
    // set the maxWidth = MAX_INT
    if ( this.noLineWrapping )
    {
      maxWidth = Integer.MAX_VALUE ;
    }
    // check whether there is Substitution...
    if ( this.defaultTypeSubstitutionList != null )
    {
      // The dimension the rendere needs to render the Substitutions
      this.substitutionSize = this.substitutionRenderer.getNeededSize ( ) ;
      // The higth is simpel
      result.height = this.substitutionSize.height ;
      // The
      // for (int i = 0; i<defaultTypeSubstitutionList.size(); i++)
      // {
      // int add = AbstractRenderer.getTextFontMetrics ( ).stringWidth
      // (defaultTypeSubstitutionList.get(i).toString());
      // result.width = result.width + add;
      // }
      // TODO wozu eigentlich das?
      result.width += 2 * this.braceSize ;
    }
    // check whether there are typformulars...
    testAusgabe ( "wad soll das?" + ( typeFormulaList != null ) ) ;
    if ( this.typeFormulaList != null )
    {
      // now check the size still available for the expression
      // TODO Test for printing
      // this.expressionSize = this.expressionRenderer.getNeededSizeAll_ (
      // maxWidth ) ;
      // this.typeFormulaSize = this.expressionRenderer.getNeededSize ( maxWidth
      // ) ;
      this.typeFormulaSize = this.typeFormularRenderer
          .getNeededSize ( maxWidth ) ;
      // this.typeFormulaSize = this.typeFormularRenderer.getNeededSize(
      // Integer.MAX_VALUE ) ;
      // testAusgabe("Die gebrauchte Größe der typeFormulaSize:
      // "+typeFormulaSize.height + ", "+ typeFormulaSize.width);
      // this.typeFormulaSize = this.typeFormularRenderer.getNeededSize ( ) ;
      result.width += Math.max ( this.typeFormulaSize.width ,
          this.substitutionSize.width ) ;
      result.height = this.typeFormulaSize.height
          + this.substitutionSize.height ;
    }
    // testAusgabe("Größe der Substitutions: "+substitutionSize.height + ",
    // "+substitutionSize.width );
    // testAusgabe("Größe der Typeformulas: "+typeFormulaSize.height+ ",
    // "+typeFormulaSize.width);
    // System.out.println("Wir adsdieren einfach mal 33");
    // TODO dreckiger Workaround...
    // result.height *= 2;
    return result ;
  }


  /**
   * The actualy rendering method.
   * 
   * @param gc The Graphics object that will be used to render the stuff.
   */
  @ Override
  protected void paintComponent ( Graphics gc )
  {
    testAusgabe ( "paintComponent wurde aufgerufen..." ) ;
    // TODO Only for test to make yompoundexpression visible
    // it also displays how often the exptresso is rednerd while srolling...
    // gc.setColor (Color.yellow);
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
    // gc.fillRect(0, 0, getWidth () - 1, getHeight () - 1);
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
    int posX = 0 ;
    int posY = 0 ;
    // if there is an environment and it is of type Store
    // draw the braces around the entire expression with environment
    // if there is no environment or the environment is of type
    // Store, the entire expression (with environment) will begin
    // with the expression
    if ( this.defaultTypeSubstitutionList == null
        || this.defaultTypeSubstitutionList instanceof ArrayList )
    {
      // this.expressionRenderer.render ( posX , posY , getHeight ( ) , gc ,
      // bonds , toListenForMouse ) ;
      // this.expressionRenderer.render ( posX , posY , getHeight () , gc ,
      // bonds , toListenForMouse ) ;
      // posX += this.expressionSize.width ;
      // if there is an environment render it now
      if ( this.defaultTypeSubstitutionList != null
          && defaultTypeSubstitutionList.size ( ) > 0 )
      {
        // testAusgabe ("hier sollte er ankommen und das sollte er auch
        // rendern...!");
        posX += this.braceSize ;
        this.substitutionRenderer.renderer ( posX , posY ,
            this.substitutionSize.width , getHeight ( ) , gc ) ;
        posY += substitutionSize.height ;
      }
    }
    else if ( this.defaultTypeSubstitutionList instanceof TypeEnvironment )
    {
      // draw the environment first
      this.substitutionRenderer.renderer ( posX , posY ,
          this.substitutionSize.width , getHeight ( ) , gc ) ;
      posX += this.substitutionSize.width ;
      // draw the arrow character in the vertical center
      int centerV = getHeight ( ) / 2 ;
      centerV += AbstractRenderer.getTextFontMetrics ( ).getAscent ( ) / 2 ;
      gc.setFont ( AbstractRenderer.getTextFont ( ) ) ;
      gc.setColor ( AbstractRenderer.getTextColor ( ) ) ;
      gc
          .drawString ( CompoundExpressionTypeInference.arrowStr , posX ,
              centerV ) ;
      posX += AbstractRenderer.getTextFontMetrics ( ).stringWidth (
          CompoundExpressionTypeInference.arrowStr ) ;
      // draw the expression at the last position.
      this.expressionRenderer.render ( posX , posY , getWidth ( ) ,
          getHeight ( ) , gc , bonds , toListenForMouse ) ;
    }
    if ( this.typeFormulaList != null )
    {
      // this.typeFormularRenderer.renderer( posX, posY,
      // this.typeFormulaSize.width, getHeight (), gc) ;
      this.typeFormularRenderer.renderer ( posX , posY ,
          this.typeFormulaSize.width , typeFormulaSize.height , gc ) ;
    }
    // TODO Test
    // gc.setColor (Color.YELLOW);
    // gc.drawRect(0, 0, getWidth () - 1, getHeight () - 1);
    // last if dragged render the dragged String
    // TODO mal gucken, ob man hier die größe änern kann...
    if ( dragged )
    {
      // setSize(getWidth()+100, getHeight()+100);
      gc.drawString ( draggedString , draggedX , draggedY ) ;
    }
  }
  
  
  /**
   * 
   * checs if the point is in the Rectangle
   *
   * @param r the Rectangle
   * @param p the Point
   * @return
   */
  private static boolean isIn (Rectangle r, Point p)
  {
    int x = p.x;
    int y = p.y;
    if (x >= r.x && x <= r.x+r.width && y >= r.y && y <= r.y+r.width)
    {
      return true;      
    }
    else
    {
      return false;
    }
  
  }


  /**
   * @return the list of TypeFormulas
   */
  public ArrayList getTypeFormulaList ( )
  {
    return typeFormulaList ;
  }


  private static void testAusgabe ( String s )
  {
    // System.out.println(s);
  }


  private static void testAusgabe ( )
  {
    // System.out.println();
  }


  private static void testAusgabeL ( String s )
  {
    // System.out.print(s);
  }
}
