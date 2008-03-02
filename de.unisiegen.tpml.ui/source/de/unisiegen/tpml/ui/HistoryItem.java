package de.unisiegen.tpml.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;

import org.apache.log4j.Logger;


/**
 * TODO
 */
public class HistoryItem extends JMenuItem
{

  /**
   * The serial version UID
   */
  private static final long serialVersionUID = -2506446517048657839L;


  /**
   * TODO
   */
  private static final Logger logger = Logger.getLogger ( HistoryItem.class );


  /**
   * TODO
   */
  private File file;


  /**
   * TODO
   */
  private MainWindow window;


  /**
   * TODO
   * 
   * @param file
   */
  public HistoryItem ( File file )
  {
    this.file = file;
    setText ( this.file.getName () );
    addActionListener ( new ActionListener ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void actionPerformed ( @SuppressWarnings ( "unused" )
      ActionEvent arg0 )
      {
        try
        {

          HistoryItem.this.getWindow ().openFile ( HistoryItem.this.file );
        }
        catch ( Exception e )
        {
          logger.error ( "Window is null! History Item cannot open the file!", //$NON-NLS-1$
              e );
        }
      }

    } );
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public File getFile ()
  {
    return this.file;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public MainWindow getWindow ()
  {
    return this.window;
  }


  /**
   * TODO
   * 
   * @param window
   */
  public void setWindow ( MainWindow window )
  {
    this.window = window;
  }
}
