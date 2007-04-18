package de.unisiegen.tpml.core.types ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
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
public final class RefType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * TODO
   */
  private MonoType [ ] types ;


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
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RefType clone ( )
  {
    return new RefType ( this.types [ 0 ].clone ( ) ) ;
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
      return ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ;
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
      this.free.addAll ( this.types [ 0 ].free ( ) ) ;
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
    return this.types [ 0 ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   */
  public MonoType getTypes ( int pIndex )
  {
    return this.types [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public String [ ] getTypesPrefix ( )
  {
    String [ ] result = new String [ 1 ] ;
    result [ 0 ] = PREFIX_TAU ;
    return result ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( ( this.types [ 0 ].hashCode ( ) + 13 ) * 17 ) / 7 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public MonoType substitute ( TypeSubstitution pTypeSubstitution )
  {
    return new RefType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ) ;
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
          .addBuilder ( this.types [ 0 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_REF_TAU ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addType ( "ref" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
