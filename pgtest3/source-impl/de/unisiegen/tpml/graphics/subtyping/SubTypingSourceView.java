package de.unisiegen.tpml.graphics.subtyping;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.StringReader;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;
import de.unisiegen.tpml.ui.EditorComponent;
import de.unisiegen.tpml.ui.MainWindow;
import de.unisiegen.tpml.ui.SideBar;
import de.unisiegen.tpml.ui.SideBarListener;

/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the suptyping interpreter user interface.
 * 
 * The <code>SubTyping</code> component.
 * 
 * 
 * @author Christian Fehler
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class SubTypingSourceView extends JPanel // AbstractProofView //JComponent
		implements EditorComponent, ClipboardOwner {
	/**
	 * The unique serialization identifier for this class.
	 */
	private static final long serialVersionUID = -425214200136389228L;

	/** 
	 * Panel and label for type enter fields
	 */
	JPanel source;

	JLabel sourceLabel;

	JLabel sourceLabel2;

	/**
	 * The type enter fields
	 */
	StyledLanguageEditor editor;

	StyledLanguageEditor editor2;

	StyledTypeEnterField sourceField;

	StyledTypeEnterField sourceField2;

	JScrollPane scrollPane1;

	JScrollPane scrollPane2;

	SideBar sidebar;

	SideBar sidebar2;

	OwnDocumentListener listener;

	OwnDocumentListener2 listener2;

	/**
	 * The actual parsed types of this view
	 */
	MonoType type;

	MonoType type2;

	/**
	 * Reference to the mainwindow
	 */
	MainWindow window;

	/**
	 * Status for save redo and undo
	 */
	boolean saveStatus;

	boolean nextStatus;

	boolean undoStatus;

	boolean undoStatus1 = false;

	boolean undoStatus2 = false;

	boolean redoStatus;

	boolean redoStatus1 = false;

	boolean redoStatus2 = false;

	/**
	 * The initial content of this file
	 */
	private String initialContent = ""; //$NON-NLS-1$

	private String currentContent = ""; //$NON-NLS-1$

	/**
	 * The <code>JScrollPane</code> for the <code>component</code>.
	 */
	protected JScrollPane scrollPane;

	/**
	 * The <code>JSplitPane</code> for the <code>component</code>.
	 */
	private JSplitPane jSplitPane;

	/**
	 * The {@link Outline} of this view.
	 * 
	 * @see #getOutline()
	 */
	private DefaultOutline outline;

	private DefaultOutline outline2;

	private JPanel outlinePanel;

	/**
	 * The actual language for this view
	 */
	private Language language;

	/**
	 * The stacks for redo and undo
	 */
	private Stack < String > undohistory = new Stack < String > ( );

	private Stack < String > redohistory = new Stack < String > ( );

	private Stack < String > undohistory2 = new Stack < String > ( );

	private Stack < String > redohistory2 = new Stack < String > ( );

	/**
	 * The {@link Logger} for this class.
	 * 
	 * @see Logger
	 */
	protected static final Logger logger = Logger.getLogger ( SubTypingSourceView.class );

	/**
	 * Allocates a new {@link SubTypingSourceView} 
	 * 
	 * @param pLanguage The {@link Language} for the
	 *          <code>SubTypingView</code>.
	 * @param pWindow the MainWindow containing this view
	 */
	@SuppressWarnings ( "synthetic-access" )
	public SubTypingSourceView ( Language pLanguage, MainWindow pWindow ) {
		super ( );
		this.language = pLanguage;
		this.window = pWindow;
		this.undohistory.push ( "" ); //$NON-NLS-1$
		this.undohistory2.push ( "" ); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints = new GridBagConstraints ( );
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT );
		this.setLayout ( new GridBagLayout ( ) );

		this.source = new JPanel ( new GridBagLayout ( ) );

		this.sourceLabel = new JLabel ( Messages.getString ( "firstType" ) ); //$NON-NLS-1$
		gridBagConstraints.insets = new Insets ( 5, 21, 0, 10 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = 2;
		this.source.add ( this.sourceLabel, gridBagConstraints );

		this.sourceLabel2 = new JLabel ( Messages.getString ( "secondType" ) ); //$NON-NLS-1$
		gridBagConstraints.insets = new Insets ( 5, 21, 0, 10 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = 2;
		this.source.add ( this.sourceLabel2, gridBagConstraints );

		this.editor = new StyledLanguageEditor ( );
		this.editor.addFocusListener ( new FocusListener ( ) {
			public void focusGained ( @SuppressWarnings ( "unused" )
			FocusEvent e ) {
				// get the saved status for this editor
				SubTypingSourceView.this.setRedoStatus ( SubTypingSourceView.this.redoStatus1 );
				SubTypingSourceView.this.setUndoStatus ( SubTypingSourceView.this.undoStatus1 );
			}

			/**
			 * Invoked when a component loses the keyboard focus.
			 */
			public void focusLost ( @SuppressWarnings ( "unused" )
			FocusEvent e ) {
				// save the status for this editor
				SubTypingSourceView.this.redoStatus1 = SubTypingSourceView.this.redoStatus;
				SubTypingSourceView.this.undoStatus1 = SubTypingSourceView.this.undoStatus;
			}
		} );

		this.sourceField = new StyledTypeEnterField ( this.language );

		this.scrollPane1 = new JScrollPane ( );
		this.sidebar = new SideBar ( this.scrollPane1, this.sourceField, this.editor );
		this.sidebar.addSideBarListener ( new SideBarListener ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void markText ( int left, int right ) {
				if ( ( SubTypingSourceView.this.editor.getSelectionStart ( ) == left )
						&& ( SubTypingSourceView.this.editor.getSelectionEnd ( ) == right ) ) {
					SubTypingSourceView.this.removeSelectedText ( );
				} else {
					SubTypingSourceView.this.selectErrorText ( left, right );
				}
			}

			@SuppressWarnings ( "synthetic-access" )
			public void insertText ( int pIndex, String pText ) {
				int countSpaces = 0;
				try {
					while ( SubTypingSourceView.this.sourceField.getText ( pIndex + countSpaces, 1 ).equals ( " " ) ) //$NON-NLS-1$
					{
						countSpaces++ ;
					}
				} catch ( BadLocationException e ) {
					// Do nothing
				}
				try {
					int offset = 0;
					String text = pText;
					if ( ( countSpaces >= 1 ) && ( text.substring ( 0, 1 ).equals ( " " ) ) ) //$NON-NLS-1$
					{
						text = text.substring ( 1 );
						offset++ ;
						countSpaces-- ;
					}
					if ( ( countSpaces >= 1 ) && ( text.substring ( text.length ( ) - 1 ).equals ( " " ) ) ) //$NON-NLS-1$
					{
						text = text.substring ( 0, text.length ( ) - 1 );
					}
					SubTypingSourceView.this.sourceField.insertString ( pIndex + offset, text, null );
				} catch ( BadLocationException e ) {
					// Do nothing
				}
			}
		} );

		this.editor.setDocument ( this.sourceField );
		this.listener = new OwnDocumentListener ( );
		this.outline = new DefaultOutline ( this, Outline.Modus.FIRST );
		this.sourceField.addDocumentListener ( this.listener );

		this.scrollPane1.setViewportView ( this.editor );

		gridBagConstraints.insets = new Insets ( 5, 0, 0, 0 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = 1;
		this.source.add ( this.sidebar, gridBagConstraints );

		gridBagConstraints.insets = new Insets ( 5, 5, 5, 5 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;
		this.source.add ( this.scrollPane1, gridBagConstraints );

		this.editor2 = new StyledLanguageEditor ( );
		this.editor2.addFocusListener ( new FocusListener ( ) {
			public void focusGained ( @SuppressWarnings ( "unused" )
			FocusEvent e ) {
				// get the saved status for this editor
				SubTypingSourceView.this.setRedoStatus ( SubTypingSourceView.this.redoStatus2 );
				SubTypingSourceView.this.setUndoStatus ( SubTypingSourceView.this.undoStatus2 );
			}

			public void focusLost ( @SuppressWarnings ( "unused" )
			FocusEvent e ) {
				// save the status for this editor
				SubTypingSourceView.this.redoStatus2 = SubTypingSourceView.this.redoStatus;
				SubTypingSourceView.this.undoStatus2 = SubTypingSourceView.this.undoStatus;
			}
		} );

		this.sourceField2 = new StyledTypeEnterField ( this.language );

		this.scrollPane2 = new JScrollPane ( );
		this.sidebar2 = new SideBar ( this.scrollPane2, this.sourceField2, this.editor2 );
		this.sidebar2.addSideBarListener ( new SideBarListener ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void markText ( int left, int right ) {
				if ( ( SubTypingSourceView.this.editor2.getSelectionStart ( ) == left )
						&& ( SubTypingSourceView.this.editor2.getSelectionEnd ( ) == right ) ) {
					SubTypingSourceView.this.removeSelectedText2 ( );
				} else {
					SubTypingSourceView.this.selectErrorText2 ( left, right );
				}
			}

			@SuppressWarnings ( "synthetic-access" )
			public void insertText ( int pIndex, String pText ) {
				int countSpaces = 0;
				try {
					while ( SubTypingSourceView.this.sourceField2.getText ( pIndex + countSpaces, 1 ).equals ( " " ) ) //$NON-NLS-1$
					{
						countSpaces++ ;
					}
				} catch ( BadLocationException e ) {
					// Do nothing
				}
				try {
					int offset = 0;
					String text = pText;
					if ( ( countSpaces >= 1 ) && ( text.substring ( 0, 1 ).equals ( " " ) ) ) //$NON-NLS-1$
					{
						text = text.substring ( 1 );
						offset++ ;
						countSpaces-- ;
					}
					if ( ( countSpaces >= 1 ) && ( text.substring ( text.length ( ) - 1 ).equals ( " " ) ) ) //$NON-NLS-1$
					{
						text = text.substring ( 0, text.length ( ) - 1 );
					}
					SubTypingSourceView.this.sourceField2.insertString ( pIndex + offset, text, null );
				} catch ( BadLocationException e ) {
					// Do nothing
				}
			}
		} );

		this.editor2.setDocument ( this.sourceField2 );

		this.outline2 = new DefaultOutline ( this, Outline.Modus.SECOND );
		this.outline.setSyncOutline ( this.outline2 );
		this.listener2 = new OwnDocumentListener2 ( );
		this.sourceField2.addDocumentListener ( this.listener2 );

		this.scrollPane2.setViewportView ( this.editor2 );

		gridBagConstraints.insets = new Insets ( 5, 0, 0, 0 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = 1;
		this.source.add ( this.sidebar2, gridBagConstraints );

		gridBagConstraints.insets = new Insets ( 5, 5, 5, 5 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;
		this.source.add ( this.scrollPane2, gridBagConstraints );

		this.outlinePanel = new JPanel ( new GridBagLayout ( ) );

		JScrollPane jPanelOutline = this.outline.getTree ( );

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets ( 0, 0, 0, 2 );
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;

		this.outlinePanel.add ( jPanelOutline, gridBagConstraints );

		JScrollPane jPanelOutline2 = this.outline2.getTree ( );

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets ( 0, 2, 0, 0 );
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;

		this.outlinePanel.add ( jPanelOutline2, gridBagConstraints );

		JPanel preferences = this.outline.getPanelPreferences ( );

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets ( 0, 0, 0, 0 );
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = 2;

		this.outlinePanel.add ( preferences, gridBagConstraints );

		this.jSplitPane.setLeftComponent ( this.source );
		this.jSplitPane.setRightComponent ( this.outlinePanel );
		this.jSplitPane.setOneTouchExpandable ( true );
		this.jSplitPane.setResizeWeight ( 0.5 );
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets ( 0, 0, 0, 0 );
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;

		this.add ( this.jSplitPane, gridBagConstraints );
	}

	/**
	 * Remove the selected text from this editor 
	 */
	public void removeSelectedText ( ) {
		int start = this.editor.getSelectionStart ( );
		int end = this.editor.getSelectionEnd ( );
		try {
			if ( start < end ) {
				this.sourceField.remove ( start, ( end - start ) );
			} else {
				this.sourceField.remove ( end, ( start - end ) );
			}
		} catch ( BadLocationException e ) {
			// Do nothing
		}
	}

	/**
	 * Remove the selected text from this editor 
	 */
	public void removeSelectedText2 ( ) {
		int start = this.editor2.getSelectionStart ( );
		int end = this.editor2.getSelectionEnd ( );
		try {
			if ( start < end ) {
				this.sourceField2.remove ( start, ( end - start ) );
			} else {
				this.sourceField2.remove ( end, ( start - end ) );
			}
		} catch ( BadLocationException e ) {
			// Do nothing
		}
	}

	/**
	 * Returns the jSplitPane.
	 * 
	 * @return The jSplitPane.
	 * @see #jSplitPane
	 */
	public JSplitPane getJSplitPane ( ) {
		return this.jSplitPane;
	}

	/**
	 * Returns the first {@link Outline} of this view.
	 * 
	 * @return The first {@link Outline} of this view.
	 */
	public Outline getOutline1 ( ) {
		return this.outline;
	}

	/**
	 * Returns the second {@link Outline} of this view.
	 * 
	 * @return The second {@link Outline} of this view.
	 */
	public Outline getOutline2 ( ) {
		return this.outline2;
	}

	private void selectErrorText ( int left, int right ) {
		this.editor.select ( left, right );
	}

	private void selectErrorText2 ( int left, int right ) {
		this.editor2.select ( left, right );
	}

	/**
	 * 
	 * This function is called when an Editor has changed.
	 * We try to parse the new type, and reload the Outline.
	 *
	 * @param pEditor the Editor which has changed
	 * @param pOutline
	 * @return
	 */
	public MonoType eventHandling ( StyledLanguageEditor pEditor, DefaultOutline pOutline ) {
		MonoType newType;
		try {
			LanguageTypeParser parser = this.language.newTypeParser ( new StringReader ( pEditor.getText ( ) ) );
			newType = parser.parse ( );

			pOutline.load ( newType, Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
			return newType;

		} catch ( Exception e ) {

			pOutline.load ( null, Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
			return null;
		}
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 * @see #editor
	 */
	public StyledLanguageEditor getEditor ( ) {
		return this.editor;
	}

	/**
	 * Returns the editor2.
	 *
	 * @return The editor2.
	 * @see #editor2
	 */
	public StyledLanguageEditor getEditor2 ( ) {
		return this.editor2;
	}

	/**
	 * 
	 * Get the first type of this view
	 *
	 * @return type MonoType the first type of this node
	 */
	public MonoType getType ( ) {
		return this.type;
	}

	/**
	 * 
	 * Get the second type of this view
	 *
	 * @return type MonoType the second type of this node
	 */
	public MonoType getType2 ( ) {
		return this.type2;
	}

	/**
	 * 
	 * Set the text of the first Editor
	 *
	 * @param text String with the text to set
	 */
	public void setText ( String text ) {
		try {
			this.sourceField.removeDocumentListener ( this.listener );
			this.sourceField.remove ( 0, this.sourceField.getLength ( ) );
			this.sourceField.insertString ( 0, text, null );
			this.sourceField.addDocumentListener ( this.listener );
			this.outline.load ( this.sourceField.getType ( ), Outline.ExecuteInit.SUBTYPING_SOURCE );
		} catch ( Exception e ) {
			logger.error ( "Cannot set Text of the document", e ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * Set the text of the second Editor
	 *
	 * @param text String with the text to set
	 */
	public void setText2 ( String text ) {
		try {
			this.sourceField2.removeDocumentListener ( this.listener2 );
			this.sourceField2.remove ( 0, this.sourceField2.getLength ( ) );
			this.sourceField2.insertString ( 0, text, null );
			this.sourceField2.addDocumentListener ( this.listener2 );
			this.outline2.load ( this.sourceField2.getType ( ), Outline.ExecuteInit.SUBTYPING_SOURCE );
		} catch ( Exception e ) {
			logger.error ( "Cannot set Text of the document", e ); //$NON-NLS-1$
		}
	}

	/**
	 * Get the content of the first sourceField
	 *
	 * @return content of the first sourceField
	 * 
	 */
	public String getText ( ) {

		try {
			return this.sourceField.getText ( 0, this.sourceField.getLength ( ) );
		} catch ( BadLocationException e ) {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Get the content of the second sourceField
	 *
	 * @return content of the second sourceField
	 * 
	 */
	public String getText2 ( ) {

		try {
			return this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) );
		} catch ( BadLocationException e ) {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc} This method always returns false, because <i>Pong</i> cannot
	 * be played from the editor.
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isPongStatus()
	 */
	public boolean isPongStatus ( ) {
		return false;
	}

	/**
	 * 
	 * Set the first {@link MonoType} of this SourceView
	 *
	 * @param type first type of this source view
	 */
	public void setType ( MonoType type ) {
		this.type = type;
	}

	/**
	 * 
	 * Set the second {@link MonoType} of this SourceView
	 *
	 * @param type2 second type of this source view
	 */
	public void setType2 ( MonoType type2 ) {
		this.type2 = type2;
	}

	/**
	 * Gives information if document has changed since last save.
	 *
	 * @return boolean has changed or not
	 */
	public boolean isSaveStatus ( ) {
		return this.saveStatus;
	}

	/**
	 * Set a new save status for this document.
	 *
	 * @param saveStatus boolean new save status
	 */
	public void setSaveStatus ( boolean saveStatus ) {
		this.saveStatus = saveStatus;
	}

	/**
	 * The Next Function is not implemented!
	 * 
	 */
	public void handleNext ( ) {
	// this function is not implemented here
	}

	/**
	 * 
	 * The editor does not have an advanced mode so this is ignored.
	 *
	 */
	public void setAdvanced ( @SuppressWarnings ( "unused" )
	boolean status ) {
	// the editor does not have an advanced mode so this is ignored.
	}

	/**
	 * The lostOwnership Function is not implemented!
	 *
	 * @param arg0 will be ignored
	 * @param arg1 will be ignored
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership ( @SuppressWarnings ( "unused" )
	Clipboard arg0, @SuppressWarnings ( "unused" )
	Transferable arg1 ) {
	// we do not care so we do nothing
	}

	/**
	 * Get the selected text of the editor which has the focus
	 *
	 * @return selected text of the focused editor
	 */
	public String getSelectedText ( ) {
		if ( this.editor.hasFocus ( ) )
			return this.editor.getSelectedText ( );
		return this.editor2.getSelectedText ( );
	}

	/**
	 * Copy the selected text to Clipboard
	 *
	 */
	public void handleCopy ( ) {
		Clipboard clipboard = getToolkit ( ).getSystemClipboard ( );
		StringSelection stringSelection = new StringSelection ( getSelectedText ( ) );
		clipboard.setContents ( stringSelection, this );
	}

	/**
	 * Copy the selected text to Clipboard and remove this text.
	 *
	 */
	public void handleCut ( ) {
		Clipboard clipboard = getToolkit ( ).getSystemClipboard ( );
		StringSelection stringSelection = new StringSelection ( getSelectedText ( ) );
		clipboard.setContents ( stringSelection, this );
		try {
			if ( this.editor.hasFocus ( ) ) {
				this.type = eventHandling ( this.editor, this.outline );
				this.window.setChangeState ( Boolean.TRUE );
				this.saveStatus = true;
				this.setUndoStatus ( true );
				this.undohistory.push ( this.currentContent );
				setRedoStatus ( false );
				this.redohistory.clear ( );
				this.currentContent = this.sourceField.getText ( 0, this.sourceField.getLength ( ) );
				removeSelectedText ( );
			} else if ( this.editor2.hasFocus ( ) ) {
				this.type2 = eventHandling ( this.editor2, this.outline2 );
				this.window.setChangeState ( Boolean.TRUE );
				this.saveStatus = true;
				this.setUndoStatus ( true );
				this.undohistory2.push ( this.currentContent );
				setRedoStatus ( false );
				this.redohistory2.clear ( );
				this.currentContent = this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) );
				removeSelectedText2 ( );
			} else {
				logger.error ( "Failed to add text to undo history2" ); //$NON-NLS-1$
			}
		} catch ( Exception e ) {
			logger.error ( "Failed to add text to undo history2", e ); //$NON-NLS-1$
		}
	}

	/**
	 * Paste the text of the Clipboard to the editor which has the focus 
	 *
	 */
	@SuppressWarnings ( "null" )
	public void handlePaste ( ) {
		Clipboard clipboard = getToolkit ( ).getSystemClipboard ( );
		Transferable contents = clipboard.getContents ( null );
		boolean hasTransferableText = ( contents != null ) && contents.isDataFlavorSupported ( DataFlavor.stringFlavor );
		if ( hasTransferableText ) {
			try {
				if ( this.editor.hasFocus ( ) ) {
					insertText ( ( String ) contents.getTransferData ( DataFlavor.stringFlavor ), this.sourceField,
							this.editor );
					SubTypingSourceView.this.type = eventHandling ( SubTypingSourceView.this.editor,
							SubTypingSourceView.this.outline );
					SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
					SubTypingSourceView.this.saveStatus = true;
					SubTypingSourceView.this.setUndoStatus ( true );
					String doctext = this.sourceField.getText ( 0, this.sourceField.getLength ( ) );
					if ( doctext.endsWith ( " " ) ) //$NON-NLS-1$
					{
						SubTypingSourceView.this.undohistory.push ( doctext );
						logger.debug ( "history added: " + doctext ); //$NON-NLS-1$
					}
					setRedoStatus ( false );
					SubTypingSourceView.this.redohistory.clear ( );
					SubTypingSourceView.this.currentContent = doctext;
				} else if ( this.editor2.hasFocus ( ) ) {
					insertText ( ( String ) contents.getTransferData ( DataFlavor.stringFlavor ), this.sourceField2,
							this.editor2 );
					SubTypingSourceView.this.type2 = eventHandling ( SubTypingSourceView.this.editor2,
							SubTypingSourceView.this.outline2 );
					SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
					SubTypingSourceView.this.saveStatus = true;
					SubTypingSourceView.this.setUndoStatus ( true );
					String doctext = this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) );
					if ( doctext.endsWith ( " " ) ) //$NON-NLS-1$
					{
						SubTypingSourceView.this.undohistory2.push ( doctext );
						logger.debug ( "history2 added: " + doctext ); //$NON-NLS-1$
					}
					setRedoStatus ( false );
					SubTypingSourceView.this.redohistory2.clear ( );
					SubTypingSourceView.this.currentContent = doctext;
				} else {
					logger.error ( "Can not paste from clipboard" ); //$NON-NLS-1$
					return;
				}

			} catch ( Exception ex ) {
				logger.error ( "Can not paste from clipboard", ex ); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 
	 * Insert a String to the given language styled editor
	 *
	 * @param text the text to insert
	 * @param document StyledTypeEnterField the document to insert the text
	 * @param styledEditor the StyledLanguageEditor containing the document
	 */
	public void insertText ( String text, StyledTypeEnterField document, StyledLanguageEditor styledEditor ) {
		try {
			document.insertString ( styledEditor.getCaretPosition ( ), text, null );
		} catch ( BadLocationException e ) {
			logger.error ( "Text could not be inserted into document", e ); //$NON-NLS-1$
		}
	}

	private void setNextStatus ( boolean nextStatus ) {
		if ( this.nextStatus != nextStatus ) {
			boolean oldNextStatus = this.nextStatus;
			this.nextStatus = nextStatus;
			firePropertyChange ( "nextStatus", oldNextStatus, nextStatus ); //$NON-NLS-1$
		}
	}

	private void setRedoStatus ( boolean redoStatus ) {
		if ( this.redoStatus != redoStatus ) {
			boolean oldRedoStatus = this.redoStatus;
			this.redoStatus = redoStatus;
			firePropertyChange ( "redoStatus", oldRedoStatus, redoStatus ); //$NON-NLS-1$
		}
		//this.redoItem.setEnabled ( this.redoStatus );
	}

	private void setUndoStatus ( boolean undoStatus ) {
		if ( this.undoStatus != undoStatus ) {
			boolean oldUndoStatus = this.undoStatus;
			this.undoStatus = undoStatus;
			firePropertyChange ( "undoStatus", oldUndoStatus, undoStatus ); //$NON-NLS-1$
		}
		//this.undoItem.setEnabled ( this.undoStatus );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isNextStatus()
	 */
	public boolean isNextStatus ( ) {
		return this.nextStatus;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isRedoStatus()
	 */
	public boolean isRedoStatus ( ) {
		return this.redoStatus;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#isUndoStatus()
	 */
	public boolean isUndoStatus ( ) {
		return this.undoStatus;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#setDefaultStates()
	 */
	public void setDefaultStates ( ) {
		// setChanged(false);
		setUndoStatus ( false );
		setRedoStatus ( false );
		setNextStatus ( false );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleRedo()
	 */
	public void handleRedo ( ) {
		try {
			if ( this.editor.hasFocus ( ) ) {
				this.sourceField.removeDocumentListener ( this.listener );
				this.undohistory.push ( this.sourceField.getText ( 0, this.sourceField.getLength ( ) ) );
				this.sourceField.remove ( 0, this.sourceField.getLength ( ) );
				this.sourceField.insertString ( 0, this.redohistory.pop ( ), null );
				setUndoStatus ( true );
				this.sourceField.addDocumentListener ( this.listener );
				this.outline.load ( this.sourceField.getType ( ), Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
				if ( this.redohistory.size ( ) == 0 ) {
					setRedoStatus ( false );
				}
			} else if ( this.editor2.hasFocus ( ) ) {
				this.sourceField2.removeDocumentListener ( this.listener2 );
				this.undohistory2.push ( this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) ) );
				this.sourceField2.remove ( 0, this.sourceField2.getLength ( ) );
				this.sourceField2.insertString ( 0, this.redohistory2.pop ( ), null );
				setUndoStatus ( true );
				this.sourceField2.addDocumentListener ( this.listener2 );
				this.outline2.load ( this.sourceField2.getType ( ), Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
				if ( this.redohistory2.size ( ) == 0 ) {
					setRedoStatus ( false );
				}
			} else {
				logger.error ( "Cannot handle an redo" ); //$NON-NLS-1$
				return;
			}
		} catch ( Exception e ) {
			logger.error ( "Cannot handle an redo", e ); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleUndo()
	 */
	public void handleUndo ( ) {
		try {
			if ( this.editor.hasFocus ( ) ) {
				this.sourceField.removeDocumentListener ( this.listener );
				String doctext = this.sourceField.getText ( 0, this.sourceField.getLength ( ) );
				String historytext;
				if ( this.undohistory.peek ( ).equals ( this.initialContent ) ) {
					historytext = this.undohistory.peek ( );
					setUndoStatus ( false );
				} else {
					historytext = this.undohistory.pop ( );
				}
				this.sourceField.remove ( 0, this.sourceField.getLength ( ) );
				this.sourceField.insertString ( 0, historytext, null );
				this.redohistory.add ( doctext );
				setRedoStatus ( true );
				this.sourceField.addDocumentListener ( this.listener );
				this.outline.load ( this.sourceField.getType ( ), Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
			} else if ( this.editor2.hasFocus ( ) ) {
				this.sourceField2.removeDocumentListener ( this.listener2 );
				String doctext = this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) );
				String historytext;
				if ( this.undohistory2.peek ( ).equals ( this.initialContent ) ) {
					historytext = this.undohistory2.peek ( );
					setUndoStatus ( false );
				} else {
					historytext = this.undohistory2.pop ( );
				}
				this.sourceField2.remove ( 0, this.sourceField2.getLength ( ) );
				this.sourceField2.insertString ( 0, historytext, null );
				this.redohistory2.add ( doctext );
				setRedoStatus ( true );
				this.sourceField2.addDocumentListener ( this.listener2 );
				this.outline2.load ( this.sourceField.getType ( ), Outline.ExecuteAutoChange.SUBTYPING_SOURCE );
			} else {
				logger.error ( "Cannot handle an undo" ); //$NON-NLS-1$
				return;
			}
		} catch ( Exception e ) {
			e.printStackTrace ( );
			logger.error ( "Cannot handle an undo", e ); //$NON-NLS-1$
		}
	}

	private class OwnDocumentListener implements DocumentListener {

		/**
		 * @inherit Doc
		 * 
		 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		public void changedUpdate ( @SuppressWarnings ( "unused" )
		DocumentEvent e ) {
			logger.debug ( "Document was changed" ); //$NON-NLS-1$
		}

		/**
		 * @inherit Doc
		 *  
		 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		@SuppressWarnings ( "synthetic-access" )
		public void insertUpdate ( DocumentEvent arg0 ) {
			try {
				SubTypingSourceView.this.type = eventHandling ( SubTypingSourceView.this.editor,
						SubTypingSourceView.this.outline );
				SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
				SubTypingSourceView.this.saveStatus = true;
				SubTypingSourceView.this.setUndoStatus ( true );
				String doctext = arg0.getDocument ( ).getText ( 0, arg0.getDocument ( ).getLength ( ) );
				if ( doctext.endsWith ( " " ) ) //$NON-NLS-1$
				{
					SubTypingSourceView.this.undohistory.push ( doctext );
					logger.debug ( "history added: " + doctext ); //$NON-NLS-1$
				}
				setRedoStatus ( false );
				SubTypingSourceView.this.redohistory.clear ( );
				SubTypingSourceView.this.currentContent = doctext;
			} catch ( BadLocationException e ) {
				logger.error ( "Failed to add text to undo history", e ); //$NON-NLS-1$
			}
		}

		/**
		 * @inherit Doc
		 *
		 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		@SuppressWarnings ( "synthetic-access" )
		public void removeUpdate ( DocumentEvent arg0 ) {
			try {
				SubTypingSourceView.this.type = eventHandling ( SubTypingSourceView.this.editor,
						SubTypingSourceView.this.outline );
				SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
				SubTypingSourceView.this.saveStatus = true;
				SubTypingSourceView.this.setUndoStatus ( true );
				SubTypingSourceView.this.undohistory.push ( SubTypingSourceView.this.currentContent );
				setRedoStatus ( false );
				SubTypingSourceView.this.redohistory.clear ( );
				SubTypingSourceView.this.currentContent = arg0.getDocument ( ).getText ( 0,
						arg0.getDocument ( ).getLength ( ) );
			} catch ( BadLocationException e ) {
				logger.error ( "Failed to add text to undo history", e ); //$NON-NLS-1$
			}
		}
	}

	private class OwnDocumentListener2 implements DocumentListener {

		/**
		 * @inherit Doc
		 * 
		 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		public void changedUpdate ( @SuppressWarnings ( "unused" )
		DocumentEvent e ) {
			logger.debug ( "Document was changed" ); //$NON-NLS-1$
		}

		/**
		 * @inherit Doc
		 *  
		 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		@SuppressWarnings ( "synthetic-access" )
		public void insertUpdate ( @SuppressWarnings ( "unused" )
		DocumentEvent arg0 ) {
			try {
				SubTypingSourceView.this.type2 = eventHandling ( SubTypingSourceView.this.editor2,
						SubTypingSourceView.this.outline2 );
				SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
				SubTypingSourceView.this.saveStatus = true;
				SubTypingSourceView.this.setUndoStatus ( true );
				String doctext = arg0.getDocument ( ).getText ( 0, arg0.getDocument ( ).getLength ( ) );
				if ( doctext.endsWith ( " " ) ) //$NON-NLS-1$
				{
					SubTypingSourceView.this.undohistory2.push ( doctext );
					logger.debug ( "history2 added: " + doctext ); //$NON-NLS-1$
				}
				setRedoStatus ( false );
				SubTypingSourceView.this.redohistory2.clear ( );
				SubTypingSourceView.this.currentContent = doctext;
			} catch ( BadLocationException e ) {
				logger.error ( "Failed to add text to undo history", e ); //$NON-NLS-1$
			}
		}

		/**
		 * @inherit Doc
		 *
		 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		@SuppressWarnings ( "synthetic-access" )
		public void removeUpdate ( DocumentEvent arg0 ) {
			try {
				SubTypingSourceView.this.type2 = eventHandling ( SubTypingSourceView.this.editor2,
						SubTypingSourceView.this.outline2 );
				SubTypingSourceView.this.window.setChangeState ( Boolean.TRUE );
				SubTypingSourceView.this.saveStatus = true;
				SubTypingSourceView.this.setUndoStatus ( true );
				SubTypingSourceView.this.undohistory2.push ( SubTypingSourceView.this.currentContent );
				setRedoStatus ( false );
				SubTypingSourceView.this.redohistory2.clear ( );
				SubTypingSourceView.this.currentContent = arg0.getDocument ( ).getText ( 0,
						arg0.getDocument ( ).getLength ( ) );
			} catch ( BadLocationException e ) {
				logger.error ( "Failed to add text to undo history2", e ); //$NON-NLS-1$
			}
		}
	}
}
