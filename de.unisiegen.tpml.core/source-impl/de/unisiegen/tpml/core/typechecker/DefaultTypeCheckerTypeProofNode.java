package de.unisiegen.tpml.core.typechecker ;


import de.unisiegen.tpml.core.expressions.Unify ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPackageList ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * This nodes with two types are needed in the type checker algorithm to check
 * subtype relations between this two given types.
 * 
 * @author Benjamin Mies
 */
public class DefaultTypeCheckerTypeProofNode extends
    AbstractTypeCheckerProofNode implements TypeCheckerTypeProofNode
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_BYRULE , 1 ,
        "\\hspace{-5mm}\\mbox{\\scriptsize\\ #1}" , "rule" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_TYPE_CHECKER_TYPE_PROOF_NODE ,
            6 ,
            LATEX_LINE_BREAK_NEW_COMMAND
                + "\\ifarrows" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\else \\refstepcounter{node}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                + "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\begin{tabular}[t]{p{#6}}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1
                // begin of the node variables
                + "#3\\ \\color{" + LATEX_COLOR_NONE + "}{<:}\\ #4" //$NON-NLS-1$//$NON-NLS-2$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\\\$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1 + "\\byrule{#5}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\end{tabular}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND
                // end of the node variables
                + "\\vspace{\\nodesep}" + LATEX_LINE_BREAK_NEW_COMMAND + "\\fi" , //$NON-NLS-1$ //$NON-NLS-2$
            "depth" , "id" , "type" , "type2" , "rule" , "space" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
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
        "\\newif\\ifarrows" + LATEX_LINE_BREAK_SOURCE_CODE //$NON-NLS-1$
            + "\\arrowsfalse" ) ) ; //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( LatexPackage.AMSMATH ) ;
    packages.add ( LatexPackage.AMSTEXT ) ;
    packages.add ( LatexPackage.COLOR ) ;
    packages.add ( LatexPackage.PSTNODE ) ;
    packages.add ( LatexPackage.PSTRICKS ) ;
    return packages ;
  }


  /**
   * The second type for this proof node
   */
  private MonoType type2 ;


  /**
   * Allocates a new <code>DefaultTypeCheckerTypeProofNode</code>
   * 
   * @param pType the first type of this type checker proof node
   * @param pType2 the second type of this type checker proof node
   */
  public DefaultTypeCheckerTypeProofNode ( MonoType pType , MonoType pType2 )
  {
    super ( new Unify ( ) ) ;
    this.type = pType ;
    this.type2 = pType2 ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    commands.add ( this.type ) ;
    commands.add ( this.type2 ) ;
    commands.add ( getRule ( ) ) ;
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions.add ( this.type ) ;
    instructions.add ( this.type2 ) ;
    instructions.add ( getRule ( ) ) ;
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( getLatexPackagesStatic ( ) ) ;
    packages.add ( this.type ) ;
    packages.add ( this.type2 ) ;
    packages.add ( getRule ( ) ) ;
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerTypeProofNode#getType2()
   */
  public MonoType getType2 ( )
  {
    return this.type2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
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
    int depth = 0 ;
    AbstractTypeCheckerProofNode myParent = this.getParent ( ) ;
    while ( myParent != null )
    {
      depth ++ ;
      myParent = myParent.getParent ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_CHECKER_TYPE_PROOF_NODE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.type.toPrettyString ( ).toString ( ) ,
        this.type2.toPrettyString ( ).toString ( ) ,
        this.getRule ( ) == null ? LATEX_NO_RULE : this.getRule ( )
            .toPrettyString ( ).toString ( ) ) ;
    builder.addText ( "{" + String.valueOf ( this.getId ( ) ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.type.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.type2.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.getRule ( ) != null )
    {
      builder.addBuilder ( this.getRule ( ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
    else
    {
      builder.addEmptyBuilder ( ) ;
    }
    int indent = 245 - depth * 7 ;
    builder.addSourceCodeBreak ( 0 ) ;
    builder.addComment ( "width of the table" ) ; //$NON-NLS-1$
    builder.addText ( "{" + indent + "mm}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
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
        this , 0 ) ;
    builder.addBuilder ( this.type
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_SUBTYPE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.type2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type checker type proof node.
   * This method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type checker type
   *         proof node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
