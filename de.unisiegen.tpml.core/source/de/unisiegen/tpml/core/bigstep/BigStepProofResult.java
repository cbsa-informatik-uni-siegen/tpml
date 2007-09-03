package de.unisiegen.tpml.core.bigstep ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
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


/**
 * Instances of this class represent results for big step nodes in the big step
 * interpreter. A big step result consists of a value (an
 * {@link de.unisiegen.tpml.core.expressions.Expression}) and the resulting
 * {@link de.unisiegen.tpml.core.interpreters.Store}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 */
public final class BigStepProofResult implements PrettyPrintable ,
    LatexPrintable
{
  /**
   * The resulting store of a proof node.
   * 
   * @see #getStore()
   */
  private Store store ;


  /**
   * The resulting value of a proof node.
   * 
   * @see #getValue()
   */
  private Expression value ;


  /**
   * Allocates a new <code>DefaultBigStepProofResult</code> with the specified
   * <code>store</code> and <code>value</code>.
   * 
   * @param pStore the resulting store of a big step node.
   * @param pValue the resulting value of a big step node.
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * @see #getStore()
   * @see #getValue()
   */
  public BigStepProofResult ( Store pStore , Expression pValue )
  {
    if ( pStore == null )
    {
      throw new NullPointerException ( "store is null" ) ; //$NON-NLS-1$
    }
    this.store = pStore ;
    this.value = pValue ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_PROOF_RESULT , 1 ,
        "{#1}" , "body" ) ) ;//$NON-NLS-1$ //$NON-NLS-2$ 
    for ( LatexCommand command : this.value.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.store.getLatexCommands ( ) )
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
  public ArrayList < LatexInstruction > getLatexInstructions ( )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : this.value.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    for ( LatexInstruction instruction : this.store.getLatexInstructions ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
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
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : this.value.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : this.store.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * Returns the {@link Store} that is part of the result of a proven big step
   * node. To be exact, a copy of the store is returned, that may then be
   * modified by the caller (the {@link Store} has copy-on-write semantics).
   * This is to ease the implementation of memory operations in the small and
   * big step interpreters.
   * 
   * @return the resulting store for a big step proof node.
   * @see Store
   */
  public Store getStore ( )
  {
    return new DefaultStore ( ( DefaultStore ) this.store ) ;
  }


  /**
   * Returns the value that is part of the result of a proven big step node.
   * 
   * @return the resulting value for a big step proof node.
   * @see Expression
   */
  public Expression getValue ( )
  {
    return this.value ;
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
    StringBuilder body = new StringBuilder ( ) ;
    if ( this.value.containsMemoryOperations ( ) )
    {
      body.append ( this.value.toPrettyString ( ).toString ( ) ) ;
      body.append ( PRETTY_SPACE ) ;
      body.append ( PRETTY_SPACE ) ;
      body.append ( this.store.toPrettyString ( ).toString ( ) ) ;
    }
    else
    {
      body.append ( this.value.toPrettyString ( ).toString ( ) ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_BIG_STEP_PROOF_RESULT , pIndent , this.toPrettyString ( )
            .toString ( ) , body.toString ( ) , this.value.toPrettyString ( )
            .toString ( ) , this.getStore ( ).toPrettyString ( ).toString ( ) ) ;
    builder.addBuilderBegin ( ) ;
    if ( this.value.containsMemoryOperations ( ) )
    {
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT )
          + LATEX_LPAREN ) ;
      builder.addBuilder ( this.value.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT )
          + LATEX_SPACE ) ;
      builder.addText ( LATEX_SPACE ) ;
      builder.addBuilder ( this.store.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT )
          + LATEX_RPAREN ) ;
    }
    else
    {
      builder.addBuilder ( this.value.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      builder.addEmptyBuilder ( ) ;
    }
    builder.addBuilderEnd ( ) ;
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
    if ( this.value.containsMemoryOperations ( ) )
    {
      builder.addText ( PRETTY_LPAREN ) ;
      builder.addBuilder ( this.value
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.store
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_RPAREN ) ;
    }
    else
    {
      builder.addBuilder ( this.value
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    return builder ;
  }


  /**
   * Returns the string representation for this big step proof result. This
   * method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this big step proof
   *         result.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
