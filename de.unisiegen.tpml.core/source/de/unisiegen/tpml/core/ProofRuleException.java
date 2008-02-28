package de.unisiegen.tpml.core;


import java.text.MessageFormat;


/**
 * This exception is thrown when an attempt to apply a rule that cannot be
 * applied to a node.
 * 
 * @author Benedikt Meurer
 * @version $Rev:415 $
 * @see de.unisiegen.tpml.core.ProofGuessException
 */
public final class ProofRuleException extends Exception
{

  //
  // Constants
  //
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = -765882201403684253L;


  //
  // Attributes
  //
  /**
   * The node to which the rule was about to be applied.
   * 
   * @see #getNode()
   */
  private ProofNode node;


  /**
   * The rule that turned out to be invalid.
   * 
   * @see #getRule()
   */
  private ProofRule rule;


  //
  // Constructor
  //
  /**
   * Allocates a new {@link ProofRuleException} telling that the specified
   * <code>rule</code> could not be applied to the given <code>node</code>.
   * 
   * @param pNode the {@link ProofNode}.
   * @param pRule the {@link ProofRule} that could not be applied to the
   *          <code>node</code>.
   * @throws NullPointerException if <code>node</code> or <code>rule</code>
   *           is <code>null</code>.
   * @see #ProofRuleException(ProofNode, ProofRule, Throwable)
   */
  public ProofRuleException ( ProofNode pNode, ProofRule pRule )
  {
    this ( pNode, pRule, null );
  }


  /**
   * Allocates a new {@link ProofRuleException} telling that the specified
   * <code>rule</code> could not be applied to the given <code>node</code>.
   * 
   * @param pNode the {@link ProofNode}.
   * @param pRule the {@link ProofRule} that could not be applied to the
   *          <code>node</code>.
   * @param cause the cause, which is saved for later retrieval by the
   *          {@link Throwable#getCause()} method. A <code>null</code> value
   *          is permitted, and indicates that the cause is nonexistent or
   *          unknown.
   * @throws NullPointerException if <code>node</code> or <code>rule</code>
   *           is <code>null</code>.
   * @see #ProofRuleException(String, ProofNode, ProofRule, Throwable)
   */
  public ProofRuleException ( ProofNode pNode, ProofRule pRule, Throwable cause )
  {
    this ( MessageFormat.format (
        Messages.getString ( "ProofRuleException.0" ), pRule, pNode ), pNode, //$NON-NLS-1$
        pRule, cause );
  }


  /**
   * Allocates a new {@link ProofRuleException} telling that the specified
   * <code>rule</code> could not be applied to the given <code>node</code>.
   * The <code>message</code> gives detailed information about the cause of
   * the problem.
   * 
   * @param message detailed information about the cause.
   * @param pNode the {@link ProofNode}.
   * @param pRule the {@link ProofRule} that could not be applied to the
   *          <code>node</code>.
   * @param cause the cause, which is saved for later retrieval by the
   *          {@link Throwable#getCause()} method. A <code>null</code> value
   *          is permitted, and indicates that the cause is nonexistent or
   *          unknown.
   * @throws NullPointerException if <code>message</code>, <code>node</code>
   *           or <code>rule</code> is <code>null</code>.
   */
  public ProofRuleException ( String message, ProofNode pNode, ProofRule pRule,
      Throwable cause )
  {
    super ( message, cause );
    if ( message == null )
    {
      throw new NullPointerException ( "message is null" ); //$NON-NLS-1$
    }
    if ( pNode == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( pRule == null )
    {
      throw new NullPointerException ( "rule is null" ); //$NON-NLS-1$
    }
    this.node = pNode;
    this.rule = pRule;
  }


  //
  // Accessors
  //
  /**
   * Returns the node on which a rule application failed.
   * 
   * @return the node on which a rule application failed.
   */
  public ProofNode getNode ()
  {
    return this.node;
  }


  /**
   * Returns the rule that failed to apply.
   * 
   * @return the rule that failed to apply.
   */
  public ProofRule getRule ()
  {
    return this.rule;
  }
}
