package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.types.TypeName ;


/**
 * Finds the bound {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @param <E> The {@link Identifier} or {@link TypeName}.
 */
public final class OutlineBinding < E >
{
  /**
   * The list of {@link Identifier}s or {@link TypeName}s, which are bound by
   * the given {@link Identifier} or {@link TypeName} in the given
   * {@link Expression}.
   * 
   * @see #get(int)
   * @see #size()
   */
  private ArrayList < E > list ;


  /**
   * Initilizes the list and sets the bound values.
   * 
   * @param pList The list of bound {@link Identifier}s or {@link TypeName}s.
   */
  public OutlineBinding ( ArrayList < E > pList )
  {
    if ( pList == null )
    {
      this.list = new ArrayList < E > ( ) ;
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
      OutlineBinding < ? > other = ( OutlineBinding ) pObject ;
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
   * Returns the bound {@link Identifier} or {@link TypeName} in the
   * {@link Expression}.
   * 
   * @param pIndex The index of the {@link Identifier} or {@link TypeName}.
   * @return The bound {@link Identifier} or {@link TypeName} in the
   *         {@link Expression}.
   * @see #list
   */
  public final E get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Removes the {@link Identifier} or {@link TypeName} with the given index.
   * 
   * @param pIndex The index of the bound {@link Identifier} or {@link TypeName}.
   * @return The removed {@link Identifier} or {@link TypeName}.
   */
  public final E remove ( int pIndex )
  {
    return this.list.remove ( pIndex ) ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of bound
   * {@link Identifier}s or {@link TypeName}s.
   * 
   * @return The number of {@link Identifier}s or {@link TypeName}.
   * @see #list
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
