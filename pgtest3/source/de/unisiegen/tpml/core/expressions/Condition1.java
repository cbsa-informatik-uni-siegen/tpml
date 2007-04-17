package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent conditions without an else block in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Condition
 * @see Expression
 */
public final class Condition1 extends Expression implements ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 0 , 1 } ;


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>Condition1</code> with the specified
   * <code>e0</code> and <code>e1</code>.
   * 
   * @param pExpression0 the conditional block.
   * @param pExpression1 the <code>true</code> case.
   * @throws NullPointerException if <code>e0</code> or <code>e1</code> is
   *           <code>null</code>.
   */
  public Condition1 ( final Expression pExpression0 ,
      final Expression pExpression1 )
  {
    if ( pExpression0 == null )
    {
      throw new NullPointerException ( "e0 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    this.expressions = new Expression [ 2 ] ;
    this.expressions [ 0 ] = pExpression0 ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ] = pExpression1 ;
    if ( this.expressions [ 1 ].getParent ( ) != null )
    {
      this.expressions [ 1 ] = this.expressions [ 1 ].clone ( ) ;
    }
    this.expressions [ 1 ].setParent ( this ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Condition1 clone ( )
  {
    return new Condition1 ( this.expressions [ 0 ].clone ( ) ,
        this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( final Object pObject )
  {
    if ( pObject instanceof Condition1 )
    {
      final Condition1 other = ( Condition1 ) pObject ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Condition-1" ; //$NON-NLS-1$
  }


  /**
   * Returns the conditional part.
   * 
   * @return the conditional block
   */
  public Expression getE0 ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the <code>true</code> case.
   * 
   * @return the <code>true</code> block.
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 1 ] ;
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
  public Expression getExpressions ( final int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return Condition1.INDICES_E ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Condition1 substitute ( final Identifier pId ,
      final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Condition1 substitute ( final Identifier pId ,
      final Expression pExpression , final boolean pAttributeRename )
  {
    final Expression newE0 = this.expressions [ 0 ].substitute ( pId ,
        pExpression , pAttributeRename ) ;
    final Expression newE1 = this.expressions [ 1 ].substitute ( pId ,
        pExpression , pAttributeRename ) ;
    return new Condition1 ( newE0 , newE1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Condition1 substitute ( final TypeSubstitution substitution )
  {
    final Expression newE0 = this.expressions [ 0 ].substitute ( substitution ) ;
    final Expression newE1 = this.expressions [ 1 ].substitute ( substitution ) ;
    return new Condition1 ( newE0 , newE1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_CONDITION ) ;
      this.prettyStringBuilder.addKeyword ( "if" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_CONDITION_E0 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "then" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_CONDITION_E1 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
