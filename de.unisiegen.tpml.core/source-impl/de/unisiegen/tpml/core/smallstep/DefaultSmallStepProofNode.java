package de.unisiegen.tpml.core.smallstep ;


import java.util.TreeSet ;
import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * Default implementation of the <code>SmallStepProofNode</code> interface.
 * The class for nodes in a
 * {@link de.unisiegen.tpml.core.smallstep.SmallStepProofModel}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode
 */
public final class DefaultSmallStepProofNode extends
    AbstractInterpreterProofNode implements SmallStepProofNode ,
    LatexPrintable , LatexCommandNames
{
  /**
   * Convenience wrapper for
   * {@link #DefaultSmallStepProofNode(Expression, Store)}, which passes an
   * empty {@link Store} for the <code>store</code> parameter.
   * 
   * @param pExpression the {@link Expression} for this node.
   * @throws NullPointerException if <code>expression</code> is
   *           <code>null</code>.
   */
  public DefaultSmallStepProofNode ( Expression pExpression )
  {
    this ( pExpression , new DefaultStore ( ) ) ;
  }


  /**
   * Allocates a new <code>DefaultSmallStepProofNode</code> with the specified
   * <code>expression</code> and <code>store</code>.
   * 
   * @param pExpression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>store</code> is <code>null</code>.
   */
  public DefaultSmallStepProofNode ( Expression pExpression , Store store )
  {
    super ( pExpression , store ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultSmallStepProofNode getChildAfter ( TreeNode aChild )
  {
    return ( DefaultSmallStepProofNode ) super.getChildAfter ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
   */
  @ Override
  public DefaultSmallStepProofNode getChildAt ( int childIndex )
  {
    return ( DefaultSmallStepProofNode ) super.getChildAt ( childIndex ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultSmallStepProofNode getChildBefore ( TreeNode aChild )
  {
    return ( DefaultSmallStepProofNode ) super.getChildBefore ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
   */
  @ Override
  public DefaultSmallStepProofNode getFirstChild ( )
  {
    return ( DefaultSmallStepProofNode ) super.getFirstChild ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
   */
  @ Override
  public DefaultSmallStepProofNode getFirstLeaf ( )
  {
    return ( DefaultSmallStepProofNode ) super.getFirstLeaf ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
   */
  @ Override
  public DefaultSmallStepProofNode getLastChild ( )
  {
    return ( DefaultSmallStepProofNode ) super.getLastChild ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
   */
  @ Override
  public DefaultSmallStepProofNode getLastLeaf ( )
  {
    return ( DefaultSmallStepProofNode ) super.getLastLeaf ( ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_PROOF_NODE , 2 ,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{#1}" + LATEX_LINE_BREAK_NEW_COMMAND + "{(#1\\ \\ #2)}" , //$NON-NLS-1$//$NON-NLS-2$
        "e" , "store" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    for ( LatexCommand command : this.getStore ( ).getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.getExpression ( ).getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.getStore ( )
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.getExpression ( )
        .getLatexInstructions ( ) )
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
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.getStore ( ).getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.getExpression ( ).getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
   */
  @ Override
  public DefaultSmallStepProofNode getParent ( )
  {
    return ( DefaultSmallStepProofNode ) super.getParent ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
   */
  @ Override
  public DefaultSmallStepProofNode getRoot ( )
  {
    return ( DefaultSmallStepProofNode ) super.getRoot ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ( )
  {
    // check if any axiom was applied
    for ( ProofRule rule : getRules ( ) )
    {
      if ( ( ( SmallStepProofRule ) rule ).isAxiom ( ) )
      {
        return true ;
      }
    }
    return false ;
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
        0 , LATEX_SMALL_STEP_PROOF_NODE ) ;
    builder.addBuilder ( this.getExpression ( ).toLatexStringBuilder (
        pLatexStringBuilderFactory ) , 0 ) ;
    if ( this.getExpression ( ).containsMemoryOperations ( ) )
    {
      builder.addBuilder ( this.getStore ( ).toLatexStringBuilder (
          pLatexStringBuilderFactory ) , 0 ) ;
    }
    else
    {
      builder.addEmptyBuilder ( ) ;
    }
    return builder ;
  }


  /**
   * {@inheritDoc} This is mainly useful for debugging.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder builder = new StringBuilder ( ) ;
    builder.append ( '(' ) ;
    builder.append ( getStore ( ) ) ;
    builder.append ( ", " ) ; //$NON-NLS-1$
    builder.append ( getExpression ( ) ) ;
    builder.append ( ')' ) ;
    return builder.toString ( ) ;
  }
}
