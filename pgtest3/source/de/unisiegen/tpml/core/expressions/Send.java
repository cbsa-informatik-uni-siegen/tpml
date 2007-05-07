package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Send extends Expression implements DefaultIdentifiers ,
    ChildrenExpressions , SortedChildren
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private static final int [ ] INDICES_ID = new int [ ]
  { - 1 } ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @param pIdentifier TODO
   */
  public Send ( Expression pExpression , Identifier pIdentifier )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    // Expression
    this.expressions = new Expression [ 1 ] ;
    this.expressions [ 0 ] = pExpression ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      // this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    // Identifier
    this.identifiers = new Identifier [ 1 ] ;
    this.identifiers [ 0 ] = pIdentifier ;
    if ( this.identifiers [ 0 ].getParent ( ) != null )
    {
      // this.identifiers [ 0 ] = this.identifiers [ 0 ].clone ( ) ;
    }
    this.identifiers [ 0 ].setParent ( this ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @param pIdentifier TODO
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   */
  public Send ( Expression pExpression , Identifier pIdentifier ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression , pIdentifier ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send clone ( )
  {
    return new Send ( this.expressions [ 0 ].clone ( ) , this.identifiers [ 0 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Send )
    {
      Send other = ( Send ) pObject ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.identifiers [ 0 ]
          .equals ( other.identifiers [ 0 ] ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Send" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Identifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return INDICES_ID ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see SortedChildren#getSortedChildren()
   */
  public PrettyPrintable [ ] getSortedChildren ( )
  {
    PrettyPrintable [ ] result = new PrettyPrintable [ 2 ] ;
    result [ 0 ] = this.expressions [ 0 ] ;
    result [ 1 ] = this.identifiers [ 0 ] ;
    return result ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( )
        + this.identifiers [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Send ( newE , this.identifiers [ 0 ] ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Send substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Send ( newE , this.identifiers [ 0 ] ) ;
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
          PRIO_SEND ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_SEND_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "#" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
    }
    return this.prettyStringBuilder ;
  }
}
