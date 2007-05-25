package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents function types (so called <i>arrow types</i>) in our
 * type systems. Arrow types are composed of two types, the parameter type
 * <code>tau1</code> and the result type <code>tau2</code>, both
 * monomorphic types.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see BooleanType
 * @see IntegerType
 * @see MonoType
 */
public final class ArrowType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { 1 , 2 } ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new <code>ArrowType</code> with the types <code>tau1</code>
   * and <code>tau2</code>, which represents the type
   * <code>tau1 -&gt; tau2</code>.
   * 
   * @param pTau1 the parameter type.
   * @param pTau2 the result type.
   * @throws NullPointerException if either <code>pTau1</code> or
   *           <code>pTau2</code> are <code>null</code>.
   */
  public ArrowType ( MonoType pTau1 , MonoType pTau2 )
  {
    if ( pTau1 == null )
    {
      throw new NullPointerException ( "Tau1 is null" ) ; //$NON-NLS-1$
    }
    if ( pTau2 == null )
    {
      throw new NullPointerException ( "Tau2 is null" ) ; //$NON-NLS-1$
    }
    this.types = new MonoType [ 2 ] ;
    this.types [ 0 ] = pTau1 ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
    this.types [ 1 ] = pTau2 ;
    if ( this.types [ 1 ].getParent ( ) != null )
    {
      // this.types [ 1 ] = this.types [ 1 ].clone ( ) ;
    }
    this.types [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>ArrowType</code> with the types <code>tau1</code>
   * and <code>tau2</code>, which represents the type
   * <code>tau1 -&gt; tau2</code>.
   * 
   * @param pTau1 the parameter type.
   * @param pTau2 the result type.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws NullPointerException if either <code>pTau1</code> or
   *           <code>pTau2</code> are <code>null</code>.
   */
  public ArrowType ( MonoType pTau1 , MonoType pTau2 , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTau1 , pTau2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ArrowType clone ( )
  {
    return new ArrowType ( this.types [ 0 ].clone ( ) , this.types [ 1 ]
        .clone ( ) ) ;
  }


  /**
   * Compares this arrow type to the <code>obj</code>. Returns
   * <code>true</code> if the <code>obj</code> is an <code>ArrowType</code>
   * with the same parameter and result types as this instance.
   * 
   * @param pObject another object.
   * @return <code>true</code> if this instance is equal to the specified
   *         <code>obj</code>.
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ArrowType )
    {
      ArrowType other = ( ArrowType ) pObject ;
      return ( this.types [ 0 ].equals ( other.types [ 0 ] ) && this.types [ 1 ]
          .equals ( other.types [ 1 ] ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Arrow-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the parameter type <code>tau1</code> for this arrow type.
   * 
   * @return the parameter type <code>tau1</code>.
   * @see #getTau2()
   */
  public MonoType getTau1 ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the result type <code>tau2</code> for this arrow type.
   * 
   * @return the result type <code>tau2</code>.
   * @see #getTau1()
   */
  public MonoType getTau2 ( )
  {
    return this.types [ 1 ] ;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
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
   * @see Type#getTypeVariablesFree()
   */
  @ Override
  public TreeSet < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.types [ 0 ].getTypeVariablesFree ( ) ) ;
      this.free.addAll ( this.types [ 1 ].getTypeVariablesFree ( ) ) ;
    }
    return this.free ;
  }


  /**
   * Returns a hash value for this arrow type, which is based on the hash values
   * for the parameter and result types.
   * 
   * @return a hash value for this arrow type.
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.types [ 0 ].hashCode ( ) + this.types [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public ArrowType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    return new ArrowType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ,
        this.types [ 1 ].substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_ARROW ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ARROW_TAU1 ) ;
      this.prettyStringBuilder.addText ( " \u2192 " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.types [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ARROW_TAU2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
