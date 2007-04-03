package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Abstract base class for primitive types, such as <tt>bool</tt>,
 * <tt>int</tt> and <tt>unit</tt>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:345 $
 * @see MonoType
 * @see Type
 */
public abstract class PrimitiveType extends MonoType
{
  /**
   * The name of this primitive type.
   */
  private String name ;


  /**
   * Allocates a new <code>PrimitiveType</code> with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the primitive type, for example <tt>"bool"</tt>
   *          or <tt>"int"</tt>.
   */
  protected PrimitiveType ( String pName )
  {
    this.name = pName.intern ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public abstract PrimitiveType clone ( ) ;


  /**
   * Compares this primitive type to the <code>obj</code>. Returns
   * <code>true</code> if the <code>obj</code> is a
   * <code>PrimitiveType</code> with the same name as this instance.
   * 
   * @param pObject another object.
   * @return <code>true</code> if this instance is equal to the specified
   *         <code>obj</code>.
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof PrimitiveType )
    {
      PrimitiveType other = ( PrimitiveType ) pObject ;
      return this.name == other.name ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


  /**
   * Returns a hash value for this primitive type, which is based on the name of
   * the type.
   * 
   * @return a hash value for this primitive type.
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public PrimitiveType substitute ( @ SuppressWarnings ( "unused" )
  TypeSubstitution pTypeSubstitution )
  {
    return this ;
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
          PRIO_PRIMITIVE ) ;
      this.prettyStringBuilder.addType ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
