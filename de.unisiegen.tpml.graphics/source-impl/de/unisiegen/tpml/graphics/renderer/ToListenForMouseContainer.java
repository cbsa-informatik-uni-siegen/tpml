package de.unisiegen.tpml.graphics.renderer;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;


/**
 * This class is a tool for the mouse over effect of the CompoundExpression.
 * Therefor the mousepositions where the mous over effekt will be aktivated are
 * safed. <br>
 * this class provides the comunication between the PrittyStringRenderer and the
 * CompoundExpression (compoundExpressionTypeInference, TypeCompounent)
 * 
 * @author michael
 * @see de.unisiegen.tpml.graphics.components.CompoundExpression
 * @see de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer
 */
public class ToListenForMouseContainer
{

  /**
   * will contain the positions where bounded identifiers are
   */
  private ArrayList < Rectangle > toListenForMouse;


  /**
   * saves teh position where the mouse pointer is
   */
  private Point hereIam;


  /**
   * shows if the highliter is aktive or not
   */
  private boolean mark;


  /**
   * the constructor. first the highlighting is not activ
   */
  public ToListenForMouseContainer ()
  {
    this.toListenForMouse = new ArrayList < Rectangle > ();
    this.mark = false;
    this.hereIam = new Point ( 0, 0 );
  }


  /**
   * adds a Rectangel given by the four edges.
   * 
   * @param x the x - coordinate left
   * @param y the y - coordinate bottom
   * @param x1 the x - coordinate right
   * @param y1 the y - coordinate top
   */
  public void add ( int x, int y, int x1, int y1 )
  {
    Rectangle r = new Rectangle ( x, y, x1 - x, y1 - y );
    this.toListenForMouse.add ( r );
  }


  /**
   * adds a Rectangel given by the top-left corner, the width and the heigth
   * 
   * @param x the x as <code> int </code> - coordinate of the top left
   * @param y the y as <code> int </code> - coordinate of the top left
   * @param width the width as <code> int </code> of the rectangel
   * @param heigth the heigth as <code> int </code> of the rectangel
   */
  public void addRect ( int x, int y, int width, int heigth )
  {
    Rectangle r = new Rectangle ( x, y, width, heigth );
    this.toListenForMouse.add ( r );
  }


  /**
   * returns one part of a position of ArrayList
   * 
   * @param i <code> int </code> of the value to return
   * @return <code> int </code> with value
   */
  public Rectangle get ( int i )
  {
    Rectangle result;
    result = this.toListenForMouse.get ( i );
    return result;
  }


  /**
   * get the infromation if the ArrayList ist empty or not
   * 
   * @return - <code> boolean </code> , true if empty
   */
  public boolean isEmpty ()
  {
    if ( this.toListenForMouse.size () != 0 )
    {
      return false;
    }
    return true;
  }


  /**
   * clears all entris from ArrayList
   */
  public void reset ()
  {
    this.toListenForMouse.clear ();
  }


  /**
   * @return as <code> int </code> of the size of the ArrayList
   */
  public int size ()
  {
    return this.toListenForMouse.size ();
  }


  /**
   * @return <code> boolean </code>, true if the bindings should be marked
   */
  public boolean isMark ()
  {
    return this.mark;
  }


  /**
   * sets the boolean if it should be marked or not
   * 
   * @param pMark as <code> boolean </code>
   */
  public void setMark ( boolean pMark )
  {
    this.mark = pMark;
  }


  /**
   * sets the position where the pointer is by x-, and y-coordinate
   * 
   * @param x the x-coordinate as <code> int </code> of the mousepointer
   * @param y the y-coordinate as <code> int </code> of the mousepoitner
   */
  public void setHereIam ( int x, int y )
  {
    this.hereIam = new Point ( x, y );
  }


  /**
   * sets the position where the pointer is
   * 
   * @param p the as <code> Point </code>
   */
  public void setHereIam ( Point p )
  {
    this.hereIam = p;
  }


  /**
   * returns the position where the pointer is
   * 
   * @return the saved position where the mouse is
   */
  public Point getHereIam ()
  {
    return this.hereIam;
  }
}
