package de.unisiegen.tpml.core.languages.l4;


import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Assign;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Deref;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l3.L3SmallStepProofRuleSet;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext;


/**
 * Small step proof rules for the <code>L4</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.languages.l3.L3SmallStepProofRuleSet
 */
public class L4SmallStepProofRuleSet extends L3SmallStepProofRuleSet
{

  /**
   * Allocates a new <code>L4SmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L4</tt> or a derived
   * language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4SmallStepProofRuleSet ( L4Language language )
  {
    super ( language );
    // register small step rules
    register ( L4Language.L4, "ASSIGN", true ); //$NON-NLS-1$
    register ( L4Language.L4, "COND-1-EVAL", false ); //$NON-NLS-1$
    register ( L4Language.L4, "COND-1-FALSE", true ); //$NON-NLS-1$
    register ( L4Language.L4, "COND-1-TRUE", true ); //$NON-NLS-1$
    register ( L4Language.L4, "DEREF", true ); //$NON-NLS-1$
    register ( L4Language.L4, "REF", true ); //$NON-NLS-1$
    register ( L4Language.L4, "SEQ-EVAL", false ); //$NON-NLS-1$
    register ( L4Language.L4, "SEQ-EXEC", true ); //$NON-NLS-1$
    register ( L4Language.L4, "WHILE", true ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see L1SmallStepProofRuleSet#applyBinaryOperator(SmallStepProofContext,
   *      Expression, BinaryOperator, Expression, Expression)
   */
  @Override
  protected Expression applyBinaryOperator ( SmallStepProofContext context,
      Expression applicationOrInfix, BinaryOperator op, Expression e1,
      Expression e2 )
  {
    // handle Assign here
    if ( op instanceof Assign )
    {
      // assign is simple, just assign the value to the location :-)
      context.getStore ().put ( ( Location ) e1, e2 );
      context.addProofStep ( getRuleByName ( "ASSIGN" ), applicationOrInfix ); //$NON-NLS-1$
      return new UnitConstant ();
    }
    // let the parent class handle this operator application
    return super.applyBinaryOperator ( context, applicationOrInfix, op, e1, e2 );
  }


  /**
   * The <code>evaluate()</code> method for <code>Condition1</code>
   * expressions.
   * 
   * @param context the small step proof context.
   * @param condition1 the {@link de.unisiegen.tpml.core.expressions.Condition1}.
   * @return the resulting expression.
   */
  public Expression evaluateCondition1 ( SmallStepProofContext context,
      Condition1 condition1 )
  {
    // determine the sub expression
    Expression e0 = condition1.getE0 ();
    Expression e1 = condition1.getE1 ();
    // check if e0 is not already a boolean constant
    if ( ! ( e0 instanceof BooleanConstant ) )
    {
      // we're about to perform (COND-1-EVAL)
      context.addProofStep ( getRuleByName ( "COND-1-EVAL" ), condition1 ); //$NON-NLS-1$
      // try to evaluate e0
      e0 = evaluate ( context, e0 );
      // exceptions need special handling
      return e0.isException () ? e0 : new Condition1 ( e0, e1 );
    }
    // determine the boolean constant value
    if ( ( ( BooleanConstant ) e0 ).booleanValue () )
    {
      // jep, that's (COND-1-TRUE) then
      context.addProofStep ( getRuleByName ( "COND-1-TRUE" ), condition1 ); //$NON-NLS-1$
      return e1;
    }
    // jep, that's (COND-1-FALSE) then
    context.addProofStep ( getRuleByName ( "COND-1-FALSE" ), condition1 ); //$NON-NLS-1$
    return new UnitConstant ();
  }


  /**
   * The <code>apply()</code> method for <code>Deref</code> operator.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the deref operator.
   * @param e2 the location.
   * @return the resulting expression.
   */
  public Expression applyDeref ( SmallStepProofContext context,
      Application application, @SuppressWarnings ( "unused" )
      Deref e1, Location e2 )
  {
    Expression value = context.getStore ().get ( e2 );
    context.addProofStep ( getRuleByName ( "DEREF" ), application ); //$NON-NLS-1$
    return value;
  }


  /**
   * The <code>apply()</code> method for the <code>Ref</code> operator.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the ref operator.
   * @param e2 the value.
   * @return the resulting expression.
   */
  public Expression applyRef ( SmallStepProofContext context,
      Application application, @SuppressWarnings ( "unused" )
      Ref e1, Expression e2 )
  {
    Location location = context.getStore ().alloc ();
    context.getStore ().put ( location, e2 );
    context.addProofStep ( getRuleByName ( "REF" ), application ); //$NON-NLS-1$
    return location;
  }


  /**
   * The <code>evaluate()</code> method for <code>Sequence</code>s.
   * 
   * @param context the small step proof context.
   * @param sequence the {@link de.unisiegen.tpml.core.expressions.Sequence}.
   * @return the resulting expression.
   */
  public Expression evaluateSequence ( SmallStepProofContext context,
      Sequence sequence )
  {
    // determine the sub expressions
    Expression e1 = sequence.getE1 ();
    Expression e2 = sequence.getE2 ();
    // check if e1 is not already a value
    if ( !e1.isValue () )
    {
      // we're about to perform (SEQ-EVAL)
      context.addProofStep ( getRuleByName ( "SEQ-EVAL" ), sequence ); //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context, e1 );
      // exceptions need special treatment
      return e1.isException () ? e1 : new Sequence ( e1, e2 );
    }
    // we're about to perform (SEQ-EXEC)
    context.addProofStep ( getRuleByName ( "SEQ-EXEC" ), sequence ); //$NON-NLS-1$
    // drop e1 from the sequence
    return e2;
  }


  /**
   * The <code>evaluate()</code> method for the <code>While</code>
   * expression.
   * 
   * @param context the small step proof context.
   * @param loop the {@link de.unisiegen.tpml.core.expressions.While} loop.
   * @return the resulting expression.
   */
  public Expression evaluateWhile ( SmallStepProofContext context, While loop )
  {
    // determine the sub expressions
    Expression e1 = loop.getE1 ();
    Expression e2 = loop.getE2 ();
    // we're about to perform (WHILE)
    context.addProofStep ( getRuleByName ( "WHILE" ), loop ); //$NON-NLS-1$
    // translate to: if e1 then (e2; while e1 do e2)
    return new Condition1 ( e1, new Sequence ( e2, loop ) );
  }
}
