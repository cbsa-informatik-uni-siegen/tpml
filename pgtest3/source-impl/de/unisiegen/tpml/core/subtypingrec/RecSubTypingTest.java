package de.unisiegen.tpml.core.subtypingrec;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;
import de.unisiegen.tpml.graphics.subtyping.SubTypingEnterTypes;

public class RecSubTypingTest extends JFrame {

	public RecSubTypingTest ( ) {

		setLayout ( new BorderLayout ( ) );

		setSize ( 800, 600 );

		setTitle ( "SubTyping Test" );

		//	 parse the program (using L4)
		/*SubTypingEnterTypes type = new SubTypingEnterTypes ( );

		 this.add ( type, BorderLayout.CENTER );

		 this.setVisible ( true );
		 */
	}

	/**
	 * TODO
	 *
	 * @param args
	 */
	public static void main ( String[] args ) {

		RecSubTypingTest test = new RecSubTypingTest ( );

		test.addWindowListener ( new WindowAdapter ( ) {
			@Override
			public void windowClosing ( WindowEvent e ) {
				System.exit ( 0 );
			}
		} );

	}

}
