package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents an judgemnet for unification
 * 
 * @see TypeFormula
 * @author Benjamin Mies
 */
public class TypeJudgement implements TypeFormula
{
  //
  // Attributes
  //
  /**
   * the type environment of this type judgement
   */
  private TypeEnvironment environment ;


  /**
   * the expression of this type judgement
   */
  private Expression expression ;


  /**
   * the type of this type judgement
   */
  private MonoType type ;


  /**
   * Allocates a new <code>Typejudgemnent</code> with the specified
   * <code>environment</code>. <code>expression</code> and
   * <code>type</code>
   * 
   * @param env DefaultTypeEnvironment
   * @param expr Expression
   * @param t MonoType
   */
  public TypeJudgement ( final TypeEnvironment env , final Expression expr ,
      final MonoType t )
  {
    this.environment = env ;
    this.expression = expr ;
    this.type = t ;
  }


  //
  // Constructor
  //
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object o )
  {
    if ( o instanceof TypeJudgement )
    {
      TypeJudgement other = ( TypeJudgement ) o ;
      if ( ( this.environment.equals ( other.environment ) )
          && ( this.expression.equals ( other.expression ) )
          && ( this.type.equals ( other.type ) ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * get the type environment of this type judgement
   * 
   * @return DefaultTypeEnvironment environment
   */
  public TypeEnvironment getEnvironment ( )
  {
    return this.environment ;
  }


  //
  // Accessors
  //
  /**
   * get the Expression of this type judgement
   * 
   * @return expression Expression of this type judgement
   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
   */
  public Expression getExpression ( )
  {
    return this.expression ;
  }


  /**
   * get the type of this type judgement
   * 
   * @return type MonoType of this judgement
   */
  public MonoType getType ( )
  {
    return this.type ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public int hashCode ( )
  {
    return this.environment.hashCode ( ) + this.expression.hashCode ( )
        + this.type.hashCode ( ) ;
  }


  /**
   * set the type environment of this type judgement
   * 
   * @param pEnvironment new type environment for this type judement
   */
  public void setEnvironment ( final DefaultTypeEnvironment pEnvironment )
  {
    this.environment = pEnvironment ;
  }


  /**
   * set a new expression for this type judgement
   * 
   * @param pExpression the expression to be set
   */
  public void setExpression ( final Expression pExpression )
  {
    this.expression = pExpression ;
  }


  //
  // Base methods
  //
  /**
   * set the type of this type judgement
   * 
   * @param pType new MonoType for this judgement
   */
  public void setType ( final MonoType pType )
  {
    this.type = pType ;
  }


  /**
   * substitude the type equation of this type judgement
   * 
   * @param substitutions TypeSubstitution to substitute
   * @return null (just needed for TypeEquation)
   */
  public TypeJudgement substitute ( ArrayList < TypeSubstitution > substitutions )
  {
    MonoType newType = this.type.clone ( ) ;
    for ( TypeSubstitution s : substitutions )
    {
      newType.substitute ( s ) ;
    }
    return new TypeJudgement ( this.environment , this.expression , newType ) ;
  }


  /**
   * Returns the string representation of the equations contained in this list.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation.
   * @see TypeEquationTypeInference#toString()
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( this.environment ) ;
    builder.append ( " \u22b3 " ) ; //$NON-NLS-1$
    builder.append ( this.expression ) ;
    builder.append ( " :: " ) ; //$NON-NLS-1$
    builder.append ( this.type ) ;
    return builder.toString ( ) ;
  }
}