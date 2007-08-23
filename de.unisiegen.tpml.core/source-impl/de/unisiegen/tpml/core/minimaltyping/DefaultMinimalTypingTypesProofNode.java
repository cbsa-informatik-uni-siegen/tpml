package de.unisiegen.tpml.core.minimaltyping ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Unify ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.types.MonoType ;


public class DefaultMinimalTypingTypesProofNode extends
    AbstractMinimalTypingProofNode implements MinimalTypingTypesProofNode ,
    LatexPrintable , LatexCommandNames
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
        LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE , 1 , "#1" , "subtype" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
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
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : this.subtype.getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
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


	public void setSeenTypes ( SeenTypes < DefaultSubType > seenTypes ) {
		this.seenTypes = seenTypes;
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
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE ) ;
    builder.addBuilder ( this.subtype
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder builder = new StringBuilder ( ) ;
    builder.append ( this.subtype.getLeft ( ) ) ;
    builder.append ( " &#60: " ) ; //$NON-NLS-1$
    builder.append ( this.subtype.getRight ( ) ) ;
    if ( getRule ( ) != null )
    {
      builder.append ( " (" + getRule ( ) + ")" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    }
    return builder.toString ( ) ;
  }
}
