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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import sun.font.AdvanceCache;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.subtyping.AbstractSubTyping;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;
import de.unisiegen.tpml.ui.SideBar;
import de.unisiegen.tpml.ui.SideBarListener;

/**
 * 
 * Input and Result mask for the Subtyping Algorithm.
 * Enter two types and find out if one is a subtype of the other one.
 *
 * @author Benjamin Mies
 * @author Feivel
 *
 */
public class SubTypingEnterTypes extends AbstractProofView {

	/**
	 * The unique serialization identifier of this class.
	 */
	private static final long serialVersionUID = 5068227950528407089L;
	
	/**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane ;
  //TODO noch einbauen
  
  private SubTypingProofModel model;

	/**
	 * The Panels for input, output and menu
	 */
	private JPanel inputFields;

	private JPanel outputField;

	private JPanel menu;
	
	private JPanel everything;
	
	private JPanel outline;

	/**
	 * The Textfields where the user is able to enter the types
	 */
	
	private SideBar			sideBar;
	
	private SideBar			sideBar2;
	
	private JScrollPane scrollpane;
	
	private JScrollPane scrollpane2;

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
	
	private SubTypingComponent component;
	
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
	/**
	 * Allocates a new <code>SubTypingEnterTypes</code> 
	 */
	public SubTypingEnterTypes(SubTypingProofModel modelP) {	
		super ( );
		model = modelP;
		// open a dialog to choose language
		clLanguage = new ChangeLanguage ( null, this );
		
		clLanguage.setLocationRelativeTo ( this );
		clLanguage.setVisible ( true );

	}

