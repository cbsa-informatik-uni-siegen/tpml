package de.unisiegen.tpml.graphics.outline.binding;


import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType;
import de.unisiegen.tpml.core.types.Type;


/**
 * Finds the unbound {@link ExpressionOrType}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineUnbound
{

  /**
   * The list of unbound {@link ExpressionOrType}s.
   */
  private ArrayList < ExpressionOrType > list;


  /**
   * Initilizes the list with the given list.
   * 
   * @param pList The given {@link ExpressionOrType}s list.
   */
  private OutlineUnbound ( ArrayList < ExpressionOrType > pList )
  {
    this.list = pList;
  }


  /**
   * Initilizes the list.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineUnbound ( Expression pExpression )
  {
    this.list = new ArrayList < ExpressionOrType > ();
    this.list.addAll ( pExpression.getIdentifiersFree () );
    this.list.addAll ( pExpression.getTypeNamesFree () );
  }


  /**
   * Initilizes the list.
   * 
   * @param pType The input {@link Type}.
   */
  public OutlineUnbound ( Type pType )
  {
    this.list = new ArrayList < ExpressionOrType > ();
    this.list.addAll ( pType.getTypeNamesFree () );
  }


  /**
   * Returns the unbound {@link ExpressionOrType}.
   * 
   * @param pIndex The index of the unbound {@link ExpressionOrType}.
   * @return The unbound {@link ExpressionOrType}.
   */
  public final ExpressionOrType get ( int pIndex )
  {
    return this.list.get ( pIndex );
  }


  /**
   * Returns a reduced {@link OutlineUnbound}, which contains only the
   * {@link ExpressionOrType}s which are present in the given
   * {@link ExpressionOrType}.
   * 
   * @param pExpressionOrType The input {@link ExpressionOrType}.
   * @return A reduced {@link OutlineUnbound}, which contains only the
   *         {@link ExpressionOrType}s which are present in the given
   *         {@link ExpressionOrType}.
   */
  public final OutlineUnbound reduce ( ExpressionOrType pExpressionOrType )
  {
    OutlineUnbound result = new OutlineUnbound (
        new ArrayList < ExpressionOrType > ( this.list ) );
    for ( int i = result.list.size () - 1 ; i >= 0 ; i-- )
    {
      try
      {
        pExpressionOrType.toPrettyString ().getAnnotationForPrintable (
            result.list.get ( i ) );
      }
      catch ( IllegalArgumentException e )
      {
        result.list.remove ( i );
        /*
         * Happens if the unbound Identifier or TypeName is not in this node.
         */
      }
    }
    return result;
  }


  /**
   * Returns the size of the {@link ExpressionOrType} list. The size is equal to
   * the number of unbound {@link ExpressionOrType}s.
   * 
   * @return The number of unbound {@link ExpressionOrType}s.
   */
  public final int size ()
  {
    return this.list.size ();
  }
}
