package de.unisiegen.tpml.core.minimaltyping ;


import de.unisiegen.tpml.core.expressions.Expression ;
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
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The Expression Proof Node for the Minimal Typing Algorithm. Containing an
 * type environment, an expression and a type.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see AbstractMinimalTypingProofNode
 */
public class DefaultMinimalTypingExpressionProofNode extends
    AbstractMinimalTypingProofNode implements MinimalTypingExpressionProofNode
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
            LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE ,
            7 ,
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
                + "#3\\ \\color{" + LATEX_COLOR_NONE + "}{" //$NON-NLS-1$//$NON-NLS-2$
                + LATEX_RIGHT_TRIANGLE + "}\\ #4\\ \\color{" + LATEX_COLOR_NONE //$NON-NLS-1$
                + "}{::}\\ #5" + LATEX_LINE_BREAK_NEW_COMMAND + "$\\\\$" //$NON-NLS-1$//$NON-NLS-2$
                + LATEX_LINE_BREAK_NEW_COMMAND_INDENT1 + "\\byrule{#6}" //$NON-NLS-1$
                // end of the node variables
                + LATEX_LINE_BREAK_NEW_COMMAND + "$\\end{tabular}$" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\vspace{\\nodesep}" //$NON-NLS-1$
                + LATEX_LINE_BREAK_NEW_COMMAND + "\\fi" , "depth" , "id" , //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
            "evironment" , "expression" , "type" , "rule" , "space" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
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
    packages.add ( LatexPackage.AMSSYMB ) ;
    packages.add ( LatexPackage.AMSTEXT ) ;
    packages.add ( LatexPackage.COLOR ) ;
    packages.add ( LatexPackage.PSTNODE ) ;
    packages.add ( LatexPackage.PSTRICKS ) ;
    return packages ;
  }


  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   * @see #setEnvironment(TypeEnvironment)
   */
  protected TypeEnvironment environment ;


  /**
   * The type for this type node, which is either a type variable or a monorphic
   * type.
   * 
   * @see #getType()
   * @see #setType(MonoType)
   */
  protected MonoType type ;


  /**
   * Allocates a new <code>DefaultMinimalTypingExpressionProofNode</code> with
   * the specified <code>environment</code>, <code>expression</code> and
   * <code>type</code>.
   * 
   * @param pEnvironment the {@link TypeEnvironment} for this node.
   * @param pExpression the {@link Expression} for this node.
   * @throws NullPointerException if <code>environment</code>,
   *           <code>expression</code> or <code>type</code> is
   *           <code>null</code>.
   */
  public DefaultMinimalTypingExpressionProofNode (
      TypeEnvironment pEnvironment , Expression pExpression )
  {
    super ( pExpression ) ;
    setEnvironment ( pEnvironment ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
   */
  public TypeEnvironment getEnvironment ( )
  {
    return this.environment ;
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
    commands.add ( this.environment ) ;
    commands.add ( this.expression ) ;
    commands.add ( this.type ) ;
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
    instructions.add ( this.environment ) ;
    instructions.add ( this.expression ) ;
    instructions.add ( this.type ) ;
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
    packages.add ( this.environment ) ;
    packages.add ( this.expression ) ;
    packages.add ( this.type ) ;
    packages.add ( getRule ( ) ) ;
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
   */
  public MonoType getType ( )
  {
    return this.type ;
  }


  /**
   * Sets the type environment for this proof node to <code>environment</code>.
   * 
   * @param pEnvironment the new type environment for this node.
   * @throws NullPointerException if <code>environment</code> is
   *           <code>null</code>.
   * @see #getEnvironment()
   */
  void setEnvironment ( TypeEnvironment pEnvironment )
  {
    if ( pEnvironment == null )
    {
      throw new NullPointerException ( "environment is null" ) ; //$NON-NLS-1$
    }
    this.environment = pEnvironment ;
  }


  /**
   * Sets the type of this proof node to <code>type</code>.
   * 
   * @param pType the new type for this proof node.
   * @throws NullPointerException if <code>type</code> is <code>null</code>.
   * @see #getType()
   */
  public void setType ( MonoType pType )
  {
    this.type = pType ;
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
    AbstractMinimalTypingProofNode myParent = this.getParent ( ) ;
    while ( myParent != null )
    {
      depth ++ ;
      myParent = myParent.getParent ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , pIndent , this
            .toPrettyString ( ).toString ( ) , this.environment
            .toPrettyString ( ).toString ( ) , this.expression
            .toPrettyString ( ).toString ( ) ,
        this.type == null ? LATEX_NO_TYPE : this.type.toPrettyString ( )
            .toString ( ) , this.getRule ( ) == null ? LATEX_NO_RULE : this
            .getRule ( ).toPrettyString ( ).toString ( ) ) ;
    builder.addText ( "{" + String.valueOf ( this.getId ( ) ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.environment.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.expression.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.type == null )
    {
      builder.addEmptyBuilder ( ) ;
    }
    else
    {
      builder.addBuilder ( this.type.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
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
    builder.addBuilder ( this.environment
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_RIGHT_TRIANGLE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    if ( this.type != null )
    {
      builder.addText ( PRETTY_SPACE ) ;
      builder.addText ( PRETTY_COLON ) ;
      builder.addText ( PRETTY_COLON ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.type
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return builder ;
  }


  /**
   * Returns the string representation for this minimal typing expression proof
   * node. This method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this minimal typing
   *         expression proof node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
