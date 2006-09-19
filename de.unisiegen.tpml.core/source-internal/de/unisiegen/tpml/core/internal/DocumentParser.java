package de.unisiegen.tpml.core.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Internal core helper interface, which provides the methods
 * required to parse (and optionally validate) XML documents
 * within the <code>de.unisiegen.tpml.core</code> package.
 * 
 * If the <code>DocumentParser</code> should also validate a the XML 
 * input source, it will first attempt to load the required DTDs from
 * the <code>de/unisiegen/tpml/core/internal/dtds</code> directory
 * (which is shipped with the distribution JAR file), and fallback
 * to loading the DTD from the system URI (usually a location on a
 * public webserver).
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.internal.DocumentParserFactory
 */
public interface DocumentParser {
  /**
   * Parse the content of the given <code>file</code> as an XML document and
   * return a new DOM {@link Document} object.
   * 
   * An {@link IllegalArgumentException} is thrown if the <code>File</code>
   * is <code>null</code>.
   * 
   * @param file the file containing the XML to parse.
   * 
   * @return a new DOM Document object.
   * 
   * @throws java.io.IOException if any IO error occurs.
   * @throws org.xml.sax.SAXException if any parse error occurs.
   */
  public Document parse(File file) throws IOException, SAXException;
  
  /**
   * Parse the content of the given <code>inputSource</code> as an XML
   * document and return a new DOM {@link Document} object.
   * 
   * An {@link IllegalArgumentException} is thrown if the <code>InputSource</code>
   * is <code>null</code>.
   * 
   * @param inputSource <code>InputSource</code> containing the content to be parsed.
   * 
   * @return a new DOM Document object.
   * 
   * @throws java.io.IOException if any IO error occurs.
   * @throws org.xml.sax.SAXException if any parse error occurs.   
   **/
  public Document parse(InputSource inputSource) throws IOException, SAXException;
  
  /**
   * Parse the content of the given <code>inputStream</code> as an XML document
   * and return a new DOM {@link Document} object.
   * 
   * An {@link IllegalArgumentException} is thrown if the <code>InputStream</code>
   * is <code>null</code>.
   * 
   * @param inputStream <code>InputStream</code> containing the content to be parsed.
   * 
   * @return a new DOM Document object.
   * 
   * @throws java.io.IOException if any IO error occurs.
   * @throws org.xml.sax.SAXException if any parse error occurs.   
   */
  public Document parse(InputStream inputStream) throws IOException, SAXException;
}
