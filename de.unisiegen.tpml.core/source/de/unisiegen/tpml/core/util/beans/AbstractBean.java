package de.unisiegen.tpml.core.util.beans;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * Abstract base class for bean objects, which provide simple bean properties
 * with change listener support.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see Bean
 * @see java.beans.PropertyChangeListener
 * @see java.beans.PropertyChangeSupport
 */
public abstract class AbstractBean implements Bean
{

  /**
   * If any <code>PropertyChangeListeners</code> have been registered, the
   * <code>changeSupport</code> field describes them.
   * 
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #firePropertyChange(String, boolean, boolean)
   * @see #firePropertyChange(String, int, int)
   * @see #firePropertyChange(String, Object, Object)
   */
  protected PropertyChangeSupport changeSupport;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>AbstractBean</code> instance.
   */
  protected AbstractBean ()
  {
    // nothing to do here...
  }


  //
  // Listener registration
  //
  /**
   * Adds a {@link PropertyChangeListener} to the listener list. The listener is
   * registered for all bound properties of the derived class. If
   * <code>listener</code> is <code>null</code>, no exception is thrown and
   * no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be added.
   * @see #getPropertyChangeListeners()
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public synchronized void addPropertyChangeListener (
      PropertyChangeListener listener )
  {
    if ( listener == null )
    {
      return;
    }
    if ( this.changeSupport == null )
    {
      this.changeSupport = new PropertyChangeSupport ( this );
    }
    this.changeSupport.addPropertyChangeListener ( listener );
  }


  /**
   * Removes a {@link PropertyChangeListener} from the listener list. This
   * method should be used to remove PropertyChangeListeners that were
   * registered for all bound properties of this class. If <code>listener</code>
   * is <code>null</code>, no exception is thrown and no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be removed.
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #getPropertyChangeListeners()
   */
  public synchronized void removePropertyChangeListener (
      PropertyChangeListener listener )
  {
    if ( listener == null || this.changeSupport == null )
    {
      return;
    }
    this.changeSupport.removePropertyChangeListener ( listener );
  }


  /**
   * Returns an array of all the property change listeners registered on this
   * object.
   * 
   * @return all of this object's {@link PropertyChangeListener}s or an empty
   *         array if no property change listeners are currently registered.
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public synchronized PropertyChangeListener [] getPropertyChangeListeners ()
  {
    if ( this.changeSupport == null )
    {
      return new PropertyChangeListener [ 0 ];
    }
    return this.changeSupport.getPropertyChangeListeners ();
  }


  /**
   * Adds a {@link PropertyChangeListener} to the listener list for a specific
   * property. The specified property may be user-defined, or one of the
   * properties provided by the object. If <code>listener</code> is
   * <code>null</code>, no exception is thrown and no action is performed.
   * 
   * @param propertyName one of the property names of the object.
   * @param listener the {@link PropertyChangeListener} to be added.
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public synchronized void addPropertyChangeListener ( String propertyName,
      PropertyChangeListener listener )
  {
    if ( listener == null )
    {
      return;
    }
    if ( this.changeSupport == null )
    {
      this.changeSupport = new PropertyChangeSupport ( this );
    }
    this.changeSupport.addPropertyChangeListener ( propertyName, listener );
  }


  /**
   * Removes a {@link PropertyChangeListener} from the listener list for a
   * specific property. This method should be used to remove
   * {@link PropertyChangeListener}s that were registered for a specific bound
   * property. If <code>listener</code> is <code>null</code>, no exception
   * is thrown and no action is performed.
   * 
   * @param propertyName a valid property name.
   * @param listener the {@link PropertyChangeListener} to be removed.
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public synchronized void removePropertyChangeListener ( String propertyName,
      PropertyChangeListener listener )
  {
    if ( listener == null || this.changeSupport == null )
    {
      return;
    }
    this.changeSupport.removePropertyChangeListener ( propertyName, listener );
  }


  /**
   * Returns an array of all the listeners which have been associated with the
   * named property.
   * 
   * @param propertyName a valid property name.
   * @return all of the {@link PropertyChangeListener}s associated with the
   *         named property or an empty array if no listeners have been added
   */
  public synchronized PropertyChangeListener [] getPropertyChangeListeners (
      String propertyName )
  {
    if ( this.changeSupport == null )
    {
      return new PropertyChangeListener [ 0 ];
    }
    return this.changeSupport.getPropertyChangeListeners ( propertyName );
  }


  //
  // Listener invocation
  //
  /**
   * Support for reporting bound property changes for Object properties. This
   * method can be called when a bound property has changed and it will send the
   * appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   * 
   * @param propertyName the property whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange ( String propertyName, Object oldValue,
      Object newValue )
  {
    PropertyChangeSupport newChangeSupport = this.changeSupport;
    if ( newChangeSupport == null )
    {
      return;
    }
    newChangeSupport.firePropertyChange ( propertyName, oldValue, newValue );
  }


  /**
   * Support for reporting bound property changes for boolean properties. This
   * method can be called when a bound property has changed and it will send the
   * appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   * 
   * @param propertyName the propery whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange ( String propertyName, boolean oldValue,
      boolean newValue )
  {
    PropertyChangeSupport newChangeSupport = this.changeSupport;
    if ( newChangeSupport == null )
    {
      return;
    }
    newChangeSupport.firePropertyChange ( propertyName, oldValue, newValue );
  }


  /**
   * Support for reporting bound property changes for boolean properties. This
   * method can be called when a bound property has changed and it will send the
   * appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   * 
   * @param propertyName the propery whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange ( String propertyName, int oldValue,
      int newValue )
  {
    PropertyChangeSupport newChangeSupport = this.changeSupport;
    if ( newChangeSupport == null )
    {
      return;
    }
    newChangeSupport.firePropertyChange ( propertyName, oldValue, newValue );
  }
}
