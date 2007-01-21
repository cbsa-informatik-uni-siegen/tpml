package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>fst</code> operator in the
 * expression hierarchy, which is syntactic sugar for <code>#2_1</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Projection
 */
public final class Fst extends Projection
{
  //
  // Constructor
  //
  /**
   * Allocates a new instance of the <code>fst</code> operator which is a
   * special case of the projection that returns the first item of a pair.
   */
  public Fst ( )
  {
    super ( 2 , 1 , "fst" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "First" ; //$NON-NLS-1$
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
  public Fst clone ( )
  {
    return new Fst ( ) ;
  }
}
