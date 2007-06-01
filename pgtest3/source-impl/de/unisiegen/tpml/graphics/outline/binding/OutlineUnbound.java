package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Finds the unbound {@link PrettyPrintable}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineUnbound
{
  /**
   * The list of unbound {@link PrettyPrintable}s.
   */
  private ArrayList < PrettyPrintable > list ;


  /**
   * Initilizes the list with the given list.
   * 
   * @param pList The given {@link PrettyPrintable}s list.
   */
  private OutlineUnbound ( ArrayList < PrettyPrintable > pList )
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
    this.list = new ArrayList < PrettyPrintable > ( ) ;
    this.list.addAll ( pExpression.getIdentifiersFree ( ) ) ;
    this.list.addAll ( pExpression.getTypeNamesFree ( ) ) ;
  }


  /**
   * Initilizes the list.
   * 
   * @param pType The input {@link Type}.
   */
  public OutlineUnbound ( Type pType )
  {
    this.list = new ArrayList < PrettyPrintable > ( ) ;
    this.list.addAll ( pType.getTypeNamesFree ( ) ) ;
  }


  /**
   * Returns the unbound {@link PrettyPrintable}.
   * 
   * @param pIndex The index of the unbound {@link PrettyPrintable}.
   * @return The unbound {@link PrettyPrintable}.
   */
  public final PrettyPrintable get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns a reduced {@link OutlineUnbound}, which contains only the
   * {@link PrettyPrintable}s which are present in the given
   * {@link PrettyPrintable}.
   * 
   * @param pPrettyPrintable The input {@link PrettyPrintable}.
   * @return A reduced {@link OutlineUnbound}, which contains only the
   *         {@link PrettyPrintable}s which are present in the given
   *         {@link PrettyPrintable}.
   */
  public OutlineUnbound reduce ( PrettyPrintable pPrettyPrintable )
  {
    OutlineUnbound result = new OutlineUnbound (
        new ArrayList < PrettyPrintable > ( this.list ) ) ;
    for ( int i = result.list.size ( ) - 1 ; i >= 0 ; i -- )
    {
      try
      {
        pPrettyPrintable.toPrettyString ( ).getAnnotationForPrintable (
            result.list.get ( i ) ) ;
      }
      catch ( IllegalArgumentException e )
      {
        result.list.remove ( i ) ;
        /*
         * Happens if the unbound Identifier or TypeName is not in this node.
         */
      }
    }
    return result ;
  }


  /**
   * Returns the size of the {@link PrettyPrintable} list. The size is equal to
   * the number of unbound {@link PrettyPrintable}s.
   * 
   * @return The number of unbound {@link PrettyPrintable}s.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
