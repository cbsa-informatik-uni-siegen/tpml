package de.unisiegen.tpml.core.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Internal implementation of the <code>EntityResolver</code> interface,
 * which is used for <code>DocumentParser</code>s returned from the
 * <code>DocumentParserFactory</code> to lookup DTDs locally first.
 * This way users won't need to be connected to the internet to
 * validate XML documents.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.internal.DocumentParser
 * @see de.unisiegen.tpml.core.internal.DocumentParserFactory
 */
final class DefaultEntityResolver implements EntityResolver {
  /**
   * Resolves the entity for <code>publicId</code> and <code>systemId</code> trying
   * a local copy first.
   * 
   * @param publicId the public resource id.
   * @param systemId the system resource id.
   * 
   * @return an <code>InputSource</code> if a local copy of the requested entity
   *         is found, else <code>null</code> to fall back to the default entity
   *         resolver logic.
   *         
   * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
   */
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    // determine the basename of the URL
    URL url = new URL(systemId);
    File file = new File(url.getPath());
    String baseName = file.getName();
    
    // check if we have a copy of that DTD
    InputStream inputStream = this.getClass().getResourceAsStream("dtds/" + baseName);
    if (inputStream != null)
      return new InputSource(inputStream);

    // fallback to the default resolver
    return null;
  }
}
