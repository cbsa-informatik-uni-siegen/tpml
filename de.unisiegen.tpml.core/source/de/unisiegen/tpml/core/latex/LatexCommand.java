package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex commands.
 * 
 * @author Christian Fehler
 */
public interface LatexCommand extends Comparable < LatexCommand >
{
  /**
   * Returns the string value of this <code>LatexCommand</code>.
   * 
   * @return The string value of this <code>LatexCommand</code>.
   */
  public String toString ( ) ;
}
