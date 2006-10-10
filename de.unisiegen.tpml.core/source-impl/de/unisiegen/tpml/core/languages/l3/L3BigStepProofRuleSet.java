package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.BigStepProofRule;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryCons;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.EmptyList;
import de.unisiegen.tpml.core.expressions.Exn;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Fst;
import de.unisiegen.tpml.core.expressions.Hd;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.expressions.List;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Snd;
import de.unisiegen.tpml.core.expressions.Tl;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.UnaryCons;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l2.L2Language;

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
   * <code>language</code>, which is the <b>L3</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L2Language)
   */
  public L3BigStepProofRuleSet(L3Language language) {
    super(language);
    
    // register the rules (order is important for guessing!)
    registerByMethodName(L3Language.L3, "CONS", "applyCons");
    registerByMethodName(L3Language.L3, "HD", "applyHd");
    registerByMethodName(L3Language.L3, "IS-EMPTY-FALSE", "applyIsEmptyFalse");
    registerByMethodName(L3Language.L3, "IS-EMPTY-TRUE", "applyIsEmptyTrue");
    registerByMethodName(L3Language.L3, "LIST", "applyList", "updateList");
    registerByMethodName(L3Language.L3, "PROJ", "applyProj");
    registerByMethodName(L3Language.L3, "FST", "applyFst");
    registerByMethodName(L3Language.L3, "SND", "applySnd");
    registerByMethodName(L3Language.L3, "TL", "applyTl");
    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple", "updateTuple");
    
    // register (VAL) once again to have higher priority than (TUPLE) for guessing
    registerByMethodName(getRuleByName("VAL").getGroup(), "VAL", "applyValue");
  }
  
  
  
  //
  // The (CONS) rule
  //
  
  /**
   * Applies the <b>(CONS)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyCons(BigStepProofContext context, BigStepProofNode node) throws BinaryOperatorException {
    // depends on whether we have an Application or InfixOperation
    BinaryCons op;
    Expression e1;
    Expression e2;
    
    // check if Application or InfixOperation
    Expression e = node.getExpression();
    if (e instanceof Application) {
      // Application: (op e1) e2
      Application a1 = (Application)e;
      Application a2 = (Application)a1.getE1();
      op = (BinaryCons)a2.getE1();
      e1 = a2.getE2();
      e2 = a1.getE2();
    }
    else {
      // otherwise must be an InfixOperation
      InfixOperation infixOperation = (InfixOperation)e;
      op = (BinaryCons)infixOperation.getOp();
      e1 = infixOperation.getE1();
      e2 = infixOperation.getE2();
    }
    
    // perform the application
    context.setProofNodeResult(node, op.applyTo(e1, e2));
  }
  
  
  
  //
  // The (FST) rule
  //
  
  /**
   * Applies the <b>(FST)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyFst(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of fst to a tuple
    Application application = (Application)node.getExpression();
    Fst e1 = (Fst)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  
  
  //
  // The (HD) rule
  //
  
  /**
   * Applies the <b>(HD)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyHd(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to Applications of Hd to a list value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Hd hd = (Hd)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new IllegalArgumentException("e2 must be a value");
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      context.setProofNodeResult(node, Exn.EMPTY_LIST);
      context.setProofNodeRule(node, context.newNoopRule("HD-EMPTY"));
      return;
    }
    
    // check if e2 is a list
    if (e2 instanceof List) {
      context.setProofNodeResult(node, ((List)e2).head());
      return;
    }
    
    // otherwise e2 must be an application of cons to a pair
    Application a1 = (Application)e2;
    Tuple tuple = (Tuple)a1.getE2();
    if (!(a1.getE1() instanceof UnaryCons) || tuple.getExpressions().length != 2) {
      throw new IllegalArgumentException("e2 must be an application of cons to a pair");
    }
    
    // jep, we can perform (HD) then
    context.setProofNodeResult(node, tuple.getExpressions(0));
  }
  
  
  
  //
  // The (IS-EMPTY-FALSE) rule
  //
  
  /**
   * Applies the <b>(IS-EMPTY-FALSE)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyIsEmptyFalse(BigStepProofContext context, BigStepProofNode node) {
    // node's expression must be an Application of IsEmpty to a value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") IsEmpty isEmpty = (IsEmpty)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new IllegalArgumentException("e2 must be a value");
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      // let (IS-EMPTY-TRUE) handle the node
      context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("IS-EMPTY-TRUE"));
      applyIsEmptyTrue(context, node);
    }
    else if (e2 instanceof List) {
      // Lists aren't empty
      context.setProofNodeResult(node, BooleanConstant.TRUE);
    }
    else {
      // otherwise e2 must be an application of cons to a pair
      Application a1 = (Application)e2;
      Tuple tuple = (Tuple)a1.getE2();
      if (a1.getE1() instanceof UnaryCons && tuple.getExpressions().length == 2) {
        context.setProofNodeResult(node, BooleanConstant.TRUE);
      }
      else {
        throw new IllegalArgumentException("e2 must be an application of cons to a pair");
      }
    }
  }
  
  
  
  //
  // The (IS-EMPTY-TRUE) rule
  //
  
  /**
   * Applies the <b>(IS-EMPTY-TRUE)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyIsEmptyTrue(BigStepProofContext context, BigStepProofNode node) {
    // node's expression must be an Application of IsEmpty to a value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") IsEmpty isEmpty = (IsEmpty)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new IllegalArgumentException("e2 must be a value");
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      // EmptyList is always empty
      context.setProofNodeResult(node, BooleanConstant.TRUE);
    }
    else {
      // let (IS-EMPTY-FALSE) handle the node
      context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("IS-EMPTY-FALSE"));
      applyIsEmptyFalse(context, node);
    }
  }
  
  
  
  //
  // The (LIST) rule
  //
  
  /**
   * Applies the <b>(LIST)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyList(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to lists
    List list = (List)node.getExpression();
  
    // check if memory is enabled
    if (context.isMemoryEnabled()) {
      // add a child node for the first expression
      context.addProofNode(node, list.getExpressions(0));
    }
    else {
      // add all child nodes at once
      for (Expression e : list.getExpressions()) {
        context.addProofNode(node, e);
      }
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(LIST)</b> was applied previously using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void updateList(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression at this node
    List list = (List)node.getExpression();
    
    // check if all child nodes were created
    if (node.getChildCount() < list.getExpressions().length) {
      // verify that the last child node is proven
      if (node.getLastChild().isProven()) {
        // add the next child node
        context.addProofNode(node, list.getExpressions(node.getChildCount()));
      }
    }
    else {
      // check if all child nodes are proven
      Expression[] values = new Expression[node.getChildCount()];
      for (int n = 0; n < values.length; ++n) {
        BigStepProofResult result = node.getChildAt(n).getResult();
        if (result == null) {
          // atleast one is not yet proven
          return;
        }
        values[n] = result.getValue();
      }
      
      // all child nodes are proven, we're done
      context.setProofNodeResult(node, new List(values));
    }
  }
  
  
  
  //
  // The (PROJ) rule
  //

  /**
   * Applies the <b>(PROJ)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyProj(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of a projection to a tuple
    Application application = (Application)node.getExpression();
    Projection e1 = (Projection)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  
  
  //
  // The (SND) rule
  //
  
  /**
   * Applies the <b>(SND)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applySnd(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of snd to a tuple
    Application application = (Application)node.getExpression();
    Snd e1 = (Snd)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  

  //
  // The (TL) rule
  //
  
  /**
   * Applies the <b>(TL)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyTl(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to Applications of Tl to a list value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Tl tl = (Tl)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new IllegalArgumentException("e2 must be a value");
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      context.setProofNodeResult(node, Exn.EMPTY_LIST);
      context.setProofNodeRule(node, context.newNoopRule("TL-EMPTY"));
      return;
    }
    
    // check if e2 is a list
    if (e2 instanceof List) {
      context.setProofNodeResult(node, ((List)e2).tail());
      return;
    }
    
    // otherwise e2 must be an application of cons to a pair
    Application a1 = (Application)e2;
    Tuple tuple = (Tuple)a1.getE2();
    if (!(a1.getE1() instanceof UnaryCons) || tuple.getExpressions().length != 2) {
      throw new IllegalArgumentException("e2 must be an application of cons to a pair");
    }
    
    // jep, we can perform (TL) then
    context.setProofNodeResult(node, tuple.getExpressions(1));
  }
  
  
  
  //
  // The (TUPLE) rule
  //
  
  /**
   * Applies the <b>(TUPLE)</b> to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyTuple(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to Tuples
    Tuple tuple = (Tuple)node.getExpression();
    
    // check if memory is enabled
    if (context.isMemoryEnabled()) {
      // add a child node for the first sub expression
      context.addProofNode(node, tuple.getExpressions(0));
    }
    else {
      // add all child nodes at once
      for (Expression e : tuple.getExpressions()) {
        context.addProofNode(node, e);
      }
    }
  }
  
  /**
   * Updates the <code>node</code>, to which <b>(TUPLE)</b> was applied previously, using
   * the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void updateTuple(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression at this node
    Tuple tuple = (Tuple)node.getExpression();
    
    // check if all child nodes were created
    if (node.getChildCount() < tuple.getExpressions().length) {
      // verify that the last child node is proven
      if (node.getLastChild().isProven()) {
        // add the next child node
        context.addProofNode(node, tuple.getExpressions(node.getChildCount()));
      }
    }
    else {
      // check if all child nodes are proven
      Expression[] values = new Expression[node.getChildCount()];
      for (int n = 0; n < values.length; ++n) {
        BigStepProofResult result = node.getChildAt(n).getResult();
        if (result == null) {
          // atleast one is not yet proven
          return;
        }
        values[n] = result.getValue();
      }
      
      // all child nodes are proven, we're done
      context.setProofNodeResult(node, new Tuple(values));
    }
  }
}
