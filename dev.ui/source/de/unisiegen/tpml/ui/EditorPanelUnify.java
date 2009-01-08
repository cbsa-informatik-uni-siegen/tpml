package de.unisiegen.tpml.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;
import de.unisiegen.tpml.core.unify.UnifyProofModel;
import de.unisiegen.tpml.core.util.beans.AbstractBean;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.EditorComponent;
import de.unisiegen.tpml.graphics.ProofViewFactory;
import de.unisiegen.tpml.graphics.editor.UnifyEditorPanel;
import de.unisiegen.tpml.ui.netbeans.EditorPanelForm;
import de.unisiegen.tpml.ui.proofview.ProofViewComponent;


/**
 * Part of the UI displayed in the tabbed pane. It includes one open file and
 * all {@link de.unisiegen.tpml.graphics.EditorComponent}s open for that file.
 * 
 * @author Christoph Fehling
 * @version $Id$
 */
public class EditorPanelUnify extends AbstractBean implements EditorPanel
{

  /**
   * TODO
   */
  private static final Logger logger = Logger
      .getLogger ( EditorPanelUnify.class );


  /**
   * TODO
   */
  private MainWindow window;


  /**
   * TODO
   */
  private EditorPanelForm mypanel;


  /**
   * 
   */
  private UnifyEditorPanel code;


  /**
   * TODO
   */
  private EditorComponent smallstep;


  /**
   * TODO
   */
  private EditorComponent bigstep;


  /**
   * TODO
   */
  private EditorComponent typechecker;


  /**
   * TODO
   */
  private EditorComponent typeinference;


  /**
   * TODO
   */
  private EditorComponent minimaltyping;


  /**
   * TODO
   */
  private EditorComponent unify;


  /**
   * TODO
   */
  private EditorComponent activeEditorComponent;


  /**
   * TODO
   */
  private PropertyChangeListener editorComponentListener;


  /**
   * TODO
   */
  private Color buttonColor = new Color ( 238, 238, 238 );


  /**
   * Filename displayed in the tab.
   */
  private String filename;


  /**
   * static number counting the new files with default name.
   */
  static private int num = 0;


  /**
   * TODO
   */
  private boolean advanced;


  /**
   * The language used in this Editor.
   */
  private Language language;


  /**
   * The file to which this document is saved.
   */
  private File file;


  /**
   * Indicated if the file was changed.
   */
  // private boolean changed;
  /**
   * Indicates the status of the Undo function.
   */
  private boolean undoStatus;


  /**
   * Indicates the status of the Redo function.
   */
  private boolean redoStatus;


  /**
   * Indicated if the displayed component is a text editor.
   */
  private boolean texteditor;


  /**
   * The serial version UID
   */
  private static final long serialVersionUID = -272175525193942130L;


