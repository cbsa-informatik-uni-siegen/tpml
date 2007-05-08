package de.unisiegen.tpml.graphics.subtyping;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.subtyping.AbstractSubTyping;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;

import de.unisiegen.tpml.graphics.Messages;

/**
 * 
 * Input and Result mask for the Subtyping Algorithm.
 * Enter two types and find out if one is a subtype of the other one.
 *
 * @author Benjamin Mies
 *
 */
public class SubTypingEnterTypes extends JComponent {

	/**
	 * TODO
	 */
	private static final long serialVersionUID = 5068227950528407089L;

	/**
	 * The Panels for input, output and menu
	 */
	private JPanel inputFields;

	private JPanel outputField;

	private JPanel outline;

	private JPanel menu;

	/**
	 * The Textfields where the user is able to enter the types
	 */

	private StyledLanguageEditor editor1;

	private StyledLanguageEditor editor2;

	private StyledTypeEnterField document1;

	private StyledTypeEnterField document2;

	/**
	 * Labels that informs the user what to do
	 */
	private JLabel label;

	private JLabel label2;

	private JLabel labelOutput;

	private JLabel lOutput;

	private JLabel labelLanguage;

	/**
	 * The entered and parsed types
	 */
	private MonoType type1;

	private MonoType type2;

	/**
	 * The before entered types
	 */
	private MonoType oldType1;

	private MonoType oldType2;
	
	/**
	 * Outlines for better understanding the entered types
	 */

	private DefaultOutline outline1;

	private DefaultOutline outline2;

	/**
	 * The actual choosen language
	 */
	private Language language;

	/**
	 * Black border 
	 */
	private LineBorder border;
	
	/**
	 * The Buttons of the menu
	 */
	private JButton changeLanguage;

	private JCheckBox setOutline;

	/**
	 * Dialog to change language
	 */
	private ChangeLanguage clLanguage;

	/**
	 * flag which shows if elements already initialized
	 */
	private boolean initialized = false;
	
	//
	// Constructor
	//

	public SubTypingEnterTypes() {
		super ( );

		// open a dialog to choose language
		clLanguage = new ChangeLanguage ( null, this );
		clLanguage.setLocationRelativeTo ( this );
		clLanguage.setVisible ( true );

	}

	private void initComponents() {

		type1 = null;

		type2 = null;
		this.setSize ( 800, 600 );

		setLayout ( new GridBagLayout ( ) );
		GridBagConstraints constraints = new GridBagConstraints ( );
		constraints.fill = GridBagConstraints.BOTH;

		border = new LineBorder ( Color.black, 2 );

		menu = new JPanel ( );
		menu.setLayout ( new GridBagLayout ( ) );
		menu.setBorder ( border );

		changeLanguage = new JButton ( Messages.getString ( "changeLanguage" ) ); //$NON-NLS-1$
		changeLanguage.addActionListener ( new ActionListener ( ) {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent event) {
				SubTypingEnterTypes.this.setEnabled ( false );
				clLanguage = new ChangeLanguage ( null, SubTypingEnterTypes.this );
				clLanguage.setLocationRelativeTo ( SubTypingEnterTypes.this );
				clLanguage.setVisible ( true );
			}
		} );
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.insets = new Insets ( 5, 10, 5, 10 );
		this.menu.add ( changeLanguage, constraints );

		labelLanguage = new JLabel (MessageFormat.format (  Messages.getString ( "actualLanguage" ),  //$NON-NLS-1$
				 language.getName ( ) ) );
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 3;
		constraints.weighty = 0;
		this.menu.add ( labelLanguage, constraints );

