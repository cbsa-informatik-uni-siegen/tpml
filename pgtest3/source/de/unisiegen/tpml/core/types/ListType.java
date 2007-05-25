package de.unisiegen.tpml.core.types ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
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
public final class ListType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


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
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>ListType</code> for the monomorphic type
   * <code>tau</code>, which represents the base type of the elements in the
   * list. I.e. if <code>tau</code> is <code>int</code>, the list type is
   * <code>int list</code>.
   * 
   * @param pTau the type for the list elements.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws NullPointerException if <code>pTau</code> is <code>null</code>.
   */
  public ListType ( MonoType pTau , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTau ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ListType clone ( )
  {
    return new ListType ( this.types [ 0 ].clone ( ) ) ;
  }


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
      return this.types [ 0 ].equals ( other.types [ 0 ] ) ;
    }
    return false ;
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
    return this.types [ 0 ] ;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getTypeVariablesFree()
   */
  @ Override
  public Set < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.types [ 0 ].getTypeVariablesFree ( ) ) ;
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( ( this.types [ 0 ].hashCode ( ) + 17 ) * 13 ) / 5 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public ListType substitute ( TypeSubstitution pTypeSubstitution )
  {
    return new ListType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ) ;
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
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LIST_TAU ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addType ( "list" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