	private void initComponents() {

		type1 = null;

		type2 = null;
		//this.setSize ( 800, 600 );

		setLayout ( new GridBagLayout ( ) );
		GridBagConstraints constraints = new GridBagConstraints ( );
		constraints.fill = GridBagConstraints.BOTH;

		border = new LineBorder ( Color.GRAY, 1 );

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
		constraints.gridwidth = 2;
		constraints.insets = new Insets ( 5, 10, 5, 10 );
		this.inputFields.add ( this.label, constraints );

		this.label2 = new JLabel ( Messages.getString ( "secondType" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridwidth = 2;
		this.inputFields.add ( label2, constraints );

		this.editor1 = new StyledLanguageEditor ( );
		
		this.document1 = new StyledTypeEnterField ( language );
		
		this.scrollpane = new JScrollPane();
		this.sideBar = new SideBar (this.scrollpane,
				this.document1,
				this.editor1);
		this.sideBar.addSibeBarListener(new SideBarListener() {
			@SuppressWarnings("synthetic-access")
			public void markText (int left, int right) {
				SubTypingEnterTypes.this.selectErrorText(left, right);
				}
		});

		

		this.editor1.setDocument ( this.document1 );

		this.outline1 = new DefaultOutline ( this );
		this.document1.addDocumentListener ( new DocumentListener ( ) {

			public void changedUpdate(DocumentEvent e) {
			//Nothing to do so far
			}

			@SuppressWarnings("synthetic-access")
			public void insertUpdate(DocumentEvent e) {
				type1 = eventHandling ( editor1, type1, oldType1, outline1 );
				if (type1 != oldType1) {
					
					check ( );
				}
			}

			@SuppressWarnings("synthetic-access")
			public void removeUpdate(DocumentEvent e) {
				type1 = eventHandling ( editor1, type1, oldType1, outline1 );
				if (type1 != oldType1)
					check ( );
			}
		} );
		
		this.scrollpane.setViewportView(this.editor1);
		this.scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0;
		constraints.gridwidth = 1;
		constraints.insets = new Insets ( 5, 0, 5, 0 );
		this.inputFields.add ( this.sideBar, constraints );
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 10;
		this.inputFields.add ( this.scrollpane, constraints );

		this.editor2 = new StyledLanguageEditor ( );
		
		this.document2 = new StyledTypeEnterField ( language );
		
		this.scrollpane2 = new JScrollPane();
		this.sideBar2 = new SideBar (this.scrollpane2,
				this.document2,
				this.editor2);
		this.sideBar2.addSibeBarListener(new SideBarListener() {
			@SuppressWarnings("synthetic-access")
			public void markText (int left, int right) {
				SubTypingEnterTypes.this.selectErrorText(left, right);
				}
		});


		this.outline2 = new DefaultOutline ( this );

		this.document2.addDocumentListener ( new DocumentListener ( ) {

			public void changedUpdate(DocumentEvent e) {
			//Nothing to do so far
			}

			@SuppressWarnings("synthetic-access")
			public void insertUpdate(DocumentEvent e) {
				type2 = eventHandling ( editor2, type2, oldType2, outline2 );
				if (type2 != oldType2)
				{
					System.out.println("Das Moedel wird aktuallisiert");
					
					//model = language.newSubTypingProofModel(type1, type2, isAdvanced());
					model.setRoot(type1, type2);
					component = new SubTypingComponent ( model, isAdvanced() );
					System.out.println("advanced: "+isAdvanced());
					GridBagConstraints constraints = new GridBagConstraints ( );
					constraints.fill = GridBagConstraints.BOTH;
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 1;
					constraints.weighty = 1;
					constraints.weightx = 1;
					//outputField.removeAll();
					outputField.remove(0);
					System.out.println(outputField.getLayout().toString());
					outputField.add(component, constraints);
					outputField.getParent().repaint();
					check ( );
					
				}
					
			}

			@SuppressWarnings("synthetic-access")
			public void removeUpdate(DocumentEvent e) {
				type2 = eventHandling ( editor2, type2, oldType2, outline2 );
				if (type2 != oldType2)
				{
					System.out.println("Das Moedel wird aktuallisiert");
					//model = language.newSubTypingProofModel(type1, type2, isAdvanced() );
					model.setRoot(type1, type2);
					component = new SubTypingComponent ( model, isAdvanced() );
					GridBagConstraints constraints = new GridBagConstraints ( );
					constraints.fill = GridBagConstraints.BOTH;
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 1;
					constraints.weighty = 1;
					constraints.weightx = 1;
					//outputField.removeAll();
					outputField.remove(0);
					System.out.println(outputField.getLayout().toString());
					outputField.add(component, constraints);
					outputField.getParent().repaint();
					check ( );
				}
					
			}
		} );

		this.editor2.setDocument ( this.document2 );
		
		this.scrollpane2.setViewportView(this.editor2);
		this.scrollpane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weightx = 0;
		this.inputFields.add ( this.sideBar2, constraints );
		
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.weightx = 10;
		this.inputFields.add ( this.scrollpane2, constraints );

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 0;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( inputFields, constraints );

		outputField = new JPanel ( );
		outputField.setBackground(Color.WHITE);
		outputField.setLayout ( new GridBagLayout ( ) );
		//outputField.setLayout( null);
		outputField.setBorder ( border );

		labelOutput = new JLabel ( Messages.getString ( "result" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weighty = 1;
		constraints.weightx = 1;
		//constraints.insets = new Insets ( 5, 5, 5, 5 );
		//this.outputField.add ( labelOutput, constraints );
		//TODO hier muss der Renderer hin....
		//this.outputField.add ( labelOutput, constraints );
		System.out.println(outputField.getLayout().toString());
		this.outputField.add(new SubTypingComponent ( model, isAdvanced() ), constraints);

		lOutput = new JLabel (" "); //$NON-NLS-1$

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weighty = 0;
		constraints.weightx = 0;
		//this.outputField.add ( lOutput, constraints );

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weighty = 5;
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
		constraints.weighty = 5;
		constraints.gridwidth = 1;
		constraints.insets = new Insets ( 5, 5, 5, 5 );
		this.outline.add ( jPanelOutline1, constraints );

		JPanel jPanelOutline2 = this.outline2.getJPanelOutline ( );
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weighty = 5;
		this.outline.add ( jPanelOutline2, constraints );

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weighty = 5;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		this.add ( outline, constraints );

		this.setVisible ( true );

	}

	private void selectErrorText (int left, int right) {
		this.editor1.select(left, right);
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

	MonoType eventHandling(StyledLanguageEditor editor, MonoType pType,
			MonoType oldType, DefaultOutline outline) {
		MonoType type;
		lOutput.setText ( " " ); //$NON-NLS-1$
		try {
			LanguageTypeParser parser = this.language
					.newTypeParser ( new StringReader ( editor.getText ( ) ) );
			type = parser.parse ( );

			outline
					.loadPrettyPrintable ( type, Outline.Execute.AUTO_CHANGE_SUBTYPING );
			return type;

		} catch (Exception e) {
			
			outline
					.loadPrettyPrintable ( null, Outline.Execute.AUTO_CHANGE_SUBTYPING );
			if (editor.getText ( ).length ( ) == 0)
				outline.setError ( false );
			return null;
		}

	}

	/**
	 * set the actual language, and update the language where needed
	 *
	 * @param pLanguage
	 */
	public void setLanguage(Language pLanguage) {

		if (pLanguage != null) {
			this.language = pLanguage;
			model = language.newSubTypingProofModel(type1, type2, isAdvanced() );
			if (!initialized) {
				initComponents ( );
				initialized = true;
			} else {
				this.document1.setLanguage ( language );
				type1 = eventHandling ( editor1, type1, oldType1, outline1 );

				this.document2.setLanguage ( language );
				type2 = eventHandling ( editor2, type2, oldType2, outline2 );
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

	public void guess() throws IllegalStateException, ProofGuessException
	{
		System.out.println("Guess");
		
	}
	
	/**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @ Override
  public void setAdvanced ( boolean advanced )
  {
    //TODO Testausgabe System.out.println("jetzt bekommt der Subtyping-View den advaced-Wert: "+advanced + "(SubTypingEnterType)");
    super.setAdvanced ( advanced ) ;
    model.setMode(advanced);
    if (this.component != null)
    {
    	this.component.setAdvanced ( isAdvanced ( ) ) ;
    }
    
  }

}