package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * Finds the bounded {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineBinding
{
  /**
   * The list of {@link Identifier}s, which are bounded by the given
   * {@link Identifier} in the given {@link Expression}.
   * 
   * @see #get(int)
   * @see #size()
   */
  private ArrayList < Identifier > list ;


  /**
   * Initilizes the list and sets the bounded values.
   * 
   * @param pList The list of bounded {@link Identifier}s.
   */
  public OutlineBinding ( ArrayList < Identifier > pList )
  {
    if ( pList == null )
    {
      this.list = new ArrayList < Identifier > ( ) ;
    }
    else
    {
      this.list = pList ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof OutlineBinding )
    {
      OutlineBinding other = ( OutlineBinding ) pObject ;
      if ( this.list.size ( ) != other.list.size ( ) )
      {
        return false ;
      }
      for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
      {
        if ( this.list.get ( i ) != other.list.get ( i ) )
        {
          return false ;
        }
      }
      return true ;
    }
    return false ;
  }


  /**
   * Returns the bounded {@link Identifier} in the {@link Expression}.
   * 
   * @param pIndex The index of the {@link Identifier}.
   * @return The bounded {@link Identifier} in the {@link Expression}.
   * @see #list
   */
  public final Identifier get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Removes the {@link Identifier} with the given index.
   * 
   * @param pIndex The index of the bounded {@link Identifier}.
   * @return The removed {@link Identifier}.
   */
  public final Identifier remove ( int pIndex )
  {
    return this.list.remove ( pIndex ) ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of bounded
   * {@link Identifier}s.
   * 
   * @return The number of {@link Identifier}s.
   * @see #list
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
