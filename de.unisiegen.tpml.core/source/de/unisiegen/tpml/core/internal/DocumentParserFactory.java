package de.unisiegen.tpml.core.internal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Factory class for <code>DocumentParser</code>s, which are used
 * to parse XML documents used for the datastructures and dataflow
 * within the <code>de.unisiegen.tpml.core</code> package.
 * 
 * The <code>DocumentParser</code>s created by this factory class
 * will try to use local copies of DTDs if available, so users
 * don't need access to the internet to run the applications
 * based on the <code>de.unisiegen.tpml.core</code> package.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.internal.DocumentParser
 */
public final class DocumentParserFactory {
  /**
   * Returns a <code>DocumentParserFactory</code> instance,
   * which can be used to obtain <code>DocumentParser</code>s
   * for generating DOM documents from XML sources.
   * 
   * Despite the name <code>newInstance()</code>, the returned
   * instance is not garantied to be a new parser factory, but
   * may also be a cached parser factory. In either case, the
   * usage of this class is thread-safe, so you can treat the
   * result as if it would be a new instance.
   * 
   * @return a <code>DocumentParserFactory</code> instance.
   */
  public static DocumentParserFactory newInstance() {
    return new DocumentParserFactory();
  }
  
  /**
   * Returns a new <code>DocumentParser</code>, that doesn't validate
   * the XML sources upon parsing. Use {@link #newValidatingParser()}
   * if you need to validate the XML input, which is highly recommended.
   *    
   * @return a new <code>DocumentParser</code>, that doesn't validate
   *         it's XML input.
   *         
   * @throws ParserConfigurationException if a <code>DocumentParser</code>
   *                                      cannot be created which satisfies
   *                                      the configuration requested. 
   * 
   * @see #newValidatingParser()
   */
  public DocumentParser newParser() throws ParserConfigurationException {
    return createDocumentParser(false);
  }
  
  /**
   * 
   * @return
   * 
   * @throws ParserConfigurationException if a <code>DocumentParser</code>
   *                                      cannot be created which satisfies
   *                                      the configuration requested. 
   * 
   * @see #newParser()
   */
  public DocumentParser newValidatingParser() throws ParserConfigurationException {
    return createDocumentParser(true);
  }

  /**
   * Creates a new <code>DocumentParser</code> for this factory. The
   * returned parser will validate its input if <code>validating</code>
   * is <code>true</code>.
   * 
   * @param validating whether the returned parser will validate its input.
   * 
   * @return the new <code>DocumentParser</code>.
   * 
   * @throws ParserConfigurationException if a <code>DocumentParser</code>
   *                                      cannot be created which satisfies
   *                                      the configuration requested. 
   */
  protected DocumentParser createDocumentParser(boolean validating) throws ParserConfigurationException {
    // allocate and configure a document builder factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setCoalescing(true);                              // expand CDATA elements to text nodes
    factory.setIgnoringComments(true);                        // ignore comments
    factory.setIgnoringElementContentWhitespace(validating);  // ignore whitespace when validating
    factory.setValidating(validating);
    
    // allocate and configure a document builder for that factory
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(new DefaultEntityResolver());
    
    // allocate a parser for that builder
    return new DefaultDocumentParser(builder);
  }
  
  // hidden constructor
  private DocumentParserFactory() {
  }
}
