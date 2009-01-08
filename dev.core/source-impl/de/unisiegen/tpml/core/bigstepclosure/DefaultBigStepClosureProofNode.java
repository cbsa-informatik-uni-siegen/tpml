package de.unisiegen.tpml.core.bigstepclosure;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
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
public final class DefaultBigStepClosureProofNode extends
    AbstractInterpreterProofNode implements BigStepClosureProofNode
{

  /**
   * Constructrs a new proof node from a closure
   * 
   * @param closure The node's closure
   */
  public DefaultBigStepClosureProofNode ( final Closure closure )
  {
    super ( closure.getExpression (), new DefaultStore () ); // FIXME: we can't
    // get rid of the
    // store here
    this.environment = closure.getEnvironment ();
  }


  public BigStepClosureProofResult getResult ()
  {
    return this.result;
  }


  public BigStepClosureProofRule getRule ()
  {
    ProofStep [] steps = getSteps ();
    return steps.length == 0 ? null : ( BigStepClosureProofRule ) steps [ 0 ]
        .getRule ();
  }


  public DefaultBigStepClosureProofNode getParent ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getParent ();
  }


  public DefaultBigStepClosureProofNode getRoot ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getRoot ();
  }


  public DefaultBigStepClosureProofNode getFirstChild ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getFirstChild ();
  }


  public DefaultBigStepClosureProofNode getLastChild ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getLastChild ();
  }


  public DefaultBigStepClosureProofNode getChildAfter ( TreeNode aChild )
  {
    return ( DefaultBigStepClosureProofNode ) super.getChildAfter ( aChild );
  }


  public DefaultBigStepClosureProofNode getChildBefore ( TreeNode aChild )
  {
    return ( DefaultBigStepClosureProofNode ) super.getChildBefore ( aChild );
  }


  public DefaultBigStepClosureProofNode getChildAt ( int index )
  {
    return ( DefaultBigStepClosureProofNode ) super.getChildAt ( index );
  }


  public DefaultBigStepClosureProofNode getFirstLeaf ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getFirstLeaf ();
  }


  public DefaultBigStepClosureProofNode getLastLeaf ()
  {
    return ( DefaultBigStepClosureProofNode ) super.getLastLeaf ();
  }


  public boolean isProven ()
  {
    return this.result != null;
  }


  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory fac )
  {
    final PrettyStringBuilder builder = fac.newBuilder ( this, 0 );

    /*
     * builder.addBuilder ( getClosure ().toPrettyStringBuilder ( fac ), 0 );
     * final ClosureEnvironment env = getClosure().getEnvironment();
     * if(env.isNotPrinted()) { builder.addText ( env.getName() + PRETTY_EQUAL
     * ); builder.addBuilder ( env.toPrettyStringBuilder ( fac ), 0 ); }
     */

    return builder;
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( getClosure () );
    instructions.add ( getResult () );
    instructions.add ( getRule () );
    return instructions;
  }


  public LatexStringBuilder toLatexStringBuilder (
      final LatexStringBuilderFactory pLatexStringBuilderFactory,
      final int pIndent )
  {
    final int depth = getDepth ();

    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_BIG_STEP_CLOSURE_PROOF_NODE, pIndent, this.toPrettyString ()
            .toString (), this.getClosure ().toPrettyString ().toString (),
        LATEX_NO_STORE, this.result == null ? LATEX_NO_RESULT : this.result
            .toPrettyString ().toString (),
        this.getRule () == null ? LATEX_NO_RULE : this.getRule ()
            .toPrettyString ().toString () );
    builder.addText ( "{" + String.valueOf ( this.getId () ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( getClosure ().toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );

    if ( this.getResult () != null )
      builder.addBuilder ( getResult ().toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    else
      builder.addEmptyBuilder ();

    if ( this.getRule () != null )
      builder.addBuilder ( this.getRule ().toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    else
      builder.addEmptyBuilder ();

    int indent = 245 - depth * 7;
    builder.addSourceCodeBreak ( 0 );
    builder.addComment ( "width of the table" ); //$NON-NLS-1$
    builder.addText ( "{" + indent + "mm}" ); //$NON-NLS-1$//$NON-NLS-2$
    return builder;
  }


  public LatexCommandList getLatexCommands ()
  {
    final LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( getClosure () );
    commands.add ( getResult () );
    commands.add ( getRule () );
    return commands;
  }


  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( getClosure () );
    packages.add ( getRule () );
    return packages;
  }


  public ClosureEnvironment getEnvironment ()
  {
    return this.environment;
  }


  public Closure getClosure ()
  {
    return new Closure ( getExpression (), getEnvironment () );
  }


  public boolean isFinished ()
  {
    if ( !isProven () )
      return false;

    for ( int n = 0 ; n < getChildCount () ; ++n )
      if ( !getChildAt ( n ).isFinished () )
        return false;
    return true;
  }


  public void setResult ( final BigStepClosureProofResult pResult )
  {
    if ( pResult != null && !pResult.getValue ().isException ()
        && !pResult.getValue ().isValue () )
    {
      throw new IllegalArgumentException ( "result is invalid" ); //$NON-NLS-1$
    }
    this.result = pResult;
  }


  @Override
  public String toString ()
  {
    return toPrettyString ().toString ();
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
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ); //$NON-NLS-1$
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
    packages.add ( LatexPackage.COLOR );
    packages.add ( LatexPackage.XIFTHEN );
    packages.add ( LatexPackage.PSTNODE );
    packages.add ( LatexPackage.PSTRICKS );
    return packages;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_BYRULE, 1,
        "\\hspace{-5mm}\\mbox{\\scriptsize\\ #1}", "rule" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_BIG_STEP_CLOSURE_PROOF_NODE,
            6,
            LATEX_LINE_BREAK_NEW_COMMAND
                + "\\ifarrows" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\else \\refstepcounter{node}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "$\\begin{tabular}[t]{p{#6}}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1
                + "\\ifthenelse{\\isempty{#4}}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT2
                // begin of the node variables
                + "{#3\\ \\color{" + LATEX_COLOR_NONE + "}{\\Downarrow}\\ #4}" //$NON-NLS-1$ //$NON-NLS-2$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT2 + "{\\color{" //$NON-NLS-1$
                + LATEX_COLOR_NONE + "}{(}#3\\ \\color{" //$NON-NLS-1$
                + LATEX_COLOR_NONE + "}{)}\\ \\color{" + LATEX_COLOR_NONE //$NON-NLS-1$
                + "}{\\Downarrow}\\ #4}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "$\\\\$" + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1 //$NON-NLS-1$
                + "\\byrule{#5}" //$NON-NLS-1$
                // end of the node variables
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\end{tabular}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\vspace{\\nodesep}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\fi", "depth", "id", "e", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "result", "proofrule", "space" ) ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
    return commands;
  }


  /**
   * Calculates the depth of this node.
   * 
   * @return The depth of this node.
   */
  private int getDepth ()
  {
    int depth = 0;
    BigStepClosureProofNode myParent = this.getParent ();
    while ( myParent != null )
    {
      depth++ ;
      myParent = myParent.getParent ();
    }
    return depth;
  }


  private BigStepClosureProofResult result;


  private ClosureEnvironment environment;
}
