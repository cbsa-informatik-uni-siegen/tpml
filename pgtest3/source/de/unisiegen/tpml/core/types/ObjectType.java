package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class ObjectType extends MonoType implements DefaultTypes
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
   * TODO
   * 
   * @param pPhi TODO
   */
  public ObjectType ( MonoType pPhi )
  {
    if ( pPhi == null )
    {
      throw new NullPointerException ( "Phi is null" ) ; //$NON-NLS-1$
    }
    if ( ! ( pPhi instanceof RowType ) )
    {
      throw new IllegalArgumentException ( "The Phi has to be a RowType" ) ; //$NON-NLS-1$
    }
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pPhi ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
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
   * 
   * @see Type#free()
   */
  @ Override
  public TreeSet < TypeVariable > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.types [ 0 ].free ( ) ) ;
    }
    return this.free ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see MonoType#getCaption()
   */
  @ Override
  public String getCaption ( )
  {
    return "Object-Type" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public RowType getPhi ( )
  {
    return ( RowType ) this.types [ 0 ] ;
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
    result [ 0 ] = PREFIX_PHI ;
    return result ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   * @see MonoType#substitute(TypeSubstitution)
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
   * TODO
   * 
   * @param pPrettyStringBuilderFactory TODO
   * @return TODO
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
