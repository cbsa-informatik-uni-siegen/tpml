package de.unisiegen.tpml.core.smallstep ;


import java.util.TreeSet ;
import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


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
    PrettyPrintable
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_PROOF_NODE , 2 ,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{#1}" + LATEX_LINE_BREAK_NEW_COMMAND + "{\\color{" //$NON-NLS-1$ //$NON-NLS-2$
            + LATEX_COLOR_NONE + "}{(}#1\\ \\ #2\\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{)}}" , "e" , "store" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
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
  public static TreeSet < LatexPackage > getLatexPackagesStatic ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    return packages ;
  }


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
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
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
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions.add ( this.getStore ( ) ) ;
    instructions.add ( this.getExpression ( ) ) ;
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
        LATEX_SMALL_STEP_PROOF_NODE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.getExpression ( ).toPrettyString ( )
            .toString ( ) , this.getStore ( ).toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.getExpression ( ).toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.getExpression ( ).containsMemoryOperations ( ) )
    {
      builder.addBuilder ( this.getStore ( ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
    else
    {
      builder.addEmptyBuilder ( ) ;
    }
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
    if ( this.getExpression ( ).containsMemoryOperations ( ) )
    {
      builder.addText ( PRETTY_LPAREN ) ;
      builder.addBuilder ( this.getExpression ( ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.getStore ( ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_RPAREN ) ;
    }
    else
    {
      builder.addBuilder ( this.getExpression ( ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return builder ;
  }


  /**
   * Returns the string representation for this small step proof node. This
   * method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this small step proof
   *         node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
