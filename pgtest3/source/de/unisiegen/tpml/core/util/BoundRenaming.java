package de.unisiegen.tpml.core.util ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * This class implements the bound renaming.
 * 
 * @author Christian Fehler
 */
public final class BoundRenaming
{
  /**
   * The negative list, containing the {@link Identifier}s which should not be
   * returned as a new {@link Identifier}.
   */
  private ArrayList < Identifier > negativeList ;


  /**
   * Initilizes the negative list.
   */
  public BoundRenaming ( )
  {
    this.negativeList = new ArrayList < Identifier > ( ) ;
  }


  /**
   * Adds a list of {@link Identifier}s to the negative list.
   * 
   * @param pIdentifiers The list of {@link Identifier}s which should be added
   *          to the negative list.
   */
  public final void add ( ArrayList < Identifier > pIdentifiers )
  {
    this.negativeList.addAll ( pIdentifiers ) ;
  }


  /**
   * Adds a {@link Identifier} to the negative list.
   * 
   * @param pId The {@link Identifier} which should be added to the negative
   *          list.
   */
  public final void add ( Identifier pId )
  {
    this.negativeList.add ( pId ) ;
  }


  /**
   * Adds a array of {@link Identifier}s to the negative list.
   * 
   * @param pIdentifiers The arry of {@link Identifier}s which should be added
   *          to the negative list.
   */
  public final void add ( Identifier [ ] pIdentifiers )
  {
    for ( Identifier element : pIdentifiers )
    {
      this.negativeList.add ( element ) ;
    }
  }


  /**
   * Clears the negative list.
   */
  public final void clear ( )
  {
    this.negativeList.clear ( ) ;
  }


  /**
   * Returns true if the negative list contains the given {@link Identifier},
   * otherwise false.
   * 
   * @param pId The {@link Identifier}, which should be tested.
   * @return True if the negative list contains the given {@link Identifier},
   *         otherwise false.
   */
  public final boolean contains ( Identifier pId )
  {
    return this.negativeList.contains ( pId ) ;
  }


  /**
   * Returns a new {@link Identifier}, which has the same name like the given
   * old {@link Identifier}, if the negative list does not contain a
   * {@link Identifier} with the same name. Otherwise a {@link Identifier} is
   * returned with the name appending a "'".
   * 
   * @param pOldIdentifier The old {@link Identifier}.
   * @return A new {@link Identifier}.
   */
  public final Identifier newId ( Identifier pOldIdentifier )
  {
    Identifier newIdentifier = pOldIdentifier ;
    while ( this.negativeList.contains ( newIdentifier ) )
    {
      newIdentifier = new Identifier ( newIdentifier.getName ( ) + "'" ) ; //$NON-NLS-1$
    }
    return newIdentifier ;
  }


  /**
   * Removes a {@link Identifier} from the negative list.
   * 
   * @param pId The {@link Identifier} which should be removed from the negative
   *          list.
   */
  public final void remove ( Identifier pId )
  {
    while ( this.negativeList.remove ( pId ) )
    {
      // Remove all Identifiers with the same name
    }
  }
}
