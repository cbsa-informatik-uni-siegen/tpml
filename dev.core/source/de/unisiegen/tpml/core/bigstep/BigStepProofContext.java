package de.unisiegen.tpml.core.bigstep;


import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;


/**
 * Base interface for {@link de.unisiegen.tpml.core.bigstep.BigStepProofRule}s
 * to interact with the {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}s
 * when applying a rule to a specified node or updating a node after a rule
 * application.
 * 
 * @author Benedikt Meurer
 * @version $Id:BigStepProofContext.java 2796 2008-03-14 19:13:11Z fehler $
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofRule
 */
public interface BigStepProofContext
{

  //
  // Primitives
  //
  /**
   * Adds a new {@link BigStepProofNode} for the specified
   * <code>expression</code> as child node for the given <code>node</code>.
   * The method takes care of registering the required redo and undo actions for
   * the {@link BigStepProofModel} associated with this proof context. This
   * method acts as a convenience wrapper for the extended
   * {@link #addProofNode(BigStepProofNode, Expression, Store)} method, and
   * tries to automatically determine the appropriate store.
   * 
   * @param node the parent node for the new child node.
   * @param expression the {@link Expression} for which to create a new big step
   *          proof node below the <code>node</code>.
   * @throws NullPointerException if either the <code>node</code> or the
   *           <code>expression</code> is <code>null</code>.
   * @see #addProofNode(BigStepProofNode, Expression, Store)
   */
  public void addProofNode ( BigStepProofNode node, Expression expression );


  /**
   * Adds a new {@link BigStepProofNode} for the specified
   * <code>expression</code> and <code>store</code> as child node for the
   * given <code>node</code>. The method takes care of registering the
   * required redo and undo actions for the {@link BigStepProofModel} associated
   * with this proof context.
   * 
   * @param node the parent node for the new child node.
   * @param expression the {@link Expression} for which to create a new big step
   *          proof node below the <code>node</code>.
   * @param store the {@link Store} for the new big step proof node.
   * @throws NullPointerException if either the <code>node</code>, the
   *           <code>store</code> or the <code>expression</code> is
   *           <code>null</code>.
   * @see #addProofNode(BigStepProofNode, Expression)
   * @see Store
   */
  public void addProofNode ( BigStepProofNode node, Expression expression,
      Store store );


  /**
   * Returns <code>true</code> if the {@link BigStepProofModel} associated
   * with this context has memory operations enabled, <code>false</code>
   * otherwise. Several rules - like <b>(APP)</b> - behave differently if
   * memory operations are enabled, so you may need to test this condition.
   * 
   * @return <code>true</code> if memory operations are enabled.
   */
  public boolean isMemoryEnabled ();


  /**
   * Allocates a new big step proof rule of the given <code>name</code> that
   * does nothing. This is used to implement various special cases like the
   * <b>(HD-EMPTY)</b> and <b>(TL-EMPTY)</b> rules.
   * 
   * @param name the name of the new rule.
   * @return the newly allocated {@link BigStepProofRule}.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  public BigStepProofRule newNoopRule ( String name );


  /**
   * Changes the result of the specified <code>node</code> to the given
   * <code>result</code>. The method takes care of registering the required
   * redo and undo actions for the {@link BigStepProofModel} associated with
   * this proof context.
   * 
   * @param node the node, whose result to change to the <code>result</code>.
   * @param result the new result for the specified <code>node</code>.
   * @throws NullPointerException if either <code>node</code> is
   *           <code>null</code>.
   */
  public void setProofNodeResult ( BigStepProofNode node,
      BigStepProofResult result );


  /**
   * This method is a convenience wrapper for the
   * {@link #setProofNodeResult(BigStepProofNode, Expression, Store)} method,
   * which simply passes the {@link Store} from the specified <code>node</code>
   * (determined using the {@link BigStepProofNode#getStore()} method) as store
   * for the {@link BigStepProofResult}, together with the <code>value</code>.
   * 
   * @param node the node, whose result to change to <code>value</code>.
   * @param value the new value for the specified <code>node</code>.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @see BigStepProofNode#getStore()
   */
  public void setProofNodeResult ( BigStepProofNode node, Expression value );


  /**
   * Changes the result of the specified <code>node</code> to a new big step
   * result with the given <code>value</code> and <code>store</code>. This
   * is a convenience wrapper for
   * {@link #setProofNodeResult(BigStepProofNode, BigStepProofResult)}. The
   * method takes care of registering the required redo and undo actions for the
   * {@link BigStepProofModel} associated with this proof context.
   * 
   * @param node the node, whose result to change to <code>value</code> and
   *          <code>store</code>.
   * @param value the new value for the specified <code>node</code>.
   * @param store the resulting store.
   * @throws NullPointerException if the <code>node</code> or
   *           <code>store</code> is <code>null</code>
   */
  public void setProofNodeResult ( BigStepProofNode node, Expression value,
      Store store );


  /**
   * Changes the big step proof rule of the specified <code>node</code> to the
   * given <code>rule</code>. This affects the proof steps of the
   * <code>node</code> (see the {@link ProofNode} method for details. The
   * method takes care of registering the required redo and undo actions for the
   * {@link BigStepProofModel} associated with this proof context.
   * 
   * @param node the node, whose associated proof step to change to a step for
   *          the specified <code>rule</code>.
   * @param rule the new rule to use for the proof step for <code>node</code>.
   */
  public void setProofNodeRule ( BigStepProofNode node, BigStepProofRule rule );
}
