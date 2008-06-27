package de.unisiegen.tpml.core.types;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;


/**
 * Abstract base class for primitive types, such as <tt>bool</tt>, <tt>int</tt>
 * and <tt>unit</tt>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see MonoType
 * @see Type
 */
public abstract class PrimitiveType extends MonoType
{

  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null"; //$NON-NLS-1$


  /**
   * The name of this primitive type.
   */
  protected String name;


  /**
   * Allocates a new <code>PrimitiveType</code> with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the primitive type, for example <tt>"bool"</tt> or
   *          <tt>"int"</tt>.
   */
  protected PrimitiveType ( String pName )
  {
    this.name = pName.intern ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @Override
  public abstract PrimitiveType clone ();


  /**
   * Compares this primitive type to the <code>obj</code>. Returns
   * <code>true</code> if the <code>obj</code> is a <code>PrimitiveType</code>
   * with the same name as this instance.
   * 
   * @param pObject another object.
   * @return <code>true</code> if this instance is equal to the specified
   *         <code>obj</code>.
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof PrimitiveType )
    {
      PrimitiveType other = ( PrimitiveType ) pObject;
      return this.name.equals ( other.name );
    }
    return false;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public abstract String getCaption ();


  /**
   * Returns a hash value for this primitive type, which is based on the name of
   * the type.
   * 
   * @return a hash value for this primitive type.
   * @see Object#hashCode()
   */
  @Override
  public int hashCode ()
  {
    return this.name.hashCode ();
  }


  /**
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no substitution
   * took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  @Override
  public PrimitiveType substitute (
      @SuppressWarnings ( UNUSED ) TypeName pTypeName,
      @SuppressWarnings ( UNUSED ) MonoType pTau )
  {
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @Override
  public PrimitiveType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL );
    }
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this,
          PRIO_PRIMITIVE );
      this.prettyStringBuilder.addType ( this.name );
    }
    return this.prettyStringBuilder;
  }
}
