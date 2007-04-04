  package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
 * Subclass of the {@link AbstractRenderer} providing the rendering of 
 * a {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}
 * @author marcell
 * @author michael
 *
 */
public class PrettyStringRenderer extends AbstractRenderer {
  
  /**
   * Inner private class the store information for the line wrapping
   * 
   * @author marcell
   * @author michael
   *
   */
//  private class CheckerResult_old {
    /// The annotation used for the linewrapping
//    public PrettyAnnotation   annotation;
//    /// The rows used to 
//    public int                rows;
//    public Dimension          size;
//   public CheckerResult_old() {
//      this.size = new Dimension ();
//      
//    }
//  }
  
  private class CheckerResult {
    /**
     * list of break Offsets
     */
    /// The annotation used for the linewrapping
    //public PrettyAnnotation   annotation;
    public ArrayList <Integer> breakOffsets;
    /**
     * row-counts
     */
    /// The rows used to 
    public int                rows;
    /**
     * the needed size
     */
    public Dimension          size;
    /**
     * constructor
     *
     */
    public CheckerResult() {
      this.size = new Dimension ();
      this.breakOffsets =  new ArrayList <Integer> ();
    }
  }
  
  /**
   * Results containg the information of all possible 
   * linewrappings.
   */
  private LinkedList<CheckerResult>   results;
  
  /**
   * Results containg the information of all needed Linewarps to fit on
   * paper.
   */
  private LinkedList<CheckerResult>   resultsForPrinting;

  /**
   * The pretty string that should be rendered.
   */
  private PrettyString                prettyString;
  
  /**
   * The result of all results that actually will be used
   * during the rendering. If <i>result</i> is <i>null</i> 
   * no line wrapping is done.
   */
  private CheckerResult               result;
  
  //private ArrayList <CheckerResult> resultsWillBeUsed = new ArrayList <CheckerResult>();
  
  /**
   * The pretty printable that will be underline during the rendering.
   */
  private PrettyPrintable             underlinePrettyPrintable;
  
  
  /**
   * saves the positions where the mouse will react
   */
  private ToListenForMouseContainer         toListenForMouse;
  
  /**
   * The annotation containing the information where the underline 
   * appears within the string.
   */
  private PrettyAnnotation            underlineAnnotation;
  
  /**
   * constructor
   *
   */
  public PrettyStringRenderer() {
    this.results                  = new LinkedList<CheckerResult>();
    this.resultsForPrinting       = new LinkedList<CheckerResult>();
    
    this.result                   = null;
    this.underlinePrettyPrintable = null;
    this.underlineAnnotation      = null;
  }
  
  /**
   * Sets the PrettyString 
   * 
   * @param prettyString
   */
  public void setPrettyString (PrettyString prettyString) {
    this.prettyString = prettyString;
    
    if (this.prettyString != null && this.underlinePrettyPrintable != null) {
      this.underlineAnnotation = this.prettyString.getAnnotationForPrintable(this.underlinePrettyPrintable);
    }
    else {
      this.underlineAnnotation = null;
    }
    
    checkLinewraps ();
  }
  
  
  /**
   * Sets the prettyPrintable that should be underlined.
   * @param prettyPrintable
   */
  public void setUndelinePrettyPrintable (PrettyPrintable prettyPrintable) {
    this.underlinePrettyPrintable = prettyPrintable;
  
    if (this.prettyString != null && this.underlinePrettyPrintable != null) {
      this.underlineAnnotation = this.prettyString.getAnnotationForPrintable(this.underlinePrettyPrintable);
    }
    else {
      this.underlineAnnotation = null;
    }
  }

  
  /**
   * Checks which of the previously calculated result (by checklinewrap) is the best
   * match for the given maxWidth.<br>
   * <br>
   * The size of the widest result that still fits in the maxWidth
   * will be returned. If no result is stored that fits in the
   * smallest size is returned; in this case the returned size
   * is wider than the given maxWidth.
   * 
   * 
   * @param maxWidth The maximum available size for this expression. 
   * @return The dimensions that will be needed to perform the rendering
   * later.
   */
  public Dimension getNeededSize (int maxWidth) {

    // Find the biggest result, that is smaller than the given
    // maxWidth. If none could be found the smllest result available
    // will be used.
    CheckerResult smallestResult = null;
    CheckerResult biggestResult = null;
    
    for (CheckerResult r : this.results) {
      if (smallestResult == null || smallestResult.size.width > r.size.width) {
        smallestResult = r;
      }

      if (r.size.width < maxWidth) {
        if (biggestResult == null || biggestResult.size.width < r.size.width) {
          biggestResult = r;
        }
      }
    }
    if (biggestResult != null)  {
      this.result = biggestResult;
    }
    else {
      this.result = smallestResult;
    }
    
    return this.result.size;
  }
  
