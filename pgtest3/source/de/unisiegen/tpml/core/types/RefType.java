package de.unisiegen.tpml.core.types ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent reference types in our type system.
 * Reference types are types for expressions of type
 * {@link de.unisiegen.tpml.core.expressions.Location} as returned by the
 * {@link de.unisiegen.tpml.core.expressions.Ref} operator.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:345 $
 * @see Location
 * @see Ref
 * @see MonoType
 */
public final class RefType extends MonoType
{
  /**
   * The type of the reference, i.e. <code>int</code> in case of an
   * <code>int ref</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * Allocates a new <code>RefType</code> for the monomorphic type
   * <code>tau</code>. I.e. if <code>tau</code> is <code>bool</code>,
   * the reference type will be <code>bool ref</code>.
   * 
   * @param pTau the monomorphic base type.
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   */
  public RefType ( MonoType pTau )
  {
    if ( pTau == null )
    {
      throw new NullPointerException ( "Tau is null" ) ; //$NON-NLS-1$
    }
    this.tau = pTau ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RefType )
    {
      RefType other = ( RefType ) pObject ;
      return ( this.tau.equals ( other.tau ) ) ;
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
    return "Ref-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the base type of the reference type.
   * 
   * @return the base type.
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
    return ( ( this.tau.hashCode ( ) + 13 ) * 17 ) / 7 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public MonoType substitute ( TypeSubstitution pTypeSubstitution )
  {
    return new RefType ( this.tau.substitute ( pTypeSubstitution ) ) ;
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
          PRIO_REF ) ;
      this.prettyStringBuilder
          .addBuilder ( this.tau
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_REF_TAU ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addType ( "ref" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
