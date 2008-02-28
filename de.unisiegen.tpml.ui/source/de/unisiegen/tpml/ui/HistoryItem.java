package de.unisiegen.tpml.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;

import org.apache.log4j.Logger;


public class HistoryItem extends JMenuItem
{

  /**
   * The serial version UID
   */
  private static final long serialVersionUID = -2506446517048657839L;


  private static final Logger logger = Logger.getLogger ( HistoryItem.class );


  private File file;


  private MainWindow window;


  public HistoryItem ( File file )
  {
    this.file = file;
    this.setText ( this.file.getName () );
    this.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent arg0 )
      {
        try
        {

          HistoryItem.this.getWindow ().openFile ( HistoryItem.this.file );
        }
        catch ( Exception e )
        {
          logger.error ( "Window is null! History Item cannot open the file!",
              e );
        }
      }

    } );
  }


  public File getFile ()
  {
    return this.file;
  }


  public MainWindow getWindow ()
  {
    return this.window;
  }


  public void setWindow ( MainWindow window )
  {
    this.window = window;
  }

}
