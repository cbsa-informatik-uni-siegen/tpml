package de.unisiegen.tpml.core.expressions ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.exceptions.CheckDisjunctionException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Represents a multi lambda abstract, which takes a single tuple argument as
 * parameter and splits the tuple items to various identifiers.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Lambda
 * @see MultiLet
 * @see Value
 */
public final class MultiLambda extends Value implements BoundIdentifiers ,
    DefaultTypes , ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


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
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   * @see #getTypes(int)
   */
  private MonoType [ ] types ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>MultiLambda</code> expression with the specified
   * <code>identifiers</code> and the function body <code>e</code>.
   * 
   * @param pIdentifiers non-empty set of identifiers.
   * @param pTau the type of the identifiers or <code>null</code>.
   * @param pExpression the function body.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code> or
   *           <code>e</code> is <code>null</code>.
   */
  public MultiLambda ( Identifier [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length == 0 )
    {
      throw new IllegalArgumentException ( "identifiers is empty" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "e is null" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].getParent ( ) != null )
      {
        // this.identifiers [ i ] = this.identifiers [ i ].clone ( ) ;
      }
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ] != null )
    {
      if ( this.types [ 0 ].getParent ( ) != null )
      {
        // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
      }
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ 1 ] ;
    this.expressions [ 0 ] = pExpression ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      // this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    checkDisjunction ( ) ;
  }


  /**
   * TODO
   */
  public void checkDisjunction ( )
  {
    ArrayList < Identifier > allIdentifiers = this.expressions [ 0 ]
        .allIdentifiers ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier current : this.identifiers )
    {
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( current.equals ( allId ) )
            && ( allId.getIdentifierSet ( )
                .equals ( Identifier.IdentifierSet.A ) ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      CheckDisjunctionException.throwException ( current , negativeIdentifiers ,
          MessageFormat.format ( Messages.getString ( "Parser.3" ) , current ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLambda clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new MultiLambda ( newIdentifiers , this.types [ 0 ] == null ? null
        : this.types [ 0 ].clone ( ) , this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof MultiLambda )
    {
      MultiLambda other = ( MultiLambda ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.addAll ( this.expressions [ 0 ].free ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        while ( this.free.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
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
    return "Multi-Lambda" ; //$NON-NLS-1$
  }


  /**
   * Returns the function body expression.
   * 
   * @return the function body expression.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
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
    return INDICES_E ;
  }


  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   * @see #getIdentifiers()
   */
  public Identifier [ ] getIdentifiers ( )
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
  public Identifier getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
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
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundedE = this.expressions [ 0 ].free ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        /*
         * An Identifier has no binding, if an Identifier after him has the same
         * name. Example: (λ(x, x).x) (1, 2).
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
        ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
        if ( hasBinding )
        {
          for ( Identifier freeId : boundedE )
          {
            if ( this.identifiers [ i ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this , this.identifiers [ i ] ) ;
              boundedIdList.add ( freeId ) ;
            }
          }
        }
        this.boundedIdentifiers.add ( boundedIdList ) ;
      }
    }
    return this.boundedIdentifiers ;
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
   * Returns the type of the <code>identifiers</code> or <code>null</code>
   * if no type was specified.
   * 
   * @return the type of the identifiers;
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the types for the <code>identifiers</code>.
   * 
   * @return the types.
   * @see #getTypes(int)
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the <code>n</code>th type.
   * 
   * @param pIndex the index of the type.
   * @return the <code>n</code>th type.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getTypes()
   */
  public MonoType getTypes ( int pIndex )
  {
    return this.types [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public String [ ] getTypesPrefix ( )
  {
    String [ ] result = new String [ 1 ] ;
    result [ 0 ] = PREFIX_TAU ;
    return result ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( )
        + ( ( this.types [ 0 ] == null ) ? 0 : this.types [ 0 ].hashCode ( ) )
        + this.expressions [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public MultiLambda substitute ( Identifier pId , Expression pExpression )
  {
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this ;
      }
    }
    Expression newE = this.expressions [ 0 ] ;
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ] ;
    }
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.free ( ) ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
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
      Identifier newId = boundRenaming.newId ( newIdentifiers [ i ] ) ;
      /*
       * Search for an Identifier before the current Identifier with the same
       * name. For example: "let a = b in lambda (b,b).a".
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
        newE = newE.substitute ( newIdentifiers [ i ] , newId ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression ) ;
    return new MultiLambda ( newIdentifiers , this.types [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public MultiLambda substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    return new MultiLambda ( this.identifiers , newTau , this.expressions [ 0 ]
        .substitute ( pTypeSubstitution ) ) ;
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
          PRIO_LAMBDA ) ;
      this.prettyStringBuilder.addKeyword ( "\u03bb" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.identifiers.length ; ++ i )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( ", " ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      }
      this.prettyStringBuilder.addText ( ")" ) ; //$NON-NLS-1$
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LAMBDA_TAU ) ;
      }
      this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LAMBDA_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
