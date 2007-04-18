package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class ObjectExpr extends Expression implements BoundIdentifiers ,
    DefaultTypes , ChildrenExpressions
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
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
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
  private SelfIdentifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   * @see #getTypes(int)
   */
  private MonoType [ ] types ;


  /**
   * TODO
   * 
   * @param pSelfIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public ObjectExpr ( SelfIdentifier pSelfIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Row is null" ) ; //$NON-NLS-1$
    }
    if ( ! ( pExpression instanceof Row ) )
    {
      throw new IllegalArgumentException ( "The Expression has to be a Row" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = new SelfIdentifier [ 1 ] ;
    this.identifiers [ 0 ] = pSelfIdentifier ;
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
    this.expressions = new Expression [ 1 ] ;
    this.expressions [ 0 ] = pExpression ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      // this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
      return ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
              : ( this.types [ 0 ].equals ( other.types [ 0 ] ) )
                  && ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) ) ) ;
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
      this.free.addAll ( this.expressions [ 0 ].free ( ) ) ;
      while ( this.free.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
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
    return "Object" ; //$NON-NLS-1$
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
   * TODO
   * 
   * @return TODO
   */
  public SelfIdentifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public SelfIdentifier [ ] getIdentifiers ( )
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
  public SelfIdentifier getIdentifiers ( int pIndex )
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
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundedE = this.expressions [ 0 ].free ( ) ;
      for ( Identifier freeId : boundedE )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
          boundedIdList.add ( freeId ) ;
        }
      }
      this.boundedIdentifiers.add ( boundedIdList ) ;
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
   * TODO
   * 
   * @return TODO
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
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( )
        + ( this.types [ 0 ] == null ? 0 : this.types [ 0 ].hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expressions [ 0 ].isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr substitute ( Identifier pId , Expression pExpression )
  {
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
    return new ObjectExpr ( this.identifiers [ 0 ] , this.types [ 0 ] , newE ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public ObjectExpr substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new ObjectExpr ( this.identifiers [ 0 ] , newTau , newE ) ;
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
          PRIO_OBJECTEXPR ) ;
      this.prettyStringBuilder.addKeyword ( "object" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " (" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_OBJECTEXPR_TAU ) ;
      }
      this.prettyStringBuilder.addText ( ") " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECTEXPR_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "end" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
