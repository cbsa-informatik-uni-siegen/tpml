package de.unisiegen.tpml.core.bigstepclosure;


import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.DefaultClosureEnvironment;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


/**
 * TODO
 */
public final class BigStepClosureProofModel extends
    AbstractInterpreterProofModel
{

  /**
   * TODO
   * 
   * @param expression
   * @param pRuleSet
   */
  public BigStepClosureProofModel ( final Expression expression,
      final AbstractProofRuleSet pRuleSet )
  {
    super ( new DefaultBigStepClosureProofNode (
          new Closure(
                expression,
                DefaultClosureEnvironment.empty ( 0 )
          )), pRuleSet );
    this.nextEnvNumber = 1;
  }


  public void setOverlap ( final int i )
  {
    this.overlap = i;
  }


  public void guess ( ProofNode node ) throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( node.getRules ().length > 0 )
    {
      throw new IllegalArgumentException ( Messages
          .getString ( "BigStepClosure.0" ) ); //$NON-NLS-1$
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
        // cast node to a DefaultBigStepClosureProofNode
        DefaultBigStepClosureProofNode current = ( DefaultBigStepClosureProofNode ) node;
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


  public void setPages ( final int i )
  {
    this.pages = i;
  }


  public void prove ( ProofRule rule, ProofNode node )
      throws ProofRuleException
  {
    apply ( ( BigStepClosureProofRule ) rule,
        ( DefaultBigStepClosureProofNode ) node );
  }


  private void apply ( final BigStepClosureProofRule rule,
      final DefaultBigStepClosureProofNode node ) throws ProofRuleException
  {
    // allocate a new big step proof context
    DefaultBigStepClosureProofContext context = new DefaultBigStepClosureProofContext (
        this );
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
      final DefaultBigStepClosureProofNode node,
      final DefaultBigStepClosureProofNode child )
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


  public void contextSetProofNodeResult (
      final DefaultBigStepClosureProofContext context,
      final DefaultBigStepClosureProofNode node,
      final BigStepClosureProofResult result )
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
      final DefaultBigStepClosureProofNode node,
      final BigStepClosureProofRule rule )
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


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages
        .add ( getLatexPackagesInternal ( ( BigStepClosureProofNode ) this.root ) );
    return packages;
  }


  private LatexPackageList getLatexPackagesInternal (
      BigStepClosureProofNode pNode )
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( pNode );
    packages.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      packages.add ( getLatexPackagesInternal ( pNode.getChildAt ( i ) ) );
    }
    return packages;
  }


  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands
        .add ( getLatexCommandsInternal ( ( BigStepClosureProofNode ) this.root ) );
    return commands;
  }


  private LatexCommandList getLatexCommandsInternal (
      BigStepClosureProofNode pNode )
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( pNode );
    commands.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      commands.add ( getLatexCommandsInternal ( pNode.getChildAt ( i ) ) );
    }
    return commands;
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions
        .add ( getLatexInstructionsInternal ( ( BigStepClosureProofNode ) this.root ) );
    return instructions;
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
      BigStepClosureProofNode pNode )
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( pNode );
    instructions.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      instructions
          .add ( getLatexInstructionsInternal ( pNode.getChildAt ( i ) ) );
    }
    return instructions;
  }


  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    ArrayList < PrettyString > descriptionList = new ArrayList < PrettyString > ();
    descriptionList.add ( this.toPrettyString () );
    descriptionList.addAll ( getDescription ( this.root ) );
    String [] description = new String [ descriptionList.size () ];
    for ( int i = 0 ; i < descriptionList.size () ; i++ )
    {
      description [ i ] = descriptionList.get ( i ).toString ();
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        pIndent, description );
    builder.addText ( "\\treeindent=0mm" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\nodeindent=7mm" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\nodesep=2mm" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder
        .addText ( "\\newcommand{\\longtext}[1]{\\oddsidemargin=#1\\enlargethispage{2730mm}" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\mktree{" ); //$NON-NLS-1$

    toLatexStringBuilderInternal ( pLatexStringBuilderFactory, builder,
        this.root, pIndent, -1 );
    builder.addText ( "}" ); //$NON-NLS-1$
    builder.addText ( "}" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\longtext{0mm}" ); //$NON-NLS-1$
    for ( int i = 1 ; i < this.pages ; i++ )
    {
      builder.addSourceCodeBreak ( 0 );
      builder.addText ( "\\newpage" ); //$NON-NLS-1$
      builder.addSourceCodeBreak ( 0 );
      int page = ( -210 + this.overlap ) * i;
      builder.addText ( "\\longtext{" + page + "mm}" ); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return builder;
  }


  /**
   * Returns the {@link PrettyString}s of all child {@link ProofNode}s.
   * 
   * @param pCurrentNode
   * @return The {@link PrettyString}s of all child {@link ProofNode}s.
   */
  private ArrayList < PrettyString > getDescription ( ProofNode pCurrentNode )
  {
    ArrayList < PrettyString > descriptionList = new ArrayList < PrettyString > ();
    descriptionList.add ( pCurrentNode.toPrettyString () );
    for ( int i = 0 ; i < pCurrentNode.getChildCount () ; i++ )
    {
      descriptionList
          .addAll ( getDescription ( pCurrentNode.getChildAt ( i ) ) );
    }
    return descriptionList;
  }


  private final void toLatexStringBuilderInternal (
      LatexStringBuilderFactory pLatexStringBuilderFactory,
      LatexStringBuilder pLatexStringBuilder, ProofNode pCurrentNode,
      int pIndent, int pDepth )
  {
    int depth = pDepth + 1;

    pLatexStringBuilder.addBuilder ( pCurrentNode.toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    int value = 180;
    if ( pCurrentNode.getChildCount () == 1 )
      value = 180;
    for ( int i = 0 ; i < pCurrentNode.getChildCount () ; i++ )
    {
      pLatexStringBuilder.addText ( "\\arrow{" + value + "}{" //$NON-NLS-1$//$NON-NLS-2$
          + pCurrentNode.getId () + "}{" //$NON-NLS-1$
          + pCurrentNode.getChildAt ( i ).getId () + "}" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
    }
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    for ( int i = 0 ; i < pCurrentNode.getChildCount () ; i++ )
    {
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory,
          pLatexStringBuilder, pCurrentNode.getChildAt ( i ), pIndent, depth );
    }
  }


  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this, 0 );
    builder.addBuilder ( this.root
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
    builder.addText ( PRETTY_LINE_BREAK );
    builder.addText ( PRETTY_CONTINUATION );
    return builder;
  }


  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_MKTREE, 1,
        "\\stepcounter{tree} #1 \\arrowstrue #1 \\arrowsfalse", "tree" ) ); //$NON-NLS-1$//$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_ARROW,
            3,
            "\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\ncangle[angleA=-90,angleB=#1]{<-}{\\thetree.#2}{\\thetree.#3}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\else" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\fi", "angle", "from", "to" ) ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( new DefaultLatexInstruction ( "\\newcounter{tree}" ) ); //$NON-NLS-1$
    instructions
        .add ( new DefaultLatexInstruction ( "\\newcounter{node}[tree]" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newlength{\\treeindent}" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newlength{\\nodeindent}" ) ); //$NON-NLS-1$
    instructions
        .add ( new DefaultLatexInstruction ( "\\newlength{\\nodesep}" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newif\\ifarrows" + LATEX_LINE_BREAK_SOURCE_CODE //$NON-NLS-1$
            + "\\arrowsfalse" ) ); //$NON-NLS-1$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.AMSMATH );
    packages.add ( LatexPackage.AMSTEXT );
    packages.add ( LatexPackage.LONGTABLE );
    packages.add ( LatexPackage.PSTNODE );
    packages.add ( LatexPackage.PSTRICKS );
    return packages;
  }


  public int getNextEnvNumber ()
  {
    return this.nextEnvNumber++ ;
  }


  private static final Logger logger = Logger
      .getLogger ( BigStepClosureProofModel.class );


  private int nextEnvNumber = 0;


  /**
   * The side overlapping for latex export
   */
  private int overlap = 0;


  /**
   * The number of pages for latex export
   */
  private int pages = 5;
}
