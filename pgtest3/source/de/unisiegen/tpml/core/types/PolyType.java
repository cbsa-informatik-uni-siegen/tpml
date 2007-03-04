package de.unisiegen.tpml.core.types ;


import java.util.Iterator ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.TypeUtilities ;


/**
 * Instances of this class represent polymorphic types, which are basicly
 * monomorphic types with a set of quantified type variables.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:511 $
 * @see Type
 */
public final class PolyType extends Type
{
  /**
   * The quantified type variables.
   * 
   * @see #getQuantifiedVariables()
   */
  private Set < TypeVariable > quantifiedVariables ;


  /**
   * The monomorphic type.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * Allocates a new <code>PolyType</code> with the given
   * <code>quantifiedVariables</code> and the monomorphic type
   * <code>tau</code>.
   * 
   * @param pQuantifiedVariables the set of quantified type variables for
   *          <code>tau</code>.
   * @param pTau the monomorphic type.
   * @throws NullPointerException if <code>quantifiedVariables</code> or
   *           <code>pTau</code> is <code>null</code>.
   * @see MonoType
   */
  public PolyType ( Set < TypeVariable > pQuantifiedVariables , MonoType pTau )
  {
    if ( pQuantifiedVariables == null )
    {
      throw new NullPointerException ( "QuantifiedVariables is null" ) ; //$NON-NLS-1$
    }
    if ( pTau == null )
    {
      throw new NullPointerException ( "Tau is null" ) ; //$NON-NLS-1$
    }
    this.quantifiedVariables = pQuantifiedVariables ;
    this.tau = pTau ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof PolyType )
    {
      PolyType other = ( PolyType ) pObject ;
      return ( this.quantifiedVariables.equals ( other.quantifiedVariables ) && this.tau
          .equals ( other.tau ) ) ;
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
    free.addAll ( this.tau.free ( ) ) ;
    free.removeAll ( this.quantifiedVariables ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Poly-Type" ; //$NON-NLS-1$
  }


  /**
   * Returns the set of quantified variables.
   * 
   * @return the quantified type variables.
   */
  public Set < TypeVariable > getQuantifiedVariables ( )
  {
    return this.quantifiedVariables ;
  }


  /**
   * Returns the monomorphic type.
   * 
   * @return the monomorphic type.
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.quantifiedVariables.hashCode ( ) + this.tau.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public Type substitute ( TypeSubstitution pTypeSubstitution )
  {
    // determine the monomorphic type
    MonoType newTau = this.tau ;
    // perform a bound rename on the type variables
    TreeSet < TypeVariable > newQuantifiedVariables = new TreeSet < TypeVariable > ( ) ;
    for ( TypeVariable tvar : this.quantifiedVariables )
    {
      // generate a type variable that is not present in the substitution
      TypeVariable tvarn = tvar ;
      while ( ! tvarn.substitute ( pTypeSubstitution ).equals ( tvarn ) )
      {
        tvarn = new TypeVariable ( tvarn.getIndex ( ) , tvarn.getOffset ( ) + 1 ) ;
      }
      // check if we had to generate a new type variable
      if ( ! tvar.equals ( tvarn ) )
      {
        // substitute tvarn for tvar in tau
        newTau = newTau.substitute ( TypeUtilities.newSubstitution ( tvar ,
            tvarn ) ) ;
      }
      // add the type variable to the set
      newQuantifiedVariables.add ( tvarn ) ;
    }
    // apply the substitution to the monomorphic type
    newTau = newTau.substitute ( pTypeSubstitution ) ;
    // generate the polymorphic type
    return new PolyType ( newQuantifiedVariables , newTau ) ;
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
        this , PRIO_POLY ) ;
    if ( ! this.quantifiedVariables.isEmpty ( ) )
    {
      builder.addText ( "\u2200" ) ; //$NON-NLS-1$
      for ( Iterator < TypeVariable > it = this.quantifiedVariables.iterator ( ) ; it
          .hasNext ( ) ; )
      {
        builder.addText ( it.next ( ).toString ( ) ) ;
        if ( it.hasNext ( ) )
        {
          builder.addText ( ", " ) ; //$NON-NLS-1$
        }
      }
      builder.addText ( "." ) ; //$NON-NLS-1$
    }
    builder.addBuilder ( this.tau
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_POLY_TAU ) ;
    return builder ;
  }
}
