package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * The type proof rules for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet
 */
public class L3TypeCheckerProofRuleSet extends L2TypeCheckerProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3TypecheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the <code>L3</code> or a derived language.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L3TypeCheckerProofRuleSet(L3Language language) {
    super(language);
    
    // register the additional type rules
    registerByMethodName("COND-1", "applyCond1");
    registerByMethodName("SEQ", "applySeq");
    registerByMethodName("WHILE", "applyWhile");
  }
  
  
  
  //
  // The (COND-1) rule
  //
  
  /**
   * Applies the <b>(COND-1)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyCond1(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Condition1 condition1 = (Condition1)node.getExpression();
    context.addEquation(node.getType(), UnitType.UNIT);
    context.addProofNode(node, node.getEnvironment(), condition1.getE0(), BooleanType.BOOL);
    context.addProofNode(node, node.getEnvironment(), condition1.getE1(), node.getType());
  }
  
  
  
  //
  // The (SEQ) rule
  //
  
  /**
   * Applies the <b>(SEQ)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applySeq(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Sequence sequence = (Sequence)node.getExpression();
    context.addProofNode(node, node.getEnvironment(), sequence.getE1(), context.newTypeVariable());
    context.addProofNode(node, node.getEnvironment(), sequence.getE2(), node.getType());
  }
  
  
  
  //
  // The (WHILE) rule
  //
  
  /**
   * Applies the <b>(WHILE)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof context.
   */
  public void applyWhile(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    While loop = (While)node.getExpression();
    context.addEquation(node.getType(), UnitType.UNIT);
    context.addProofNode(node, node.getEnvironment(), loop.getE1(), BooleanType.BOOL);
    context.addProofNode(node, node.getEnvironment(), loop.getE2(), node.getType());
  }
}
