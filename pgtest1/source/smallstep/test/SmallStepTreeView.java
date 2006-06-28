package smallstep.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;
import smallstep.SmallStepProofNode;

import common.ProofNode;

/**
 * Test class for the small step interpreter.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SuppressWarnings("serial")
public class SmallStepTreeView extends JFrame {
  /**
   * Simple test expression.
   */
  //private static final String SIMPLE = "not ((+) 1 ( (/) 10 (9 + ~- (8 + 1))))";
  //private static final String SIMPLE = "let f = ref (lambda x.x) in let fact = lambda x.if x = 0 then 1 else x * (!f (x - 1)) in (f := fact, !f 3)";
  private static final String SIMPLE = "let rec f = lambda x.if x = 0 then 1 else x * (f (x - 1)) in f 3";
  //private static final String SIMPLE = "(1 + 2, 5 * 8, let x = 9 in (8,(+) 9 x), y)";
  //private static final String SIMPLE = "let (x, y, z) = (~- 8, not true, 1) in x > z || y";
  //private static final String SIMPLE = "let rev l = let rec rev_helper s t = if is_empty s then t else rev_helper (tl s) ((hd s) :: t) in rev_helper l [] in rev [1+5,2+5,3+5]";
  //private static final String SIMPLE = "let rec f s t = s + t in f [] [1,2,3]";
  //private static final String SIMPLE = "let x = 1 in x";

  
  
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
      SmallStepProofNode node = (SmallStepProofNode)value;
      StringBuilder builder = new StringBuilder();
      builder.append('[');
      for (int n = 0; n < node.getSteps().length; ++n) {
        if (n > 0)
          builder.append(", ");
        builder.append(node.getSteps()[n].getRule().getName());
      }
      builder.append("] -> ");
      if (node.getExpression().containsReferences()) {
        builder.append('(');
      }
      builder.append(node.getExpression());
      if (node.getExpression().containsReferences()) {
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
  public SmallStepTreeView(final SmallStepProofModel model) {
    // setup the frame
    setLayout(new BorderLayout());
    setSize(630, 580);
    setTitle("SmallStepTreeView test");

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
          JOptionPane.showMessageDialog(SmallStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          JOptionPane.showMessageDialog(SmallStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          JOptionPane.showMessageDialog(SmallStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          model.translateToCoreSyntax(node);
        }
        catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(SmallStepTreeView.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
      // parse the program
      SmallStepProofModelFactory factory = SmallStepProofModelFactory.newInstance();
      SmallStepProofModel model = factory.newProofModel(SIMPLE);
      
      // evaluate the resulting small step expression
      SmallStepTreeView tv = new SmallStepTreeView(model);
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
