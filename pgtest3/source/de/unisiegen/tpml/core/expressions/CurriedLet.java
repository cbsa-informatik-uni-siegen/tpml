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
 * Instances of this class represents curried let expressions, which are
 * syntactic sugar for {@link Let} and {@link Lambda}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1080 $
 * @see Let
 * @see Lambda
 */
public class CurriedLet extends Expression implements BoundIdentifiers ,
    DefaultTypes , ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , 2 } ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  protected MonoType [ ] types ;


  /**
   * The expressions.
   */
  protected Expression [ ] expressions ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  protected int [ ] indicesId ;


  /**
   * Indeces of the child {@link Type}s.
   */
  protected int [ ] indicesType ;


  /**
   * Allocates a new <code>CurriedLet</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link #getTypes()} for an extensive description of the meaning of
   *          <code>types</code>.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> doesn't
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public CurriedLet ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( "types is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length < 2 )
    {
      throw new IllegalArgumentException (
          "identifiers must contain at least two items" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException (
          "the arity of identifiers and types must match" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    this.indicesId [ 0 ] = - 1 ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      this.indicesId [ i ] = i ;
    }
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].getParent ( ) != null )
      {
        // this.identifiers [ i ] = this.identifiers [ i ].clone ( ) ;
      }
      this.identifiers [ i ].setParent ( this ) ;
    }
    // Type
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    this.indicesType [ 0 ] = - 1 ;
    for ( int i = 1 ; i < this.types.length ; i ++ )
    {
      this.indicesType [ i ] = i ;
    }
    for ( int i = 0 ; i < this.types.length ; i ++ )
    {
      if ( this.types [ i ] != null )
      {
        if ( this.types [ i ].getParent ( ) != null )
        {
          // this.types [ i ] = this.types [ i ].clone ( ) ;
        }
        this.types [ i ].setParent ( this ) ;
      }
    }
    // Expression
    this.expressions = new Expression [ 2 ] ;
    this.expressions [ 0 ] = pExpression1 ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      // this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ] = pExpression2 ;
    if ( this.expressions [ 1 ].getParent ( ) != null )
    {
      // this.expressions [ 1 ] = this.expressions [ 1 ].clone ( ) ;
    }
    this.expressions [ 1 ].setParent ( this ) ;
    checkDisjunction ( ) ;
  }


  /**
   * TODO
   */
  public void checkDisjunction ( )
  {
    // Identifier 0
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .allIdentifiers ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifiers [ 0 ].equals ( allId ) )
          && ( allId.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) ) )
      {
        negativeIdentifiers.add ( allId ) ;
      }
    }
    CheckDisjunctionException.throwException ( this.identifiers [ 0 ] ,
        negativeIdentifiers , MessageFormat.format ( Messages
            .getString ( "Parser.3" ) , this.identifiers [ 0 ] ) ) ; //$NON-NLS-1$
    // Identifier 1-n
    allIdentifiers = this.expressions [ 0 ].allIdentifiers ( ) ;
    negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( this.identifiers [ i ].equals ( allId ) )
            && ( allId.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      CheckDisjunctionException.throwException ( this.identifiers [ i ] ,
          negativeIdentifiers , MessageFormat.format ( Messages
              .getString ( "Parser.3" ) , this.identifiers [ i ] ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public CurriedLet clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedLet ( newIdentifiers , newTypes , this.expressions [ 0 ]
        .clone ( ) , this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof CurriedLet )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      CurriedLet other = ( CurriedLet ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( Arrays.equals ( this.types , other.types ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
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
      ArrayList < Identifier > freeE1 = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > freeE2 = new ArrayList < Identifier > ( ) ;
      freeE1.addAll ( this.expressions [ 0 ].free ( ) ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        while ( freeE1.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      freeE2.addAll ( this.expressions [ 1 ].free ( ) ) ;
      while ( freeE2.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
      this.free.addAll ( freeE1 ) ;
      this.free.addAll ( freeE2 ) ;
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Let" ; //$NON-NLS-1$
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
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
  }


  /**
   * Returns the identifiers, where the first identifier is the name of the
   * function and the remaining identifiers name the parameters of the
   * functions, in a curried fashion.
   * 
   * @return the identifiers.
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
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundE1 = this.expressions [ 0 ].free ( ) ;
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ].free ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        if ( i == 0 )
        {
          ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
          for ( Identifier freeId : boundE2 )
          {
            if ( this.identifiers [ 0 ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
              boundIdList.add ( freeId ) ;
            }
          }
          this.boundIdentifiers.add ( boundIdList ) ;
        }
        else
        {
          /*
           * An Identifier has no binding, if an Identifier after him has the
           * same name. Example: let f x x = x + 1 in f 1 2.
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
            for ( Identifier freeId : boundE1 )
            {
              if ( this.identifiers [ i ].equals ( freeId ) )
              {
                boundIdList.add ( freeId ) ;
              }
            }
            for ( Identifier boundId : boundIdList )
            {
              boundId.setBoundTo ( this , this.identifiers [ i ] ) ;
            }
          }
          this.boundIdentifiers.add ( boundIdList ) ;
        }
      }
    }
    return this.boundIdentifiers ;
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
   * Returns the types for the <code>identifiers</code>.
   * <code>let id (id1:tau1)...(idn:taun): tau = e1 in e2</code> is translated
   * to <code>let id = lambda id1:tau1...lambda idn:taun.e1 in e2</code> which
   * means <code>types[0]</code> is used for <code>identifiers[0]</code> and
   * so on (where <code>types[0]</code> corresponds to <code>tau</code> in
   * the example above, and <code>identifiers[0]</code> to <code>id</code>).
   * Any of the types may be null, in which case the type will be inferred in
   * the type checker, while the <code>types</code> itself may not be
   * <code>null</code>. For recursion (see {@link CurriedLetRec}) the
   * <code>tau</code> is used to build the type of the recursive identifier.
   * 
   * @return the types.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( )
  {
    return this.indicesType ;
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
        + ( ( this.types == null ) ? 0 : this.types.hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public CurriedLet substitute ( Identifier pId , Expression pExpression )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ] ;
    }
    Expression newE1 = this.expressions [ 0 ] ;
    Expression newE2 = this.expressions [ 1 ] ;
    boolean found = false ;
    /*
     * Do not substitute in e1, if the Identifiers are equal.
     */
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        found = true ;
        break ;
      }
    }
    if ( ! found )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.expressions [ 0 ].free ( ) ) ;
        boundRenaming.remove ( newIdentifiers [ i ] ) ;
        boundRenaming.add ( pExpression.free ( ) ) ;
        boundRenaming.add ( pId ) ;
        /*
         * The new Identifier should not be equal to an other Identifier.
         */
        if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
        {
          for ( int j = 1 ; j < newIdentifiers.length ; j ++ )
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
         * name. For example: "let a = b in let f b b = b a in f".
         */
        for ( int j = 1 ; j < i ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ] ;
          }
        }
        /*
         * Substitute the old Identifier only with the new Identifier, if they
         * are different.
         */
        if ( ! newIdentifiers [ i ].equals ( newId ) )
        {
          newE1 = newE1.substitute ( newIdentifiers [ i ] , newId ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
      /*
       * Perform the substitution in e1.
       */
      newE1 = newE1.substitute ( pId , pExpression ) ;
    }
    if ( ! ( this.identifiers [ 0 ].equals ( pId ) ) )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.expressions [ 1 ].free ( ) ) ;
      boundRenaming.remove ( this.identifiers [ 0 ] ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
      boundRenaming.add ( pId ) ;
      Identifier newId = boundRenaming.newId ( this.identifiers [ 0 ] ) ;
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( ! this.identifiers [ 0 ].equals ( newId ) )
      {
        newE2 = newE2.substitute ( this.identifiers [ 0 ] , newId ) ;
        newIdentifiers [ 0 ] = newId ;
      }
      /*
       * Perform the substitution in e2.
       */
      newE2 = newE2.substitute ( pId , pExpression ) ;
    }
    return new CurriedLet ( newIdentifiers , this.types , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public CurriedLet substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new CurriedLet ( this.identifiers , newTypes , newE1 , newE2 ) ;
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
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBuilder ( this.types [ i ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
          this.prettyStringBuilder.addText ( ")" ) ; //$NON-NLS-1$
        }
      }
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
