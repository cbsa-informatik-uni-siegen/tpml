package de.unisiegen.tpml.core.interpreters;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;

/**
 * Abstract base class for all classes implementing the <code>InterpreterProofModel</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofModel
 * @see de.unisiegen.tpml.core.AbstractProofModel
 */
public abstract class AbstractInterpreterProofModel extends AbstractProofModel implements InterpreterProofModel {
  //
  // Attributes
  //
 
  /**
   * <code>true</code> if memory operations are enabled for this proof model.
   * 
   * @see #isMemoryEnabled()
   * @see #setMemoryEnabled(boolean)
   */
  private boolean memoryEnabled;
  
  
  
  //
  // Constructor (protected)
  //

  /**
   * Allocates a new <code>AbstractInterpreterProofModel</code> with the specified <code>root</code> node.
   * 
   * This method automatically calls {@link #setMemoryEnabled(boolean)} for the <code>root</code> node using
   * its expression and the {@link de.unisiegen.tpml.core.expressions.Expression#containsMemoryOperations()}
   * method. 
   * 
   * @param root the new root item.
   * @param ruleSet the set of proof rules.
   * 
   * @throws NullPointerException if <code>language</code> or <code>root</code> is <code>null</code>.
   * 
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode, AbstractProofRuleSet)
   * @see #setMemoryEnabled(boolean)
   */
  protected AbstractInterpreterProofModel(AbstractInterpreterProofNode root, AbstractProofRuleSet ruleSet) {
    super(root, ruleSet);
    
    // check if we have memory operations according to the expression
    setMemoryEnabled(root.getExpression().containsMemoryOperations());
  }
  
  
  
  //
  // Accessors
  //

  /**
   * {@inheritDoc}
   *
   * @see #setMemoryEnabled(boolean)
   * @see de.unisiegen.tpml.core.interpreters.InterpreterProofModel#isMemoryEnabled()
   */
  public boolean isMemoryEnabled() {
    return this.memoryEnabled;
  }

  /**
   * If <code>memoryEnabled</code> is <code>true</code> the proof model will indicate that memory
   * operations will be used, otherwise the simple configurations should be used and the
   * {@link InterpreterProofNode#getStore()} return value will be ignored (and invalid).
   * 
   * @param memoryEnabled the new setting.
   * 
   * @see #isMemoryEnabled()
   */
  public void setMemoryEnabled(boolean memoryEnabled) {
    if (this.memoryEnabled != memoryEnabled) {
      boolean oldMemoryEnabled = this.memoryEnabled;
      this.memoryEnabled = memoryEnabled;
      firePropertyChange("memoryEnabled", oldMemoryEnabled, memoryEnabled);
    }
  }
}
