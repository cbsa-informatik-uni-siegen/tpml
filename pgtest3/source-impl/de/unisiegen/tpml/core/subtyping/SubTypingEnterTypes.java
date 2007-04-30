package de.unisiegen.tpml.core.subtyping;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.io.StringReader;

import javax.swing.JButton;
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
	 * The Panel where everything is layed out
	 */
	private JPanel						panel;
	
	private JPanel						buttons;
	
	/**
	 * The Textfields where the user is able to enter the types
	 */
	//private JTextField				firstField;
	
	//private JTextField				secondField;
	
	private StyledLanguageEditor editor1;
	
	private StyledLanguageEditor editor2;
	
	private StyledLanguageDocument document1;
	
	private StyledLanguageDocument document2;
	
	private JTextArea					output;
	
	/**
	 * Labels that informs the user what to do
	 */
	private JLabel						label;
	
	private JLabel						label2;
	
	private JButton 					okButton;
	
	private JButton						clearButton;
	
  private DefaultOutline outline ;
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
	
		setLayout (new BorderLayout());
		
		
		
		this.panel = new JPanel(new GridLayout(2,2));
		

		this.label = new JLabel ("Enter first Type: "); //$NON-NLS-1$
		this.panel.add(this.label);
		
		this.editor1 = new StyledLanguageEditor();
		
		this.document1 = new StyledLanguageDocument( language );
		
		
		this.editor1.setDocument(this.document1);
		//this.editor1.setSize(200,15);
		this.document1.addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent e) {
				//Nothing to do so far
				
			}

			public void insertUpdate(DocumentEvent e) {
				
				eventHandling( document1 );
				
			}

			public void removeUpdate(DocumentEvent e) {
				eventHandling( document1 );
				
			}
			
		});
		
		this.panel.add(this.editor1);
		
		this.label2 = new JLabel ("Enter second Type: "); //$NON-NLS-1$
		
		this.panel.add(this.label2);
		
		
		this.editor2 = new StyledLanguageEditor();
		
		this.document2 = new StyledLanguageDocument( language );
		

		this.editor2.setDocument(this.document2);
		//this.editor2.setSize(200,15);
		
		this.panel.add(this.editor2);
		
		add (this.panel, BorderLayout.NORTH);
	
	



		

		
		this.buttons = new JPanel();
		this.buttons.setSize(200, 25);
		this.buttons.setLayout(new GridLayout(1,2));
		
		
		// setup the "OK" Button
		okButton = new JButton("OK");
		/*okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	       
	      LanguageTypeParser parser = language.newTypeParser(new StringReader (firstField.getText()));
	      LanguageTypeParser parser2 = language.newTypeParser(new StringReader (secondField.getText()));
	      	
	      	MonoType type1 = null;
	      	MonoType type2 = null;
	      	String actual = firstField.getText();
	      	if (okButton.isEnabled())
	        try {
	          type1 = parser.parse();
	          output.append( firstField.getText( ) + " wurde erfolgreich geparsed \n" );
	          actual= secondField.getText( );
	          type2 = parser2.parse( );
	          output.append( secondField.getText( ) + " wurde erfolgreich geparsed \n" );
	          
	          check(type1, type2);
	         
	        } catch (Exception e) {
	          
	          //output.append( actual + " konnte leider nicht geparsed werden" );
	          return;
	        }
	      }
	    });*/
		this.buttons.add (okButton);
		
		// setup the "Clear" Button
		clearButton = new JButton("Clear");
		/*clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
       
      	firstField.setText("");
      	secondField.setText("");
      	output.setText("");
      	
      	buttons.repaint();
        
      }
    });*/
	this.buttons.add (clearButton);
		this.add(buttons, BorderLayout.CENTER);
		
		output = new JTextArea("", 50, 15);
		output.setEditable(false);
		output.setVisible(false);
		this.add(output, BorderLayout.SOUTH);
    
    
    this.outline = new DefaultOutline ( this ) ;
    

    JPanel jPanelOutline = this.outline.getJPanelOutline ( ) ;
		
    this.add(jPanelOutline, BorderLayout.CENTER);
	}
	
	private void check( MonoType type1, MonoType type2 ){
		if (checkSubtype.check(type1, type2))
			output.append("You got it");
		else
			output.append("Sorry no subtype");
	}
	
	private void eventHandling ( StyledLanguageDocument document ) {
		try {
		LanguageTypeParser parser = language.newTypeParser( new StringReader ( 
			 document1.getText( document1.getStartPosition ( ).getOffset( ), document1.getEndPosition ( ).getOffset( ) - document1.getStartPosition ( ).getOffset( ) ) ) ) ;
		 
		MonoType type = null ;
	 
      type = parser.parse();
      
      this.outline.loadPrettyPrintable ( type, Outline.Execute.AUTO_CHANGE_TYPEINFERENCE ) ;
      
      output.setVisible(true);
      output.append( document.getText( document1.getStartPosition ( ).getOffset( ), document1.getEndPosition ( ).getOffset( ) - document1.getStartPosition ( ).getOffset( ) )   + " wurde erfolgreich geparsed \n" );
      
     
    } catch ( Exception e ) 
    {
      this.outline.loadPrettyPrintable ( null, Outline.Execute.AUTO_CHANGE_TYPEINFERENCE ) ;
      output.setVisible(false);
      return;
    }
  
	
	}

}