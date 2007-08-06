package de.unisiegen.tpml.core.bigstep ;


import java.text.MessageFormat ;
import org.apache.log4j.Logger ;
import de.unisiegen.tpml.core.AbstractProofRuleSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel ;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode ;
import de.unisiegen.tpml.core.interpreters.Store ;


/**
 * This class is the heart of the big step interpreter. A big step proof model
 * consists of any number of
 * {@link de.unisiegen.tpml.core.bigstep.BigStepProofNode}s, which together
 * make up the big step proof.
 * 
 * @author Benedikt Meurer
 * @version $Rev:878 $
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel
 */
public final class BigStepProofModel extends AbstractInterpreterProofModel
{
  //
  // Constants
  //
  /**
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger
      .getLogger ( BigStepProofModel.class ) ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>BigStepProofModel</code> with the specified
   * <code>expression</code> as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param pRuleSet the available big step proof rules for the model.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>ruleSet</code> is <code>null</code>.
   * @see AbstractInterpreterProofModel#AbstractInterpreterProofModel(AbstractInterpreterProofNode,
   *      AbstractProofRuleSet)
   */
  public BigStepProofModel ( Expression expression ,
      AbstractProofRuleSet pRuleSet )
  {
    super ( new DefaultBigStepProofNode ( expression ) , pRuleSet ) ;
  }


