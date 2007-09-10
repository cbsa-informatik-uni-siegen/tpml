package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
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
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents an judgemnet for unification
 * 
 * @see TypeFormula
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class TypeJudgement implements TypeFormula
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_JUDGEMENT , 3 ,
        "#1\\ \\color{" + LATEX_COLOR_NONE + "}{" + LATEX_RIGHT_TRIANGLE //$NON-NLS-1$ //$NON-NLS-2$
            + "}\\ #2\\ \\color{" + LATEX_COLOR_NONE + "}{::}\\ #3" , //$NON-NLS-1$//$NON-NLS-2$
        "env" , "e" , "tau" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
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
    packages.add ( LatexPackage.AMSSYMB ) ;
    packages.add ( LatexPackage.COLOR ) ;
    return packages ;
  }


  /**
   * the type environment of this type judgement
   */
  private TypeEnvironment environment ;


  /**
   * the expression of this type judgement
   */
  private Expression expression ;


  /**
   * the type of this type judgement
   */
  private MonoType type ;


  /**
   * Allocates a new <code>Typejudgemnent</code> with the specified
   * <code>environment</code>. <code>expression</code> and
   * <code>type</code>
   * 
   * @param env DefaultTypeEnvironment
   * @param expr Expression
   * @param t MonoType
   */
  public TypeJudgement ( final TypeEnvironment env , final Expression expr ,
      final MonoType t )
  {
    this.environment = env ;
    this.expression = expr ;
    this.type = t ;
  }


  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object o )
  {
    if ( o instanceof TypeJudgement )
    {
      TypeJudgement other = ( TypeJudgement ) o ;
      if ( ( this.environment.equals ( other.environment ) )
          && ( this.expression.equals ( other.expression ) )
          && ( this.type.equals ( other.type ) ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * get the type environment of this type judgement
   * 
   * @return DefaultTypeEnvironment environment
   */
  public TypeEnvironment getEnvironment ( )
  {
    return this.environment ;
  }


  /**
   * get the Expression of this type judgement
   * 
   * @return expression Expression of this type judgement
   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
   */
  public Expression getExpression ( )
  {
    return this.expression ;
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
    commands.add ( this.environment ) ;
    commands.add ( this.expression ) ;
    commands.add ( this.type ) ;
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
    instructions.add ( this.environment ) ;
    instructions.add ( this.expression ) ;
    instructions.add ( this.type ) ;
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
    packages.add ( this.environment ) ;
    packages.add ( this.type ) ;
    packages.add ( this.expression ) ;
    return packages ;
  }


  /**
   * get the type of this type judgement
   * 
   * @return type MonoType of this judgement
   */
  public MonoType getType ( )
  {
    return this.type ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public int hashCode ( )
  {
    return this.environment.hashCode ( ) + this.expression.hashCode ( )
        + this.type.hashCode ( ) ;
  }


  /**
   * set the type environment of this type judgement
   * 
   * @param pEnvironment new type environment for this type judement
   */
  public void setEnvironment ( final DefaultTypeEnvironment pEnvironment )
  {
    this.environment = pEnvironment ;
  }


  /**
   * set a new expression for this type judgement
   * 
   * @param pExpression the expression to be set
   */
  public void setExpression ( final Expression pExpression )
  {
    this.expression = pExpression ;
  }


  /**
   * set the type of this type judgement
   * 
   * @param pType new MonoType for this judgement
   */
  public void setType ( final MonoType pType )
  {
    this.type = pType ;
  }


  /**
   * substitude the type equation of this type judgement
   * 
   * @param substitutions TypeSubstitution to substitute
   * @return null (just needed for TypeEquation)
   */
  public TypeJudgement substitute ( ArrayList < TypeSubstitution > substitutions )
  {
    MonoType newType = this.type.clone ( ) ;
    for ( TypeSubstitution s : substitutions )
    {
      newType.substitute ( s ) ;
    }
    return new TypeJudgement ( this.environment , this.expression , newType ) ;
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
        LATEX_TYPE_JUDGEMENT , pIndent , this.toPrettyString ( ).toString ( ) ,
        this.environment.toPrettyString ( ).toString ( ) , this.expression
            .toPrettyString ( ).toString ( ) , this.type.toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.environment.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expression.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.type.toLatexStringBuilder (
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
    builder.addBuilder ( this.environment
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_RIGHT_TRIANGLE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_COLON ) ;
    builder.addText ( PRETTY_COLON ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.type
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type judgement. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type judgement.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
