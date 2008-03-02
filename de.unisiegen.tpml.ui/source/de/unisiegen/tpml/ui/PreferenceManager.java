package de.unisiegen.tpml.ui;


import java.awt.Frame;
import java.awt.Rectangle;
import java.io.File;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.JFrame;


/**
 * Manages the preferences for the user interface.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * @see java.util.prefs.Preferences
 */
public class PreferenceManager
{

  //
  // Attributes
  //

  /**
   * TODO
   */
  private static PreferenceManager preferenceManager;


  /**
   * TODO
   */
  private static int defaultWidth = 640;


  /**
   * TODO
   */
  private static int defaultHeight = 480;


  /**
   * TODO
   */
  private static int defaultPosition = 0;


  /**
   * The {@link Preferences} object for the node where the settings are stored
   * and loaded.
   * 
   * @see Preferences
   */
  private Preferences prefs;


  //
  // Constructor
  //

  /**
   * Allocates a new <code>PreferencesManager</code>.
   */
  private PreferenceManager ()
  {
    this.prefs = Preferences.userNodeForPackage ( this.getClass () );
  }


  //
  // Primitives
  //

  /**
   * TODO Add documentation here.
   * 
   * @param list
   */
  public void setRecentlyUsed ( LinkedList < HistoryItem > list )
  {
    int length = list.size ();
    for ( int i = 0 ; i < length ; i++ )
    {
      this.prefs.put ( "historyitem" + i, list.get ( i ).getFile () //$NON-NLS-1$
          .getAbsolutePath () );
    }

  }


  /**
   * TODO Add documentation here.
   * 
   * @return TODO
   */
  public LinkedList < HistoryItem > getRecentlyUsed ()
  {
    int count = 0;
    String result = ""; //$NON-NLS-1$
    LinkedList < HistoryItem > list = new LinkedList < HistoryItem > ();
    while ( true )
    {
      result = this.prefs.get ( "historyitem" + count, "end" ); //$NON-NLS-1$//$NON-NLS-2$
      if ( result.equals ( "end" ) ) //$NON-NLS-1$
      {
        break;
      }

      list.add ( new HistoryItem ( new File ( result ) ) );
      count++ ;
    }
    return list;
  }


  /**
   * TODO
   * 
   * @param list
   */
  public void setOpenFiles ( LinkedList < File > list )
  {
    // delete all openitems
    int delete = 0;
    while ( !this.prefs.get ( "openitem" + delete, "end" ).equals ( "end" ) ) //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    {
      this.prefs.remove ( "openitem" + delete ); //$NON-NLS-1$
      delete++ ;
    }
    // insert new items
    int length = list.size ();
    for ( int i = 0 ; i < length ; i++ )
    {
      this.prefs.put ( "openitem" + i, list.get ( i ).getAbsolutePath () ); //$NON-NLS-1$
    }
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public LinkedList < File > getOpenFiles ()
  {
    int count = 0;
    String result = ""; //$NON-NLS-1$
    LinkedList < File > list = new LinkedList < File > ();
    while ( true )
    {
      result = this.prefs.get ( "openitem" + count, "end" ); //$NON-NLS-1$ //$NON-NLS-2$
      if ( result.equals ( "end" ) ) //$NON-NLS-1$
      {
        break;
      }

      list.add ( new File ( result ) );
      count++ ;
    }
    return list;
  }


  /**
   * TODO Add documentation here.
   * 
   * @param status
   */
  public void setAdvanced ( boolean status )
  {
    this.prefs.putBoolean ( "advanced", status ); //$NON-NLS-1$
  }


  /**
   * TODO Add documentation here.
   * 
   * @return TODO
   */
  public boolean getAdvanced ()
  {
    return this.prefs.getBoolean ( "advanced", false ); //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @param path
   */
  public void setWorkingPath ( String path )
  {
    this.prefs.put ( "openPath", path ); //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public String getWorkingPath ()
  {
    return this.prefs.get ( "openPath", null ); //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @param frame
   */
  public void setWindowPreferences ( JFrame frame )
  {

    if ( ( frame.getExtendedState () & Frame.MAXIMIZED_BOTH ) != 0 )
    {
      this.prefs.putBoolean ( "maximized", true ); //$NON-NLS-1$
    }
    else
    {
      this.prefs.putBoolean ( "maximized", false ); //$NON-NLS-1$
      Rectangle bounds = frame.getBounds ();
      this.prefs.putInt ( "xPosition", bounds.x );//$NON-NLS-1$
      this.prefs.putInt ( "yPosition", bounds.y );//$NON-NLS-1$
      this.prefs.putInt ( "width", bounds.width );//$NON-NLS-1$
      this.prefs.putInt ( "height", bounds.height );//$NON-NLS-1$
    }
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Rectangle getWindowBounds ()
  {
    int x = this.prefs.getInt ( "xPosition", PreferenceManager.defaultPosition );//$NON-NLS-1$
    int y = this.prefs.getInt ( "yPosition", PreferenceManager.defaultPosition );//$NON-NLS-1$
    int width = this.prefs.getInt ( "width", PreferenceManager.defaultWidth );//$NON-NLS-1$
    int height = this.prefs.getInt ( "height", PreferenceManager.defaultHeight );//$NON-NLS-1$
    return new Rectangle ( x, y, width, height );
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean getWindowMaximized ()
  {
    return this.prefs.getBoolean ( "maximized", false );//$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public static PreferenceManager get ()
  {
    if ( PreferenceManager.preferenceManager == null )
    {
      PreferenceManager.preferenceManager = new PreferenceManager ();
    }
    return PreferenceManager.preferenceManager;
  }
}
