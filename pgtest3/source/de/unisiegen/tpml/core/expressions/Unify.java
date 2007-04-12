package de.unisiegen.tpml.core.expressions;

/**
 * Expression for type equations in the unification algorithm.
 * Every type formula has an expression, but type equation typical don't have one.
 *
 * @author Benjamin Mies
 *
 */
public class Unify extends Constant {

	/**
	 * TODO
	 *
	 * @param pText
	 */
	public Unify() {
		super("unify");
	}



  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "unify" ; //$NON-NLS-1$
  }

  //
  // Primitives
  //
  
	/**
	 *(@inheritDoc)
	 * @see de.unisiegen.tpml.core.expressions.Constant#clone()
	 */
	@Override
	public Constant clone() {
		
		return new Unify();
	}

}
