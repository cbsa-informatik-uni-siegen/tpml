package de.unisiegen.tpml.core.interfaces;


import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Interface for classes whose instances have child {@link Expression}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultExpressions
{

  /**
   * Returns the child {@link Expression}s.
   * 
   * @return the child {@link Expression}s.
   */
  public Expression [] getExpressions ();


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [] getExpressionsIndex ();
}
