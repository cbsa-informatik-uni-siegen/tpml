package de.unisiegen.tpml.core.subtyping;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringReader;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.types.MonoType;

public class SubTypingProofModelTest extends JFrame {
	private static final String TYPE = "int ref";
	private static final String TYPE2 = "int ref";
	
	
	 
		public SubTypingProofModelTest ( final SubTypingProofModel model ) {
			// setup the frame
			setLayout ( new BorderLayout ( ) );
			setSize ( 630, 580 );
			setTitle ( "TypeCheckerProofModel Test" );

			// setup the tree panel
			JPanel treePanel = new JPanel ( new BorderLayout ( ) );
			treePanel.setBorder ( BorderFactory.createEtchedBorder ( ) );
			add ( treePanel, BorderLayout.CENTER );

			// setup the tree
			final JTree tree = new JTree ( model );
			treePanel.add ( tree, BorderLayout.CENTER );

			// setup the button panel
			JPanel buttons = new JPanel ( new FlowLayout ( ) );
			add ( buttons, BorderLayout.SOUTH );

			// setup the guess button
			JButton guessButton = new JButton ( "Guess" );
			guessButton.addActionListener ( new ActionListener ( ) {
				public void actionPerformed ( ActionEvent event ) {
					try {
						// guess the last node
						model.guess ( nextNode ( model ) );

						// expand to the all nodes
						for ( int n = 0; n < tree.getRowCount ( ); ++n ) {
							tree.expandRow ( n );
						}
					} catch ( Exception e ) {
						JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e
								.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
					}
				}
			} );
			buttons.add ( guessButton );

			// setup the undo button
			final JButton undoButton = new JButton ( "Undo" );
			undoButton.setEnabled ( false );
			undoButton.addActionListener ( new ActionListener ( ) {
				public void actionPerformed ( ActionEvent event ) {
					try {
						// undo the last change
						model.undo ( );
					} catch ( Exception e ) {
						JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e
								.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
					}
				}
			} );
			model.addPropertyChangeListener ( "undoable",
					new PropertyChangeListener ( ) {
						public void propertyChange ( PropertyChangeEvent event ) {
							undoButton.setEnabled ( model.isUndoable ( ) );
						}
					} );
			buttons.add ( undoButton );

			// setup the redo button
			final JButton redoButton = new JButton ( "Redo" );
			redoButton.setEnabled ( false );
			redoButton.addActionListener ( new ActionListener ( ) {
				public void actionPerformed ( ActionEvent event ) {
					try {
						// redo the last undone change
						model.redo ( );

						// expand to the last node
						for ( int n = 0; n < tree.getRowCount ( ); ++n ) {
							tree.expandRow ( n );
						}
					} catch ( Exception e ) {
						JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e
								.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
					}
				}
			} );
			model.addPropertyChangeListener ( "redoable",
					new PropertyChangeListener ( ) {
						public void propertyChange ( PropertyChangeEvent event ) {
							redoButton.setEnabled ( model.isRedoable ( ) );
						}
					} );
			buttons.add ( redoButton );

			// setup the translate button
			/*
			JButton translateButton = new JButton ( "Translate" );
			translateButton.addActionListener ( new ActionListener ( ) {
				public void actionPerformed ( ActionEvent event ) {
					try {
						// translate the last node
						TreePath path = tree.getSelectionPath ( );
						if ( path != null ) {
							model.translateToCoreSyntax ( ( ExpressionProofNode ) path
									.getLastPathComponent ( ), false );
						}
					} catch ( Exception e ) {
						JOptionPane.showMessageDialog ( TypeCheckerProofModelTest.this, e
								.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
					}
				}
			} );
			buttons.add ( translateButton );
*/
			// setup the close button
			JButton closeButton = new JButton ( "Close" );
			closeButton.addActionListener ( new ActionListener ( ) {
				public void actionPerformed ( ActionEvent event ) {
					System.exit ( 0 );
				}
			} );
			buttons.add ( closeButton );
		}

	  private static ProofNode nextNode(SubTypingProofModel model) {
		  
	    LinkedList<DefaultSubTypingProofNode> nodes = new LinkedList<DefaultSubTypingProofNode>();
	    nodes.add((DefaultSubTypingProofNode)model.getRoot());
	    while (!nodes.isEmpty()) {
	    	DefaultSubTypingProofNode node = nodes.poll();
	      if (node.getSteps().length == 0) {
	        return node;
	      }
	      for (int n = 0; n < node.getChildCount(); ++n) {
	        nodes.add(node.getChildAt(n));
	      }
	    }
	    
	    throw new IllegalStateException("Unable to find next node");
	  }

		//
		// Program entry point
		//

		/**
		 * Runs the small step interpreter test.
		 * 
		 * @param args the command line arguments.
		 */
public static void main ( String[] args ) {
		try {
			// parse the program (using L4)
			LanguageFactory factory = LanguageFactory.newInstance ( );
			Language language = factory.getLanguageById ( "l4" );
			LanguageTypeParser parser = language
			.newTypeParser ( new StringReader ( TYPE ) );
			MonoType type = parser.parse ( );
			
			LanguageTypeParser parser2 = language
			.newTypeParser ( new StringReader ( TYPE2 ) );
			MonoType type2 = parser2.parse ( );
			
	
			SubTypingProofModel model = language
					.newSubTypingProofModel ( type, type2 );

			// evaluate the resulting small step expression
			SubTypingProofModelTest window = new SubTypingProofModelTest ( model );
			window.addWindowListener ( new WindowAdapter ( ) {
				@Override
				public void windowClosing ( WindowEvent e ) {
					System.exit ( 0 );
				}
			} );
			window.setVisible ( true );
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}

	}

}
