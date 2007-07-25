package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BodyOrRow ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent body expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Body extends Expression implements BodyOrRow ,
    BoundIdentifiers , DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 , - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected Identifier [ ] identifiers ;


  /**
   * Allocates a new {@link Body}.
   * 
   * @param pIdentifiers The attribute {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pBodyOrRow The child {@link BodyOrRow}.
   */
  public Body ( Identifier [ ] pIdentifiers , Expression pExpression ,
      BodyOrRow pBodyOrRow )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( "One identifier is null" ) ; //$NON-NLS-1$
      }
      if ( ! id.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) )
      {
        throw new IllegalArgumentException (
            "The set of the identifier has to be 'attribute'" ) ; //$NON-NLS-1$
      }
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Espression is null" ) ; //$NON-NLS-1$
    }
    if ( pBodyOrRow == null )
    {
      throw new NullPointerException ( "Body or Row is null is null" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression , ( Expression ) pBodyOrRow } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
    // Check the disjunction
    getIdentifiersBound ( ) ;
    checkDisjunction ( ) ;
  }


  /**
   * Allocates a new {@link Body}.
   * 
   * @param pIdentifiers The attribute {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pBodyOrRow The child {@link BodyOrRow}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Body ( Identifier [ ] pIdentifiers , Expression pExpression ,
      BodyOrRow pBodyOrRow , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pExpression , pBodyOrRow ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ( )
  {
    /*
     * Check the disjunction of the attribute identifiers.
     */
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .getIdentifiersAll ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier idAttribute : this.identifiers )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( idAttribute.equals ( allId ) )
            && ( ! allId.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      /*
       * Throw an exception, if the negative identifier list contains one or
       * more identifiers. If this happens, all Identifiers are added.
       */
      if ( negativeIdentifiers.size ( ) > 0 )
      {
        negativeIdentifiers.clear ( ) ;
        for ( Identifier allId : allIdentifiers )
        {
          if ( idAttribute.equals ( allId ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( idAttribute ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Body clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Body ( newIdentifiers , this.expressions [ 0 ].clone ( ) ,
        ( BodyOrRow ) this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Body )
    {
      Body other = ( Body ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
    }
    return false ;
  }


  /**
   * Returns the sub {@link BodyOrRow}.
   * 
   * @return the sub {@link BodyOrRow}.
   */
  public BodyOrRow getBodyOrRow ( )
  {
    return ( BodyOrRow ) this.expressions [ 1 ] ;
  }


  /**
   * Returns a list of all {@link Attribute} {@link Identifier}s in the domain
   * of this {@link Expression}.
   * 
   * @return A list of all {@link Attribute} {@link Identifier}s in the domain
   *         of this {@link Expression}.
   */
  @ Override
  public ArrayList < Identifier > getDomA ( )
  {
    if ( this.domA == null )
    {
      this.domA = new ArrayList < Identifier > ( ) ;
      for ( Identifier a : this.identifiers )
      {
        this.domA.add ( a ) ;
      }
      this.domA.addAll ( this.expressions [ 0 ].getDomA ( ) ) ;
      this.domA.addAll ( this.expressions [ 1 ].getDomA ( ) ) ;
    }
    return this.domA ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Body" ; //$NON-NLS-1$
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
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> (
          this.identifiers.length + 1 ) ;
      ArrayList < Identifier > boundExpressionBody = new ArrayList < Identifier > ( ) ;
      boundExpressionBody
          .addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
        for ( Identifier freeId : boundExpressionBody )
        {
          if ( this.identifiers [ i ].equals ( freeId ) )
          {
            freeId.setBoundTo ( this , this.identifiers [ i ] ) ;
            freeId.setSet ( Identifier.Set.ATTRIBUTE ) ;
            boundIdList.add ( freeId ) ;
          }
        }
        this.boundIdentifiers.add ( boundIdList ) ;
      }
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
      this.identifiersFree
          .add ( new Identifier ( "self" , Identifier.Set.SELF ) ) ; //$NON-NLS-1$
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      ArrayList < Identifier > freeB = new ArrayList < Identifier > ( ) ;
      freeB.addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      for ( Identifier a : this.identifiers )
      {
        while ( freeB.remove ( a ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      this.identifiersFree.addAll ( freeB ) ;
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
    return this.indicesId ;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_BODY ;
    }
    return this.prefix ;
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
  public Body substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    boolean substituteBody = true ;
    for ( Identifier a : this.identifiers )
    {
      if ( pId.equals ( a ) )
      {
        substituteBody = false ;
        break ;
      }
    }
    /*
     * Perform the substitution.
     */
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    BodyOrRow newBodyOrRow ;
    if ( substituteBody )
    {
      newBodyOrRow = ( BodyOrRow ) this.expressions [ 1 ].substitute ( pId ,
          pExpression ) ;
    }
    else
    {
      newBodyOrRow = ( BodyOrRow ) this.expressions [ 1 ] ;
    }
    return new Body ( this.identifiers , newE , newBodyOrRow ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Body substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    BodyOrRow newBodyOrRow = ( BodyOrRow ) this.expressions [ 1 ]
        .substitute ( pTypeSubstitution ) ;
    return new Body ( this.identifiers , newE , newBodyOrRow ) ;
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
          PRIO_BODY ) ;
      this.prettyStringBuilder.addKeyword ( "inherit" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( i != this.identifiers.length - 1 )
        {
          this.prettyStringBuilder.addText ( ", " ) ; //$NON-NLS-1$
        }
      }
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "from" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_BODY_E ) ;
      this.prettyStringBuilder.addText ( " ; " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_BODY_B ) ;
    }
    return this.prettyStringBuilder ;
  }
}
