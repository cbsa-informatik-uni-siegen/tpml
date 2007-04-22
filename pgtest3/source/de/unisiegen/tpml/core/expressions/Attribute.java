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
public final class Attribute extends Expression implements BoundIdentifiers ,
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
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
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
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public Attribute ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
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
  public Attribute clone ( )
  {
    return new Attribute ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Attribute )
    {
      Attribute other = ( Attribute ) pObject ;
      return ( ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
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
    return "Attribute" ; //$NON-NLS-1$
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
      ArrayList < Identifier > boundIdList = ( ( Row ) this.parent )
          .getIdentifiersBound ( this ) ;
      // Set the bound Identifier to an Attribute-Identifier
      for ( Identifier boundId : boundIdList )
      {
        boundId.setIdentifierSet ( Identifier.IdentifierSet.A ) ;
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
  public Attribute substitute ( Identifier pId , Expression pExpression )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Attribute ( this.identifiers [ 0 ] , this.types [ 0 ] , newE ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Attribute substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Attribute ( this.identifiers [ 0 ] , newTau , newE ) ;
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
          PRIO_ATTRIBUTE ) ;
      this.prettyStringBuilder.addKeyword ( "val" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_ATTRIBUTE_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ATTRIBUTE_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
