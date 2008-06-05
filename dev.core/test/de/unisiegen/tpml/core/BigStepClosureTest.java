package de.unisiegen.tpml.core;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofModel;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModelTest;

/**
 * TODO
 *
 */
public final class BigStepClosureTest extends JFrame
{
  class Renderer extends DefaultTreeCellRenderer
  {

    /**
     * {@inheritDoc}
     * 
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
     *      java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent ( JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus )
    {
      super.getTreeCellRendererComponent ( tree, value, sel, expanded, leaf,
          row, hasFocus );
      BigStepClosureProofNode node = ( BigStepClosureProofNode ) value;
      StringBuilder builder = new StringBuilder ();
      boolean memoryEnabled = ( ( BigStepClosureProofModel ) tree.getModel () )
          .isMemoryEnabled ();
      builder.append ( '[' );
      for ( int n = 0 ; n < node.getSteps ().length ; ++n )
      {
        if ( n > 0 )
          builder.append ( ", " );
        builder.append ( node.getSteps () [ n ].getRule ().getName () );
      }
      builder.append ( "] -> " );
      if ( memoryEnabled )
      {
        builder.append ( '(' );
      }
      builder.append ( node.getExpression () );
      if ( memoryEnabled )
      {
        builder.append ( ", " );
        builder.append ( node.getStore () );
        builder.append ( ')' );
      }
      builder.append ( " \u21d3 " );
      if ( node.getResult () != null )
      {
        if ( memoryEnabled )
        {
          builder.append ( '(' );
        }
        builder.append ( node.getResult ().getValue () );
        if ( memoryEnabled )
        {
          builder.append ( ", " );
          builder.append ( node.getResult ().getStore () );
          builder.append ( ')' );
        }
      }
      setText ( builder.toString () );
      return this;
    }
  }
  
  public static void main ( String [] args )
  {
    try
    {
      // parse the program (using L4)
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l1" );
      
      String testexpr = "(+) 1 1";
      
      Expression expression = language.newParser ( new StringReader ( testexpr ) )
          .parse ();
      BigStepClosureProofModel model = language.newBigStepClosureProofModel ( expression );
      // evaluate the resulting small step expression
      BigStepClosureTest window = new BigStepClosureTest ( model );
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
  
  public BigStepClosureTest ( final BigStepClosureProofModel model )
  {
    // setup the frame
    setLayout ( new BorderLayout () );
    setSize ( 630, 580 );ProofRule choosen = null;

    setTitle ( "BigStepProofModel Test" );
    // setup the tree panel
    JPanel treePanel = new JPanel ( new BorderLayout () );
    treePanel.setBorder ( BorderFactory.createEtchedBorder () );
    add ( treePanel, BorderLayout.CENTER );
    // setup the tree
    final JTree tree = new JTree ( model );
    tree.setCellRenderer ( new Renderer () );
    treePanel.add ( tree, BorderLayout.CENTER );
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
          // guess the last node
          model.guess ( nextNode ( model ) );
          // expand to the all nodes
          for ( int n = 0 ; n < tree.getRowCount () ; ++n )
          {
            tree.expandRow ( n );
          }
        }
        catch ( Exception e )
        {
          e.printStackTrace ();
          JOptionPane.showMessageDialog ( BigStepClosureTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    buttons.add ( guessButton );
    // setup the complete button
    JButton completeButton = new JButton ( "Complete" );
    completeButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          // guess the last node
          model.complete ( ( ProofNode ) tree.getSelectionPath ()
              .getLastPathComponent () );
          // expand to the all nodes
          for ( int n = 0 ; n < tree.getRowCount () ; ++n )
          {
            tree.expandRow ( n );
          }
        }
        catch ( Exception e )
        {
          e.printStackTrace ();
          JOptionPane.showMessageDialog ( BigStepClosureTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    buttons.add ( completeButton );
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
          e.printStackTrace ();
          JOptionPane.showMessageDialog ( BigStepClosureTest.this, e
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
          e.printStackTrace ();
          JOptionPane.showMessageDialog ( BigStepClosureTest.this, e
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
    // setup the translate button
    JButton translateButton = new JButton ( "Translate" );
    translateButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        try
        {
          // translate the last node
          TreePath path = tree.getSelectionPath ();
          if ( path != null )
          {
            model.translateToCoreSyntax ( ( ExpressionProofNode ) path
                .getLastPathComponent (), false );
          }
        }
        catch ( Exception e )
        {
          e.printStackTrace ();
          JOptionPane.showMessageDialog ( BigStepClosureTest.this, e
              .getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
        }
      }
    } );
    buttons.add ( translateButton );
    
    // Setup combo box for prove
    JComboBox combo1 = new JComboBox ();
    for ( ProofRule rule : model.getRules () )
    {
      combo1.addItem ( rule.getName () );
    }
    buttons.add ( combo1 );
    combo1.addItemListener ( new ItemListener ()
    {

      ProofRule chosen = null;
      
      public void itemStateChanged ( ItemEvent e )
      {
        JComboBox selectedChoice = ( JComboBox ) e.getSource ();
        if ( e.getStateChange () == 1 )
        {
          for ( ProofRule rules : model.getRules () )
          {
            if ( rules.getName ().equals ( selectedChoice.getSelectedItem () ) )
            {
              chosen = rules;
              break;
            }
          }
          try
          {
            // prove the last node
            model.prove ( chosen, nextNode ( model ) );
            // expand to the all nodes
            for ( int n = 0 ; n < tree.getRowCount () ; ++n )
            {
              tree.expandRow ( n );
            }
          }
          catch ( Exception e1 )
          {
            e1.printStackTrace();
            JOptionPane.showMessageDialog ( BigStepClosureTest.this,
                e1.getMessage (), "Error", JOptionPane.ERROR_MESSAGE );
          }
        }
      }
    } );
    
    
    // setup the close button
    JButton closeButton = new JButton ( "Close" );
    closeButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        System.exit ( 0 );
      }
    } );
    buttons.add ( closeButton );
  }


  private static ProofNode nextNode ( BigStepClosureProofModel model )
  {
    LinkedList < ProofNode > nodes = new LinkedList < ProofNode > ();
    nodes.add ( model.getRoot () );
    while ( !nodes.isEmpty () )
    {
      ProofNode node = nodes.poll ();
      if ( !node.isProven()) //getRules ().length == 0 )
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
}
