package de.unisiegen.tpml.core.languages;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory class for {@link de.unisiegen.tpml.core.languages.Language}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 */
public final class LanguageFactory {
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>LanguageFactory</code>.
   */
  private LanguageFactory() {
    // nothing to do here...
  }
  
  

  //
  // Factory instantiation
  //
  
  /**
   * Returns a new <code>LanguageFactory</code> instance.
   * 
   * @return a newly allocated <code>LanguageFactory</code>.
   */
  public static LanguageFactory newInstance() {
    return new LanguageFactory();
  }
  
  
  
  //
  // Language management
  //
  
  /**
   * Returns the available languages for this <code>LanguageFactory</code> as array of {@link Language} objects.
   * 
   * @return an array with all available {@link Language}s.
   */
  public Language[] getAvailableLanguages() {
    return new Language[0];
  }
  
  /**
   * Returns the {@link Language} with the specified <code>id</code>.
   * 
   * @param id the unique identifier of the {@link Language} to return, for example <code>"l1"</code>.
   * 
   * @throws NoSuchLanguageException if the <code>id</code> does not refer to a valid language.
   */
  public Language getLanguageById(String id) throws NoSuchLanguageException {
    try {
      // determine the class name for the language class
      String clazzName = getClass().getPackage().getName() + "." + id.toLowerCase() + "." + id.toUpperCase() + "Language";

      // determine the language class
      Class clazz = Class.forName(clazzName);

      // instantiate the language class
      return (Language)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
    }
    catch (ClassNotFoundException e) {
      throw new NoSuchLanguageException(id, e);
    }
    catch (IllegalAccessException e) {
      throw new NoSuchLanguageException(id, e);
    }
    catch (InstantiationException e) {
      throw new NoSuchLanguageException(id, e);
    }
    catch (InvocationTargetException e) {
      throw new NoSuchLanguageException(id, e);
    }
    catch (NoSuchMethodException e) {
      throw new NoSuchLanguageException(id, e);
    }
  }
}
