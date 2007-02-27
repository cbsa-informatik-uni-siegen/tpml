package de.unisiegen.tpml.ui.editor ;


import java.awt.BorderLayout ;
import java.awt.datatransfer.Clipboard ;
import java.awt.datatransfer.ClipboardOwner ;
import java.awt.datatransfer.DataFlavor ;
import java.awt.datatransfer.StringSelection ;
import java.awt.datatransfer.Transferable ;
import java.awt.datatransfer.UnsupportedFlavorException ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.io.IOException ;
import java.util.Stack ;
import javax.swing.JMenuItem ;
import javax.swing.JPanel ;
import javax.swing.JPopupMenu ;
import javax.swing.JScrollPane ;
import javax.swing.JSeparator ;
import javax.swing.JSplitPane ;
import javax.swing.event.DocumentEvent ;
import javax.swing.event.DocumentListener ;
import javax.swing.text.BadLocationException ;
import org.apache.log4j.Logger ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.graphics.StyledLanguageDocument ;
import de.unisiegen.tpml.graphics.StyledLanguageEditor ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.ui.EditorComponent ;
import de.unisiegen.tpml.ui.SideBar ;
import de.unisiegen.tpml.ui.SideBarListener ;


/**
 * The Editor displayed in the Source Tab.
 * 
 * @author Christoph Fehling
 * @author Christian Fehler
 * @version $Rev: 1249 $
 * @see de.unisiegen.tpml.ui.EditorComponent
 */
