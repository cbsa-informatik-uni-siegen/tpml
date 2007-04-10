package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.Vector ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Syntactic sugar for lists.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see EmptyList
 * @see Expression
 * @see UnaryCons
 */
public final class List extends Expression
{
  /**
   * The expressions within this list.
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * Creates a new <code>List</code> with <code>e1</code> as the first list
   * item. <code>e2</code> can either be the empty list, anoter
   * <code>List</code> instance, or an application of the {@link UnaryCons}
   * operator to a pair where the second item can again be interpreted as
   * <code>List</code> using this constructor.
   * 
   * @param pExpression1 the first item.
   * @param pExpression2 another list.
   * @throws ClassCastException if none of the above conditions match.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  List ( Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    // allocate a vector for the expressions of the list and prepend e1 as new
    // first item
    Vector < Expression > newExpressions = new Vector < Expression > ( ) ;
    newExpressions.add ( pExpression1 ) ;
    // now check e2
    if ( pExpression2 instanceof EmptyList )
    {
      // e2 is the empty list, nothing to append
    }
    else if ( pExpression2 instanceof List )
    {
      // e2 is a List, append the items
      newExpressions.addAll ( Arrays.asList ( ( ( List ) pExpression2 )
          .getExpressions ( ) ) ) ;
    }
    else
    {
      // e2 must be an application of unary cons to a pair
      Application app2 = ( Application ) pExpression2 ;
      Tuple tuple = ( Tuple ) app2.getE2 ( ) ;
      if ( ! ( app2.getE1 ( ) instanceof UnaryCons )
          || tuple.getExpressions ( ).length != 2 )
      {
        throw new ClassCastException ( ) ;
      }
      // turn the tuple into a list
      List list = new List ( tuple.getExpressions ( 0 ) , tuple
          .getExpressions ( 1 ) ) ;
      // and add the list items to our expressions
      newExpressions.addAll ( Arrays.asList ( list.getExpressions ( ) ) ) ;
    }
    // jep, we have our expression list
    this.expressions = newExpressions.toArray ( new Expression [ 0 ] ) ;
  }


  /**
   * Allocates a new <code>List</code> instance with the specified
   * <code>expressions</code>.
   * 
   * @param pExpressions a non empty array of {@link Expression}s.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   */
  public List ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "expressions is null" ) ; //$NON-NLS-1$
    }
    if ( pExpressions.length == 0 )
    {
      throw new IllegalArgumentException ( "expressions is empty" ) ; //$NON-NLS-1$
    }
    this.expressions = pExpressions ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public List clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new List ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof List )
    {
      List other = ( List ) pObject ;
      return Arrays.equals ( this.expressions , other.expressions ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "List" ; //$NON-NLS-1$
  }


  /**
   * Returns the list expressions.
   * 
   * @return the expressions.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the <code>n</code>th expression.
   * 
   * @param pIndex the index of the expression to return.
   * @return the <code>n</code>th expression.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return Arrays.hashCode ( this.expressions ) ;
  }


  /**
   * Returns the first in the list.
   * 
   * @return the first expression in the list.
   * @see #tail()
   */
  public Expression head ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#isValue()
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ! e.isValue ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public List substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public List substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression , pAttributeRename ) ;
    }
    return new List ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public List substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new List ( newExpressions ) ;
  }


  /**
   * Returns the list without the first expression which may be the empty list.
   * 
   * @return the list without the first expression.
   * @see #head()
   */
  public Expression tail ( )
  {
    if ( this.expressions.length > 1 )
    {
      Expression [ ] newExpressions = new Expression [ this.expressions.length - 1 ] ;
      for ( int i = 0 ; i < newExpressions.length ; i ++ )
      {
        newExpressions [ i ] = this.expressions [ i + 1 ] ;
      }
      return new List ( newExpressions ) ;
    }
    return new EmptyList ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LIST ) ;
      this.prettyStringBuilder.addText ( "[" ) ; //$NON-NLS-1$
      for ( int n = 0 ; n < this.expressions.length ; ++ n )
      {
        if ( n > 0 )
        {
          this.prettyStringBuilder.addText ( "; " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBreak ( ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.expressions [ n ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LIST_E ) ;
      }
      this.prettyStringBuilder.addText ( "]" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
