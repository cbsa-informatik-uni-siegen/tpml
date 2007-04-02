package de.unisiegen.tpml.core.types ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent list types in our type system. List types
 * are types for expressions of type {@link List} and {@link EmptyList}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:345 $
 * @see List
 * @see EmptyList
 * @see MonoType
 */
public final class ListType extends MonoType
{
  /**
   * The base type of the list elements.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * Allocates a new <code>ListType</code> for the monomorphic type
   * <code>tau</code>, which represents the base type of the elements in the
   * list. I.e. if <code>tau</code> is <code>int</code>, the list type is
   * <code>int list</code>.
   * 
   * @param pTau the type for the list elements.
   * @throws NullPointerException if <code>pTau</code> is <code>null</code>.
   */
  public ListType ( MonoType pTau )
  {
    if ( pTau == null )
    {
      throw new NullPointerException ( "Tau is null" ) ; //$NON-NLS-1$
    }
    this.tau = pTau ;
  }


  //
  // Base methods
  //
  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ListType )
    {
      ListType other = ( ListType ) pObject ;
      return this.tau.equals ( other.tau ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#free()
   */
  @ Override
  public Set < TypeVariable > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.tau.free ( ) ) ;
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "List-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the base element type.
   * 
   * @return the base element type
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( ( this.tau.hashCode ( ) + 17 ) * 13 ) / 5 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public ListType substitute ( TypeSubstitution pTypeSubstitution )
  {
    return new ListType ( this.tau.substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LIST ) ;
      this.prettyStringBuilder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LIST_TAU ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addType ( "list" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
