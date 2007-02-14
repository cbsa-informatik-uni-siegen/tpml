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
   * Initilizes the list.
   */
  public OutlineUnbound ( )
  {
    this.list = new ArrayList < Identifier > ( ) ;
  }


  /**
   * Adds an {@link Identifier} to the list of unbound {@link Identifier}s. At
   * the beginning all {@link Identifier}s are unbound. After that they could
   * be removed.
   * 
   * @param pIdentifier The {@link Identifier} which should be added.
   */
  public final void add ( Identifier pIdentifier )
  {
    this.list.add ( pIdentifier ) ;
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
   * Removes an {@link Identifier} from the list.
   * 
   * @param pIdentifier The {@link Identifier} which should be removed.
   */
  public final void remove ( Identifier pIdentifier )
  {
    boolean removed = false ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      // Have to use '==' and not 'equals()'
      if ( pIdentifier == this.list.get ( i ) )
      {
        this.list.remove ( i ) ;
        removed = true ;
        break ;
      }
    }
    if ( ! removed )
    {
      System.err.println ( "Outline: The Identifier \"" //$NON-NLS-1$
          + pIdentifier.toPrettyString ( ).toString ( )
          + "\" has more than one Expression to which it is bound!" ) ; //$NON-NLS-1$
    }
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
