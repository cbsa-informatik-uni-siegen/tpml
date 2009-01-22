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
    return getNameBuilder ();
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
    final LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_CLOSURE_ENVIRONMENT, 1,
        "#1", "name" ) );
    commands.add ( new DefaultLatexCommand ( "clousreenvironmentfull", 1, "#1",
        "content" ) );
    return commands;
  }


  public LatexStringBuilder toLatexStringBuilder (
      final LatexStringBuilderFactory fac, final int pIndent )
  {
    final LatexStringBuilder builder = fac.newBuilder ( 0,
        LATEX_CLOSURE_ENVIRONMENT, pIndent );

    builder.addText ( "{" + LATEX_ETA + "_{" + this.index + "}}" ); //$NON-NLS-1$

    return builder;
  }


  public LatexStringBuilder toLatexFullStringBuilder (
      final LatexStringBuilderFactory fac, final int pIndent )
  {
    final LatexStringBuilder builder = fac.newBuilder ( 0,
        "clousreenvironmentfull"/* LATEX_CLOSURE_ENVIRONMENT_FULL */, pIndent );

    builder.addText ( "{" );
    builder.addText(this.toLatexStringBuilder(fac, pIndent).toLatexString ().toString());
    builder.addText ( LATEX_EQUAL + LATEX_LBRACKET );
    Enumeration < Identifier > e = super.symbols ();
    while ( e.hasMoreElements () )
    {
      final Identifier id = e.nextElement ();
      // builder.addBuilder ( id.toLatexStringBuilder ( fac, pIndent ), 0 );
      builder.addText ( id.toLatexStringBuilder ( fac, pIndent )
          .toLatexString ().toString () );
      builder.addText ( LATEX_COLON );
      builder.addText ( LATEX_SPACE );
      builder.addText ( super.get ( id ).toLatexString ().toString () );
      // builder.addBuilder ( super.get ( id ).toLatexStringBuilder ( fac,
      // pIndent ), 0 );
      if ( e.hasMoreElements () )
        builder.addText ( LATEX_SPACE );

    }
    builder.addText ( LATEX_RBRACKET );
    builder.addText ( "}" );
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
    return toPrettyString ().toString ();
  }


  public PrettyString toPrettyFullString ()
  {
    return toPrettyFullStringBuilder ().toPrettyString ();
  }


  public PrettyStringBuilder toPrettyFullStringBuilder ()
  {
    final PrettyStringBuilderFactory fac = PrettyStringBuilderFactory
        .newInstance ();
    final PrettyStringBuilder builder = fac.newBuilder ( this, 0 );

    builder.addText ( PRETTY_LBRACKET );
    Enumeration < Identifier > e = super.symbols ();
    while ( e.hasMoreElements () )
    {
      final Identifier id = e.nextElement ();
      builder.addBuilder ( id.toPrettyStringBuilder ( fac ), 0 );
      builder.addText ( PRETTY_COLON );
      builder.addText ( PRETTY_SPACE );
      builder.addBuilder ( super.get ( id ).toPrettyStringBuilder ( fac ), 0 );
      if ( e.hasMoreElements () )
        builder.addText ( PRETTY_SPACE );

    }
    builder.addText ( PRETTY_RBRACKET );
    return builder;
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
  public PrettyString getName ()
  {
    return getNameBuilder ().toPrettyString ();
  }


  public PrettyStringBuilder getNameBuilder ()
  {
    final PrettyStringBuilderFactory fac = PrettyStringBuilderFactory
        .newInstance ();
    final PrettyStringBuilder builder = fac.newBuilder ( this, 0 );

    builder.addText ( PRETTY_ETA );
    builder.addText ( String.valueOf ( this.index ) );
    return builder;
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
