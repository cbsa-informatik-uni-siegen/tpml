package de.unisiegen.tpml.core.subtyping;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringReader;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.types.MonoType;

@SuppressWarnings( "all" )
public class SubTypingProofModelTest extends JFrame {
	private static final String TYPE = "<add: int; sub: bool;>";

	private static final String TYPE2 = "<sub:bool; add:int;>";

	ProofRule choosen = null;

	public SubTypingProofModelTest ( final SubTypingProofModel model ) {
		// setup the frame
		setLayout ( new BorderLayout ( ) );
		setSize ( 800, 600 );
		setTitle ( "SubTypingProofModel Test" );

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

					{
						model.guess ( nextNode ( model ) );

						// expand to the all nodes
						for ( int n = 0; n < tree.getRowCount ( ); ++n ) {
							tree.expandRow ( n );

						}
					}
				}

				catch ( Exception e ) {
					JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e
							.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
				}
			}
		} );
		buttons.add ( guessButton );

		// setup the guess all button
		JButton guessAllButton = new JButton ( "Guess all" );
		guessAllButton.addActionListener ( new ActionListener ( ) {
			public void actionPerformed ( ActionEvent event ) {

				try {
					while ( nextNode ( model ) != null ) {
						model.guess ( nextNode ( model ) );

						// expand to the all nodes
						for ( int n = 0; n < tree.getRowCount ( ); ++n ) {
							tree.expandRow ( n );

						}
					}
				}

				catch ( Exception e ) {
					JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e
							.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
				}

			}
		} );
		buttons.add ( guessAllButton );

		//	  Setup combo box for prove
		final JComboBox combo1 = new JComboBox ( );

		for ( ProofRule rule : model.getRules ( ) ) {
			combo1.addItem ( rule.getName ( ) );
		}
		buttons.add ( combo1 );
		combo1.addItemListener ( new ItemListener ( ) {
			public void itemStateChanged ( ItemEvent e ) {
				JComboBox selectedChoice = ( JComboBox ) e.getSource ( );
				if ( e.getStateChange ( ) == 1 ) {
					for ( ProofRule rules : model.getRules ( ) ) {
						if ( rules.getName ( ).equals ( selectedChoice.getSelectedItem ( ) ) ) {
							choosen = rules;
							break;
						}
					}

					try {
						// prove  the last node
						model.prove ( choosen, nextNode ( model ) );

						// expand to the all nodes
						for ( int n = 0; n < tree.getRowCount ( ); ++n ) {
							tree.expandRow ( n );
						}
					} catch ( Exception e1 ) {
						JOptionPane.showMessageDialog ( SubTypingProofModelTest.this, e1
								.getMessage ( ), "Error", JOptionPane.ERROR_MESSAGE );
					}

				}
			}
		} );

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

		// setup the guess button
		JButton beginnerButton = new JButton ( "Beginner" );
		beginnerButton.addActionListener ( new ActionListener ( ) {
			public void actionPerformed ( ActionEvent event ) {
				model.setMode ( false );
				ItemListener temp = combo1.getItemListeners ( )[0];
				combo1.removeItemListener ( temp );
				combo1.removeAllItems ( );
				for ( ProofRule rule : model.getRules ( ) ) {
					combo1.addItem ( rule.getName ( ) );
				}
				combo1.addItemListener ( temp );
			}
		} );
		buttons.add ( beginnerButton );

		// setup the guess button
		JButton advancedButton = new JButton ( "Advanced" );
		advancedButton.addActionListener ( new ActionListener ( ) {
			public void actionPerformed ( ActionEvent event ) {

				model.setMode ( true );
				ItemListener temp = combo1.getItemListeners ( )[0];
				combo1.removeItemListener ( temp );
				combo1.removeAllItems ( );
				for ( ProofRule rule : model.getRules ( ) ) {
					combo1.addItem ( rule.getName ( ) );
				}
				combo1.addItemListener ( temp );

			}
		} );
		buttons.add ( advancedButton );

		// setup the close button
		JButton closeButton = new JButton ( "Close" );
		closeButton.addActionListener ( new ActionListener ( ) {
			public void actionPerformed ( ActionEvent event ) {
				System.exit ( 0 );
			}
		} );
		buttons.add ( closeButton );
	}

	private static ProofNode nextNode ( SubTypingProofModel model ) {

		LinkedList < ProofNode > nodes = new LinkedList < ProofNode > ( );
		nodes.add ( ( ProofNode ) model.getRoot ( ) );
		while ( !nodes.isEmpty ( ) ) {
			ProofNode node = nodes.poll ( );
			if ( node.getRules ( ).length == 0 ) {
				return node;
			}
			for ( int n = 0; n < node.getChildCount ( ); ++n ) {
				nodes.add ( node.getChildAt ( n ) );
			}
		}

		throw new IllegalStateException ( "Unable to find next node" );
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
			Language language = factory.getLanguageById ( "L2O" );
			LanguageTypeParser parser = language.newTypeParser ( new StringReader (
					TYPE ) );
			MonoType type = parser.parse ( );

			LanguageTypeParser parser2 = language.newTypeParser ( new StringReader (
					TYPE2 ) );
			MonoType type2 = parser2.parse ( );

			SubTypingProofModel model = language.newSubTypingProofModel ( type,
					type2, true );

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
