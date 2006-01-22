package de.unisiegen.tpml.core.prettyprinter;

import java.util.HashMap;

import org.w3c.dom.Node;

import de.unisiegen.tpml.core.util.IterableNodeList;

/**
 * Collection of {@link PrettyRule}s as parsed from a pretty printer
 * descriptor.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyRule
 */
final class PrettyRuleSet {
  /**
   * Allocates a new {@ PrettyRuleSet} and loads the {@link PrettyRule}s specified
   * below the <code>root</code> element node.
   *
   * @param root the root element of a pretty rule set descriptor.
   * 
   * @throws ClassNotFoundException if one of the required pretty printer classes is not
   *                                found during parsing. This is unlikely to happen, as
   *                                it represents a programming error, rather than a normal
   *                                runtime error condition. 
   */
  PrettyRuleSet(Node root) throws ClassNotFoundException {
    // allocate rules for all elements below the root
    for (Node node : new IterableNodeList(root)) {
      // determine the rule class based on the class attribute value (required)
      Class clazz = Class.forName(node.getAttributes().getNamedItem("class").getTextContent());
      
      // allocate a new rule for this node and associate it with the class
      this.rules.put(clazz, new PrettyRule(node));
    }
  }
  
  /**
   * Looks up a {@link PrettyRule} which can be used to pretty print
   * instances of the specified <code>clazz</code>. If a rule for <code>clazz</code>
   * (or any of its superclasses) is present within this rule set, this rule is
   * returned, else <code>null</code> will be returned.
   * 
   * @param clazz the {@link Class} for whose instances a matching
   *              {@link PrettyRule} should be looked up.
   * 
   * @return a matching {@link PrettyRule} for <code>clazz</code> (or any of
   *         its superclasses), or <code>null</code> if no such rule exists.
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
   * Simple wrapper method to {@link #lookupRuleForClass(Class)},
   * which determines the class of the given object <code>o</code>
   * and uses it for the rule lookup.
   * 
   * @param o the object for which to lookup a matching rule.
   * 
   * @return a matching {@link PrettyRule} or <code>null</code>
   *         if no rule is available for <code>o</code>.
   *
   * @see #lookupRuleForClass(Class)        
   */
  PrettyRule lookupRuleForObject(Object o) {
    return lookupRuleForClass(o.getClass());
  }

  // member attributes
  private HashMap<Class, PrettyRule> rules = new HashMap<Class, PrettyRule>();
}