  /**
   * Checks which of the previously calculated results will be used
   * to match the given maxWidth.<br>
   * <br>
   * 
   * @param maxWidth The maximum available size for this expression. 
   * @return The dimensions that will be needed to perform the rendering
   * later.
   */
  public Dimension getNeededSizeAll_(int maxWidth) {
  	//JAVA makes teh Window very samll at first (-56)
  	//catch this 
  	if (maxWidth <0)
  	{
  		//TODO Testausgabe
  		//System.out.println("Dämlicher Sonderfall");
  		return new Dimension (0,0);
  	}
  	
  	//first, find out if the normal algorithem
  	//is ok for rendering, then use it
  	Dimension dimOfOne=getNeededSize(maxWidth);
  	if (dimOfOne.width <= maxWidth)
  	{
  		return dimOfOne;
  	}
  	
  	//else find out everey needed preakpoint
  	  	
  	//get all Breakpoints
  	ArrayList <Integer> allBreakPoints = new ArrayList <Integer>();
  	for (int i=0; i<results.size(); i++)
  	{
  		for (int j = 0; j<results.get(i).breakOffsets.size(); j++)
  			{
  				allBreakPoints.add(results.get(i).breakOffsets.get(j));
  			}
  	}
  	
  	//TODO Testausgabe
  	//for (Integer i : allBreakPoints)
  	//{
  	//	System.out.println(i);
  	//}
  	//sort all Breakpoints them
  	Collections.sort(allBreakPoints);
  	
  	//TODO Testausgabe
  	//for (Integer i : allBreakPoints)
  	//{
  	//	System.out.println(i);
  	//}
  	
  	//die benutzen merken
  	ArrayList <Integer> useBreakPoints = new ArrayList <Integer>();

    //these results will be used for printing
    resultsForPrinting.clear();
    //resultsForPrinting.addAll(results);
    //TODO Testausgabe
    //System.out.println("Maximale Breite: "+maxWidth);
    int actualMaxNeededWidth = 0;
    int actualWidth=0;
    //int actualPosiotion=0;
    PrettyCharIterator it = this.prettyString.toCharacterIterator();
   
    int j = 0;
    int w = 0;
    for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), j++) 
    {
    	//TODO Testausgabe
    	//System.out.print(c);
    
    	//Find out the width of the actual char
    	w = 0;
    	
    	switch (it.getStyle()) {
        case IDENTIFIER:
        case NONE:
          w += AbstractRenderer.expFontMetrics.charWidth(c);
          break;
        case KEYWORD:
          w += AbstractRenderer.keywordFontMetrics.charWidth(c);
          break;
        case CONSTANT:
          w += AbstractRenderer.constantFontMetrics.charWidth(c);
          break;
        case COMMENT:
          break;
        case TYPE:
          w += AbstractRenderer.typeFontMetrics.charWidth (c);
          break;
        }
    	
    	//width of the expression is
    	actualWidth = actualWidth + w;
    	//TODO Testausgabe
    	//System.out.println(","+j+" => "+w + "  Gesamtbreite: "+actualWidth);
    	actualMaxNeededWidth = Math.max(actualWidth, actualMaxNeededWidth);
    	
    	//The expression has grwon to big
    	//the programm will find the very next brakepoint to the actual position
    	//being smaller then it. If there is no possible breakpoint, it will force to
    	//break at this position
    	if (actualWidth > maxWidth)
    	{
    		//TODO Testausgabe
    		//System.out.println("Jetzt ist die Breite größer der Maxbreite: "+actualWidth+", "+maxWidth);
    		//System.out.println("Die Position ist: "+j);
    		//wir brauchen den Breakpoint, der am nähsten an dieser stelle liegt, und kleiner ist
    		//gibt es keinen, dann brechen wir einfach hart hier um.
    		
    		if (allBreakPoints.size() < 1)
    		{
    			//TODO Testausgabe
    			//System.out.println("###################### KEINE BREAKPOINTS #####################");
    			useBreakPoints.add(j-1);
    			j--;
    			it.setIndex((j-1));
    		}
    		else if (allBreakPoints.get(0) > j-1)
    		{
    			//TODO Testausgabe
    			//System.out.println("********************** komische Sache *********************");
    			//System.out.println("Der erste Umbruchpunkt liegt bei: "+allBreakPoints.get(0) + ", wir müssen aber bis "+(j-1)+"bereits umgebrochen haben...");
    			useBreakPoints.add(j-1);
    			j--;
    			it.setIndex((j-1));
    		}
    		else
    		{
    			for (int i = 1; i<allBreakPoints.size(); i++)
      		{
      			//System.out.println("Der Vergleiche: "+allBreakPoints.get(i).intValue()+ " > " +j + " = "+ (allBreakPoints.get(i).intValue() > j));
      			if (allBreakPoints.get(i).intValue() > j)
      			{
      				
      				if (!useBreakPoints.contains(allBreakPoints.get(i-1)))
      				{
      					//TODO Testausgabe	
      					//System.out.println("Es wird folgende Stelle verwendet: "+allBreakPoints.get(i-1));
      					useBreakPoints.add(allBreakPoints.get(i-1));
      					//Wir wollen da weiter machen, wo wir umbrechen
      					it.setIndex(allBreakPoints.get(i-1));
      					j=allBreakPoints.get(i-1);
      				}
      				else
      				{
      					//TODO Testausgabe
      					//System.out.println("Dieser Umbruchpunkt wird leider schon verwendet... jetzt müssen wir auch zwangsweise umbrehcen!!!");
      					useBreakPoints.add(j-1);
          			j--;
          			it.setIndex((j-1));
      				}
      				
      				break;
      			}
      		}
	
    		}
    		
    		
    		//next line will start with width 0
    		actualWidth = 0;
    	}
    	
    }
