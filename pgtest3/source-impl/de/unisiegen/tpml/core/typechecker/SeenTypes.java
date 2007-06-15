package de.unisiegen.tpml.core.typechecker ;


import java.util.ArrayList ;
import java.util.Iterator ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * A list of seen {@link Type}s.
 * 
 * @author Christian Fehler
 * @param <E>
 */
public class SeenTypes < E > implements Iterable < E >
{
  /**
   * TODO
   */
  private ArrayList < E > list ;


  /**
   * TODO
   */
  public SeenTypes ( )
  {
    this.list = new ArrayList < E > ( ) ;
  }


  /**
   * TODO
   * 
   * @param pItem
   */
  public void add ( E pItem )
  {
    this.list.remove ( pItem ) ;
    this.list.add ( 0 , pItem ) ;
  }


  /**
   * TODO
   * 
   * @param pSeenTypes
   */
  public void addAll ( SeenTypes < E > pSeenTypes )
  {
    for ( E item : pSeenTypes )
    {
      this.list.remove ( item ) ;
      this.list.add ( 0 , item ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pItem
   * @return TODO
   */
  public boolean contains ( E pItem )
  {
    return this.list.contains ( pItem ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see Iterable#iterator()
   */
  public Iterator < E > iterator ( )
  {
    return this.list.iterator ( ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder result = new StringBuilder ( ) ;
    result.append ( "[" ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      result.append ( this.list.get ( i ).toString ( ) ) ;
      if ( i < this.list.size ( ) - 1 )
      {
        result.append ( ", " ) ; //$NON-NLS-1$
      }
    }
    result.append ( "]" ) ; //$NON-NLS-1$
    return result.toString ( ) ;
  }
}
