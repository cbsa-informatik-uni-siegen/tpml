package de.unisiegen.tpml.core.subtypingrec;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;

/**
 * Abstract base class for subtyping proof rule sets.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofRuleSet
 */
public abstract class AbstractRecSubTypingProofRuleSet extends
		AbstractProofRuleSet {

	/**
	 * Allocates a new <code>AbstractSubTypingProofRuleSet</code> for the specified <code>language</code>.
	 * 
	 * @param language the {@link Language} to which the subtyping proof rules in this set belong.
	 * @param mode the mode chosen by the user
	 * 
	 * @throws NullPointerException if <code>language</code> is <code>null</code>.
	 */
	public AbstractRecSubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language );
	}

	//
	// Rule registration
	//

	/**
	 * Convenience wrapper for the {@link #register(int, String, Method, Method)} method, which simply passes
	 * <code>null</code> for the <code>updateMethod</code> parameter.
	 * 
	 * The rule is prepended to the list, which is important for guessing, as
	 * the last registered proof rule will be used first when guessing.
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *              {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the type checker proof rule to create.
	 * @param applyMethod the implementation of the apply method for the type checker.
	 * 
	 * @throws NullPointerException if <code>name</code> or <code>applyMethod</code> is <code>null</code>.
	 * 
	 * @see #register(int, String, Method, Method)
	 */
	public void register ( int group, String name, Method applyMethod ) {
		register ( group, name, applyMethod, null );
	}

	/**
	 * Registers the rule with the given <code>name</code> and the <code>applyMethod</code> and
	 * <code>updateMethod</code>. The <code>updateMethod</code> may be <code>null</code>.
	 * 
	 * The rule is prepended to the list, which is important for guessing, as
	 * the last registered proof rule will be used first when guessing.
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *              {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the rule.
	 * @param applyMethod the <code>apply()</code> method.
	 * @param updateMethod the <code>update()</code> method or <code>null</code>.
	 * 
	 * @throws NullPointerException if <code>name</code> or <code>applyMethod</code> is <code>null</code>.
	 * 
	 * @see #register(int, String, Method)
	 * @see AbstractProofRuleSet#register(AbstractProofRule)
	 */
	protected void register ( int group, String name, final Method applyMethod,
			final Method updateMethod ) {
		if ( name == null ) {
			throw new NullPointerException ( "name is null" ); //$NON-NLS-1$
		}
		if ( applyMethod == null ) {
			throw new NullPointerException ( "applyMethod is null" ); //$NON-NLS-1$
		}

		// register a new proof rule with the name and methods
		register ( new AbstractRecSubTypingProofRule ( group, name ) {
			@Override
			protected void applyInternal ( RecSubTypingProofContext context,
					RecSubTypingProofNode node ) throws Exception {
				applyMethod.invoke ( AbstractRecSubTypingProofRuleSet.this, context,
						node );
			}
		} );
	}

	/**
	 * Convenience wrapper for the {@link #register(int, String, Method)} method, which determines the
	 * {@link Method} for the specified <code>applyMethodName</code> and uses it for the
	 * <code>applyMethod</code>.
	 * 
	 * The rule is prepended to the list, which is important for guessing, as
	 * the last registered proof rule will be used first when guessing. 
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *              {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the rule.
	 * @param applyMethodName the name of the <code>apply</code> method.
	 * 
	 * @throws NullPointerException if either <code>name</code> or <code>applyMethodName</code>
	 *                              is <code>null</code>.
	 * 
	 * @see #register(int, String, Method)
	 */
	protected void registerByMethodName ( int group, String name,
			String applyMethodName ) {
		register ( group, name, getMethodByName ( applyMethodName ) );
	}

	/**
	 * Convenience wrapper for the {@link #register(int, String, Method, Method)} method, which determines
	 * the {@link Method}s for the specified <code>applyMethodName</code> and <code>updateMethodName</code>
	 * and uses them for the {@link Method} parameters.
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *              {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the rule.
	 * @param applyMethodName the name of the <code>apply</code> method.
	 * @param updateMethodName the name of the <code>update</code> method.
	 * 
	 * @throws NullPointerException if either <code>name</code>, <code>applyMethodName</code> or
	 *                              <code>updateMethodName</code> is <code>null</code>.
	 * 
	 * @see #register(int, String, Method, Method)
	 */
	protected void registerByMethodName ( int group, String name,
			String applyMethodName, String updateMethodName ) {
		register ( group, name, getMethodByName ( applyMethodName ),
				getMethodByName ( updateMethodName ) );
	}

	/**
	 * Convenience wrapper for the {@link AbstractProofRuleSet#unregister(AbstractProofRule)} method,
	 * which takes a rule name rather than an {@link AbstractProofRule} object. The <code>rule</code>
	 * must have been previously registered.
	 * 
	 * @param name the name of the rule to unregister.
	 * 
	 * @throws java.util.NoSuchElementException if no rule of the given <code>name</code> was registered
	 *                                          previously. 
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 * 
	 * @see #register(int, String, Method, Method)
	 * @see AbstractProofRuleSet#unregister(AbstractProofRule)
	 */
	public void unregister ( String name ) {
		unregister ( getRuleByName ( name ) );
	}

	/**
	 * Returns the {@link Method} with the specified <code>methodName</code>, which has two parameters,
	 * a <code>BigStepProofContext</code> and a <code>BigStepProofNode</code>.
	 * 
	 * @param methodName the name of the method to look up.
	 * 
	 * @return the {@link Method} with the specified <code>methodName</code>.
	 * 
	 * @throws NullPointerException if <code>methodName</code> is <code>null</code>.
	 * 
	 * @see #registerByMethodName(int, String, String)
	 * @see #registerByMethodName(int, String, String, String)
	 */
	private Method getMethodByName ( String methodName ) {
		if ( methodName == null ) {
			throw new NullPointerException ( "methodName is null" ); //$NON-NLS-1$
		}
		try {
			// lookup the method with the parameters BigStepProofContext and BigStepProofNode
			return getClass ( ).getMethod (
					methodName,
					new Class[] { RecSubTypingProofContext.class,
							RecSubTypingProofNode.class } );
		} catch ( RuntimeException e ) {
			// just re-throw the exception
			throw e;
		} catch ( Exception e ) {
			// translate the exception to a runtime exception
			throw new RuntimeException ( "Method " + methodName + " not found", e ); //$NON-NLS-1$//$NON-NLS-2$
		}
	}
}
