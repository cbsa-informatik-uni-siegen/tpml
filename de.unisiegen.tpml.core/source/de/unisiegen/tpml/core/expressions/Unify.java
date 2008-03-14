package de.unisiegen.tpml.core.expressions;


/**
 * Expression for type equations in the unification algorithm. Every type
 * formula has an expression, but type equation typical don't have one.
 * 
 * @author Benjamin Mies
 * @version $Id$
 */
public class Unify extends Constant
{

  /**
   * The keyword <code>unify</code>.
   */
  private static final String UNIFY = "unify"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Unify.class );


  /**
   * Allocates a new <code>Unify</code>.
   */
  public Unify ()
  {
    super ( UNIFY );
  }


  /**
   * (@inheritDoc)
   * 
   * @see Constant#clone()
   */
  @Override
  public Unify clone ()
  {
    return new Unify ();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }
}
