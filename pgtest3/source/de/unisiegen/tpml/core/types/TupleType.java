package de.unisiegen.tpml.core.types ;


import java.util.Arrays ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents tuple types in our type system. Tuple types are
 * composed of two or more types, all of which are monomorphic types.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:340 $
 * @see MonoType
 */
public final class TupleType extends MonoType implements DefaultTypes
{
  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * String for the case that the types are null.
   */
  private static final String TYPES_NULL = "types is null" ; //$NON-NLS-1$


  /**
   * String for the case that one type is null.
   */
  private static final String TYPE_NULL = "one type is null" ; //$NON-NLS-1$


  /**
   * String for the case that the types are to small.
   */
  private static final String TO_SMALL = "types must contain at least two items" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = "Tuple-Type" ; //$NON-NLS-1$


  /**
   * The space string.
   */
  private static final String SPACE = " " ; //$NON-NLS-1$


  /**
   * The string <code>*</code>.
   */
  private static final String MULT = "*" ; //$NON-NLS-1$


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private int [ ] indicesType ;


  /**
   * Allocates a new <code>TupleType</code> with the specified
   * <code>types</code>.
   * 
   * @param pTypes the monomorphic types for the tuple elements.
   * @throws IllegalArgumentException if <code>types</code> contains less than
   *           two elements.
   * @throws NullPointerException if <code>pTypes</code> is <code>null</code>.
   */
  public TupleType ( MonoType [ ] pTypes )
  {
    if ( pTypes == null )
    {
      throw new NullPointerException ( TYPES_NULL ) ;
    }
    if ( pTypes.length < 2 )
    {
      throw new IllegalArgumentException ( TO_SMALL ) ;
    }
    for ( MonoType type : pTypes )
    {
      if ( type == null )
      {
        throw new NullPointerException ( TYPE_NULL ) ;
      }
    }
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    for ( int i = 0 ; i < this.indicesType.length ; i ++ )
    {
      this.types [ i ].setParent ( this ) ;
      this.indicesType [ i ] = i + 1 ;
    }
  }


  /**
   * Allocates a new <code>TupleType</code> with the specified
   * <code>types</code>.
   * 
   * @param pTypes the monomorphic types for the tuple elements.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws IllegalArgumentException if <code>types</code> contains less than
   *           two elements.
   * @throws NullPointerException if <code>pTypes</code> is <code>null</code>.
   */
  public TupleType ( MonoType [ ] pTypes , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTypes ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public TupleType clone ( )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].clone ( ) ;
    }
    return new TupleType ( newTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof TupleType )
    {
      TupleType other = ( TupleType ) pObject ;
      return Arrays.equals ( this.types , other.types ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
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
    return this.indicesType ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return Arrays.hashCode ( this.types ) ;
  }


  /**
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no
   * substitution took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  @ Override
  public TupleType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeName , pTau ) ;
    }
    return new TupleType ( newTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public TupleType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new TupleType ( newTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_TUPLE ) ;
      for ( int i = 0 ; i < this.types.length ; i ++ )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( SPACE ) ;
          this.prettyStringBuilder.addText ( MULT ) ;
          this.prettyStringBuilder.addText ( SPACE ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_TUPLE_TAU ) ;
        if ( i != this.types.length - 1 )
        {
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
    }
    return this.prettyStringBuilder ;
  }
}
