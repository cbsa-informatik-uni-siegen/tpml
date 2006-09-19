package de.unisiegen.tpml.core.expressions;

import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Represents the <code>let rec</code> expression, which is syntactic sugar for <b>(LET)</b> and <b>(REC)</b>.
 * 
 * The expression
 * <pre>let rec id = e1 in e2</pre>
 * is equal to
 * <pre>let id = rec id.e1 in e2</pre>
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Let
 */
public final class LetRec extends Let {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>LetRec</code> with the given <code>id</code>, <code>e1</code> and <code>e2</code>.
   * 
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression.
   * 
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public LetRec(String id, Expression e1, Expression e2) {
    super(id, e1, e2);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Let#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> set = new TreeSet<String>();
    set.addAll(this.e1.free());
    set.addAll(this.e2.free());
    set.remove(this.id);
    return set;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Let#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> free = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId))
        newId = newId + "'";
      
      // perform the bound renaming
      Expression newE1 = this.e1.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new LetRec(newId, newE1.substitute(id, e), this.e2.substitute(id, e));
    }
  }
  
  
  
  //
  // Pretty Printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Let#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public @Override PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LET);
    builder.addKeyword("let");
    builder.addText(" ");
    builder.addKeyword("rec");
    builder.addText(" " + this.id + " = ");
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_LET_E1);
    builder.addBreak();
    builder.addText(" ");
    builder.addKeyword("in");
    builder.addText(" ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_LET_E2);
    return builder;
  }
}
