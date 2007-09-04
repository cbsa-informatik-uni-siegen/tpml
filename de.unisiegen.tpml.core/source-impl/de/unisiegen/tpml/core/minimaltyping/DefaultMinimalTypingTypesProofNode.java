package de.unisiegen.tpml.core.minimaltyping ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Unify ;
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
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The Type Proof Node for the Minimal Typing Algorithm. Containing two types,
 * and a list of alreday seen types.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see AbstractMinimalTypingProofNode
 */
/**
 * The Types Proof Node for the Minimal Typing Algorithm. Containing a seen
 * types and a sub type.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
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
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_BYRULE , 1 ,
        "\\hspace{-5mm}\\mbox{\\scriptsize\\ #1}" , "rule" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE ,
            5 ,
            "\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\else \\refstepcounter{node}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "$\\begin{tabular}[t]{p{#5}}$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "#3$\\\\$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\byrule{#4} " //$NON-NLS-1$
                + "$\\end{tabular}$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\vspace{\\nodesep}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
                + "\\fi" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            , "depth" , "id" , "subtype" , "rule" , "space" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
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
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE_STYLE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE_STYLE + ": color of normal text" ) ) ; //$NON-NLS-1$
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
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amsmath" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "pstricks" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "pst-node" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amstext" ) ) ; //$NON-NLS-1$
     return packages ;
  }


  /**
   * List of all already seen types
   */
  private SeenTypes < DefaultSubType > seenTypes ;


  /**
   * Subtype Object, containing the subtype and supertype
   */
  private DefaultSubType subtype ;


  /**
   * Allocates a new <code>DefaultMinimalTypingTypesProofNode</code> with the
   * specified <code>types</code>.
   * 
   * @param pType the left{@link MonoType} for this node.
   * @param pType2 the right {@link MonoType} for this node.
   */
  public DefaultMinimalTypingTypesProofNode ( MonoType pType , MonoType pType2 )
  {
    super ( new Unify ( ) ) ;
    this.subtype = new DefaultSubType ( pType , pType2 ) ;
    this.seenTypes = new SeenTypes < DefaultSubType > ( ) ;
  }


  /**
   * There is no type environment needed for this node. So null is returned.
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
   */
  public DefaultTypeEnvironment getEnvironment ( )
  {
    return null ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.subtype.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    if ( getRule ( ) != null )
    {
      for ( LatexCommand command : getRule ( ).getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
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
    for ( LatexInstruction instruction : this.subtype.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    if ( getRule ( ) != null )
    {
      for ( LatexInstruction instruction : getRule ( ).getLatexInstructions ( ) )
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
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.subtype.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( getRule ( ) != null )
    {
      for ( LatexPackage pack : getRule ( ).getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSeenTypes()
   */
  public SeenTypes < DefaultSubType > getSeenTypes ( )
  {
    return this.seenTypes ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getSubType()
   */
  public DefaultSubType getSubType ( )
  {
    return this.subtype ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
   */
  public MonoType getType ( )
  {
    return this.subtype.getLeft ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getType2()
   */
  public MonoType getType2 ( )
  {
    return this.subtype.getRight ( ) ;
  }


  /**
   * Set the seen types of this node
   * 
   * @param pSeenTypes the new already seen types
   */
  public void setSeenTypes ( SeenTypes < DefaultSubType > pSeenTypes )
  {
    this.seenTypes = pSeenTypes ;
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
  public LatexStringBuilder toLatexStringBuilder (
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
        LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE , pIndent , this
            .toPrettyString ( ).toString ( ) , this.subtype.toPrettyString ( )
            .toString ( ) ) ;
    builder.addText ( "{" + String.valueOf ( this.getId ( ) ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.subtype.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.getRule ( ) != null )
      builder.addBuilder ( this.getRule ( ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent ) , 0 ) ;
    else builder.addEmptyBuilder ( ) ;
    int indent = 245 - depth * 7 ;
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
    builder.addBuilder ( this.subtype
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
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
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
