package ui.smallstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;
import ui.annotations.EditorActionInfo;
import ui.newgui.AbstractEditorComponent;
import ui.newgui.EditorComponent;

public class SmallStepGUI extends AbstractEditorComponent implements EditorComponent {
  private static final long serialVersionUID = 9199836815081138367L;
  
  private SmallStepView view;
  
  private JScrollPane scrollPane;
  
  private SmallStepProofModel model;
  
  public SmallStepGUI(String title, String program) throws Exception {
    super(title);
    
    this.setLayout(new BorderLayout());
    view = new SmallStepView();
    this.add(view, BorderLayout.CENTER);
    
    this.scrollPane = new JScrollPane();
    this.view = new SmallStepView();
    this.scrollPane.getViewport().add(this.view);
    this.add(this.scrollPane, BorderLayout.CENTER);
    
    SmallStepProofModelFactory sspmf = SmallStepProofModelFactory.newInstance();
    model = sspmf.newProofModel(program);
    view.setModel(model);
    
    this.scrollPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent event) {
        view.setAvailableSize(new Dimension(scrollPane.getWidth()
            - scrollPane.getVerticalScrollBar().getWidth() * 2,
            scrollPane.getHeight()));
      }
    });
    
    this.scrollPane.setBackground(Color.WHITE);
    this.scrollPane.getViewport().setBackground(Color.WHITE);
    
    // synchronize the redo action
    setActionStatus("Redo", false);
    this.model.addPropertyChangeListener("redoable", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        setActionStatus("Redo", (Boolean)event.getNewValue());
      }
    });
    
    // synchronize the undo action
    setActionStatus("Undo", false);
    this.model.addPropertyChangeListener("undoable", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        setActionStatus("Undo", (Boolean)event.getNewValue());
      }
    });
  }
  
  @EditorActionInfo(visible = true, name = "Guess", icon = "icons/next.png", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
  public void handleGuess() {
    try {
      model.guess(view.getRootNode().getFirstLeaf());
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(SmallStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  @EditorActionInfo(visible = false, name = "Undo", icon = "icons/undo.gif", accelModifiers = KeyEvent.CTRL_MASK, accelKey = KeyEvent.VK_Z)
  public void handleUndo() {
    try {
      model.undo();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(SmallStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  @EditorActionInfo(visible = false, name = "Redo", icon = "icons/redo.gif", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
  public void handleRedo() {
    try {
      model.redo();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(SmallStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
