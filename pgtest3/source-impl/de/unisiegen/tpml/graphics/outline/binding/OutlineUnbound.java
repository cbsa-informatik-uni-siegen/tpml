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
   * TODO
   * 
   * @param pList TODO
   */
  private OutlineUnbound ( final ArrayList < Identifier > pList )
  {
    this.list = pList ;
  }


  /**
   * Initilizes the list and finds the unbound {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineUnbound ( final Expression pExpression )
  {
    this.list = pExpression.free ( ) ;
  }


  /**
   * Returns the unbound {@link Identifier} in the {@link Expression}.
   * 
   * @param pIndex The index of the unbound {@link Identifier}.
   * @return The unbound {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( final int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @return TODO
   */
  public OutlineUnbound reduce ( final Expression pExpression )
  {
    final OutlineUnbound result = new OutlineUnbound (
        new ArrayList < Identifier > ( this.list ) ) ;
    for ( int i = result.size ( ) - 1 ; i >= 0 ; i -- )
    {
      try
      {
        pExpression.toPrettyString ( ).getAnnotationForPrintable (
            result.get ( i ) ) ;
      }
      catch ( final IllegalArgumentException e )
      {
        result.remove ( i ) ;
        /*
         * Happens if the unbound Identifiers are not in this node.
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
  public final Identifier remove ( final int pIndex )
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
