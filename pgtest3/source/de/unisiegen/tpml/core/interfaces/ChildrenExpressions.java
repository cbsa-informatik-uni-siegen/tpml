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
   */
  public Expression [ ] getExpressions ( ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( ) ;
}
