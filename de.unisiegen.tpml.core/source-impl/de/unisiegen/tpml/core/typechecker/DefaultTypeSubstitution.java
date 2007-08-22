package de.unisiegen.tpml.core.typechecker ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.TypeVariable ;


/**
 * Default implementation of the <code>TypeSubstitution</code> interface. This
 * is an internal class of the type checker. If you need to generate new
 * substitutions outside the generic type checker implementation, use the
 * {@link de.unisiegen.tpml.core.typechecker.TypeUtilities#newSubstitution(TypeVariable, MonoType)}
 * method instead.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1194 $
 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution
 */
public final class DefaultTypeSubstitution implements TypeSubstitution ,
    PrettyPrintable , LatexCommandNames
{
  /**
   * The empty type substitution, which does not contain any mappings.
   * 
   * @see #DefaultTypeSubstitution()
   */
  public static final DefaultTypeSubstitution EMPTY_SUBSTITUTION = new DefaultTypeSubstitution ( ) ;


  /**
   * The type variable at this level of the substitution.
   */
  private TypeVariable tvar ;


  /**
   * The type to substitute for the <code>tvar</code>.
   */
  private MonoType type ;


  /**
   * The remaining <code>(tvar,type)</code> pairs in the substitution.
   */
  private DefaultTypeSubstitution parent ;


  /**
   * Allocates a new empty <code>DefaultTypeSubstitution</code>.
   */
  DefaultTypeSubstitution ( )
  {
    super ( ) ;
  }


  /**
   * Convenience wrapper for the
   * {@link #DefaultTypeSubstitution(TypeVariable, MonoType, DefaultTypeSubstitution)}
   * constructor, which passes {@link #EMPTY_SUBSTITUTION} for the
   * <code>parent</code> parameter.
   * 
   * @param pTvar the type variable.
   * @param pType the (concrete) monomorphic type to substitute for
   *          <code>tvar</code>.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public DefaultTypeSubstitution ( TypeVariable pTvar , MonoType pType )
  {
    this ( pTvar , pType , EMPTY_SUBSTITUTION ) ;
  }


  /**
   * Allocates a new <code>DefaultTypeSubstitution</code> which represents a
   * pair <code>(tvar,type)</code> and chains up to the specified
   * <code>parent</code>.
   * 
   * @param pTvar the type variable.
   * @param pType the (concrete) monomorphic type to substitute for
   *          <code>tvar</code>.
   * @param pParent the parent substitution to chain up to.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  DefaultTypeSubstitution ( TypeVariable pTvar , MonoType pType ,
      DefaultTypeSubstitution pParent )
  {
    if ( pTvar == null )
    {
      throw new NullPointerException ( "tvar is null" ) ; //$NON-NLS-1$
    }
    if ( pType == null )
    {
      throw new NullPointerException ( "type is null" ) ; //$NON-NLS-1$
    }
    if ( pParent == null )
    {
      throw new NullPointerException ( "parent is null" ) ; //$NON-NLS-1$
    }
    this.tvar = pTvar ;
    this.type = pType ;
    this.parent = pParent ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#compose(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  public DefaultTypeSubstitution compose ( TypeSubstitution s )
  {
    // if this is the empty substitution, the
    // result of the composition is simply s
    if ( this == EMPTY_SUBSTITUTION )
    {
      return ( DefaultTypeSubstitution ) s ;
    }
    // compose(parent, s)
    DefaultTypeSubstitution parentSubstitution = this.parent.compose ( s ) ;
    // and prepend (name,type) pair
    return new DefaultTypeSubstitution ( this.tvar , this.type ,
        parentSubstitution ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#free()
   */
  public Set < TypeVariable > free ( )
  {
    if ( this == EMPTY_SUBSTITUTION )
    {
      return new TreeSet < TypeVariable > ( ) ;
    }
    TreeSet < TypeVariable > free = new TreeSet < TypeVariable > ( ) ;
    free.addAll ( this.type.getTypeVariablesFree ( ) ) ;
    free.remove ( this.tvar ) ;
    free.addAll ( this.parent.free ( ) ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#get(de.unisiegen.tpml.core.types.TypeVariable)
   */
  public MonoType get ( TypeVariable pTvar )
  {
    if ( this == EMPTY_SUBSTITUTION )
    {
      // reached the end of the substitution chain
      return pTvar ;
    }
    else if ( this.tvar.equals ( pTvar ) )
    {
      // we have a match here
      return this.type ;
    }
    else
    {
      // check the parent substitution
      return this.parent.get ( pTvar ) ;
    }
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_SUBSTITUTION , 2 ,
        "#1/#2" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.type.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.tvar.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.tvar.getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : this.type.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.tvar.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_SUBSTITUTION ) ;
    builder.addBuilder ( this.type
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    builder.addBuilder ( this.tvar
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , 0 ) ;
    builder.addBuilder ( this.type
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( "/" ) ; //$NON-NLS-1$
    builder.addBuilder ( this.tvar
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return this.type.toString ( ) + "/" + this.tvar.toString ( ) ; //$NON-NLS-1$
  }
}
