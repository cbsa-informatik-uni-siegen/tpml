package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
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
public final class MultiLambda extends Value
{
  /**
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private String [ ] identifiers ;


  /**
   * The type of the <code>identifiers</code> or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * The function body expression.
   * 
   * @see #getE()
   */
  private Expression e ;


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
  public MultiLambda ( String [ ] pIdentifiers , MonoType pTau ,
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
    this.identifiers = pIdentifiers ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLambda clone ( )
  {
    return new MultiLambda ( this.identifiers , this.tau , this.e.clone ( ) ) ;
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
          && ( this.e.equals ( other.e ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.addAll ( this.e.free ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        this.free.remove ( this.identifiers [ i ] ) ;
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
    return this.e ;
  }


  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   * @see #getIdentifiers()
   */
  public String [ ] getIdentifiers ( )
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
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * Returns the type of the <code>identifiers</code> or <code>null</code>
   * if no type was specified.
   * 
   * @return the type of the identifiers;
   */
  public MonoType getTau ( )
  {
    return this.tau ;
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
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 )
        + this.e.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public MultiLambda substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public MultiLambda substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this.clone ( ) ;
      }
    }
    Expression newE = this.e.clone ( ) ;
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    if ( this.e.free ( ).contains ( pId ) )
    {
      for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
      {
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.free ( ) ) ;
        boundRenaming.add ( pExpression.free ( ) ) ;
        boundRenaming.add ( pId ) ;
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
        String newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] ) ;
        for ( int j = 0 ; j < i ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ] ;
          }
        }
        if ( ! newIdentifiers [ i ].equals ( newId ) )
        {
          newE = newE.substitute ( newIdentifiers [ i ] , new Identifier (
              newId ) , pAttributeRename ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
      newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    return new MultiLambda ( newIdentifiers , this.tau , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public MultiLambda substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new MultiLambda ( this.identifiers , newTau , this.e
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
        this.prettyStringBuilder.addIdentifier ( this.identifiers [ i ] ) ;
      }
      this.prettyStringBuilder.addText ( ")" ) ; //$NON-NLS-1$
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LAMBDA_TAU ) ;
      }
      this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LAMBDA_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
