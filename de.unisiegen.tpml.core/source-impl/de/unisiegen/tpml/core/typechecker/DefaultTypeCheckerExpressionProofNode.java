package de.unisiegen.tpml.core.typechecker ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The normal type checker proof node in the type checker algorithm, containing
 * an evironment. an expression and a type.
 * 
 * @author Benjamin Mies
 */
public class DefaultTypeCheckerExpressionProofNode extends
    AbstractTypeCheckerProofNode implements TypeCheckerExpressionProofNode
{
  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   * @see #setEnvironment(TypeEnvironment)
   */
  protected TypeEnvironment environment ;


  /**
   * Allocates a new <code>DefaultTypeCheckerExpressionProofNode</code>
   * 
   * @param pEnvironment the type environment of this proof node
   * @param pExpression the expression of this proof node
   * @param pType the type of this proof node
   */
  public DefaultTypeCheckerExpressionProofNode ( TypeEnvironment pEnvironment ,
      Expression pExpression , MonoType pType )
  {
    super ( pExpression ) ;
    this.environment = pEnvironment ;
    this.type = pType ;
  }


  /**
   * Get the type environment of this proof node
   * 
   * @return the type environment of this proof node
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
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_CHECKER_EXPRESSION_PROOF_NODE , 3 , "#1\\ " //$NON-NLS-1$
            + LATEX_RIGHT_TRIANGLE + "\\ #2\\ ::\\ #3" , "env" , "e" , "tau" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    for ( LatexCommand command : this.expression.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.type.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.environment.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.expression
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.environment
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
    packages.add ( new DefaultLatexPackage ( "amssymb" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.expression.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.type.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.environment.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_CHECKER_EXPRESSION_PROOF_NODE , pIndent ) ;
    builder.addBuilder ( this.environment.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.expression.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.type.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
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
    builder.append ( this.environment ) ;
    builder.append ( " \u22b3 " ) ; //$NON-NLS-1$
    builder.append ( this.expression ) ;
    builder.append ( " :: " ) ; //$NON-NLS-1$
    builder.append ( this.type ) ;
    if ( getRule ( ) != null )
    {
      builder.append ( " (" + getRule ( ) + ")" ) ; //$NON-NLS-1$//$NON-NLS-2$
    }
    return builder.toString ( ) ;
  }
}
