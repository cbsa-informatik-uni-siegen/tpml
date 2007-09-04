package de.unisiegen.tpml.core.subtyping ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import java.util.TreeSet ;
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
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
public class SubTypingProofModel extends AbstractProofModel implements
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
      .getLogger ( SubTypingProofModel.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_MKTREE , 1 ,
        "\\stepcounter{tree} #1 \\arrowstrue #1 \\arrowsfalse" , "tree" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_ARROW ,
            3 ,
            "\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\ncangle[angleA=-90,angleB=#1]{<-}{\\thetree.#2}{\\thetree.#3}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\else" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\fi" , "bli" , "bla" , "blub" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static ArrayList < LatexInstruction > getLatexInstructionsStatic ( )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
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
    instructions.add ( new DefaultLatexInstruction (
        "\\newcommand{\\blong}{\\!\\!\\begin{array}[t]{l}}" ) ) ; //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newcommand{\\elong}{\\end{array}}" ) ) ; //$NON-NLS-1$
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
   * The mode choosen by the user (advanced or beginner)
   */
  private boolean mode = true ;


  /**
   * The rule set for this proof model
   */
  AbstractSubTypingProofRuleSet ruleSet ;


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
  public SubTypingProofModel ( MonoType type , MonoType type2 ,
      AbstractSubTypingProofRuleSet pRuleSet , boolean pMode )
  {
    super ( new DefaultSubTypingProofNode ( type , type2 ) , pRuleSet ) ;
    this.ruleSet = pRuleSet ;
    this.mode = pMode ;
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
  private void applyInternal ( SubTypingProofRule rule ,
      DefaultSubTypingProofNode node ) throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    DefaultSubTypingProofContext context = new DefaultSubTypingProofContext (
        this ) ;
    try
    {
      context.apply ( rule , node ) ;
      // check if we are finished
      final DefaultSubTypingProofNode modelRoot = getRoot ( ) ;
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
   * @param type2 type the second concrete type for the child node, used for the
   *          subtype algorithm.
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  void contextAddProofNode ( final DefaultSubTypingProofContext context ,
      final SubTypingProofNode pNode , final MonoType type ,
      final MonoType type2 )
  {
    final DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode ;
    final DefaultSubTypingProofNode child = new DefaultSubTypingProofNode (
        type , type2 ) ;
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
   * {@link DefaultSubTypingProofContext#apply(SubTypingProofRule, DefaultSubTypingProofNode)}
   * method of the {@link DefaultSubTypingProofContext} class.
   * 
   * @param context the type inference proof context.
   * @param node the type inference node.
   * @param rule the type checker rule.
   * @see DefaultSubTypingProofContext#apply(SubTypingProofRule,
   *      DefaultSubTypingProofNode)
   */
  void contextSetProofNodeRule ( DefaultSubTypingProofContext context ,
      final DefaultSubTypingProofNode node , final SubTypingProofRule rule )
  {
    final ProofStep [ ] oldSteps = node.getSteps ( ) ;
    context.addRedoAction ( new Runnable ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void run ( )
      {
        node.setSteps ( new ProofStep [ ]
        { new ProofStep ( node.getLeft ( ) , node.getRight ( ) , rule ) } ) ;
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
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexCommands()
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : getLatexCommandsInternal ( ( SubTypingProofNode ) this.root ) )
    {
      commands.add ( command ) ;
    }
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
  private TreeSet < LatexCommand > getLatexCommandsInternal (
      SubTypingProofNode pNode )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : pNode.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexCommand command : pNode.getRule ( ).getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexCommand command : getLatexCommandsInternal ( pNode
          .getChildAt ( i ) ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexInstructions()
   */
  public ArrayList < LatexInstruction > getLatexInstructions ( )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : getLatexInstructionsStatic ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    for ( LatexInstruction instruction : getLatexInstructionsInternal ( ( SubTypingProofNode ) this.root ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
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
  private ArrayList < LatexInstruction > getLatexInstructionsInternal (
      SubTypingProofNode pNode )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : pNode.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexInstruction instruction : pNode.getRule ( )
          .getLatexInstructions ( ) )
      {
        if ( ! instructions.contains ( instruction ) )
        {
          instructions.add ( instruction ) ;
        }
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexInstruction instruction : getLatexInstructionsInternal ( pNode
          .getChildAt ( i ) ) )
      {
        if ( ! instructions.contains ( instruction ) )
        {
          instructions.add ( instruction ) ;
        }
      }
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
    for ( LatexPackage pack : getLatexPackagesInternal ( ( SubTypingProofNode ) this.root ) )
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
      SubTypingProofNode pNode )
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
   * @return the root of this model
   * @see de.unisiegen.tpml.core.AbstractProofModel#getRoot()
   */
  @ Override
  public DefaultSubTypingProofNode getRoot ( )
  {
    return ( DefaultSubTypingProofNode ) super.getRoot ( ) ;
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
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode pNode ) throws ProofGuessException
  {
    DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode ;
    guessInternal ( node ) ;
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
  private void guessInternal ( DefaultSubTypingProofNode node )
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
        applyInternal ( ( SubTypingProofRule ) rule , node ) ;
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
    DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode ;
    // try to apply the rule to the specified node
    applyInternal ( ( SubTypingProofRule ) rule , node ) ;
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


  /**
   * set a new root for this model
   * 
   * @param type first MonoType of the new root
   * @param type2 second MonoType of the new root
   */
  public void setRoot ( MonoType type , MonoType type2 )
  {
    this.root = new DefaultSubTypingProofNode ( type , type2 ) ;
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
    for ( int i = 1 ; i < 6 ; i ++ )
    {
      builder.addSourceCodeBreak ( 0 ) ;
      builder.addText ( "\\newpage" ) ; //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 ) ;
      int page = - 205 * i ;
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
}
