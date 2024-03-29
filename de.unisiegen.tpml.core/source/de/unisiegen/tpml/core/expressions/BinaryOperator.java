package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Abstract base class for binary operators. Binary operators, like unary
 * operators, are always constants, and as such, are always values.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 */
public abstract class BinaryOperator extends Constant
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_BINARY_OPERATOR , 1 ,
        "\\textbf{\\color{" + LATEX_COLOR_CONSTANT + "}{$#1$}}" , "op" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * The base pretty print priority for this binary operator, when used within
   * an infix operation.
   * 
   * @see #getPrettyPriority()
   * @see InfixOperation
   */
  private int prettyPriority ;


  /**
   * Allocates a new <code>BinaryOperator</code> with the specified
   * <code>prettyPriority</code> used for pretty printing of
   * {@link InfixOperation}s.
   * 
   * @param pText the string representation for this binary operator.
   * @param pPrettyPriority the pretty print priority for infix operations.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  protected BinaryOperator ( String pText , int pPrettyPriority )
  {
    super ( pText ) ;
    this.prettyPriority = pPrettyPriority ;
  }


  /**
   * Applies the binary operator to the operands <code>e1</code> and
   * <code>e2</code> and returns the resulting expression. If the operator
   * cannot be applied to <code>e1</code> and <code>e2</code> because of a
   * runtime type exception, a {@link BinaryOperatorException} is thrown.
   * 
   * @param pExpression1 the first operand.
   * @param pExpression2 the second operand.
   * @return the resulting expression.
   * @throws BinaryOperatorException if the operator cannot be applied to
   *           <code>e1</code> and <code>e2</code>.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public abstract Expression applyTo ( Expression pExpression1 ,
      Expression pExpression2 ) throws BinaryOperatorException ;


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract BinaryOperator clone ( ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = super.getLatexCommands ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    return commands ;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_BINARYOPERATOR ;
    }
    return this.prefix ;
  }


  /**
   * Returns the base pretty print priority for this binary operator, when used
   * within an {@link de.unisiegen.tpml.core.expressions.InfixOperation}.
   * 
   * @return the base pretty print priority for this binary operator.
   */
  public int getPrettyPriority ( )
  {
    return this.prettyPriority ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Constant#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_CONSTANT , LATEX_BINARY_OPERATOR , pIndent , this
            .toPrettyString ( ).toString ( ) ) ;
    builder.addText ( "{" ) ; //$NON-NLS-1$
    if ( ! ( this.parent instanceof InfixOperation ) )
    {
      builder.addText ( LATEX_LPAREN ) ;
    }
    builder.addText ( this.text.replaceAll ( "_" , "\\\\_" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    if ( ! ( this.parent instanceof InfixOperation ) )
    {
      builder.addText ( LATEX_RPAREN ) ;
    }
    builder.addText ( "}" ) ; //$NON-NLS-1$
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Constant#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CONSTANT ) ;
      if ( ! ( this.parent instanceof InfixOperation ) )
      {
        this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
      }
      this.prettyStringBuilder.addConstant ( this.text ) ;
      if ( ! ( this.parent instanceof InfixOperation ) )
      {
        this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
      }
    }
    return this.prettyStringBuilder ;
  }
}
