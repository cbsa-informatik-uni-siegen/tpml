package de.unisiegen.tpml.core.prettyprinter;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.unisiegen.tpml.core.internal.DocumentParser;
import de.unisiegen.tpml.core.internal.DocumentParserFactory;

/**
 * Factory class used to generate {@link PrettyString}s for
 * objects based on a given pretty print descriptor. The format
 * for pretty print descriptors is determined by the file
 * <tt>pretty-rule-set_1.0.dtd</tt>. A local copy of the DTD
 * can be found in <tt>/de/unisiegen/tpml/core/internal/dtds</tt>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.PrettyString
 */
public final class PrettyStringFactory {
  /**
   * Allocates a new {@link PrettyStringFactory} that can be used to generate
   * {@link PrettyString}s based on the pretty print descriptor to which the
   * <code>inputStream</code> is connected.
   * 
   * @param inputStream an {@link InputStream} connected with an XML input source,
   *                    that contains a valid pretty print descriptor. 
   * 
   * @return the {@link PrettyStringFactory} for <code>inputStream</code>.
   * 
   * @throws ClassNotFoundException if a required pretty printer class is not found while parsing
   *                                the XML document connected with the <code>inputStream</code>.
   * @throws IOException if an error occurs while reading data from the <code>inputStream</code>.
   * @throws NullPointerException if <code>inputStream</code> is <code>null</code>.
   * @throws ParserConfigurationException if a required feature is not provided by the XML
   *                                      parser available on the host.
   * @throws SAXException if parsing of the XML document fails.
   */
  public static PrettyStringFactory newInstance(InputStream inputStream) throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
    // allocate a document parser for the given input stream
    DocumentParserFactory factory = DocumentParserFactory.newInstance();
    DocumentParser parser = factory.newValidatingParser();
    
    // parse the XML input into a DOM document
    Document document = parser.parse(inputStream);
    
    // generate a rule set from the DOM document
    PrettyRuleSet ruleSet = new PrettyRuleSet(document.getDocumentElement());
    
    // generate a factory for that rule set
    return new PrettyStringFactory(ruleSet);
  }
  
  /**
   * Returns the {@link PrettyString} for <code>object</code> using
   * the rule set associated with this {@link PrettyStringFactor}.
   * 
   * @param object the {@link Object} to pretty print.
   * 
   * @return the {@link PrettyString} for <code>object</code>.
   * 
   * @throws NullPointerException if <code>object</code> is <code>null</code>.
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
}
