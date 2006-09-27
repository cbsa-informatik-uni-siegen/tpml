package de.unisiegen.tpml.core.smallstep;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;

/**
 * Test class for the small step interpreter.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SuppressWarnings("serial")
public class SmallStepProofModelTest extends JFrame {
  /**
   * Simple test expression.
   */
  private static final String SIMPLE = "(lambda (x,y):int*int.x+y) (1,9)";

  
  
  //
  // Renderer
  //
  
  /**
   * The tree renderer.
   */
  class Renderer extends DefaultTreeCellRenderer {
    /**
     * {@inheritDoc}
     * 
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      boolean memoryEnabled = ((SmallStepProofModel)tree.getModel()).isMemoryEnabled();
      SmallStepProofNode node = (SmallStepProofNode)value;
      StringBuilder builder = new StringBuilder();
      builder.append('[');
      for (int n = 0; n < node.getSteps().length; ++n) {
        if (n > 0)
          builder.append(", ");
        builder.append(node.getSteps()[n].getRule().getName());
      }
      builder.append("] -> ");
      if (memoryEnabled) {
        builder.append('(');
      }
      builder.append(node.getExpression());
      if (memoryEnabled) {
        builder.append(", ");
        builder.append(node.getStore());
        builder.append(')');
      }
      setText(builder.toString());
      return this;
    }
  }
  
  
  
  //
  // Constructor
  //
  
  /**
   * Default constructor.
   */
  public SmallStepProofModelTest(final SmallStepProofModel model) {
    // setup the frame
    setLayout(new BorderLayout());
    setSize(630, 580);
    setTitle("TestSmallStepProofModel test");

    // setup the tree panel
    JPanel treePanel = new JPanel(new BorderLayout());
    treePanel.setBorder(BorderFactory.createEtchedBorder());
    add(treePanel, BorderLayout.CENTER);
    
    // setup the tree
    final JTree tree = new JTree(model);
    tree.setCellRenderer(new Renderer());
    treePanel.add(tree, BorderLayout.CENTER);
    
    // setup the button panel
    JPanel buttons = new JPanel(new FlowLayout());
    add(buttons, BorderLayout.SOUTH);
    
    // setup the guess button
    JButton guessButton = new JButton("Guess");
    guessButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          // guess the last node
          ProofNode node = model.getRoot().getLastLeaf();
          model.guess(node);
          
          // expand to the last node
          tree.expandRow(tree.getRowCount() - 1);
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(SmallStepProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    buttons.add(guessButton);
    
    // setup the undo button
    final JButton undoButton = new JButton("Undo");
    undoButton.setEnabled(false);
    undoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          // undo the last change
          model.undo();
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(SmallStepProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    model.addPropertyChangeListener("undoable", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        undoButton.setEnabled(model.isUndoable());
      }
    });
    buttons.add(undoButton);
    
    // setup the redo button
    final JButton redoButton = new JButton("Redo");
    redoButton.setEnabled(false);
    redoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          // redo the last undone change
          model.redo();
          
          // expand to the last node
          tree.expandRow(tree.getRowCount() - 1);
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(SmallStepProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    model.addPropertyChangeListener("redoable", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        redoButton.setEnabled(model.isRedoable());
      }
    });
    buttons.add(redoButton);
    
    // setup the translate button
    JButton translateButton = new JButton("Translate");
    translateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          // translate the last node
          ProofNode node = model.getRoot().getLastLeaf();
          model.translateToCoreSyntax(node, false);
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(SmallStepProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    buttons.add(translateButton);
    
    // setup the close button
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    buttons.add(closeButton);
  }
  
  
  
  //
  // Program entry point
  //
  
  /**
   * Runs the small step interpreter test.
   * 
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    try {
      // parse the program (using L3)
      LanguageFactory factory = LanguageFactory.newInstance();
      Language language = factory.getLanguageById("l3");
      Expression expression = language.newParser(new StringReader(SIMPLE)).parse();
      SmallStepProofModel model = language.newSmallStepProofModel(expression);
      
      // evaluate the resulting small step expression
      SmallStepProofModelTest tv = new SmallStepProofModelTest(model);
      tv.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      tv.setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
