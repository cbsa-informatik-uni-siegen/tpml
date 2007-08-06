package de.unisiegen.tpml.core.interpreters ;


import java.util.Arrays ;
import de.unisiegen.tpml.core.AbstractExpressionProofModel ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.AbstractProofRuleSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.ProofRule ;


/**
 * Abstract base class for all classes implementing the
 * <code>InterpreterProofModel</code> interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofModel
 * @see de.unisiegen.tpml.core.AbstractExpressionProofModel
 */
public abstract class AbstractInterpreterProofModel extends
    AbstractExpressionProofModel implements InterpreterProofModel
{
  //
  // Attributes
  //
  /**
   * <code>true</code> if memory operations are enabled for this proof model.
   * 
   * @see #isMemoryEnabled()
   * @see #setMemoryEnabled(boolean)
   */
  private boolean memoryEnabled ;


  //
  // Constructor (protected)
  //
  /**
   * Allocates a new <code>AbstractInterpreterProofModel</code> with the
   * specified <code>root</code> node. This method automatically calls
   * {@link #setMemoryEnabled(boolean)} for the <code>root</code> node using
   * its expression and the
   * {@link de.unisiegen.tpml.core.expressions.Expression#containsMemoryOperations()}
   * method.
   * 
   * @param pRoot the new root item.
   * @param pRuleSet the set of proof rules.
   * @throws NullPointerException if <code>language</code> or
   *           <code>root</code> is <code>null</code>.
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode,
   *      AbstractProofRuleSet)
   * @see #setMemoryEnabled(boolean)
   */
  protected AbstractInterpreterProofModel ( AbstractInterpreterProofNode pRoot ,
      AbstractProofRuleSet pRuleSet )
  {
    super ( pRoot , pRuleSet ) ;
    // check if we have memory operations according to the expression
    setMemoryEnabled ( pRoot.getExpression ( ).containsMemoryOperations ( ) ) ;
  }


  //
  // Actions
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#complete(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void complete ( ProofNode node ) throws ProofGuessException
  {
    // check if we're not at the root, as we want to avoid endless diversion
    if ( node != null && ! node.isRoot ( ) )
    {
      // start at the parent node and look for a pattern
      ProofNode current = node.getParent ( ) ;
      ProofNode parent = current.getParent ( ) ;
      // determine the current sequence of rules
      ProofRule [ ] rules = current.getRules ( ) ;
      for ( int count = 0 ; parent != null ; parent = parent.getParent ( ) )
      {
        // check if the same sequence of rules was applied to this node
        if ( Arrays.equals ( rules , parent.getRules ( ) ) )
        {
          // yet another time...
          count += 1 ;
          // ...maybe already too often
          if ( count >= 20 )
          {
            // stop the automatic completion, warn the user and suggest manual
            // checkin
            throw new ProofGuessException ( Messages
                .getString ( "AbstractInterpreterProofModel.0" ) , node ) ; //$NON-NLS-1$
          }
        }
      }
    }
    // otherwise, try to complete the proof
    super.complete ( node ) ;
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
  public boolean isMemoryEnabled ( )
  {
    return this.memoryEnabled ;
  }


  /**
   * If <code>memoryEnabled</code> is <code>true</code> the proof model will
   * indicate that memory operations will be used, otherwise the simple
   * configurations should be used and the
   * {@link InterpreterProofNode#getStore()} return value will be ignored (and
   * invalid).
   * 
   * @param pMemoryEnabled the new setting.
   * @see #isMemoryEnabled()
   */
  public void setMemoryEnabled ( boolean pMemoryEnabled )
  {
    if ( this.memoryEnabled != pMemoryEnabled )
    {
      boolean oldMemoryEnabled = this.memoryEnabled ;
      this.memoryEnabled = pMemoryEnabled ;
      firePropertyChange ( "memoryEnabled" , oldMemoryEnabled , pMemoryEnabled ) ; //$NON-NLS-1$
    }
  }
}
