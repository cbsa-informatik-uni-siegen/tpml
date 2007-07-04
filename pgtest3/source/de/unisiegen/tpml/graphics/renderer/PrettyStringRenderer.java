package de.unisiegen.tpml.graphics.renderer ;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.graphics.Theme;
import de.unisiegen.tpml.graphics.components.Bonds;
import de.unisiegen.tpml.graphics.components.ShowBonds;


/**
 * Subclass of the {@link AbstractRenderer} providing the rendering of a
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}
 * 
 * @author marcell
 * @author michael
 */
public class PrettyStringRenderer extends AbstractRenderer
{
  /**
   * Inner private class the store information for the line wrapping
   * 
   * @author marcell
   * @author michael
   */
  private class CheckerResult
  {
    /**
     * list of break Offsets
     */
    // / The annotation used for the linewrapping
    public ArrayList < Integer > breakOffsets ;


    /**
     * row-counts
     */
    // / The rows used to
    public int rows ;


    /**
     * the needed size
     */
    public Dimension sizeOfResult ;


    /**
     * constructor
     */
    public CheckerResult ( )
    {
      this.sizeOfResult = new Dimension ( ) ;
      this.breakOffsets = new ArrayList < Integer > ( ) ;
    }
  }


  /**
   * Results containg the information of all possible linewrappings.
   */
  private LinkedList < CheckerResult > results ;


  /**
   * Results containg the information of all needed Linewarps to fit on paper.
   */
  private LinkedList < CheckerResult > resultsForPrinting ;


  /**
   * The pretty string that should be rendered.
   */
  private PrettyString prettyString ;


  /**
   * The result of all results that actually will be used during the rendering.
   * If <i>result</i> is <i>null</i> no line wrapping is done.
   */
  private CheckerResult result ;


  // private ArrayList <CheckerResult> resultsWillBeUsed = new ArrayList
  // <CheckerResult>();
  /**
   * The pretty printable that will be underline during the rendering.
   */
  private PrettyPrintable underlinePrettyPrintable ;


  /**
   * saves the positions where the mouse will react
   */
  private ToListenForMouseContainer toListenForMouse ;


  /**
   * The annotation containing the information where the underline appears
   * within the string.
   */
  private PrettyAnnotation underlineAnnotation ;


  /**
   * constructor
   */
  public PrettyStringRenderer ( )
  {
    this.results = new LinkedList < CheckerResult > ( ) ;
    this.resultsForPrinting = new LinkedList < CheckerResult > ( ) ;
    this.result = null ;
    this.underlinePrettyPrintable = null ;
    this.underlineAnnotation = null ;
  }


  /**
   * Sets the PrettyString
   * 
   * @param prettyString
   */
  public void setPrettyString ( PrettyString prettyString )
  {
    this.prettyString = prettyString ;
    if ( this.prettyString != null && this.underlinePrettyPrintable != null )
    {
      this.underlineAnnotation = this.prettyString
          .getAnnotationForPrintable ( this.underlinePrettyPrintable ) ;
    }
    else
    {
      this.underlineAnnotation = null ;
    }
    checkLinewraps ( ) ;
  }


  /**
   * Sets the prettyPrintable that should be underlined.
   * 
   * @param prettyPrintable
   */
  public void setUndelinePrettyPrintable ( PrettyPrintable prettyPrintable )
  {
    this.underlinePrettyPrintable = prettyPrintable ;
    if ( this.prettyString != null && this.underlinePrettyPrintable != null )
    {
      this.underlineAnnotation = this.prettyString
          .getAnnotationForPrintable ( this.underlinePrettyPrintable ) ;
    }
    else
    {
      this.underlineAnnotation = null ;
    }
  }


