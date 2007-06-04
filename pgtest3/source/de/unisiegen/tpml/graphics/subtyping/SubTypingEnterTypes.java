package de.unisiegen.tpml.graphics.subtyping;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.subtyping.AbstractSubTyping;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.bigstep.BigStepView;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;
import de.unisiegen.tpml.ui.SideBar;
import de.unisiegen.tpml.ui.SideBarListener;

/**
 * This is the Subtyping view. This view is very different to the other views.
 * It is not connected to the source-view. So the user must select the language
 * and inter the two types he wants to check. The user can change the language
 * everytime.
 * the scond part of the view is alike the BigStepView. ({@link BigStepView})
 * where you have the view and two outlines, one for each type. 

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
   * The {@link SubTypingProofModel}.
   */
  private SubTypingProofModel model;
  
  /**
	 * The entered and parsed type1 ({@link MonoType})
	 */
	private MonoType type1;

	 /**
	 * The entered and parsed type1 ({@link MonoType})
	 */
	private MonoType type2;
	
	/**
	 * The last entered types
	 */
	private MonoType oldType1;
	/***/
	private MonoType oldType2;
	
	/**
	 * The {@link SubTypingComponent} component
	 */
	private SubTypingComponent component;
	
	/**
	 * The actual choosen language
	 */
	private Language language;
	

	
  //GUI-components
  
  /**
	 * The panel for input
	 */
	private JPanel inputFields;

	 /**
	 * The panel for output
	 */
	private JPanel outputField;

	 /**
	 * The panel for menu
	 */
	private JPanel menu;
	
	/**
	 * The panel for the two outlines
	 */
	private JPanel outline;

	/**
	 * The Textfields where the user is able to enter the types
	 */
	private SideBar			sideBar;
	/***/
	private SideBar			sideBar2;
	/***/
	private JScrollPane scrollpane;
	/***/
	private JScrollPane scrollpane2;
	/***/
	private StyledLanguageEditor editor1;
	/***/
	private StyledLanguageEditor editor2;
	/***/
	private StyledTypeEnterField document1;
	/***/
	private StyledTypeEnterField document2;

	/**
	 * Labels that informs the user what to do
	 */
	private JLabel label;
	/***/
	private JLabel label2;
	/***/
	private JLabel lOutput;
	/***/
	private JLabel labelLanguage;
	
	
	/**
	 * The Buttons of the menu
	 */
	private JButton changeLanguage;

	/**
	 * The JCheckBox to enable or disable the Outlines
	 */
	private JCheckBox setOutline;
	
	/**
   * The <code>JSplitPane</code> to devide the outline and the result
   */
	private JSplitPane splitPane;
	
	/**
	 * Black border 
	 */
	private LineBorder border;
	
	/**
	 * Outlines for better understanding the entered type 1
	 */
	private DefaultOutline outline1;

	/**
	 * Outlines for better understanding the entered type 2
	 */
	private DefaultOutline outline2;



	/**
	 * Dialog to change language
	 */
	private ChangeLanguage clLanguage;

	/**
	 * flag which shows if elements already initialized
	 */
	private boolean initialized = false;

	/**
	 * hte division of the JSplitPane
	 */
	private double dividerLocation = 0.5;
	

	//
	// Constructor
	//
	/**
	 * Allocates a new <code>SubTypingEnterTypes</code> 
	 * @param modelP	The model 
	 */
	public SubTypingEnterTypes(SubTypingProofModel modelP) {	
		super ( );
		model = modelP;
		// open a dialog to choose language
		clLanguage = new ChangeLanguage ( null, this );
		
		clLanguage.setLocationRelativeTo ( this );
		clLanguage.setVisible ( true );

	}

	/**
	 * inizialisize the gui
	 */
	private void initComponents() {

		type1 = null;

		type2 = null;
		//this.setSize ( 800, 600 );
		
		splitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(dividerLocation );

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
		setOutline.setSelected ( false );
		setOutline.setToolTipText ( Messages.getString ( "tooltipOutline" ) ); //$NON-NLS-1$
		setOutline.addItemListener ( new ItemListener(){

			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange ( ) == ItemEvent.SELECTED ){
					SubTypingEnterTypes.this.outline.setVisible ( true );
					splitPane.setOneTouchExpandable(true);
					splitPane.setDividerLocation(SubTypingEnterTypes.this.dividerLocation);
				}
				if ( e.getStateChange ( ) == ItemEvent.DESELECTED ){
					SubTypingEnterTypes.this.outline.setVisible ( false );
					splitPane.setOneTouchExpandable(false);

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
		//this.editor1.getInputMap ( ).put(KeyStroke.getKeyStroke("F2"), "nextComponent");
		//this.editor1.getActionMap().put("nextComponent",nextComponent());
		this.editor1.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_TAB){
        	//Remove tabs and so on
        	editor1.setText ( editor1.getText ( ).trim ( ));
        	editor2.setText ( editor2.getText ( ).trim ( ));
           nextEditor ( );
        }
     }
  });
		
		//inputFields.addKeyListener ( new KeyListener() )
		
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
		
		this.editor2.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_TAB){
        	//remove tabs and so on
        	editor1.setText ( editor1.getText ( ).trim ( ));
        	editor2.setText ( editor2.getText ( ).trim ( ));
           nextEditor ( );
        }
     }
  });
		
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
					outputField.remove ( component );
					//SubTypingEnterTypes.this.remove ( outputField );
					
					
					model.setRoot(type1, type2);
					component = new SubTypingComponent ( model, isAdvanced() );
					
					GridBagConstraints constraints = new GridBagConstraints ( );
					constraints.fill = GridBagConstraints.BOTH;
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 1;
					constraints.weighty = 1;
					constraints.weightx = 1;
					constraints.insets = new Insets ( 15, 15, 15, 15 );
					
					outputField.add ( component, constraints );
					
					constraints.gridx = 0;
					constraints.gridy = 2;
					constraints.weighty = 5;
					constraints.weightx = 0;
					constraints.insets = new Insets ( 15, 15, 15, 15 );
					//SubTypingEnterTypes.this.add ( outputField, constraints );
					
					//SubTypingEnterTypes.this.validate ( );
					outputField.validate ( );
					
					check ( );
					
				}
					
			}

			@SuppressWarnings("synthetic-access")
			public void removeUpdate(DocumentEvent e) {
				type2 = eventHandling ( editor2, type2, oldType2, outline2 );
				if (type2 != oldType2)
				{
					outputField.remove ( component );
					//SubTypingEnterTypes.this.remove ( outputField );
					
					
					model.setRoot(type1, type2);
					component = new SubTypingComponent ( model, isAdvanced() );
					
					GridBagConstraints constraints = new GridBagConstraints ( );
					constraints.fill = GridBagConstraints.BOTH;
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 1;
					constraints.weighty = 1;
					constraints.weightx = 1;
					constraints.insets = new Insets ( 15, 15, 15, 15 );
					
					outputField.add ( component, constraints );
					
					constraints.gridx = 0;
					constraints.gridy = 2;
					constraints.weighty = 5;
					constraints.weightx = 0;
					constraints.insets = new Insets ( 15, 15, 15, 15 );
					//SubTypingEnterTypes.this.add ( outputField, constraints );
					
					//SubTypingEnterTypes.this.validate ( );
					outputField.validate ( );
					
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

		//labelOutput = new JLabel ( Messages.getString ( "result" ) ); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.insets = new Insets ( 15, 15, 15, 15 );

		component = new SubTypingComponent ( model, isAdvanced() );
		this.outputField.add(component, constraints);

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
		//this.add ( outputField, constraints );
		splitPane.add ( outputField, JSplitPane.TOP );
		splitPane.setDividerLocation(dividerLocation );

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
		this.outline.setVisible ( false );

		JPanel jPanelOutline2 = this.outline2.getJPanelOutline ( );
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weighty = 5;
		this.outline.add ( jPanelOutline2, constraints );

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weighty = 5;
		constraints.insets = new Insets ( 15, 15, 15, 15 );
		//this.add ( outline, constraints );
		splitPane.add ( outline, JSplitPane.BOTTOM );
		splitPane.setDividerLocation(dividerLocation );
		this.add (splitPane, constraints);

		this.setVisible ( true );
		
		this.validate();

	}

	/**
	 * privides the TAB-Key Funktion in the Textfields
	 * if the user is in editor1 he comes to editor2
	 * and inversly
	 * 
	 */
	private void nextEditor ( )
	{
		//normal variant: The focus switches from editor1 to editor2 and fro,
		//editor2 to the next component that will be the buttonmenu at the top
		if (editor1.hasFocus ( ))
		{
			editor1.transferFocus ( );
		}
		if (editor2.hasFocus ( ))
		{
			editor2.transferFocus ( );	
		}
		
		//other variant: The focus only switchs between the 2 editors
//		if (editor1.hasFocus ( ))
//		{
//			editor2.requestFocus ( );
//		}
//		else 
//		{
//			editor1.requestFocus ( );
//		}
	}

	/**
	 * TODO Dok
	 * @param left
	 * @param right
	 */
	private void selectErrorText (int left, int right) {
		this.editor1.select(left, right);
	}

	/**
	 * TODO Dokumentation
	 */
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

	/**
	 * @param editor		The Editor to read the Type
	 * @param pType			The type
	 * @param oldType		The last type
	 * @param outline		The outline
	 * @return					The Monotype
	 */
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
			//no new model, we have to actualisize the old one
			//model = language.newSubTypingProofModel(type1, type2, isAdvanced() );
			model.setRoot(type1, type2);
			model.setMode(isAdvanced());
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

	/**
	 * provides a Guess 
	 * @throws IllegalStateException
	 * @throws ProofGuessException
	 */
	public void guess() throws IllegalStateException, ProofGuessException
	{
		this.component.guess ( );
	}
	
	/**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @ Override
  public void setAdvanced ( boolean advanced )
  {
    super.setAdvanced ( advanced ) ;
    model.setMode(advanced);
    if (this.component != null)
    {
    	this.component.setAdvanced ( isAdvanced ( ) ) ;
    }
    
  }

}