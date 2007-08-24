package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
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
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_POLY_TYPE , 2 ,
        "\\ifthenelse{\\equal{#1}{}}" + LATEX_LINE_BREAK_NEW_COMMAND + "{#2}" //$NON-NLS-1$ //$NON-NLS-2$
            + LATEX_LINE_BREAK_NEW_COMMAND + "{#1.#2}" , //$NON-NLS-1$
        "forall tvar1, ..., tvarn" , "tau" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    for ( TypeVariable typeVariable : this.quantifiedVariables )
    {
      for ( LatexCommand command : typeVariable.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  @ Override
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = super.getLatexPackages ( ) ;
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    return packages ;
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
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    if ( this.latexStringBuilder == null )
    {
      StringBuilder body = new StringBuilder ( ) ;
      if ( ! this.quantifiedVariables.isEmpty ( ) )
      {
        body.append ( PRETTY_FORALL ) ;
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          body.append ( it.next ( ).toPrettyString ( ).toString ( ) ) ;
          if ( it.hasNext ( ) )
          {
            body.append ( PRETTY_COMMA ) ;
            body.append ( PRETTY_SPACE ) ;
          }
        }
        body.append ( PRETTY_DOT ) ;
      }
      body.append ( this.types [ 0 ].toPrettyString ( ).toString ( ) ) ;
      String descriptions[] = new String [ 2 + this.quantifiedVariables.size ( )
          + this.types.length ] ;
      descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
      descriptions [ 1 ] = body.toString ( ) ;
      int count = 2 ;
      for ( Iterator < TypeVariable > it = this.quantifiedVariables.iterator ( ) ; it
          .hasNext ( ) ; )
      {
        descriptions [ count ] = it.next ( ).toPrettyString ( ).toString ( ) ;
        count ++ ;
      }
      descriptions [ descriptions.length - 1 ] = this.types [ 0 ]
          .toPrettyString ( ).toString ( ) ;
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_POLY , LATEX_POLY_TYPE , pIndent , descriptions ) ;
      if ( ! this.quantifiedVariables.isEmpty ( ) )
      {
        this.latexStringBuilder.addBuilderBegin ( ) ;
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_FORALL ) ;
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          this.latexStringBuilder.addBuilder ( it.next ( )
              .toLatexStringBuilder ( pLatexStringBuilderFactory ,
                  pIndent + LATEX_INDENT * 2 ) , 0 ) ;
          if ( it.hasNext ( ) )
          {
            this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
            this.latexStringBuilder.addText ( DefaultLatexStringBuilder
                .getIndent ( pIndent + LATEX_INDENT )
                + LATEX_COMMA ) ;
            this.latexStringBuilder.addText ( LATEX_SPACE ) ;
          }
        }
        this.latexStringBuilder.addBuilderEnd ( ) ;
      }
      else
      {
        this.latexStringBuilder.addEmptyBuilder ( ) ;
      }
      this.latexStringBuilder.addBuilder ( this.types [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
              + LATEX_INDENT ) , PRIO_POLY_TAU ) ;
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
        this.prettyStringBuilder.addText ( PRETTY_FORALL ) ;
        for ( Iterator < TypeVariable > it = this.quantifiedVariables
            .iterator ( ) ; it.hasNext ( ) ; )
        {
          this.prettyStringBuilder.addBuilder ( it.next ( )
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
          if ( it.hasNext ( ) )
          {
            this.prettyStringBuilder.addText ( PRETTY_COMMA ) ;
            this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          }
        }
        this.prettyStringBuilder.addText ( PRETTY_DOT ) ;
      }
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_POLY_TAU ) ;
    }
    return this.prettyStringBuilder ;
  }
}
