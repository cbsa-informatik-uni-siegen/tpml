package de.unisiegen.tpml.core.subtyping;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;

public class SubTypingTest extends JFrame {
	
	public SubTypingTest(){
		
		 
		
    // setup the frame
    setLayout(new BorderLayout());
    setSize(400, 400);
    setTitle("SubTyping Test");
    
    
		
//	 parse the program (using L4)
    LanguageFactory factory = LanguageFactory.newInstance();
    Language language;
		try {
			language = factory.getLanguageById("l2O");
			SubTypingEnterTypes type = new SubTypingEnterTypes(language);
			
	    this.add(type, BorderLayout.NORTH);
	    
		} catch (NoSuchLanguageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		
		
		

    

	}

	/**
	 * TODO
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		SubTypingTest test = new SubTypingTest();
		
		 test.addWindowListener(new WindowAdapter() {
       @Override
       public void windowClosing(WindowEvent e) {
         System.exit(0);
       }
     });
     test.setVisible(true);
	}

}
