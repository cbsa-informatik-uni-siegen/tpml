package ui.bigstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import bigstep.BigStepProofModel;
import bigstep.BigStepProofModelFactory;

import ui.annotations.EditorActionInfo;
import ui.newgui.AbstractEditorComponent;
import ui.newgui.EditorComponent;

public class BigStepGUI extends AbstractEditorComponent implements EditorComponent {
	private static final long serialVersionUID = 2406644116586088635L;

	private JScrollPane scrollPane;
	
	private BigStepView	view;
  
  private BigStepProofModel model;
  public BigStepGUI(String title, String program) throws Exception {
    super(title);
    
    
    this.setLayout(new BorderLayout());
    this.scrollPane = new JScrollPane();
    this.view = new BigStepView();
    this.scrollPane.getViewport().add(this.view);
    this.add(this.scrollPane, BorderLayout.CENTER);
    
    BigStepProofModelFactory bspmf = BigStepProofModelFactory.newInstance();
    model = bspmf.newProofModel(program);
    view.setModel(model);
    
    
    this.scrollPane.setBackground(Color.WHITE);
    this.scrollPane.getViewport().setBackground(Color.WHITE);
    this.scrollPane.addComponentListener(new ComponentAdapter() {
    	public void componentResized (ComponentEvent event) {
    		view.layout(scrollPane.getWidth());
    	}
    });
            
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
//      model.guess(view.getRootNode().getFirstLeaf());
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(BigStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  @EditorActionInfo(visible = false, name = "Undo", icon = "icons/undo.gif", accelModifiers = KeyEvent.CTRL_MASK, accelKey = KeyEvent.VK_Z)
  public void handleUndo() {
    try {
//      model.undo();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(BigStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  @EditorActionInfo(visible = false, name = "Redo", icon = "icons/redo.gif", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
  public void handleRedo() {
    try {
//      model.redo();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(BigStepGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
