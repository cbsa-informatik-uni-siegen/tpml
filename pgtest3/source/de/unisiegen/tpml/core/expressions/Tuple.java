package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Instances of this class represent tuples in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Tuple extends Expression
{
  /**
   * The sub expressions.
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>Tuple</code> with the given
   * <code>expressions</code>.
   * 
   * @param pExpressions a non-empty array of {@link Expression}s.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   */
  public Tuple ( Expression [ ] pExpressions )
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
  public Tuple clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Tuple ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Tuple )
    {
      Tuple other = ( Tuple ) pObject ;
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
    return "Tuple" ; //$NON-NLS-1$
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the <code>n</code>th sub expression.
   * 
   * @param pIndex the index of the expression to return.
   * @return the <code>n</code>th sub expression.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getExpressions()
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
    return this.expressions.hashCode ( ) ;
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Tuple substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression , pAttributeRename ) ;
    }
    return new Tuple ( newExpressions ) ;
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
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_TUPLE ) ;
    builder.addText ( "(" ) ; //$NON-NLS-1$
    for ( int n = 0 ; n < this.expressions.length ; ++ n )
    {
      if ( n > 0 )
      {
        builder.addText ( ", " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
      builder
          .addBuilder ( this.expressions [ n ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_TUPLE_E ) ;
    }
    builder.addText ( ")" ) ; //$NON-NLS-1$
    return builder ;
  }
}
