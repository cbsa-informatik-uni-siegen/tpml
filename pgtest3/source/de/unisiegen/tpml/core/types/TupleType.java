package de.unisiegen.tpml.core.types ;


import java.util.Arrays ;
import java.util.TreeSet ;
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
   * The types for the tuple elements.
   */
  private MonoType [ ] types ;


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
      throw new NullPointerException ( "Types is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes.length < 2 )
    {
      throw new IllegalArgumentException (
          "Types must contain atleast two elements" ) ; //$NON-NLS-1$
    }
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    for ( int i = 0 ; i < this.indicesType.length ; i ++ )
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
   * Indeces of the child {@link Type}s.
   */
  private int [ ] indicesType ;


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
    return "Tuple-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the element types of the tuple.
   * 
   * @return the element types.
   * @see #getTypes(int)
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the <code>n</code>th type in the tuple type.
   * 
   * @param pIndex the index of the type.
   * @return the <code>n</code>th type.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getTypes()
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
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public TupleType substitute ( TypeSubstitution pTypeSubstitution )
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
          this.prettyStringBuilder.addText ( " * " ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_TUPLE_TAU ) ;
      }
    }
    return this.prettyStringBuilder ;
  }
}
