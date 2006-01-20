package de.unisiegen.tpml.core.prettyprinter;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.unisiegen.tpml.core.util.StringUtilities;

/**
 * Represents a pretty printer rule, as parsed from a pretty printer
 * rule descriptor file. Each <code>PrettyRule</code> consists of
 * zero or more <code>PrettyElement</code>s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyRuleSet
 */
final class PrettyRule {
  /**
   * Allocates a new <code>PrettyRule</code> from the contents of the
   * <tt>&lt;rule&gt;</tt> element identified by the <code>node</code>
   * argument.
   * 
   * @param node the XML node for a <tt>&lt;pretty-rule&gt;</tt> element.
   */
  PrettyRule(Node node) {
    // determine the value of the priority attribute 
    this.priority = Integer.valueOf(node.getAttributes().getNamedItem("priority").getTextContent());

    // add elements for all child nodes
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); ++i) {
      try {
        // determine the child node and verify that we have an element
        Node childNode = childNodes.item(i);
        if (childNode.getNodeType() != Node.ELEMENT_NODE)
          continue;
      
        // determine the element class name based on the node name
        String clazzName = PrettyElement.class.getCanonicalName() + StringUtilities.toCamelCase(childNode.getNodeName());
      
        // determine the element class based on the name
        Class clazz = Class.forName(clazzName);

        // determine the constructor that takes a node
        Constructor constructor = clazz.getConstructor(Node.class);
        
        // add an instance of the element class based on the constructor and the node
        this.elements.add((PrettyElement)constructor.newInstance(childNode));
      }
      catch (RuntimeException e) {
        // just rethrow that exception
        throw e;
      }
      catch (Exception e) {
        // transform to a runtime exception
        throw new RuntimeException(e);
      }
    }
  }
  
  /**
   * Appends the <code>object</code> to the <code>builder</code> according
   * to the specification under which this rule was constructed.
   * 
   * @param builder the pretty string builder to which the elements
   *                of this rule should be appended.
   * @param object the object which should be appended to the <code>builder</code>
   *               using the specification of this rule.
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#appendObject(Object, int)               
   */
  void appendObjectToBuilder(PrettyStringBuilder builder, Object object) {
    // process all elements for this rule
    for (PrettyElement element : this.elements)
      element.appendObjectToBuilder(builder, object);
  }
  
  /**
   * Returns the priority of the rule.
   * 
   * @return the priority of the rule.
   */
  int getPriority() {
    return this.priority;
  }
  
  private int priority;
  private LinkedList<PrettyElement> elements = new LinkedList<PrettyElement>();
}
