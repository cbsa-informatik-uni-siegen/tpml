package de.unisiegen.tpml.ui.unify;


import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.unify.UnifyProofModel;
import de.unisiegen.tpml.core.unify.UnifyProofNode;


/**
 * Test class for the
 * {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel} class.
 * 
 * @author Benedikt Meurer
 * @version $Id: TypeInferenceProofModelTest.java 2796 2008-03-14 19:13:11Z
 *          fehler $
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
@SuppressWarnings ( "all" )
public final class UnifyProofModelTest extends JFrame
{

  /**
   * Simple test expression.
   */
  //int = int, int -> int = int -> int
  //'a -> int = int -> 'b, 'a = int
  //int = int, int = bool
  private static final String SIMPLE = "{ 'a -> int = int -> 'b, 'a = int }";


  private JScrollPane jScrollPane;


  ProofRule choosen = null;


  /**
   * Default constructor.
   */
  public UnifyProofModelTest ( final UnifyProofModel model )
  {
    // setup the frame
    setLayout ( new BorderLayout () );
    setSize ( 630, 580 );
    setTitle ( "UnifyProofModel Test" );
    // setup the tree panel
    JPanel treePanel = new JPanel ( new BorderLayout () );
    treePanel.setBorder ( BorderFactory.createEtchedBorder () );
    add ( treePanel, BorderLayout.CENTER );
    // setup the tree
    final JTree tree = new JTree ( model );
    treePanel.add ( tree, BorderLayout.CENTER );
    tree.setRowHeight ( 0 );
    DefaultTreeCellRenderer dtcr = ( DefaultTreeCellRenderer ) tree
        .getCellRenderer ();
    dtcr.setBorder ( new LineBorder ( Color.black ) );
    this.jScrollPane = new JScrollPane ( treePanel );
    this.add ( jScrollPane );
    // setup the button panel
    JPanel buttons = new JPanel ( new FlowLayout () );
    add ( buttons, BorderLayout.SOUTH );
    // setup the guess button
    JButton guessButton = new JButton ( "Guess" );
    guessButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          {
            model.guess ( nextNode ( model ) );
            // expand to the all nodes
            for ( int n = 0 ; n < tree.getRowCount () ; ++n )
            {
              tree.expandRow ( n );
            }
          }
        }
        catch ( Exception e )
        {
          JOptionPane.showMessageDialog ( UnifyProofModelTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    buttons.add ( guessButton );
    // setup the guess all button
    JButton guessAllButton = new JButton ( "Guess all" );
    guessAllButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          while ( nextNode ( model ) != null )
          {
            model.guess ( nextNode ( model ) );
            // expand to the all nodes
            for ( int n = 0 ; n < tree.getRowCount () ; ++n )
            {
              tree.expandRow ( n );
            }
          }
        }
        catch ( Exception e )
        {
          JOptionPane.showMessageDialog ( UnifyProofModelTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    buttons.add ( guessAllButton );
    // Setup combo box for prove
    JComboBox combo1 = new JComboBox ();
    for ( ProofRule rule : model.getRules () )
    {
      combo1.addItem ( rule.getName () );
    }
    buttons.add ( combo1 );
    combo1.addItemListener ( new ItemListener ()
    {

      public void itemStateChanged ( ItemEvent e )
      {
        JComboBox selectedChoice = ( JComboBox ) e.getSource ();
        if ( e.getStateChange () == 1 )
        {
          for ( ProofRule rules : model.getRules () )
          {
            if ( rules.getName ().equals ( selectedChoice.getSelectedItem () ) )
            {
              choosen = rules;
              break;
            }
          }
          try
          {
            // prove the last node
            model.prove ( choosen, nextNode ( model ) );
            // expand to the all nodes
            for ( int n = 0 ; n < tree.getRowCount () ; ++n )
            {
              tree.expandRow ( n );
            }
          }
          catch ( Exception e1 )
          {
            JOptionPane.showMessageDialog ( UnifyProofModelTest.this, e1
                .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
          }
        }
      }
    } );
    // setup the undo button
    final JButton undoButton = new JButton ( "Undo" );
    undoButton.setEnabled ( false );
    undoButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          // undo the last change
          model.undo ();
        }
        catch ( Exception e )
        {
          JOptionPane.showMessageDialog ( UnifyProofModelTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    model.addPropertyChangeListener ( "undoable", new PropertyChangeListener ()
    {

      public void propertyChange ( PropertyChangeEvent event )
      {
        undoButton.setEnabled ( model.isUndoable () );
      }
    } );
    buttons.add ( undoButton );
    // setup the redo button
    final JButton redoButton = new JButton ( "Redo" );
    redoButton.setEnabled ( false );
    redoButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          // redo the last undone change
          model.redo ();
          // expand to the last node
          for ( int n = 0 ; n < tree.getRowCount () ; ++n )
          {
            tree.expandRow ( n );
          }
        }
        catch ( Exception e )
        {
          JOptionPane.showMessageDialog ( UnifyProofModelTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    model.addPropertyChangeListener ( "redoable", new PropertyChangeListener ()
    {

      public void propertyChange ( PropertyChangeEvent event )
      {
        redoButton.setEnabled ( model.isRedoable () );
      }
    } );
    buttons.add ( redoButton );

    // setup the close button
    JButton closeButton = new JButton ( "Close" );
    closeButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        // System.exit(0);
      }
    } );
    buttons.add ( closeButton );
  }


  private static ProofNode nextNode ( UnifyProofModel model )
  {
    LinkedList < UnifyProofNode > nodes = new LinkedList < UnifyProofNode > ();
    nodes.add ( ( UnifyProofNode ) model.getRoot () );
    while ( !nodes.isEmpty () )
    {
      UnifyProofNode node = nodes.poll ();
      // if ( node.getSteps ().length == 0 )
      if ( !node.isProven () )
      {
        return node;
      }
      for ( int n = 0 ; n < node.getChildCount () ; ++n )
      {
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
  public static void main ( String [] args )
  {
    try
    {
      // parse the program (using l1unify)
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l1unify" );
      TypeEquationList equations = language.newUnifyParser (
          new StringReader ( SIMPLE ) ).parse ();
      UnifyProofModel model = language.newUnifyProofModel ( equations );
      // evaluate the resulting small step expression
      UnifyProofModelTest window = new UnifyProofModelTest ( model );
      // window.typechecker= language.newTypeCheckerProofModel(expression);
      window.addWindowListener ( new WindowAdapter ()
      {

        @Override
        public void windowClosing ( WindowEvent e )
        {
          System.exit ( 0 );
        }
      } );
      window.setVisible ( true );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
    }
  }
}
