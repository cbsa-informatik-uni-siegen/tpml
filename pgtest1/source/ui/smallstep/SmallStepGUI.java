package ui.smallstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

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
		setActionStatus("Undo", false);
		setActionStatus("Redo", false);
	}

	@EditorActionInfo(visible = true, name = "Guess", icon = "none", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
	public void handleGuess() {
		try {
			model.guess(view.getRootNode().getFirstLeaf());
		} catch (IllegalStateException exc) {
			JOptionPane.showMessageDialog(SmallStepGUI.this, exc.getMessage());
		}
	}
	@EditorActionInfo(visible = false, name = "Undo", icon = "icons/undo.gif", accelModifiers = KeyEvent.CTRL_MASK, accelKey = KeyEvent.VK_Z)
	public void handleUndo() {
		// TODO Auto-generated method stub
		
	}
	@EditorActionInfo(visible = false, name = "Redo", icon = "icons/redo.gif", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
	public void handleRedo() {
		// TODO Auto-generated method stub
		
	}
}