  /**
   * Checks which of the previously calculated result (by checklinewrap) is the
   * best match for the given maxWidth.<br>
   * <br>
   * The size of the widest result that still fits in the maxWidth will be
   * returned. If no result is stored that fits in the smallest size is
   * returned; in this case the returned size is wider than the given maxWidth.
   * 
   * @param maxWidth The maximum available size for this expression.
   * @return The dimensions that will be needed to perform the rendering later.
   */
  public Dimension getNeededSize ( int maxWidth )
  {
    // Find the biggest result, that is smaller than the given
    // maxWidth. If none could be found the smllest result available
    // will be used.
    CheckerResult smallestResult = null ;
    CheckerResult biggestResult = null ;
    for ( CheckerResult r : this.results )
    {
      //as long as the actual result is smaller than the smallesResul update it
      if ( smallestResult == null || smallestResult.sizeOfResult.width > r.sizeOfResult.width )
      {
        smallestResult = r ;
      }
      //let only the results in that are smaller than the maxwidht or equal
      if ( r.sizeOfResult.width <= maxWidth )
      {
        //the best one will be the one with the smallest heigh. everyone with the same height the best one will be the smallest
        //if ( biggestResult == null || biggestResult.sizeOfResult.width < r.sizeOfResult.width || biggestResult.sizeOfResult.height > r.sizeOfResult.height)
        if ( biggestResult == null || biggestResult.sizeOfResult.height > r.sizeOfResult.height || (biggestResult.sizeOfResult.height == r.sizeOfResult.height && biggestResult.sizeOfResult.width > r.sizeOfResult.width) )
        {
          biggestResult = r ;
        }
      }
    }
    if ( biggestResult != null )
    {
      this.result = biggestResult ;
    }
    else if ( smallestResult != null )
    {
      this.result = smallestResult ;
    }
    else 
    {
    	this.result = null;
    }
    return this.result.sizeOfResult ;
  }


  /**
   * Checks which of the previously calculated results will be used to match the
   * given maxWidth.<br>
   * <br>
   * 
   * @param maxWidth The maximum available size for this expression.
   * @return The dimensions that will be needed to perform the rendering later.
   */
  public Dimension getNeededSizeAll_ ( int maxWidth )
  {
    // JAVA makes teh Window very samll at first (-56)
    // catch this
    if ( maxWidth < 0 )
    {
      return new Dimension ( 0 , 0 ) ;
    }
    // first, find out if the normal algorithem
    // is ok for rendering, then use it
    Dimension dimOfOne = getNeededSize ( maxWidth ) ;
    if ( dimOfOne.width <= maxWidth )
    {
      return dimOfOne ;
    }
    // else find out everey needed preakpoint
    // get all Breakpoints
    ArrayList < Integer > allBreakPoints = new ArrayList < Integer > ( ) ;
    for ( int i = 0 ; i < results.size ( ) ; i ++ )
    {
      for ( int j = 0 ; j < results.get ( i ).breakOffsets.size ( ) ; j ++ )
      {
        allBreakPoints.add ( results.get ( i ).breakOffsets.get ( j ) ) ;
      }
    }
    // sort all Breakpoints them
    Collections.sort ( allBreakPoints ) ;
    // remember the used ones
    ArrayList < Integer > useBreakPoints = new ArrayList < Integer > ( ) ;
    // these results will be used for printing
    resultsForPrinting.clear ( ) ;
    int actualMaxNeededWidth = 0 ;
    int actualWidth = 0 ;
    // int actualPosiotion=0;
    PrettyCharIterator it = this.prettyString.toCharacterIterator ( ) ;
    int j = 0 ;
    int w = 0 ;
    for ( char c = it.first ( ) ; c != CharacterIterator.DONE ; c = it.next ( ) , j ++ )
    {
      // Find out the width of the actual char
      w = 0 ;
      switch ( it.getStyle ( ) )
      {
        case IDENTIFIER :
          w += AbstractRenderer.identifierFontMetrics.charWidth ( c ) ;
          break ;
        case NONE :
          w += AbstractRenderer.expFontMetrics.charWidth ( c ) ;
          break ;
        case KEYWORD :
          w += AbstractRenderer.keywordFontMetrics.charWidth ( c ) ;
          break ;
        case CONSTANT :
          w += AbstractRenderer.constantFontMetrics.charWidth ( c ) ;
          break ;
        case COMMENT :
          break ;
        case TYPE :
          w += AbstractRenderer.typeFontMetrics.charWidth ( c ) ;
          break ;
      }
      // width of the expression is
      actualWidth = actualWidth + w ;
      actualMaxNeededWidth = Math.max ( actualWidth , actualMaxNeededWidth ) ;
      
      // The expression has grwon to big
      // the programm will find the very next brakepoint to the actual position
      // being smaller then it. If there is no possible breakpoint, it will
      // force to break at this position
      if ( actualWidth > maxWidth )
      {
      	if (j < 2)
      	{
      		//TODO Der Fall, dass die Größe kleiner ist als die Ziechen, dann klappt das nicht...
      		return new Dimension (0,0);
      	}
      	// use the next breakpoint bevor the actual position. If there is no breakpoint
      	// a breakpoint will be inserted
        if ( allBreakPoints.size ( ) == 0 )  //no brakpoint available
        {
        	//use the actual position -1
          //useBreakPoints.add (Math.max(0, j - 1) ) ;
        	useBreakPoints.add (j - 1);
          j -- ;
          it.setIndex(j - 1);
        }
        else if (allBreakPoints.get(0) > j - 1) // The first breakpoint is after the actual position
				{
					// TODO Das muss noch ausgibig getestet werden...
					// Vergleich mit der Repository-Version!!!
					//System.out.println("So, das ist scheiße bis hier...");
					
					useBreakPoints.add(j - 1);
					j--;
					it.setIndex((j - 1));
				}
        else
        {
          for ( int i = 1 ; i < allBreakPoints.size ( ) ; i ++ )
          {
            if ( allBreakPoints.get ( i ).intValue ( ) > j )
            {
            	//i is after the actual position, check, if i-1 is already used
              if ( ! useBreakPoints.contains ( allBreakPoints.get ( i - 1 ) ) )
              {
                //so use it
                useBreakPoints.add ( allBreakPoints.get ( i - 1 ) ) ;
                //go back to the breakpoint
                it.setIndex ( allBreakPoints.get ( i - 1 ) ) ;
                j = allBreakPoints.get ( i - 1 ) ;
              }
              // i is allready in use, so ther will be a breakpoint
              else
              {
                useBreakPoints.add ( j - 1 ) ;
                j -- ;
                it.setIndex ( ( j - 1 ) ) ;
              }
              break ;
            }
          }
        }
        // next line will start with width 0
        actualWidth = 0 ;
      }
    }

    this.result = new CheckerResult ( ) ;
    this.result.rows = 0 ;
    this.result.sizeOfResult.height = 0 ;
    this.result.sizeOfResult.width = actualMaxNeededWidth ;

    for ( Integer i : useBreakPoints )
    {
      this.result.breakOffsets.add ( i ) ;
    }
    
    this.result.sizeOfResult.height = AbstractRenderer.getAbsoluteHeight() * ( result.breakOffsets.size ( ) + 2 ) ;
    this.result.rows = result.breakOffsets.size ( ) + 1 ;
    return ( this.result.sizeOfResult ) ;
  }


