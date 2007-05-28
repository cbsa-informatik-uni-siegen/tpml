package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents {@link ObjectType} in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see RowType
 */
public final class ObjectType extends MonoType implements DefaultTypes
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
   * Allocates a new <code>ObjectType</code> with the specified
   * {@link RowType}.
   * 
   * @param pPhi The {@link RowType}.
   */
  public ObjectType ( MonoType pPhi )
  {
    if ( pPhi == null )
    {
      throw new NullPointerException ( "Phi is null" ) ; //$NON-NLS-1$
    }
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pPhi ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>ObjectType</code> with the specified
   * {@link RowType}.
   * 
   * @param pPhi The {@link RowType}.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public ObjectType ( MonoType pPhi , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pPhi ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ObjectType clone ( )
  {
    return new ObjectType ( this.types [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectType )
    {
      ObjectType other = ( ObjectType ) pObject ;
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
    return "Object-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the sub {@link MonoType}.
   * 
   * @return The sub {@link MonoType}.
   */
  public MonoType getPhi ( )
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
  public TreeSet < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.types [ 0 ].getTypeVariablesFree ( ) ) ;
    }
    return this.free ;
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
  public ObjectType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType newTau = this.types [ 0 ].substitute ( pTypeName , pTau ) ;
    return new ObjectType ( newTau ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public ObjectType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    return new ObjectType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ) ;
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
          PRIO_OBJECT ) ;
      this.prettyStringBuilder.addKeyword ( "<" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_ROW ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ">" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