  //
  // Actions
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( node.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( Messages.getString ( "BigStep.0" ) ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    // try to guess the next rule
    logger.debug ( "Trying to guess a rule for " + node ) ; //$NON-NLS-1$
    for ( ProofRule rule : this.ruleSet.getRules ( ) )
    { // MUST be the getRules() from the ProofRuleSet
      try
      {
        // cast node to a DefaultBigStepProofNode
        DefaultBigStepProofNode current = ( DefaultBigStepProofNode ) node ;
        // (APP) is a special case, because (APP) can always be applied to
        // applications,
        // which can turn into trouble for expressions such as "1 + true"
        if ( rule.getName ( ).equals ( "APP" ) ) //$NON-NLS-1$
        {
          // determine the parent node
          BigStepProofNode parent = current.getParent ( ) ;
          // in order to avoid endless guessing of (APP) for said expressions,
          // we check if the
          // parent node has exactly the same expression and (APP) was applied
          // to it, and if
          // so, skip the (APP) rule here
          if ( parent != null && parent.getRule ( ).equals ( rule )
              && parent.getExpression ( ).equals ( current.getExpression ( ) ) )
          {
            // log the details of the problem...
            logger
                .debug ( MessageFormat
                    .format (
                        "Detected endless guessing of ({0}) for {1}" , rule , current ) ) ; //$NON-NLS-1$
            // ...and skip the (APP) rule for the guess operation
            continue ;
          }
        }
        // try to apply the rule to the specified node
        apply ( ( BigStepProofRule ) rule , current ) ;
        // remember that the user cheated
        setCheating ( true ) ;
        // yep, we did it
        logger.debug ( MessageFormat.format (
            "Successfully applied ({0}) to {1}" , rule , node ) ) ; //$NON-NLS-1$
        return ;
      }
      catch ( ProofRuleException e )
      {
        // rule failed to apply... so, next one, please
        logger.debug ( MessageFormat.format (
            "Failed to apply ({0}) to {1}" , rule , node ) , e ) ; //$NON-NLS-1$
        continue ;
      }
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ) ; //$NON-NLS-1$
    throw new ProofGuessException (
        Messages.getString ( "InterpreterModel.0" ) , node ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void prove ( ProofRule rule , ProofNode node )
      throws ProofRuleException
  {
    if ( rule == null )
    {
      throw new NullPointerException ( "rule is null" ) ; //$NON-NLS-1$
    }
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( node.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( Messages.getString ( "BigStep.0" ) ) ; //$NON-NLS-1$
    }
    // try to apply the rule to the specified node
    apply ( ( BigStepProofRule ) rule , ( DefaultBigStepProofNode ) node ) ;
  }


  //
  // Rule application
  //
  /**
   * Applies the <code>rule</code> to the <code>node</code>.
   * 
   * @param rule the big step proof rule to apply to <code>node</code>.
   * @param node the big step proof node to which to apply the <code>rule</code>.
   * @throws ProofRuleException if the <code>rule</code> could not be applied
   *           to <code>node</code>.
   */
  private void apply ( final BigStepProofRule rule ,
      final DefaultBigStepProofNode node ) throws ProofRuleException
  {
    // allocate a new big step proof context
    DefaultBigStepProofContext context = new DefaultBigStepProofContext ( this ) ;
    try
    {
      // try to apply the rule to the node
      context.apply ( rule , node ) ;
      // check if we are finished
      final BigStepProofNode tmpRoot = ( BigStepProofNode ) getRoot ( ) ;
      context.addRedoAction ( new Runnable ( )
      {
        @ SuppressWarnings ( "synthetic-access" )
        public void run ( )
        {
          setFinished ( tmpRoot.isFinished ( ) ) ;
        }
      } ) ;
      context.addUndoAction ( new Runnable ( )
      {
        @ SuppressWarnings ( "synthetic-access" )
        public void run ( )
        {
          setFinished ( false ) ;
        }
      } ) ;
      // determine the redo and undo actions from the context
      final Runnable redoActions = context.getRedoActions ( ) ;
      final Runnable undoActions = context.getUndoActions ( ) ;
      // record the undo edit action for this proof step
      addUndoableTreeEdit ( new UndoableTreeEdit ( )
      {
        public void redo ( )
        {
          redoActions.run ( ) ;
        }


        public void undo ( )
        {
          undoActions.run ( ) ;
        }
      } ) ;
    }
    catch ( ProofRuleException e )
    {
      // revert the actions performed so far
      context.revert ( ) ;
      // re-throw the exception
      throw e ;
    }
  }


  //
  // Proof context support
  //
  /**
   * Used to implement the
   * {@link BigStepProofContext#addProofNode(BigStepProofNode, Expression, Store)}
   * method of the {@link DefaultBigStepProofContext} class.
   * 
   * @param context the big step proof context.
   * @param node the big step node.
   * @param child the new big step node.
   * @see BigStepProofContext#addProofNode(BigStepProofNode, Expression, Store)
   */
  void contextAddProofNode ( DefaultBigStepProofContext context ,
      final DefaultBigStepProofNode node , final DefaultBigStepProofNode child )
  {
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.add ( child ) ;
        nodesWereInserted ( node , new int [ ]
        { node.getIndex ( child ) } ) ;
      }
    } ) ;
    context.addUndoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        int index = node.getIndex ( child ) ;
        node.remove ( index ) ;
        nodesWereRemoved ( node , new int [ ]
        { index } , new Object [ ]
        { child } ) ;
      }
    } ) ;
  }


  /**
   * Used to implement the
   * {@link BigStepProofContext#setProofNodeResult(BigStepProofNode, BigStepProofResult)}
   * method of the {@link DefaultBigStepProofContext} class.
   * 
   * @param context the big step proof context.
   * @param node the big step node.
   * @param result the big step result.
   * @see BigStepProofContext#setProofNodeResult(BigStepProofNode,
   *      BigStepProofResult)
   */
  void contextSetProofNodeResult ( DefaultBigStepProofContext context ,
      final DefaultBigStepProofNode node , final BigStepProofResult result )
  {
    final BigStepProofResult oldResult = node.getResult ( ) ;
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setResult ( result ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
    context.addUndoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setResult ( oldResult ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
  }


  /**
   * Used to implement the
   * {@link BigStepProofContext#setProofNodeRule(BigStepProofNode, BigStepProofRule)}
   * method of the {@link DefaultBigStepProofContext} class.
   * 
   * @param context the big step proof context.
   * @param node the big step node.
   * @param rule the big step rule.
   * @see BigStepProofContext#setProofNodeRule(BigStepProofNode,
   *      BigStepProofRule)
   */
  void contextSetProofNodeRule ( DefaultBigStepProofContext context ,
      final DefaultBigStepProofNode node , final BigStepProofRule rule )
  {
    final ProofStep [ ] oldSteps = node.getSteps ( ) ;
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setSteps ( new ProofStep [ ]
        { new ProofStep ( node.getExpression ( ) , rule ) } ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
    context.addUndoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setSteps ( oldSteps ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
  }
}
