package de.unisiegen.tpml.core.subtypingrec;


import de.unisiegen.tpml.core.AbstractProofNode;
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
import de.unisiegen.tpml.core.subtyping.ProofStep;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * Default implementation of the <code>RecSubTypingProofNode</code> interface.
 * The class for nodes in a
 * {@link de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel}.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode
 */
public class DefaultRecSubTypingProofNode extends AbstractProofNode implements
    RecSubTypingProofNode, SubTypingProofNode
{

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
            LATEX_REC_SUB_TYPING_PROOF_NODE,
            7,
            LATEX_LINE_BREAK_NEW_COMMAND
                + "\\ifarrows" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\else \\refstepcounter{node}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\begin{tabular}[t]{p{#7}}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1
                // begin of the node variables
                + "#3\\ \\color{" + LATEX_COLOR_NONE + "}{\\vdash}" //$NON-NLS-1$//$NON-NLS-2$
                + "\\ #4\\ \\color{" + LATEX_COLOR_NONE + "}{<:}\\ #5" //$NON-NLS-1$ //$NON-NLS-2$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\\\$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1 + "\\byrule{#6}" //$NON-NLS-1$
                // end of the node variables
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\end{tabular}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\vspace{\\nodesep}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\fi", "depth", "id", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            "seenTypes", "type", "type2", "rule", "space" ) ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
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
    packages.add ( LatexPackage.PSTNODE );
    packages.add ( LatexPackage.PSTRICKS );
    return packages;
  }


  /**
   * The subtype object containing the subtype and supertype of this node
   */
  private DefaultSubType type;


  /**
   * List with the already seen types
   */
  private SeenTypes < DefaultSubType > seenTypes;


  /**
   * list of proof steps of this node
   */
  private ProofStep [] steps = new ProofStep [ 0 ];


  /**
   * Allocates a new proof step with the given <code>expression</code> and the
   * specified <code>rule</code>.
   * 
   * @param pLeft the first MonoType of this node
   * @param pRight the second MonoType of this node
   * @param pSeenTypes list of all so far seen types
   */
  public DefaultRecSubTypingProofNode ( MonoType pLeft, MonoType pRight,
      SeenTypes < DefaultSubType > pSeenTypes )
  {
    this.type = new DefaultSubType ( pLeft, pRight );
    this.seenTypes = pSeenTypes;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getChildAt(int)
   */
  @Override
  public DefaultRecSubTypingProofNode getChildAt ( final int childIndex )
  {
    return ( DefaultRecSubTypingProofNode ) super.getChildAt ( childIndex );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
   */
  @Override
  public DefaultRecSubTypingProofNode getLastLeaf ()
  {
    return ( DefaultRecSubTypingProofNode ) super.getLastLeaf ();
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( this.getLeft () );
    commands.add ( this.getRight () );
    commands.add ( this.seenTypes.getLatexCommands () );
    commands.add ( getRule () );
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( this.getLeft () );
    instructions.add ( this.getRight () );
    instructions.add ( this.seenTypes.getLatexInstructions () );
    instructions.add ( getRule () );
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( this.getLeft () );
    packages.add ( this.getRight () );
    packages.add ( this.seenTypes.getLatexPackages () );
    packages.add ( getRule () );
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getLeft()
   */
  public MonoType getLeft ()
  {
    return this.type.getLeft ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getRight()
   */
  public MonoType getRight ()
  {
    return this.type.getRight ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getRule()
   */
  public RecSubTypingProofRule getRule ()
  {
    ProofStep [] proofSteps = getSteps ();
    if ( proofSteps.length > 0 )
    {
      return ( RecSubTypingProofRule ) proofSteps [ 0 ].getRule ();
    }
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSeenTypes()
   */
  public SeenTypes < DefaultSubType > getSeenTypes ()
  {
    return this.seenTypes;
  }


  /**
   * get the proof steps of this node
   * 
   * @return ProofStep[] steps
   */
  public ProofStep [] getSteps ()
  {
    return this.steps;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSubType()
   */
  public DefaultSubType getSubType ()
  {
    return this.type;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#isFinished()
   */
  public boolean isFinished ()
  {
    if ( !isProven () )
    {
      return false;
    }
    for ( int n = 0 ; n < getChildCount () ; ++n )
    {
      if ( ! ( getChildAt ( n ) ).isFinished () )
      {
        return false;
      }
    }
    return true;
  }


  /**
   * {@inheritDoc} de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ()
  {
    return ( getSteps ().length > 0 );
  }


  /**
   * get the proof steps of this node
   * 
   * @param pSteps new proof steps for this node
   */
  public void setSteps ( ProofStep [] pSteps )
  {
    this.steps = pSteps;
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
    int depth = 0;
    AbstractProofNode myParent = this.getParent ();
    while ( myParent != null )
    {
      depth++ ;
      myParent = myParent.getParent ();
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_REC_SUB_TYPING_PROOF_NODE, pIndent, this.toPrettyString ()
            .toString (), this.seenTypes.toPrettyString ().toString (), this
            .getLeft ().toPrettyString ().toString (), this.getRight ()
            .toPrettyString ().toString (),
        this.getRule () == null ? LATEX_NO_RULE : this.getRule ()
            .toPrettyString ().toString () );
    builder.addText ( "{" + String.valueOf ( this.getId () ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.seenTypes.toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    builder.addBuilder ( this.getLeft ().toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    builder.addBuilder ( this.getRight ().toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    if ( this.getRule () != null )
    {
      builder.addBuilder ( this.getRule ().toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    }
    else
    {
      builder.addEmptyBuilder ();
    }
    int indent = 245 - depth * 7;
    builder.addSourceCodeBreak ( 0 );
    builder.addComment ( "width of the table" ); //$NON-NLS-1$
    builder.addText ( "{" + indent + "mm}" ); //$NON-NLS-1$//$NON-NLS-2$
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
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
    builder.addBuilder ( this.type.getLeft ().toPrettyStringBuilder (
        pPrettyStringBuilderFactory ), 0 );
    builder.addText ( PRETTY_SPACE );
    builder.addText ( PRETTY_SUBTYPE );
    builder.addText ( PRETTY_SPACE );
    builder.addBuilder ( this.type.getRight ().toPrettyStringBuilder (
        pPrettyStringBuilderFactory ), 0 );
    return builder;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    final StringBuilder builder = new StringBuilder ();
    builder.append ( "[ A = " ); //$NON-NLS-1$
    for ( DefaultSubType subtype : this.seenTypes )
    {
      builder.append ( " ( " + subtype + " ) " ); //$NON-NLS-1$//$NON-NLS-2$
    }
    builder.append ( " ]" ); //$NON-NLS-1$
    builder.append ( "\n" ); //$NON-NLS-1$
    builder.append ( this.type.getLeft () );
    builder.append ( " <: " ); //$NON-NLS-1$
    builder.append ( this.type.getRight () );
    builder.append ( " " ); //$NON-NLS-1$
    if ( this.getSteps ().length > 0 )
      builder.append ( this.getSteps () [ 0 ].getRule ().toString () );
    return builder.toString ();
  }
}
