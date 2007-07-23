package de.unisiegen.tpml.graphics.subtyping;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.StringReader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
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
 * interface for the type checker interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class SubTypingSourceView extends JPanel // AbstractProofView //JComponent
		implements EditorComponent {
	/**
	 * The unique serialization identifier for this class.
	 */
	private static final long serialVersionUID = -425214200136389228L;

	/**
	 * The <code>SubTyping</code> component.
	 */
	// protected NewSubTypingComponent component ;
	JPanel source;

	JLabel sourceLabel;

	JLabel sourceLabel2;

	StyledLanguageEditor editor;

	StyledLanguageEditor editor2;

	StyledTypeEnterField sourceField;

	StyledTypeEnterField sourceField2;

	JScrollPane scrollPane1;

	JScrollPane scrollPane2;

	SideBar sidebar;

	SideBar sidebar2;

	MonoType type;

	MonoType type2;

	OwnDocumentListener listener;

	OwnDocumentListener2 listener2;

	MainWindow window;

	boolean saveStatus;

	/**
	 * The <code>JScrollPane</code> for the <code>component</code>.
	 */
	protected JScrollPane scrollPane;

	/**
	 * The <code>JSplitPane</code> for the <code>component</code>.
	 */
	private JSplitPane jSplitPane;

	private JPanel outlinePanel;

	/**
	 * The {@link Outline} of this view.
	 * 
	 * @see #getOutline()
	 */
	private DefaultOutline outline;

	private DefaultOutline outline2;

	/**
	 * The {@link SubTypingProofModel}.
	 */
	private Language language;

	/**
	 * Allocates a new {@link SubTypingSourceView} for the specified
	 * {@link SubTypingProofModel}.
	 * 
	 * @param pLanguage The {@link Language} for the
	 *          <code>SubTypingView</code>.
	 * @param pWindow the MainWindow containing this view
	 */
	@SuppressWarnings("synthetic-access")
	public SubTypingSourceView ( Language pLanguage, MainWindow pWindow ) {
		super ( );
		this.language = pLanguage;
		this.window = pWindow;
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
		//this.scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

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
		//this.scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

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

		//this.scrollPane.setViewportView ( this.source ) ;
		//this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
		/* this.scrollPane.addComponentListener ( new ComponentAdapter ( )
		 {
		 @ Override
		 public void componentResized ( @ SuppressWarnings ( "unused" )
		 ComponentEvent event )
		 {
		 SubTypingSourceView.this.source
		 .setAvailableWidth ( SubTypingSourceView.this.scrollPane.getViewport ( )
		 .getWidth ( ) ) ;
		 }
		 } ) ;*/

		this.outlinePanel = new JPanel ( new GridBagLayout ( ) );

		//JPanel jPanelOutline = this.outline.getPanel ( ) ;
		JScrollPane jPanelOutline = this.outline.getTree ( );

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets ( 0, 0, 0, 2 );
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 10;
		gridBagConstraints.weighty = 10;
		gridBagConstraints.gridwidth = 1;

		this.outlinePanel.add ( jPanelOutline, gridBagConstraints );

		//JPanel jPanelOutline2 = this.outline2.getPanel ( ) ;
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
		} catch ( BadLocationException e ) {
			//TODO
			//logger.error ( "Cannot set Text of the document" , e ) ;
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
		} catch ( BadLocationException e ) {
			//TODO
			//logger.error ( "Cannot set Text of the document" , e ) ;
		}
	}
	
	public String getText(){
		
		try {
			return this.sourceField.getText ( 0, this.sourceField.getLength ( ) );
		} catch ( BadLocationException e ) {
			return "";
		}
	}
	
	public String getText2(){
		
		try {
			return this.sourceField2.getText ( 0, this.sourceField2.getLength ( ) );
		} catch ( BadLocationException e ) {
			return "";
		}
	}

	private class OwnDocumentListener implements DocumentListener {

		public void changedUpdate ( DocumentEvent e ) {
		//Nothing to do so far
		}

		@SuppressWarnings ( { "synthetic-access", "unqualified-field-access" } )
		public void insertUpdate ( DocumentEvent e ) {
			type = eventHandling ( editor, outline );
			window.setChangeState ( true );
			saveStatus = true;
		}

		@SuppressWarnings ( "synthetic-access" )
		public void removeUpdate ( DocumentEvent e ) {
			type = eventHandling ( editor, outline );
			window.setChangeState ( true );
			saveStatus = true;
		}
	}

	private class OwnDocumentListener2 implements DocumentListener {

		public void changedUpdate ( DocumentEvent e ) {
		//Nothing to do so far
		}

		@SuppressWarnings ( "synthetic-access" )
		public void insertUpdate ( DocumentEvent e ) {
			type2 = eventHandling ( editor2,  outline2 );
			window.setChangeState ( true );
			saveStatus = true;
		}

		@SuppressWarnings ( "synthetic-access" )
		public void removeUpdate ( DocumentEvent e ) {
			type2 = eventHandling ( editor2, outline2 );
			window.setChangeState ( true );
			saveStatus = true;
		}
	}

	public void setType ( MonoType type ) {
		this.type = type;
	}

	public void setType2 ( MonoType type2 ) {
		this.type2 = type2;
	}

	public boolean isSaveStatus ( ) {
		return this.saveStatus;
	}

	public void setSaveStatus ( boolean saveStatus ) {
		this.saveStatus = saveStatus;
	}
	
	public void handleNext ( ) {
		// TODO Auto-generated method stub

		}

		public void handleRedo ( ) {
		// TODO Auto-generated method stub

		}

		public void handleUndo ( ) {
		// TODO Auto-generated method stub

		}

		public boolean isNextStatus ( ) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isPongStatus ( ) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isRedoStatus ( ) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isUndoStatus ( ) {
			// TODO Auto-generated method stub
			return false;
		}

		public void setAdvanced ( boolean status ) {
		// TODO Auto-generated method stub

		}

		public void setDefaultStates ( ) {
		// TODO Auto-generated method stub

		}
		
		public void guess ( ) throws IllegalStateException, ProofGuessException {
			// TODO Auto-generated method stub

			}

}