//    for (int i = 0; i<results.size(); i++)
//    {
//    	System.out.println("Breite: "+results.get(i).size.width + ", bis her sind wie bei "+actualWidth);
//    	actualWidth = actualWidth + results.get(i).breakOffsets.get(0);
//    	if (actualWidth >maxWidth)
//    	{
//    		resultsForPrinting.add(results.get(i));
//    		actualWidth = 0;
//    	}
//    	
//    }
    
//    for (CheckerResult r : this.results) {
//      if (smallestResult == null || smallestResult.size.width > r.size.width) {
//        smallestResult = r;
//      }
//
//      if (r.size.width < maxWidth) {
//        if (biggestResult == null || biggestResult.size.width < r.size.width) {
//          biggestResult = r;
//        }
//      }
//    }
//    if (biggestResult != null)  {
//      this.result = biggestResult;
//    }
//    else {
//      this.result = smallestResult;
//    }
    this.result = new CheckerResult();
    this.result.rows=0;
    this.result.size.height = 0;
    //this.result.size.width = results.get(0).size.width;
    this.result.size.width = actualMaxNeededWidth;
    //this.result.size.width = maxWidth;
    
    
    
    
    //for (CheckerResult r : this.resultsForPrinting) {
    //TODO Testausgabe
    //System.out.print("Es werden die Umbrüche verwendet: ");
    for (Integer i : useBreakPoints)
    	//if (r.breakOffsets.size()>0)
    {
    		//System.out.println(r.breakOffsets.size());
    		//System.out.println(this.result.breakOffsets.size());
    	//TODO Testausgabe
    	//System.out.print(i+", " );
    	this.result.breakOffsets.add(i);
    		//this.result.size.height=result.size.height+r.size.height;
    	  //this.result.rows = this.result.rows+r.rows;		
    }
    
    //this.result.size.height=this.result.size.height/2;
    this.result.size.height = fontHeight*(result.breakOffsets.size() +2);
    this.result.rows = result.breakOffsets.size() +1;

    //TODO Testausgabe
    //System.out.println("Rows: "+result.rows+" Umbrüche: "+result.breakOffsets.size());
    return (this.result.size);
  }


  /**
   * Checks the results for all possible annotations.
   */
  public void checkLinewraps () {
    this.results.clear();
    
    this.results.add(checkLinewrap (null));
    
    for (PrettyAnnotation annotation : this.prettyString.getAnnotations()) {
      this.results.add(checkLinewrap (annotation));
    }
    
    //TODO Nur ein Test
    //resultsWillBeUsed.clear();
    //resultsWillBeUsed.addAll(results);
    
  }

  /**
   * Checks the size that would be needed when the expression 
   * would be rendered using the given Annotation.
   * 
   * @param annotation 
   * @return
   */
  private CheckerResult checkLinewrap (PrettyAnnotation annotation) {
    
    CheckerResult result = new CheckerResult ();
    result.rows = 1;
    
    //int[] breakOffsets = null;
    
    //Wir wollen ja alle haben...
    //if (annotation != null) {
    //  breakOffsets = annotation.getBreakOffsets();
    //}
    //else {
    //  breakOffsets = new int[0];
    //}
    
    //result.annotation = annotation;
    if (annotation != null)
    {
    	for (int i = 0; i<annotation.getBreakOffsets().length; i++)
      {
      	result.breakOffsets.add(annotation.getBreakOffsets()[i]);
      }
    }
    

    
    //TODO Hier steht die tolle Höhe vom Ausdruck, gefunden!
    //Testen, ob es reicht, die anzahl der Umbrüche zu zählen und mit der Schrifgröße zu multiplizieren...
    //result.size.height = AbstractRenderer.fontHeight;
    //das muss auch einfacher gehen
    //ArrayList <Integer> tmp = new ArrayList<Integer>();
    //for (int i = 0; i<results.size(); i++)
  	//{
  	//	if (results.get(i).annotation != null)
  	//	{
  	//		int [] bO = results.get(i).annotation.getBreakOffsets();
    //		for (int j = 0; j<bO.length; j++)
    //		{
    //			Integer t = new Integer(bO[j]);
    //			tmp.add(t);
    //		}
  	//	}		
  	//}
  	//wir brauchen alle Rows
  	//int rowsAll = 0;
  	//for (int i = 0; i<results.size(); i++)
  	//{
  	//	if (results.get(i).annotation != null)
  	//	{
  	//		rowsAll = rowsAll+results.get(i).rows;
  	//	}		
  	//}
  	//System.out.println("RowsAll: "+rowsAll);
  	
  	//pakcen wir das alles wieder in das Array rein...
  	//  packen wir diese Scheiße in ein Array
//  	breakOffsets = new int [tmp.size()];
//  	for (int i = 0; i<tmp.size(); i++)
//  	{
//  		
//  		breakOffsets[i] =  tmp.get(i).intValue();
//  		System.out.println("Umbruchstellenindex: "+breakOffsets[i]);
//  	}
  	
  	
  	//
    //result.size.height = AbstractRenderer.fontHeight * rowsAll ;
  	result.size.height = AbstractRenderer.fontHeight;
    
    PrettyCharIterator it = this.prettyString.toCharacterIterator();
    int i = 0;
    int w = 0;
    for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
      
      for (int j=0; j<result.breakOffsets.size(); j++) {
        if (result.breakOffsets.get(j).intValue() == i) {
        	//System.out.println("Treffer, die Zeilen werden um eins erhöht!");
          result.size.height += AbstractRenderer.fontHeight;
          result.size.width = Math.max(w, result.size.width);
          result.rows++;
          
          // let the next line be to big fonts indentated
          w = AbstractRenderer.expFontMetrics.stringWidth("GG");
          break;
        }
      }
      
      switch (it.getStyle()) {
      case IDENTIFIER:
      case NONE:
        w += AbstractRenderer.expFontMetrics.charWidth(c);
        break;
      case KEYWORD:
        w += AbstractRenderer.keywordFontMetrics.charWidth(c);
        break;
      case CONSTANT:
        w += AbstractRenderer.constantFontMetrics.charWidth(c);
        break;
      case COMMENT:
        break;
      case TYPE:
        w += AbstractRenderer.typeFontMetrics.charWidth (c);
        break;
      }
      //System.out.println("Rows: "+result.rows);

    }
    result.size.width = Math.max(w, result.size.width);
    //the result contains the size of the whole expression when renderd 
    //with the given Annotation
    return result;
  }
  

