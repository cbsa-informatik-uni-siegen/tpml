package de.unisiegen.tpml.core.types ;


import java.util.Iterator ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
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
public final class PolyType extends Type implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * The quantified type variables.
   * 
   * @see #getQuantifiedVariables()
   */
  private Set < TypeVariable > quantifiedVariables ;


  /**
   * TODO
   */
  private MonoType [ ] types ;


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
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ].getParent ( ) != null )
    {
      this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
    }
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public PolyType clone ( )
  {
    Set < TypeVariable > newQuantifiedVariables = new TreeSet < TypeVariable > ( ) ;
    Iterator < TypeVariable > it = this.quantifiedVariables.iterator ( ) ;
    while ( it.hasNext ( ) )
    {
      newQuantifiedVariables.add ( it.next ( ) ) ;
    }
    return new PolyType ( newQuantifiedVariables , this.types [ 0 ].clone ( ) ) ;
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
      return ( this.quantifiedVariables.equals ( other.quantifiedVariables ) && this.types [ 0 ]
          .equals ( other.types [ 0 ] ) ) ;
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
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      this.free.addAll ( this.types [ 0 ].free ( ) ) ;
      this.free.removeAll ( this.quantifiedVariables ) ;
    }
    return this.free ;
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
    return this.types [ 0 ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
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
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.quantifiedVariables.hashCode ( ) + this.types [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public PolyType substitute ( TypeSubstitution pTypeSubstitution )
  {
    // determine the monomorphic type
    MonoType newTau = this.types [ 0 ] ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_POLY ) ;
      if ( ! this.quantifiedVariables.isEmpty ( ) )
      {
        this.prettyStringBuilder.addText ( "\u2200" ) ; //$NON-NLS-1$
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          this.prettyStringBuilder.addText ( it.next ( ).toString ( ) ) ;
          if ( it.hasNext ( ) )
          {
            this.prettyStringBuilder.addText ( ", " ) ; //$NON-NLS-1$
          }
        }
        this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      }
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_POLY_TAU ) ;
    }
    return this.prettyStringBuilder ;
  }
}
