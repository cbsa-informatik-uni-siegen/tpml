package de.unisiegen.tpml.core.typechecker ;


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
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents a type equation. Used primarily for the unification algorithm.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:838 $
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker
 */
public final class TypeEquationTypeChecker implements LatexPrintable ,
    LatexCommandNames
{
  /**
   * The monomorphic type on the left side.
   * 
   * @see #getLeft()
   */
  private MonoType left ;


  /**
   * The monomorphic type on the right side.
   * 
   * @see #getRight()
   */
  private MonoType right ;


  /**
   * The {@link TypeEquationTypeChecker}s which were unified before.
   */
  private SeenTypes < TypeEquationTypeChecker > seenTypes ;


  /**
   * Allocates a new <code>TypeEquation</code> with the given
   * <code>left</code> and <code>right</code> side types.
   * 
   * @param pLeft the monomorphic type on the left side.
   * @param pRight the monomorphic type on the right side.
   * @param pSeenTypes The {@link TypeEquationTypeChecker}s which were unified
   *          before.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  public TypeEquationTypeChecker ( MonoType pLeft , MonoType pRight ,
      SeenTypes < TypeEquationTypeChecker > pSeenTypes )
  {
    if ( pLeft == null )
    {
      throw new NullPointerException ( "Left is null" ) ; //$NON-NLS-1$
    }
    if ( pRight == null )
    {
      throw new NullPointerException ( "Right is null" ) ; //$NON-NLS-1$
    }
    if ( pSeenTypes == null )
    {
      throw new NullPointerException ( "SeenTypes is null" ) ; //$NON-NLS-1$
    }
    this.left = pLeft ;
    this.right = pRight ;
    this.seenTypes = pSeenTypes ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof TypeEquationTypeChecker )
    {
      TypeEquationTypeChecker other = ( TypeEquationTypeChecker ) obj ;
      return ( this.left.equals ( other.left ) && this.right
          .equals ( other.right ) ) ;
    }
    return false ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_EQUATION_TYPE_CHECKER ,
        2 , "#1\\ =\\ #2" , "tau1" , "tau2" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
    for ( LatexCommand command : this.left.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.right.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.left.getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.right.getLatexInstructions ( ) )
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
    for ( LatexPackage pack : this.left.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.right.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * Returns the monomorphic type on the left side.
   * 
   * @return the left side type.
   */
  public MonoType getLeft ( )
  {
    return this.left ;
  }


  /**
   * Returns the monomorphic type on the right side.
   * 
   * @return the right side type.
   */
  public MonoType getRight ( )
  {
    return this.right ;
  }


  /**
   * Returns the seenTypes.
   * 
   * @return The seenTypes.
   * @see #seenTypes
   */
  public SeenTypes < TypeEquationTypeChecker > getSeenTypes ( )
  {
    return this.seenTypes ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.left.hashCode ( ) + this.right.hashCode ( ) ;
  }


  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to the types on both
   * sides of the equation and returns the resulting equation.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * @return the resulting {@link TypeEquationTypeChecker}.
   * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
   */
  public TypeEquationTypeChecker substitute ( TypeSubstitution s )
  {
    // apply the substitution to the left and the right side
    return new TypeEquationTypeChecker ( this.left.substitute ( s ) ,
        this.right.substitute ( s ) , this.seenTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_EQUATION_TYPE_CHECKER , pIndent ) ;
    builder.addBuilder ( this.left.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBuilder ( this.right.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc} Returns the string representation for the type equation,
   * which is primarily useful for debugging.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return ( this.seenTypes + " " + this.left + " = " + this.right ) ; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
