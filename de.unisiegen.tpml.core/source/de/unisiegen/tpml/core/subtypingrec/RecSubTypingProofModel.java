package de.unisiegen.tpml.core.subtypingrec ;


import java.text.MessageFormat ;
import org.apache.log4j.Logger ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.AbstractProofRuleSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;
import de.unisiegen.tpml.core.subtyping.ProofStep ;
import de.unisiegen.tpml.core.subtyping.SubTypingModel ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The heart of the subtyping algorithm. Subtyping proof rules are supplied via
 * an {@link de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet}
 * that is passed to the constructor.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofContext
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode
 */
public class RecSubTypingProofModel extends AbstractProofModel implements
    SubTypingModel
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
      .getLogger ( RecSubTypingProofModel.class ) ;


  /**
   * The choosen mode (advanced or beginner)
   */
  private boolean mode = true ;


  /**
   * The ruleSet for this proof model
   */
  AbstractRecSubTypingProofRuleSet ruleSet ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>SubTypingProofModel</code> with the specified
   * <code>type</code> and <code>type2</type> as its root node.
   * 
   * @param type the first {@link MonoType} for the root node.
   * @param type2 the second {@link MonoType} for the root node.
   * @param pRuleSet the available type rules for the model.
   * @param pMode the chosen mode (Advanced or Beginner)
   * 
   * @throws NullPointerException if either one <code>type</code> or <code>ruleSet</code> is
   *                              <code>null</code>.
   *
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode, AbstractProofRuleSet)
   */
  public RecSubTypingProofModel ( MonoType type , MonoType type2 ,
      AbstractRecSubTypingProofRuleSet pRuleSet , boolean pMode )
  {
    super ( new DefaultRecSubTypingProofNode ( type , type2 ,
        new SeenTypes < DefaultSubType > ( ) ) , pRuleSet ) ;
    this.ruleSet = pRuleSet ;
    this.mode = pMode ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode pNode ) throws ProofGuessException
  {
    DefaultRecSubTypingProofNode node = ( DefaultRecSubTypingProofNode ) pNode ;
    guessInternal ( node ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void prove ( ProofRule rule , ProofNode pNode )
      throws ProofRuleException
  {
    if ( ! this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( pNode ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( pNode.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    DefaultRecSubTypingProofNode node = ( DefaultRecSubTypingProofNode ) pNode ;
    // try to apply the rule to the specified node
    applyInternal ( ( RecSubTypingProofRule ) rule , node ) ;
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
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed.
   * @see #guess(ProofNode)
   * @see #prove(ProofRule, ProofNode)
   */
  private void applyInternal ( RecSubTypingProofRule rule ,
      DefaultRecSubTypingProofNode node ) throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    DefaultRecSubTypingProofContext context = new DefaultRecSubTypingProofContext (
        this , node ) ;
    try
    {
      context.apply ( rule , node ) ;
      // check if we are finished
      final DefaultRecSubTypingProofNode modelRoot = getRoot ( ) ;
      context.addRedoAction ( new Runnable ( )
      {
        @ SuppressWarnings ( "synthetic-access" )
        public void run ( )
        {
          setFinished ( modelRoot.isFinished ( ) ) ;
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
    catch ( RuntimeException e )
    {
      // revert the actions performed so far
      context.revert ( ) ;
      // re-throw the exception
      throw e ;
    }
  }


  /**
   * Implementation of the {@link #guess(ProofNode)} method.
   * 
   * @param node the proof node for which to guess the next step.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this model.
   * @throws IllegalStateException if for some reason <code>node</code> cannot
   *           be proven.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @throws ProofGuessException if the next proof step could not be guessed.
   * @see #guess(ProofNode)
   */
  private void guessInternal ( DefaultRecSubTypingProofNode node )
      throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( node.getSteps ( ).length > 0 )
    {
      throw new IllegalArgumentException ( MessageFormat.format ( Messages
          .getString ( "IllegalArgumentException.0" ) , node ) ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( MessageFormat.format ( Messages
          .getString ( "IllegalArgumentException.1" ) , node ) ) ; //$NON-NLS-1$
    }
    // try to guess the next rule
    logger.debug ( "Trying to guess a rule for " + node ) ; //$NON-NLS-1$
    for ( ProofRule rule : this.ruleSet.getRules ( ) )
    {
      try
      {
        // try to apply the rule to the specified node
        applyInternal ( ( RecSubTypingProofRule ) rule , node ) ;
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
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ) ; //$NON-NLS-1$
    throw new ProofGuessException ( MessageFormat.format ( Messages
        .getString ( "ProofGuessException.0" ) , node ) , node ) ; //$NON-NLS-1$
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
   * @param pNode the parent <code>DefaultTypeInferenceProofNode</code>.
   * @param type the first concrete type for the child node, used for the
   *          subtype algorithm.
   * @param type2 the second concrete type for the child node, used for the
   *          subtype algorithm.
   * @param seenTypes a list with already seen types
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  void contextAddProofNode ( final DefaultRecSubTypingProofContext context ,
      final RecSubTypingProofNode pNode , final MonoType type ,
      final MonoType type2 , final SeenTypes < DefaultSubType > seenTypes )
  {
    final DefaultRecSubTypingProofNode node = ( DefaultRecSubTypingProofNode ) pNode ;
    final DefaultRecSubTypingProofNode child = new DefaultRecSubTypingProofNode (
        type , type2 , seenTypes ) ;
    // add redo and undo options
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
   * {@link DefaultRecSubTypingProofContext#apply(RecSubTypingProofRule, DefaultRecSubTypingProofNode)}
   * method of the {@link DefaultTypeInferenceProofContext} class.
   * 
   * @param context the type inference proof context.
   * @param node the type inference node.
   * @param rule the type checker rule.
   * @see DefaultRecSubTypingProofContext#apply(RecSubTypingProofRule,
   *      DefaultRecSubTypingProofNode)
   */
  void contextSetProofNodeRule ( DefaultRecSubTypingProofContext context ,
      final DefaultRecSubTypingProofNode node , final RecSubTypingProofRule rule )
  {
    final ProofStep [ ] oldSteps = node.getSteps ( ) ;
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setSteps ( new ProofStep [ ]
        { new ProofStep ( node.getType ( ) , node.getType2 ( ) , rule ) } ) ;
        ProofRule [ ] rules = new ProofRule [ 1 ] ;
        rules [ 0 ] = rule ;
        node.setRules ( rules ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
    context.addUndoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setSteps ( oldSteps ) ;
        ProofRule [ ] rules = null ;
        node.setRules ( rules ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
  }


  /**
   * get the rules of the actual proof rule set
   * 
   * @return ProofRuleSet[] with all rules
   * @see de.unisiegen.tpml.core.AbstractProofModel#getRules()
   */
  @ Override
  public ProofRule [ ] getRules ( )
  {
    return this.ruleSet.getRules ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#getRoot()
   */
  @ Override
  public DefaultRecSubTypingProofNode getRoot ( )
  {
    return ( DefaultRecSubTypingProofNode ) super.getRoot ( ) ;
  }


  /**
   * set a new root for this model
   * 
   * @param type first MonoType of the new root
   * @param type2 second MonoType of the new root
   * @param seenTypes list of the already seen subtypes
   */
  public void setRoot ( MonoType type , MonoType type2 ,
      SeenTypes < DefaultSubType > seenTypes )
  {
    this.root = new DefaultRecSubTypingProofNode ( type , type2 , seenTypes ) ;
  }


  /**
   * Set the mode (Beginner, Advanced) of choosen by the user
   * 
   * @param pMode boolean, true means advanced, false beginner mode
   */
  public void setMode ( boolean pMode )
  {
    if ( this.mode != pMode )
    {
      this.mode = pMode ;
      if ( this.ruleSet.getLanguage ( ).getName ( ).equalsIgnoreCase ( "l2o" ) ) { //$NON-NLS-1$
        if ( pMode )
        {
          this.ruleSet.unregister ( "TRANS" ) ; //$NON-NLS-1$
          this.ruleSet.unregister ( "OBJECT-WIDTH" ) ; //$NON-NLS-1$
          this.ruleSet.unregister ( "OBJECT-DEPTH" ) ; //$NON-NLS-1$
          this.ruleSet.unregister ( "REFL" ) ; //$NON-NLS-1$
          this.ruleSet.registerByMethodName ( L2OLanguage.L2O ,
              "OBJECT" , "applyObject" ) ; //$NON-NLS-1$ //$NON-NLS-2$
          this.ruleSet.registerByMethodName ( L1Language.L1 ,
              "REFL" , "applyRefl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
          this.ruleSet.unregister ( "OBJECT" ) ; //$NON-NLS-1$
          this.ruleSet.unregister ( "REFL" ) ; //$NON-NLS-1$
          this.ruleSet.registerByMethodName ( L2OLanguage.L2O ,
              "TRANS" , "applyTrans" ) ; //$NON-NLS-1$ //$NON-NLS-2$
          this.ruleSet.registerByMethodName ( L2OLanguage.L2O ,
              "OBJECT-WIDTH" , "applyObjectWidth" ) ; //$NON-NLS-1$ //$NON-NLS-2$
          this.ruleSet.registerByMethodName ( L2OLanguage.L2O ,
              "OBJECT-DEPTH" , "applyObjectDepth" ) ; //$NON-NLS-1$ //$NON-NLS-2$
          this.ruleSet.registerByMethodName ( L1Language.L1 ,
              "REFL" , "applyRefl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
    }
  }
}
