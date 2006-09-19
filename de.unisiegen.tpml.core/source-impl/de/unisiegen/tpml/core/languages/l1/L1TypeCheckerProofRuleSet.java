package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * The type proof rules for the <code>L1</code> language.
 *
 * @author Benedikt Meurer
 * @version $Id: Lt1TypeCheckerProofRuleSet.java 272 2006-09-19 15:55:48Z benny $
 *
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L1TypeCheckerProofRuleSet extends AbstractTypeCheckerProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1TypeCheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L1TypeCheckerProofRuleSet(L1Language language) {
    super(language);
    
    // register the type rules
    registerByMethodName("ABSTR", "applyAbstr");
    registerByMethodName("APP", "applyApp");
    registerByMethodName("COND", "applyCond");
    registerByMethodName("CONST", "applyConst");
    registerByMethodName("ID", "applyId");
    registerByMethodName("LET", "applyLet");
  }
  
  
  
  //
  // The (ABSTR) rule
  //
  
  /**
   * Applies the <b>(ABSTR)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyAbstr(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    // generate new type variables
    TypeVariable tau1 = context.newTypeVariable();
    TypeVariable tau2 = context.newTypeVariable();
    
    // add type equations for tau and tau1->tau2
    context.addEquation(node.getType(), new ArrowType(tau1, tau2));
    
    // generate a new child node
    Lambda lambda = (Lambda)node.getExpression();
    TypeEnvironment environment = node.getEnvironment();
    context.addProofNode(node, environment.extend(lambda.getId(), tau1), lambda.getE(), tau2);
  }
  
  
  
  //
  // The (APP) rule
  //
  
  /**
   * Applies the <b>(APP)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyApp(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    // split into tau1 and tau2 for the application
    TypeVariable tau2 = context.newTypeVariable();
    ArrowType tau1 = new ArrowType(tau2, node.getType());
    
    // can be either an application or an infix operation
    try {
      // generate new child nodes
      Application application = (Application)node.getExpression();
      context.addProofNode(node, node.getEnvironment(), application.getE1(), tau1);
      context.addProofNode(node, node.getEnvironment(), application.getE2(), tau2);
    }
    catch (ClassCastException e) {
      // generate new child nodes
      InfixOperation infixOperation = (InfixOperation)node.getExpression();
      Application application = new Application(infixOperation.getOp(), infixOperation.getE1());
      context.addProofNode(node, node.getEnvironment(), application, tau1);
      context.addProofNode(node, node.getEnvironment(), infixOperation.getE2(), tau2);
    }
  }
  
  
  
  //
  // The (COND) rule
  //
  
  /**
   * Applies the <b>(COND)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyCond(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Condition condition = (Condition)node.getExpression();
    context.addProofNode(node, node.getEnvironment(), condition.getE0(), BooleanType.BOOL);
    context.addProofNode(node, node.getEnvironment(), condition.getE1(), node.getType());
    context.addProofNode(node, node.getEnvironment(), condition.getE2(), node.getType());
  }
  
  
  
  //
  // The (CONST) rule
  //
  
  /**
   * Applies the <b>(CONST)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyConst(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Constant constant = (Constant)node.getExpression();
    context.addEquation(node.getType(), (MonoType)context.getTypeForExpression(constant));
  }
  
  
  
  //
  // The (ID) rule
  //
  
  /**
   * Applies the <b>(ID)</b> rule to the <code>node</node> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyId(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Type type = node.getEnvironment().get(((Identifier)node.getExpression()).getName());
    context.addEquation(node.getType(), (MonoType)type);
  }
  
  
  
  //
  // The (LET) rule
  //
  
  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyLet(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    // generate a new type variable
    TypeVariable tau1 = context.newTypeVariable();
    
    // determine the type environment
    TypeEnvironment environment = node.getEnvironment();
    
    // can be applied to LetRec, Let, CurriedLetRec and CurriedLet
    Expression expression = node.getExpression();
    if (expression instanceof Let) {
      // determine the first sub expression
      Let let = (Let)expression;
      Expression e1 = let.getE1();
      
      // add the recursion for let rec
      if (expression instanceof LetRec) {
        e1 = new Recursion(let.getId(), tau1, e1);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, e1, tau1);
      context.addProofNode(node, environment.extend(let.getId(), tau1), let.getE2(), node.getType());
    }
    else {
      // determine the first sub expression
      CurriedLet curriedLet = (CurriedLet)expression;
      Expression e1 = curriedLet.getE1();
      
      // generate the appropriate lambda abstractions
      String[] identifiers = curriedLet.getIdentifiers();
      for (int n = identifiers.length - 1; n > 0; --n) {
        e1 = new Lambda(identifiers[n], null, e1);
      }
      
      // add the recursion for let rec
      if (expression instanceof CurriedLetRec) {
        e1 = new Recursion(identifiers[0], tau1, e1);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, e1, tau1);
      context.addProofNode(node, environment.extend(identifiers[0], tau1), curriedLet.getE2(), node.getType());
    }
  }
}
