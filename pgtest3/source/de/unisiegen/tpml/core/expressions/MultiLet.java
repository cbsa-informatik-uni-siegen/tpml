package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


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
public final class MultiLet extends Expression implements BoundIdentifiers ,
    DefaultTypes , DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , 2 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * The bound identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  private MonoType [ ] types ;


  /**
   * The first and second expression.
   */
  private Expression [ ] expressions ;


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
  public MultiLet ( Identifier [ ] pIdentifiers , MonoType pTau ,
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
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = new MonoType [ ]
    { pTau } ;
    if ( this.types [ 0 ] != null )
    {
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression1 , pExpression2 } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
    checkDisjunction ( ) ;
  }


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
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public MultiLet ( Identifier [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression1 , Expression pExpression2 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pTau , pExpression1 , pExpression2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ( )
  {
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .getIdentifiersAll ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier current : this.identifiers )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( current.equals ( allId ) )
            && ( ! ( ( allId.getSet ( ).equals ( Identifier.Set.VARIABLE ) || ( allId
                .getSet ( ).equals ( Identifier.Set.METHOD ) ) ) ) ) )
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
          if ( current.equals ( allId ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( current ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLet clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new MultiLet ( newIdentifiers , this.types [ 0 ] == null ? null
        : this.types [ 0 ].clone ( ) , this.expressions [ 0 ].clone ( ) ,
        this.expressions [ 1 ].clone ( ) ) ;
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
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.expressions [ 1 ].equals ( other.expressions [ 1 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ) ;
    }
    return false ;
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
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ( )
  {
    return this.expressions [ 1 ] ;
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
   * Returns a list of lists of in this {@link Expression} bound
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bound
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ]
          .getIdentifiersFree ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        /*
         * An Identifier has no binding, if an Identifier after him has the same
         * name. Example: let(x, x) = (1, 2) in x.
         */
        boolean hasBinding = true ;
        for ( int j = i + 1 ; j < this.identifiers.length ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            hasBinding = false ;
            break ;
          }
        }
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
        if ( hasBinding )
        {
          for ( Identifier freeId : boundE2 )
          {
            if ( this.identifiers [ i ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this , this.identifiers [ i ] ) ;
              boundIdList.add ( freeId ) ;
            }
          }
        }
        this.boundIdentifiers.add ( boundIdList ) ;
      }
    }
    return this.boundIdentifiers ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 1 ]
          .getIdentifiersFree ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        while ( this.identifiersFree.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
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
   * Returns the tuple type for <code>e1</code> or <code>null</code>.
   * 
   * @return the type for <code>e1</code> or <code>null</code>.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the types for the <code>identifiers</code>.
   * 
   * @return the types.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 1 ].hashCode ( )
        + ( ( this.types [ 0 ] == null ) ? 0 : this.types [ 0 ].hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public MultiLet substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        Expression newE1 = this.expressions [ 0 ].substitute ( pId ,
            pExpression ) ;
        Expression newE2 = this.expressions [ 1 ] ;
        return new MultiLet ( this.identifiers , this.types [ 0 ] , newE1 ,
            newE2 ) ;
      }
    }
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ] ;
    }
    Expression newE2 = this.expressions [ 1 ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
      boundRenaming.add ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      boundRenaming.remove ( newIdentifiers [ i ] ) ;
      boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( pId ) ;
      /*
       * The new Identifier should not be equal to an other Identifier.
       */
      if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
      {
        for ( int j = 0 ; j < newIdentifiers.length ; j ++ )
        {
          if ( i != j )
          {
            boundRenaming.add ( newIdentifiers [ j ] ) ;
          }
        }
      }
      Identifier newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] ) ;
      /*
       * Search for an Identifier before the current Identifier with the same
       * name. For example: "let a = b in let(b, b) = (1, 2) in b a".
       */
      for ( int j = 0 ; j < i ; j ++ )
      {
        if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
        {
          newId = newIdentifiers [ j ] ;
        }
      }
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( ! newIdentifiers [ i ].equals ( newId ) )
      {
        newE2 = newE2.substitute ( newIdentifiers [ i ] , newId ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    /*
     * Perform the substitution.
     */
    Expression newE1 = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    newE2 = newE2.substitute ( pId , pExpression ) ;
    return new MultiLet ( newIdentifiers , this.types [ 0 ] , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public MultiLet substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new MultiLet ( this.identifiers , newTau , newE1 , newE2 ) ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( "(" ) ;//$NON-NLS-1$
      for ( int i = 0 ; i < this.identifiers.length ; ++ i )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( ", " ) ;//$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      }
      this.prettyStringBuilder.addText ( ")" ) ;//$NON-NLS-1$
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ;//$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_CONSTANT ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
