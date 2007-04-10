package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
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
  private Expression e ;


  /**
   * TODO
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private Identifier [ ] identifiers ;


  /**
   * TODO
   * 
   * @param pFirstExpression TODO
   * @param pIdentifiers TODO
   * @param pExpressions TODO
   */
  public Duplication ( Expression pFirstExpression ,
      Identifier [ ] pIdentifiers , Expression [ ] pExpressions )
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
    ArrayList < Identifier > idList = new ArrayList < Identifier > ( ) ;
    ArrayList < Expression > exprList = new ArrayList < Expression > ( ) ;
    for ( int i = pIdentifiers.length - 1 ; i >= 0 ; i -- )
    {
      if ( ! idList.contains ( pIdentifiers [ i ] ) )
      {
        idList.add ( 0 , pIdentifiers [ i ] ) ;
        exprList.add ( 0 , pExpressions [ i ] ) ;
      }
    }
    this.e = pFirstExpression ;
    this.identifiers = new Identifier [ idList.size ( ) ] ;
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
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Duplication ( this.e.clone ( ) , newIdentifiers , newExpressions ) ;
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
      return ( ( this.e.equals ( other.e ) )
          && ( Arrays.equals ( this.expressions , other.expressions ) ) && ( Arrays
          .equals ( this.identifiers , other.identifiers ) ) ) ;
    }
    return false ;
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
   * @see #e
   */
  public Expression getE ( )
  {
    return this.e ;
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
  public Identifier [ ] getIdentifiers ( )
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
  public Identifier getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.e.hashCode ( ) + this.identifiers.hashCode ( )
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
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Duplication substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication substitute ( Identifier pId , Expression pExpression ,
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
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression ) ;
    }
    Expression newE = this.e.substitute ( pId , pExpression ) ;
    return new Duplication ( newE , newIdentifiers , newExpressions ) ;
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
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    Expression newFirstExpression = this.e.substitute ( pTypeSubstitution ) ;
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Duplication ( newFirstExpression , newIdentifiers ,
        newExpressions ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   * @param pExpression TODO
   * @return TODO
   */
  private Duplication substituteAttribute ( Identifier pId ,
      Expression pExpression )
  {
    if ( ! ( pExpression instanceof Identifier ) )
    {
      throw new IllegalArgumentException (
          "Expression must be an instance of Identifier" ) ; //$NON-NLS-1$
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      /*
       * If the Identifier, which should be substituted, is equal to the
       * Identifier of a child of this Duplication, it should be renamed.
       */
      if ( newIdentifiers [ i ].equals ( pId ) )
      {
        newIdentifiers [ i ] = ( ( Identifier ) pExpression ).clone ( ) ;
      }
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression , true ) ;
    }
    Expression newFirstExpression = this.e.substitute ( pId , pExpression ,
        true ) ;
    return new Duplication ( newFirstExpression , newIdentifiers ,
        newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_DUPLICATION ) ;
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_DUPLICATION_FIRST_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "{" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "<" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
        this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.expressions [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_DUPLICATION_E ) ;
        if ( i != this.expressions.length - 1 )
        {
          this.prettyStringBuilder.addText ( "; " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      // Only one space for '{< >}'
      if ( this.expressions.length > 0 )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      }
      this.prettyStringBuilder.addKeyword ( ">" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "}" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
