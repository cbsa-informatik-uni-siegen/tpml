package de.unisiegen.tpml.core.typeinference;

/**
 * This interface includes the pretty print priorities, according to the
 * priority grammar, for the various expressions. It is implemented by the
 * {@link de.unisiegen.tpml.core.expressions.Expression} class, so one can
 * easily use the constants in this interface without having to explicitly use
 * this interface each time.
 * 
 * @author Benjamin Mies
 * @version $Rev: 1061 $
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 */
interface PrettyPrintPriorities {

	/**
	 * The pretty print priority for type equations.
	 */
	public static final int PRIO_EQUATION = 0;
}
