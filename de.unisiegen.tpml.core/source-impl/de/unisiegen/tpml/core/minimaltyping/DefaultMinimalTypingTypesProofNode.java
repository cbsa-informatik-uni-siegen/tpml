package de.unisiegen.tpml.core.minimaltyping ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Unify ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
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
    commands.add ( new DefaultLatexCommand (
        LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE , 3 ,
        "#3" , "id" , "env" , "subtype" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    for ( LatexCommand command : this.subtype.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
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
    for ( LatexInstruction instruction : this.subtype.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
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
    for ( LatexPackage pack : this.subtype.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE , pIndent , this
            .toPrettyString ( ).toString ( ) , this.subtype.toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.subtype.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
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
