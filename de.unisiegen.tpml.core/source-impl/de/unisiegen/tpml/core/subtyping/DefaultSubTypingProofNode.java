package de.unisiegen.tpml.core.subtyping ;


import java.util.ArrayList ;
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
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Default implementation of the <code>SubTypingProofNode</code> interface.
 * The class for nodes in a
 * {@link de.unisiegen.tpml.core.subtyping.SubTypingProofModel}.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode
 */
public class DefaultSubTypingProofNode extends AbstractProofNode implements
    SubTypingProofNode
{
  /**
   * The subtype of this proof node
   */
  private MonoType left ;


  /**
   * The supertype of this proof node
   */
  private MonoType right ;


  /**
   * list of proof steps of this node
   */
  private ProofStep [ ] steps = new ProofStep [ 0 ] ;


  /**
   * Allocates a new proof node with the given types.
   * 
   * @param pLeft the first MonoType of this node
   * @param pRight the second MonoType of this node
   */
  public DefaultSubTypingProofNode ( MonoType pLeft , MonoType pRight )
  {
    this.left = pLeft ;
    this.right = pRight ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getChildAt(int)
   */
  @ Override
  public DefaultSubTypingProofNode getChildAt ( final int childIndex )
  {
    return ( DefaultSubTypingProofNode ) super.getChildAt ( childIndex ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
   */
  @ Override
  public DefaultSubTypingProofNode getLastLeaf ( )
  {
    return ( DefaultSubTypingProofNode ) super.getLastLeaf ( ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SUB_TYPE_PROOF_NODE , 4 ,
        "#3\\ " //$NON-NLS-1$
            + "<:\\ #4" , "depth" , "id" , "tau1" , "tau2" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    for ( LatexCommand command : this.left.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.right.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.left.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    for ( LatexInstruction instruction : this.right.getLatexInstructions ( ) )
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
    for ( LatexPackage pack : this.left.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.right.getLatexPackages ( ) )
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
    return this.left ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getRight()
   */
  public MonoType getRight ( )
  {
    return this.right ;
  }


  /**
   * {inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getRule()
   */
  public SubTypingProofRule getRule ( )
  {
    ProofStep [ ] proofSteps = getSteps ( ) ;
    if ( proofSteps.length > 0 )
      return ( SubTypingProofRule ) proofSteps [ 0 ].getRule ( ) ;
    return null ;
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
   * {@inheritDoc}de.unisiegen.tpml.core.ProofNode#isProven()
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#isProven()
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_SUB_TYPE_PROOF_NODE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.left.toPrettyString ( ).toString ( ) ,
        this.right.toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.left.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.right.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
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
    builder.addBuilder ( this.left
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_SUBTYPE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.right
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this sub typing proof node. This
   * method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this sub typing
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