  /**
   * Checks the results for all possible annotations.
   */
  public void checkLinewraps ( )
  {
    this.results.clear ( ) ;
    this.results.add ( checkLinewrap ( null ) ) ;
    for ( PrettyAnnotation annotation : this.prettyString.getAnnotations ( ) )
    {
      this.results.add ( checkLinewrap ( annotation ) ) ;
    }
  }


  /**
   * Checks the size that would be needed when the expression would be rendered
   * using the given Annotation.
   * 
   * @param annotation
   * @return
   */
  private CheckerResult checkLinewrap ( PrettyAnnotation annotation )
  {
    CheckerResult result = new CheckerResult ( ) ;
    result.rows = 1 ;

    if ( annotation != null )
    {
      //for ( int i = 0 ; i < annotation.getBreakOffsets ( ).length ; i ++ )
      	for (int anno : annotation.getBreakOffsets() )
      {
        //result.breakOffsets.add ( annotation.getBreakOffsets ( ) [ i ] ) ;
      		result.breakOffsets.add(anno);
      }
    }

    result.sizeOfResult.height = AbstractRenderer.getAbsoluteHeight();
    
    //System.out.println("Größe: "+AbstractRenderer.fontHeight + ", Leading"+AbstractRenderer.fontLeading);
    
    PrettyCharIterator it = this.prettyString.toCharacterIterator ( ) ;
    int i = 0 ;
    int w = 0 ;
    for ( char c = it.first ( ) ; c != CharacterIterator.DONE ; c = it.next ( ) , i ++ )
    {
      for ( int j = 0 ; j < result.breakOffsets.size ( ) ; j ++ )
      {
        if ( result.breakOffsets.get ( j ).intValue ( ) == i )
        {
          // System.out.println("Treffer, die Zeilen werden um eins erhöht!");
          result.sizeOfResult.height += AbstractRenderer.getAbsoluteHeight();
          result.sizeOfResult.width = Math.max ( w , result.sizeOfResult.width ) ;
          result.rows ++ ;
          // let the next line be to big fonts indentated
          w = AbstractRenderer.expFontMetrics.stringWidth ( "GG" ) ;
          break ;
        }
      }
      switch ( it.getStyle ( ) )
      {
        case IDENTIFIER :
          w += AbstractRenderer.identifierFontMetrics.charWidth ( c ) ;
          break ;
        case NONE :
          w += AbstractRenderer.expFontMetrics.charWidth ( c ) ;
          break ;
        case KEYWORD :
          w += AbstractRenderer.keywordFontMetrics.charWidth ( c ) ;
          break ;
        case CONSTANT :
          w += AbstractRenderer.constantFontMetrics.charWidth ( c ) ;
          break ;
        case COMMENT :
          break ;
        case TYPE :
          w += AbstractRenderer.typeFontMetrics.charWidth ( c ) ;
          break ;
      }
      // System.out.println("Rows: "+result.rows);
    }
    result.sizeOfResult.width = Math.max ( w , result.sizeOfResult.width ) ;
    // the result contains the size of the whole expression when renderd
    // with the given Annotation
    return result ;
  }


