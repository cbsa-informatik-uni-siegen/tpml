package de.unisiegen.tpml.core.typechecker ;


import java.util.Enumeration ;
import org.apache.log4j.Logger ;
import de.unisiegen.tpml.core.AbstractExpressionProofModel ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.AbstractProofRuleSet ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.TypeVariable ;


/**
 * The heart of the type checker. Type checker proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet}
 * that is passed to the constructor.
 * 
 * @author Benedikt Meurer
 * @version $Rev:878M $
 * @see de.unisiegen.tpml.core.AbstractExpressionProofModel
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
public class TypeCheckerProofModel extends AbstractExpressionProofModel
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
      .getLogger ( TypeCheckerProofModel.class ) ;


  //
  // Attributes
  //
  /**
   * The current proof index, which indicates the number of steps that have been
   * performed on the proof model so far (starting with one), and is used to
   * generate new unique type variables in the associated contexts.
   * 
   * @see #getIndex()
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  private int index = 1 ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>TypeCheckerProofModel</code> with the specified
   * <code>expression</code> as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param pRuleSet the available type rules for the model.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>ruleSet</code> is <code>null</code>.
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode,
   *      AbstractProofRuleSet)
   */
  public TypeCheckerProofModel ( Expression expression ,
      AbstractTypeCheckerProofRuleSet pRuleSet )
  {
    super (
        new DefaultTypeCheckerExpressionProofNode (
            new DefaultTypeEnvironment ( ) , expression , new TypeVariable ( 1 ,
                0 ) ) , pRuleSet ) ;
  }


  //
  // Accessors
  //
  /**
   * Returns the current proof model index, which is the number of steps already
   * performed on the model (starting with one) and used to allocate new, unique
   * {@link  de.unisiegen.tpml.core.types.TypeVariable}s. It is incremented
   * with every proof step performed on the model.
   * 
   * @return the current index of the proof model.
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  /**
   * Sets the current proof model index. This is a support operation, called by
   * {@link DefaultTypeCheckerProofContext} whenever a new proof context is
   * allocated.
   * 
   * @param pIndex the new index for the proof model.
   * @see #getIndex()
   * @see DefaultTypeCheckerProofContext
   * @see DefaultTypeCheckerProofContext#DefaultTypeCheckerProofContext(TypeCheckerProofModel)
   */
  public void setIndex ( int pIndex )
  {
    if ( pIndex < 1 )
    {
      throw new IllegalArgumentException ( "index is invalid" ) ; //$NON-NLS-1$
    }
    this.index = pIndex ;
  }


  //
  // Actions
  //
  /**
   * {@inheritDoc}
   * 
   * @see #guessWithType(ProofNode, MonoType)
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
    guessInternal ( ( AbstractTypeCheckerProofNode ) node , null ) ;
  }


  /**
   * Guesses the next proof step for the specified <code>node</code> using the
   * <code>type</code> for the <code>node</code>. This method is used for
   * the <tt>"Enter type"</tt> action in the type checker user interface. The
   * <code>node</code> must not be already proven (see the
   * {@link ProofNode#isProven()} method for details), otherwise an
   * {@link IllegalStateException} is thrown.
   * 
   * @param node the {@link ProofNode} for which the next proof step should be
   *          guessed.
   * @param type the type for the node
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this model.
   * @throws IllegalStateException if for some reason <code>node</code> cannot
   *           be proven.
   * @throws NullPointerException if <code>node</code> or <code>type</code>
   *           is <code>null</code>.
   * @throws ProofGuessException if the next proof step could not be guessed.
   * @see #guess(ProofNode)
   * @see #prove(ProofRule, ProofNode)
   */
  public void guessWithType ( ProofNode node , MonoType type )
      throws ProofGuessException
  {
    if ( type == null )
    {
      throw new NullPointerException ( "type is null" ) ; //$NON-NLS-1$
    }
    // guess the rule for the node utilizing the type
    guessInternal ( ( AbstractTypeCheckerProofNode ) node , type ) ;
    // try to complete the node
    complete ( node ) ;
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
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    // try to apply the rule to the specified node
    applyInternal ( ( TypeCheckerProofRule ) rule ,
        ( AbstractTypeCheckerProofNode ) node , null ) ;
  }


  //
  // Rule application
  //
  /**
   * Applies the specified proof <code>rule</code> to the given
   * <code>node</code> in this proof model.
   * 
   * @param rule the type proof rule to apply.
   * @param node the type proof node to which to apply the <code>rule</code>.
   * @param type the type the user guessed for the <code>node</code> or
   *          <code>null</code> if the user didn't enter a type.
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed.
   * @see #guess(ProofNode)
   * @see #prove(ProofRule, ProofNode)
   */
  private void applyInternal ( TypeCheckerProofRule rule ,
      AbstractTypeCheckerProofNode node , MonoType type )
      throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    DefaultTypeCheckerProofContext context ;
    context = new DefaultTypeCheckerProofContext ( this ) ;
    try
    {
      // try to apply the rule to the node
      context.apply ( rule , node , type ) ;
      // check if we are finished
      final TypeCheckerProofNode rootNode = ( TypeCheckerProofNode ) getRoot ( ) ;
      context.addRedoAction ( new Runnable ( )
      {
        @ SuppressWarnings ( "synthetic-access" )
        public void run ( )
        {
          setFinished ( rootNode.isFinished ( ) ) ;
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
    catch ( UnificationException e )
    {
      // revert the actions performed so far
      context.revert ( ) ;
      // re-throw the exception as proof rule exception
      throw new ProofRuleException ( node , rule , e ) ;
    }
    catch ( RuntimeException e )
    {
      // revert the actions performed so far
      context.revert ( ) ;
      // re-throw the exception
      throw e ;
    }
  }


  /**
   * Implementation of the {@link #guess(ProofNode)} and
   * {@link #guessWithType(ProofNode, MonoType)} methods.
   * 
   * @param node the proof node for which to guess the next step.
   * @param type the type that the user entered for this <code>node</code> or
   *          <code>null</code> to let the type inference algorithm guess the
   *          type.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this model.
   * @throws IllegalStateException if for some reason <code>node</code> cannot
   *           be proven.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @throws ProofGuessException if the next proof step could not be guessed.
   * @see #guess(ProofNode)
   * @see #guessWithType(ProofNode, MonoType)
   */
  private void guessInternal ( AbstractTypeCheckerProofNode node , MonoType type )
      throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( node.getSteps ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
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
        // try to apply the rule to the specified node
        applyInternal ( ( TypeCheckerProofRule ) rule , node , type ) ;
        // remember that the user cheated
        setCheating ( true ) ;
        // yep, we did it
        logger.debug ( "Successfully applied (" + rule + ") to " + node ) ; //$NON-NLS-1$ //$NON-NLS-2$
        return ;
      }
      catch ( ProofRuleException e )
      {
        // rule failed to apply... so, next one, please
        logger.debug ( "Failed to apply (" + rule + ") to " + node , e ) ; //$NON-NLS-1$ //$NON-NLS-2$
        continue ;
      }
      catch ( RuntimeException e )
      {
        throw new ProofGuessException ( e.getMessage ( ) , node ) ;
      }
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ) ; //$NON-NLS-1$
    throw new ProofGuessException ( node ) ;
  }


  //
  // Proof context support
  //
  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code>, for the <code>environment</code>,
   * <code>expression</code> and <code>type</code>.
   * 
   * @param context the <code>TypeCheckerProofContext</code> on which the
   *          action is to be performed.
   * @param node the parent <code>DefaultTypeCheckerProofNode</code>.
   * @param environment the <code>TypeEnvironment</code> for the child node.
   * @param expression the <code>Expression</code> for the child node.
   * @param type the type variable or the concrete type for the child node, used
   *          for the unification.
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void contextAddProofNode ( DefaultTypeCheckerProofContext context ,
      final AbstractTypeCheckerProofNode node , TypeEnvironment environment ,
      Expression expression , MonoType type )
  {
    if ( context == null )
    {
      throw new NullPointerException ( "context is null" ) ; //$NON-NLS-1$
    }
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( environment == null )
    {
      throw new NullPointerException ( "environment is null" ) ; //$NON-NLS-1$
    }
    if ( expression == null )
    {
      throw new NullPointerException ( "expression is null" ) ; //$NON-NLS-1$
    }
    if ( type == null )
    {
      throw new NullPointerException ( "type is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ) ; //$NON-NLS-1$
    }
    final DefaultTypeCheckerExpressionProofNode child = new DefaultTypeCheckerExpressionProofNode (
        environment , expression , type ) ;
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
        int nodeIndex = node.getIndex ( child ) ;
        node.remove ( nodeIndex ) ;
        nodesWereRemoved ( node , new int [ ]
        { nodeIndex } , new Object [ ]
        { child } ) ;
      }
    } ) ;
  }


  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code> and for the <code>type</code>s.
   * 
   * @param context the context calling this method
   * @param node the parent node to add this child
   * @param type the first type of the new child node
   * @param type2 the second type of the new child node
   */
  public void contextAddProofNode ( DefaultTypeCheckerProofContext context ,
      final AbstractTypeCheckerProofNode node , MonoType type , MonoType type2 )
  {
    if ( context == null )
    {
      throw new NullPointerException ( "context is null" ) ; //$NON-NLS-1$
    }
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( type == null )
    {
      throw new NullPointerException ( "expression is null" ) ; //$NON-NLS-1$
    }
    if ( type2 == null )
    {
      throw new NullPointerException ( "type is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ) ; //$NON-NLS-1$
    }
    final DefaultTypeCheckerTypeProofNode child = new DefaultTypeCheckerTypeProofNode (
        type , type2 ) ;
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
        int nodeIndex = node.getIndex ( child ) ;
        node.remove ( nodeIndex ) ;
        nodesWereRemoved ( node , new int [ ]
        { nodeIndex } , new Object [ ]
        { child } ) ;
      }
    } ) ;
  }


  /**
   * Perform the given substitution to the given node
   * 
   * @param context the proof context calling this mehtod
   * @param s the {@link TypeSubstitution} to apply to all nodes in the proof
   *          tree.
   * @throws NullPointerException if <code>s</code> is <code>null</code>.
   */
  @ SuppressWarnings ( "unchecked" )
  void contextApplySubstitution ( DefaultTypeCheckerProofContext context ,
      TypeSubstitution s )
  {
    if ( s == null )
    {
      throw new NullPointerException ( "s is null" ) ; //$NON-NLS-1$
    }
    // apply the substitution s to all nodes in the proof node
    Enumeration nodes = this.root.postorderEnumeration ( ) ;
    while ( nodes.hasMoreElements ( ) )
    {
      // determine the previous settings for the node
      final AbstractTypeCheckerProofNode nextNode = ( AbstractTypeCheckerProofNode ) nodes
          .nextElement ( ) ;
      if ( nextNode instanceof DefaultTypeCheckerExpressionProofNode )
      {
        final DefaultTypeCheckerExpressionProofNode node = ( DefaultTypeCheckerExpressionProofNode ) nextNode ;
        final TypeEnvironment oldEnvironment = node.getEnvironment ( ) ;
        final Expression oldExpression = node.getExpression ( ) ;
        final MonoType oldType = node.getType ( ) ;
        // determine the new settings for the node
        final TypeEnvironment newEnvironment = oldEnvironment.substitute ( s ) ;
        final Expression newExpression = oldExpression.substitute ( s ) ;
        final MonoType newType = oldType.substitute ( s ) ;
        // check if the old and new settings differ
        if ( ! oldEnvironment.equals ( newEnvironment )
            || ! oldExpression.equals ( newExpression )
            || ! oldType.equals ( newType ) )
        {
          // add the redo action for the substitution
          context.addRedoAction ( new Runnable ( )
          {
            @ SuppressWarnings ( "synthetic-access" )
            public void run ( )
            {
              node.setEnvironment ( newEnvironment ) ;
              node.setExpression ( newExpression ) ;
              node.setType ( newType ) ;
              nodeChanged ( node ) ;
            }
          } ) ;
          // add the undo action for the substitution
          context.addUndoAction ( new Runnable ( )
          {
            @ SuppressWarnings ( "synthetic-access" )
            public void run ( )
            {
              node.setEnvironment ( oldEnvironment ) ;
              node.setExpression ( oldExpression ) ;
              node.setType ( oldType ) ;
              nodeChanged ( node ) ;
            }
          } ) ;
        }
      }
    }
  }


  /**
   * Used to implement the
   * {@link DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule, TypeCheckerProofNode, MonoType)}
   * method of the {@link DefaultTypeCheckerProofContext} class.
   * 
   * @param context the type checker proof context.
   * @param node the type checker node.
   * @param rule the type checker rule.
   * @see DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule,
   *      TypeCheckerProofNode, MonoType)
   */
  public void contextSetProofNodeRule ( DefaultTypeCheckerProofContext context ,
      final AbstractTypeCheckerProofNode node , final TypeCheckerProofRule rule )
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
