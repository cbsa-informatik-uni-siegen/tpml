package de.unisiegen.tpml.ui;


import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;
import de.unisiegen.tpml.ui.netbeans.EditorPanelForm;
import de.unisiegen.tpml.ui.netbeans.FileWizard;
import de.unisiegen.tpml.ui.netbeans.MainWindowForm;


/**
 * The main programm window.
 * 
 * @author Christoph Fehling
 * @version $Rev:499 $
 * @see de.unisiegen.tpml.ui.Main
 */
public class MainWindow
{

  //
  // Constants
  //
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = -3820623104618482450L;


  /**
   * TODO
   */
  private MainWindowForm window;


  /**
   * The preferences for the <code>de.unisiegen.tpml.ui</code> package.
   */
  // private static final Preferences preferences = Preferences
  // .userNodeForPackage(MainWindow.class);
  //
  // Constructor
  //
  /**
   * Creates new form <code>MainWindow</code>.
   */
  public MainWindow ()
  {
    this.window = new MainWindowForm ();
    this.window.setMainWindow ( this );

    // initComponents ( ) ;
    this.window.setTitle ( "TPML " + Versions.UI ); //$NON-NLS-1$
    // position the window
    PreferenceManager prefmanager = PreferenceManager.get ();
    this.window.setBounds ( prefmanager.getWindowBounds () );
    this.window.setVisible ( true );
    // Setting the default states
    setGeneralStates ( false );
    this.window.saveItem.setEnabled ( false );
    this.window.saveButton.setEnabled ( false );
    this.window.preferencesItem.setEnabled ( true );
    this.window.copyItem.setEnabled ( false );
    this.window.pasteItem.setEnabled ( false );
    this.window.recentFilesMenu.setVisible ( false );
    this.window.cutButton.setEnabled ( false );
    this.window.copyButton.setEnabled ( false );
    this.window.pasteButton.setEnabled ( false );
    this.window.printButton.setEnabled ( false );
    // Finished setting the states.
    this.window.addWindowListener ( new WindowAdapter ()
    {

      @Override
      public void windowClosing ( @SuppressWarnings ( "unused" )
      WindowEvent e )
      {
        MainWindow.this.handleQuit ();
      }
    } );
    KeyboardFocusManager.getCurrentKeyboardFocusManager ()
        .addKeyEventDispatcher ( new KeyEventDispatcher ()
        {

          @SuppressWarnings ( "synthetic-access" )
          public boolean dispatchKeyEvent ( KeyEvent evt )
          {
            if ( ( evt.getID () == KeyEvent.KEY_PRESSED ) )
            {
              if ( ( ( evt.getKeyCode () == KeyEvent.VK_RIGHT ) && evt
                  .isAltDown () )
                  || ( ( evt.getKeyCode () == KeyEvent.VK_PAGE_DOWN ) && evt
                      .isControlDown () ) )
              {
                if ( MainWindow.this.window.tabbedPane.getSelectedIndex () + 1 == MainWindow.this.window.tabbedPane
                    .getTabCount () )
                {
                  MainWindow.this.window.tabbedPane.setSelectedIndex ( 0 );
                  return true;
                }
                MainWindow.this.window.tabbedPane
                    .setSelectedIndex ( MainWindow.this.window.tabbedPane
                        .getSelectedIndex () + 1 );
                return true;
              }
              if ( ( ( evt.getKeyCode () == KeyEvent.VK_LEFT ) && evt
                  .isAltDown () )
                  || ( ( evt.getKeyCode () == KeyEvent.VK_PAGE_UP ) && evt
                      .isControlDown () ) )
              {
                if ( MainWindow.this.window.tabbedPane.getSelectedIndex () == 0 )
                {
                  MainWindow.this.window.tabbedPane
                      .setSelectedIndex ( MainWindow.this.window.tabbedPane
                          .getTabCount () - 1 );
                  return true;
                }
                MainWindow.this.window.tabbedPane
                    .setSelectedIndex ( MainWindow.this.window.tabbedPane
                        .getSelectedIndex () - 1 );
                return true;
              }
            }
            return false;
          }
        } );
    this.editorPanelListener = new PropertyChangeListener ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void propertyChange ( PropertyChangeEvent evt )
      {
        editorStatusChange ( evt.getPropertyName (), evt.getNewValue () );
      }
    };
    this.recentlyUsed = prefmanager.getRecentlyUsed ();
    // TODO this is ugly :(
    for ( int i = 0 ; i < this.recentlyUsed.size () ; i++ )
    {
      this.recentlyUsed.get ( i ).setWindow ( this );
    }
    updateRecentlyUsed ();
    // apply the last "advanced mode" setting
    boolean advanced = prefmanager.getAdvanced ();
    this.window.advancedRadioButton.setSelected ( advanced );
    this.window.beginnerRadioButton.setSelected ( !advanced );
    // apply the last maximization state
    if ( prefmanager.getWindowMaximized () )
    {

      // set to maximized
      this.window.setExtendedState ( this.window.getExtendedState ()
          | Frame.MAXIMIZED_BOTH );
    }
  }


  /**
   * TODO
   */
  // End of variables declaration//GEN-END:variables
  private PropertyChangeListener editorPanelListener;


  /**
   * TODO
   */
  private static final Logger logger = Logger.getLogger ( MainWindow.class );


  // private PreferenceManager prefmanager;
  /**
   * TODO
   */
  private static int historyLength = 9;


  /**
   * TODO
   */
  private LinkedList < HistoryItem > recentlyUsed;


  // Self-defined methods:
  /**
   * TODO
   * 
   * @param file
   */
  void openFile ( File file )
  {
    if ( file == null )
    {
      throw new NullPointerException ( "file is null" ); //$NON-NLS-1$
    }
    try
    {
      // check if we already have an editor panel for the file
      EditorPanel editorPanel = null;
      for ( Component component : this.window.tabbedPane.getComponents () )
      {
        if ( ( component instanceof EditorPanelForm )
            && file.equals ( ( ( ( EditorPanelForm ) component ).getCaller () )
                .getFile () ) )
        {
          editorPanel = ( ( EditorPanelForm ) component ).getCaller ();
          break;
        }
      }
      // if we don't already have the editor panel, create a new one
      if ( editorPanel == null )
      {
        LanguageFactory langfactory = LanguageFactory.newInstance ();
        Language language = langfactory.getLanguageByFile ( file );
        StringBuilder buffer = new StringBuilder ();
        int onechar;
        try
        {
          BufferedReader in = new BufferedReader ( new InputStreamReader (
              new FileInputStream ( file ), "UTF8" ) ); //$NON-NLS-1$
          while ( ( onechar = in.read () ) != -1 )
          {
            buffer.append ( ( char ) onechar );
          }
          in.close ();
        }
        catch ( UnsupportedEncodingException e )
        {
          System.err.println ( "UnsupportedEncodingException" ); //$NON-NLS-1$
        }
        if ( language.isTypeLanguage () )
        {
          editorPanel = new EditorPanelTypes ( language, this );
        }
        else
        {
          editorPanel = new EditorPanelExpression ( language, this );
        }
        this.window.tabbedPane.add ( editorPanel.getPanel () );
        editorPanel
            .setAdvanced ( this.window.advancedRadioButton.isSelected () );
        editorPanel.setFileName ( file.getName () );
        editorPanel.setEditorText ( buffer.toString () );
        editorPanel.setFile ( file );
        editorPanel.addPropertyChangeListener ( this.editorPanelListener );
        editorPanel.setTexteditor ( true );
      }
      this.window.tabbedPane.setSelectedComponent ( editorPanel.getPanel () );
      setGeneralStates ( true );
      updateEditorStates ( editorPanel );
    }
    catch ( NoSuchLanguageException e )
    {
      logger.error ( "Language does not exist.", e ); //$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.window, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "FileNotSupported" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
          "de/unisiegen/tpml/ui/ui" ).getString ( "Open_File" ), //$NON-NLS-1$ //$NON-NLS-2$
          JOptionPane.ERROR_MESSAGE );
    }
    catch ( FileNotFoundException e )
    {
      logger.error ( "File specified could not be found", e ); //$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.window, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "FileCannotBeFound" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
          "de/unisiegen/tpml/ui/ui" ).getString ( "Open_File" ), //$NON-NLS-1$//$NON-NLS-2$
          JOptionPane.ERROR_MESSAGE );
    }
    catch ( IOException e )
    {
      logger.error ( "Could not read from the file specified", e ); //$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.window, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "FileCannotBeRead" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
          "de/unisiegen/tpml/ui/ui" ).getString ( "Open_File" ), //$NON-NLS-1$//$NON-NLS-2$
          JOptionPane.ERROR_MESSAGE );
    }
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setGeneralStates ( boolean state )
  {
    this.window.smallstepItem.setEnabled ( state );
    this.window.bigstepItem.setEnabled ( state );
    this.window.typecheckerItem.setEnabled ( state );
    this.window.minimaltypingItem.setEnabled ( state );
    this.window.typeinferenceItem.setEnabled ( state );
    this.window.subtypingItem.setEnabled ( state );
    this.window.subtypingrecItem.setEnabled ( state );
    this.window.saveAsItem.setEnabled ( state );
    this.window.saveAsButton.setEnabled ( state );
    this.window.saveAllItem.setEnabled ( state );
    this.window.closeItem.setEnabled ( state );
    this.window.cutItem.setEnabled ( state );
    this.window.cutButton.setEnabled ( state );
    this.window.copyItem.setEnabled ( state );
    this.window.copyButton.setEnabled ( state );
    this.window.pasteItem.setEnabled ( state );
    this.window.pasteButton.setEnabled ( state );
    this.window.printButton.setEnabled ( state );
    this.window.printItem.setEnabled ( state );
    this.window.latexExportItem.setEnabled ( state );
    this.window.latexExportButton.setEnabled ( state );
    setUndoState ( new Boolean ( state ) );
    setRedoState ( new Boolean ( state ) );
  }


  /**
   * TODO
   * 
   * @param ident
   * @param newValue
   */
  private void editorStatusChange ( @SuppressWarnings ( "unused" )
  String ident, @SuppressWarnings ( "unused" )
  Object newValue )
  {
    // logger.debug ( "Editor status changed: " + ident ) ;
    // if ( ident.equals ( "redoStatus" ) )
    // {
    // logger.debug ( "Editor status changed. Ident: redoStatus" ) ;
    // setRedoState ( ( Boolean ) newValue ) ;
    // }
    // else if ( ident.equals ( "filename" ) )
    // {
    // logger.debug ( "Editor status changed. Ident: filename" ) ;
    // tabbedPane.setTitleAt ( tabbedPane.getSelectedIndex ( ) ,
    // ( String ) newValue ) ;
    // // TODO merge undostatus and changestatus
    // }
    // else if ( ident.equals ( "undoStatus" ) )
    // {
    // logger.debug ( "Editor status changed. Ident: undoStatus" ) ;
    // setUndoState ( ( Boolean ) newValue ) ;
    // // setChangeState((Boolean) newValue);
    // }
    // else if ( ident.equals ( "changed" ) )
    // {
    // logger.debug ( "Editor status changed. Ident: changed" ) ;
    // setChangeState ( ( Boolean ) newValue ) ;
    // // setSaveState((Boolean) newValue);
    // }
    // else if ( ident.equals ( "texteditor" ) )
    // {
    // logger.debug ( "Editor status changed. Ident: textditor" ) ;
    // cutItem.setEnabled ( ( Boolean ) newValue ) ;
    // cutButton.setEnabled ( ( Boolean ) newValue ) ;
    // copyItem.setEnabled ( ( Boolean ) newValue ) ;
    // copyButton.setEnabled ( ( Boolean ) newValue ) ;
    // pasteItem.setEnabled ( ( Boolean ) newValue ) ;
    // pasteButton.setEnabled ( ( Boolean ) newValue ) ;
    // }
    updateEditorStates ( ( ( EditorPanelForm ) this.window.tabbedPane
        .getSelectedComponent () ).getCaller () );
  }


  /**
   * TODO
   * 
   * @param editor
   */
  public void updateEditorStates ( EditorPanel editor )
  {
    updateEditorStates ( ( EditorPanelForm ) editor.getPanel () );
  }


  /**
   * TODO
   * 
   * @param editorform
   */
  public void updateEditorStates ( EditorPanelForm editorform )
  {
    if ( editorform == null )
    {// last tab was closed
      setGeneralStates ( false );
      // }
      // if (getActiveEditor() == null) { // the same as above?
      // setGeneralStates(false);
    }
    else
    {
      EditorPanel editor = editorform.getCaller ();
      setRedoState ( new Boolean ( editor.isRedoStatus () ) );
      setUndoState ( new Boolean ( editor.isUndoStatus () ) );
      // setSaveState(editor.isUndoStatus());
      if ( editor.isTexteditor () )
      {
        setChangeState ( new Boolean ( editor.isSaveStatus () ) );
        setEditorFunctions ( true );
      }
      else
      {
        setChangeState ( new Boolean ( editor.isSaveStatus () ) );
        setEditorFunctions ( false );
      }
    }
    if ( getActiveEditor () instanceof EditorPanelExpression )
    {
      setExpressionMode ();
    }
    else
    {
      setTypeMode ();
    }
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setEditorFunctions ( boolean state )
  {
    this.window.cutButton.setEnabled ( state );
    this.window.cutItem.setEnabled ( state );
    this.window.copyButton.setEnabled ( state );
    this.window.copyItem.setEnabled ( state );
    this.window.pasteButton.setEnabled ( state );
    this.window.pasteItem.setEnabled ( state );
  }


  /**
   * TODO
   */
  private void updateRecentlyUsed ()
  {
    final int length = ( this.recentlyUsed.size () > historyLength ) ? historyLength
        : this.recentlyUsed.size ();
    if ( length > historyLength )
    {
      logger.error ( "Error: The list of recently used files is larger than " //$NON-NLS-1$
          + historyLength );
    }
    HistoryItem item;
    this.window.recentFilesMenu.setVisible ( length > 0 );
    this.window.fileMenuSeperator3.setVisible ( length > 0 );
    this.window.recentFilesMenu.removeAll ();
    for ( int i = 0 ; i < length ; i++ )
    {
      item = this.recentlyUsed.get ( i );
      item.setText ( "" + ( i + 1 ) + ". " + item.getFile ().getName () ); //$NON-NLS-1$//$NON-NLS-2$
      this.window.recentFilesMenu.add ( item );
    }
    this.window.recentFilesMenu.addSeparator ();
    JMenuItem openAllItem = new JMenuItem ( ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "OpenAll" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    openAllItem.setMnemonic ( ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
        .getString ( "OpenAllMnemonic" ).charAt ( 0 ) ); //$NON-NLS-1$
    openAllItem.addActionListener ( new ActionListener ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void actionPerformed ( @SuppressWarnings ( "unused" )
      ActionEvent e )
      {
        for ( int i = 0 ; i < length ; ++i )
        {
          File file = MainWindow.this.recentlyUsed.get ( i ).getFile ();
          openFile ( file );
        }
      }
    } );
    this.window.recentFilesMenu.add ( openAllItem );
  }


  /**
   * TODO
   * 
   * @param historyItem
   */
  public void addRecentlyUsed ( HistoryItem historyItem )
  {
    boolean alreadyPresent = false;
    // check if a similar entry in the history already exists:
    for ( int i = 0 ; i < this.recentlyUsed.size () ; i++ )
    {
      if ( this.recentlyUsed.get ( i ).getFile ().toURI ().equals (
          historyItem.getFile ().toURI () ) )
      {
        alreadyPresent = true;
      }
    }
    if ( !alreadyPresent )
    {
      this.recentlyUsed.addFirst ( historyItem );
    }
    historyItem.setWindow ( this );
    if ( this.recentlyUsed.size () > historyLength )
    {
      this.recentlyUsed.removeLast ();
    }
    updateRecentlyUsed ();
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setRedoState ( Boolean state )
  {
    this.window.redoButton.setEnabled ( state.booleanValue () );
    this.window.redoItem.setEnabled ( state.booleanValue () );
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setUndoState ( Boolean state )
  {
    logger.debug ( "UndoStatus of MainWindow set to " + state ); //$NON-NLS-1$
    this.window.undoButton.setEnabled ( state.booleanValue () );
    this.window.undoItem.setEnabled ( state.booleanValue () );
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setSaveState ( Boolean state )
  {
    this.window.saveButton.setEnabled ( state.booleanValue () );
    this.window.saveItem.setEnabled ( state.booleanValue () );
  }


  /**
   * TODO
   * 
   * @param state
   */
  private void setChangeState ( Boolean state )
  {
    if ( state.booleanValue () )
    {
      this.window.tabbedPane.setTitleAt ( this.window.tabbedPane
          .getSelectedIndex (), "*" //$NON-NLS-1$
          + ( ( ( EditorPanelForm ) this.window.tabbedPane
              .getSelectedComponent () ) ).getCaller ().getFileName () );
      setSaveState ( Boolean.TRUE );
    }
    else
    {
      this.window.tabbedPane.setTitleAt ( this.window.tabbedPane
          .getSelectedIndex (), ( ( ( EditorPanelForm ) this.window.tabbedPane
          .getSelectedComponent () ) ).getCaller ().getFileName () );
      setSaveState ( Boolean.FALSE );
    }
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public EditorPanel getActiveEditor ()
  {
    if ( this.window.tabbedPane.getSelectedComponent () == null )
    {
      return null;
    }
    return ( ( EditorPanelForm ) this.window.tabbedPane.getSelectedComponent () )
        .getCaller ();
  }


  /**
   * TODO
   */
  public void handleNew ()
  {
    FileWizard wizard = new FileWizard ( this.window, true );
    wizard.setLocationRelativeTo ( this.window );
    wizard.setVisible ( true );
    Language language = wizard.getLanguage ();
    if ( language == null )
    {
      return;
    }

    EditorPanel newEditorPanel = null;
    if ( !language.isTypeLanguage () )
    {
      newEditorPanel = new EditorPanelExpression ( language, this );
      setExpressionMode ();
    }
    else
    {
      newEditorPanel = new EditorPanelTypes ( language, this );
      setTypeMode ();
    }

    this.window.tabbedPane.add ( newEditorPanel.getPanel () );
    newEditorPanel.setAdvanced ( this.window.advancedRadioButton.isSelected () );
    this.window.tabbedPane.setSelectedComponent ( newEditorPanel.getPanel () );
    newEditorPanel.addPropertyChangeListener ( this.editorPanelListener );
    newEditorPanel.setTexteditor ( true );
    setGeneralStates ( true );
    updateEditorStates ( newEditorPanel );
  }


  /**
   * TODO
   */
  private void setTypeMode ()
  {
    this.window.smallstepItem.setVisible ( false );
    this.window.bigstepItem.setVisible ( false );
    this.window.typecheckerItem.setVisible ( false );
    this.window.minimaltypingItem.setVisible ( false );
    this.window.typeinferenceItem.setVisible ( false );
    this.window.subtypingItem.setVisible ( true );
    this.window.subtypingrecItem.setVisible ( true );
  }


  /**
   * TODO
   */
  private void setExpressionMode ()
  {

    this.window.smallstepItem.setVisible ( true );
    this.window.bigstepItem.setVisible ( true );
    this.window.typecheckerItem.setVisible ( true );
    this.window.minimaltypingItem.setVisible ( true );
    this.window.typeinferenceItem.setVisible ( true );
    this.window.subtypingItem.setVisible ( false );
    this.window.subtypingrecItem.setVisible ( false );
  }


  /**
   * TODO
   */
  public void handleOpen ()
  {
    PreferenceManager prefmanager = PreferenceManager.get ();
    JFileChooser chooser = new JFileChooser ( prefmanager.getWorkingPath () );
    chooser.setMultiSelectionEnabled ( true );
    final LanguageFactory factory = LanguageFactory.newInstance ();
    chooser.addChoosableFileFilter ( new FileFilter ()
    {

      @Override
      public boolean accept ( File f )
      {
        if ( f.isDirectory () )
        {
          return true;
        }
        try
        {
          factory.getLanguageByFile ( f );
          return true;
        }
        catch ( NoSuchLanguageException e )
        {
          return false;
        }
      }


      @Override
      public String getDescription ()
      {
        Language [] languages = factory.getAvailableLanguages ();
        StringBuilder builder = new StringBuilder ( 128 );
        builder.append ( "Source Files (" ); //$NON-NLS-1$
        for ( int n = 0 ; n < languages.length ; ++n )
        {
          if ( n > 0 )
          {
            builder.append ( "; " ); //$NON-NLS-1$
          }
          builder.append ( "*." ); //$NON-NLS-1$
          builder.append ( languages [ n ].getName ().toLowerCase () );
        }
        builder.append ( ')' );
        return builder.toString ();
      }
    } );
    chooser.setAcceptAllFileFilterUsed ( false );
    int returnVal = chooser.showOpenDialog ( this.window );
    if ( returnVal == JFileChooser.APPROVE_OPTION )
    {
      File [] files = chooser.getSelectedFiles ();
      for ( File element : files )
      {
        openFile ( element );
      }
    }
    prefmanager.setWorkingPath ( chooser.getCurrentDirectory ()
        .getAbsolutePath () );
  }


  /**
   * TODO
   */
  public void handleQuit ()
  {
    // be sure to save all files first
    for ( Component component : this.window.tabbedPane.getComponents () )
    {
      if ( component instanceof EditorPanelForm )
      {
        EditorPanel editorPanel = ( ( EditorPanelForm ) component )
            .getCaller ();
        if ( !editorPanel.shouldBeSaved () )
        {
          continue;
        }
        // Custom button text
        Object [] options =
        { java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
            .getString ( "Yes" ), //$NON-NLS-1$
            java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
                .getString ( "No" ), //$NON-NLS-1$
            java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
                .getString ( "Cancel" ) }; //$NON-NLS-1$
        int n = JOptionPane
            .showOptionDialog (
                this.window,
                editorPanel.getFileName ()
                    + " " //$NON-NLS-1$
                    + java.util.ResourceBundle.getBundle (
                        "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
                        .getString ( "WantTosave" ), java.util.ResourceBundle //$NON-NLS-1$
                    .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( "Save_File" ), //$NON-NLS-1$ //$NON-NLS-2$
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options [ 2 ] );
        switch ( n )
        {
          case 0 : // Save changes
            logger.debug ( "Quit dialog: YES" ); //$NON-NLS-1$
            if ( !editorPanel.handleSave () )
            {
              // abort the quit
              return;
            }
            break;
          case 1 : // Do not save changes
            logger.debug ( "Quit dialog: NO" ); //$NON-NLS-1$
            break;
          default : // Cancelled
            logger.debug ( "Quit dialog: CANCEL" ); //$NON-NLS-1$
            return;
        }
      }
    }
    // save the session
    saveOpenFiles ();
    // remember the settings
    PreferenceManager prefmanager = PreferenceManager.get ();
    prefmanager.setAdvanced ( this.window.advancedRadioButton.isSelected () );
    // remember the history
    prefmanager.setRecentlyUsed ( this.recentlyUsed );
    // remember window state
    prefmanager.setWindowPreferences ( this.window );
    // terminate the application
    System.exit ( 0 );
  }


  /**
   * Closes the active editor window.
   * 
   * @return true if the active editor could be closed.
   */
  @SuppressWarnings (
  { "fallthrough", "fallthrough" } )
  public boolean handleClose ()
  {
    EditorPanel selectedEditor = getActiveEditor ();
    boolean success;
    if ( selectedEditor.shouldBeSaved () )
    {
      Object [] options =
      { java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
          .getString ( "Yes" ), //$NON-NLS-1$
          java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
              .getString ( "No" ), //$NON-NLS-1$
          java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
              .getString ( "Cancel" ) }; //$NON-NLS-1$
      int n = JOptionPane.showOptionDialog ( this.window, selectedEditor
          .getFileName ()
          + java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
              .getString ( "WantTosave" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
              "de/unisiegen/tpml/ui/ui" ).getString ( "Save_File" ), //$NON-NLS-1$ //$NON-NLS-2$
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
          options, options [ 2 ] );
      switch ( n )
      {
        case 0 : // Save Changes
          logger.debug ( "Close dialog: YES" ); //$NON-NLS-1$
          success = selectedEditor.handleSave ();
          if ( success )
          {
            this.window.tabbedPane.remove ( this.window.tabbedPane
                .getSelectedIndex () );
            this.window.repaint ();
          }
          return success;
        case 1 : // Do not save changes
          logger.debug ( "Close dialog: NO" ); //$NON-NLS-1$
          this.window.tabbedPane.remove ( this.window.tabbedPane
              .getSelectedIndex () );
          this.window.repaint ();
          success = true;
        case 2 : // Cancelled.
          logger.debug ( "Close dialog: CANCEL" ); //$NON-NLS-1$
          success = false;
        default :
          success = false;
      }
    }
    else
    {
      this.window.tabbedPane.remove ( this.window.tabbedPane
          .getSelectedIndex () );
      this.window.repaint ();
      success = true;
    }
    if ( getActiveEditor () == null )
    {
      setGeneralStates ( false );
      this.window.saveItem.setEnabled ( false );
      this.window.saveButton.setEnabled ( false );
    }
    return success;
  }


  /**
   * TODO
   */
  public void handleSaveAll ()
  {
    int tabcount = this.window.tabbedPane.getComponentCount ();
    for ( int i = 0 ; i < tabcount ; i++ )
    {
      if ( ! ( ( ( EditorPanelForm ) this.window.tabbedPane.getComponentAt ( i ) ) )
          .getCaller ().handleSave () )
      {
        return;
      }
    }
  }


  /**
   * Stores the list of open files for the next start (see
   * {@link #restoreOpenFiles()}), that is the list of files from the
   * {@link EditorPanel}s that have valid <code>File</code> objects. This is
   * called exactly once on quit.
   * 
   * @see #restoreOpenFiles()
   */
  public void saveOpenFiles ()
  {
    int tabcount = this.window.tabbedPane.getComponentCount ();
    LinkedList < File > filelist = new LinkedList < File > ();
    File file;
    for ( int i = 0 ; i < tabcount ; i++ )
    {
      file = ( ( EditorPanelForm ) this.window.tabbedPane.getComponentAt ( i ) )
          .getCaller ().getFile ();
      if ( file != null )
      {
        filelist.add ( file );
      }
    }
    PreferenceManager.get ().setOpenFiles ( filelist );
  }


  /**
   * Restores the list of open files from a previous session, previously saved
   * by the {@link #saveOpenFiles()} method. This is called on startup if no
   * files where provided.
   * 
   * @see #saveOpenFiles()
   */
  public void restoreOpenFiles ()
  {
    LinkedList < File > filelist = PreferenceManager.get ().getOpenFiles ();
    File currentfile;
    for ( int i = 0 ; i < filelist.size () ; i++ )
    {
      currentfile = filelist.get ( i );
      if ( currentfile.exists () && currentfile.canRead () )
      {
        openFile ( currentfile );
      }
    }
  }
}
