package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
public class TypeJudgement implements TypeFormula , LatexCommandNames
{
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
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_JUDGEMENT , 3 , "#1\\ " //$NON-NLS-1$
        + LATEX_RIGHT_TRIANGLE + "\\ #2\\ ::\\ #3" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.environment.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.expression.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.type.getLatexCommands ( ) )
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
    for ( LatexInstruction instruction : this.environment
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.expression
        .getLatexInstructions ( ) )
    {
      instructions.add ( instruction ) ;
    }
    for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) )
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
    packages.add ( new DefaultLatexPackage ( "amssymb" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.environment.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.type.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.expression.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
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
        0 , LATEX_TYPE_JUDGEMENT ) ;
    builder.addBuilder ( this.environment
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    builder.addBuilder ( this.expression
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    builder.addBuilder ( this.type
        .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation of the equations contained in this list.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation.
   * @see TypeEquationTypeInference#toString()
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( this.environment ) ;
    builder.append ( " \u22b3 " ) ; //$NON-NLS-1$
    builder.append ( this.expression ) ;
    builder.append ( " :: " ) ; //$NON-NLS-1$
    builder.append ( this.type ) ;
    return builder.toString ( ) ;
  }
}
