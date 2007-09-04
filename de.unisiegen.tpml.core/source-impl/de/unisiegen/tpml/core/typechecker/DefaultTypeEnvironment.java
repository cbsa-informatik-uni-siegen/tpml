package de.unisiegen.tpml.core.typechecker ;


import java.util.Enumeration ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.PolyType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.util.AbstractEnvironment ;


/**
 * Default implementation of the <code>TypeEnvironment</code> interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:511M $
 * @see TypeEnvironment
 * @see AbstractEnvironment
 */
public final class DefaultTypeEnvironment extends
    AbstractEnvironment < Identifier , Type > implements TypeEnvironment
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_ENVIRONMENT , 1 ,
        "\\color{" + LATEX_COLOR_NONE + "}{[}#1\\color{" //$NON-NLS-1$ //$NON-NLS-2$
            + LATEX_COLOR_NONE + "}{]}" , "id1: tau1, ..., idn: taun" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
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
  public static TreeSet < LatexPackage > getLatexPackagesStatic ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    return packages ;
  }


  /**
   * Allocates a new empty <code>DefaultTypeEnvironment</code>.
   * 
   * @see AbstractEnvironment#AbstractEnvironment()
   */
  public DefaultTypeEnvironment ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new <code>DefaultTypeEnvironment</code> based on the
   * <code>environment</code>.
   * 
   * @param environment the type environment from which to copy the mappings.
   * @throws NullPointerException if <code>environment</code> is
   *           <code>null</code>.
   * @see AbstractEnvironment#AbstractEnvironment(AbstractEnvironment)
   */
  DefaultTypeEnvironment ( DefaultTypeEnvironment environment )
  {
    super ( environment ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#closure(MonoType)
   */
  public PolyType closure ( MonoType tau )
  {
    // determine the quantified type variables
    TreeSet < TypeVariable > quantifiedVariables = new TreeSet < TypeVariable > ( ) ;
    quantifiedVariables.addAll ( tau.getTypeVariablesFree ( ) ) ;
    quantifiedVariables.removeAll ( free ( ) ) ;
    // allocate the polymorphic type
    return new PolyType ( quantifiedVariables , tau ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#containsIdentifier(Identifier)
   */
  public boolean containsIdentifier ( Identifier identifier )
  {
    return containsSymbol ( identifier ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#extend(Identifier, Type)
   */
  public TypeEnvironment extend ( Identifier identifier , Type type )
  {
    DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( this ) ;
    environment.put ( identifier , type ) ;
    return environment ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#free()
   */
  public Set < TypeVariable > free ( )
  {
    TreeSet < TypeVariable > free = new TreeSet < TypeVariable > ( ) ;
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      free.addAll ( mapping.getEntry ( ).getTypeVariablesFree ( ) ) ;
    }
    return free ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      for ( LatexCommand command : mapping.getSymbol ( ).getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
      for ( LatexCommand command : mapping.getEntry ( ).getLatexCommands ( ) )
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
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      instructions.add ( mapping.getSymbol ( ) ) ;
      instructions.add ( mapping.getEntry ( ) ) ;
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
    for ( LatexPackage pack : getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      for ( LatexPackage pack : mapping.getSymbol ( ).getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
      for ( LatexPackage pack : mapping.getEntry ( ).getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#identifiers()
   */
  public Enumeration < Identifier > identifiers ( )
  {
    return symbols ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#star()
   */
  public TypeEnvironment star ( )
  {
    DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      Identifier id = mapping.getSymbol ( ) ;
      if ( ( ! id.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) )
          && ( ! id.getSet ( ).equals ( Identifier.Set.SELF ) ) )
      {
        environment.put ( id , mapping.getEntry ( ) ) ;
      }
    }
    return environment ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEnvironment#substitute(TypeSubstitution)
   */
  public TypeEnvironment substitute ( TypeSubstitution s )
  {
    // create a new environment with the (possibly) new types
    DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      Type newType = mapping.getEntry ( ).substitute ( s ) ;
      if ( ! newType.equals ( mapping.getEntry ( ) ) )
      {
        environment.mappings.add ( new Mapping < Identifier , Type > ( mapping
            .getSymbol ( ) , newType ) ) ;
      }
      else
      {
        environment.mappings.add ( mapping ) ;
      }
    }
    return environment ;
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
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    StringBuilder body = new StringBuilder ( ) ;
    body.append ( PRETTY_LBRACKET ) ;
    for ( int i = 0 ; i < this.mappings.size ( ) ; i ++ )
    {
      body.append ( this.mappings.get ( i ).getSymbol ( ).toPrettyString ( )
          .toString ( ) ) ;
      body.append ( PRETTY_COLON ) ;
      body.append ( PRETTY_SPACE ) ;
      body.append ( this.mappings.get ( i ).getEntry ( ).toPrettyString ( )
          .toString ( ) ) ;
      if ( i != this.mappings.size ( ) - 1 )
      {
        body.append ( PRETTY_COMMA ) ;
        body.append ( PRETTY_SPACE ) ;
      }
    }
    body.append ( PRETTY_RBRACKET ) ;
    String descriptions[] = new String [ 2 + this.mappings.size ( ) * 2 ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    for ( int i = 0 ; i < this.mappings.size ( ) ; i ++ )
    {
      descriptions [ 2 + i * 2 ] = this.mappings.get ( i ).getSymbol ( )
          .toPrettyString ( ).toString ( ) ;
      descriptions [ 3 + i * 2 ] = this.mappings.get ( i ).getEntry ( )
          .toPrettyString ( ).toString ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_ENVIRONMENT , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.mappings.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.mappings.get ( i ).getSymbol ( )
          .toLatexStringBuilder ( pLatexStringBuilderFactory ,
              pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT ) ) ;
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_COLON ) ;
      builder.addText ( LATEX_SPACE ) ;
      builder.addText ( "}" ) ; //$NON-NLS-1$
      builder.addBuilder ( this.mappings.get ( i ).getEntry ( )
          .toLatexStringBuilder ( pLatexStringBuilderFactory ,
              pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      if ( i < this.mappings.size ( ) - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addText ( "}" ) ; //$NON-NLS-1$
      }
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
    builder.addText ( PRETTY_LBRACKET ) ;
    for ( int i = 0 ; i < this.mappings.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.mappings.get ( i ).getSymbol ( )
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      builder.addText ( PRETTY_COLON ) ;
      builder.addText ( PRETTY_SPACE ) ;
      builder.addBuilder ( this.mappings.get ( i ).getEntry ( )
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      if ( i != this.mappings.size ( ) - 1 )
      {
        builder.addText ( PRETTY_COMMA ) ;
        builder.addText ( PRETTY_SPACE ) ;
      }
    }
    builder.addText ( PRETTY_RBRACKET ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type environment. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type environment.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
