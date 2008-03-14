package de.unisiegen.tpml.core.minimaltyping;


import de.unisiegen.tpml.core.expressions.Unify;
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
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * The Type Proof Node for the Minimal Typing Algorithm. Containing two types,
 * and a list of alreday seen types.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @version $Id$
 * @see AbstractMinimalTypingProofNode
 */
public class DefaultMinimalTypingTypesProofNode extends
    AbstractMinimalTypingProofNode implements MinimalTypingTypesProofNode
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
            LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE,
            5,
            LATEX_LINE_BREAK_NEW_COMMAND
                + "\\ifarrows" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\else \\refstepcounter{node}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\begin{tabular}[t]{p{#5}}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1
                // begin of the node variables
                + "#3" + LATEX_LINE_BREAK_NEW_COMMAND + "$\\\\$" //$NON-NLS-1$ //$NON-NLS-2$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1 + "\\byrule{#4}" //$NON-NLS-1$
                // end of the node variables
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\end{tabular}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\vspace{\\nodesep}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\fi", "depth", "id", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            "subtype", "rule", "space" ) ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
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
   * List of all already seen types
   */
  private SeenTypes < DefaultSubType > seenTypes;


  /**
   * Subtype Object, containing the subtype and supertype
   */
  private DefaultSubType subtype;


  /**
   * Allocates a new <code>DefaultMinimalTypingTypesProofNode</code> with the
   * specified <code>types</code>.
   * 
   * @param pType the left{@link MonoType} for this node.
   * @param pType2 the right {@link MonoType} for this node.
   */
  public DefaultMinimalTypingTypesProofNode ( MonoType pType, MonoType pType2 )
  {
    super ( new Unify () );
    this.subtype = new DefaultSubType ( pType, pType2 );
    this.seenTypes = new SeenTypes < DefaultSubType > ();
  }


  /**
   * There is no type environment needed for this node. So null is returned.
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
   */
  public DefaultTypeEnvironment getEnvironment ()
  {
    return null;
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
    commands.add ( this.subtype );
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
    instructions.add ( this.subtype );
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
    packages.add ( this.subtype );
    packages.add ( getRule () );
    return packages;
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
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getSubType()
   */
  public DefaultSubType getSubType ()
  {
    return this.subtype;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
   */
  public MonoType getType ()
  {
    return this.subtype.getLeft ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getType2()
   */
  public MonoType getType2 ()
  {
    return this.subtype.getRight ();
  }


  /**
   * Set the seen types of this node
   * 
   * @param pSeenTypes the new already seen types
   */
  public void setSeenTypes ( SeenTypes < DefaultSubType > pSeenTypes )
  {
    this.seenTypes = pSeenTypes;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    int depth = 0;
    AbstractMinimalTypingProofNode myParent = this.getParent ();
    while ( myParent != null )
    {
      depth++ ;
      myParent = myParent.getParent ();
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE, pIndent, this.toPrettyString ()
            .toString (), this.subtype.toPrettyString ().toString (), this
            .getRule () == null ? LATEX_NO_RULE : this.getRule ()
            .toPrettyString ().toString () );
    builder.addText ( "{" + String.valueOf ( this.getId () ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.subtype.toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    if ( this.getRule () != null )
    {
      builder.addBuilder ( this.getRule ().toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent ), 0 );
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
    builder.addBuilder ( this.subtype
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
    return builder;
  }


  /**
   * Returns the string representation for this minimal typing types proof node.
   * This method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this minimal typing
   *         types proof node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
