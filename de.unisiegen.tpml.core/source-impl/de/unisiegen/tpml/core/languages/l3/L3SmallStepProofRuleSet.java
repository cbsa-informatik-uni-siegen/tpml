package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Assign;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.Deref;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext;

/**
 * Small step proof rules for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet
 */
public class L3SmallStepProofRuleSet extends L2SmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>L3</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2SmallStepProofRuleSet#L2SmallStepProofRuleSet(L1Language)
   */
  public L3SmallStepProofRuleSet(L3Language language) {
    super(language);
    
    // register small step rules
    register("ASSIGN", true);
    register("DEREF", true);
    register("REF", true);
  }
  
  
  
  //
  // The (ASSIGN) rule
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet#applyBinaryOperator(de.unisiegen.tpml.core.smallstep.SmallStepProofContext, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.expressions.BinaryOperator, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  protected Expression applyBinaryOperator(SmallStepProofContext context, Expression applicationOrInfix, BinaryOperator op, Expression e1, Expression e2) {
    // handle Assign here
    if (op instanceof Assign) {
      // assign is simple, just assign the value to the location :-)
      context.getStore().put((Location)e1, e2);
      context.addProofStep(getRuleByName("ASSIGN"), applicationOrInfix);
      return UnitConstant.UNIT;
    }
    else {
      // let the parent class handle this operator application 
      return super.applyBinaryOperator(context, applicationOrInfix, op, e1, e2);
    }
  }
  
  
  
  //
  // The (DEREF) rule
  //
  
  /**
   * The <code>apply()</code> method for <code>Deref</code> operator.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the deref operator.
   * @param e2 the location.
   * 
   * @return the resulting expression.
   */
  public Expression applyDeref(SmallStepProofContext context, Application application, Deref e1, Location e2) {
    Expression value = context.getStore().get(e2);
    context.addProofStep(getRuleByName("DEREF"), application);
    return value;
  }
  
  
  
  //
  // The (REF) rule
  //
  
  /**
   * The <code>apply()</code> method for the <code>Ref</code> operator.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the ref operator.
   * @param e2 the value.
   * 
   * @return the resulting expression.
   */
  public Expression applyRef(SmallStepProofContext context, Application application, Ref e1, Expression e2) {
    Location location = context.getStore().alloc();
    context.getStore().put(location, e2);
    context.addProofStep(getRuleByName("REF"), application);
    return location;
  }
}
