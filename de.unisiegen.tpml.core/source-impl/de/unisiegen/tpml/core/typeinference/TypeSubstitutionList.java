package de.unisiegen.tpml.core.typeinference ;


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
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * List of collected {@link TypeSubstitution}s needed in type inference
 * algorithm to perform the {@link TypeEquationTypeInference}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class TypeSubstitutionList implements LatexPrintable , LatexCommandNames
{
  /**
   * empty type substitution list
   */
  public static final TypeSubstitutionList EMPTY_LIST = new TypeSubstitutionList ( ) ;


  /**
   * The first TypeSubstitution in the list.
   */
  private DefaultTypeSubstitution first ;


  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeSubstitutionList remaining ;


  /**
   * Allocates a new, empty<code>TypeSubstitutionList</code> .
   */
  private TypeSubstitutionList ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new <code>TypeSubstitutionList</code> .
   * 
   * @param pFirst the first type substitution of the new list
   * @param pRemaining the remaining list of substitutions
   */
  private TypeSubstitutionList ( final DefaultTypeSubstitution pFirst ,
      final TypeSubstitutionList pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "first is null" ) ; //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "remaining is null" ) ; //$NON-NLS-1$
    }
    this.first = pFirst ;
    this.remaining = pRemaining ;
  }


  /**
   * add a new type substitution to this list
   * 
   * @param s DefaultTypeSubstitution to add
   * @return extended list of type substitutions
   */
  public TypeSubstitutionList extend ( final DefaultTypeSubstitution s )
  {
    return new TypeSubstitutionList ( s , this ) ;
  }


  /**
   * get the head of this type substitution list
   * 
   * @return first element of this list
   */
  public DefaultTypeSubstitution getFirst ( )
  {
    return this.first ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_SUBSTITUTION_LIST , 1 ,
        "\\{#1\\}" ) ) ; //$NON-NLS-1$
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexCommand command : list.first.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
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
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexInstruction instruction : list.first.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
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
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexPackage pack : list.first.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * get the tail of the type substitution list
   * 
   * @return the remaing list of substitutions
   */
  public TypeSubstitutionList getRemaining ( )
  {
    return this.remaining ;
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
        0 , LATEX_TYPE_SUBSTITUTION_LIST ) ;
    builder.addBuilderBegin ( ) ;
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
      builder.addBuilder ( list.first
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , 0 ) ;
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * needed for output and debug
   * 
   * @return string with all type substitutions
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( '{' ) ;
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.append ( ", " ) ; //$NON-NLS-1$
      }
      builder.append ( list.first.toString ( ) ) ;
    }
    builder.append ( '}' ) ;
    return builder.toString ( ) ;
  }
}
