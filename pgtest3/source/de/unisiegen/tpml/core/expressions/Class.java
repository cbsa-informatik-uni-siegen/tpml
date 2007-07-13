package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent class expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Class extends Expression implements BoundIdentifiers ,
    DefaultExpressions
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
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * Allocates a new {@link Class}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pExpression The child {@link Expression}.
   */
  public Class ( Identifier pIdentifier , Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Id is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = new Identifier [ ]
    { pIdentifier } ;
    this.identifiers [ 0 ].setParent ( this ) ;
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
    if ( this.expressions [ 0 ] instanceof Row )
    {
      Row row = ( Row ) this.expressions [ 0 ] ;
      // Calculate the bound Identifiers
      for ( Expression child : row.getExpressions ( ) )
      {
        if ( child instanceof Attribute )
        {
          ( ( Attribute ) child ).getIdentifiersBound ( ) ;
        }
      }
      row.checkDisjunction ( ) ;
    }
  }


  /**
   * Allocates a new {@link Class}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pExpression The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Class ( Identifier pIdentifier , Expression pExpression ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifier , pExpression ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Class clone ( )
  {
    return new Class ( this.identifiers [ 0 ].clone ( ) , this.expressions [ 0 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Class )
    {
      Class other = ( Class ) pObject ;
      return ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Class" ; //$NON-NLS-1$
  }


  /**
   * Returns the sub {@link Expression}.
   * 
   * @return the sub {@link Expression}.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the sub {@link Expression}s.
   * 
   * @return the sub {@link Expression}s.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
  }


  /**
   * Returns the {@link Identifier} of this {@link Expression}.
   * 
   * @return The {@link Identifier} of this {@link Expression}.
   */
  public Identifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns a list of in this {@link Expression} bound {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bound {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundE = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      for ( Identifier freeId : boundE )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
          boundIdList.add ( freeId ) ;
        }
      }
      this.boundIdentifiers.add ( boundIdList ) ;
    }
    return this.boundIdentifiers ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      while ( this.identifiersFree.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
    }
    return this.identifiersFree ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return INDICES_ID ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expressions [ 0 ] instanceof Row ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Class substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    if ( pId.getName ( ).equals ( "self" ) ) //$NON-NLS-1$
    {
      return this ;
    }
    /*
     * Perform the substitution.
     */
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Class ( this.identifiers [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Class substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Class ( this.identifiers [ 0 ] , newE ) ;
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
          PRIO_CLASS ) ;
      this.prettyStringBuilder.addKeyword ( "class" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " (" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      this.prettyStringBuilder.addText ( ") " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder
          .addBuilder ( this.expressions [ 0 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_CLASS_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "end" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
