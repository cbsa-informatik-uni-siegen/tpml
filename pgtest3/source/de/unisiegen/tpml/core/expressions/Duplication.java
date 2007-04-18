package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Duplication extends Expression implements
    DefaultIdentifiers , ChildrenExpressions , SortedChildren
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


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
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private Identifier [ ] identifiers ;


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   * @param pExpressions TODO
   */
  public Duplication ( Identifier [ ] pIdentifiers , Expression [ ] pExpressions )
  {
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
    // Identifier
    this.identifiers = new Identifier [ idList.size ( ) ] ;
    this.indicesId = new int [ idList.size ( ) ] ;
    this.expressions = new Expression [ exprList.size ( ) ] ;
    this.indicesE = new int [ exprList.size ( ) ] ;
    for ( int i = 0 ; i < idList.size ( ) ; i ++ )
    {
      // Identifier
      this.identifiers [ i ] = idList.get ( i ) ;
      if ( this.identifiers [ i ].getParent ( ) != null )
      {
        this.identifiers [ i ] = this.identifiers [ i ].clone ( ) ;
      }
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
      // Expression
      this.expressions [ i ] = exprList.get ( i ) ;
      if ( this.expressions [ i ].getParent ( ) != null )
      {
        this.expressions [ i ] = this.expressions [ i ].clone ( ) ;
      }
      this.expressions [ i ].setParent ( this ) ;
      this.indicesE [ i ] = i + 1 ;
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
    return new Duplication ( newIdentifiers , newExpressions ) ;
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
      return ( ( Arrays.equals ( this.expressions , other.expressions ) ) && ( Arrays
          .equals ( this.identifiers , other.identifiers ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.add ( new SelfIdentifier ( ) ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.free.addAll ( this.expressions [ i ].free ( ) ) ;
        this.free.addAll ( this.identifiers [ i ].free ( ) ) ;
      }
    }
    return this.free ;
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
   */
  public int [ ] getExpressionsIndex ( )
  {
    return this.indicesE ;
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
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return this.indicesId ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public String [ ] getIdentifiersPrefix ( )
  {
    String [ ] result = new String [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      result [ i ] = PREFIX_ID ;
    }
    return result ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see SortedChildren#getSortedChildren()
   */
  public PrettyPrintable [ ] getSortedChildren ( )
  {
    PrettyPrintable [ ] result = new PrettyPrintable [ this.identifiers.length
        + this.expressions.length ] ;
    for ( int i = 0 ; i < this.identifiers.length + this.expressions.length ; i ++ )
    {
      if ( i % 2 == 0 )
      {
        result [ i ] = this.identifiers [ i / 2 ] ;
      }
      else
      {
        result [ i ] = this.expressions [ i / 2 ] ;
      }
    }
    return result ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Expression substitute ( Identifier pId , Expression pExpression )
  {
    if ( ( pId.equals ( new SelfIdentifier ( ) ) )
        && ( pExpression instanceof ObjectExpr ) )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pExpression ;
      Row row = ( Row ) objectExpr.getE ( ) ;
      Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( row.free ( ) ) ;
        boundRenaming.add ( this.expressions [ i ].free ( ) ) ;
        newIdentifiers [ i ] = boundRenaming.newId ( new Identifier ( "x" //$NON-NLS-1$
            + ( i + 1 ) ) ) ;
      }
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        row = row
            .substituteRow ( this.identifiers [ i ] , newIdentifiers [ i ] ) ;
      }
      Expression result = new ObjectExpr ( objectExpr.getTau ( ) , row ) ;
      for ( int i = this.expressions.length - 1 ; i >= 0 ; i -- )
      {
        result = new Let ( newIdentifiers [ i ] , null , this.expressions [ i ]
            .substitute ( pId , pExpression ) , result ) ;
      }
      return result ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression ) ;
    }
    return new Duplication ( this.identifiers , newExpressions ) ;
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
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Duplication ( newIdentifiers , newExpressions ) ;
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
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "{<" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
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
      this.prettyStringBuilder.addKeyword ( ">}" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
