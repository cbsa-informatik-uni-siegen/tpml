package expressions;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import expressions.annotation.SyntacticSugar;

/**
 * Abstract base class for all kinds of expressions in the
 * expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class Expression {
  /**
   * Returns <code>true</code> if the expression is an
   * exception that cannot be evaluated any further.
   * 
   * The default implementation simply returns <code>false</code>,
   * so derived classes will need to override this method if the
   * class represents an exception.
   * 
   * @return <code>true</code> if the expression is an
   *         exception.
   */
  public boolean isException() {
    return false;
  }
  
  /**
   * Returns <code>true</code> if the expression is
   * syntactic sugar, that is not included in the
   * core syntax.
   * 
   * Note that <code>false</code> is returned if this
   * expression is part of the core syntax, but one of
   * the sub expressions is syntactic sugar. If you
   * want to know whether an expression contains
   * syntactic sugar in some way, you should use the
   * {@link #containsSyntacticSugar()} method instead.
   * 
   * The default implementation of this method returns
   * <code>true</code> if the class on which this method
   * is called is annotated with the {@link SyntacticSugar}
   * annotation (or any of it's super classes is annotated
   * with {@link SyntacticSugar}).
   * 
   * @return <code>true</code> if this - the outer most -
   *         expression is not part of the core syntax.
   *         
   * @see #containsSyntacticSugar()         
   */
  public boolean isSyntacticSugar() {
    for (Class<?> clazz = getClass(); clazz != Expression.class; clazz = clazz.getSuperclass()) {
      if (clazz.isAnnotationPresent(SyntacticSugar.class)) {
        return true;
      }
    }
    return false;
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
   * The default implementation in the {@link Expression}
   * class uses introspection to determine the sub expressions
   * and calls <code>free()</code> recursively on all sub
   * expressions. Some of the derived classes might need to
   * override this method if they represents a binding mechanism,
   * like {@link Let}, or if they don't have sub expressions,
   * but provide free identifiers, like {@link Identifier}.
   * 
   * @return the free identifiers within this expression.
   */
  public Set<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    for (Enumeration<Expression> c = children(); c.hasMoreElements(); ) {
      free.addAll(c.nextElement().free());
    }
    return free;
  }
  
  /**
   * Returns <code>true</code> if the expression contains
   * memory references or locations.
   * 
   * The default implementation in the {@link Expression}
   * class uses introspection to determine the sub expressions
   * and calls <code>containsReferences()</code> recursively
   * on all sub expressions. Some of the derived classes, like
   * {@link Location} or {@link Ref}, will need to override
   * this method appropriately.
   * 
   * @return <code>true</code> if the expression contains
   *                           memory references or locations.
   */
  public boolean containsReferences() {
    for (Enumeration<Expression> c = children(); c.hasMoreElements(); ) {
      if (c.nextElement().containsReferences()) {
        return true;
      }
    }
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
   * @see #isSyntacticSugar()         
   * @see #translateSyntacticSugar()
   */
  public final boolean containsSyntacticSugar() {
    // check if this expression is syntactic sugar
    if (isSyntacticSugar()) {
      return true;
    }
    else {
      // test if any of the sub expressions is syntactic sugar
      for (Enumeration<Expression> c = children(); c.hasMoreElements(); ) {
        if (c.nextElement().containsSyntacticSugar()) {
          return true;
        }
      }
      return false;
    }
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
  
  
  
  //
  // Introspections
  //
  
  /**
   * Returns an enumeration for the direct ancestor expressions, the
   * direct children, of this expression. The enumeration is generated
   * using the bean properties for every {@link Expression} derived
   * class. For example, {@link Application} provides <code>getE1</code>
   * and <code>getE2</code>, and thereby the sub expressions <code>e1</code>
   * and <code>e2</code>. It also supports arrays of expressions, as used
   * in the {@link Tuple} expression class.
   * 
   * @return an {@link Enumeration} for the direct ancestor expressions
   *         of this expression.
   */
  protected final Enumeration<Expression> children() {
    try {
      Vector<Expression> expressions = new Vector<Expression>();
      PropertyDescriptor[] properties = Introspector.getBeanInfo(getClass(), Expression.class).getPropertyDescriptors();
      for (PropertyDescriptor property : properties) {
        Object value = property.getReadMethod().invoke(this);
        if (value instanceof Expression[]) {
          expressions.addAll(Arrays.asList((Expression[])value));
        }
        else if (value instanceof Expression) {
          expressions.add((Expression)value);
        }
      }
      return expressions.elements();
    }
    catch (RuntimeException exception) {
      throw exception;
    }
    catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
