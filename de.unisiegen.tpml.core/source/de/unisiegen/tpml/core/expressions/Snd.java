package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>snd</code> operator in the
 * expression hierarchy, which is syntactic sugar for <code>#2_2</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Projection
 */
public final class Snd extends Projection
{
  //
  // Constructor
  //
  /**
   * Allocates a new instance of the <code>snd</code> operator which is a
   * special case of the projection that returns the second item of a pair.
   */
  public Snd ( )
  {
    super ( 2 , 2 , "snd" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Second" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Projection#clone()
   */
  @ Override
  public Snd clone ( )
  {
    return new Snd ( ) ;
  }
}
