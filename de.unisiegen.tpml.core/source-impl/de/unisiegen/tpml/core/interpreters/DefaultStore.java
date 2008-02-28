package de.unisiegen.tpml.core.interpreters;


import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.util.AbstractEnvironment;


/**
 * Default implementation of the <code>Store</code> interface, which is based
 * on the abstract {@link de.unisiegen.tpml.core.util.AbstractEnvironment}
 * class.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.Store
 * @see de.unisiegen.tpml.core.util.AbstractEnvironment
 */
public final class DefaultStore extends
    AbstractEnvironment < Location, Expression > implements Store
{

  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_STORE, 1, "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{[}#1\\color{" + LATEX_COLOR_NONE //$NON-NLS-1$
        + "}{]}", "X1: e1, ..., Xn: en" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ); //$NON-NLS-1$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.COLOR );
    return packages;
  }


  /**
   * Default constructor, creates a new store with no mappings.
   */
  public DefaultStore ()
  {
    super ();
  }


  /**
   * Allocates a new <code>DefaultStore</code>, based on the mappings from
   * the <code>store</code>.
   * 
   * @param store another <code>DefaultStore</code> whose mappings to inherit.
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   */
  public DefaultStore ( DefaultStore store )
  {
    super ( store );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.interpreters.Store#alloc()
   */
  public Location alloc ()
  {
    // try to find a new unique location
    for ( String suffix = "" ; ; suffix += "'" ) //$NON-NLS-1$//$NON-NLS-2$
    {
      for ( int n = 0 ; n < 26 ; ++n )
      {
        // generate the location base character
        char c = ( char ) ( 'A' + ( ( ( 'X' - 'A' ) + n ) % 26 ) );
        // try the location with this name
        Location location = new Location ( c + suffix );
        if ( !containsLocation ( location ) )
        {
          return location;
        }
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.interpreters.Store#containsLocation(de.unisiegen.tpml.core.expressions.Location)
   */
  public boolean containsLocation ( Location location )
  {
    return containsSymbol ( location );
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    for ( Mapping < Location, Expression > mapping : this.mappings )
    {
      commands.add ( mapping.getSymbol () );
      commands.add ( mapping.getEntry () );
    }
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    for ( Mapping < Location, Expression > mapping : this.mappings )
    {
      instructions.add ( mapping.getSymbol () );
      instructions.add ( mapping.getEntry () );
    }
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    for ( Mapping < Location, Expression > mapping : this.mappings )
    {
      packages.add ( mapping.getSymbol () );
      packages.add ( mapping.getEntry () );
    }
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.interpreters.Store#locations()
   */
  public Enumeration < Location > locations ()
  {
    return symbols ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.interpreters.Store#put(de.unisiegen.tpml.core.expressions.Location,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public void put ( Location location, Expression expression )
  {
    if ( !expression.isValue () )
    {
      throw new IllegalArgumentException ( "expression must be a value" ); //$NON-NLS-1$
    }
    super.put ( location, expression );
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    StringBuilder body = new StringBuilder ();
    body.append ( PRETTY_LBRACKET );
    for ( int i = 0 ; i < this.mappings.size () ; i++ )
    {
      body.append ( this.mappings.get ( i ).getSymbol ().toPrettyString ()
          .toString () );
      body.append ( PRETTY_COLON );
      body.append ( PRETTY_SPACE );
      body.append ( this.mappings.get ( i ).getEntry ().toPrettyString ()
          .toString () );
      if ( i != this.mappings.size () - 1 )
      {
        body.append ( PRETTY_COMMA );
        body.append ( PRETTY_SPACE );
      }
    }
    body.append ( PRETTY_RBRACKET );
    String descriptions[] = new String [ 2 + this.mappings.size () * 2 ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body.toString ();
    for ( int i = 0 ; i < this.mappings.size () ; i++ )
    {
      descriptions [ 2 + i * 2 ] = this.mappings.get ( i ).getSymbol ()
          .toPrettyString ().toString ();
      descriptions [ 3 + i * 2 ] = this.mappings.get ( i ).getEntry ()
          .toPrettyString ().toString ();
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_STORE, pIndent, descriptions );
    builder.addBuilderBegin ();
    for ( int i = 0 ; i < this.mappings.size () ; i++ )
    {
      builder.addBuilder ( this.mappings.get ( i ).getSymbol ()
          .toLatexStringBuilder ( pLatexStringBuilderFactory,
              pIndent + LATEX_INDENT * 2 ), 0 );
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT ) );
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_COLON );
      builder.addText ( LATEX_SPACE );
      builder.addText ( "}" ); //$NON-NLS-1$
      builder.addBuilder ( this.mappings.get ( i ).getEntry ()
          .toLatexStringBuilder ( pLatexStringBuilderFactory,
              pIndent + LATEX_INDENT * 2 ), 0 );
      if ( i != this.mappings.size () - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) );
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_COMMA );
        builder.addText ( LATEX_SPACE );
        builder.addText ( "}" ); //$NON-NLS-1$
      }
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
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
        this, 0 );
    builder.addText ( PRETTY_LBRACKET );
    for ( int i = 0 ; i < this.mappings.size () ; i++ )
    {
      builder.addBuilder ( this.mappings.get ( i ).getSymbol ()
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
      builder.addText ( PRETTY_COLON );
      builder.addText ( PRETTY_SPACE );
      builder.addBuilder ( this.mappings.get ( i ).getEntry ()
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
      if ( i != this.mappings.size () - 1 )
      {
        builder.addText ( PRETTY_COMMA );
        builder.addText ( PRETTY_SPACE );
      }
    }
    builder.addText ( PRETTY_RBRACKET );
    return builder;
  }


  /**
   * Returns the string representation for this type equation. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type equation.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