  /*
   * public static boolean isIn (int test, LinkedList <Bonds> list) {
   * //System.out.println("Nun wird �berpr�ft, ob die Zahl in der Liste
   * steht..."); //System.out.println("L�nge der komischen Liste:
   * "+list.size()); boolean result = false; //steht f�r false, positive Werte
   * werden als Position und true missbraucht for (int i=0; i<list.size(); i++) {
   * int min = list.get(i).getStartOffset(); int max =
   * list.get(i).getEndOffset(); //LinkedList<PrettyAnnotation> other =
   * list.get(i).marks; //System.out.println("alles zwischen "+min+" und "+max+ "
   * wird makiert."); //list.get(i). //int tmp = 1;
   * //System.out.println(""+tmp); if ((test <= max) && (test >= min)) { return
   * true; } else { int count=0; LinkedList <PrettyAnnotation> rest =
   * list.get(i).getMarks(); for (int j = 0 ; j<rest.size(); j++) { count++;
   * PrettyAnnotation tmp = rest.get(j); int min1 = tmp.getStartOffset(); int
   * max1 = tmp.getEndOffset(); if ((test <= max1) && (test >= min1)) { return
   * true; } } } } //nur nachsehen, ob diese Methode geht, damit tats�chlich was
   * gemalt wird return result; }
   */
  /**
   * checks, if the int test is in the List list and returns the position
   * 
   * @param test int to finde
   * @param list list to serach in
   * @return int - the position
   */
  public static int isInList ( int test , ArrayList < Bonds > list )
  {
    // System.out.println("Nun wird �berpr�ft, ob die Zahl in der Liste
    // steht...");
    // System.out.println("L�nge der komischen Liste: "+list.size());
    int result = - 1 ;
    for ( int i = 0 ; i < list.size ( ) ; i ++ )
    {
      int min = list.get ( i ).getStartOffset ( ) ;
      int max = list.get ( i ).getEndOffset ( ) ;
      // LinkedList<PrettyAnnotation> other = list.get(i).marks;
      // System.out.println("alles zwischen "+min+" und "+max+ " wird
      // makiert.");
      // list.get(i).
      // int tmp = 1;
      // System.out.println(""+tmp);
      if ( ( test <= max ) && ( test >= min ) )
      {
        return i ;
      }
      else
      {
        ArrayList < PrettyAnnotation > rest = list.get ( i )
            .getPrettyAnnotation ( ) ;
        for ( int j = 0 ; j < rest.size ( ) ; j ++ )
        {
          PrettyAnnotation tmp = rest.get ( j ) ;
          int min1 = tmp.getStartOffset ( ) ;
          int max1 = tmp.getEndOffset ( ) ;
          if ( ( test <= max1 ) && ( test >= min1 ) )
          {
            return i ;
          }
        }
      }
    }
    return result ;
  }


