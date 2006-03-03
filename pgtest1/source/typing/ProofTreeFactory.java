package typing;

import smallstep.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofTreeFactory {
  /**
   * Returns a new proof tree factory instance, which
   * can be used to allocate new {@link ProofTree}
   * instances.
   * 
   * @return a new proof tree factory instance.
   */
  public static ProofTreeFactory getFactory() {
    return new ProofTreeFactory();
  }
  
  /**
   * Allocates a new {@link ProofTree} to determine the type
   * for the <code>expression</code> in the empty type
   * {@link Environment}.
   * 
   * @param expression the {@link Expression} whose type should
   *                   be determined.
   *                   
   * @return the {@link ProofTree} to determine the type for
   *         the <code>expression</code>.
   *         
   * @throws IllegalArgumentException if <code>exception</code> is <code>null</code>. 
   */
  public ProofTree createProofTree(Expression expression) {
    return createProofTree(expression, Environment.EMPTY_ENVIRONMENT);
  }
  
  /**
   * Allocates a new {@link ProofTree} to determine the type
   * for the <code>expression</code> in the specified type
   * <code>environment</code>.
   * 
   * @param expression the {@link Expression} whose type should be
   *                   determined.
   * @param environment the initial type {@link Environment}.
   * 
   * @return the {@link ProofTree} to determine the type for
   *         the <code>expression</code>.
   * 
   * @throws IllegalArgumentException if <code>exception</code> or
   *                                  or <code>environment</code>
   *                                  is <code>null</code>. 
   */
  public ProofTree createProofTree(Expression expression, Environment environment) {
    // verify that the expression is not null
    if (expression == null)
      throw new IllegalArgumentException("Expression may not be null");
    
    // verify that the environment is not null
    if (environment == null)
      throw new IllegalArgumentException("Environment may not be null");
    
    // allocate the new proof tree
    return new ProofTree(environment, expression);
  }
  
  private ProofTreeFactory() {
  }
}
