package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Expression ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface ChildrenExpressions
{
  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( ) ;


  /**
   * Returns the <code>n</code>th sub expression.
   * 
   * @param pIndex the index of the expression to return.
   * @return the <code>n</code>th sub expression.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( ) ;
}
