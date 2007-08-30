package de.unisiegen.tpml.core.bigstep ;


import java.util.TreeSet;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintableNode;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


/**
 * Default implementation of the <code>BigStepProofNode</code> interface. The
 * class for nodes in a {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode
 */
public final class DefaultBigStepProofNode extends AbstractInterpreterProofNode
    implements BigStepProofNode
{
  /**
   * The result of the evaluation of the expression at this node. May be either
   * <code>null</code> if the node is not yet proven, or a value (see
   * {@link Expression#isValue()}) or an exception (see
   * {@link Expression#isException()}) with a store.
   * 
   * @see #getResult()
   * @see #setResult(BigStepProofResult)
   */
  private BigStepProofResult result ;


  /**
   * Convenience wrapper for {@link #DefaultBigStepProofNode(Expression, Store)},
   * which passes an empty {@link Store} for the <code>store</code> parameter.
   * 
   * @param pExpression the {@link Expression} for this node.
   * @throws NullPointerException if <code>expression</code> is
   *           <code>null</code>.
   */
  public DefaultBigStepProofNode ( Expression pExpression )
  {
    this ( pExpression , new DefaultStore ( ) ) ;
  }


  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the specified
   * <code>expression</code> and <code>store</code>.
   * 
   * @param pExpression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>store</code> is <code>null</code>.
   */
  public DefaultBigStepProofNode ( Expression pExpression , Store store )
  {
    super ( pExpression , store ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultBigStepProofNode getChildAfter ( TreeNode aChild )
  {
    return ( DefaultBigStepProofNode ) super.getChildAfter ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAt(int)
   */
  @ Override
  public DefaultBigStepProofNode getChildAt ( int childIndex )
  {
    return ( DefaultBigStepProofNode ) super.getChildAt ( childIndex ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultBigStepProofNode getChildBefore ( TreeNode aChild )
  {
    return ( DefaultBigStepProofNode ) super.getChildBefore ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstChild()
   */
  @ Override
  public DefaultBigStepProofNode getFirstChild ( )
  {
    return ( DefaultBigStepProofNode ) super.getFirstChild ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstLeaf()
   */
  @ Override
  public DefaultBigStepProofNode getFirstLeaf ( )
  {
    return ( DefaultBigStepProofNode ) super.getFirstLeaf ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastChild()
   */
  @ Override
  public DefaultBigStepProofNode getLastChild ( )
  {
    return ( DefaultBigStepProofNode ) super.getLastChild ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
   */
  @ Override
  public DefaultBigStepProofNode getLastLeaf ( )
  {
    return ( DefaultBigStepProofNode ) super.getLastLeaf ( ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
	  
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    
    commands.add ( new DefaultLatexCommand ( LATEX_BYRULE , 1 ,
   		 "\\hspace{-5mm}\\byrulecolor\\mbox{\\scriptsize\\ #1}","rule"));
    
    commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_PROOF_NODE , 7 ,
   		 "\\ifarrows"+ LATEX_LINE_BREAK_NEW_COMMAND
   		 +"\\else \\refstepcounter{node}"+ LATEX_LINE_BREAK_NEW_COMMAND
   		 +"\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}"+ LATEX_LINE_BREAK_NEW_COMMAND
   		 +"\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}"+ LATEX_LINE_BREAK_NEW_COMMAND
   		   +"$\\begin{tabular}[t]{p{#7}}|$"+ LATEX_LINE_BREAK_NEW_COMMAND
            + "\\ifthenelse{\\equal{#4}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$ 
            + "{#3\\ \\Downarrow\\ #5}" //$NON-NLS-1$
            + "{(#3\\ \\ #4)\\ \\Downarrow\\ #5}" //$NON-NLS-1$
         		+"$\\\\$"+ LATEX_LINE_BREAK_NEW_COMMAND  +
         		"\\byrule{#6} "
           + "$\\end{tabular}$"+ LATEX_LINE_BREAK_NEW_COMMAND
         +"\\vspace{\\nodesep}"+ LATEX_LINE_BREAK_NEW_COMMAND
         +"\\fi"+ LATEX_LINE_BREAK_NEW_COMMAND
        , "depth" , "id" , "e" , "store" , "result", "proofrule", "space" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    for ( LatexCommand command : this.expression.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.getStore ( ).getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    if ( this.result != null )
    {
      for ( LatexCommand command : this.result.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    if ( getRule() != null )
    {
      for ( LatexCommand command : getRule().getLatexCommands ( ) )
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
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : this.expression
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.getStore ( )
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    if ( this.result != null )
    {
      for ( LatexInstruction instruction : this.result.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    if ( getRule() != null )
    {
      for ( LatexInstruction instruction : getRule().getLatexInstructions ( ) )
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
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.expression.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.getStore ( ).getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( this.result != null )
    {
      for ( LatexPackage pack : this.result.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    if ( getRule ( ) != null )
    {
      for ( LatexPackage pack : getRule().getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getParent()
   */
  @ Override
  public DefaultBigStepProofNode getParent ( )
  {
    return ( DefaultBigStepProofNode ) super.getParent ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getResult()
   */
  public BigStepProofResult getResult ( )
  {
    return this.result ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofNode#getRoot()
   */
  @ Override
  public DefaultBigStepProofNode getRoot ( )
  {
    return ( DefaultBigStepProofNode ) super.getRoot ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getRule()
   */
  public BigStepProofRule getRule ( )
  {
    ProofStep [ ] steps = getSteps ( ) ;
    if ( steps.length > 0 )
    {
      return ( BigStepProofRule ) steps [ 0 ].getRule ( ) ;
    }
    return null ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#isFinished()
   */
  public boolean isFinished ( )
  {
    if ( ! isProven ( ) )
    {
      return false ;
    }
    for ( int n = 0 ; n < getChildCount ( ) ; ++ n )
    {
      if ( ! getChildAt ( n ).isFinished ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc} A big step proof node is proven if the {@link #result} is
   * non-<code>null</code>.
   * 
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ( )
  {
    return ( this.result != null ) ;
  }


  /**
   * Sets the result for the expression at this node. The <code>result</code>
   * must be either <code>null</code> or a {@link BigStepProofResult} with a
   * valid store and a value or an exception (according to the semantics of the
   * big step interpreter), otherwise an {@link IllegalArgumentException} will
   * be thrown.
   * 
   * @param pResult the new result for this node, or <code>null</code> to
   *          reset the result.
   * @throws IllegalArgumentException if <code>result</code> is invalid.
   * @see #getResult()
   */
  public void setResult ( BigStepProofResult pResult )
  {
    if ( pResult != null && ! pResult.getValue ( ).isException ( )
        && ! pResult.getValue ( ).isValue ( ) )
    {
      throw new IllegalArgumentException ( "result is invalid" ) ; //$NON-NLS-1$
    }
    this.result = pResult ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintableNode#toLatexString()
   */
  public LatexString toLatexString (  )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) ,
       0 ).toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintableNode#toLatexStringBuilder(LatexStringBuilderFactory,int,int,int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent 
       )
  {
	  int depth = 0;
	  BigStepProofNode myParent = this.getParent ( );
	  while (myParent != null){
		  depth++;
		  myParent = myParent.getParent ( );
	  }
	  
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_BIG_STEP_PROOF_NODE , pIndent /*, this.toPrettyString ( )
            .toString ( ) , this.expression.toPrettyString ( ).toString ( ) ,
        this.getStore ( ).toPrettyString ( ).toString ( ) ,
        this.result == null ? LATEX_EMPTY_STRING : this.result
            .toPrettyString ( ).toString ( )*/ ) ;
    builder.addText ( "{" + String.valueOf ( this.getId ( ) ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( depth ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addBuilder ( this.expression.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.expression.containsMemoryOperations ( ) )
    {
      builder.addBuilder ( getStore ( ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
    else
    {
      builder.addEmptyBuilder ( ) ;
    }
    if ( this.result != null )
    {
      builder.addBuilder ( this.result.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
    else
    {
      builder.addEmptyBuilder ( ) ;
    }
    if (this.getRule ( )!= null)
   	 builder.addBuilder ( this.getRule ( ).toLatexStringBuilder ( pLatexStringBuilderFactory, pIndent ), 0 );
    else
   	 builder.addEmptyBuilder ( );
    int indent = 245- depth*7;
    builder.addText ( "{"+ indent +"mm}" );
    
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
    boolean memoryEnabled = getExpression ( ).containsMemoryOperations ( ) ;
    if ( memoryEnabled )
    {
      builder.addText ( PRETTY_LPAREN ) ;
    }
    builder.addBuilder ( this.getExpression ( ).toPrettyStringBuilder (
        pPrettyStringBuilderFactory ) , 0 ) ;
    if ( memoryEnabled )
    {
      builder.addText ( PRETTY_SPACE ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.getStore ( ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_RPAREN ) ;
    }
    builder.addText ( " \u21d3 " ) ; //$NON-NLS-1$
    if ( this.result != null )
    {
      builder.addBuilder ( this.result
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder builder = new StringBuilder ( ) ;
    boolean memoryEnabled = getExpression ( ).containsMemoryOperations ( ) ;
    if ( memoryEnabled )
    {
      builder.append ( '(' ) ;
    }
    builder.append ( getExpression ( ) ) ;
    if ( memoryEnabled )
    {
      builder.append ( ", " ) ; //$NON-NLS-1$
      builder.append ( getStore ( ) ) ;
      builder.append ( ')' ) ;
    }
    builder.append ( " \u21d3 " ) ; //$NON-NLS-1$
    if ( this.result != null )
    {
      if ( memoryEnabled )
      {
        builder.append ( '(' ) ;
      }
      builder.append ( this.result.getValue ( ) ) ;
      if ( memoryEnabled )
      {
        builder.append ( ", " ) ; //$NON-NLS-1$
        builder.append ( this.result.getStore ( ) ) ;
        builder.append ( ')' ) ;
      }
    }
    return builder.toString ( ) ;
  }
}
