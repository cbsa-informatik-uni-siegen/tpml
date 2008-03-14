package de.unisiegen.tpml.core.util;


import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.interfaces.IdentifierOrTypeName;
import de.unisiegen.tpml.core.types.TypeName;


/**
 * This class implements the bound renaming.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @param <E> The {@link Identifier} or {@link TypeName}.
 */
public final class BoundRenaming < E extends IdentifierOrTypeName >
{

  /**
   * The negative list, containing the {@link Identifier}s or {@link TypeName}s
   * which should not be returned as a new {@link Identifier} or
   * {@link TypeName}.
   */
  private ArrayList < E > negativeList;


  /**
   * Initilizes the negative list.
   */
  public BoundRenaming ()
  {
    this.negativeList = new ArrayList < E > ();
  }


  /**
   * Adds a list of {@link Identifier}s or {@link TypeName}s to the negative
   * list.
   * 
   * @param pIdentifiers The list of {@link Identifier}s or {@link TypeName}s
   *          which should be added to the negative list.
   */
  public final void add ( ArrayList < E > pIdentifiers )
  {
    this.negativeList.addAll ( pIdentifiers );
  }


  /**
   * Adds a {@link Identifier} or {@link TypeName} to the negative list.
   * 
   * @param pId The {@link Identifier} or {@link TypeName} which should be added
   *          to the negative list.
   */
  public final void add ( E pId )
  {
    this.negativeList.add ( pId );
  }


  /**
   * Adds a array of {@link Identifier}s or {@link TypeName}s to the negative
   * list.
   * 
   * @param pIdentifiers The arry of {@link Identifier}s or {@link TypeName}s
   *          which should be added to the negative list.
   */
  public final void add ( E [] pIdentifiers )
  {
    for ( E element : pIdentifiers )
    {
      this.negativeList.add ( element );
    }
  }


  /**
   * Clears the negative list.
   */
  public final void clear ()
  {
    this.negativeList.clear ();
  }


  /**
   * Returns true if the negative list contains the given {@link Identifier} or
   * {@link TypeName}, otherwise false.
   * 
   * @param pId The {@link Identifier} or {@link TypeName}, which should be
   *          tested.
   * @return True if the negative list contains the given {@link Identifier} or
   *         {@link TypeName}, otherwise false.
   */
  public final boolean contains ( E pId )
  {
    return this.negativeList.contains ( pId );
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
  public final Identifier newIdentifier ( Identifier pOldIdentifier )
  {
    Identifier newIdentifier = pOldIdentifier;
    while ( this.negativeList.contains ( newIdentifier ) )
    {
      newIdentifier = new Identifier (
          newIdentifier + "'", newIdentifier.getSet () ); //$NON-NLS-1$
    }
    return newIdentifier;
  }


  /**
   * Returns a new {@link TypeName}, which has the same name like the given old
   * {@link TypeName}, if the negative list does not contain a {@link TypeName}
   * with the same name. Otherwise a {@link TypeName} is returned with the name
   * appending a "'".
   * 
   * @param pOldTypeName The old {@link TypeName}.
   * @return A new {@link TypeName}.
   */
  public final TypeName newTypeName ( TypeName pOldTypeName )
  {
    TypeName newTypeName = pOldTypeName;
    while ( this.negativeList.contains ( newTypeName ) )
    {
      newTypeName = new TypeName ( newTypeName.getName () + "'" ); //$NON-NLS-1$
    }
    return newTypeName;
  }


  /**
   * Removes a {@link Identifier} or {@link TypeName} from the negative list.
   * 
   * @param pId The {@link Identifier} or {@link TypeName} which should be
   *          removed from the negative list.
   */
  public final void remove ( E pId )
  {
    while ( this.negativeList.remove ( pId ) )
    {
      // Remove all Identifiers with the same name
    }
  }
}
