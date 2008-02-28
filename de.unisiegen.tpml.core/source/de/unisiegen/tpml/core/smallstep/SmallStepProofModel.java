package de.unisiegen.tpml.core.smallstep;


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
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


/**
 * The heart of the small step interpreter. Smallstep rules are supplied via an
 * {@link de.unisiegen.tpml.core.smallstep.AbstractSmallStepProofRuleSet} that
 * is passed to the constructor.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode
 */
public final class SmallStepProofModel extends AbstractInterpreterProofModel
{

  /**
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger
      .getLogger ( SmallStepProofModel.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_NEW_NODE, 0,
        "\\\\[10mm]" ) ); //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_NEW_RULE, 0,
        "\\\\" ) ); //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_RULES_COMPLETED,
        0, "&" ) ); //$NON-NLS-1$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_SMALL_STEP_PROOF_MODEL,
            1,
            "\\begin{longtable}{p{3.5cm}@{}p{22cm}@{}}#1\\end{longtable}", "model" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_ARROW, 2,
        "\\xrightarrow" + LATEX_LINE_BREAK_NEW_COMMAND + "[\\mbox{\\color{" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_COLOR_RULE + "}{\\scriptsize{#2}}}]" //$NON-NLS-1$
            + LATEX_LINE_BREAK_NEW_COMMAND + "{\\mbox{\\color{" //$NON-NLS-1$
            + LATEX_COLOR_RULE + "}{\\scriptsize{#1}}}}", "not axiom rules", //$NON-NLS-1$ //$NON-NLS-2$
        "axiom rules" ) ); //$NON-NLS-1$
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
    instructions.add ( new DefaultLatexInstruction (
        "\\newenvironment{smallsteprulearrow}" //$NON-NLS-1$
            + "{\\begin{tabular}[t]{p{3.5cm}@{}}}{\\end{tabular}}", //$NON-NLS-1$
        "The environment of the small step rules with the arrow" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newenvironment{smallsteprules}" //$NON-NLS-1$
            + "{\\begin{tabular}{p{2.5cm}@{}}}{\\end{tabular}}", //$NON-NLS-1$
        "The environment of the small step rules" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newenvironment{smallstepnode}" //$NON-NLS-1$
            + "{\\begin{tabular}[b]{p{22cm}@{}}}{\\end{tabular}}", //$NON-NLS-1$
        "The environment of the small step nodes" ) ); //$NON-NLS-1$
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
    packages.add ( LatexPackage.COLOR );
    packages.add ( LatexPackage.LONGTABLE );
    return packages;
  }


  /**
   * Allocates a new <code>SmallStepProofModel</code> with the specified
   * <code>expression</code> as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param pRuleSet the available small step proof rules for the model.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>ruleSet</code> is <code>null</code>.
   * @see AbstractInterpreterProofModel#AbstractInterpreterProofModel(AbstractInterpreterProofNode,
   *      AbstractProofRuleSet)
   */
  public SmallStepProofModel ( Expression expression,
      AbstractSmallStepProofRuleSet pRuleSet )
  {
    super ( new DefaultSmallStepProofNode ( expression ), pRuleSet );
    // set the "finished" state initially
    setFinished ( expression.isException () || expression.isValue () );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#addUndoableTreeEdit(de.unisiegen.tpml.core.AbstractProofModel.UndoableTreeEdit)
   */
  @Override
  protected void addUndoableTreeEdit ( UndoableTreeEdit edit )
  {
    // perform the redo of the edit
    edit.redo ();
    // add to the undo history
    super.addUndoableTreeEdit ( edit );
  }


  /**
   * Applies the <code>rule</code> to the <code>node</code>. The
   * <code>rule</code> must be any of the remaining rules for
   * <code>node</code>.
   * 
   * @param rule the small step proof rule to apply to <code>node</code>.
   * @param node the node to which to apply the <code>rule</code>.
   * @throws ProofRuleException if the <code>rule</code> cannot be applied to
   *           the <code>node</code>.
   * @see #apply(ProofRule, ProofNode)
   * @see #remaining(ProofNode)
   */
  private void apply ( DefaultSmallStepProofRule rule,
      final DefaultSmallStepProofNode node ) throws ProofRuleException
  {
    // evaluate the expression and determine the proof steps
    DefaultSmallStepProofContext context = new DefaultSmallStepProofContext (
        node, this.ruleSet );
    final Expression expression = context.getExpression ();
    ProofStep [] evaluatedSteps = context.getSteps ();
    // determine the completed steps for the node
    final ProofStep [] completedSteps = node.getSteps ();
    // check if the node is already completed
    if ( completedSteps.length == evaluatedSteps.length )
    {
      // check if the interpreter is stuck
      SmallStepProofRule lastRule = ( SmallStepProofRule ) evaluatedSteps [ evaluatedSteps.length - 1 ]
          .getRule ();
      if ( !lastRule.isAxiom () )
      {
        // the proof is stuck
        throw new ProofRuleException ( node, rule );
      }
      // an internal error in the upper layers
      throw new IllegalStateException ( "Cannot prove an already proven node" ); //$NON-NLS-1$
    }
    else if ( completedSteps.length > evaluatedSteps.length )
    {
      // this is a bug then
      throw new IllegalStateException ( "completedSteps > evaluatedSteps" ); //$NON-NLS-1$
    }
    // verify the completed steps
    int n;
    for ( n = 0 ; n < completedSteps.length ; ++n )
    {
      if ( !completedSteps [ n ].getRule ().equals (
          evaluatedSteps [ n ].getRule () ) )
        throw new IllegalStateException (
            "Completed steps don't match evaluated steps" ); //$NON-NLS-1$
    }
    // check if the rule is valid, accepting regular meta-rules for EXN rules
    int m;
    for ( m = n ; m < evaluatedSteps.length ; ++m )
    {
      if ( evaluatedSteps [ m ].getRule ().equals ( rule )
          || evaluatedSteps [ m ].getRule ().equals ( rule.toExnRule () ) )
        break;
    }
    // check if rule is invalid
    if ( m >= evaluatedSteps.length )
    {
      throw new ProofRuleException ( node, rule );
    }
    // determine the new step(s) for the node
    final ProofStep [] newSteps = new ProofStep [ m + 1 ];
    System.arraycopy ( evaluatedSteps, 0, newSteps, 0, m + 1 );
    // check if the node is finished (the last
    // step is an application of an axiom rule)
    if ( ( ( SmallStepProofRule ) newSteps [ m ].getRule () ).isAxiom () )
    {
      // create the child node for the node
      final DefaultSmallStepProofNode child = new DefaultSmallStepProofNode (
          expression, context.getStore () );
      // add the undoable edit
      addUndoableTreeEdit ( new UndoableTreeEdit ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void redo ()
        {
          // update the "finished" state
          setFinished ( expression.isException () || expression.isValue () );
          // apply the new steps and add the child
          node.setSteps ( newSteps );
          node.add ( child );
          nodesWereInserted ( node, new int []
          { node.getIndex ( child ) } );
          nodeChanged ( node );
        }


        @SuppressWarnings ( "synthetic-access" )
        public void undo ()
        {
          // update the "finished" state
          setFinished ( false );
          // remove the child and revert the steps
          int [] indices =
          { node.getIndex ( child ) };
          node.remove ( child );
          nodesWereRemoved ( node, indices, new Object []
          { child } );
          node.setSteps ( completedSteps );
          nodeChanged ( node );
        }
      } );
    }
    else
    {
      // add the undoable edit
      addUndoableTreeEdit ( new UndoableTreeEdit ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void redo ()
        {
          // apply the new steps
          node.setSteps ( newSteps );
          nodeChanged ( node );
        }


        @SuppressWarnings ( "synthetic-access" )
        public void undo ()
        {
          // revert to the previous steps
          node.setSteps ( completedSteps );
          nodeChanged ( node );
        }
      } );
    }
  }


  /**
   * Convenience wrapper for the
   * {@link #apply(DefaultSmallStepProofRule, DefaultSmallStepProofNode)}
   * method, which automatically casts to the appropriate types.
   * 
   * @param rule the small step proof rule to apply to <code>node</code>.
   * @param node the node to which to apply the <code>rule</code>.
   * @throws ProofRuleException if the <code>rule</code> cannot be applied to
   *           the <code>node</code>.
   * @see #apply(DefaultSmallStepProofRule, DefaultSmallStepProofNode)
   */
  private void apply ( ProofRule rule, ProofNode node )
      throws ProofRuleException
  {
    apply ( ( DefaultSmallStepProofRule ) rule,
        ( DefaultSmallStepProofNode ) node );
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( getLatexCommandsInternal ( this.root ) );
    return commands;
  }


  /**
   * Returns a set of needed latex commands for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex commands for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexCommandList getLatexCommandsInternal ( ProofNode pNode )
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( pNode );
    commands.add ( pNode.getRules () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      commands.add ( getLatexCommandsInternal ( pNode.getChildAt ( i ) ) );
    }
    return commands;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( getLatexInstructionsInternal ( this.root ) );
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
  private LatexInstructionList getLatexInstructionsInternal ( ProofNode pNode )
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( pNode );
    instructions.add ( pNode.getRules () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      instructions
          .add ( getLatexInstructionsInternal ( pNode.getChildAt ( i ) ) );
    }
    return instructions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( getLatexPackagesInternal ( this.root ) );
    return packages;
  }


  /**
   * Returns a set of needed latex packages for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex packages for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexPackageList getLatexPackagesInternal ( ProofNode pNode )
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( pNode.getLatexPackages () );
    packages.add ( pNode.getRules () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      packages.add ( getLatexPackagesInternal ( pNode.getChildAt ( i ) ) );
    }
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
    // guess the remaining steps for the node
    ProofStep [] remainingSteps = remaining ( node );
    // check if the node is already completed
    if ( remainingSteps.length == 0 )
    {
      // check if we are already completed
      if ( node.isProven () )
      {
        // the node is already proven, programming error
        throw new IllegalStateException ( "Cannot prove an already proven node" ); //$NON-NLS-1$
      }
      // the evaluation got stuck
      throw new ProofGuessException ( Messages
          .getString ( "InterpreterModel.0" ), node ); //$NON-NLS-1$
    }
    // try to prove using the guessed rule
    try
    {
      // apply the last rule of the remaining steps to the node
      apply ( remainingSteps [ remainingSteps.length - 1 ].getRule (), node );
      // remember that the user cheated
      setCheating ( true );
    }
    catch ( ProofRuleException e )
    {
      // failed to guess
      throw new ProofGuessException ( node, e );
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void prove ( ProofRule rule, ProofNode node )
      throws ProofRuleException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( rule == null )
    {
      throw new NullPointerException ( "rule is null" ); //$NON-NLS-1$
    }
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
    }
    if ( !this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ); //$NON-NLS-1$
    }
    // apply the rule to the specified node
    try
    {
      apply ( rule, node );
    }
    catch ( RuntimeException e )
    {
      logger
          .error (
              "An internal error occurred while proving " + node + " using (" + rule + ")", e ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      throw e;
    }
  }


  /**
   * Returns the remaining {@link de.unisiegen.tpml.core.ProofStep}s required
   * to prove the specified <code>node</code>. This method is used to guess
   * the next step, see the {@link #guess(ProofNode)} method for further
   * details, and in the user interface, to highlight the next expression.
   * 
   * @param node the {@link de.unisiegen.tpml.core.ProofNode} for which to
   *          return the remaining steps required to prove the <code>node</code>.
   * @return the remaining {@link ProofStep}s required to prove the
   *         <code>node</code>, or an empty array if the <code>node</code>
   *         is already proven or the evaluation is stuck.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           the model.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  public ProofStep [] remaining ( ProofNode node )
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
    }
    // evaluate the next step for the node
    DefaultSmallStepProofContext context = new DefaultSmallStepProofContext (
        ( SmallStepProofNode ) node, this.ruleSet );
    // determine the completed/evaluated steps
    ProofStep [] completedSteps = ( ( SmallStepProofNode ) node ).getSteps ();
    ProofStep [] evaluatedSteps = context.getSteps ();
    // generate the remaining steps
    ProofStep [] remainingSteps = new ProofStep [ evaluatedSteps.length
        - completedSteps.length ];
    System.arraycopy ( evaluatedSteps, completedSteps.length, remainingSteps,
        0, remainingSteps.length );
    return remainingSteps;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#setOverlap(int)
   */
  public void setOverlap ( @SuppressWarnings ( "unused" )
  int pOverlap )
  {
    throw new UnsupportedOperationException ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#setPages(int)
   */
  public void setPages ( @SuppressWarnings ( "unused" )
  int pPages )
  {
    throw new UnsupportedOperationException ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_SMALL_STEP_PROOF_MODEL, pIndent, this.toPrettyString ()
            .toString () );
    builder.addBuilderBegin ();
    builder.addSourceCodeBreak ( 0 );
    builder.addComment ( "no small step rules in the first node" ); //$NON-NLS-1$
    builder.addText ( LATEX_PREFIX_COMMAND + LATEX_SMALL_STEP_RULES_COMPLETED );
    builder.addSourceCodeBreak ( 0 );
    // small steps
    builder.addText ( "\\begin{smallstepnode}" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "$" ); //$NON-NLS-1$
    builder.addBuilderWithoutBrackets ( this.root.toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "$" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\end{smallstepnode}" ); //$NON-NLS-1$
    if ( this.root.getChildCount () > 0 )
    {
      builder.addSourceCodeBreak ( 0 );
      builder.addText ( LATEX_PREFIX_COMMAND + LATEX_SMALL_STEP_NEW_NODE );
      builder.addSourceCodeBreak ( 0 );
    }
    for ( int i = 0 ; i < this.root.getChildCount () ; i++ )
    {
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory, builder,
          this.root, this.root.getChildAt ( i ), pIndent );
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * Build the latex string for the given <code>pCurrentNode</code>.
   * 
   * @param pLatexStringBuilderFactory The factory which should be used.
   * @param pLatexStringBuilder The {@link LatexStringBuilder} which should be
   *          completed.
   * @param pParentNode The parent of the current {@link ProofNode}. This node
   *          is needed because of his {@link ProofNode}s.
   * @param pCurrentNode The current {@link ProofNode}.
   * @param pIndent The indent of this object.
   */
  public final void toLatexStringBuilderInternal (
      LatexStringBuilderFactory pLatexStringBuilderFactory,
      LatexStringBuilder pLatexStringBuilder, ProofNode pParentNode,
      ProofNode pCurrentNode, int pIndent )
  {
    ProofRule [] rules = pParentNode.getRules ();
    pLatexStringBuilder.addText ( "\\begin{smallsteprulearrow}" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder
        .addText ( LATEX_PREFIX_COMMAND + LATEX_SMALL_STEP_ARROW );
    // not axiom rules
    pLatexStringBuilder.addBuilderBegin ();
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\begin{smallsteprules}" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    int countNotAxiomRules = 0;
    for ( int i = 0 ; i < rules.length ; i++ )
    {
      DefaultSmallStepProofRule rule = ( DefaultSmallStepProofRule ) rules [ i ];
      if ( !rule.isAxiom () )
      {
        int sameRule = 1;
        for ( int j = i + 1 ; j < rules.length ; j++ )
        {
          if ( rules [ j ].getName ().equals ( rule.getName () ) )
          {
            sameRule++ ;
            i = j;
          }
          else
          {
            break;
          }
        }
        if ( countNotAxiomRules > 0 )
        {
          pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
          pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
              + LATEX_SMALL_STEP_NEW_RULE );
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
          pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
        }
        if ( sameRule > 1 )
        {
          pLatexStringBuilder.addBuilder ( rule.toLatexStringBuilder (
              pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 3 ), 0 );
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
          pLatexStringBuilder.addText ( "^" + sameRule ); //$NON-NLS-1$
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
        }
        else
        {
          pLatexStringBuilder.addBuilder ( rule.toLatexStringBuilder (
              pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 3 ), 0 );
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
        }
        countNotAxiomRules++ ;
      }
    }
    if ( countNotAxiomRules == 0 )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
    }
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{smallsteprules}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addBuilderEnd ();
    // axiom rules
    pLatexStringBuilder.addBuilderBegin ();
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\begin{smallsteprules}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    int countAxiomRules = 0;
    for ( int i = 0 ; i < rules.length ; i++ )
    {
      DefaultSmallStepProofRule rule = ( DefaultSmallStepProofRule ) rules [ i ];
      if ( rule.isAxiom () )
      {
        if ( countAxiomRules > 0 )
        {
          pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
          pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
              + LATEX_SMALL_STEP_NEW_RULE );
          pLatexStringBuilder.addSourceCodeBreak ( 0 );
          pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
        }
        pLatexStringBuilder.addBuilder ( rule.toLatexStringBuilder (
            pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 3 ), 0 );
        pLatexStringBuilder.addSourceCodeBreak ( 0 );
        countAxiomRules++ ;
      }
    }
    if ( countAxiomRules == 0 )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
    }
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{smallsteprules}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addBuilderEnd ();
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{smallsteprulearrow}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
        + LATEX_SMALL_STEP_RULES_COMPLETED );
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    // small steps
    pLatexStringBuilder.addText ( "\\begin{smallstepnode}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addBuilderWithoutBrackets ( pCurrentNode
        .toLatexStringBuilder ( pLatexStringBuilderFactory, pIndent
            + LATEX_INDENT ), 0 );
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{smallstepnode}" );//$NON-NLS-1$
    for ( int i = 0 ; i < pCurrentNode.getChildCount () ; i++ )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
          + LATEX_SMALL_STEP_NEW_NODE );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory,
          pLatexStringBuilder, pCurrentNode, pCurrentNode.getChildAt ( i ),
          pIndent );
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this, 0 );
    builder.addBuilder ( this.root
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
    builder.addText ( PRETTY_LINE_BREAK );
    builder.addText ( PRETTY_CONTINUATION );
    return builder;
  }


  /**
   * Returns the string representation for this model. This method is mainly
   * used for debugging.
   * 
   * @return The pretty printed string representation for this model.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