/*
  public static boolean isIn (int test, LinkedList <Bonds> list)
  {
    //System.out.println("Nun wird �berpr�ft, ob die Zahl in der Liste steht...");
    //System.out.println("L�nge der komischen Liste: "+list.size());
    boolean result = false; //steht f�r false, positive Werte werden als Position und true missbraucht
    
    for (int i=0; i<list.size(); i++)
    {
      int min = list.get(i).getStartOffset();
      int max = list.get(i).getEndOffset();
      
      //LinkedList<PrettyAnnotation> other = list.get(i).marks;
      //System.out.println("alles zwischen "+min+" und "+max+ " wird makiert.");
      //list.get(i).
      //int tmp = 1;
      //System.out.println(""+tmp);
      if ((test <= max) && (test >= min)) 
        {
          return true;
        }
      else
      {
        int count=0;
        LinkedList <PrettyAnnotation> rest = list.get(i).getMarks();
        for (int j = 0 ; j<rest.size(); j++)
        {
          count++;
          PrettyAnnotation tmp = rest.get(j);
          int min1 = tmp.getStartOffset();
          int max1 = tmp.getEndOffset();
          if ((test <= max1) && (test >= min1)) 
          {
            return true;
          }
        }
      }
    }
    //nur nachsehen, ob diese Methode geht, damit tats�chlich was gemalt wird
    return result;
  }*/
  
  /**
   * checks, if the int test is in the List list and returns the position
   *
   * @param test	int to finde
   * @param list	list to serach in
   * @return			int - the position
   */
  
  public static int isInList (int test, ArrayList <Bonds> list)
  {
    //System.out.println("Nun wird �berpr�ft, ob die Zahl in der Liste steht...");
    //System.out.println("L�nge der komischen Liste: "+list.size());
    int result = -1;
    
    for (int i=0; i<list.size(); i++)
    {
      int min = list.get(i).getStartOffset();
      int max = list.get(i).getEndOffset();
      
      //LinkedList<PrettyAnnotation> other = list.get(i).marks;
      //System.out.println("alles zwischen "+min+" und "+max+ " wird makiert.");
      //list.get(i).
      //int tmp = 1;
      //System.out.println(""+tmp);
      if ((test <= max) && (test >= min)) 
        {
          return i;
        }
      else
      {
        ArrayList <PrettyAnnotation> rest = list.get(i).getMarks();
        for (int j = 0 ; j<rest.size(); j++)
        {
          PrettyAnnotation tmp = rest.get(j);
          int min1 = tmp.getStartOffset();
          int max1 = tmp.getEndOffset();
          if ((test <= max1) && (test >= min1)) 
          {
            return i;
          }
        }
      }
    }
    return result;
  }
  
  /**
   * checks if the given int test is the one of the first elements
   *
   * @param test	the int to find
   * @param list	the list to search
   * @return			true/false
   */
  public static boolean isFirstInListe (int test, ArrayList <Bonds> list)
  {
    //it is not first in List
    boolean result = false;
    
    for (int i=0; i<list.size(); i++)
    {
      int min = list.get(i).getStartOffset();
      int max = list.get(i).getEndOffset();
      
      //if it is first in List
      if ((test <= max) && (test >= min)) 
        {
        //return true 
        return true;
        }
    }
    //return false if for was alway wrong
    return result;
  }

  
  
  
  
  /**
   * Renders the Prettystring.
   * <br><br>
   * The Prettystring will be rendered verticaly centered between the
   * position y and (y + height).
   * 
   * @param x Left position where the rendering should take place
   * @param y Top position where the rendering should take place
   * @param height The height that is available for the rendering.
   * @param gc The Graphics context that will be used to render
   * @param bound 
   * @param toListenForM 
   * @return The width of the expression will get returned.
   */
  public void render (int x, int y, int height,  Graphics gc, ShowBonds bound, ToListenForMouseContainer toListenForM) {
    
    toListenForMouse = toListenForM;
    
    //TODO Das muss noch sch�ner weerden, diese Forschleife ersetzt vern�nftige Logik leider nicht...
    for (int schei = 0; schei<=1 ; schei++)
    {
     
    //mal gucken, dass wir hier alle aus den ergbenissen verwerten können...	
    //int[] breakOffsets = null;
    //if (this.result.annotation != null) {
    //  breakOffsets = this.result.annotation.getBreakOffsets();
    //}
    //else {
    //  breakOffsets = new int[0];
    //}
    
    	//liste, die die breakOffsets zwischenspeichert...
   // 	ArrayList <Integer> tmp = new ArrayList <Integer>();
    	//TODO Testausgabe
    	//System.out.println("Umbruchstellen: "+result.breakOffsets.size());
    	
    	//breakoffsetst zusammenbauen
//    	for (int i = 0; i<results.size(); i++)
//    	{
//    		if (results.get(i).annotation != null)
//    		{
//    			int [] bO = results.get(i).annotation.getBreakOffsets();
//      		for (int j = 0; j<bO.length; j++)
//      		{
//      			Integer t = new Integer(bO[j]);
//      			tmp.add(t);
//      		}
//    		}		
//    	}
    	//wir brauchen alle Rows
//    	int rowsAll = result.breakOffsets.size();
//    	for (int i = 0; i<results.size(); i++)
//    	{
//    		if (results.get(i).annotation != null)
//    		{
//    			rowsAll = rowsAll+results.get(i).rows;
//    		}		
//    	}
//    	System.out.println("RowsAll: "+rowsAll);
    	
    	
    	//packen wir diese Scheiße in ein Array
    	int arraySize=0;
    	
    	if (result != null)
    	{
    		arraySize = result.breakOffsets.size();
    	}
    	int[] breakOffsets = new int [arraySize];
    	
    	//TODO Testausgabe
    	//System.out.println("----------------------------------------------"+result.breakOffsets.size());
    	//System.out.print("Umbruchstellen: ");
    	for (int i = 0; i<result.breakOffsets.size(); i++)
    	{	
    		breakOffsets[i] =  result.breakOffsets.get(i).intValue();
    		//System.out.print(+breakOffsets[i]+", ");
    	}
    	//System.out.println();
    	
    
    // get the starting offsets x is just the left border
    // y will be the center of the space available minus the
    // propper amount of rows 
    int i = 0;
    int posX = x;
    
    int posY = y + height / 2;
    posY += AbstractRenderer.fontAscent / 2;
    
    //System.out.println("Rows: "+this.result.rows);
    //TODO Testausgabe
    //System.out.println("übegebene Höhe: "+height);
    //System.out.println("errechnete Höhe: "+(AbstractRenderer.fontHeight * result.rows));
    //float addY = (this.result.rows) / 2.0f;
    float addY = (this.result.rows-1) / 2.0f;
    //float addY = (rowsAll - 1) / 2.0f;
    addY *= AbstractRenderer.fontHeight;
    posY -= addY;
    //TODO Testausgabe
    //System.out.println("Position y: "+posY);
    //System.out.println("was ist denn y: "+y);
    
    
    // start and end position for the underlining
    int underlineStart  = -1;
    int underlineEnd    = -1;
    if (this.underlineAnnotation != null) {
      underlineStart  = this.underlineAnnotation.getStartOffset();
      underlineEnd    = this.underlineAnnotation.getEndOffset();
    }
    
    // now we can start to render the expression
    PrettyCharIterator it = this.prettyString.toCharacterIterator();
    for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) 
    {
      for (int j=0; j<breakOffsets.length; j++) 
      {
        if (breakOffsets [j] == i) {
          posY += AbstractRenderer.fontHeight;
          
          posX = x;
        }
      }
      
      FontMetrics fm = null;
      
      fm = AbstractRenderer.expFontMetrics;
      int charWidth = fm.stringWidth("" + c);
      //TODO Testausgaben
      //Debug.out.println("lasss mal die H�he der Schrift sehen: "+(fm.getHeight()-fm.getDescent()), "feivel");
      //just corrects the posY to start at baseline instead of the middel
      int posYC = posY - (fm.getHeight()-fm.getDescent());
      //int charHighth = fm.getHeight();
      
      //Here we get the information where bindings exists in positions
      ShowBonds instanceOfShowBound = bound; 
      ArrayList <Bonds> annotationsList = instanceOfShowBound.getAnnotations();

      //look for aktual char is in this list (-1 stands for false)
      if (!(toListenForMouse.getMark()) && (isInList(i, annotationsList)) > -1)
      {
        //tell mouselistener in CompoundExpression to react at these positions
        //posY dose not stand for the baseline but for the center, so we have to use corrected values for posY
        toListenForMouse.add(posX-1);
        toListenForMouse.add(posX+charWidth+1);
        //toListenForMouse.add(posY-fm.getDescent());
        //he will just react from baseline to upper corner of char, not to lower corner of char
        toListenForMouse.add(posYC);
        toListenForMouse.add(posYC+fm.getAscent());
        //TODO Testausgaben
        //Debug.out.println("Es geht um Char: " + c, "feivel");
        //Debug.out.println("Differenz: " + (posY-(posY+fm.getAscent())) , "feivel");
        //Debug.out.println("H�he: " + (fm.getHeight()) , "feivel");
        //Debug.out.println("Position: " + posX + "," +posY , "feivel");
        //Debug.out.println("Position: " + x + "," +y , "feivel");
        //Debug.out.println("Maus ist: " + toListenForMouse.getHereIam()[0] + "," +toListenForMouse.getHereIam()[1] , "feivel");
        //fm.get
        
        
        //fm.get
        //toListenForMouse.add(posY);
        
        //toListenForMouse.add(posY);
      }
              
      //Wenn gemalt werden soll, also die Maus �ber einem Buchstaben steht
      //Damit die Liste mit jeder neuen Expression neu gesetzt wird, wird in CopoundExpression neu gesetz
      if (toListenForMouse.getMark() && isInList(i, annotationsList) != -1)
        {
      	//if the char will be highlited first teh font and color will be set to normal
      	//and later it will be overwritten
      	gc.setFont(AbstractRenderer.expFont);
        gc.setColor(AbstractRenderer.expColor);
    
        //get X-Pos of MousePointer
        int xPos = toListenForMouse.getHereIam()[0];
        int yPos = toListenForMouse.getHereIam()[1];
          
        //checks if MousePointer stands on the actual char
        //if ( ( (xPos >= posX) && (xPos <= posX+charWidth) ) && ( (yPos >= posY-fm.getDescent()) && (yPos <=posY+fm.getAscent()) ) ) 
          if ( ( (xPos >= posX-1) && (xPos <= posX+charWidth+1) ) && ( (yPos >= posYC) && (yPos <=posYC+fm.getAscent()) ) )
        {
          //TODO in diesem Fall muss er neu anfangen, er darf nicht mit den n�chsten Buchstaben weiter machen...
          
          
          
          // - Vielleicht pr�fen, ob die setRightList schon gesertzt, dann �berspringen, sonst vorner anfangen. also die gleiche Funktion einfach nochmal aufrufen...
          toListenForMouse.setRightList(isInList(i, annotationsList));
    
         }
        
        //if actual char is in the same List as the list in wich the char where MousePointer is
        if (isInList(i, annotationsList) == toListenForMouse.getRightList())
        {
          //Prüfen, ob es der erste in der Liste ist, und dann anders anmalen...
          
          //let font be in right color
          if (isFirstInListe (i, annotationsList))
          {
            gc.setColor(Theme.currentTheme().getIdColor());
          }
          else
          {
            gc.setColor(Theme.currentTheme().getBindingColor());
          }
          
          //underline the actual char
          gc.drawLine(posX, posY + 1, posX + charWidth, posY + 1);
        }
        
      }
      
      //manipulating font witch is not marked as bounded
      else
      {
        //select the proppert font and color for the character
        switch (it.getStyle()) {
        	case KEYWORD:
            gc.setFont(AbstractRenderer.keywordFont);
            gc.setColor(AbstractRenderer.keywordColor);
            //fm = AbstractRenderer.keywordFontMetrics;
            break;
            
        case IDENTIFIER:
        	gc.setFont(AbstractRenderer.expFont);
          gc.setColor(AbstractRenderer.expColor);
          //fm = AbstractRenderer.expFontMetrics;
          break;
        case NONE:
          gc.setFont(AbstractRenderer.expFont);
          gc.setColor(AbstractRenderer.expColor);
          //fm = AbstractRenderer.expFontMetrics;
          break;
        
        case CONSTANT:
          gc.setFont(AbstractRenderer.constantFont);
          gc.setColor(AbstractRenderer.constantColor);
          //fm = AbstractRenderer.constantFontMetrics;
          break;
        case COMMENT:
          continue;
        case TYPE:
          gc.setFont(AbstractRenderer.typeFont);
          gc.setColor(AbstractRenderer.typeColor);
          //fm = AbstractRenderer.typeFontMetrics;
          break;
        
        }
        
      }
      if (i >= underlineStart && i <= underlineEnd) {
        // the current character is in the range, where underlining
        // should happen
        
        // save the current color, it will become resetted later
        Color color = gc.getColor();
        gc.setColor(AbstractRenderer.underlineColor);
        
        
        // draw the line below the character 
        //int charWidth = fm.stringWidth("" + c);
        gc.drawLine(posX, posY + 1, posX + charWidth, posY + 1);
        
        // reset the color for the characters
        gc.setColor(color);
        
      }
      if (this.alternativeColor != null) {
        gc.setColor(this.alternativeColor);
      }
      
      // draw the character and move the position
      gc.drawString("" + c, posX, posY);      
      posX += fm.stringWidth("" + c);
      
      // go on to the next character
    }   
  }
  }
  
}


