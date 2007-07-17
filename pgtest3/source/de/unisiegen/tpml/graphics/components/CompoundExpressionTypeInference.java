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

import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
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
  //private int mousePositionPressedX ;


  /**
   * saves the position where the mouse starts the dragging
   */
  //private int mousePositionPressedY ;


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
        //testAusgabe ( "Die Maus wurde gedragged..." ) ;
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

        Point pos = pMouseEvent.getPoint();
        
        ArrayList<Rectangle> leftType = typeFormularRenderer.getLeftTypePositions();
				ArrayList<Rectangle> rightType = typeFormularRenderer.getRightTypePositions();
				ArrayList<Rectangle> expressionPositions = typeFormularRenderer.getExpressionPositions();
				ArrayList<Rectangle> typePositions = typeFormularRenderer.getTypePositions();
				Outline outline = ((TypeInferenceView) CompoundExpressionTypeInference.this.getParent().getParent()
						.getParent().getParent().getParent().getParent()).getOutline();
				for (int i = 0; i < CompoundExpressionTypeInference.this.typeFormulaList.size(); i++)
				{
					TypeFormula t = CompoundExpressionTypeInference.this.typeFormulaList.get(i);
					if (t instanceof TypeJudgement)
					{
						TypeJudgement typeJudgement = (TypeJudgement) t;
						// Expression
						if (isIn(expressionPositions.get(i), pos))
						{
							outline.load (typeJudgement.getExpression(), Outline.ExecuteMouseClick.TYPEINFERENCE);
						}
						// Type
						else
							if (isIn(typePositions.get(i), pos))
							{
								outline.load(typeJudgement.getType(), Outline.ExecuteMouseClick.TYPEINFERENCE);
							}
					}
					else
						if (t instanceof TypeEquationTypeInference )
						{
							TypeEquationTypeInference typeEquation = (TypeEquationTypeInference) t;
							// Left type
							if (isIn(leftType.get(i), pos))
							{
								outline.load(typeEquation.getLeft(), Outline.ExecuteMouseClick.TYPEINFERENCE);
							}
							// Right type
							else
								if (isIn(rightType.get(i), pos))
								{
									outline.load(typeEquation.getRight(), Outline.ExecuteMouseClick.TYPEINFERENCE);
								}
						}
						else
							if (t instanceof TypeSubType )
							{
								TypeSubType subType = (TypeSubType) t;
								// Left type
								if (isIn(leftType.get(i), pos))
								{
									outline.load(subType.getType(), Outline.ExecuteMouseClick.TYPEINFERENCE);
								}
								// Right type
								else
									if (isIn(rightType.get(i), pos))
									{
										outline.load(subType.getType2(), Outline.ExecuteMouseClick.TYPEINFERENCE);
									}
							}
				}
			}
		});
    this.addMouseListener ( new MouseAdapter ( )
    {
      /**
       * the element where the mouse starts dragging
       */
    	private int rectPressed=-1;
    	/**
       * the element where the mouse ends dragging 
    	 */
    	private int rectReleased=-1;
      
      @ Override
      public void mouseExited ( MouseEvent e )
      {
      	// tell the toListenForMouse that the mose is gone
        toListenForMouse.reset ( ) ;
        toListenForMouse.setMark ( false ) ;
        CompoundExpressionTypeInference.this.repaint ( ) ;
      }


      @ Override
      public void mousePressed ( MouseEvent event )
      {
        // remember the position. If the user dragges thes point to another,
        // they will be switched
        Point mousePosititonPressex = event.getPoint();
      
        // look up if there is a typeformula at the point mousePosition.
        // if ther is no typformular, ther will be no dragg'n'drop
        ArrayList < Rectangle > rects = typeFormularRenderer.getTypeFormularPositions ( ) ;
        for ( int i = 0 ; i < rects.size ( ) ; i ++ )
        {
        	if (isIn(rects.get(i), mousePosititonPressex))
          {
            // set the rectPressed
            rectPressed=i;
            
            // set the String shown next to the mouse while dragging
            draggedString = typeFormulaList.get ( i ).toString ( ) ;
            
            //if the String is to large shorter it
            if ( draggedString.length ( ) > 13 )
            {
              draggedString = draggedString.substring ( 0 , 10 ) + "..." ;
            }
            dragged = true ;
          }
        }
      }


      @Override
      public void mouseReleased (MouseEvent event)
      {
        // 
        Point mouseReleased = event.getPoint();

        // resette
        ArrayList <Rectangle> rects = typeFormularRenderer.getTypeFormularPositions();
        for (int i = 0; i < rects.size(); i++)
        {
          if (isIn(rects.get(i), mouseReleased))
          {
            // set the rectReleased
            rectReleased = i;
          }
        }

        // if we have a dragg'n'drop
        if (dragged && rectPressed != rectReleased && rectPressed != -1 && rectReleased != -1)
        {
          typeFormularRenderer.draggNDropp(rectReleased, rectPressed);
        }
        dragged = false;
        repaint();
      }
    } ) ;
  }


  /**
   * Sets an alternative color.<br>
   * <br>
   * Both renderers, the {@link PrettyStringRenderer} and the {@link EnvironmentRenderer}, are updated with this color.
   * 
   * @param color
   *          The alternative color.
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
    // the point where the mosue is
    Point pointMousePosition = event.getPoint();
    
    //first, we do not want to have an tooltip
    setToolTipText ( null ) ;

    //find out if the mosue is over an A
    ArrayList <Rectangle> rectsOfAPositions = typeFormularRenderer.getAPositions();
    
    for (int i = 0; i < rectsOfAPositions.size(); i++)
    {
      Rectangle rectOfActualA = rectsOfAPositions.get(i);

      if (isIn(rectOfActualA, pointMousePosition)) //if (x >= r.x && x <= r.x+r.width && y >= r.y && y <= r.y+r.width)
      {
        //genereate the TooltioText
        String genreateTooltip = "";

        //get the Infos about the tooltip from the typeFormularRenderer
        ArrayList <ArrayList<PrettyString>> list = typeFormularRenderer.getAPrettyStrings();
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
    toListenForMouse.setHereIam ( pointMousePosition ) ;

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

      if (isIn(r, pointMousePosition))
      {
        setToolTipText ( this.substitutionRenderer.getCollapsedString ( ) ) ;
        testAusgabe(this.substitutionRenderer.getCollapsedString ( ));
      }
    }
    
    // tooltip for the single typeenvironments of every item 
    if (this.typeFormularRenderer != null  )
    {
      ArrayList <Rectangle> rectsOfCollapsedAreasPositions = this.typeFormularRenderer.getCollapsedAreas();
      
      for (int i = 0; i<rectsOfCollapsedAreasPositions.size(); i++)
      {
        Rectangle r = rectsOfCollapsedAreasPositions.get(i);
        if (isIn(r, pointMousePosition))
        {
          setToolTipText ( this.typeFormularRenderer.getCollapsedStrings().get(i) ) ;
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
      // TODO wozu eigentlich das?
      result.width += 2 * this.braceSize ;
    }
    // check whether there are typformulars...
    //testAusgabe ( "wad soll das?" + ( typeFormulaList != null ) ) ;
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
      result.width += Math.max ( this.typeFormulaSize.width ,
          this.substitutionSize.width ) ;
      result.height = this.typeFormulaSize.height
          + this.substitutionSize.height ;
    }
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
  	//TODO test the different coponents of the renderer:
  	gc.setColor(Color.RED);
  	for (int i=0; i<typeFormularRenderer.getTypeFormularPositions().size(); i++)
  	{
  		drawRectAngle(typeFormularRenderer.getTypeFormularPositions().get(i), gc);
  	}
  	
  	gc.setColor(Color.GREEN);
  	for (int i=0; i<typeFormularRenderer.getLeftTypePositions().size(); i++)
  	{
  		drawRectAngle(typeFormularRenderer.getLeftTypePositions().get(i), gc);
  	}
  	
  	gc.setColor(Color.GREEN);
  	for (int i=0; i<typeFormularRenderer.getRightTypePositions().size(); i++)
  	{
  		drawRectAngle(typeFormularRenderer.getRightTypePositions().get(i), gc);
  	}
  	
  	gc.setColor(Color.YELLOW);
  	for (int i=0; i<typeFormularRenderer.getTypePositions().size(); i++)
  	{
  		drawRectAngle(typeFormularRenderer.getTypePositions().get(i), gc);
  	}
  	
  	gc.setColor(Color.BLUE);
  	for (int i=0; i<typeFormularRenderer.getExpressionPositions().size(); i++)
  	{
  		drawRectAngle(typeFormularRenderer.getExpressionPositions().get(i), gc);
  	}
    
    gc.setColor(Color.PINK);
    for (int i=0; i<typeFormularRenderer.getCollapsedAreas().size(); i++)
    {
      drawRectAngle(typeFormularRenderer.getCollapsedAreas().get(i), gc);
    }
  	
  	
    //testAusgabe ( "paintComponent wurde aufgerufen..." ) ;
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
    if (x >= r.x && x <= r.x+r.width && y >= r.y && y <= r.y+r.height)
    {
    	//System.out.println("der Punkg ist in dem Rect: "+r+ " - "+p);
      return true;      
    }
    else
    {
    	//System.out.println("der Punkg ist in nicht drin: "+r+ " - "+p);
      return false;
    }
  
  }

  //TODO testMEthode...
  public void drawRectAngle (Rectangle r, Graphics g)
  {
  	g.drawRect(r.x, r.y, r.width, r.height);
  	//Rectangle rect = new Rectangle()
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
     //System.out.println(s);
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
