package de.unisiegen.tpml.core.minimaltyping ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintableNode ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
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
        LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , 5 ,
        "\\ifthenelse{\\equal{#3}{}}" + LATEX_LINE_BREAK_NEW_COMMAND + "{#3\\ " //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_RIGHT_TRIANGLE + "\\ #4}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{#3\\ " + LATEX_RIGHT_TRIANGLE + "\\ #4\\ ::\\ #5}" , "depth" , //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        "id" , "env" , "e" , "tau" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
   * @see LatexPrintableNode#toLatexString(int, int)
   */
  public final LatexString toLatexString ( int pDepth , int pId )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) ,
        0 , pDepth , pId ).toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintableNode#toLatexStringBuilder(LatexStringBuilderFactory,int,int,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent ,
      int pDepth , int pId )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , pIndent , this
            .toPrettyString ( ).toString ( ) , this.environment
            .toPrettyString ( ).toString ( ) , this.expression
            .toPrettyString ( ).toString ( ) ,
        this.type == null ? LATEX_EMPTY_STRING : this.type.toPrettyString ( )
            .toString ( ) ) ;
    builder.addText ( "{" + String.valueOf ( pDepth ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
    builder.addText ( "{" + String.valueOf ( pId ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$
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
    builder.addBuilder ( this.environment
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_RIGHT_TRIANGLE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    if ( this.type != null )
    {
      builder.addText ( PRETTY_SPACE ) ;
      builder.addText ( PRETTY_COLON ) ;
      builder.addText ( PRETTY_COLON ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.type
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return builder ;
  }


  /**
   * Returns the string representation for this minimal typing expression proof
   * node. This method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this minimal typing
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
