package typing;

import smallstep.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofTreeFactory {
  public ProofTree createProofTree(Expression expression) {
    return createProofTree(expression, Environment.EMPTY_ENVIRONMENT);
  }
  
  public ProofTree createProofTree(Expression expression, Environment environment) {
    return null;
  }
}
