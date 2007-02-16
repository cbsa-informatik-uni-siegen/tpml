package de.unisiegen.tpml.core.expressions ;


import java.beans.Introspector ;
import java.beans.PropertyDescriptor ;
import java.util.Arrays ;
import java.util.Collections ;
import java.util.Enumeration ;
import java.util.LinkedList ;
import java.util.Set ;
import java.util.TreeSet ;
import java.util.Vector ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Base class for all classes in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see PrettyPrintable
 */
public abstract class Expression implements Cloneable , PrettyPrintable ,
    PrettyPrintPriorities
{
  /**
   * A level-order enumeration of the expressions within a given expression.
   * Used to implement the {@link Expression#levelOrderEnumeration()} method.
   * 
   * @see Expression#levelOrderEnumeration()
   */
  private class LevelOrderEnumeration implements Enumeration < Expression >
  {
    /**
     * Initializes the list.
     */
    private LinkedList < Expression > queue = new LinkedList < Expression > ( ) ;


    /**
     * Adds the Expression.
     * 
     * @param pExpression The new Expression.
     */
    LevelOrderEnumeration ( Expression pExpression )
    {
      this.queue.add ( pExpression ) ;
    }


    /**
     * Returns true, if the enumeration has more elements.
     * 
     * @return True, if the enumeration has more elements.
     * @see Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements ( )
    {
      return ! this.queue.isEmpty ( ) ;
    }


    /**
     * Returns the next element.
     * 
     * @return The next element.
     * @see Enumeration#nextElement()
     */
    public Expression nextElement ( )
    {
      Expression e = this.queue.poll ( ) ;
      this.queue.addAll ( Collections.list ( e.children ( ) ) ) ;
      return e ;
    }
  }


  /**
   * Cached vector of sub expressions, so the children do not need to be
   * determined on every invocation of {@link #children()}.
   * 
   * @see #children()
   */
  private transient Vector < Expression > children = null ;


  /**
   * Returns an enumeration for the direct ancestor expressions, the direct
   * children, of this expression. The enumeration is generated using the bean
   * properties for every {@link Expression} derived class. For example,
   * {@link Application} provides <code>getE1()</code> and
   * <code>getE2()</code>, and thereby the sub expressions <code>e1</code>
   * and <code>e2</code>. It also supports arrays of expressions, as used in
   * the {@link Tuple} expression class.
   * 
   * @return an {@link Enumeration} for the direct ancestor expressions of this
   *         expression.
   */
  public final Enumeration < Expression > children ( )
  {
    // check if we already determined the children
    if ( this.children == null )
    {
      try
      {
        this.children = new Vector < Expression > ( ) ;
        PropertyDescriptor [ ] properties = Introspector.getBeanInfo (
            getClass ( ) , Expression.class ).getPropertyDescriptors ( ) ;
        for ( PropertyDescriptor property : properties )
        {
          Object value = property.getReadMethod ( ).invoke ( this ) ;
          if ( value instanceof Expression [ ] )
          {
            this.children.addAll ( Arrays.asList ( ( Expression [ ] ) value ) ) ;
          }
          else if ( value instanceof Expression )
          {
            this.children.add ( ( Expression ) value ) ;
          }
        }
      }
      catch ( RuntimeException exception )
      {
        throw exception ;
      }
      catch ( Exception exception )
      {
        throw new RuntimeException ( exception ) ;
      }
    }
    // return an enumeration for the children
    return this.children.elements ( ) ;
  }


  /**
   * Clones this expression, so that the result is an expression equal to this
   * expression, but with a different object identity. This is used in the
   * substitution of expressions to be able to distinguish different appearances
   * of the same identifier in the pretty printer, as required by the
   * highlighting of bound variables.
   * 
   * @return a deep clone of this object.
   * @see Object#clone()
   */
  @ Override
  public abstract Expression clone ( ) ;


  /**
   * Returns <code>true</code> if the expression contains any of the memory
   * operations, that is either {@link Assign}, {@link Deref} or {@link Ref}.
   * Otherwise <code>false</code> will be returned. This method is used for
   * the {@link de.unisiegen.tpml.core.interpreters.InterpreterProofModel}s to
   * enable the
   * {@link de.unisiegen.tpml.core.interpreters.InterpreterProofModel#isMemoryEnabled()}
   * property depending on whether the expression at the root node contains
   * memory operations or not. This method uses the
   * {@link #levelOrderEnumeration()} method to traverse all sub expressions, so
   * this method does not need to be implemented by any derived class, but it'll
   * work automagically.
   * 
   * @return <code>true</code> if the expression contains any of the memory
   *         operations, <code>false</code> otherwise.
   * @see #children()
   * @see #levelOrderEnumeration()
   */
  public final boolean containsMemoryOperations ( )
  {
    Enumeration < Expression > enumeration = levelOrderEnumeration ( ) ;
    while ( enumeration.hasMoreElements ( ) )
    {
      Expression e = enumeration.nextElement ( ) ;
      if ( ( e instanceof Assign ) || ( e instanceof Deref )
          || ( e instanceof Ref ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public abstract boolean equals ( Object pObject ) ;


  /**
   * Returns the free (unbound) identifiers within the expression, e.g. the name
   * of the identifier for an identifier expression or the free identifiers for
   * its sub expressions in applications, abstractions and recursions. The
   * default implementation in the {@link Expression} class uses introspection
   * to determine the sub expressions and calls <code>free()</code>
   * recursively on all sub expressions. Some of the derived classes might need
   * to override this method if they represents a binding mechanism, like
   * {@link Let}, or if they don't have sub expressions, but provide free
   * identifiers, like {@link Identifier}.
   * 
   * @return the set of free (unbound) identifiers within the expression.
   */
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    for ( Enumeration < Expression > c = children ( ) ; c.hasMoreElements ( ) ; )
    {
      free.addAll ( c.nextElement ( ).free ( ) ) ;
    }
    return free ;
  }


  /**
   * Returns the caption of this {@link Expression}.
   * 
   * @return The caption of this {@link Expression}.
   */
  public abstract String getCaption ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public abstract int hashCode ( ) ;


  /**
   * Returns <code>true</code> if the expression should be considered an
   * exception in the big and small step interpreters and must thereby not be
   * evaluated any futher, nor must any operation be performed on exceptions.
   * The default implementation in the <code>Expression</code> class simply
   * returns <code>false</code>, so derived classes will need to override
   * this method if their instances should be considered values under certain
   * circumstances.
   * 
   * @return <code>true</code> if the expression is an exception,
   *         <code>false</code> otherwise.
   */
  public boolean isException ( )
  {
    return false ;
  }


  /**
   * Returns <code>true</code> if the expression should be considered a value
   * in the big and small step interpreters and must thereby not be evaluated
   * any further. The default implementation in the <code>Expression</code>
   * class simply returns <code>false</code>, so derived classes will need to
   * override this method if their instances should be considered values under
   * certain circumstances.
   * 
   * @return <code>true</code> if the expression is a value,
   *         <code>false</code> otherwise.
   */
  public boolean isValue ( )
  {
    return false ;
  }


  /**
   * Returns an {@link Enumeration} that enumerates the expression within the
   * expression hierarchy starting at this expression in level order (that is
   * breadth first enumeration).
   * 
   * @return a breadth first enumeration of all expressions within the
   *         expression hierarchy starting at this item.
   */
  public Enumeration < Expression > levelOrderEnumeration ( )
  {
    return new LevelOrderEnumeration ( this ) ;
  }


  /**
   * Substitutes the expression <code>e</code> for the identifier
   * <code>id</code> in this expression, and returns the resulting expression.
   * The resulting expression may be a new <code>Expression</code> object or
   * if no substitution took place, the same object. The method operates
   * recursively.
   * 
   * @param pId the name of the identifier.
   * @param pExpression the <code>Expression</code> to substitute.
   * @return the resulting expression.
   * @throws NullPointerException if <code>id</code> or </code>e</code> is
   *           <code>null</code>.
   */
  public abstract Expression substitute ( String pId , Expression pExpression ) ;


  /**
   * Substitutes the expression <code>e</code> for the identifier
   * <code>id</code> in this expression, and returns the resulting expression.
   * The resulting expression may be a new <code>Expression</code> object or
   * if no substitution took place, the same object. The method operates
   * recursively.
   * 
   * @param pId the name of the identifier.
   * @param pExpression the <code>Expression</code> to substitute.
   * @param pAttributeRename False, if a normal substitution should be
   *          performed. True if a attribute rename should be performed.
   * @return the resulting expression.
   * @throws NullPointerException if <code>id</code> or </code>e</code> is
   *           <code>null</code>.
   */
  public abstract Expression substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename ) ;


  /**
   * Applies the type <code>substitution</code> to this expression, to be
   * exact, to the types within this expression, and returns the new expression
   * with the new types. If the expression does not contain any types, this
   * method simply returns a reference to this expression.
   * 
   * @param pTypeSubstitution the type substitution to apply.
   * @return the resulting expression.
   * @throws NullPointerException if <code>substitution</code> is
   *           <code>null</code>.
   */
  public Expression substitute ( @ SuppressWarnings ( "unused" )
  TypeSubstitution pTypeSubstitution )
  {
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * Returns the pretty string builder used to pretty print this expression. The
   * pretty string builder must be allocated from the specified
   * <code>factory</code>, which is currently always the default factory, but
   * may also be another factory in the future.
   * 
   * @param pPrettyStringBuilderFactory the {@link PrettyStringBuilderFactory}
   *          used to allocate the required pretty string builders to pretty
   *          print this expression.
   * @return the pretty string builder used to pretty print this expression.
   * @see #toPrettyString()
   * @see PrettyStringBuilder
   * @see PrettyStringBuilderFactory
   */
  public abstract PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory ) ;


  /**
   * Returns the string representation for this expression. This method is
   * mainly used for debugging.
   * 
   * @return the pretty printed string representation for this expression.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
