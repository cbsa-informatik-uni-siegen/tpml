package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * Finds the unbound {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineUnbound
{
  /**
   * The list of unbound {@link Identifier}s in the {@link Expression}.
   */
  private ArrayList < Identifier > list ;


  /**
   * Initilizes the list with the given list.
   * 
   * @param pList The given list.
   */
  private OutlineUnbound ( ArrayList < Identifier > pList )
  {
    this.list = pList ;
  }


  /**
   * Initilizes the list.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineUnbound ( Expression pExpression )
  {
    this.list = pExpression.getIdentifiersFree ( ) ;
  }


  /**
   * Returns the unbound {@link Identifier} in the {@link Expression}.
   * 
   * @param pIndex The index of the unbound {@link Identifier}.
   * @return The unbound {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns a reduces {@link OutlineUnbound}, which contains only the
   * {@link Identifier}s which are present in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return A reduces {@link OutlineUnbound}, which contains only the
   *         {@link Identifier}s which are present in the given
   *         {@link Expression}.
   */
  public OutlineUnbound reduce ( Expression pExpression )
  {
    OutlineUnbound result = new OutlineUnbound ( new ArrayList < Identifier > (
        this.list ) ) ;
    for ( int i = result.size ( ) - 1 ; i >= 0 ; i -- )
    {
      try
      {
        pExpression.toPrettyString ( ).getAnnotationForPrintable (
            result.get ( i ) ) ;
      }
      catch ( IllegalArgumentException e )
      {
        result.remove ( i ) ;
        /*
         * Happens if the unbound Identifier is not in this node.
         */
      }
    }
    return result ;
  }


  /**
   * Removes the {@link Identifier} with the given index.
   * 
   * @param pIndex The index of the unbound {@link Identifier}.
   * @return The removed {@link Identifier}.
   */
  public final Identifier remove ( int pIndex )
  {
    return this.list.remove ( pIndex ) ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of unbound
   * {@link Identifier}s.
   * 
   * @return The number of unbound {@link Identifier}s.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
