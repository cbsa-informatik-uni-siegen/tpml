package de.unisiegen.tpml.core.subtyping;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.io.StringReader;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent;

public class SubTypingEnterTypes extends JComponent {
	
	/**
	 * The Textfields where the user is able to enter the types
	 */
	
	private StyledLanguageEditor editor1;
	
	private StyledLanguageEditor editor2;
	
	private StyledTypeEnterField document1;
	
	private StyledTypeEnterField document2;
	
	private JTextArea					output;
	
	/**
	 * Labels that informs the user what to do
	 */
	private JLabel						label;
	
	private JLabel						label2;
	
	private MonoType 					type1;
	
	private MonoType					type2;
	
	private MonoType					oldType1;
	
	private MonoType					oldType2;
	
  private DefaultOutline outline1 ;
  
  private DefaultOutline outline2 ;
	/**
	 * The {@link TypeCheckerNodeComponent} can determine whether
	 * the TypeCheckerEnterType-GUI is active. It will need the
	 * information to do a propper layouting.
	 * 
	 */
	private boolean						active;
	
	/**
	 * The {@link ComponentAdapter} that will be used to determine
	 * when the component gets shown. When the component gets shown
	 * a {@link #requestTextFocus()} will be called.
	 */
	private ComponentAdapter	componentAdapter;
	
	Language language;
	
	SubTyping checkSubtype = new SubTyping();
	
	public SubTypingEnterTypes (Language l) {
		super ();
		
		language = l;
		
		type1 = null;
		
		type2 = null;
	
		setLayout (new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		//this.panel = new JPanel( new GridBagLayout ( ) );
		//GridBagConstraints enterConstraints = new GridBagConstraints();
		
		this.label = new JLabel ("Enter first Type: "); //$NON-NLS-1$
		constraints.gridx = 0 ;
		constraints.gridy = 0 ;
		constraints.weightx = 0.5 ;
		constraints.weighty = 0 ;
		constraints.insets = new Insets( 15, 15, 15, 15);
		
		
		this.add( this.label, constraints );

		this.label2 = new JLabel ("Enter second Type: "); //$NON-NLS-1$
		constraints.gridx = 1 ;
		constraints.gridy = 0 ;
		constraints.weighty = 0 ;
		this.add(label2, constraints);
		
		this.editor1 = new StyledLanguageEditor();
		
		this.document1 = new StyledTypeEnterField( language );
		
		this.editor1.setDocument(this.document1);
		
		this.outline1 = new DefaultOutline ( this ) ;
		this.document1.addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent e) {
				//Nothing to do so far
				
			}

			public void insertUpdate(DocumentEvent e) {
				
				type1 = eventHandling( document1, type1, oldType1, outline1 );
				if ( type1 != oldType1 )
					check();
				
			}

			public void removeUpdate(DocumentEvent e) {
				type1 = eventHandling( document1, type1, oldType1, outline1 );
				if ( type1 != oldType1 )
					check();
				
			}
			
		});
		
		constraints.gridx = 0 ;
		constraints.gridy = 1 ;
		constraints.weighty = 0 ;
		this.add( this.editor1, constraints );
		
		this.editor2 = new StyledLanguageEditor();
		
		this.document2 = new StyledTypeEnterField( language );
		
		this.outline2 = new DefaultOutline ( this ) ;
		
		this.document2.addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent e) {
				//Nothing to do so far
				
			}

			public void insertUpdate(DocumentEvent e) {
				
				type2 = eventHandling( document2, type2, oldType2, outline2 );
				if ( type2 != oldType2)
					check();
			}

			public void removeUpdate(DocumentEvent e) {
				type2 = eventHandling( document2, type2, oldType2, outline2 );
				if ( type2 != oldType2)
					check();
			}
			
		});

		this.editor2.setDocument(this.document2);
		
		constraints.gridx = 1 ;
		constraints.gridy = 1 ;
		constraints.weighty = 0 ;
		this.add( this.editor2, constraints );
		
		
		 JPanel jPanelOutline1 = this.outline1.getJPanelOutline ( ) ;
			constraints.gridx = 0 ;
			constraints.gridy = 2 ;
			constraints.weighty = 10 ;
	   this.add( jPanelOutline1, constraints );
	   
		 JPanel jPanelOutline2 = this.outline2.getJPanelOutline ( ) ;
			constraints.gridx = 1 ;
			constraints.gridy = 2 ;
			constraints.weighty = 10 ;
	   this.add( jPanelOutline2, constraints );
	
		
		
		output = new JTextArea("", 2, 500);
		output.setEditable(false);
		constraints.gridx = 0 ;
		constraints.gridy = 3 ;
		constraints.gridwidth = 2 ;
		constraints.weighty = 0 ;
		constraints.weightx = 1 ;
		this.add( output, constraints );
    
    
    
    

   
	}
	
	void check(){
		if (checkSubtype.check(type1, type2)){
			output.setText("You got it");
			System.out.println("You got it");
		}
			
		else
			output.setText("Sorry no subtype");
	}
	
	MonoType eventHandling ( StyledLanguageDocument document, MonoType type, MonoType oldType, DefaultOutline outline ) {
		try {
		LanguageTypeParser parser = language.newTypeParser( new StringReader ( 
			 document.getText( document.getStartPosition ( ).getOffset( ), document.getEndPosition ( ).getOffset( ) - document.getStartPosition ( ).getOffset( ) ) ) ) ;
		 
	 
      type = parser.parse();
      
      	outline.loadPrettyPrintable ( type, Outline.Execute.AUTO_CHANGE_SUBTYPING ) ;
      	return type;
      	
      	
      
     
    } catch ( Exception e ) 
    {
      outline.loadPrettyPrintable ( null, Outline.Execute.AUTO_CHANGE_SUBTYPING ) ;
      return null;
    }
  
	
	}

}