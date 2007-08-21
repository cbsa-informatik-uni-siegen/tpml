package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex instructions.
 * 
 * @author Christian Fehler
 */
public interface LatexInstruction extends Comparable < LatexInstruction >
{
  /**
   * Returns the string value of this <code>LatexInstruction</code>.
   * 
   * @return The string value of this <code>LatexInstruction</code>.
   */
  public String toString ( ) ;
}