public class TextEditorPanel extends JPanel implements EditorComponent ,
    ClipboardOwner
{
  //
  // Constants
  //
  /**
   * The {@link Logger} for this class.
   */
  private static final Logger logger = Logger
      .getLogger ( TextEditorPanel.class ) ;


  /**
   * The serial version Identifier.
   */
  private static final long serialVersionUID = - 4886621661465144817L ;


  //
  // Attributes
  //
  private StyledLanguageEditor editor ;


  private StyledLanguageDocument document ;


  private JScrollPane scrollpane ;


  private SideBar sideBar ;


  /**
   * The initial content of this file
   */
  private String initialContent = "" ;


  private String currentContent = "" ;


  private Stack < String > undohistory = new Stack < String > ( ) ;


  private Stack < String > redohistory = new Stack < String > ( ) ;


  private TextDocumentListener doclistener = new TextDocumentListener ( ) ;


  private boolean nextStatus ;


  private boolean redoStatus ;


  private boolean undoStatus ;


  private boolean changed ;


  private JPopupMenu popup ;


  private JMenuItem undoItem ;


  private JMenuItem redoItem ;


  /**
   * The {@link Outline} of this view.
   */
  private Outline outline ;


  /**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane ;


  /**
   * Editor with syntax highlighting and undo/redo history.
   */
  public TextEditorPanel ( Language language )
  {
    if ( language == null )
      throw new NullPointerException ( "language is null" ) ;
    setLayout ( new BorderLayout ( ) ) ;
    initComponents ( language ) ;
  }


  private void initComponents ( Language language )
  {
    this.editor = new StyledLanguageEditor ( ) ;
    this.document = new StyledLanguageDocument ( language ) ;
    JPanel compoundPanel = new JPanel ( ) ;
    compoundPanel.setLayout ( new BorderLayout ( ) ) ;
    this.scrollpane = new JScrollPane ( ) ;
    compoundPanel.add ( this.scrollpane , BorderLayout.CENTER ) ;
    this.sideBar = new SideBar ( this.scrollpane , this.document , this.editor ) ;
    this.sideBar.addSibeBarListener ( new SideBarListener ( )
    {
      public void markText ( int left , int right )
      {
        TextEditorPanel.this.selectErrorText ( left , right ) ;
      }
    } ) ;
    compoundPanel.add ( this.sideBar , BorderLayout.WEST ) ;
    // this.scrollpane.setHorizontalScrollBarPolicy (
    // JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ) ;
    this.scrollpane.setViewportView ( this.editor ) ;
    this.editor.setDocument ( this.document ) ;
    this.editor.setAutoscrolls ( false ) ;
    this.outline = new DefaultOutline ( Outline.Start.EDITOR ) ;
    JPanel jPanelOutline = this.outline.getJPanelOutline ( ) ;
    this.editor.addMouseListener ( new OutlineMouseListener ( this ) ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.jSplitPane.setLeftComponent ( compoundPanel ) ;
    this.jSplitPane.setRightComponent ( jPanelOutline ) ;
    this.jSplitPane.setOneTouchExpandable ( true ) ;
    this.jSplitPane.setResizeWeight ( 0.5 ) ;
    this.addPropertyChangeListener ( new OutlinePropertyChangeListener (
        this.jSplitPane , this.outline ) ) ;
    jPanelOutline.addComponentListener ( new OutlineComponentListener (
        this.jSplitPane , this.outline ) ) ;
    this.document.addDocumentListener ( this.doclistener ) ;
    this.undohistory.push ( "" ) ;
    // the popup menu and listeners
    this.popup = new JPopupMenu ( ) ;
    MenuListener menulistener = new MenuListener ( ) ;
    this.undoItem = new JMenuItem ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Undo" ) ) ;
    this.redoItem = new JMenuItem ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Redo" ) ) ;
    JSeparator separator = new JSeparator ( ) ;
    JMenuItem copyItem = new JMenuItem ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Copy" ) ) ;
    JMenuItem cutItem = new JMenuItem ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Cut" ) ) ;
    JMenuItem pasteItem = new JMenuItem ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Paste" ) ) ;
    this.undoItem.addActionListener ( menulistener ) ;
    this.undoItem.setEnabled ( false ) ;
    this.undoItem.setIcon ( new javax.swing.ImageIcon ( getClass ( )
        .getResource ( "/de/unisiegen/tpml/ui/icons/undo16.gif" ) ) ) ;
    this.redoItem.addActionListener ( menulistener ) ;
    this.redoItem.setEnabled ( false ) ;
    this.redoItem.setIcon ( new javax.swing.ImageIcon ( getClass ( )
        .getResource ( "/de/unisiegen/tpml/ui/icons/redo16.gif" ) ) ) ;
    copyItem.addActionListener ( menulistener ) ;
    copyItem.setIcon ( new javax.swing.ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/copy16.gif" ) ) ) ;
    cutItem.addActionListener ( menulistener ) ;
    cutItem.setIcon ( new javax.swing.ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/cut16.gif" ) ) ) ;
    pasteItem.addActionListener ( menulistener ) ;
    pasteItem.setIcon ( new javax.swing.ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/paste16.gif" ) ) ) ;
    this.popup.add ( this.undoItem ) ;
    this.popup.add ( this.redoItem ) ;
    this.popup.add ( separator ) ;
    this.popup.add ( copyItem ) ;
    this.popup.add ( cutItem ) ;
    this.popup.add ( pasteItem ) ;
    this.editor.addMouseListener ( new PopupListener ( ) ) ;
    add ( this.jSplitPane , BorderLayout.CENTER ) ;
  }


  private void selectErrorText ( int left , int right )
  {
    this.editor.select ( left , right ) ;
  }


  public boolean isNextStatus ( )
  {
    return this.nextStatus ;
  }


  private void setNextStatus ( boolean nextStatus )
  {
    if ( this.nextStatus != nextStatus )
    {
      boolean oldNextStatus = this.nextStatus ;
      this.nextStatus = nextStatus ;
      firePropertyChange ( "nextStatus" , oldNextStatus , nextStatus ) ;
    }
  }


  /**
   * {@inheritDoc} This method always returns false, because <i>Pong</i> cannot
   * be played from the editor.
   * 
   * @see de.unisiegen.tpml.ui.EditorComponent#isPongStatus()
   */
  public boolean isPongStatus ( )
  {
    return false ;
  }


  public boolean isRedoStatus ( )
  {
    return this.redoStatus ;
  }


  private void setRedoStatus ( boolean redoStatus )
  {
    if ( this.redoStatus != redoStatus )
    {
      boolean oldRedoStatus = this.redoStatus ;
      this.redoStatus = redoStatus ;
      firePropertyChange ( "redoStatus" , oldRedoStatus , redoStatus ) ;
    }
    this.redoItem.setEnabled ( this.redoStatus ) ;
  }


  public boolean isUndoStatus ( )
  {
    return this.undoStatus ;
  }


  private void setUndoStatus ( boolean undoStatus )
  {
    if ( this.undoStatus != undoStatus )
    {
      boolean oldUndoStatus = this.undoStatus ;
      this.undoStatus = undoStatus ;
      firePropertyChange ( "undoStatus" , oldUndoStatus , undoStatus ) ;
    }
    this.undoItem.setEnabled ( this.undoStatus ) ;
  }


  public void clearHistory ( )
  {
    undohistory.clear ( ) ;
    redohistory.clear ( ) ;
    setUndoStatus ( false ) ;
  }


  public void setDefaultStates ( )
  {
    // setChanged(false);
    setUndoStatus ( false ) ;
    setRedoStatus ( false ) ;
    setNextStatus ( false ) ;
  }


  // public void setChanged(boolean changeStatus) {
  // firePropertyChange("changed", this.changed, changeStatus);
  // this.changed = changeStatus;
  // }
  public boolean isChanged ( )
  {
    return this.changed ;
  }


  public String getText ( )
  {
    try
    {
      return this.document.getText ( 0 , this.document.getLength ( ) ) ;
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Cannot get Text from document" , e ) ;
    }
    return "" ;
  }


  public String getSelectedText ( )
  {
    return this.editor.getSelectedText ( ) ;
  }


  public void insertText ( String text )
  {
    try
    {
      this.document.insertString ( this.editor.getCaretPosition ( ) , text ,
          null ) ;
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Text could not be inserted into document" , e ) ;
    }
  }


  public void removeSelectedText ( )
  {
    int start = this.editor.getSelectionStart ( ) ;
    int end = this.editor.getSelectionEnd ( ) ;
    try
    {
      if ( start < end )
      {
        this.document.remove ( start , ( end - start ) ) ;
      }
      else
      {
        this.document.remove ( end , ( start - end ) ) ;
      }
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Cannot remove text from document" , e ) ;
    }
  }


  public void setText ( String text )
  {
    try
    {
      this.initialContent = text ;
      this.currentContent = text ;
      this.document.removeDocumentListener ( this.doclistener ) ;
      this.document.remove ( 0 , this.document.getLength ( ) ) ;
      this.document.insertString ( 0 , text , null ) ;
      setRedoStatus ( false ) ;
      this.redohistory.clear ( ) ;
      setUndoStatus ( false ) ;
      this.undohistory.clear ( ) ;
      this.undohistory.push ( text ) ;
      this.document.addDocumentListener ( this.doclistener ) ;
      loadOutlineExpression ( Outline.Execute.INIT_EDITOR ) ;
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Cannot set Text of the document" , e ) ;
    }
  }


  public StyledLanguageEditor getEditor ( )
  {
    return this.editor ;
  }


  /**
   * The Next Function is not implemented!
   * 
   * @author Christoph Fehling
   * @version $Rev$
   */
  public void handleNext ( )
  {
    // this function is not implemented here
  }


  public void handleRedo ( )
  {
    try
    {
      this.document.removeDocumentListener ( this.doclistener ) ;
      this.undohistory.push ( this.document.getText ( 0 , this.document
          .getLength ( ) ) ) ;
      this.document.remove ( 0 , this.document.getLength ( ) ) ;
      this.document.insertString ( 0 , this.redohistory.pop ( ) , null ) ;
      setUndoStatus ( true ) ;
      this.document.addDocumentListener ( this.doclistener ) ;
      loadOutlineExpression ( Outline.Execute.AUTO_CHANGE_EDITOR ) ;
      if ( this.redohistory.size ( ) == 0 )
      {
        setRedoStatus ( false ) ;
      }
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Cannot handle an undo" , e ) ;
    }
  }


  public void handleUndo ( )
  {
    try
    {
      this.document.removeDocumentListener ( this.doclistener ) ;
      String doctext = this.document.getText ( 0 , this.document.getLength ( ) ) ;
      String historytext ;
      if ( this.undohistory.peek ( ).equals ( this.initialContent ) )
      {
        historytext = this.undohistory.peek ( ) ;
        setUndoStatus ( false ) ;
      }
      else
      {
        historytext = this.undohistory.pop ( ) ;
      }
      this.document.remove ( 0 , this.document.getLength ( ) ) ;
      this.document.insertString ( 0 , historytext , null ) ;
      this.redohistory.add ( doctext ) ;
      setRedoStatus ( true ) ;
      this.document.addDocumentListener ( this.doclistener ) ;
      loadOutlineExpression ( Outline.Execute.AUTO_CHANGE_EDITOR ) ;
    }
    catch ( BadLocationException e )
    {
      logger.error ( "Cannot handle an undo" , e ) ;
    }
  }


  public void handleCut ( )
  {
    Clipboard clipboard = getToolkit ( ).getSystemClipboard ( ) ;
    StringSelection stringSelection = new StringSelection ( getSelectedText ( ) ) ;
    clipboard.setContents ( stringSelection , this ) ;
    removeSelectedText ( ) ;
  }


  public void handleCopy ( )
  {
    Clipboard clipboard = getToolkit ( ).getSystemClipboard ( ) ;
    StringSelection stringSelection = new StringSelection ( getSelectedText ( ) ) ;
    clipboard.setContents ( stringSelection , this ) ;
  }


  public void handlePaste ( )
  {
    Clipboard clipboard = getToolkit ( ).getSystemClipboard ( ) ;
    Transferable contents = clipboard.getContents ( null ) ;
    boolean hasTransferableText = ( contents != null )
        && contents.isDataFlavorSupported ( DataFlavor.stringFlavor ) ;
    if ( hasTransferableText )
    {
      try
      {
        insertText ( ( String ) contents
            .getTransferData ( DataFlavor.stringFlavor ) ) ;
      }
      catch ( UnsupportedFlavorException ex )
      {
        logger.error ( "Can not paste from clipboard" , ex ) ;
      }
      catch ( IOException ex )
      {
        logger.error ( "Can not paste from clipboard" , ex ) ;
      }
    }
  }


  public StyledLanguageDocument getDocument ( )
  {
    return this.document ;
  }


  /**
   * Loads a new {@link Expression} into the {@link Outline}.
   * 
   * @param pExecute Indicates who loads the new Expression.
   */
  protected void loadOutlineExpression ( Outline.Execute pExecute )
  {
    try
    {
      TextEditorPanel.this.outline.loadExpression (
          TextEditorPanel.this.document.getExpression ( ) , pExecute ) ;
    }
    catch ( Exception e )
    {
      TextEditorPanel.this.outline.loadExpression ( null , pExecute ) ;
    }
  }


  private class TextDocumentListener implements DocumentListener
  {
    public void insertUpdate ( DocumentEvent arg0 )
    {
      logger.debug ( "Text inserted into document" ) ;
      try
      {
        TextEditorPanel.this.setUndoStatus ( true ) ;
        String doctext = arg0.getDocument ( ).getText ( 0 ,
            arg0.getDocument ( ).getLength ( ) ) ;
        if ( doctext.endsWith ( " " ) )
        {
          TextEditorPanel.this.undohistory.push ( doctext ) ;
          logger.debug ( "history added: " + doctext ) ;
        }
        setRedoStatus ( false ) ;
        TextEditorPanel.this.redohistory.clear ( ) ;
        TextEditorPanel.this.currentContent = doctext ;
        loadOutlineExpression ( Outline.Execute.AUTO_CHANGE_EDITOR ) ;
      }
      catch ( BadLocationException e )
      {
        logger.error ( "Failed to add text to undo history" , e ) ;
      }
    }


    public void removeUpdate ( DocumentEvent arg0 )
    {
      logger.debug ( "Text removed from document" ) ;
      try
      {
        TextEditorPanel.this.setUndoStatus ( true ) ;
        TextEditorPanel.this.undohistory
            .push ( TextEditorPanel.this.currentContent ) ;
        setRedoStatus ( false ) ;
        TextEditorPanel.this.redohistory.clear ( ) ;
        TextEditorPanel.this.currentContent = arg0.getDocument ( ).getText ( 0 ,
            arg0.getDocument ( ).getLength ( ) ) ;
        loadOutlineExpression ( Outline.Execute.AUTO_CHANGE_EDITOR ) ;
      }
      catch ( BadLocationException e )
      {
        logger.error ( "Failed to add text to undo history" , e ) ;
      }
    }


    public void changedUpdate ( DocumentEvent arg0 )
    {
      logger.debug ( "Document was changed" ) ;
    }
  }


  private class PopupListener extends MouseAdapter
  {
    @ Override
    public void mousePressed ( MouseEvent e )
    {
      maybeShowPopup ( e ) ;
    }


    @ Override
    public void mouseReleased ( MouseEvent e )
    {
      maybeShowPopup ( e ) ;
    }


    private void maybeShowPopup ( MouseEvent e )
    {
      if ( e.isPopupTrigger ( ) )
      {
        TextEditorPanel.this.popup.show ( e.getComponent ( ) , e.getX ( ) , e
            .getY ( ) ) ;
      }
    }
  }


  private class MenuListener implements ActionListener
  {
    public void actionPerformed ( ActionEvent evt )
    {
      String command = evt.getActionCommand ( ) ;
      if ( command.equals ( java.util.ResourceBundle.getBundle (
          "de/unisiegen/tpml/ui/ui" ).getString ( "Copy" ) ) )
      {
        handleCopy ( ) ;
      }
      else if ( command.equals ( java.util.ResourceBundle.getBundle (
          "de/unisiegen/tpml/ui/ui" ).getString ( "Cut" ) ) )
      {
        handleCut ( ) ;
      }
      else if ( command.equals ( java.util.ResourceBundle.getBundle (
          "de/unisiegen/tpml/ui/ui" ).getString ( "Paste" ) ) )
      {
        handlePaste ( ) ;
      }
      else if ( command.equals ( java.util.ResourceBundle.getBundle (
          "de/unisiegen/tpml/ui/ui" ).getString ( "Undo" ) ) )
      {
        handleUndo ( ) ;
      }
      else if ( command.equals ( java.util.ResourceBundle.getBundle (
          "de/unisiegen/tpml/ui/ui" ).getString ( "Redo" ) ) )
      {
        handleRedo ( ) ;
      }
    }
  }


  public void lostOwnership ( Clipboard arg0 , Transferable arg1 )
  {
    // we do not care so we do nothing
  }


  public void setAdvanced ( boolean status )
  {
    // the editor does not have an advanced mode so this is ignored.
  }


  /**
   * Returns the outline.
   * 
   * @return The outline.
   * @see #outline
   */
  public Outline getOutline ( )
  {
    return this.outline ;
  }
}
