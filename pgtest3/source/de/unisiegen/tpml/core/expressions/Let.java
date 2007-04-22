package de.unisiegen.tpml.core.expressions ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
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
 * Represents the simple binding mechanism <code>let</code>. The string
 * representation is <code>let id = e1 in e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 * @see Lambda
 */
public class Let extends Expression implements BoundIdentifiers , DefaultTypes ,
    ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , 2 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private static final int [ ] INDICES_ID = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


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
   * @see #getTypes(int)
   */
  protected MonoType [ ] types ;


  /**
   * The first and second expression.
   */
  protected Expression [ ] expressions ;


  /**
   * Allocates a new <code>Let</code> with the specified <code>id</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pIdentifier the name of the identifier.
   * @param pTau the type for the <code>id</code> (and thereby for
   *          <code>e1</code>) or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Let ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression1 , Expression pExpression2 )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "id is null" ) ; //$NON-NLS-1$
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
    this.identifiers = new Identifier [ 1 ] ;
    this.identifiers [ 0 ] = pIdentifier ;
    if ( this.identifiers [ 0 ].getParent ( ) != null )
    {
      // this.identifiers [ 0 ] = this.identifiers [ 0 ].clone ( ) ;
    }
    this.identifiers [ 0 ].setParent ( this ) ;
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
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .allIdentifiers ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifiers [ 0 ].equals ( allId ) )
          && ( allId.getIdentifierSet ( ).equals ( Identifier.IdentifierSet.A ) ) )
      {
        negativeIdentifiers.add ( allId ) ;
      }
    }
    CheckDisjunctionException.throwException ( this.identifiers [ 0 ] ,
        negativeIdentifiers , MessageFormat.format ( Messages
            .getString ( "Parser.3" ) , this.identifiers [ 0 ] ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Let clone ( )
  {
    return new Let ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) , this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof Let )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      Let other = ( Let ) pObject ;
      return ( ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.expressions [ 1 ].equals ( other.expressions [ 1 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ;
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
      this.free.addAll ( this.expressions [ 1 ].free ( ) ) ;
      while ( this.free.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
      this.free.addAll ( this.expressions [ 0 ].free ( ) ) ;
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Let" ; //$NON-NLS-1$
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
   * Returns the identifier of the <code>Let</code> expression.
   * 
   * @return the identifier of the <code>Let</code> expression.
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
   * Returns the <code>pIndex</code>th {@link Identifier} of this
   * {@link Expression}.
   * 
   * @param pIndex The index of the {@link Identifier} to return.
   * @return The <code>pIndex</code>th {@link Identifier} of this
   *         {@link Expression}.
   */
  public Identifier getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
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
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ].free ( ) ;
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
    return this.boundIdentifiers ;
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
   */
  public String [ ] getIdentifiersPrefix ( )
  {
    String [ ] result = new String [ 1 ] ;
    result [ 0 ] = PREFIX_ID ;
    return result ;
  }


  /**
   * Returns the type for the identifier (and thereby the type for
   * <code>e1</code>) or <code>null</code> if no type was specified by the
   * user or the translation to core syntax.
   * 
   * @return the type for <code>id</code> or <code>null</code>.
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
    return this.identifiers [ 0 ].hashCode ( )
        + ( ( this.types [ 0 ] == null ) ? 0 : this.types [ 0 ].hashCode ( ) )
        + this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Let substitute ( Identifier pId , Expression pExpression )
  {
    /*
     * Perform the substitution in e1.
     */
    Expression newE1 = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newE2 = this.expressions [ 1 ] ;
    /*
     * Do not substitute in e2 , if the Identifiers are equal.
     */
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return new Let ( this.identifiers [ 0 ] , this.types [ 0 ] , newE1 ,
          newE2 ) ;
    }
    /*
     * Perform the bound renaming if required.
     */
    ArrayList < Identifier > freeE2 = newE2.free ( ) ;
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( freeE2 ) ;
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
    }
    /*
     * Perform the substitution in e2.
     */
    newE2 = newE2.substitute ( pId , pExpression ) ;
    return new Let ( newId , this.types [ 0 ] , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Let substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new Let ( this.identifiers [ 0 ] , newTau , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
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
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
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
