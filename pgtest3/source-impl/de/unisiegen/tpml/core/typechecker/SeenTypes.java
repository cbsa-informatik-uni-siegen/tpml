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
public class SeenTypes < E > implements Cloneable , Iterable < E >
{
  /**
   * The internal list of seen {@link Type}s.
   */
  private ArrayList < E > list ;


  /**
   * Initializes the seen {@link Type}s.
   */
  public SeenTypes ( )
  {
    this.list = new ArrayList < E > ( ) ;
  }


  /**
   * Inserts the given item to the beginning of the list and removes it before.
   * So the new item is only one time in the list.
   * 
   * @param pItem The item to add.
   */
  public void add ( E pItem )
  {
    this.list.remove ( pItem ) ;
    this.list.add ( 0 , pItem ) ;
  }


  /**
   * Inserts the given items to the beginning of the list and remove them
   * before. So the new items are only one time in the list.
   * 
   * @param pSeenTypes The {@link SeenTypes} to add.
   */
  public void addAll ( SeenTypes < E > pSeenTypes )
  {
    for ( int i = pSeenTypes.size ( ) - 1 ; i >= 0 ; i -- )
    {
      E item = pSeenTypes.get ( i ) ;
      this.list.remove ( item ) ;
      this.list.add ( 0 , item ) ;
    }
  }


  /**
   * Returns a shallow copy of this <tt>SeenTypes</tt> instance. (The elements
   * themselves are not copied.)
   * 
   * @return A clone of this <tt>SeenTypes</tt> instance.
   * @see Object#clone()
   */
  @ Override
  public SeenTypes < E > clone ( )
  {
    SeenTypes < E > newSeenTypes = new SeenTypes < E > ( ) ;
    for ( E item : this.list )
    {
      newSeenTypes.add ( item ) ;
    }
    return newSeenTypes ;
  }


  /**
   * Returns <tt>true</tt> if this {@link SeenTypes} contains the specified
   * item.
   * 
   * @param pItem The item whose presence in this list is to be tested.
   * @return <tt>true</tt> if this list contains the specified item.
   */
  public boolean contains ( E pItem )
  {
    return this.list.contains ( pItem ) ;
  }


  /**
   * Returns the element at the specified position in this list.
   * 
   * @param pIndex The index of the element to return.
   * @return The element at the specified position in this list.
   */
  public E get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns an iterator over the elements in this list in proper sequence.
   * 
   * @return An iterator over the elements in this list in proper sequence.
   * @see Iterable#iterator()
   */
  public Iterator < E > iterator ( )
  {
    return this.list.iterator ( ) ;
  }


  /**
   * Returns the number of elements in this list.
   * 
   * @return The number of elements in this list.
   */
  public int size ( )
  {
    return this.list.size ( ) ;
  }


  /**
   * Returns a string representation of this {@link SeenTypes}.
   * 
   * @return A string representation of this {@link SeenTypes}.
   * @see Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder result = new StringBuilder ( ) ;
    result.append ( "[" ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      result.append ( this.list.get ( i ) ) ;
      if ( i < this.list.size ( ) - 1 )
      {
        result.append ( ", " ) ; //$NON-NLS-1$
      }
    }
    result.append ( "]" ) ; //$NON-NLS-1$
    return result.toString ( ) ;
  }
}
