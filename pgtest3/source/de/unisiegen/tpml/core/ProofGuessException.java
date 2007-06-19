package de.unisiegen.tpml.core ;


/**
 * This exception is thrown when an attempt to guess the next proof rule for a
 * given proof node failed.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:467 $
 * @see de.unisiegen.tpml.core.ProofRuleException
 */
public final class ProofGuessException extends Exception
{
  //
  // Constants
  //
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 8678802810950005887L ;


  //
  // Attributes
  //
  /**
   * The {@link ProofNode} for which the next proof step could not be guessed.
   * 
   * @see #getNode()
   */
  private ProofNode node ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>ProofGuessException</code> telling that the next
   * proof step for the specified <code>node</code> cannot be guessed.
   * 
   * @param pNode the node for which the guess failed.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @see ProofModel#guess(ProofNode)
   */
  public ProofGuessException ( ProofNode pNode )
  {
    this ( pNode , null ) ;
  }


  /**
   * Shortcut for {@link #ProofGuessException(String, ProofNode, Throwable)}
   * passing <code>null</code> for the <code>cause</code> parameter.
   * 
   * @param message the descriptive text.
   * @param pNode the node for which the guess failed.
   * @throws NullPointerException if <code>message</code> or <code>node</code>
   *           is <code>null</code>.
   */
  public ProofGuessException ( String message , ProofNode pNode )
  {
    this ( message , pNode , null ) ;
  }


  /**
   * Same as {@link #ProofGuessException(ProofNode)}, but also accepts an
   * additional {@link Throwable} which yields information about the cause of
   * the exception.
   * 
   * @param pNode the node for which the guess failed.
   * @param cause the cause, which is saved for later retrieval by the
   *          {@link Throwable#getCause()} method. A <code>null</code> value
   *          is permitted, and indicates that the cause is nonexistent or
   *          unknown.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  public ProofGuessException ( ProofNode pNode , Throwable cause )
  {
    this ( Messages.getString ( "ProofGuessException.1" ) , pNode , cause ) ; //$NON-NLS-1$
  }


  /**
   * Same as {@link #ProofGuessException(ProofNode, Throwable)}, but also
   * accepts an additional <code>message</code> describing the cause of the
   * exception.
   * 
   * @param message the descriptive text.
   * @param pNode the node for which the guess failed.
   * @param cause the cause, which is saved for later retrieval by the
   *          {@link Throwable#getCause()} method. A <code>null</code> value
   *          is permitted, and indicates that the cause is nonexistent or
   *          unknown.
   * @throws NullPointerException if <code>message</code> or <code>node</code>
   *           is <code>null</code>.
   */
  public ProofGuessException ( String message , ProofNode pNode ,
      Throwable cause )
  {
    super ( message , cause ) ;
    if ( message == null )
    {
      throw new NullPointerException ( "Message is null" ) ; //$NON-NLS-1$
    }
    if ( pNode == null )
    {
      throw new NullPointerException ( "Node is null" ) ; //$NON-NLS-1$
    }
    this.node = pNode ;
  }


  //
  // Accessors
  //
  /**
   * Returns the proof node for which the next proof step could not be guessed.
   * 
   * @return the proof node.
   */
  public ProofNode getNode ( )
  {
    return this.node ;
  }
}
