package de.unisiegen.tpml.core.bigstepclosure;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * TODO
 *
 */
public final class BigStepClosureProofModel extends AbstractInterpreterProofModel
{
  public BigStepClosureProofModel( Expression expression,
      AbstractProofRuleSet pRuleSet)
  {
    super( new DefaultBigStepClosureProofNode (expression), pRuleSet);
  }


  public void setOverlap(int i)
  {
    
  }
  
  public void guess(ProofNode node) throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( node.getRules ().length > 0 )
    {
      throw new IllegalArgumentException ( Messages.getString ( "BigStep.0" ) ); //$NON-NLS-1$
    }
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
    }
    // try to guess the next rule
    logger.debug ( "Trying to guess a rule for " + node ); //$NON-NLS-1$
    for ( ProofRule rule : this.ruleSet.getRules () )
    { // MUST be the getRules() from the ProofRuleSet
      try
      {
        // cast node to a DefaultBigStepProofNode
        DefaultBigStepClosureProofNode current = ( DefaultBigStepClosureProofNode ) node;
        // (APP) is a special case, because (APP) can always be applied to
        // applications,
        // which can turn into trouble for expressions such as "1 + true"
        if ( rule.getName ().equals ( "APP" ) ) //$NON-NLS-1$
        {
          // determine the parent node
          BigStepClosureProofNode parent = current.getParent ();
          // in order to avoid endless guessing of (APP) for said expressions,
          // we check if the
          // parent node has exactly the same expression and (APP) was applied
          // to it, and if
          // so, skip the (APP) rule here
          if ( parent != null && parent.getRule ().equals ( rule )
              && parent.getExpression ().equals ( current.getExpression () ) )
          {
            // log the details of the problem...
            logger.debug ( MessageFormat.format (
                "Detected endless guessing of ({0}) for {1}", rule, current ) ); //$NON-NLS-1$
            // ...and skip the (APP) rule for the guess operation
            continue;
          }
        }
        // try to apply the rule to the specified node
        apply ( ( BigStepClosureProofRule ) rule, current );
        // remember that the user cheated
        setCheating ( true );
        // yep, we did it
        logger.debug ( MessageFormat.format (
            "Successfully applied ({0}) to {1}", rule, node ) ); //$NON-NLS-1$
        return;
      }
      catch ( ProofRuleException e )
      {
        // rule failed to apply... so, next one, please
        logger.debug ( MessageFormat.format (
            "Failed to apply ({0}) to {1}", rule, node ), e ); //$NON-NLS-1$
        continue;
      }
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ); //$NON-NLS-1$
    throw new ProofGuessException (
        Messages.getString ( "InterpreterModel.0" ), node ); //$NON-NLS-1$
  }
  
  public void setPages(int i)
  {
    
  }
  
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException
  {
    apply ( ( BigStepClosureProofRule ) rule, ( DefaultBigStepClosureProofNode ) node );
  }
  
  private void apply ( final BigStepClosureProofRule rule,
      final DefaultBigStepClosureProofNode node ) throws ProofRuleException
  {
    // allocate a new big step proof context
    DefaultBigStepClosureProofContext context = new DefaultBigStepClosureProofContext ( this );
    try
    {
      // try to apply the rule to the node
      context.apply ( rule, node );
      // check if we are finished
      final BigStepClosureProofNode tmpRoot = ( BigStepClosureProofNode ) getRoot ();
      context.addRedoAction ( new Runnable ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void run ()
        {
          setFinished ( tmpRoot.isFinished () );
        }
      } );
      context.addUndoAction ( new Runnable ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void run ()
        {
          setFinished ( false );
        }
      } );
      // determine the redo and undo actions from the context
      final Runnable redoActions = context.getRedoActions ();
      final Runnable undoActions = context.getUndoActions ();
      // record the undo edit action for this proof step
      addUndoableTreeEdit ( new UndoableTreeEdit ()
      {

        public void redo ()
        {
          redoActions.run ();
        }


        public void undo ()
        {
          undoActions.run ();
        }
      } );
    }
    catch ( ProofRuleException e )
    {
      e.printStackTrace ();
      // revert the actions performed so far
      context.revert ();
      // re-throw the exception
      throw e;
    }
  }
  
  public void contextAddProofNode ( DefaultBigStepClosureProofContext context,
      final DefaultBigStepClosureProofNode node, final DefaultBigStepClosureProofNode child )
  {
    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.add ( child );
        nodesWereInserted ( node, new int []
        { node.getIndex ( child ) } );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        int index = node.getIndex ( child );
        node.remove ( index );
        nodesWereRemoved ( node, new int []
        { index }, new Object []
        { child } );
      }
    } );
  }
  
  public void contextSetProofNodeResult ( DefaultBigStepClosureProofContext context,
      final DefaultBigStepClosureProofNode node, final BigStepClosureProofResult result )
  {
    final BigStepClosureProofResult oldResult = node.getResult ();
    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setResult ( result );
        nodeChanged ( node );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setResult ( oldResult );
        nodeChanged ( node );
      }
    } );
  }
  
  void contextSetProofNodeRule ( DefaultBigStepClosureProofContext context,
      final DefaultBigStepClosureProofNode node, final BigStepClosureProofRule rule )
  {
    final ProofStep [] oldSteps = node.getSteps ();
    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setSteps ( new ProofStep []
        { new ProofStep ( node.getExpression (), rule ) } );
        nodeChanged ( node );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setSteps ( oldSteps );
        nodeChanged ( node );
      }
    } );
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null;
  }
  
  public LatexCommandList getLatexCommands()
  {
    return null;
  }
  
  public LatexInstructionList getLatexInstructions()
  {
    return null;
  }
  
  public LatexStringBuilder toLatexStringBuilder(LatexStringBuilderFactory fac, int i)
  {
    return null;
  }
  
  public LatexString toLatexString()
  {
    return null;
  }
  
  public PrettyString toPrettyString()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory fac)
  {
    return null;
  }
  
  private static final Logger logger = Logger
  .getLogger ( BigStepClosureProofModel.class );
}
