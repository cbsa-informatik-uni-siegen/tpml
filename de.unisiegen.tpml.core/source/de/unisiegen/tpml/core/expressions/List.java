package de.unisiegen.tpml.core.expressions;

import java.util.Arrays;
import java.util.Vector;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Syntactic sugar for lists.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.EmptyList
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.expressions.UnaryCons
 */
public final class List extends Expression {
  //
  // Attributes
  //
  
  /**
   * The expressions within this list.
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression[] expressions;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>List</code> instance with the specified <code>expressions</code>.
   * 
   * @param expressions a non empty array of {@link Expression}s.
   * 
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   * @throws NullPointerException if <code>expressions</code> is <code>null</code>.
   */
  public List(Expression[] expressions) {
    if (expressions == null) {
      throw new NullPointerException("expressions is null");
    }
    if (expressions.length == 0) {
      throw new IllegalArgumentException("expressions is empty");
    }
    this.expressions = expressions;
  }
  
  /**
   * Creates a new <code>List</code> with <code>e1</code> as the first list item. <code>e2</code> can
   * either be the empty list, anoter <code>List</code> instance, or an application of the
   * {@link UnaryCons} operator to a pair where the second item can again be interpreted as
   * <code>List</code> using this constructor.
   * 
   * @param e1 the first item.
   * @param e2 another list.
   * 
   * @throws ClassCastException if none of the above conditions match.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  List(Expression e1, Expression e2) {
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    
    // allocate a vector for the expressions of the list and prepend e1 as new first item
    Vector<Expression> expressions = new Vector<Expression>();
    expressions.add(e1);
    
    // now check e2
    if (e2 instanceof EmptyList) {
      // e2 is the empty list, nothing to append
    }
    else if (e2 instanceof List) {
      // e2 is a List, append the items
      expressions.addAll(Arrays.asList(((List)e2).getExpressions()));
    }
    else {
      // e2 must be an application of unary cons to a pair
      Application app2 = (Application)e2;
      Tuple tuple = (Tuple)app2.getE2();
      if (!(app2.getE1() instanceof UnaryCons) || tuple.getExpressions().length != 2) {
        throw new ClassCastException();
      }
      
      // turn the tuple into a list
      List list = new List(tuple.getExpressions(0), tuple.getExpressions(1));
      
      // and add the list items to our expressions
      expressions.addAll(Arrays.asList(list.getExpressions()));
    }
    
    // jep, we have our expression list
    this.expressions = expressions.toArray(new Expression[0]);
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the list expressions.
   * 
   * @return the expressions.
   */
  public Expression[] getExpressions() {
    return this.expressions;
  }
  
  /**
   * Returns the <code>n</code>th expression.
   * 
   * @param n the index of the expression to return.
   * 
   * @return the <code>n</code>th expression.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of bounds.
   */
  public Expression getExpressions(int n) {
    return this.expressions[n];
  }
  
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public List clone() {
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n) {
      expressions[n] = this.expressions[n].clone();
    }
    return new List(expressions);
  }
  
  /**
   * Returns the first in the list.
   * 
   * @return the first expression in the list.
   * 
   * @see #tail()
   */
  public Expression head() {
    return this.expressions[0];
  }
  
  /**
   * Returns the list without the first expression which may be the empty list.
   * 
   * @return the list without the first expression.
   * 
   * @see #head()
   */
  public Expression tail() {
    if (this.expressions.length > 1) {
      Expression[] expressions = new Expression[this.expressions.length - 1];
      for (int n = 0; n < expressions.length; ++n) {
        expressions[n] = this.expressions[n + 1];
      }
      return new List(expressions);
    }
    else {
      return EmptyList.EMPTY_LIST;
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Expression substitute(TypeSubstitution substitution) {
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n) {
      expressions[n] = this.expressions[n].substitute(substitution);
    }
    return new List(expressions);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n) {
      expressions[n] = this.expressions[n].substitute(id, e);
    }
    return new List(expressions);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    for (Expression e : this.expressions) {
      if (!e.isValue()) {
        return false;
      }
    }
    return true;
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LIST);
    builder.addText("[");
    for (int n = 0; n < this.expressions.length; ++n) {
      if (n > 0) {
        builder.addText("; ");
        builder.addBreak();
      }
      builder.addBuilder(this.expressions[n].toPrettyStringBuilder(factory), PRIO_LIST_E);
    }
    builder.addText("]");
    return builder;
  }

  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof List) {
      List other = (List)obj;
      return Arrays.equals(this.expressions, other.expressions);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(this.expressions);
  }
}
