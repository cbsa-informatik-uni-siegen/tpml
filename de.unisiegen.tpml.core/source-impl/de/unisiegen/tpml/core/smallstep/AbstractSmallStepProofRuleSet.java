package de.unisiegen.tpml.core.smallstep;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.languages.Language;


/**
 * Abstract base class for small step proof rule sets.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:377 $
 * @see de.unisiegen.tpml.core.AbstractProofRuleSet
 */
public abstract class AbstractSmallStepProofRuleSet extends
    AbstractProofRuleSet
{

  //
  // Constructor (protected)
  //
  /**
   * Allocates a new <code>AbstractSmallStepProofRuleSet</code> for the
   * specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the small step proof rules in
   *          this set belong.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  protected AbstractSmallStepProofRuleSet ( Language language )
  {
    super ( language );
  }


  //
  // Rule registration
  //
  /**
   * Convenience wrapper for the
   * {@link AbstractProofRuleSet#register(AbstractProofRule)} method, which
   * registers a new small step rule with the given <code>name</code>.
   * 
   * @param group the group id of the small step rule, see the description of
   *          the {@link AbstractProofRule#getGroup()} method for details.
   * @param name the name of the new small step rule.
   * @param axiom whether the new small step rule is an axiom.
   * @see SmallStepProofRule#isAxiom()
   */
  protected void register ( int group, String name, boolean axiom )
  {
    register ( new DefaultSmallStepProofRule ( group, name, axiom ) );
  }


  /**
   * Convenience wrapper for the
   * {@link AbstractProofRuleSet#unregister(AbstractProofRule)} method, which
   * takes a rule name rather than an {@link AbstractProofRule} object. The
   * <code>rule</code> must have been previously registered.
   * 
   * @param name the name of the rule to unregister.
   * @throws java.util.NoSuchElementException if no rule of the given
   *           <code>name</code> was registered previously.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   * @see AbstractProofRuleSet#unregister(AbstractProofRule)
   */
  protected void unregister ( String name )
  {
    unregister ( getRuleByName ( name ) );
  }


  //
  // Evaluation
  //
  /**
   * Evaluates the next small steps for the <code>expression</code> and
   * returns the resulting {@link Expression}. The resulting {@link Store} is
   * available from the <code>context</code> and the
   * {@link de.unisiegen.tpml.core.ProofStep}s are added to the
   * <code>context</code> as well. This method determines the class of the
   * <code>expression</code> (i.e. <tt>Application</tt> for applications)
   * and then looks for a method named <tt>evaluateClass</tt> (i.e.
   * <tt>evaluateApplication</tt> for applications). The method is passed the
   * <code>context</code> and the <code>expression</code>. If no such
   * method exists, a method for the parent class of the <code>expression</code>
   * will be search and so on. If no method is found, the
   * <code>expression</code> will be returned and <code>context</code> will
   * not be modified.
   * 
   * @param context the small step proof context.
   * @param expression the expression for which to evaluate the next small step.
   * @return the resulting expression.
   * @throws NullPointerException if <code>context</code> or
   *           <code>expression</code> is <code>null</code>.
   * @see #lookupMethod(String, Class)
   */
  public Expression evaluate ( SmallStepProofContext context,
      Expression expression )
  {
    if ( context == null )
    {
      throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
    }
    if ( expression == null )
    {
      throw new NullPointerException ( "expression is null" ); //$NON-NLS-1$
    }
    try
    {
      // determine the specific evaluate method
      Method method = lookupMethod ( "evaluate", expression.getClass () ); //$NON-NLS-1$
      return ( Expression ) method.invoke ( this, context, expression );
    }
    catch ( ClassCastException e )
    {
      // no way to further evaluate the expression
      return expression;
    }
    catch ( NoSuchMethodException e )
    {
      // no way to further evaluate the expression
      return expression;
    }
    catch ( RuntimeException e )
    {
      // rethrow the exception, as something is really completely broken
      throw e;
    }
    catch ( InvocationTargetException e )
    {
      throw new RuntimeException ( e.getTargetException ().getMessage () );
    }
    catch ( Exception e )
    {
      // rethrow as runtime exception, something is really completely broken
      throw new RuntimeException ( e );
    }
  }


  /**
   * Looks up the method with the given <code>baseName</code> and
   * <code>clazz</code>. I.e. if <code>baseName</code> is <tt>"apply"</tt>
   * and <code>clazz</code> is <tt>Application</tt>, the method
   * <code>applyApplication()</code> will be looked up.
   * 
   * @param baseName the basename of the method.
   * @param clazz class whose name is used in the method name.
   * @return the {@link Method} object.
   * @throws NoSuchMethodException if the method cannot be found.
   * @throws NullPointerException if <code>baseName</code> or
   *           <code>clazz</code> is <code>null</code>.
   */
  @SuppressWarnings ( "unchecked" )
  protected Method lookupMethod ( String baseName, Class clazz )
      throws NoSuchMethodException
  {
    // try for this class and all super classes up to Expression
    Class tmpClazz = clazz;
    while ( tmpClazz != Expression.class )
    {
      // try to find a suitable method
      for ( Method method : getClass ().getMethods () )
      {
        if ( method.getName ().equals ( baseName + tmpClazz.getSimpleName () ) )
        {
          return method;
        }
      }
      tmpClazz = tmpClazz.getSuperclass ();
    }
    throw new NoSuchMethodException ( baseName );
  }
}
