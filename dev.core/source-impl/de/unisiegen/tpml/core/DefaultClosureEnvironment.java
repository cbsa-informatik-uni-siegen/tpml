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

  // TODO: remove later
  public DefaultClosureEnvironment()
  {
    this.name = "";
  }
  
  public DefaultClosureEnvironment(final String name)
  {
    this.name = name;
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


  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory fac )
  {
    PrettyStringBuilder builder = fac.newBuilder ( this, 0 );

    builder.addText ( PRETTY_LBRACKET );

    Enumeration < Identifier > e = this.symbols ();
    while ( e.hasMoreElements () )
    {
      Identifier id = e.nextElement ();
      Closure closure = this.get ( id );

      if ( closure.getEnvironment () == this )
        continue;

      builder.addBuilder ( id.toPrettyStringBuilder ( fac ), 0 );
      builder.addText ( PRETTY_COLON );
      builder.addBuilder ( closure.toPrettyStringBuilder ( fac ), 0 );

      if ( e.hasMoreElements () )
        builder.addText ( PRETTY_COMMA );
    }

    builder.addText ( PRETTY_RBRACKET );

    return builder;
  }


  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList();
    commands.add ( getLatexCommandsStatic() );
    return commands;
  }
  
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_CLOSURE_ENVIRONMENT, 0, ""));
    //2, LATEX_LPAREN
    //    + "#1" + LATEX_COMMA + "#2" + LATEX_RPAREN, "expression", "environment" ));
    return commands;
  }


  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory fac, int pIndent )
  {
     LatexStringBuilder builder = fac.newBuilder ( 0, LATEX_CLOSURE_ENVIRONMENT,
        pIndent); // FIXME

     builder.addText ( "{" + toPrettyString().toString() + "}");
    // FIXME
    
    return builder;
  }


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList();
    return packages; // FIXME
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList();
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


  public static final DefaultClosureEnvironment empty ()
  {
    return new DefaultClosureEnvironment ();
  }


  public Object clone ()
  {
    DefaultClosureEnvironment cl = new DefaultClosureEnvironment ();
    Enumeration < Identifier > e = super.symbols ();
    while ( e.hasMoreElements () )
    {
      final Identifier id = e.nextElement ();
      cl.put ( id, this.get ( id ) );
    }
    return cl;
  }
  
  private String name;
}
