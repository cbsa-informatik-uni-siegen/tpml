package de.unisiegen.tpml.core.subtypingrec ;


import de.unisiegen.tpml.core.AbstractProofNode ;
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
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType()
   */
  public MonoType getType ( )
  {
    return this.type.getLeft ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType2()
   */
  public MonoType getType2 ( )
  {
    return this.type.getRight ( ) ;
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
    builder.addText ( " <: " ) ; //$NON-NLS-1$
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
