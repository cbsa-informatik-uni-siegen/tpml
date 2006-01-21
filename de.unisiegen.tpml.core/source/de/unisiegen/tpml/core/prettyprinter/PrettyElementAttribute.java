package de.unisiegen.tpml.core.prettyprinter;

import java.lang.reflect.Method;

import org.w3c.dom.Node;

import de.unisiegen.tpml.core.util.StringUtilities;

/**
 * This is about the most advanced <code>PrettyElement</code>, which determines
 * the value of a specified attribute of a given <code>Object</code> and formats
 * this attribute value using the <code>appendObject</code> method of the
 * <code>PrettyStringBuilder</code> class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement
 */
final class PrettyElementAttribute extends PrettyElement {
  /**
   * Allocates a new <code>PrettyElementAttribute</code> instance
   * based on the XML <code>node</code> of the rule descriptor.
   * 
   * @param node the XML node used for the construction.
   */
  public PrettyElementAttribute(Node node) {
    // determine the value of the priority attribute 
    this.priority = Integer.valueOf(node.getAttributes().getNamedItem("priority").getTextContent());
    
    // determine the getter method name from the attribute name (node content)
    this.getterMethodName = "get" + StringUtilities.toCamelCase(node.getTextContent());
  }
  
  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement#appendObjectToBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder, java.lang.Object)
   */
  @Override
  void appendObjectToBuilder(PrettyStringBuilder builder, Object object) {
    try {
      // determine the getter method for the attribute based on the name
      Method method = object.getClass().getMethod(this.getterMethodName);
      
      // invoke the getter method to determine the attribute value
      Object value = method.invoke(object);
      
      // let the string builder process the attribute value
      builder.appendObject(value, this.priority);
    }
    catch (RuntimeException e) {
      // just rethrow the exception
      throw e;
    }
    catch (Exception e) {
      // construct a new runtime exception from the exception
      throw new RuntimeException(e);
    }
  }

  // member attributes
  private String getterMethodName;
  private int priority;
}
