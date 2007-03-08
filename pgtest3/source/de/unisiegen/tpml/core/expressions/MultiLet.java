package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.Free ;


/**
 * Instances of this class represent multi let expressions in the expression
 * hierarchy, which assign identifiers values from tuple items.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 * @see Let
 * @see MultiLambda
 */
public final class MultiLet extends Expression
{
  /**
   * The bound identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private String [ ] identifiers ;


  /**
   * The type of the <code>identifiers</code> tuple.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new <code>MultiLet</code> expression with the specified
   * <code>identifiers</code> and the given expressions <code>e1</code> and
   * <code>e2</code>.
   * 
   * @param pIdentifiers non-empty set of identifiers.
   * @param pTau the type of the <code>identifiers</code> tuple (that is the
   *          type of <code>e1</code>) or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public MultiLet ( String [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression1 , Expression pExpression2 )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length == 0 )
    {
      throw new IllegalArgumentException ( "identifiers is empty" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.identifiers = pIdentifiers ;
    this.tau = pTau ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Multi-Let" ; //$NON-NLS-1$
  }


  /**
   * Returns the identifiers for the tuple items.
   * 
   * @return the identifiers for the tuple items.
   * @see #getIdentifiers(int)
   */
  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the <code>n</code>th identifier.
   * 
   * @param pIndex the index of the identifier to return.
   * @return the <code>n</code>th identifier.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getIdentifiers()
   */
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * Returns the tuple type for <code>e1</code> or <code>null</code>.
   * 
   * @return the type for <code>e1</code> or <code>null</code>.
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLet clone ( )
  {
    return new MultiLet ( this.identifiers , this.tau , this.e1.clone ( ) ,
        this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.e2.free ( ) ) ;
    free.removeAll ( Arrays.asList ( this.identifiers ) ) ;
    free.addAll ( this.e1.free ( ) ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Expression substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new MultiLet ( this.identifiers , newTau , this.e1
        .substitute ( pTypeSubstitution ) , this.e2
        .substitute ( pTypeSubstitution ) ) ;
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
  public Expression substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return new MultiLet ( this.identifiers , this.tau , this.e1.substitute (
            pId , pExpression , pAttributeRename ) , this.e2.clone ( ) ) ;
      }
    }
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    Expression newE2 = this.e2 ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      Free free = new Free ( ) ;
      free.add ( this.e2.free ( ) ) ;
      free.remove ( newIdentifiers [ i ] ) ;
      free.add ( pExpression.free ( ) ) ;
      free.add ( pId ) ;
      if ( free.contains ( newIdentifiers [ i ] ) )
      {
        for ( int j = 0 ; j < newIdentifiers.length ; j ++ )
        {
          if ( i != j )
          {
            free.add ( this.identifiers [ j ] ) ;
          }
        }
      }
      String newId = free.newIdentifier ( newIdentifiers [ i ] ) ;
      if ( ! newIdentifiers [ i ].equals ( newId ) )
      {
        newE2 = newE2.substitute ( newIdentifiers [ i ] , new Identifier (
            newId ) , pAttributeRename ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    return new MultiLet ( newIdentifiers , this.tau , this.e1.substitute ( pId ,
        pExpression , pAttributeRename ) , newE2.substitute ( pId ,
        pExpression , pAttributeRename ) ) ;
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
        this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ; //$NON-NLS-1$
    builder.addText ( "(" ) ;//$NON-NLS-1$
    for ( int i = 0 ; i < this.identifiers.length ; ++ i )
    {
      if ( i > 0 )
      {
        builder.addText ( ", " ) ;//$NON-NLS-1$
      }
      builder.addIdentifier ( this.identifiers [ i ] ) ;
    }
    builder.addText ( ")" ) ;//$NON-NLS-1$
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ;//$NON-NLS-1$
      builder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONSTANT ) ;
    }
    builder.addText ( " = " ) ;//$NON-NLS-1$
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
    builder.addText ( " " ) ;//$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "in" ) ;//$NON-NLS-1$
    builder.addText ( " " ) ;//$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof MultiLet )
    {
      MultiLet other = ( MultiLet ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.e1.hashCode ( )
        + this.e2.hashCode ( )
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 ) ;
  }
}
