package de.unisiegen.tpml.core.minimaltyping ;


import java.util.TreeSet ;
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
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The heart of the type checker. Minimal typing proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet}
 * that is passed to the constructor.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.AbstractExpressionProofModel
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode
 */
public class MinimalTypingProofModel extends AbstractExpressionProofModel
{
  /**
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger
      .getLogger ( MinimalTypingProofModel.class ) ;


  /**
   * The side overlapping for latex export
   */
  int overlap = 0 ;


  /**
   * The number of pages for latex export
   */
  int pages = 5 ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_MKTREE , 1 ,
        "\\stepcounter{tree} #1 \\arrowstrue #1 \\arrowsfalse" , "tree" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_ARROW ,
            3 ,
            "\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\ncangle[angleA=-90,angleB=#1]{<-}{\\thetree.#2}{\\thetree.#3}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\else" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\fi" , "angle" , "from" , "to" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( new DefaultLatexInstruction ( "\\newcounter{tree}" ) ) ; //$NON-NLS-1$
    instructions
        .add ( new DefaultLatexInstruction ( "\\newcounter{node}[tree]" ) ) ; //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newlength{\\treeindent}" ) ) ; //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newlength{\\nodeindent}" ) ) ; //$NON-NLS-1$
    instructions
        .add ( new DefaultLatexInstruction ( "\\newlength{\\nodesep}" ) ) ; //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newif\\ifarrows  " + LATEX_LINE_BREAK_SOURCE_CODE //$NON-NLS-1$
            + "\\arrowsfalse" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static TreeSet < LatexPackage > getLatexPackagesStatic ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "longtable" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amsmath" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "pstricks" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "pst-node" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amstext" ) ) ; //$NON-NLS-1$
    return packages ;
  }


  /**
   * The current proof index, which indicates the number of steps that have been
   * performed on the proof model so far (starting with one), and is used to
   * generate new unique type variables in the associated contexts.
   * 
   * @see #getIndex()
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  private int index = 1 ;


  /**
   * The rule set for this proof model
   */
  AbstractMinimalTypingProofRuleSet ruleSet ;


  /**
   * The choosen mode (advanced or beginner)
   */
  private boolean mode ;


  /**
   * Allocates a new <code>MinimalTypingProofModel</code> with the specified
   * <code>expression</code> as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param pRuleSet the available type rules for the model.
   * @param pMode The choosen mode (advanced or beginner)
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>ruleSet</code> is <code>null</code>.
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode,
   *      AbstractProofRuleSet)
   */
  public MinimalTypingProofModel ( Expression expression ,
      AbstractMinimalTypingProofRuleSet pRuleSet , boolean pMode )
  {
    super ( new DefaultMinimalTypingExpressionProofNode (
        new DefaultTypeEnvironment ( ) , expression ) , pRuleSet ) ;
    this.mode = pMode ;
    this.ruleSet = pRuleSet ;
  }


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
  private void applyInternal ( MinimalTypingProofRule rule ,
      MinimalTypingProofNode node , MonoType type ) throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    DefaultMinimalTypingProofContext context ;
    context = new DefaultMinimalTypingProofContext ( this ) ;
    try
    {
      // try to apply the rule to the node
      context.apply ( rule , node , type ) ;
      // check if we are finished
      final MinimalTypingProofNode modelRoot = ( MinimalTypingProofNode ) getRoot ( ) ;
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
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code>, for the <code>type</code> and <code>type2</code>.
   * 
   * @param context the <code>MinimalTypingProofContext</code> on which the
   *          action is to be performed.
   * @param node the parent <code>DefaultMinimalTypingProofNode</code>.
   * @param type the first concrete type for type Comparison.
   * @param type2 the second concrete type for type Comparison.
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void contextAddProofNode ( DefaultMinimalTypingProofContext context ,
      final AbstractMinimalTypingProofNode node , MonoType type , MonoType type2 )
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
      throw new NullPointerException ( "type is null" ) ; //$NON-NLS-1$
    }
    if ( type2 == null )
    {
      throw new NullPointerException ( "type2 is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ) ; //$NON-NLS-1$
    }
    final DefaultMinimalTypingTypesProofNode child = new DefaultMinimalTypingTypesProofNode (
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
        int finalIndex = node.getIndex ( child ) ;
        node.remove ( finalIndex ) ;
        nodesWereRemoved ( node , new int [ ]
        { finalIndex } , new Object [ ]
        { child } ) ;
      }
    } ) ;
  }


  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code>, for the <code>environment</code>,
   * <code>expression</code> and <code>type</code>.
   * 
   * @param context the <code>MinimalTypingProofContext</code> on which the
   *          action is to be performed.
   * @param node the parent <code>DefaultMinimalTypingProofNode</code>.
   * @param environment the <code>TypeEnvironment</code> for the child node.
   * @param expression the <code>Expression</code> for the child node.
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void contextAddProofNode ( DefaultMinimalTypingProofContext context ,
      final AbstractMinimalTypingProofNode node , TypeEnvironment environment ,
      Expression expression )
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
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ) ; //$NON-NLS-1$
    }
    final DefaultMinimalTypingExpressionProofNode child = new DefaultMinimalTypingExpressionProofNode (
        environment , expression ) ;
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
        int finalIndex = node.getIndex ( child ) ;
        node.remove ( finalIndex ) ;
        nodesWereRemoved ( node , new int [ ]
        { finalIndex } , new Object [ ]
        { child } ) ;
      }
    } ) ;
  }


  /**
   * Used to implement the
   * {@link DefaultMinimalTypingProofContext#apply(MinimalTypingProofRule, MinimalTypingProofNode, MonoType)}
   * method of the {@link DefaultMinimalTypingProofContext} class.
   * 
   * @param context the type checker proof context.
   * @param node the type checker node.
   * @param rule the type checker rule.
   * @see DefaultMinimalTypingProofContext#apply(MinimalTypingProofRule,
   *      MinimalTypingProofNode, MonoType)
   */
  public void contextSetProofNodeRule (
      DefaultMinimalTypingProofContext context ,
      final AbstractMinimalTypingProofNode node ,
      final MinimalTypingProofRule rule )
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


  /**
   * Set the type of the pNode
   * 
   * @param context minimal typing proof context
   * @param pNode the node to set the type
   * @param type the new type
   */
  public void contextSetProofNodeType (
      DefaultMinimalTypingProofContext context ,
      AbstractMinimalTypingProofNode pNode , final MonoType type )
  {
    final DefaultMinimalTypingExpressionProofNode node = ( DefaultMinimalTypingExpressionProofNode ) pNode ;
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setType ( type ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
    context.addUndoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setType ( null ) ;
        nodeChanged ( node ) ;
      }
    } ) ;
  }


  /**
   * Returns the current proof model index, which is the number of steps already
   * performed on the model (starting with one). It is incremented with every
   * proof step performed on the model.
   * 
   * @return the current index of the proof model.
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    commands
        .add ( getLatexCommandsInternal ( ( MinimalTypingProofNode ) this.root ) ) ;
    return commands ;
  }


  /**
   * Returns a set of needed latex commands for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex commands for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexCommandList getLatexCommandsInternal (
      MinimalTypingProofNode pNode )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( pNode.getLatexCommands ( ) ) ;
    commands.add ( pNode.getRule ( ) ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      commands.add ( getLatexCommandsInternal ( pNode.getChildAt ( i ) ) ) ;
    }
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions
        .add ( getLatexInstructionsInternal ( ( MinimalTypingProofNode ) this.root ) ) ;
    return instructions ;
  }


  /**
   * Returns a set of needed latex instructions for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex instructions for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexInstructionList getLatexInstructionsInternal (
      MinimalTypingProofNode pNode )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( pNode ) ;
    instructions.add ( pNode.getRule ( ) ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      instructions
          .add ( getLatexInstructionsInternal ( pNode.getChildAt ( i ) ) ) ;
    }
    return instructions ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexPackages()
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : getLatexPackagesInternal ( ( MinimalTypingProofNode ) this.root ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * Returns a set of needed latex packages for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex packages for the given latex printable
   *         {@link ProofNode}.
   */
  private TreeSet < LatexPackage > getLatexPackagesInternal (
      MinimalTypingProofNode pNode )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : pNode.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexPackage pack : pNode.getRule ( ).getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexPackage pack : getLatexPackagesInternal ( pNode
          .getChildAt ( i ) ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
    guessInternal ( ( MinimalTypingProofNode ) node , null ) ;
  }


  /**
   * Implementation of the {@link #guess(ProofNode)} method.
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
   */
  private void guessInternal ( MinimalTypingProofNode node , MonoType type )
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
        applyInternal ( ( MinimalTypingProofRule ) rule , node , type ) ;
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
    applyInternal ( ( MinimalTypingProofRule ) rule ,
        ( MinimalTypingProofNode ) node , null ) ;
  }


  /**
   * Sets the current proof model index. This is a support operation, called by
   * {@link DefaultMinimalTypingProofContext} whenever a new proof context is
   * allocated.
   * 
   * @param pIndex the new index for the proof model.
   * @see #getIndex()
   * @see DefaultMinimalTypingProofContext
   */
  public void setIndex ( int pIndex )
  {
    if ( pIndex < 1 )
    {
      throw new IllegalArgumentException ( "index is invalid" ) ; //$NON-NLS-1$
    }
    this.index = pIndex ;
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
      if ( pMode )
      {
        this.ruleSet.unregister ( "ARROW" ) ; //$NON-NLS-1$
        this.ruleSet.unregister ( "S-MU-LEFT" ) ; //$NON-NLS-1$
        this.ruleSet.unregister ( "S-MU-RIGHT" ) ; //$NON-NLS-1$
        this.ruleSet.unregister ( "REFL" ) ; //$NON-NLS-1$
        this.ruleSet.unregister ( "S-ASSUME" ) ; //$NON-NLS-1$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "SUBTYPE" , "applySubtype" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      }
      else
      {
        this.ruleSet.unregister ( "SUBTYPE" ) ; //$NON-NLS-1$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "ARROW" , "applyArrow" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "S-MU-LEFT" , "applyMuLeft" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "S-MU-RIGHT" , "applyMuRight" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "REFL" , "applyRefl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleSet.registerByMethodName ( L1Language.L1 ,
            "S-ASSUME" , "applyAssume" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        pIndent ) ;
    {
      builder.addText ( "\\treeindent=0mm" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      builder.addText ( "\\nodeindent=7mm" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      builder.addText ( "\\nodesep=2mm" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      builder
          .addText ( "\\newcommand{\\longtext}[1]{\\oddsidemargin=#1\\enlargethispage{840mm}" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      builder.addText ( "\\mktree{" ) ; //$NON-NLS-1$
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory , builder ,
          this.root , pIndent + LATEX_INDENT , - 1 ) ;
    }
    builder.addText ( "}" ) ; //$NON-NLS-1$
    builder.addText ( "}" ) ; //$NON-NLS-1$
    builder.addText ( "\\longtext{-30pt}" ) ; //$NON-NLS-1$
    for ( int i = 1 ; i < this.pages ; i ++ )
    {
      builder.addSourceCodeBreak ( 0 ) ;
      builder.addText ( "\\newpage" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      int page ;
      if ( i == 1 )
        page = ( - 220 + this.overlap ) * i ;
      else page = ( - 215 + this.overlap ) * i ;
      builder.addText ( "\\longtext{" + page + "mm}" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    }
    return builder ;
  }


  /**
   * Build the latex string for the given <code>pCurrentNode</code>.
   * 
   * @param pLatexStringBuilderFactory The factory which should be used.
   * @param pLatexStringBuilder The {@link LatexStringBuilder} which should be
   *          completed. is needed because of his {@link ProofNode}s.
   * @param pCurrentNode The current {@link ProofNode}.
   * @param pIndent The indent of this object.
   * @param pDepth the depth of the actual node
   */
  public final void toLatexStringBuilderInternal (
      LatexStringBuilderFactory pLatexStringBuilderFactory ,
      LatexStringBuilder pLatexStringBuilder , ProofNode pCurrentNode ,
      int pIndent , int pDepth )
  {
    int depth = pDepth + 1 ;
    pLatexStringBuilder.addBuilder ( pCurrentNode.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent ) , 0 ) ;
    int value = 180 ;
    for ( int i = 0 ; i < pCurrentNode.getChildCount ( ) ; i ++ )
    {
      pLatexStringBuilder.addText ( "\\arrow{" + value + "}{" //$NON-NLS-1$//$NON-NLS-2$
          + pCurrentNode.getId ( ) + "}{" //$NON-NLS-1$
          + pCurrentNode.getChildAt ( i ).getId ( ) + "}" ) ; //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    }
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    for ( int i = 0 ; i < pCurrentNode.getChildCount ( ) ; i ++ )
    {
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory ,
          pLatexStringBuilder , pCurrentNode.getChildAt ( i ) , pIndent , depth ) ;
    }
  }


  /**
   * Set the actual side overlap for the exported latex file
   * 
   * @param pOverlap the side overlapping
   */
  public void setOverlap ( int pOverlap )
  {
    this.overlap = pOverlap ;
  }


  /**
   * Set the number of pages for the exported latex file
   * 
   * @param pPages number of pages
   */
  public void setPages ( int pPages )
  {
    this.pages = pPages ;
  }
}
