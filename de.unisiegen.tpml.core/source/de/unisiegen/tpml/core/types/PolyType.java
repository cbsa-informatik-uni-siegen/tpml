package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
   * The keyword <code>for all</code>.
   */
  private static final String FORALL = "\u2200" ; //$NON-NLS-1$


  /**
   * The keyword <code>,</code>.
   */
  private static final String COMMA = "," ; //$NON-NLS-1$


  /**
   * The keyword <code>.</code>.
   */
  private static final String DOT = "." ; //$NON-NLS-1$


  /**
   * The space string.
   */
  private static final String SPACE = " " ; //$NON-NLS-1$


  /**
   * String for the case that the quantified variables are null.
   */
  private static final String QUANTIFIED_VARIABLES_NULL = "quantified variables are null" ; //$NON-NLS-1$


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * String for the case that tau is null.
   */
  private static final String TAU_NULL = "tau is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( PolyType.class ) ;


  /**
   * The quantified type variables.
   * 
   * @see #getQuantifiedVariables()
   */
  private Set < TypeVariable > quantifiedVariables ;


  /**
   * The children {@link Type}s of this {@link Type}.
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
      throw new NullPointerException ( QUANTIFIED_VARIABLES_NULL ) ;
    }
    if ( pTau == null )
    {
      throw new NullPointerException ( TAU_NULL ) ;
    }
    this.quantifiedVariables = pQuantifiedVariables ;
    this.types = new MonoType [ ]
    { pTau } ;
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
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_POLY_TYPE , 1 , "#1" ) ) ; //$NON-NLS-1$
    for ( TypeVariable typeVariable : this.quantifiedVariables )
    {
      for ( LatexCommand command : typeVariable.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( LatexCommand command : this.types [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
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
  public ArrayList < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.typeVariablesFree == null )
    {
      this.typeVariablesFree = new ArrayList < TypeVariable > ( ) ;
      this.typeVariablesFree
          .addAll ( this.types [ 0 ].getTypeVariablesFree ( ) ) ;
      for ( TypeVariable tvar : this.quantifiedVariables )
      {
        while ( this.typeVariablesFree.remove ( tvar ) )
        {
          // Remove all TypeVariables
        }
      }
    }
    return this.typeVariablesFree ;
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
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no
   * substitution took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  @ Override
  public PolyType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType newTau = this.types [ 0 ].substitute ( pTypeName , pTau ) ;
    return new PolyType ( this.quantifiedVariables , newTau ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public PolyType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
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
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_POLY , LATEX_POLY_TYPE ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      if ( ! this.quantifiedVariables.isEmpty ( ) )
      {
        this.latexStringBuilder.addText ( LATEX_FORALL ) ;
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          this.latexStringBuilder.addText ( it.next ( ).toLatexString ( )
              .toString ( ) ) ;
          if ( it.hasNext ( ) )
          {
            this.latexStringBuilder.addText ( LATEX_COMMA ) ;
            this.latexStringBuilder.addText ( LATEX_SPACE ) ;
          }
        }
        this.latexStringBuilder.addText ( LATEX_DOT ) ;
      }
      this.latexStringBuilder.addBuilder ( this.types [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_POLY_TAU ) ;
      this.latexStringBuilder.addBuilderEnd ( ) ;
    }
    return this.latexStringBuilder ;
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
        this.prettyStringBuilder.addText ( FORALL ) ;
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          this.prettyStringBuilder.addText ( it.next ( ).toString ( ) ) ;
          if ( it.hasNext ( ) )
          {
            this.prettyStringBuilder.addText ( COMMA ) ;
            this.prettyStringBuilder.addText ( SPACE ) ;
          }
        }
        this.prettyStringBuilder.addText ( DOT ) ;
      }
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_POLY_TAU ) ;
    }
    return this.prettyStringBuilder ;
  }
}
