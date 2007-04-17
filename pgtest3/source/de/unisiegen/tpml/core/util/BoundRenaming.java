package de.unisiegen.tpml.core.util ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class BoundRenaming
{
  /**
   * TODO
   */
  private ArrayList < Identifier > negativeList ;


  /**
   * TODO
   */
  public BoundRenaming ( )
  {
    this.negativeList = new ArrayList < Identifier > ( ) ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   */
  public final void add ( final ArrayList < Identifier > pIdentifiers )
  {
    this.negativeList.addAll ( pIdentifiers ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   */
  public final void add ( final Identifier pId )
  {
    this.negativeList.add ( pId ) ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   */
  public final void add ( final Identifier [ ] pIdentifiers )
  {
    for ( Identifier element : pIdentifiers )
    {
      this.negativeList.add ( element ) ;
    }
  }


  /**
   * TODO
   */
  public final void clear ( )
  {
    this.negativeList.clear ( ) ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   * @return TODO
   */
  public final boolean contains ( final ArrayList < Identifier > pIdentifiers )
  {
    return this.negativeList.containsAll ( pIdentifiers ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   * @return TODO
   */
  public final boolean contains ( final Identifier pId )
  {
    return this.negativeList.contains ( pId ) ;
  }


  /**
   * TODO
   * 
   * @param pOldIdentifier TODO
   * @return TODO
   */
  public final Identifier newId ( final Identifier pOldIdentifier )
  {
    Identifier newIdentifier = new Identifier ( pOldIdentifier.getName ( ) ) ;
    while ( this.negativeList.contains ( newIdentifier ) )
    {
      newIdentifier = new Identifier ( newIdentifier.getName ( ) + "'" ) ; //$NON-NLS-1$
    }
    return newIdentifier ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   */
  public final void remove ( final ArrayList < Identifier > pIdentifiers )
  {
    while ( this.negativeList.removeAll ( pIdentifiers ) )
    {
      // Remove all Identifiers with the same name
    }
  }


  /**
   * TODO
   * 
   * @param pId TODO
   */
  public final void remove ( final Identifier pId )
  {
    while ( this.negativeList.remove ( pId ) )
    {
      // Remove all Identifiers with the same name
    }
  }
}
