package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.Expression;

/**
 * The heart of the type checker. Type checker proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet}
 * that is passed to the constructor.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
public final class TypeCheckerProofModel extends AbstractProofModel {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>TypeCheckerProofModel</code> with the specified <code>expression</code>
   * as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param ruleSet the available type rules for the model.
   * 
   * @throws NullPointerException if either <code>expression</code> or <code>ruleSet</code> is
   *                              <code>null</code>.
   *
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode, AbstractProofRuleSet)
   */
  public TypeCheckerProofModel(Expression expression, AbstractTypeCheckerProofRuleSet ruleSet) {
    super(new DefaultTypeCheckerProofNode(new DefaultTypeEnvironment(), expression, /*FIXME*/null), ruleSet);
  }
  
  
  
  //
  // Actions
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void guess(ProofNode node) throws ProofGuessException {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {
    // TODO Auto-generated method stub
  }
}
