package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Assign;
import de.unisiegen.tpml.core.expressions.Deref;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet;

/**
 * Big step proof rules for the <b>L3</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet
 */
public class L3BigStepProofRuleSet extends L2BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L2</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L1Language)
   */
  public L3BigStepProofRuleSet(L3Language language) {
    super(language);
    
    // register the big step rules (order is important for guessing!)
    registerByMethodName("ASSIGN", "applyAssign");
    registerByMethodName("DEREF", "applyDeref");
    registerByMethodName("REF", "applyRef");
  }
  
  
  
  //
  // The (ASSIGN) rule
  //
  
  /**
   * Applies the <b>(ASSIGN)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyAssign(BigStepProofContext context, BigStepProofNode node) {
    // depends on whether we have an Application or InfixOperation
    @SuppressWarnings("unused") Assign assign;
    Expression e1;
    Expression e2;
    
    // check if Application or InfixOperation
    Expression e = node.getExpression();
    if (e instanceof Application) {
      // Application: (:= e1) e2
      Application a1 = (Application)e;
      Application a2 = (Application)a1.getE1();
      assign = (Assign)a2.getE1();
      e1 = a2.getE2();
      e2 = a1.getE2();
    }
    else {
      // otherwise must be an InfixOperation
      InfixOperation infixOperation = (InfixOperation)e;
      assign = (Assign)infixOperation.getOp();
      e1 = infixOperation.getE1();
      e2 = infixOperation.getE2();
    }
    
    // assign a new value to the location
    Store store = node.getStore();
    store.put((Location)e1, e2);
    context.setProofNodeResult(node, UnitConstant.UNIT, store);
  }

  
  
  //
  // The (DEREF) rule
  //
  
  /**
   * Applies the <b>(DEREF)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyDeref(BigStepProofContext context, BigStepProofNode node) {
    // the expression must be an application of the deref operator...
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Deref e1 = (Deref)application.getE1();
    
    // ...to a location using the node's store
    Location e2 = (Location)application.getE2();
    context.setProofNodeResult(node, node.getStore().get(e2));
  }
  
  
  
  //
  // The (REF) rule
  //
  
  /**
   * Applies the <b>(REF)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyRef(BigStepProofContext context, BigStepProofNode node) {
    // the expression must be an application a the ref operator...
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Ref e1 = (Ref)application.getE1();
    
    // ...to a value using the node's store
    Store store = node.getStore();
    Location location = store.alloc();
    store.put(location, application.getE2());
    context.setProofNodeResult(node, location, store);
  }
}
