package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>snd</code> operator in the
 * expression hierarchy, which is syntactic sugar for <code>#2_2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Projection
 */
public final class Snd extends Projection
{
  /**
   * Allocates a new instance of the <code>snd</code> operator which is a
   * special case of the projection that returns the second item of a pair.
   */
  public Snd ( )
  {
    super ( 2 , 2 , "snd" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Second" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Projection#clone()
   */
  @ Override
  public Snd clone ( )
  {
    return new Snd ( ) ;
  }
}
