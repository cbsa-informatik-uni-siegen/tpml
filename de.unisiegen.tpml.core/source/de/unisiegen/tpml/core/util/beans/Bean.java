package de.unisiegen.tpml.core.util.beans;

import java.beans.PropertyChangeListener;

/**
 * Common interface for beans with simple property change listener support. While not mandatory this
 * interface should be implemented by all classes whose instances act as Java Beans, either by implementing
 * this interface directly, or by deriving from the {@link de.unisiegen.tpml.core.beans.AbstractBean}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.beans.AbstractBean
 * @see java.beans.PropertyChangeListener
 * @see java.beans.PropertyChangeSupport
 */
public interface Bean {
  /**
   * Adds a {@link PropertyChangeListener} to the listener list. The listener
   * is registered for all bound properties of the derived class.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown
   * and no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be added.
   * 
   * @see #getPropertyChangeListeners()
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a {@link PropertyChangeListener} from the listener list. This method
   * should be used to remove PropertyChangeListeners that were registered for all
   * bound properties of this class.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown and
   * no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be removed.
   * 
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #getPropertyChangeListeners()
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);
  
  /**
   * Returns an array of all the property change listeners registered
   * on this object.
   * 
   * @return all of this object's {@link PropertyChangeListener}s or an
   *         empty array if no property change listeners are currently
   *         registered.
   *
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public PropertyChangeListener[] getPropertyChangeListeners();

  /**
   * Adds a {@link PropertyChangeListener} to the listener list for a specific
   * property. The specified property may be user-defined, or one of the properties
   * provided by the object.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown and
   * no action is performed.
   * 
   * @param propertyName one of the property names of the object.
   * @param listener the {@link PropertyChangeListener} to be added.
   * 
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
  
  /**
   * Removes a {@link PropertyChangeListener} from the listener list for a specific
   * property. This method should be used to remove {@link PropertyChangeListener}s
   * that were registered for a specific bound property.
   *
   * If <code>listener</code> is <code>null</code>, no exception is thrown and no
   * action is performed.
   * 
   * @param propertyName a valid property name.
   * @param listener the {@link PropertyChangeListener} to be removed.
   * 
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
  
  /**
   * Returns an array of all the listeners which have been associated 
   * with the named property.
   *
   * @param propertyName a valid property name.
   * 
   * @return all of the {@link PropertyChangeListeners} associated with
   *         the named property or an empty array if no listeners have 
   *         been added
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);
}
