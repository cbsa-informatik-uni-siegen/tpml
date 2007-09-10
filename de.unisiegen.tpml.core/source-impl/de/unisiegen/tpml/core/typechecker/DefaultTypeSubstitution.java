package de.unisiegen.tpml.core.typechecker ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPackageList ;
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
public final class DefaultTypeSubstitution implements TypeSubstitution
{
  /**
   * The empty type substitution, which does not contain any mappings.
   * 
   * @see #DefaultTypeSubstitution()
   */
  public static final DefaultTypeSubstitution EMPTY_SUBSTITUTION = new DefaultTypeSubstitution ( ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_SUBSTITUTION , 2 ,
        "#1\\color{" + LATEX_COLOR_NONE + "}{/}#2" , "tau" , "tvar" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( LatexPackage.COLOR ) ;
    return packages ;
  }


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
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    commands.add ( this.type ) ;
    commands.add ( this.tvar ) ;
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions.add ( this.type ) ;
    instructions.add ( this.tvar ) ;
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( getLatexPackagesStatic ( ) ) ;
    packages.add ( this.type ) ;
    packages.add ( this.tvar ) ;
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_SUBSTITUTION , pIndent ,
        this.toPrettyString ( ).toString ( ) , this.type.toPrettyString ( )
            .toString ( ) , this.tvar.toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.type.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.tvar.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ( )
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
    builder.addText ( PRETTY_SLASH ) ;
    builder.addBuilder ( this.tvar
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type substitution. This method
   * is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type
   *         substitution.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
