package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


/**
 * In this class the start and the end index of the Identifiers is saved.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTPair
{
  /**
   * The start index.
   * 
   * @see #getStart()
   */
  private int start ;


  /**
   * The end index.
   * 
   * @see #getEnd()
   */
  private int end ;


  /**
   * Initializes the start and end index.
   * 
   * @param pStart
   * @param pEnd
   */
  public ASTPair ( int pStart , int pEnd )
  {
    this.start = pStart ;
    this.end = pEnd ;
  }


  /**
   * Returns the end index.
   * 
   * @return The end index.
   * @see #end
   */
  public int getEnd ( )
  {
    return this.end ;
  }


  /**
   * Returns the start index.
   * 
   * @return The start index.
   * @see #start
   */
  public int getStart ( )
  {
    return this.start ;
  }
}
