package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
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
public final class ArrowType extends MonoType
{
  /**
   * The type for <code>bool -&gt; bool</code>.
   */
  public static final ArrowType BOOL_BOOL = new ArrowType ( BooleanType.BOOL ,
      BooleanType.BOOL ) ;


  /**
   * The type for <code>int -&gt; int -&gt; bool</code>.
   */
  public static final ArrowType INT_INT_BOOL = new ArrowType ( IntegerType.INT ,
      new ArrowType ( IntegerType.INT , BooleanType.BOOL ) ) ;


  /**
   * The type for <code>int -&gt; int -&gt; int</code>.
   */
  public static final ArrowType INT_INT_INT = new ArrowType ( IntegerType.INT ,
      new ArrowType ( IntegerType.INT , IntegerType.INT ) ) ;


  /**
   * The parameter type <code>tau1</code>.
   * 
   * @see #getTau1()
   */
  private MonoType tau1 ;


  /**
   * The result type <code>tau2</code>.
   * 
   * @see #getTau2()
   */
  private MonoType tau2 ;


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
    this.tau1 = pTau1 ;
    this.tau2 = pTau2 ;
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
      ArrowType type = ( ArrowType ) pObject ;
      return ( this.tau1.equals ( type.tau1 ) && this.tau2.equals ( type.tau2 ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#free()
   */
  @ Override
  public TreeSet < TypeVariable > free ( )
  {
    TreeSet < TypeVariable > free = new TreeSet < TypeVariable > ( ) ;
    free.addAll ( this.tau1.free ( ) ) ;
    free.addAll ( this.tau2.free ( ) ) ;
    return free ;
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
    return this.tau1 ;
  }


  /**
   * Returns the result type <code>tau2</code> for this arrow type.
   * 
   * @return the result type <code>tau2</code>.
   * @see #getTau1()
   */
  public MonoType getTau2 ( )
  {
    return this.tau2 ;
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
    return this.tau1.hashCode ( ) + this.tau2.hashCode ( ) ;
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
    return new ArrowType ( this.tau1.substitute ( pTypeSubstitution ) ,
        this.tau2.substitute ( pTypeSubstitution ) ) ;
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
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_ARROW ) ;
    builder.addBuilder ( this.tau1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_ARROW_TAU1 ) ;
    builder.addText ( " \u2192 " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.tau2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_ARROW_TAU2 ) ;
    return builder ;
  }
}
