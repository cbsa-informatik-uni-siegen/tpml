package de.unisiegen.tpml.core.expressions ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Enumeration ;
import java.util.LinkedList ;
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
     * The queue.
     */
    private LinkedList < Expression > queue = new LinkedList < Expression > ( ) ;


    /**
     * Initializes a new <code>LevelOrderEnumeration</code>.
     * 
     * @param pExpression The input <code>Expression</code>.
     */
    LevelOrderEnumeration ( Expression pExpression )
    {
      this.queue.add ( pExpression ) ;
    }


    /**
     * Returns true, if there are more elements, otherwise false.
     * 
     * @return True, if there are more elements, otherwise false.
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
      this.queue.addAll ( e.children ( ) ) ;
      return e ;
    }
  }


  /**
   * The <code>String</code> for an array of children.
   */
  private static final String GET_EXPRESSIONS = "getExpressions" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link Expression}.
   */
  protected static final String PREFIX_EXPRESSION = "e" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link Expression} which is a value.
   */
  protected static final String PREFIX_VALUE = "v" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link Row}.
   */
  protected static final String PREFIX_ROW = "r" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link Row} which is a value.
   */
  protected static final String PREFIX_ROW_VALUE = "\u03C9" ; //$NON-NLS-1$


  /**
   * Prefix of {@link Identifier}s after the first.
   */
  public static final String PREFIX_ID = "id" ; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link Method}s.
   */
  public static final String PREFIX_ID_M = "m" ; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link Attribute}s.
   */
  public static final String PREFIX_ID_A = "a" ; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link ObjectExpr}s.
   */
  public static final String PREFIX_ID_S = "self" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link Exn}.
   */
  protected static final String PREFIX_EXN = "ep" ; //$NON-NLS-1$


  /**
   * Prefix of the {@link BinaryOperator}.
   */
  protected static final String PREFIX_BINARYOPERATOR = "op" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiers
   */
  private static final String GET_IDENTIFIERS = "getIdentifiers" ; //$NON-NLS-1$


  /**
   * Cached <code>TreeSet</code> of the free Identifiers, so the free
   * Identifier do not need to be determined on every invocation of
   * {@link #getIdentifiersFree()}.
   * 
   * @see #getIdentifiersFree()
   */
  protected ArrayList < Identifier > identifiersFree = null ;


  /**
   * A list of all {@link Identifier}s in this {@link Expression}.
   * 
   * @see #getIdentifiersAll()
   */
  private ArrayList < Identifier > identifiersAll = null ;


  /**
   * Cached {@link PrettyStringBuilder}, so the {@link PrettyStringBuilder} do
   * not need to be determined on every invocation of
   * {@link #toPrettyStringBuilder(PrettyStringBuilderFactory)}.
   * 
   * @see #toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  protected PrettyStringBuilder prettyStringBuilder = null ;


  /**
   * A list of lists of bound {@link Identifier}s in this {@link Expression}.
   */
  protected ArrayList < ArrayList < Identifier >> boundIdentifiers = null ;


  /**
   * Cached vector of sub expressions, so the children do not need to be
   * determined on every invocation of {@link #children()}.
   * 
   * @see #children()
   */
  private ArrayList < Expression > children = null ;


  /**
   * The parent of this {@link Expression}.
   * 
   * @see #getParent()
   * @see #setParent(PrettyPrintable)
   */
  protected PrettyPrintable parent = null ;


  /**
   * The start offset of this {@link Expression} in the source code.
   */
  protected int parserStartOffset = - 1 ;


  /**
   * The end offset of this {@link Expression} in the source code.
   */
  protected int parserEndOffset = - 1 ;


  /**
   * The prefix of this {@link Expression}.
   * 
   * @see #getPrefix()
   */
  protected String prefix = null ;


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
  public final ArrayList < Expression > children ( )
  {
    if ( this.children == null )
    {
      this.children = new ArrayList < Expression > ( ) ;
      for ( Class < ? > currentInterface : this.getClass ( ).getInterfaces ( ) )
      {
        if ( currentInterface
            .equals ( de.unisiegen.tpml.core.interfaces.DefaultExpressions.class ) )
        {
          try
          {
            Expression [ ] expressions = ( Expression [ ] ) this.getClass ( )
                .getMethod ( GET_EXPRESSIONS , new Class [ 0 ] ).invoke ( this ,
                    new Object [ 0 ] ) ;
            this.children.addAll ( Arrays.asList ( expressions ) ) ;
          }
          catch ( IllegalArgumentException e )
          {
            System.err.println ( "Expression: IllegalArgumentException" ) ; //$NON-NLS-1$
          }
          catch ( SecurityException e )
          {
            System.err.println ( "Expression: SecurityException" ) ; //$NON-NLS-1$
          }
          catch ( IllegalAccessException e )
          {
            System.err.println ( "Expression: IllegalAccessException" ) ; //$NON-NLS-1$
          }
          catch ( InvocationTargetException e )
          {
            System.err.println ( "Expression: InvocationTargetException" ) ; //$NON-NLS-1$
          }
          catch ( NoSuchMethodException e )
          {
            System.err.println ( "Expression: NoSuchMethodException" ) ; //$NON-NLS-1$
          }
        }
      }
    }
    return this.children ;
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
   * Returns the caption of this {@link Expression}.
   * 
   * @return The caption of this {@link Expression}.
   */
  public abstract String getCaption ( ) ;


  /**
   * Returns true, if a free Identifier is a attribute or self
   * {@link Identifier}, otherwise false.
   * 
   * @return True, if a free Identifier is a attribute or self
   *         {@link Identifier}, otherwise false.
   */
  public boolean getIdentifierFreeNotOnlyVariable ( )
  {
    for ( Identifier id : getIdentifiersFree ( ) )
    {
      if ( ( id.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) )
          || ( id.getSet ( ).equals ( Identifier.Set.SELF ) ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * Returns a list of all {@link Identifier}s in this {@link Expression}.
   * 
   * @return A list of all {@link Identifier}s in this {@link Expression}.
   * @see #identifiersAll
   */
  public final ArrayList < Identifier > getIdentifiersAll ( )
  {
    if ( this.identifiersAll == null )
    {
      this.identifiersAll = new ArrayList < Identifier > ( ) ;
      if ( this instanceof Identifier )
      {
        this.identifiersAll.add ( ( Identifier ) this ) ;
        return this.identifiersAll ;
      }
      for ( Class < ? > currentInterface : this.getClass ( ).getInterfaces ( ) )
      {
        if ( ( currentInterface
            .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
            || ( currentInterface
                .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) ) )
        {
          try
          {
            Identifier [ ] identifiers = ( Identifier [ ] ) this.getClass ( )
                .getMethod ( GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( this ,
                    new Object [ 0 ] ) ;
            this.identifiersAll.addAll ( Arrays.asList ( identifiers ) ) ;
          }
          catch ( IllegalArgumentException e )
          {
            System.err.println ( "Expression: IllegalArgumentException" ) ; //$NON-NLS-1$
          }
          catch ( SecurityException e )
          {
            System.err.println ( "Expression: SecurityException" ) ; //$NON-NLS-1$
          }
          catch ( IllegalAccessException e )
          {
            System.err.println ( "Expression: IllegalAccessException" ) ; //$NON-NLS-1$
          }
          catch ( InvocationTargetException e )
          {
            System.err.println ( "Expression: InvocationTargetException" ) ; //$NON-NLS-1$
          }
          catch ( NoSuchMethodException e )
          {
            System.err.println ( "Expression: NoSuchMethodException" ) ; //$NON-NLS-1$
          }
        }
      }
      for ( Expression child : children ( ) )
      {
        this.identifiersAll.addAll ( child.getIdentifiersAll ( ) ) ;
      }
    }
    return this.identifiersAll ;
  }


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
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      for ( Expression child : children ( ) )
      {
        this.identifiersFree.addAll ( child.getIdentifiersFree ( ) ) ;
      }
    }
    return this.identifiersFree ;
  }


  /**
   * Returns the parent of this {@link Expression}.
   * 
   * @return The parent of this {@link Expression}.
   * @see #parent
   * @see #setParent(PrettyPrintable)
   */
  public final PrettyPrintable getParent ( )
  {
    return this.parent ;
  }


  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   * @see #parserEndOffset
   */
  public int getParserEndOffset ( )
  {
    return this.parserEndOffset ;
  }


  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   * @see #parserStartOffset
   */
  public int getParserStartOffset ( )
  {
    return this.parserStartOffset ;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      if ( this.isValue ( ) )
      {
        this.prefix = PREFIX_VALUE ;
      }
      else
      {
        this.prefix = PREFIX_EXPRESSION ;
      }
    }
    return this.prefix ;
  }


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
   * Sets the parent of this {@link Expression}.
   * 
   * @param pParent The parent to set.
   * @see #parent
   * @see #getParent()
   */
  public final void setParent ( PrettyPrintable pParent )
  {
    this.parent = pParent ;
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
  public abstract Expression substitute ( Identifier pId ,
      Expression pExpression ) ;


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
