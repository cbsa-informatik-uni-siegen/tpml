package de.unisiegen.tpml.core.subtypingrec ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.AbstractProofNode ;
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
import de.unisiegen.tpml.core.subtyping.ProofStep ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.types.MonoType ;


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
    RecSubTypingProofNode , SubTypingProofNode
{
  /**
   * The subtype object containing the subtype and supertype of this node
   */
  private DefaultSubType type ;


  /**
   * List with the already seen types
   */
  private SeenTypes < DefaultSubType > seenTypes ;


  /**
   * list of proof steps of this node
   */
  private ProofStep [ ] steps = new ProofStep [ 0 ] ;


  /**
   * Allocates a new proof step with the given <code>expression</code> and the
   * specified <code>rule</code>.
   * 
   * @param pLeft the first MonoType of this node
   * @param pRight the second MonoType of this node
   * @param pSeenTypes list of all so far seen types
   */
  public DefaultRecSubTypingProofNode ( MonoType pLeft , MonoType pRight ,
      SeenTypes < DefaultSubType > pSeenTypes )
  {
    this.type = new DefaultSubType ( pLeft , pRight ) ;
    this.seenTypes = pSeenTypes ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getChildAt(int)
   */
  @ Override
  public DefaultRecSubTypingProofNode getChildAt ( final int childIndex )
  {
    return ( DefaultRecSubTypingProofNode ) super.getChildAt ( childIndex ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
   */
  @ Override
  public DefaultRecSubTypingProofNode getLastLeaf ( )
  {
    return ( DefaultRecSubTypingProofNode ) super.getLastLeaf ( ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands
        .add ( new DefaultLatexCommand (
            LATEX_REC_SUB_TYPE_PROOF_NODE ,
            5 ,
            "#3\\vdash\\newline " //$NON-NLS-1$
                + "\\ #4\\ <:\\ #5" , "depth" , "id" , "seenTypes" , "tau1" , "tau2" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    for ( LatexCommand command : this.getLeft ( ).getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.getRight ( ).getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.seenTypes.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.getLeft ( )
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.getRight ( )
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.seenTypes.getLatexInstructions ( ) )
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
    for ( LatexPackage pack : this.getLeft ( ).getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.getRight ( ).getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.seenTypes.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getLeft()
   */
  public MonoType getLeft ( )
  {
    return this.type.getLeft ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getRight()
   */
  public MonoType getRight ( )
  {
    return this.type.getRight ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getRule()
   */
  public RecSubTypingProofRule getRule ( )
  {
    ProofStep [ ] proofSteps = getSteps ( ) ;
    if ( proofSteps.length > 0 )
    {
      return ( RecSubTypingProofRule ) proofSteps [ 0 ].getRule ( ) ;
    }
    return null ;
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
   * get the proof steps of this node
   * 
   * @return ProofStep[] steps
   */
  public ProofStep [ ] getSteps ( )
  {
    return this.steps ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSubType()
   */
  public DefaultSubType getSubType ( )
  {
    return this.type ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#isFinished()
   */
  public boolean isFinished ( )
  {
    if ( ! isProven ( ) )
    {
      return false ;
    }
    for ( int n = 0 ; n < getChildCount ( ) ; ++ n )
    {
      if ( ! ( getChildAt ( n ) ).isFinished ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc} de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ( )
  {
    return ( getSteps ( ).length > 0 ) ;
  }


  /**
   * get the proof steps of this node
   * 
   * @param pSteps new proof steps for this node
   */
  public void setSteps ( ProofStep [ ] pSteps )
  {
    this.steps = pSteps ;
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
    StringBuilder body = new StringBuilder ( ) ;
    body.append ( this.seenTypes.toPrettyString ( ).toString ( ) ) ;
    body.append ( PRETTY_SPACE ) ;
    body.append ( PRETTY_NAIL ) ;
    body.append ( PRETTY_SPACE ) ;
    body.append ( this.getLeft ( ).toPrettyString ( ).toString ( ) ) ;
    body.append ( PRETTY_SPACE ) ;
    body.append ( PRETTY_SUBTYPE ) ;
    body.append ( PRETTY_SPACE ) ;
    body.append ( this.getRight ( ).toPrettyString ( ).toString ( ) ) ;
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_REC_SUB_TYPE_PROOF_NODE , pIndent , body.toString ( ) ,
        this.seenTypes.toPrettyString ( ).toString ( ) , this.getLeft ( )
            .toPrettyString ( ).toString ( ) , this.getRight ( )
            .toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.seenTypes.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.getLeft ( ).toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.getRight ( ).toLatexStringBuilder (
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
    builder.addBuilder ( this.type.getLeft ( ).toPrettyStringBuilder (
        pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_SUBTYPE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.type.getRight ( ).toPrettyStringBuilder (
        pPrettyStringBuilderFactory ) , 0 ) ;
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
    final StringBuilder builder = new StringBuilder ( ) ;
    builder.append ( "[ A = " ) ; //$NON-NLS-1$
    for ( DefaultSubType subtype : this.seenTypes )
    {
      builder.append ( " ( " + subtype + " ) " ) ; //$NON-NLS-1$//$NON-NLS-2$
    }
    builder.append ( " ]" ) ; //$NON-NLS-1$
    builder.append ( "\n" ) ; //$NON-NLS-1$
    builder.append ( this.type.getLeft ( ) ) ;
    builder.append ( " <: " ) ; //$NON-NLS-1$
    builder.append ( this.type.getRight ( ) ) ;
    builder.append ( " " ) ; //$NON-NLS-1$
    if ( this.getSteps ( ).length > 0 )
      builder.append ( this.getSteps ( ) [ 0 ].getRule ( ).toString ( ) ) ;
    return builder.toString ( ) ;
  }
}
