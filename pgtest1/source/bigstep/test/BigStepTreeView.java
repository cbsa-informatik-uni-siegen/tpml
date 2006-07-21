package bigstep.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import bigstep.BigStepProofModel;
import bigstep.BigStepProofModelFactory;
import bigstep.BigStepProofNode;

import common.ProofNode;

/**
 * Test class for the big step interpreter.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SuppressWarnings("serial")
public class BigStepTreeView extends JFrame {
  /**
   * Simple test expression.
   */
  //private static final String SIMPLE = "(lambda id.id) 1";
  //private static final String SIMPLE = "1 + 2 * (4 / 5)";
  //private static final String SIMPLE = "if true then 1 + (0 / 0) else 1 / 0";
  //private static final String SIMPLE = "let rec f x = if x = 0 then 1 else x * f (x - 1) in f 3";
  //private static final String SIMPLE = "#2_2 (1 + 1, 2 + 2, 3 + 3, (4 + 4))";
  //private static final String SIMPLE = "let (x, y, z) = (1, 2, 3) in x + z + y";
  //private static final String SIMPLE = "let f = lambda (x, y, z).x + y + z in f (1, 2, 3)";
  //private static final String SIMPLE = "1 + 1;2 + 2; 3+3";
  //private static final String SIMPLE = "let v = ref (1) in !v";
  //private static final String SIMPLE = "let f = ref (lambda x.x) in f := (lambda x.if x = 0 then 1 else x * ! f (x - 1)); ! f 3";
  //private static final String SIMPLE = "let y = ref 1 in let x = ref 3 in (while (!x > 0) do (y := !y * !x; x := !x - 1)); !y";
  private static final String SIMPLE = "lambda x.x x";

  
  
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
      BigStepProofNode node = (BigStepProofNode)value;
      StringBuilder builder = new StringBuilder();
      boolean memoryEnabled = ((BigStepProofModel)tree.getModel()).isMemoryEnabled();
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
      builder.append(" \u21d3 ");
      if (node.getResult() != null) {
        if (memoryEnabled) {
          builder.append('(');
        }
        builder.append(node.getResult().getValue());
        if (memoryEnabled) {
          builder.append(", ");
          builder.append(node.getResult().getStore());
          builder.append(')');
        }
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
  public BigStepTreeView(final BigStepProofModel model) {
    // setup the frame
    setLayout(new BorderLayout());
    setSize(630, 580);
    setTitle("BigStepTreeView test");

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
          model.guess(nextNode(model));
          
          // expand to the all nodes
          for (int n = 0; n < tree.getRowCount(); ++n) {
            tree.expandRow(n);
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(BigStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          JOptionPane.showMessageDialog(BigStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          for (int n = 0; n < tree.getRowCount(); ++n) {
            tree.expandRow(n);
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(BigStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          TreePath path = tree.getSelectionPath();
          if (path != null) {
            model.translateToCoreSyntax((ProofNode)path.getLastPathComponent());
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(BigStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
  
  private static ProofNode nextNode(BigStepProofModel model) {
    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
    nodes.add(model.getRoot());
    while (!nodes.isEmpty()) {
      ProofNode node = nodes.poll();
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
  public static void main(String[] args) {
    try {
      // parse the program
      BigStepProofModelFactory factory = BigStepProofModelFactory.newInstance();
      BigStepProofModel model = factory.newProofModel(SIMPLE);
      
      // evaluate the resulting small step expression
      BigStepTreeView tv = new BigStepTreeView(model);
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
