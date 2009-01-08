package de.unisiegen.tpml.core;


import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.util.AbstractEnvironment;


/**
 * TODO
 */
public final class DefaultClosureEnvironment extends
    AbstractEnvironment < Identifier, Closure > implements ClosureEnvironment
{

  public DefaultClosureEnvironment ( final int index )
  {
    this.index = index;
  }


  public void put ( final Identifier identifier, final Closure closure )
  {
    super.put ( identifier, closure );
  }


  public Closure get ( final Identifier identifier )
  {
    if ( !super.containsSymbol ( identifier ) )
      throw new RuntimeException ( identifier.toString () + " not found!" );
    return super.get ( identifier );
  }


  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * TODO
   * 
   * @param fac
   * @return
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory fac )
  {
    final PrettyStringBuilder builder = fac.newBuilder ( this, 0 );

    builder.addText ( getName () );

    /*
     * builder.addText ( PRETTY_LBRACKET ); Enumeration < Identifier > e =
     * this.symbols (); while ( e.hasMoreElements () ) { Identifier id =
     * e.nextElement (); Closure closure = this.get ( id ); if (
     * closure.getEnvironment () == this ) continue; builder.addBuilder (
     * id.toPrettyStringBuilder ( fac ), 0 ); builder.addText ( PRETTY_COLON );
     * builder.addBuilder ( closure.toPrettyStringBuilder ( fac ), 0 ); if (
     * e.hasMoreElements () ) builder.addText ( PRETTY_COMMA ); }
     * builder.addText ( PRETTY_RBRACKET );
     */

    return builder;
  }


  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public LatexCommandList getLatexCommands ()
  {
    final LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    //commands
    //    .add ( new DefaultLatexCommand ( LATEX_CLOSURE_ENVIRONMENT, 0, "" ) );
    // 2, LATEX_LPAREN
    // + "#1" + LATEX_COMMA + "#2" + LATEX_RPAREN, "expression", "environment"
    // ));
    return commands;
  }


  public LatexStringBuilder toLatexStringBuilder (
      final LatexStringBuilderFactory fac, final int pIndent )
  {
    final LatexStringBuilder builder = fac.newBuilder ( 0, LATEX_CLOSURE_ENVIRONMENT,
        pIndent );
    
    builder.addText ( LATEX_ETA + "_{" + this.index + '}' ); //$NON-NLS-1$

    return builder;
  }


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    return packages; // FIXME
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    return instructions; // FIXME
  }


  public String toString ()
  {
    StringBuilder builder = new StringBuilder ();
    builder.append ( '[' );
    Enumeration < Identifier > e = super.symbols ();
    while ( e.hasMoreElements () )
    {
      final Identifier id = e.nextElement ();
      builder.append ( id.toString () );
      builder.append ( ": " );
      builder.append ( super.get ( id ).toString () );
      builder.append ( ' ' );

    }
    builder.append ( ']' );
    return builder.toString ();
  }


  /**
   * Creates a new empty environment with a new index. We start with an empty
   * environment, and (OP-1) and (OP-2) can produce empty environments as well
   * 
   * @param index The new environment's index
   * @return The new empty environment
   */
  public static final DefaultClosureEnvironment empty ( final int index )
  {
    return new DefaultClosureEnvironment ( index );
  }


  /**
   * Clones the current environment, but gives the new environment a new index
   * 
   * @param newIndex The new environment's index
   * @return The new environment
   * @see de.unisiegen.tpml.core.ClosureEnvironment#clone(int)
   */
  public Object clone ( final int newIndex )
  {
    final DefaultClosureEnvironment cl = new DefaultClosureEnvironment (
        newIndex );
    Enumeration < Identifier > e = super.symbols ();
    while ( e.hasMoreElements () )
    {
      final Identifier id = e.nextElement ();
      cl.put ( id, this.get ( id ) );
    }
    return cl;
  }


  /**
   * @return The name of this environment (like etha_0)
   * @see de.unisiegen.tpml.core.ClosureEnvironment#getName()
   */
  public String getName ()
  {
    return PRETTY_ETA + this.index;
  }


  /**
   * @return Tells whether this environment has to be printed
   * @see de.unisiegen.tpml.core.ClosureEnvironment#isNotPrinted()
   */
  public boolean isNotPrinted ()
  {
    final boolean ret = this.notPrinted;
    this.notPrinted = false;
    return ret;
  }


  /**
   * This environment's index, like 0, 1, 2, ...
   */
  private int index;


  /**
   * Tells if this environment should be printed
   */
  private boolean notPrinted = true;
}
