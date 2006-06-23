package expressions;

import java.util.Arrays;
import java.util.Vector;

import expressions.annotation.SyntacticSugar;

/**
 * Syntactic sugar for lists.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
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
   * Constructs a new <code>List</code> instance
   * with the specified <code>expressions</code>.
   * 
   * @param expressions a non empty array of {@link Expression}s.
   *                    
   * @throws IllegalArgumentException if <code>expressions</code>
   *                                  is empty.                    
   */
  public List(Expression[] expressions) {
    if (expressions.length == 0) {
      throw new IllegalArgumentException("expressions is empty");
    }
    this.expressions = expressions;
  }

  /**
   * Creates a new <code>List</code> with <code>e1</code> as the
   * first list item. <code>e2</code> can either be the empty
   * list, anoter <code>List</code> instance, or an application
   * of the {@link UnaryCons} operator to a pair where the
   * second item can again be interpreted as <code>List</code>
   * using this constructor.
   * 
   * @param e1 the first item.
   * @param e2 another list.
   * 
   * @throws ClassCastException if none of the above conditions
   *                            match.
   */
  List(Expression e1, Expression e2) {
    // allocate a vector for the expressions of the list and
    // prepend e1 as new first item
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
      if (!(app2.getE1() instanceof UnaryCons) || tuple.getArity() != 2) {
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
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    for (Expression expression : this.expressions) {
      if (!expression.isValue()) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    Expression e = EmptyList.EMPTY_LIST;
    for (int n = this.expressions.length - 1; n >= 0; --n) {
      e = new Application(UnaryCons.CONS, new Tuple(new Expression[] { this.expressions[n], e }));
    }
    return e;
  }

  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // substitute all subexpressions
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n)
      expressions[n] = this.expressions[n].substitute(id, e);
    return new List(expressions);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText("[");
    for (int n = 0; n < this.expressions.length; ++n) {
      if (n > 0) {
        builder.appendText("; ");
        builder.appendBreak();
      }
      builder.appendBuilder(this.expressions[n].toPrettyStringBuilder(), 0);
    }
    builder.appendText("]");
    return builder;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the sub expressions within this <code>List</code>.
   * 
   * @return the sub expressions within this <code>List</code>.
   */
  public Expression[] getExpressions() {
    return this.expressions;
  }
  
  /**
   * Returns the sub expression at the specified <code>index</code>.
   * 
   * @param index the index of the sub expression to return.
   * 
   * @return the sub expression at <code>index</code>.
   * 
   * @throws ArrayIndexOutOfBoundsException if the <code>index</code>
   *                                        is out of bounds.
   */
  public Expression getExpressions(int index) {
    return this.expressions[index];
  }
  
  
  
  //
  // List methods
  //
  
  /**
   * Returns the first expression in the list.
   * 
   * @return the first expression in the list.
   * 
   * @see #tail()
   */
  public Expression head() {
    return this.expressions[0];
  }
  
  /**
   * Returns the list without the first expression,
   * which may be the empty list.
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
}