  /**
   * checks if the given int test is the one of the first elements
   * 
   * @param test the int to find
   * @param list the list to search
   * @return true/false
   */
  public static boolean isFirstInListe ( int test , ArrayList < Bonds > list )
  {
    // it is not first in List
    boolean result = false ;
    for ( int i = 0 ; i < list.size ( ) ; i ++ )
    {
      int min = list.get ( i ).getStartOffset ( ) ;
      int max = list.get ( i ).getEndOffset ( ) ;
      // if it is first in List
      if ( ( test <= max ) && ( test >= min ) )
      {
        // return true
        return true ;
      }
    }
    // return false if for was alway wrong
    return result ;
  }
  
  
  /**
   * Dose the same as the render mathode, but corrects the y value to render the prettystring 
   * to the the baseline
   *
   * @param x
   * @param y
   * @param width
   * @param height
   * @param gc
   * @param bound
   * @param toListenForM
   */
  public void renderBase ( int x , int y ,int width , int height,  Graphics gc , ShowBonds bound , ToListenForMouseContainer toListenForM )
  {
    render (x, y-(height / 2) - fontAscent / 2, width, height, gc, bound, toListenForM); 
  }


  /**
   * Renders the Prettystring. <br>
   * <br>
   * The Prettystring will be rendered verticaly centered between the position y
   * and (y + height).
   * 
   * @param x Left position where the rendering should take place
   * @param y Top position where the rendering should take place
   * @param height The height that is available for the rendering.
   * @param width The width that is available for the rendering.
   * @param gc The Graphics context that will be used to render
   * @param bound
   * @param toListenForM
   * @return The width of the expression will get returned.
   */
  public void render ( int x , int y ,int width , int height,  Graphics gc , ShowBonds bound , ToListenForMouseContainer toListenForM )
  {
  	toListenForMouse = toListenForM ;
    // get The MousePosition
    Point mousePosition = toListenForMouse.getHereIam ( ) ;
    
    boolean mouseOver = true ;
    
    // System.out.println("mousePostition: "+mousePosition[0]+", "+mousePosition[1]);
    
    if ( mousePosition.x == 0 && mousePosition.y == 0 )
    {
      mouseOver = false ;
    }
    
    // System.out.println("also die Mausposition: "+mouseOver);
    
    // get the Char-Position to the MousePosition
    // count the chars by using the charwidth adding till the mouseposition is
    // found
    // for functioning in more than 1 line the lines are count
    // the breakoffsets to find the line
    
    //check the linewraping
    int arraySize = 0 ;
    if ( result != null )
    {
      arraySize = result.breakOffsets.size ( ) ;
    }
    else 
    {
    	//The expression has no breakpoint!
    	result = new CheckerResult ();
    }
    int [ ] breakOffsets = new int [ arraySize ] ;

    for ( int i = 0 ; i < result.breakOffsets.size ( ) ; i ++ )
    {
      breakOffsets [ i ] = result.breakOffsets.get ( i ).intValue ( ) ;
    }
    
    // System.out.println();
    
    // an Iterator through all chars
    PrettyCharIterator it = this.prettyString.toCharacterIterator ( ) ;
    // find out wher the mousepointer is, at wich char
    
    int charPosition = x ;
    int charIndex = 0 ;
    FontMetrics fm = null ;
    fm = AbstractRenderer.expFontMetrics ;
    // if the cahr is not in the first line the startvalue must be different
    int lineCount = 0 ;
    
    int posY_ = y + height / 2 ;
    //int posY_ = y + AbstractRenderer.getAbsoluteHeight()/2;
    posY_ += AbstractRenderer.fontAscent / 2 ;
    float addY_ = ( this.result.rows - 1 ) / 2.0f ;
    addY_ *= AbstractRenderer.getAbsoluteHeight();
    posY_ -= addY_ ;
    
    // System.out.println("Y-Position: "+posY_);
    
    // mousePosition[1] is the x-coordinate, start to count at 1
    //lineCount = ( ( mousePosition [ 1 ] - ( posY_ - AbstractRenderer.getAbsoluteHeight() ) ) / fm.getHeight ( ) ) + 1 ;
    lineCount = ( ( mousePosition.y - ( posY_ - AbstractRenderer.getAbsoluteHeight() ) ) / AbstractRenderer.getAbsoluteHeight ( ) ) + 1 ;
    
    //System.out.println("Linecount: "+lineCount);
    
    //Testen, ob die Maus wirklich über dem PrittySTring ist...
    boolean highlight = false;
    if ( lineCount >= 1 && lineCount <= height / AbstractRenderer.getAbsoluteHeight() ) //schon mal die richtige Zeile
    {
    	if ( (mousePosition.x > x) && (mousePosition.x <= x + width) && (mousePosition.y > y) && (mousePosition.y <= y + height) )
    	{
    		highlight = true;
    	}
    	
    }   
    
    if ( lineCount > 1 && lineCount <= height / AbstractRenderer.getAbsoluteHeight() )
    {
      
    	// may be the mousepointer is under the expression
      try
      {
        charIndex = breakOffsets [ lineCount - 2 ] ;
      }
      catch ( IndexOutOfBoundsException e )
      {
        
      }
    }

    // add the width of the chars till the mousepointer is reached an dcount the
    // chars. charIdenx will be the index where the mousePointer is over
    for ( char c = it.setIndex ( Math.max ( charIndex , 0 ) ) ; c != CharacterIterator.DONE ; c = it.next ( ) , charIndex++ )
    {
      switch ( it.getStyle ( ) )
      {
        case KEYWORD :
          gc.setFont ( AbstractRenderer.keywordFont ) ;
          gc.setColor ( AbstractRenderer.keywordColor ) ;
           fm = AbstractRenderer.keywordFontMetrics;
          break ;
        case IDENTIFIER :
          gc.setFont ( AbstractRenderer.identifierFont ) ;
          gc.setColor ( AbstractRenderer.identifierColor ) ;
           fm = AbstractRenderer.identifierFontMetrics;
          break ;
        case NONE :
          gc.setFont ( AbstractRenderer.expFont ) ;
          gc.setColor ( AbstractRenderer.expColor ) ;
          fm = AbstractRenderer.expFontMetrics;
          break ;
        case CONSTANT :
          gc.setFont ( AbstractRenderer.constantFont ) ;
          gc.setColor ( AbstractRenderer.constantColor ) ;
           fm = AbstractRenderer.constantFontMetrics;
          break ;
        case COMMENT :
          continue ;
        case TYPE :
          gc.setFont ( AbstractRenderer.typeFont ) ;
          gc.setColor ( AbstractRenderer.typeColor ) ;
           fm = AbstractRenderer.typeFontMetrics;
          break ;
      } 

      int charWidth = fm.stringWidth ( "" + c ) ;
      
      // System.out.print(c);
      
      charPosition = charPosition + charWidth ;
      if ( charPosition > mousePosition.x )
      {
        break ;
      }
    }
    
    // get the annotations
    ArrayList < Bonds > annotationsList = new ArrayList < Bonds > ( ) ;
    if ( mouseOver )
    {
      // System.out.println("Ich rufe die getA auf! ");
      annotationsList = bound.getAnnotations ( ) ;
    }
    //check if the mouse stands on a char which is to highlight
    //rightAnnotationList will be the annotation to underline, if -1 there is no underlining
    int rightAnnotationList = isInList ( charIndex , annotationsList ) ;

    // get the starting offsets x is just the left border
    // y will be the center of the space available minus the
    // propper amount of rows
    int i = 0 ;
    int posX = x ;
    int posY = y + height / 2 ;
    posY += AbstractRenderer.fontAscent / 2 ;
    float addY = ( this.result.rows - 1 ) / 2.0f ;
    addY *= AbstractRenderer.getAbsoluteHeight() ;
    posY -= addY ;
    // start and end position for the underlining (if the pointer is over the
    // button)
    int underlineStart = - 1 ;
    int underlineEnd = - 1 ;
    if ( this.underlineAnnotation != null )
    {
      underlineStart = this.underlineAnnotation.getStartOffset ( ) ;
      underlineEnd = this.underlineAnnotation.getEndOffset ( ) ;
    }
    // now we can start to render the expression
    // everey char
    for ( char c = it.first ( ) ; c != CharacterIterator.DONE ; c = it.next ( ) , i ++ )
    {
      for ( int j = 0 ; j < breakOffsets.length ; j ++ )
      {
        if ( breakOffsets [ j ] == i )
        {
          posY += AbstractRenderer.getAbsoluteHeight() ;
          posX = x ;
        }
      }
      fm = AbstractRenderer.expFontMetrics ;
      int charWidth = fm.stringWidth ( "" + c ) ;
      // just corrects the posY to start at baseline instead of the middel
      //int posYC = posY - ( fm.getHeight ( ) - fm.getDescent ( ) ) ;
      int posYC = posY - ( AbstractRenderer.getAbsoluteHeight ( ) - fm.getDescent ( ) ) ;
      // int charHighth = fm.getHeight();
      // ArrayList <Bonds> annotationsList =
      // instanceOfShowBound.getAnnotations();
      // look for aktual char is in this list (-1 stands for false)
      if ( ! ( toListenForMouse.getMark ( ) )
          && ( isInList ( i , annotationsList ) ) > - 1 )
      {
        // tell mouselistener in CompoundExpression to react at these positions
        // posY dose not stand for the baseline but for the center, so we have
        // to use corrected values for posY
//      he will just react from baseline to upper corner of char, not to
        // lower corner of char
        toListenForMouse.add ( posX - 1,posYC, posX + charWidth + 1,posYC + fm.getAscent ( ) ) ;

      }
      // Wenn gemalt werden soll, also die Maus �ber einem Buchstaben steht
      // Damit die Liste mit jeder neuen Expression neu gesetzt wird, wird in
      // CopoundExpression neu gesetz
      if ( toListenForMouse.getMark ( )
          && isInList ( i , annotationsList ) != - 1 )
      {
        // if the char will be highlited first teh font and color will be set to
        // normal
        // and later it will be overwritten
        gc.setFont ( AbstractRenderer.identifierFont ) ;
        gc.setColor ( AbstractRenderer.identifierColor ) ;
        // if actual char is in the same List as the list in wich the char where
        // MousePointer is
        // the char should be highlightet
        if ( isInList ( i , annotationsList ) == rightAnnotationList && highlight)
        {
          // type highlighted in bold
          fm.getFont ( ).getName ( ) ;
          Font test = new Font ( fm.getFont ( ).getName ( ) , Font.BOLD , fm
              .getFont ( ).getSize ( ) ) ;
          gc.setFont ( test ) ;
          // let font be in right color
          // the first in the list is the Id an should be highlighted in an
          // other color than the Bounded ones
          if ( isFirstInListe ( i , annotationsList ) )
          {
            gc.setColor ( Theme.currentTheme ( ).getBindingIdColor ( ) ) ;
          }
          else
          {
            gc.setColor ( Theme.currentTheme ( ).getBoundIdColor ( ) ) ;
          }
          // underline the actual char
          gc.drawLine ( posX , posY + 1 , posX + charWidth , posY + 1 ) ;
        }
      }
      // manipulating font witch is not marked as bounded
      else
      {
        // select the proppert font and color for the character
        switch ( it.getStyle ( ) )
        {
          case KEYWORD :
            gc.setFont ( AbstractRenderer.keywordFont ) ;
            gc.setColor ( AbstractRenderer.keywordColor ) ;
             fm = AbstractRenderer.keywordFontMetrics;
            break ;
          case IDENTIFIER :
            gc.setFont ( AbstractRenderer.identifierFont ) ;
            gc.setColor ( AbstractRenderer.identifierColor ) ;
             fm = AbstractRenderer.identifierFontMetrics;
            break ;
          case NONE :
            gc.setFont ( AbstractRenderer.expFont ) ;
            gc.setColor ( AbstractRenderer.expColor ) ;
            fm = AbstractRenderer.expFontMetrics;
            break ;
          case CONSTANT :
            gc.setFont ( AbstractRenderer.constantFont ) ;
            gc.setColor ( AbstractRenderer.constantColor ) ;
             fm = AbstractRenderer.constantFontMetrics;
            break ;
          case COMMENT :
            continue ;
          case TYPE :
            gc.setFont ( AbstractRenderer.typeFont ) ;
            gc.setColor ( AbstractRenderer.typeColor ) ;
             fm = AbstractRenderer.typeFontMetrics;
            break ;
        }
      }
      if ( i >= underlineStart && i <= underlineEnd )
      {
        // the current character is in the range, where underlining
        // should happen
        // save the current color, it will become resetted later
        Color color = gc.getColor ( ) ;
        gc.setColor ( AbstractRenderer.underlineColor ) ;
        // draw the line below the character
        // int charWidth = fm.stringWidth("" + c);
        gc.drawLine ( posX , posY + 1 , posX + charWidth , posY + 1 ) ;
        // reset the color for the characters
        gc.setColor ( color ) ;
      }
      if ( this.alternativeColor != null )
      {
        gc.setColor ( this.alternativeColor ) ;
      }
      // draw the character and move the position
      gc.drawString ( "" + c , posX , posY ) ;
      posX += fm.stringWidth ( "" + c ) ;
      // go on to the next character
    }
  }

}
