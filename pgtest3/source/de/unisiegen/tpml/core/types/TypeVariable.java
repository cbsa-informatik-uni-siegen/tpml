package de.unisiegen.tpml.core.types ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents type variables in our type system. Type variables can
 * either be specified in the source code by the user, in which case the
 * <code>index</code> assigned to this variable is <code>0</code>, or
 * automatically generated by the type inference algorithm, in which case the
 * index will be a positive integer. The <code>offset</code> is the number of
 * the variable at the specified <code>index</code>. I.e. the first variable
 * specified by the user will have <code>0</code> for <code>index</code> and
 * <code>offset</code>, the second will have an <code>index</code> of
 * <code>0</code> and an <code>offset</code> of <code>1</code> and so on.
 * The <code>offset</code> determines the greek letter assigned to this
 * variable, i.e. α for <code>0</code>, β for <code>1</code> and so on.
 * Since the greek alphabet does only include 24 letters, an <code>offset</code>
 * greater than 23 results in a greek letter with the <code>index</code> plus
 * a number of ticks appended. I.e. α1' if <code>offset</code> is
 * <code>24</code> and <code>index</code> is <code>1</code>. If the
 * <code>index</code> is <code>0</code> only the greek letter determined
 * from the <code>offset</code> will be printed. Otherwise, for positive
 * indices, the greek letter plus the index will be printed.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:538 $
 * @see Type
 */
public final class TypeVariable extends MonoType implements
    Comparable < TypeVariable >
{
  /**
   * The type variable alpha.
   */
  public static final TypeVariable ALPHA = new TypeVariable ( 0 , 0 ) ;


  /**
   * Returns the greek letter that is assigned to the specified
   * <code>offset</code>.
   * 
   * @param pOffset the offset to translate to a greek letter.
   * @return the greek letter that should be used to represent the specified
   *         <code>offset</code>.
   * @throws IllegalArgumentException if <code>offset</code> is larger than
   *           <code>23</code> or negative.
   */
  private static char offsetToGreekLetter ( int pOffset )
  {
    switch ( pOffset )
    {
      case 0 :
        return 'α' ;
      case 1 :
        return 'β' ;
      case 2 :
        return 'γ' ;
      case 3 :
        return 'δ' ;
      case 4 :
        return 'ε' ;
      case 5 :
        return 'ζ' ;
      case 6 :
        return 'η' ;
      case 7 :
        return 'θ' ;
      case 8 :
        return 'ι' ;
      case 9 :
        return 'κ' ;
      case 10 :
        return 'λ' ;
      case 11 :
        return 'μ' ;
      case 12 :
        return 'ν' ;
      case 13 :
        return 'ξ' ;
      case 14 :
        return 'ο' ;
      case 15 :
        return 'π' ;
      case 16 :
        return 'ρ' ;
      case 17 :
        return 'σ' ;
      case 18 :
        return 'τ' ;
      case 19 :
        return 'υ' ;
      case 20 :
        return 'φ' ;
      case 21 :
        return 'χ' ;
      case 22 :
        return 'ψ' ;
      case 23 :
        return 'ω' ;
      default :
        throw new IllegalArgumentException ( "Offset is invalid" ) ; //$NON-NLS-1$
    }
  }


  /**
   * The index of the variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number for
   * variables generated by the type inference algorithm. See the description
   * above for further details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int index ;


  /**
   * The offset for the variable, which identifies the variable together with
   * the <code>index</code>. See the description above for further details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int offset ;


  /**
   * Allocates a new <code>TypeVariable</code> with the given
   * <code>index</code> and <code>offset</code>.
   * 
   * @param pIndex the index of the type variable, that is the step of the
   *          proof, in which it was created. A value of <code>0</code> means
   *          that the type variable was specified in the source code.
   * @param pOffset the offset, that is the number of type variables already
   *          allocated in the proof step identified by <code>index</code>.
   * @throws IllegalArgumentException if <code>index</code> or
   *           <code>offset</code> is negative.
   * @see TypeVariable
   * @see #index
   * @see #offset
   */
  public TypeVariable ( int pIndex , int pOffset )
  {
    if ( pIndex < 0 )
    {
      throw new IllegalArgumentException ( "Index is negative" ) ; //$NON-NLS-1$
    }
    if ( pOffset < 0 )
    {
      throw new IllegalArgumentException ( "Offset is negative" ) ; //$NON-NLS-1$
    }
    this.index = pIndex ;
    this.offset = pOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( TypeVariable pTypeVariable )
  {
    if ( this.index < pTypeVariable.index )
    {
      return - 1 ;
    }
    else if ( this.index > pTypeVariable.index )
    {
      return 1 ;
    }
    else if ( this.offset < pTypeVariable.offset )
    {
      return - 1 ;
    }
    else if ( this.offset > pTypeVariable.offset )
    {
      return 1 ;
    }
    else
    {
      return 0 ;
    }
  }


  /**
   * Compares this type variable to the <code>obj</code>. Returns
   * <code>true</code> if <code>obj</code> is a type variable with the same
   * index and offset as this type variable. Otherwise <code>false</code> is
   * returned.
   * 
   * @param pObject another object.
   * @return <code>true</code> if <code>obj</code> is a type variable with
   *         the same index and offset.
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof TypeVariable )
    {
      TypeVariable type = ( TypeVariable ) pObject ;
      return ( this.index == type.index && this.offset == type.offset ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#free()
   */
  @ Override
  public Set < TypeVariable > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.add ( this ) ;
    }
    return this.free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Type-Variable" ; //$NON-NLS-1$
  }


  /**
   * Returns the index of this variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number for
   * variables generated by the type inference algorithm. See the description of
   * the {@link TypeVariable} class for details about the <code>index</code>
   * and the <code>offset</code>.
   * 
   * @return the index of this variable.
   * @see #index
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  /**
   * Returns the offset of this variable. See the description of the
   * {@link TypeVariable} class for details about the <code>index</code> and
   * the <code>offset</code>.
   * 
   * @return the offset of this variable.
   * @see #offset
   */
  public int getOffset ( )
  {
    return this.offset ;
  }


  /**
   * Returns a hash value for this type variable, which is based on the index
   * and the offset of the type.
   * 
   * @return a hash value for this type variable.
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( this.index << 5 ) + this.offset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public MonoType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    // perform the substitution on this type variable
    MonoType tau = pTypeSubstitution.get ( this ) ;
    if ( ! tau.equals ( this ) )
    {
      // another type variable, substitute again
      tau = tau.substitute ( pTypeSubstitution ) ;
    }
    return tau ;
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
          PRIO_TYPE_VARIABLE ) ;
      String type = "" + offsetToGreekLetter ( this.offset % 24 ) //$NON-NLS-1$
          + ( ( this.index > 0 ) ? this.index + "" : "" ) ; //$NON-NLS-1$//$NON-NLS-2$
      for ( int n = ( this.offset / 24 ) ; n > 0 ; -- n )
      {
        type = type + "'" ; //$NON-NLS-1$
      }
      this.prettyStringBuilder.addType ( type ) ;
    }
    return this.prettyStringBuilder ;
  }
}