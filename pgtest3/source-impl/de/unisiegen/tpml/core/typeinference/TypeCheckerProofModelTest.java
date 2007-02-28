package de.unisiegen.tpml.core.typeinference;

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
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.ExpressionProofNode;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;

/**
 * Test class for the {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel} class.
 *
 * @author Benedikt Meurer
 * @version $Id: BigStepProofModelTest.java 268 2006-09-19 12:45:02Z benny $
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
@SuppressWarnings("serial")
public final class TypeCheckerProofModelTest extends JFrame {
  /**
   * Simple test expression.
   */
//	private static final String SIMPLE = "(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)";
	private static final String SIMPLE = "lambda f:'a->'b. lambda x.f (f x)";
  
  
  //
  // Constructor
  //
  
  /**
   * Default constructor.
   */
  public TypeCheckerProofModelTest(final TypeCheckerProofModel model) {
    // setup the frame
    setLayout(new BorderLayout());
    setSize(630, 580);
    setTitle("TypeCheckerProofModel Test");

    // setup the tree panel
    JPanel treePanel = new JPanel(new BorderLayout());
    treePanel.setBorder(BorderFactory.createEtchedBorder());
    add(treePanel, BorderLayout.CENTER);
    
    // setup the tree
    final JTree tree = new JTree(model);
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
          JOptionPane.showMessageDialog(TypeCheckerProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          JOptionPane.showMessageDialog(TypeCheckerProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
          JOptionPane.showMessageDialog(TypeCheckerProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            model.translateToCoreSyntax((ExpressionProofNode)path.getLastPathComponent(), false);
          }
        }
        catch (Exception e) {
          JOptionPane.showMessageDialog(TypeCheckerProofModelTest.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
  
  private static ProofNode nextNode(TypeCheckerProofModel model) {
    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
    nodes.add(model.getRoot());
    while (!nodes.isEmpty()) {
      ProofNode node = nodes.poll();
      if (node.getRules().length == 0) {
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
      // parse the program (using L4)
      LanguageFactory factory = LanguageFactory.newInstance();
      Language language = factory.getLanguageById("l4");
      Expression expression = language.newParser(new StringReader(SIMPLE)).parse();
      TypeCheckerProofModel model = language.newTypeCheckerProofModel(expression);
      
      // evaluate the resulting small step expression
      TypeCheckerProofModelTest window = new TypeCheckerProofModelTest(model);
      window.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      window.setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
