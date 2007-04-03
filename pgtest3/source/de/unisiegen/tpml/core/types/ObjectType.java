package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class ObjectType extends MonoType
{
  /**
   * TODO
   */
  private RowType phi ;


  /**
   * TODO
   * 
   * @param pPhi TODO
   */
  public ObjectType ( RowType pPhi )
  {
    if ( pPhi == null )
    {
      throw new NullPointerException ( "Phi is null" ) ; //$NON-NLS-1$
    }
    this.phi = pPhi ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ObjectType clone ( )
  {
    return new ObjectType ( this.phi.clone ( ) ) ;
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
      return this.phi.equals ( other.phi ) ;
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
      this.free.addAll ( this.phi.free ( ) ) ;
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
    return this.phi ;
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
    return new ObjectType ( this.phi.substitute ( pTypeSubstitution ) ) ;
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
      this.prettyStringBuilder.addBuilder ( this.phi
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_ROW ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ">" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
