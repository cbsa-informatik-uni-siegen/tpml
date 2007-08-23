package de.unisiegen.tpml.core.minimaltyping ;


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
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The Expression Proof Node for the Minimal Typing Algorithm. Containing an
 * type environment, an expression and a type.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see AbstractMinimalTypingProofNode
 */
public class DefaultMinimalTypingExpressionProofNode extends
    AbstractMinimalTypingProofNode implements MinimalTypingExpressionProofNode
{
  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   * @see #setEnvironment(TypeEnvironment)
   */
  protected TypeEnvironment environment ;


  /**
   * The type for this type node, which is either a type variable or a monorphic
   * type.
   * 
   * @see #getType()
   * @see #setType(MonoType)
   */
  protected MonoType type ;


  /**
   * Allocates a new <code>DefaultMinimalTypingExpressionProofNode</code> with
   * the specified <code>environment</code>, <code>expression</code> and
   * <code>type</code>.
   * 
   * @param pEnvironment the {@link TypeEnvironment} for this node.
   * @param pExpression the {@link Expression} for this node.
   * @throws NullPointerException if <code>environment</code>,
   *           <code>expression</code> or <code>type</code> is
   *           <code>null</code>.
   */
  public DefaultMinimalTypingExpressionProofNode (
      TypeEnvironment pEnvironment , Expression pExpression )
  {
    super ( pExpression ) ;
    setEnvironment ( pEnvironment ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
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
        LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , 3 ,
        "\\ifthenelse{\\equal{#3}{}}" + LATEX_LINE_BREAK_NEW_COMMAND + "{#1\\ " //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_RIGHT_TRIANGLE + "\\ #2}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{#1\\ " + LATEX_RIGHT_TRIANGLE + "\\ #2\\ ::\\ #3}" , "env" , //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        "e" , "tau" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    for ( LatexCommand command : this.environment.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.expression.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    if ( this.type != null )
    {
      for ( LatexCommand command : this.type.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.environment
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.expression
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    if ( this.type != null )
    {
      for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) )
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
    packages.add ( new DefaultLatexPackage ( "amssymb" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.environment.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.expression.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( this.type != null )
    {
      for ( LatexPackage pack : this.type.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
   */
  public MonoType getType ( )
  {
    return this.type ;
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
   * Sets the type of this proof node to <code>type</code>.
   * 
   * @param pType the new type for this proof node.
   * @throws NullPointerException if <code>type</code> is <code>null</code>.
   * @see #getType()
   */
  public void setType ( MonoType pType )
  {
    this.type = pType ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , pIndent ) ;
    builder.addBuilder ( this.environment.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.expression.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    if ( this.type == null )
    {
      builder.addEmptyBuilder ( ) ;
    }
    else
    {
      builder.addBuilder ( this.type.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
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