  /**
   * Creates new form EditorPanelExpression
   * 
   * @param language
   * @param window
   */
  public EditorPanelUnify ( Language language, MainWindow window )
  {
    // initComponents();
    this.mypanel = new EditorPanelForm ( this );

    this.window = window;
    // setting the default button states
    this.mypanel.nextButton.setVisible ( false );
    this.mypanel.pongButton.setVisible ( false );
    this.mypanel.smallstepButton.setVisible ( false );
    this.mypanel.bigstepButton.setVisible ( false );
    this.mypanel.typecheckerButton.setVisible ( false );
    this.mypanel.typeinferenceButton.setVisible ( false );
    this.mypanel.minimalTypingButton.setVisible ( false );
    this.mypanel.subTypingButton.setVisible ( false );
    this.mypanel.subTypingRecButton.setVisible ( false );
    this.mypanel.unifyButton.setVisible ( false );
    this.mypanel.bigstepclosureButton.setVisible ( false );
    // finished setting the default states

    // hack to get consistent heights
    this.mypanel.codeButton.setPreferredSize ( new Dimension (
        this.mypanel.codeButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    this.mypanel.smallstepButton.setPreferredSize ( new Dimension (
        this.mypanel.smallstepButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    this.mypanel.bigstepButton.setPreferredSize ( new Dimension (
        this.mypanel.bigstepButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    this.mypanel.typecheckerButton.setPreferredSize ( new Dimension (
        this.mypanel.typecheckerButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    this.mypanel.typeinferenceButton.setPreferredSize ( new Dimension (
        this.mypanel.typeinferenceButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    this.mypanel.minimalTypingButton.setPreferredSize ( new Dimension (
        this.mypanel.minimalTypingButton.getPreferredSize ().width,
        this.mypanel.pongButton.getPreferredSize ().height ) );
    // There will be no SubTypingButton
    // TODO vielleicht auch machen müssen

    this.language = language;

    // TODO PREFERENCES get this from the preferences
    setAdvanced ( false );

    setFileName ( "newfile" + num + "." + language.getName () ); //$NON-NLS-1$ //$NON-NLS-2$
    num++ ;
    this.editorComponentListener = new PropertyChangeListener ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void propertyChange ( PropertyChangeEvent evt )
      {
        componentStatusChanged ( evt.getPropertyName (), evt.getNewValue () );
      }
    };
    initEditor ();

    this.mypanel.addComponentListener ( new java.awt.event.ComponentAdapter ()
    {

      @SuppressWarnings ( "synthetic-access" )
      @Override
      public void componentShown ( @SuppressWarnings ( "unused" )
      java.awt.event.ComponentEvent evt )
      {
        EditorPanelUnify.this.code.getEditor ().requestFocus ();
      }
    } );
  }


  /**
   * Is called when a status of the displayed component in changed
   * 
   * @param ident name of the changed status
   * @param newValue new value of the status
   */
  private void componentStatusChanged ( String ident, Object newValue )
  {
    if ( ident.equals ( "nextStatus" ) ) //$NON-NLS-1$
    {
      this.mypanel.nextButton.setEnabled ( ( ( Boolean ) newValue )
          .booleanValue () );
    }
    else if ( ident.equals ( "pongStatus" ) )//$NON-NLS-1$
    {
      this.mypanel.pongButton.setVisible ( ( ( Boolean ) newValue )
          .booleanValue () );
    }
    else if ( ident.equals ( "redoStatus" ) )//$NON-NLS-1$
    {
      setRedoStatus ( ( ( Boolean ) newValue ).booleanValue () );
    }
    else if ( ident.equals ( "title" ) )//$NON-NLS-1$
    {
      setFileName ( ( String ) newValue );
    }
    else if ( ident.equals ( "undoStatus" ) )//$NON-NLS-1$
    {
      setUndoStatus ( ( ( Boolean ) newValue ).booleanValue () );
    }
    else if ( ident.equals ( "changed" ) )//$NON-NLS-1$
    {
      // setChanged((Boolean) newValue);
      setUndoStatus ( ( ( Boolean ) newValue ).booleanValue () );
    }

  }


  /**
   * TODO
   * 
   * @param comp
   */
  private void updateComponentStates ( EditorComponent comp )
  {
    setRedoStatus ( comp.isRedoStatus () );
    setUndoStatus ( comp.isUndoStatus () );
    this.mypanel.nextButton.setEnabled ( comp.isNextStatus () );
    this.mypanel.nextButton.setVisible ( comp != this.code );
    this.mypanel.pongButton.setVisible ( comp.isPongStatus () );
  }


  /**
   * Sets the Component shown in the Editor Panel.
   * 
   * @param comp
   */
  private void setComponent ( EditorComponent comp )
  {

    this.mypanel.editorPanel.removeAll ();
    this.mypanel.editorPanel.add ( ( JComponent ) comp, BorderLayout.CENTER );
    this.activeEditorComponent = comp;
    updateComponentStates ( comp );
    this.mypanel.paintAll ( this.mypanel.getGraphics () );
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  private EditorComponent getComponent ()
  {
    return ( EditorComponent ) this.mypanel.editorPanel.getComponent ( 0 );
  }


  /**
   * This method is called from within the constructor to initialize the source
   * editor.
   */
  private void initEditor ()
  {
    this.code = new UnifyEditorPanel ( this.language );

    this.mypanel.editorPanel.removeAll ();
    this.mypanel.editorPanel.add ( this.code, BorderLayout.CENTER );
    ( ( JPanel ) this.code )
        .addPropertyChangeListener ( this.editorComponentListener );
    this.code.setDefaultStates ();
    updateComponentStates ( this.code );
    deselectButtons ();
    this.mypanel.codeButton.setSelected ( true );
    this.mypanel.codeButton.setEnabled ( true );

    this.mypanel.paintAll ( this.mypanel.getGraphics () );
  }


  /**
   * TODO: implement
   */
  public void handleUnify ()
  {
    setTexteditor ( false );
    try
    {
      UnifyProofModel model = this.language.newUnifyProofModel ( this.code
          .getDocument ().getTypeEquation () );
      this.unify = new ProofViewComponent ( ProofViewFactory
          .newUnifyView ( model ), model );
      this.mypanel.editorPanel.removeAll ();
      activateFunction ( this.mypanel.unifyButton, this.unify );
      this.mypanel.unifyButton.setIcon ( null );
      this.mypanel.paintAll ( this.mypanel.getGraphics () );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      logger.debug ( "Could not create new UnifyView", e ); //$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.mypanel, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "CouldNotUnify" )//$NON-NLS-1$
          + "\n" + e.getMessage () + ".", "Unifikation",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          //$NON-NLS-2$ //$NON-NLS-3$
          JOptionPane.ERROR_MESSAGE );
    }
  }


  /**
   * TODO
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handleCut()
   */
  public void handleCut ()
  {
    this.code.handleCut ();
  }


  /**
   * TODO
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handleCopy()
   */
  public void handleCopy ()
  {
    this.code.handleCopy ();
  }


  /**
   * TODO
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handlePaste()
   */
  public void handlePaste ()
  {
    this.code.handlePaste ();
  }


  /**
   * activates one of the following: smallstep, bigstep, typechecker etc.
   * buttons and special component functions.
   * 
   * @param button the button to be activated
   * @param comp the component related to that button
   */
  private void activateFunction ( JToggleButton button, EditorComponent comp )
  {
    comp.setDefaultStates ();
    ( ( JComponent ) comp )
        .addPropertyChangeListener ( this.editorComponentListener );
    setComponent ( comp );
    deselectButtons ();
    if ( button != null )
    {
      button.setSelected ( true );
      button.setVisible ( true );
      this.mypanel.nextButton.setVisible ( true );

    }
  }


  /**
   * Sets the select states of the code, smallstep, bigstep and typechecker
   * buttons to false.
   */
  private void deselectButtons ()
  {
    this.mypanel.codeButton.setSelected ( false );
    this.mypanel.smallstepButton.setSelected ( false );
    this.mypanel.bigstepButton.setSelected ( false );
    this.mypanel.typecheckerButton.setSelected ( false );
    this.mypanel.typeinferenceButton.setSelected ( false );
    this.mypanel.minimalTypingButton.setSelected ( false );
    this.mypanel.unifyButton.setSelected ( false );
  }


  /**
   * Returns the redo status
   * 
   * @return true if redo is available
   */
  public boolean isRedoStatus ()
  {
    return this.redoStatus;
  }


  /**
   * Sets the redo status.
   * 
   * @param redoStatus redo status to be set.
   */
  public void setRedoStatus ( boolean redoStatus )
  {
    boolean oldRedoStatus = this.redoStatus;
    this.redoStatus = redoStatus;
    firePropertyChange ( "redoStatus", oldRedoStatus, redoStatus );//$NON-NLS-1$
  }


  /**
   * Returns the file name.
   * 
   * @return the file name.
   */
  public String getFileName ()
  {
    return this.filename;
  }


  /**
   * Sets the file name.
   * 
   * @param filename the file name to be set.
   * @exception NullPointerException if <code>filename</code> is
   *              <code>null</code>
   */
  public void setFileName ( String filename )
  {
    if ( filename == null )
    {
      throw new NullPointerException ( "filename is null" );//$NON-NLS-1$
    }
    String oldFilename = this.filename;
    this.filename = filename;
    firePropertyChange ( "filename", oldFilename, filename );//$NON-NLS-1$
  }


  /**
   * Returns the file name.
   * 
   * @return the file name.
   */
  public File getFile ()
  {
    return this.file;
  }


  /**
   * Sets the <code>File</code> for this editor.
   * 
   * @param file the <code>File</code> to be set.
   * @throws NullPointerException if the <code>File</code> is
   *           <code>null</code>.
   */
  public void setFile ( File file )
  {
    if ( file == null )
    {
      throw new NullPointerException ( "File is null" );//$NON-NLS-1$
    }
    // if (this.file != null) window.removeRecentlyUsed(this.file);
    this.file = file;
    this.window.addRecentlyUsed ( new HistoryItem ( this.file ) );
    setFileName ( file.getName () );

  }


  /**
   * Returns the language used in this editor.
   * 
   * @return the language used.
   */
  public Language getLanguage ()
  {
    return this.language;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see de.unisiegen.tpml.ui.EditorPanel#getActiveEditorComponent()
   */
  public EditorComponent getActiveEditorComponent ()
  {
    return this.activeEditorComponent;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see de.unisiegen.tpml.ui.EditorPanel#isTexteditor()
   */
  public boolean isTexteditor ()
  {
    return this.texteditor;
  }


  /**
   * TODO
   * 
   * @param texteditor
   * @see de.unisiegen.tpml.ui.EditorPanel#setTexteditor(boolean)
   */
  public void setTexteditor ( boolean texteditor )
  {
    firePropertyChange ( "texteditor", this.texteditor, texteditor );//$NON-NLS-1$
    logger.debug ( "Texteditor is active" );//$NON-NLS-1$
    this.texteditor = texteditor;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public String getEditorText ()
  {
    return this.code.getText ();
  }


  /**
   * TODO
   * 
   * @param text
   * @see de.unisiegen.tpml.ui.EditorPanel#setEditorText(java.lang.String)
   */
  public void setEditorText ( String text )
  {
    this.code.setText ( text );
  }


  /**
   * Sets the undo status
   * 
   * @return true if the undo function is available
   */
  public boolean isUndoStatus ()
  {
    return this.undoStatus;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see de.unisiegen.tpml.ui.EditorPanel#isSaveStatus()
   */
  public boolean isSaveStatus ()
  {
    if ( isTexteditor () )
    {
      return this.undoStatus;
    }
    return false;
  }


  /**
   * sets the undo status
   * 
   * @param undoStatus
   */
  public void setUndoStatus ( boolean undoStatus )
  {
    if ( this.undoStatus != undoStatus )
    {
      logger
          .debug ( "UndoStatus of EditorPanelExpression set to " + undoStatus );//$NON-NLS-1$
      boolean oldUndoStatus = this.undoStatus;
      this.undoStatus = undoStatus;
      firePropertyChange ( "undoStatus", oldUndoStatus, undoStatus );//$NON-NLS-1$
      if ( isTexteditor () )
      {
        firePropertyChange ( "changed", oldUndoStatus, undoStatus );//$NON-NLS-1$
      }
    }
  }


  /**
   * sets the advanced state
   * 
   * @param state
   * @see de.unisiegen.tpml.ui.EditorPanel#setAdvanced(boolean)
   */
  public void setAdvanced ( boolean state )
  {
    if ( this.bigstep != null )
    {
      this.bigstep.setAdvanced ( state );
    }
    if ( this.smallstep != null )
    {
      this.smallstep.setAdvanced ( state );
    }
    if ( this.typechecker != null )
    {
      this.typechecker.setAdvanced ( state );
    }
    if ( this.typeinference != null )
    {
      this.typeinference.setAdvanced ( state );
    }
    if ( this.minimaltyping != null )
    {
      this.minimaltyping.setAdvanced ( state );
    }
    if ( this.unify != null )
    {
      this.unify.setAdvanced ( state );
    }
    this.advanced = state;
  }


  /**
   * requests whether its safe to save the file or not
   * 
   * @return true when its safe to save the file, false otherwise
   * @see de.unisiegen.tpml.ui.EditorPanel#shouldBeSaved()
   */
  public boolean shouldBeSaved ()
  {
    return this.code.isUndoStatus ();
  }


  /**
   * handles the undo
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handleUndo()
   */
  public void handleUndo ()
  {
    getComponent ().handleUndo ();
  }


  /**
   * handles the redo
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handleRedo()
   */
  public void handleRedo ()
  {
    getComponent ().handleRedo ();
  }


  /**
   * handles the save
   * 
   * @return whether saving the file was successfully
   * @see de.unisiegen.tpml.ui.EditorPanel#handleSave()
   */
  public boolean handleSave ()
  {
    if ( this.file == null )
    {
      return handleSaveAs ();
    }
    return writeFile ();
  }


  /**
   * Saves the active editor component.
   * 
   * @return true if the file could be saved.
   */
  public boolean handleSaveAs ()
  {
    // setup the file chooser
    final LanguageFactory factory = LanguageFactory.newInstance ();
    PreferenceManager prefmanager = PreferenceManager.get ();
    JFileChooser chooser = new JFileChooser ( prefmanager.getWorkingPath () );
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
        builder.append ( "Source Files (" );//$NON-NLS-1$
        for ( int n = 0 ; n < languages.length ; ++n )
        {
          if ( n > 0 )
          {
            builder.append ( "; " );//$NON-NLS-1$
          }
          builder.append ( "*." );//$NON-NLS-1$
          builder.append ( languages [ n ].getName ().toLowerCase () );
        }
        builder.append ( ')' );
        return builder.toString ();
      }
    } );
    chooser.setAcceptAllFileFilterUsed ( false );
    prefmanager.setWorkingPath ( chooser.getCurrentDirectory ()
        .getAbsolutePath () );

    // determine the file name
    File outfile;
    for ( ; ; )
    {
      // run the dialog
      int n = chooser.showSaveDialog ( this.mypanel.getParent () );

      if ( n != JFileChooser.APPROVE_OPTION )
      {
        logger.debug ( "Save as dialog cancelled" );//$NON-NLS-1$
        return false;
      }

      // check the extension
      File f = chooser.getSelectedFile ();
      String name = f.getName ();
      int i = name.lastIndexOf ( '.' );
      if ( ( i > 0 ) && ( i < name.length () ) )
      {
        if ( !name.substring ( i + 1 ).equalsIgnoreCase (
            this.language.getName () ) )
        {
          JOptionPane.showMessageDialog ( this.mypanel,
              java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" )//$NON-NLS-1$
                  .getString ( "FileMustEndWith" )//$NON-NLS-1$
                  + " \"." + this.language.getName ().toLowerCase () + "\".",//$NON-NLS-1$//$NON-NLS-2$
              java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" )//$NON-NLS-1$
                  .getString ( "Save" ), JOptionPane.ERROR_MESSAGE );//$NON-NLS-1$
          continue;
        }
      }
      else
      {
        name = name + "." + this.language.getName ().toLowerCase (); //$NON-NLS-1$
      }

      // try to create the new file
      try
      {
        outfile = new File ( f.getParent (), name );
        if ( !outfile.createNewFile () )
        {
          int j = JOptionPane
              .showConfirmDialog ( this.mypanel,
                  java.util.ResourceBundle.getBundle (
                      "de/unisiegen/tpml/ui/ui" ).getString ( "The_File" )//$NON-NLS-1$//$NON-NLS-2$
                      + " \""//$NON-NLS-1$
                      + outfile.getName ()
                      + "\" "//$NON-NLS-1$
                      + java.util.ResourceBundle.getBundle (
                          "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
                          "alreadyExists" ), java.util.ResourceBundle//$NON-NLS-1$
                      .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
                          "Overwrite" ), JOptionPane.YES_NO_CANCEL_OPTION,//$NON-NLS-1$
                  JOptionPane.QUESTION_MESSAGE );
          if ( j == JFileChooser.CANCEL_OPTION )
          {
            logger.debug ( "Cancelled overwrite of \"" + outfile.getName ()//$NON-NLS-1$
                + "\"" );//$NON-NLS-1$
            return false;
          }
          else if ( j == JOptionPane.NO_OPTION )
          {
            // next try
            continue;
          }
        }

        // save to the new file
        setFile ( outfile );
        setFileName ( outfile.getName () );
        return writeFile ();
      }
      catch ( IOException e )
      {
        logger.error ( "Selected file could not be created.", e );//$NON-NLS-1$
        JOptionPane.showMessageDialog ( this.mypanel, java.util.ResourceBundle
            .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
                "FileCantBeCreated" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
            "de/unisiegen/tpml/ui/ui" ).getString ( "Save" ),//$NON-NLS-1$//$NON-NLS-2$
            JOptionPane.ERROR_MESSAGE );
        return false;
      }
    }
  }


  /**
   * Writes content of the source panel to a specified file.
   * 
   * @return true if the file could be written
   */
  private boolean writeFile ()
  {
    try
    {
      BufferedWriter out = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( this.file ), "UTF8" ) ); //$NON-NLS-1$
      out.write ( this.code.getText () );
      out.close ();
      this.code.clearHistory ();
      return true;
    }
    catch ( UnsupportedEncodingException e )
    {
      logger.error ( "Could not write to file", e );//$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.mypanel, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "CouldNotWriteToFile" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
          "de/unisiegen/tpml/ui/ui" ).getString ( "WriteFile" ),//$NON-NLS-1$//$NON-NLS-2$
          JOptionPane.ERROR_MESSAGE );
      return false;
    }
    catch ( IOException e )
    {
      logger.error ( "Could not write to file", e );//$NON-NLS-1$
      JOptionPane.showMessageDialog ( this.mypanel, java.util.ResourceBundle
          .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
              "CouldNotWriteToFile" ), java.util.ResourceBundle.getBundle ( //$NON-NLS-1$
          "de/unisiegen/tpml/ui/ui" ).getString ( "WriteFile" ),//$NON-NLS-1$//$NON-NLS-2$
          JOptionPane.ERROR_MESSAGE );
      return false;
    }
  }


  /**
   * prints the current editor content
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handlePrint()
   */
  public void handlePrint ()
  {
    GeneralPrinter printer = new GeneralPrinter ( this.mypanel );
    if ( isTexteditor () )
    {
      printer.print ( this.code.getEditor () );
    }
    else
    {
      printer.print ( ( ( AbstractProofComponent ) getComponent ()
          .getPrintPart () ) );
    }

  }


  /**
   * handles the latex export
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#handleLatexExport()
   */
  public void handleLatexExport ()
  {
    JOptionPane
        .showConfirmDialog (
            ( Component ) this.unify,
            "Latex export on unify is disabled", "Latex Export disabled", JOptionPane.OK_OPTION ); //$NON-NLS-1$ //$NON-NLS-2$
    return;
    // if ( isTexteditor () )
    // {
    // try
    // {
    // Expression exp = this.code.getDocument ().getExpression ();
    // GeneralLaTex laTex = new GeneralLaTex ( exp, this.mypanel, true );
    // laTex.export ();
    // }
    // catch ( Exception e )
    // {
    // // no real expression
    //
    // JOptionPane.showMessageDialog ( this.mypanel, java.util.ResourceBundle
    // .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( //$NON-NLS-1$
    // "CouldNotLaTeXExpression" ), "Editor",//$NON-NLS-1$//$NON-NLS-2$
    // JOptionPane.ERROR_MESSAGE );
    //
    // // JOptionPane.showMessageDialog(mypanel, "Sorry, no Expression
    // // enterd!");
    // }
    // }
    // else
    // {
    // GeneralLaTex laTex = new GeneralLaTex (
    // ( ( ProofViewComponent ) getComponent () ).getModel (), this.mypanel,
    // false );
    // laTex.export ();
    // }

  }


  /**
   * TODO: implement, checkSourceCode ??
   */
  public void selectUnify ()
  {
    setTexteditor ( false );
    setComponent ( this.unify );
    deselectButtons ();
    this.mypanel.unifyButton.setSelected ( true );
  }


  /**
   * selects the panel containing the source code input
   * 
   * @see de.unisiegen.tpml.ui.EditorPanel#selectCode()
   */
  public void selectCode ()
  {
    setTexteditor ( true );
    setComponent ( this.code );
    deselectButtons ();
    this.mypanel.codeButton.setSelected ( true );
    this.code.getEditor ().requestFocus ();
  }


  /**
   * requests the underlying panel
   * 
   * @return the underlying panel
   * @see de.unisiegen.tpml.ui.EditorPanel#getPanel()
   */
  public JPanel getPanel ()
  {
    return this.mypanel;

  }
}
