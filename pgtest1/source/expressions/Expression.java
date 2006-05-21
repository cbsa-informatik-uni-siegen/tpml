package expressions;

import java.util.Set;
import java.util.TreeSet;

public abstract class Expression {
  /**
   * Returns <code>true</code> if the expression is an
   * exception that cannot be evaluated any further.
   * 
   * @return <code>true</code> if the expression is an
   *         exception.
   */
  public boolean isException() {
    return (this instanceof Exn);
  }
  
  /**
   * Returns <code>true</code> if the expression is a value
   * that cannot be evaluated any further.
   * 
   * @return <code>true</code> if the expression is a value
   *         and no further evaluation is possible.
   */
  public boolean isValue() {
    return false;
  }
  
  /**
   * Substitutes the value <code>v</code> for the identifier <code>>id</code>
   * and returns the resulting expression.
   * 
   * The default implementation of this method provided by the abstract base
   * class <code>Expression</code> simply returns a reference to the expression
   * itself. Derived classes need to override this method if substitution is
   * possible for those expressions.
   * 
   * @param id the name of the identifier.
   * @param e the expression to substitute.
   * 
   * @return the resulting expression.
   */
  public Expression substitute(String id, Expression e) {
    return this;
  }

  /**
   * @throws UnsupportedOperationException on every invocation.
   */
  @Deprecated
  public final Expression evaluate(RuleChain ruleChain) {
    throw new UnsupportedOperationException("evaluate() is no longer used");
  }
  
  /**
   * Returns the free identifiers within this expression.
   * 
   * The default implementation of this method provided by
   * the abstract base class <code>Expression</code> simply
   * returns the empty set. Derived classes should override
   * this method if their respective expressions can contain
   * free identifiers.
   * 
   * @return the free identifiers within this expression.
   */
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  /**
   * Returns <code>true</code> if the expression contains
   * memory references or locations.
   * 
   * @return <code>true</code> if the expression contains
   *                           memory references or locations.
   */
  public boolean containsReferences() {
    return false;
  }
  
  /**
   * Returns <code>true</code> if the expression contains
   * syntactic sugar, else <code>false</code>.
   * 
   * You can call translateSyntacticSugar() to translate
   * all expressions to the core language.
   * 
   * @return <code>true</code> if the expression contains
   *         syntactic sugar.
   *         
   * @see #translateSyntacticSugar()
   */
  public boolean containsSyntacticSugar() {
    return false;
  }
  
  /**
   * If this {@link Expression} is syntactic, it is translated
   * to core syntax.
   *
   * @return the new expression without syntactic sugar.
   * 
   * @see #containsSyntacticSugar()
   */
  public Expression translateSyntacticSugar() {
    return this;
  }

  /**
   * Returns the <code>PrettyString</code> for this expression,
   * which can be used to present the expression to the user.
   * @return the <code>PrettyString</code> for this expression.
   */
  public final PrettyString toPrettyString() {
    return toPrettyStringBuilder().toPrettyString();
  }
  
  /**
   * Default string converter for expressions, will be overridden
   * by special constructs like constants.
   * @return the string representation for the whole expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return toPrettyString().toString();
  }
  
  /**
   * Returns the <code>PrettyStringBuilder</code> for this
   * expression, which can be used to generate a pretty
   * string for the expression.
   * 
   * @return the pretty string builder for the expression.
   */
  protected abstract PrettyStringBuilder toPrettyStringBuilder();  
  
  /**
   * An empty string set, used solely to save memory in
   * derived classes, that don't provide any free
   * identifiers.
   */
  public static final TreeSet<String> EMPTY_SET = new TreeSet<String>();
}
