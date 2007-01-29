package de.unisiegen.tpml.graphics.outline.binding ;


import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * In this class the start and the end index of the {@link Identifier}s is
 * saved.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlinePair
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
   * Initializes the start and the end index.
   * 
   * @param pStart
   * @param pEnd
   */
  public OutlinePair ( int pStart , int pEnd )
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
  public final int getEnd ( )
  {
    return this.end ;
  }


  /**
   * Returns the start index.
   * 
   * @return The start index.
   * @see #start
   */
  public final int getStart ( )
  {
    return this.start ;
  }
}
