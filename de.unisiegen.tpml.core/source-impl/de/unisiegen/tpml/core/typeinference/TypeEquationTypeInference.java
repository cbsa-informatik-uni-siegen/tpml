package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
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
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents a type equation. Used for the unification algorithm.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker
 */
public final class TypeEquationTypeInference implements ShowBondsInput ,
    TypeFormula , PrettyPrintable , LatexCommandNames
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_EQUATION_TYPE_INFERENCE , 2 , "#1\\ \\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{=}\\ #2" , "tau1" , "tau2" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
   * The {@link TypeEquationTypeInference}s which were unified before.
   */
  private SeenTypes < TypeEquationTypeInference > seenTypes ;


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
  public TypeEquationTypeInference ( final MonoType pLeft ,
      final MonoType pRight , SeenTypes < TypeEquationTypeInference > pSeenTypes )
  {
    if ( pLeft == null )
    {
      throw new NullPointerException ( "left is null" ) ; //$NON-NLS-1$
    }
    if ( pRight == null )
    {
      throw new NullPointerException ( "right is null" ) ; //$NON-NLS-1$
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
   * Clones this type equation, so that the result is an type equation equal to
   * this type equation.
   * 
   * @return a clone of this object.
   * @see Object#clone()
   */
  @ Override
  public TypeEquationTypeInference clone ( )
  {
    return new TypeEquationTypeInference ( this.left , this.right ,
        this.seenTypes ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( final Object obj )
  {
    if ( obj instanceof TypeEquationTypeInference )
    {
      final TypeEquationTypeInference other = ( TypeEquationTypeInference ) obj ;
      return ( this.left.equals ( other.left ) && this.right
          .equals ( other.right ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getEnvironment()
   */
  public DefaultTypeEnvironment getEnvironment ( )
  {
    return new DefaultTypeEnvironment ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
   */
  public Expression getExpression ( )
  {
    return null ;
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
    commands.add ( this.left ) ;
    commands.add ( this.right ) ;
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
    instructions.add ( this.left ) ;
    instructions.add ( this.right ) ;
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
    packages.add ( this.left ) ;
    packages.add ( this.right ) ;
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
  public SeenTypes < TypeEquationTypeInference > getSeenTypes ( )
  {
    return this.seenTypes ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getType()
   */
  public MonoType getType ( )
  {
    throw new RuntimeException ( "Do not use me!" ) ; //$NON-NLS-1$
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
   * set the left type of this type equation
   * 
   * @param pLeft MonoType to set as left type
   */
  public void setLeft ( MonoType pLeft )
  {
    this.left = pLeft ;
  }


  /**
   * set the right type of this type equation
   * 
   * @param pRight MonoType to set as right type
   */
  public void setRight ( MonoType pRight )
  {
    this.right = pRight ;
  }


  //
  // Primitives
  //
  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to the types on both
   * sides of the equation and returns the resulting equation.
   * 
   * @param substitutions The {@link DefaultTypeSubstitution}s.
   * @return the resulting {@link TypeEquationTypeInference}.
   * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
   */
  public TypeEquationTypeInference substitute (
      ArrayList < TypeSubstitution > substitutions )
  {
    TypeEquationTypeInference newEqn = this.clone ( ) ;
    for ( TypeSubstitution s : substitutions )
    {
      newEqn.setLeft ( newEqn.getLeft ( ).substitute ( s ) ) ;
      newEqn.setRight ( newEqn.getRight ( ).substitute ( s ) ) ;
    }
    // apply the substitution to the left and the right side
    return newEqn ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_EQUATION_TYPE_INFERENCE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.left.toPrettyString ( ).toString ( ) ,
        this.right.toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.left.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.right.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
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
    builder.addBuilder ( this.left
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_EQUAL ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.right
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type equation. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type equation.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
