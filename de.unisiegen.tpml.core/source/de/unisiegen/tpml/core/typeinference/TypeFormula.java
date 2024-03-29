package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TypeFormula can be a <code>TypeEquation</code> or a
 * <code>DefaultTypeJudgement</code>. TypeFormula are needed for unification
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public interface TypeFormula extends PrettyPrintable , LatexPrintable
{
  /**
   * get the environment of this type formula
   * 
   * @return DefaultTypeEnvironment
   */
  public TypeEnvironment getEnvironment ( ) ;


  /**
   * get the Expression of this type formula
   * 
   * @return Expression
   */
  public Expression getExpression ( ) ;


  /**
   * get the type of this type formula
   * 
   * @return MonoType
   */
  public MonoType getType ( ) ;


  /**
   * substitute equation or type of this type formula
   * 
   * @param s TypeSubstitution
   * @return the substituted type formula
   */
  public TypeFormula substitute ( ArrayList < TypeSubstitution > s ) ;


  /**
   * return a string with all attributes of this formula
   * 
   * @return String
   */
  public String toString ( ) ;
}
