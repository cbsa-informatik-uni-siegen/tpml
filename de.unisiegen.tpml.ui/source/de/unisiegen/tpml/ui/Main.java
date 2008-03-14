package de.unisiegen.tpml.ui;


import java.io.File;

import javax.swing.UIManager;


/**
 * The main starter class for the TPML project.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.ui.MainWindow
 */
public final class Main
{

  /**
   * The main entry point for the TPML project, which allocates and runs a new
   * {@link MainWindow}. This method also sets up native look and feel for the
   * platform if possible.
   * 
   * @param args the command line arguments.
   * @see MainWindow
   */
  public static void main ( final String args[] )
  {
    try
    {
      /*
       * try to setup native look and feel for the platform if the native look
       * and feel is not GTKLookAndFeel.
       */
      String nativeLAF = UIManager.getSystemLookAndFeelClassName ();
      if ( nativeLAF.contains ( "GTK" ) ) //$NON-NLS-1$
      {
        UIManager.setLookAndFeel ( "javax.swing.plaf.metal.MetalLookAndFeel" ); //$NON-NLS-1$
      }
      else
      {
        UIManager.setLookAndFeel ( UIManager.getSystemLookAndFeelClassName () );
      }
    }
    catch ( Exception e )
    {
      // just ignore the exception here
    }
    // run a new MainWindow
    java.awt.EventQueue.invokeLater ( new Runnable ()
    {

      public void run ()
      {
        // allocate the main window
        MainWindow window = new MainWindow ();
        // check if any files are specified
        if ( args.length > 0 )
        {
          // open any specified files
          for ( String fileName : args )
          {
            File file = new File ( fileName );
            window.openFile ( file );
          }
        }
        else
        {
          // restore the files from the previous session
          window.restoreOpenFiles ();
        }
      }
    } );
  }
}
