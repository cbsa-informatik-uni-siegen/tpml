package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Instances of this class are used to represent memory locations as returned by
 * the <code>ref</code> operator and used by the <code>:=</code> (Assign)
 * and <code>!</code> (Deref) operators.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Value
 */
public final class Location extends Value
{
  /**
   * The name of the location (uses uppercase letters).
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * Allocates a new <code>Location</code> instance with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the memory location.
   */
  public Location ( final String pName )
  {
    this.name = pName ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Location clone ( )
  {
    return new Location ( this.name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( final Object pObject )
  {
    if ( pObject instanceof Location )
    {
      final Location other = ( Location ) pObject ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Location" ; //$NON-NLS-1$
  }


  /**
   * Returns the name of the memory location.
   * 
   * @return the name of the memory location.
   */
  public String getName ( )
  {
    return this.name ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Location substitute ( final Identifier pId ,
      final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc} For <code>Location</code>s, this method always returns the
   * location itself, because substituting below a location is not possible.
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Location substitute ( @ SuppressWarnings ( "unused" )
  final Identifier pId , @ SuppressWarnings ( "unused" )
  final Expression pExpression , @ SuppressWarnings ( "unused" )
  final boolean pAttributeRename )
  {
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_LOCATION ) ;
      this.prettyStringBuilder.addText ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
