package de.unisiegen.tpml.core.languages;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import de.unisiegen.tpml.core.languages.l0.L0Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.lt1.Lt1Language;

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
    return new Language[] {
        new L0Language(),
        new L1Language(),
        new L2Language(),
        new Lt1Language(),
    };
  }
  
  /**
   * Returns the {@link Language} for the specified <code>file</code>. I.e. if the <code>file</code>
   * is <tt>"sample.l0"</tt> the language <code>L0</code> will be returned.
   * 
   * @param file the file for which to return the {@link Language}.
   * 
   * @return the language for the given <code>fileName</code>.
   * 
   * @throws NoSuchLanguageException if the <code>file</code> is not recognized.
   * @throws NullPointerException if <code>file</code> is <code>null</code>.
   */
  public Language getLanguageByFile(File file) throws NoSuchLanguageException {
    String[] components = file.getName().split("\\.");
    return getLanguageById(components[components.length - 1]);
  }
  
  /**
   * Returns the {@link Language} with the specified <code>id</code>.
   * 
   * @param id the unique identifier of the {@link Language} to return, for example <code>"l1"</code>.
   * 
   * @return the language with the specified <code>id</code>.
   * 
   * @throws NoSuchLanguageException if the <code>id</code> does not refer to a valid language.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   */
  public Language getLanguageById(String id) throws NoSuchLanguageException {
    try {
      // determine the class name for the language class
      String clazzName = getClass().getPackage().getName() + "." + id.toLowerCase() + "." + id.substring(0, 1).toUpperCase() + id.substring(1) + "Language";

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