		setOutline = new JCheckBox ( Messages.getString ( "showOutline" ) ); //$NON-NLS-1$
		setOutline.setSelected ( true );
		setOutline.setToolTipText ( Messages.getString ( "tooltipOutline" ) ); //$NON-NLS-1$
		setOutline.addItemListener ( new ItemListener(){

			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange ( ) == ItemEvent.SELECTED ){
					SubTypingEnterTypes.this.outline.setVisible ( true );
				}
				if ( e.getStateChange ( ) == ItemEvent.DESELECTED ){
					SubTypingEnterTypes.this.outline.setVisible ( false );
				}
				
			}
			
		});
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.weightx = 2;
		constraints.weighty = 0;
		this.menu.add ( setOutline, constraints );

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( menu, constraints );

		inputFields = new JPanel ( );
		inputFields.setLayout ( new GridBagLayout ( ) );
		inputFields.setBorder ( border );

		this.label = new JLabel ( Messages.getString ( "firstType" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.insets = new Insets ( 5, 10, 5, 10 );
		this.inputFields.add ( this.label, constraints );

		this.label2 = new JLabel ( Messages.getString ( "secondType" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		this.inputFields.add ( label2, constraints );

		this.editor1 = new StyledLanguageEditor ( );

		this.document1 = new StyledTypeEnterField ( language );

		this.editor1.setDocument ( this.document1 );

		this.outline1 = new DefaultOutline ( this );
		this.document1.addDocumentListener ( new DocumentListener ( ) {

			public void changedUpdate(DocumentEvent e) {
			//Nothing to do so far
			}

			@SuppressWarnings("synthetic-access")
			public void insertUpdate(DocumentEvent e) {
				type1 = eventHandling ( document1, type1, oldType1, outline1 );
				if (type1 != oldType1) {
					check ( );
				}
			}

			@SuppressWarnings("synthetic-access")
			public void removeUpdate(DocumentEvent e) {
				type1 = eventHandling ( document1, type1, oldType1, outline1 );
				if (type1 != oldType1)
					check ( );
			}
		} );

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 0;
		this.inputFields.add ( this.editor1, constraints );

		this.editor2 = new StyledLanguageEditor ( );

		this.document2 = new StyledTypeEnterField ( language );

		this.outline2 = new DefaultOutline ( this );

		this.document2.addDocumentListener ( new DocumentListener ( ) {

			public void changedUpdate(DocumentEvent e) {
			//Nothing to do so far
			}

			@SuppressWarnings("synthetic-access")
			public void insertUpdate(DocumentEvent e) {
				type2 = eventHandling ( document2, type2, oldType2, outline2 );
				if (type2 != oldType2)
					check ( );
			}

			@SuppressWarnings("synthetic-access")
			public void removeUpdate(DocumentEvent e) {
				type2 = eventHandling ( document2, type2, oldType2, outline2 );
				if (type2 != oldType2)
					check ( );
			}
		} );

		this.editor2.setDocument ( this.document2 );

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weighty = 0;
		this.inputFields.add ( this.editor2, constraints );

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 0;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( inputFields, constraints );

		outputField = new JPanel ( );
		outputField.setLayout ( new GridBagLayout ( ) );
		outputField.setBorder ( border );

		labelOutput = new JLabel ( Messages.getString ( "result" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weighty = 0;
		constraints.weightx = 1;
		constraints.insets = new Insets ( 5, 5, 5, 5 );
		this.outputField.add ( labelOutput, constraints );

		lOutput = new JLabel (" ");

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weighty = 0;
		constraints.weightx = 0;
		this.outputField.add ( lOutput, constraints );

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weighty = 0;
		constraints.weightx = 0;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( outputField, constraints );

		outline = new JPanel ( );
		outline.setLayout ( new GridBagLayout ( ) );
		outline.setBorder ( border );

		JPanel jPanelOutline1 = this.outline1.getJPanelOutline ( );
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 10;
		constraints.gridwidth = 1;
		constraints.insets = new Insets ( 5, 5, 5, 5 );
		this.outline.add ( jPanelOutline1, constraints );

		JPanel jPanelOutline2 = this.outline2.getJPanelOutline ( );
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weighty = 10;
		this.outline.add ( jPanelOutline2, constraints );

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weighty = 10;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( outline, constraints );

		this.setVisible ( true );

	}

	void check() {
		if (type1 != null && type2 != null) {
			if (AbstractSubTyping.check ( type1, type2 )) {
				lOutput.setText ( Messages.getString ( "subtypeFound" ) ) ; //$NON-NLS-1$
				lOutput.setForeground(Color.green);
			}

			else {
				lOutput.setText ( Messages.getString ( "noSubtype" ) ); //$NON-NLS-1$
				lOutput.setForeground(Color.red);
			}
		
		}
	}

	MonoType eventHandling(StyledLanguageDocument document, MonoType type,
			MonoType oldType, DefaultOutline outline) {
		lOutput.setText ( " " );
		try {
			LanguageTypeParser parser = this.language
					.newTypeParser ( new StringReader ( document.getAllText ( ) ) );

			type = parser.parse ( );

			outline
					.loadPrettyPrintable ( type, Outline.Execute.AUTO_CHANGE_SUBTYPING );
			return type;

		} catch (Exception e) {
			
			outline
					.loadPrettyPrintable ( null, Outline.Execute.AUTO_CHANGE_SUBTYPING );
			if (document.getAllText ( ).length ( ) == 0)
				outline.setError ( false );
			return null;
		}

	}

	public void setLanguage(Language pLanguage) {

		if (pLanguage != null) {
			this.language = pLanguage;
			if (!initialized) {
				initComponents ( );
				initialized = true;
			} else {
				this.document1.setLanguage ( language );
				type1 = eventHandling ( document1, type1, oldType1, outline1 );

				this.document2.setLanguage ( language );
				type2 = eventHandling ( document2, type2, oldType2, outline2 );
				check ( );
				try {
					this.document1.processChanged ( );
				} catch (BadLocationException e) {
					// Nothing to do
				}

				this.labelLanguage.setText ( MessageFormat.format ( Messages.getString ( "actualLanguage" ), language.getName ( ) ) ) ; //$NON-NLS-1$
			}

			this.setEnabled ( true );
		} else if (this.language == null) {
			this.setEnabled ( false );
		}

	}

}