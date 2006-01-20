package de.unisiegen.tpml.core.prettyprinter;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Factory class used to generate <code>PrettyString</code>s for
 * objects based on a given pretty print descriptor. 
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class PrettyStringFactory {
  /**
   * Returns the <code>PrettyStringFactory</code>, which pretty prints strings
   * according to the pretty printer descriptor (the rule set) contained within
   * the specified <code>resource</code>.
   * 
   * @param resource the resource identifier, looked up using the class loader
   *                 mechanism (<code>getResourceAsStream</code> in the
   *                 <code>PrettyStringFactory</code> class).
   * 
   * @return the <code>PrettyStringFactor</code> for the given <code>resource</code>.
   * 
   * @throws Exception if an error occurred, e.g. the <code>resource</code> is
   *                   not present or idenfies an invalid rule set.
   */
  public static synchronized PrettyStringFactory getFactoryForResource(String resource) throws Exception {
    // check if we already have a factory for that resource
    PrettyStringFactory factory = factories.get(resource);
    if (factory == null) {
      // open the specified resource as stream
      InputStream inputStream = PrettyStringFactory.class.getResourceAsStream(resource);
      
      // parse the rule set from the input stream
      PrettyRuleSet ruleSet = new PrettyRuleSet(inputStream);
      
      // allocate a factory for the rule set
      factory = new PrettyStringFactory(ruleSet);
      
      // add the factory to our internal map
      factories.put(resource, factory);
    }
    
    // and return the factory
    return factory;
  }
  
  /**
   * Returns the <code>PrettyString</code> for <code>object</code> using
   * the rule set associated with this <code>PrettyStringFactor</code>.
   * 
   * @param object the <code>Object</code> to pretty print.
   * 
   * @return the <code>PrettyString</code> for <code>object</code>.
   */
  public PrettyString getPrettyStringForObject(Object object) {
    // generate a matching builder and process the object
    PrettyStringBuilder builder = new PrettyStringBuilder(this.ruleSet);
    builder.appendObject(object, 0);
    return builder.toPrettyString();
  }
  
  // internal constructor
  private PrettyStringFactory(PrettyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
  }
  
  // member attributes
  private PrettyRuleSet ruleSet;
  
  // the number of factories already loaded per resource
  private static final HashMap<String, PrettyStringFactory> factories = new HashMap<String, PrettyStringFactory>();
}
