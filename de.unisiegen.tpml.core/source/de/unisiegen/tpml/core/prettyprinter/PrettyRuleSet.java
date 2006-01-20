package de.unisiegen.tpml.core.prettyprinter;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Collection of <code>PrettyRule</code>s as parsed from a pretty printer
 * descriptor.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class PrettyRuleSet {
  /**
   * Allocates a new <code>PrettyRuleSet</code> and loads the <code>PrettyRule</code>s
   * specified by the XML source connected to the <code>inputStream</code>.
   * 
   * @param inputStream the <code>InputStream</code> conntected with the
   *                    XML source (the ruleset descriptor).
   * 
   * @throws Exception if an error occurs while parsing the
   *                   <code>inputStream</code> contents.
   */
  PrettyRuleSet(InputStream inputStream) throws Exception {
    // allocate a new document builder factory and set it to validate
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    
    // allocate a builder from that factory
    DocumentBuilder builder = factory.newDocumentBuilder();
    
    // parse the document identified by the input stream
    Document document = builder.parse(inputStream);
    
    // determine the root node
    Element root = document.getDocumentElement();
    
    // allocate rules for all elements below the root
    NodeList childNodes = root.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); ++i) {
      // determine the child node and verify that we have an element here
      Node node = childNodes.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE)
        continue;
      
      // determine the rule class based on the class attribute value (required)
      Class clazz = Class.forName(node.getAttributes().getNamedItem("class").getTextContent());
      
      // allocate a new rule for this node and associate it with the class
      this.rules.put(clazz, new PrettyRule(node));
    }
  }
  
  /**
   * Looks up a <code>PrettyRule</code> which can be used to pretty print
   * instances of the specified <code>clazz</code>. If a rule for <code>clazz</code>
   * (or any of its superclasses) is present within this rule set, this rule is
   * returned, else <code>null</code> will be returned.
   * 
   * @param clazz the <code>Class</code> for whose instances a matching
   *              <code>PrettyRule</code> should be looked up.
   * 
   * @return a matching <code>PrettyRule</code> for <code>clazz</code> (or any
   *         of its superclasses), or <code>null</code> if no such rule exists.
   * 
   * @see #lookupRuleForObject(Object)
   */
  PrettyRule lookupRuleForClass(Class clazz) {
    PrettyRule rule = null;
    for (; rule == null && clazz != null; clazz = clazz.getSuperclass())
      rule = this.rules.get(clazz);
    return rule;
  }
  
  /**
   * Simple wrapper method to <code>lookupRuleForClass</code>,
   * which determines the class of the given object <code>o</code>
   * and uses it for the rule lookup.
   * 
   * @param o the object for which to lookup a matching rule.
   * 
   * @return a matching <code>PrettyRule</code> or <code>null</code>
   *         if no rule is available for <code>o</code>.
   *
   * @see #lookupRuleForClass(Class)        
   */
  PrettyRule lookupRuleForObject(Object o) {
    return lookupRuleForClass(o.getClass());
  }

  private HashMap<Class, PrettyRule> rules = new HashMap<Class, PrettyRule>();
}
