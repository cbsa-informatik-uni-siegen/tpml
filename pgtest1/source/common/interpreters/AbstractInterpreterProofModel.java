package common.interpreters;

import common.AbstractProofModel;
import common.ProofNode;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class AbstractInterpreterProofModel extends AbstractProofModel implements InterpreterProofModel {
  //
  // Attributes
  //
  
  /**
   * Whether memory operations are enabled.
   * 
   * @see #isMemoryEnabled()
   * @see #setMemoryEnabled(boolean)
   */
  protected boolean memoryEnabled;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractInterpreterProofModel</code> with the
   * specified <code>root</code> node.
   * 
   * @param root the root node for the proof tree.
   * 
   * @throws NullPointerException if <code>root</code> is <code>null</code>.
   */
  protected AbstractInterpreterProofModel(AbstractInterpreterProofNode root) {
    super(root);
  }

  

  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#isMemoryEnabled()
   */
  public boolean isMemoryEnabled() {
    return this.memoryEnabled;
  }
  
  /**
   * If <code>memoryEnabled</code> is <code>true</code> the proof
   * model will indicate the upper layers that memory operations
   * will be used, otherwise the simple configurations should be
   * used and the {@link ProofNode#getStore()} return value will
   * be ignored (and invalid).
   *  
   * @param memoryEnabled the new setting.
   * 
   * @see #isMemoryEnabled()
   */
  public void setMemoryEnabled(boolean memoryEnabled) {
    // check if we have a new setting
    if (this.memoryEnabled != memoryEnabled) {
      boolean oldMemoryEnabled = this.memoryEnabled;
      this.memoryEnabled = memoryEnabled;
      firePropertyChange("memoryEnabled", oldMemoryEnabled, memoryEnabled);
    }
  } 
}