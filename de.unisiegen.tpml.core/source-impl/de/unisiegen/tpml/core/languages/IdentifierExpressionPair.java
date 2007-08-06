package de.unisiegen.tpml.core.languages ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public class IdentifierExpressionPair
{
  /**
   * The {@link Identifier}.
   */
  private Identifier id ;


  /**
   * The {@link Expression}.
   */
  private Expression e ;


  /**
   * Initializes the {@link IdentifierExpressionPair} class.
   * 
   * @param pId The input {@link Identifier}.
   * @param pExpression The input {@link Expression}.
   */
  public IdentifierExpressionPair ( Identifier pId , Expression pExpression )
  {
    this.id = pId ;
    this.e = pExpression ;
  }


  /**
   * Returns the id.
   * 
   * @return The id.
   * @see #id
   */
  public Identifier getId ( )
  {
    return this.id ;
  }


  /**
   * Returns the expression.
   * 
   * @return The expression.
   * @see #e
   */
  public Expression getE ( )
  {
    return this.e ;
  }
}
