package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents {@link RecType} in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see #typeNames
 */
public final class RecType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link TypeName}s.
   */
  private static final int [ ] INDICES_TYPE_NAME = new int [ ]
  { - 1 } ;


  /**
   * The list of {@link TypeName}s.
   * 
   * @see #getTypeNames()
   */
  private TypeName [ ] typeNames ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new <code>RecType</code> with the specified {@link TypeName}
   * and {@link Type}.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link Type}.
   */
  public RecType ( TypeName pTypeName , MonoType pTau )
  {
    if ( pTypeName == null )
    {
      throw new NullPointerException ( "TypeName is null" ) ; //$NON-NLS-1$
    }
    if ( pTau == null )
    {
      throw new NullPointerException ( "Tau is null" ) ; //$NON-NLS-1$
    }
    // TypeName
    this.typeNames = new TypeName [ ]
    { pTypeName } ;
    if ( this.typeNames [ 0 ].getParent ( ) != null )
    {
      // this.typeNames [ 0 ] = this.typeNames [ 0 ].clone ( ) ;
    }
    this.typeNames [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>RecType</code> with the specified {@link TypeName}
   * and {@link Type}.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link Type}.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public RecType ( TypeName pTypeName , MonoType pTau , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTypeName , pTau ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RecType clone ( )
  {
    return new RecType ( this.typeNames [ 0 ].clone ( ) , this.types [ 0 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RecType )
    {
      RecType other = ( RecType ) pObject ;
      return ( this.typeNames [ 0 ].equals ( other.typeNames [ 0 ] ) )
          && ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ;
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
    return "Rec-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the sub {@link Type}.
   * 
   * @return The sub {@link Type}.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the {@link TypeName}s of this {@link Type}.
   * 
   * @return The {@link TypeName}s of this {@link Type}.
   * @see #typeNames
   */
  public TypeName [ ] getTypeNames ( )
  {
    return this.typeNames ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  @ Override
  public ArrayList < TypeName > getTypeNamesFree ( )
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ( ) ;
      this.typeNamesFree.addAll ( this.types [ 0 ].getTypeNamesFree ( ) ) ;
      while ( this.typeNamesFree.remove ( this.typeNames [ 0 ] ) )
      {
        // Remove all TypeNames with the same name
      }
    }
    return this.typeNamesFree ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypeNamesIndex ( )
  {
    return INDICES_TYPE_NAME ;
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
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.typeNames [ 0 ].hashCode ( ) + this.types [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public RecType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    return new RecType ( this.typeNames [ 0 ] , this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ) ;
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
      // TODO PrettyPrintPriorities
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          0 ) ;
      this.prettyStringBuilder.addKeyword ( "\u03bc" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.typeNames [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
