package de.unisiegen.tpml.graphics;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 */
public class Messages {
  private static final String BUNDLE_NAME = "de.unisiegen.tpml.graphics.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle(BUNDLE_NAME);

  private Messages() {
  }

  public static String getString(String key) {
    // TODO Auto-generated method stub
    try {
      return RESOURCE_BUNDLE.getString(key);
    }
    catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
