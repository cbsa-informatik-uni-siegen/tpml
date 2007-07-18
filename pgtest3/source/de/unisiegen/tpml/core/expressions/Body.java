package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent body expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Body extends Expression implements BoundIdentifiers ,
    DefaultExpressions , SortedChildren
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
   * The list of attribute identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiersAttribute ;


  /**
   * The list of method identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiersMethod ;


  /**
   * The base class name identifier.
   * 
   * @see #getIdentifiers()
   */
  private Identifier identifierSuper ;


  /**
   * Allocates a new {@link Body}.
   * 
   * @param pAttributes The attribute {@link Identifier}s.
   * @param pMethods The method {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pSuper The super {@link Identifier}.
   * @param pBody The child {@link Body}.
   */
  public Body ( Identifier [ ] pAttributes , Identifier [ ] pMethods ,
      Expression pExpression , Identifier pSuper , Expression pBody )
  {
    if ( pAttributes == null )
    {
      throw new NullPointerException ( "Attribute identifiers is null" ) ; //$NON-NLS-1$
    }
    for ( Identifier id : pAttributes )
    {
      if ( id == null )
      {
        throw new NullPointerException ( "One attribute identifier is null" ) ; //$NON-NLS-1$
      }
    }
    if ( pMethods == null )
    {
      throw new NullPointerException ( "Method identifiers is null" ) ; //$NON-NLS-1$
    }
    for ( Identifier id : pMethods )
    {
      if ( id == null )
      {
        throw new NullPointerException ( "One method identifier is null" ) ; //$NON-NLS-1$
      }
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Espression is null" ) ; //$NON-NLS-1$
    }
    if ( pSuper == null )
    {
      throw new NullPointerException ( "Super Identifier is null" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiersAttribute = pAttributes ;
    this.identifiersMethod = pMethods ;
    this.identifierSuper = pSuper ;
    this.identifiers = new Identifier [ this.identifiersAttribute.length
        + this.identifiersMethod.length + 1 ] ;
    this.indicesId = new int [ this.identifiersAttribute.length
        + this.identifiersMethod.length + 1 ] ;
    for ( int i = 0 ; i < this.identifiersAttribute.length ; i ++ )
    {
      this.identifiersAttribute [ i ].setParent ( this ) ;
      this.identifiers [ i ] = this.identifiersAttribute [ i ] ;
      this.indicesId [ i ] = i + 1 ;
    }
    for ( int i = 0 ; i < this.identifiersMethod.length ; i ++ )
    {
      this.identifiersMethod [ i ].setParent ( this ) ;
      this.identifiers [ this.identifiersAttribute.length + i ] = this.identifiersMethod [ i ] ;
      this.indicesId [ this.identifiersAttribute.length + i ] = i + 1 ;
    }
    this.identifierSuper.setParent ( this ) ;
    this.identifiers [ this.identifiers.length - 1 ] = this.identifierSuper ;
    this.indicesId [ this.indicesId.length - 1 ] = - 1 ;
    // Expression
    this.expressions = new Expression [ ]
    { pExpression , pBody } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
    // Check the disjunction
    getIdentifiersBound ( ) ;
    checkDisjunction ( ) ;
  }


  /**
   * Allocates a new {@link Body}.
   * 
   * @param pAttributes The attribute {@link Identifier}s.
   * @param pMethods The method {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pSuper The super {@link Identifier}.
   * @param pBody The child {@link Body}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Body ( Identifier [ ] pAttributes , Identifier [ ] pMethods ,
      Expression pExpression , Identifier pSuper , Expression pBody ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pAttributes , pMethods , pExpression , pSuper , pBody ) ;
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
    for ( Identifier idAttribute : this.identifiersAttribute )
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
    /*
     * Check th disjunction of the super identifier.
     */
    allIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier id : this.identifiersAttribute )
    {
      allIdentifiers.add ( id ) ;
    }
    for ( Identifier id : this.identifiersMethod )
    {
      allIdentifiers.add ( id ) ;
    }
    allIdentifiers.addAll ( this.expressions [ 0 ].getIdentifiersAll ( ) ) ;
    allIdentifiers.addAll ( this.expressions [ 1 ].getIdentifiersAll ( ) ) ;
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifierSuper.equals ( allId ) )
          && ( ! allId.getSet ( ).equals ( Identifier.Set.SUPER ) ) )
      {
        negativeIdentifiers.add ( allId ) ;
      }
    }
    /*
     * Throw an exception, if the negative identifier list contains one or more
     * identifiers. If this happens, all Identifiers are added.
     */
    if ( negativeIdentifiers.size ( ) > 0 )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( this.identifierSuper.equals ( allId ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      negativeIdentifiers.add ( this.identifierSuper ) ;
      LanguageParserMultiException
          .throwExceptionDisjunction ( negativeIdentifiers ) ;
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Body clone ( )
  {
    Identifier [ ] newAttributeIdentifiers = new Identifier [ this.identifiersAttribute.length ] ;
    for ( int i = 0 ; i < newAttributeIdentifiers.length ; i ++ )
    {
      newAttributeIdentifiers [ i ] = this.identifiersAttribute [ i ].clone ( ) ;
    }
    Identifier [ ] newMethodIdentifiers = new Identifier [ this.identifiersMethod.length ] ;
    for ( int i = 0 ; i < newMethodIdentifiers.length ; i ++ )
    {
      newMethodIdentifiers [ i ] = this.identifiersMethod [ i ].clone ( ) ;
    }
    return new Body ( newAttributeIdentifiers , newMethodIdentifiers ,
        this.expressions [ 0 ].clone ( ) , this.identifierSuper.clone ( ) ,
        this.expressions [ 1 ].clone ( ) ) ;
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
      return ( ( Arrays.equals ( this.identifiersAttribute ,
          other.identifiersAttribute ) )
          && ( Arrays
              .equals ( this.identifiersMethod , other.identifiersMethod ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.identifierSuper.equals ( other.identifierSuper ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
    }
    return false ;
  }


  /**
   * Returns the sub {@link Body}.
   * 
   * @return the sub {@link Body}.
   */
  public Expression getBody ( )
  {
    return this.expressions [ 1 ] ;
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
   * Returns the super {@link Identifier}.
   * 
   * @return The super {@link Identifier}.
   */
  public Identifier getIdentifierSuper ( )
  {
    return this.identifierSuper ;
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
   * Returns the {@link Attribute} {@link Identifier}s of this
   * {@link Expression}.
   * 
   * @return The {@link Attribute} {@link Identifier}s of this
   *         {@link Expression}.
   */
  public Identifier [ ] getIdentifiersAttribute ( )
  {
    return this.identifiersAttribute ;
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
      for ( int i = 0 ; i < this.identifiersAttribute.length ; i ++ )
      {
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
        for ( Identifier freeId : boundExpressionBody )
        {
          if ( this.identifiersAttribute [ i ].equals ( freeId ) )
          {
            freeId.setBoundTo ( this , this.identifiersAttribute [ i ] ) ;
            freeId.setSet ( Identifier.Set.ATTRIBUTE ) ;
            boundIdList.add ( freeId ) ;
          }
        }
        this.boundIdentifiers.add ( boundIdList ) ;
      }
      for ( int i = this.identifiersAttribute.length ; i < this.identifiers.length + 1 ; i ++ )
      {
        this.boundIdentifiers.add ( null ) ;
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
      this.identifiersFree.add ( new Identifier ( "self" ) ) ; //$NON-NLS-1$
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      ArrayList < Identifier > freeB = new ArrayList < Identifier > ( ) ;
      freeB.addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      for ( Identifier a : this.identifiersAttribute )
      {
        while ( freeB.remove ( a ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      // Remove {z#m | m in M}
      for ( Identifier m : this.identifiersMethod )
      {
        Identifier inherited = new Identifier (
            this.identifierSuper.getName ( ) , m.getName ( ) ) ;
        while ( freeB.remove ( inherited ) )
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
   * Returns the {@link Method} {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Method} {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiersMethod ( )
  {
    return this.identifiersMethod ;
  }


  /**
   * Returns the {@link Identifier}s and {@link Expression}s in the right
   * sorting.
   * 
   * @return The {@link Identifier}s and {@link Expression}s in the right
   *         sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public ExpressionOrType [ ] getSortedChildren ( )
  {
    ExpressionOrType [ ] result = new ExpressionOrType [ this.identifiers.length + 2 ] ;
    for ( int i = 0 ; i < this.identifiersAttribute.length ; i ++ )
    {
      result [ i ] = this.identifiersAttribute [ i ] ;
    }
    for ( int i = 0 ; i < this.identifiersMethod.length ; i ++ )
    {
      result [ this.identifiersAttribute.length + i ] = this.identifiersMethod [ i ] ;
    }
    result [ result.length - 3 ] = this.expressions [ 0 ] ;
    result [ result.length - 2 ] = this.identifierSuper ;
    result [ result.length - 1 ] = this.expressions [ 1 ] ;
    return result ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiersAttribute.hashCode ( )
        + this.identifiersMethod.hashCode ( ) + this.expressions.hashCode ( )
        + this.identifierSuper.hashCode ( ) ;
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
    for ( Identifier id : this.identifiersAttribute )
    {
      if ( pId.equals ( id ) )
      {
        substituteBody = false ;
        break ;
      }
    }
    if ( substituteBody )
    {
      for ( Identifier m : this.identifiersMethod )
      {
        Identifier inherited = new Identifier (
            this.identifierSuper.getName ( ) , m.getName ( ) ) ;
        if ( pId.equals ( inherited ) )
        {
          substituteBody = false ;
          break ;
        }
      }
    }
    /*
     * Perform the substitution.
     */
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newBody ;
    if ( substituteBody )
    {
      newBody = this.expressions [ 1 ].substitute ( pId , pExpression ) ;
    }
    else
    {
      newBody = this.expressions [ 1 ] ;
    }
    return new Body ( this.identifiersAttribute , this.identifiersMethod ,
        newE , this.identifierSuper , newBody ) ;
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
    Expression newBody = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new Body ( this.identifiersAttribute , this.identifiersMethod ,
        newE , this.identifierSuper , newBody ) ;
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
          0 ) ;
      this.prettyStringBuilder.addKeyword ( "inherit" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.identifiersAttribute.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiersAttribute [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( i != this.identifiersAttribute.length - 1 )
        {
          this.prettyStringBuilder.addText ( ", " ) ; //$NON-NLS-1$
        }
      }
      this.prettyStringBuilder.addText ( " ; " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      for ( int i = 0 ; i < this.identifiersMethod.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiersMethod [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( i != this.identifiersMethod.length - 1 )
        {
          this.prettyStringBuilder.addText ( ", " ) ; //$NON-NLS-1$
        }
      }
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "from" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "as" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifierSuper
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      this.prettyStringBuilder.addText ( " ; " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
