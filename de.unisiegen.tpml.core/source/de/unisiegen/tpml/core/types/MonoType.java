package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Abstract base class for monomorphic types.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see Type
 */
public abstract class MonoType extends Type
{
  /**
   * Constructor for monomorphic types.
   */
  protected MonoType ( )
  {
    // Nothing to do here...
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public abstract MonoType clone ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public abstract boolean equals ( Object pObject ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


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
  public abstract MonoType substitute ( TypeName pTypeName , MonoType pTau ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public abstract MonoType substitute ( TypeSubstitution pTypeSubstitution ) ;
}
