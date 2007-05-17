package de.unisiegen.tpml.core.types ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents {@link RowType} in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see ObjectType
 */
public final class RowType extends MonoType implements DefaultIdentifiers ,
    DefaultTypes , SortedChildren
{
  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private int [ ] indicesType ;


  /**
   * Allocates a new <code>RowType</code> with the specified
   * {@link Identifier}s and {@link Type}s.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   */
  public RowType ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( "Types is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException (
          "The arity of method names and types must match" ) ; //$NON-NLS-1$
    }
    for ( MonoType type : pTypes )
    {
      if ( type == null )
      {
        throw new NullPointerException ( "One type is null" ) ; //$NON-NLS-1$
      }
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].getParent ( ) != null )
      {
        // this.identifiers [ i ] = this.identifiers [ i ].clone ( ) ;
      }
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    for ( int i = 0 ; i < this.types.length ; i ++ )
    {
      if ( this.types [ i ].getParent ( ) != null )
      {
        // this.types [ i ] = this.types [ i ].clone ( ) ;
      }
      this.types [ i ].setParent ( this ) ;
      this.indicesType [ i ] = i + 1 ;
    }
  }


  /**
   * Allocates a new <code>RowType</code> with the specified
   * {@link Identifier}s and {@link Type}s.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public RowType ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pTypes ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RowType clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].clone ( ) ;
    }
    return new RowType ( newIdentifiers , newTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RowType )
    {
      RowType other = ( RowType ) pObject ;
      return ( Arrays.equals ( this.identifiers , other.identifiers ) && Arrays
          .equals ( this.types , other.types ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#free()
   */
  @ Override
  public TreeSet < TypeVariable > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      for ( MonoType type : this.types )
      {
        this.free.addAll ( type.free ( ) ) ;
      }
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   * @see #identifiers
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return this.indicesId ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getPrefix()
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_PHI ;
    }
    return this.prefix ;
  }


  /**
   * Returns the {@link Identifier}s and {@link Type}s in the right sorting.
   * 
   * @return The {@link Identifier}s and {@link Type}s in the right sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public PrettyPrintable [ ] getSortedChildren ( )
  {
    PrettyPrintable [ ] result = new PrettyPrintable [ this.identifiers.length
        + this.types.length ] ;
    for ( int i = 0 ; i < this.identifiers.length + this.types.length ; i ++ )
    {
      if ( i % 2 == 0 )
      {
        result [ i ] = this.identifiers [ i / 2 ] ;
      }
      else
      {
        result [ i ] = this.types [ i / 2 ] ;
      }
    }
    return result ;
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
    return Arrays.hashCode ( this.identifiers ) + Arrays.hashCode ( this.types ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public RowType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new RowType ( this.identifiers , newTypes ) ;
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
          PRIO_ROW ) ;
      for ( int i = 0 ; i < this.types.length ; i ++ )
      {
        if ( i != 0 )
        {
          this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_ROW_TAU ) ;
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
        if ( i != this.types.length - 1 )
        {
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      if ( this.types.length == 0 )
      {
        this.prettyStringBuilder.addText ( "\u00D8" ) ; //$NON-NLS-1$
      }
    }
    return this.prettyStringBuilder ;
  }
}
