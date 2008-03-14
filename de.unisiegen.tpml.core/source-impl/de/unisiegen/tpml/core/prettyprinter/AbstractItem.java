package de.unisiegen.tpml.core.prettyprinter;


import java.util.List;
import java.util.Map;


/**
 * Abstract base class for items in the
 * {@link de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder},
 * which are used to collect the information while generating the pretty string
 * content.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder
 */
abstract class AbstractItem
{

  //
  // Primitives
  //
  /**
   * @param buffer
   * @param annotations
   * @param breakOffsets
   * @param styles
   */
  abstract void determineString ( StringBuilder buffer,
      Map < PrettyPrintable, PrettyAnnotation > annotations,
      List < Integer > breakOffsets, PrettyStyle [] styles );


  /**
   * Returns the length of the string in characters required to serialize this
   * item to a pretty string.
   * 
   * @return the required string length.
   * @see #determineString(StringBuilder, Map, List, PrettyStyle[])
   */
  abstract int determineStringLength ();
}
