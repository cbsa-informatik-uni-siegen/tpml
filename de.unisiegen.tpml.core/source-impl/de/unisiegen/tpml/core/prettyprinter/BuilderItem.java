package de.unisiegen.tpml.core.prettyprinter;


import java.util.List;
import java.util.Map;


/**
 * This class represents a pretty string builder that is added to another pretty
 * string builder.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder
 */
final class BuilderItem extends AbstractItem
{

  //
  // Attributes
  //
  /**
   * The pretty string builder associated with this item.
   */
  private DefaultPrettyStringBuilder builder;


  //
  // Constructor (package)
  //
  /**
   * Allocates a new <code>BuilderItem</code> for the specified
   * <code>builder</code>.
   * 
   * @param pBuilder the pretty string builder for which to allocate a builder
   *          item.
   * @throws NullPointerException if <code>builder</code> is <code>null</code>.
   */
  BuilderItem ( DefaultPrettyStringBuilder pBuilder )
  {
    if ( pBuilder == null )
    {
      throw new NullPointerException ( "builder is null" ); //$NON-NLS-1$
    }
    this.builder = pBuilder;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineString(java.lang.StringBuilder,
   *      java.util.Map, java.util.List,
   *      de.unisiegen.tpml.core.prettyprinter.PrettyStyle[])
   */
  @Override
  void determineString ( StringBuilder buffer,
      Map < PrettyPrintable, PrettyAnnotation > annotations,
      @SuppressWarnings ( "unused" )
      List < Integer > breakOffsets, PrettyStyle [] styles )
  {
    this.builder.determineString ( buffer, annotations, styles );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineStringLength()
   */
  @Override
  int determineStringLength ()
  {
    return this.builder.determineStringLength ();
  }
}
