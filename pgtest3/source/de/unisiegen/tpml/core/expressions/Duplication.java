package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Duplication extends Expression
{
  /**
   * TODO
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression firstExpression ;


  /**
   * TODO
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  protected String [ ] identifiers ;


  /**
   * TODO
   * 
   * @param pFirstExpression TODO
   * @param pIdentifiers TODO
   * @param pExpressions TODO
   */
  public Duplication ( Expression pFirstExpression , String [ ] pIdentifiers ,
      Expression [ ] pExpressions )
  {
    if ( pFirstExpression == null )
    {
      throw new NullPointerException ( "FirstExpression is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "Expressions is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length != pExpressions.length )
    {
      throw new IllegalArgumentException (
          "Identifiers and Expressions length must be equal" ) ; //$NON-NLS-1$
    }
    /*
     * Delete all Expressions with the same Identifier apart from the last.
     * Example: "self {< a = 1 ; a = 2 ; a = 3 ; b = 4 >}" -> "self {< a = 3 ;
     * b = 4 >}"
     */
    ArrayList < String > idList = new ArrayList < String > ( ) ;
    ArrayList < Expression > exprList = new ArrayList < Expression > ( ) ;
    for ( int i = pIdentifiers.length - 1 ; i >= 0 ; i -- )
    {
      if ( ! idList.contains ( pIdentifiers [ i ] ) )
      {
        idList.add ( 0 , pIdentifiers [ i ] ) ;
        exprList.add ( 0 , pExpressions [ i ] ) ;
      }
    }
    this.firstExpression = pFirstExpression ;
    this.identifiers = new String [ idList.size ( ) ] ;
    this.expressions = new Expression [ exprList.size ( ) ] ;
    for ( int i = 0 ; i < idList.size ( ) ; i ++ )
    {
      this.identifiers [ i ] = idList.get ( i ) ;
      this.expressions [ i ] = exprList.get ( i ) ;
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Duplication ( this.firstExpression.clone ( ) , this.identifiers ,
        newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Duplication )
    {
      Duplication other = ( Duplication ) pObject ;
      return ( ( this.firstExpression.equals ( other.firstExpression ) )
          && ( Arrays.equals ( this.expressions , other.expressions ) ) && ( Arrays
          .equals ( this.identifiers , other.identifiers ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    /*
     * Add all free Identifiers of the first Expression.
     */
    free.addAll ( this.firstExpression.free ( ) ) ;
    /*
     * Add all free Identifiers of each child Expression.
     */
    for ( Expression expr : this.expressions )
    {
      free.addAll ( expr.free ( ) ) ;
    }
    return free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Duplication" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #firstExpression
   */
  public Expression getE ( )
  {
    return this.firstExpression ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expressions
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   * @see #expressions
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #identifiers
   * @see #getIdentifiers(int)
   */
  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * TODO
   * 
   * @param pIndex
   * @return TODO
   * @see #identifiers
   * @see #getIdentifiers()
   */
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.firstExpression.hashCode ( ) + this.identifiers.hashCode ( )
        + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return false ;
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
   */
  @ Override
  public Duplication substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    /*
     * Perform the Attribute renaming, if pAttributeRename is true.
     */
    if ( pAttributeRename )
    {
      return substituteAttribute ( pId , pExpression ) ;
    }
    /*
     * Perform the normal substitution.
     */
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression ) ;
    }
    return new Duplication ( this.firstExpression.substitute ( pId ,
        pExpression ) , this.identifiers , newExpressions ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   * @param pExpression TODO
   * @return TODO
   */
  private Duplication substituteAttribute ( String pId , Expression pExpression )
  {
    if ( ! ( pExpression instanceof Identifier ) )
    {
      throw new IllegalArgumentException (
          "Expression must be an instance of Identifier" ) ; //$NON-NLS-1$
    }
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    for ( int i = 0 ; i < newExpr.length ; i ++ )
    {
      /*
       * If the Identifier, which should be substituted, is equal to the
       * Identifier of a child of this Duplication, it should be renamed.
       */
      if ( newIdentifiers [ i ].equals ( pId ) )
      {
        newIdentifiers [ i ] = ( ( Identifier ) pExpression ).getName ( ) ;
      }
      newExpr [ i ] = this.expressions [ i ].substitute ( pId , pExpression ,
          true ) ;
    }
    return new Duplication ( this.firstExpression.substitute ( pId ,
        pExpression , true ) , newIdentifiers , newExpr ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Duplication substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpr [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new Duplication ( this.firstExpression
        .substitute ( pTypeSubstitution ) , this.identifiers , newExpr ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_DUPLICATION ) ;
    builder.addBuilder ( this.firstExpression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_DUPLICATION_FIRST_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( "{" ) ; //$NON-NLS-1$
    builder.addKeyword ( "<" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addIdentifier ( this.identifiers [ i ] ) ;
      builder.addText ( " = " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_DUPLICATION_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( "; " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
    }
    // Only one space for '{< >}'
    if ( this.expressions.length > 0 )
    {
      builder.addText ( " " ) ; //$NON-NLS-1$
    }
    builder.addKeyword ( ">" ) ; //$NON-NLS-1$
    builder.addKeyword ( "}" ) ; //$NON-NLS-1$
    return builder ;
  }
}
