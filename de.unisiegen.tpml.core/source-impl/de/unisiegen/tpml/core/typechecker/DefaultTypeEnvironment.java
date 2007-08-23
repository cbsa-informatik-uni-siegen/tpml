package de.unisiegen.tpml.core.typechecker ;


import java.util.Enumeration ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_ENVIRONMENT , 1 ,
        "[#1]" , "id1: tau1, ..., idn: taun" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
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
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( Mapping < Identifier , Type > mapping : this.mappings )
    {
      for ( LatexInstruction instruction : mapping.getSymbol ( )
          .getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
      for ( LatexInstruction instruction : mapping.getEntry ( )
          .getLatexInstructions ( ) )
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_ENVIRONMENT , pIndent ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.mappings.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.mappings.get ( i ).getSymbol ( )
          .toLatexStringBuilder ( pLatexStringBuilderFactory ,
              pIndent + LATEX_INDENT ) , 0 ) ;
      builder.addText ( LATEX_COLON ) ;
      builder.addText ( LATEX_SPACE ) ;
      builder.addBuilder ( this.mappings.get ( i ).getEntry ( )
          .toLatexStringBuilder ( pLatexStringBuilderFactory ,
              pIndent + LATEX_INDENT ) , 0 ) ;
      if ( i < this.mappings.size ( ) - 1 )
      {
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }
}
