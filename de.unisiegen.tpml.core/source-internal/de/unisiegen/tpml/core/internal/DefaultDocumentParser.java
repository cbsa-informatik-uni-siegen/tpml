package de.unisiegen.tpml.core.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Default implementation of the <code>DocumentParser</code> interface,
 * which will be returned from the <code>DocumentParserFactory</code>
 * methods.
 *
 * @author Benedikt Meurer
 * @version $Id:DefaultDocumentParser.java 244 2006-08-18 14:31:01Z benny $
 * 
 * @see de.unisiegen.tpml.core.internal.DocumentParser
 * @see de.unisiegen.tpml.core.internal.DocumentParserFactory
 */
final class DefaultDocumentParser implements DocumentParser {
  /**
   * Allocates a new <code>DefaultDocumentParser</code> instance,
   * which can be used to translate XML sources to DOM documents.
   * 
   * @param builder the <code>DocumentBuilder</code> that should
   *                be used to parse the XML sources.
   */
  DefaultDocumentParser(DocumentBuilder builder) {
    this.builder = builder;
  }
  
  /**
   * @see de.unisiegen.tpml.core.internal.DocumentParser#parse(java.io.File)
   */
  public Document parse(File file) throws IOException, SAXException {
    return this.builder.parse(file);
  }

  /**
   * @see de.unisiegen.tpml.core.internal.DocumentParser#parse(org.xml.sax.InputSource)
   */
  public Document parse(InputSource inputSource) throws IOException, SAXException {
    return this.builder.parse(inputSource);
  }

  /**
   * @see de.unisiegen.tpml.core.internal.DocumentParser#parse(java.io.InputStream)
   */
  public Document parse(InputStream inputStream) throws IOException, SAXException {
    return this.builder.parse(inputStream);
  }

  private DocumentBuilder builder;
}
