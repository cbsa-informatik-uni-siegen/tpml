package de.unisiegen.tpml.core.minimaltyping ;


import java.util.Enumeration ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Identifier ;
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
}