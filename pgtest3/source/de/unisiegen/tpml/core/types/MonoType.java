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
    // nothing to do here...
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
   */
  @ Override
  public abstract String getCaption ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public abstract MonoType substitute ( TypeSubstitution pTypeSubstitution ) ;
}
