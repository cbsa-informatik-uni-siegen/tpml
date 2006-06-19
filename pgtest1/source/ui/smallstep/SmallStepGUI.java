package ui.smallstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;
import ui.annotations.EditorActionInfo;
import ui.newgui.DefaultEditorAction;
import ui.newgui.EditorAction;
import ui.newgui.EditorComponent;


public class SmallStepGUI extends JPanel implements EditorComponent {
	
	private SmallStepView			view;
	
	private JScrollPane				scrollPane;
	
	private SmallStepProofModel		model;
	
	//next lines added by christoph
	private List<DefaultEditorAction>	actions;
	
	private String title;
	

	public SmallStepGUI ( String title, boolean modal, String program) {
		
		//next 3 lines were added by christoph
		actions = new LinkedList<DefaultEditorAction>();
		generateActions();
		this.title = title;
		
		this.setLayout(new BorderLayout ());
		view = new SmallStepView ();
		this.add(view, BorderLayout.CENTER);
		
		this.scrollPane = new JScrollPane();
		this.view = new SmallStepView ();
		this.scrollPane.getViewport().add(this.view);
		this.add(this.scrollPane, BorderLayout.CENTER);
		
		
		SmallStepProofModelFactory sspmf = SmallStepProofModelFactory.newInstance();
		try {
			model = sspmf.newProofModel(program);
			view.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		this.scrollPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent event) {
				view.setAvailableSize(new Dimension(scrollPane.getWidth() -
						scrollPane.getVerticalScrollBar().getWidth() * 2, scrollPane.getHeight()));
			}
		});
		
		this.scrollPane.setBackground(Color.WHITE);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		setSize (600, 800);
	}
	
	//all following methods added by christoph
	@EditorActionInfo(name="Guess" ,
		    icon="none")
	public void guess(){
		try {
			model.guess(view.getRootNode().getFirstLeaf());
		} catch (IllegalStateException exc) {
			JOptionPane.showMessageDialog(SmallStepGUI.this, exc.getMessage());
		}
	}

	private void generateActions(){
		Class me = this.getClass();
		Method[] methods = me.getDeclaredMethods();
		for (int i = 0; i < methods.length ; i++){
			final Method tmp = methods[i];
			EditorActionInfo actioninfo = tmp.getAnnotation(EditorActionInfo.class);
			if (actioninfo != null){
				DefaultEditorAction newaction = new DefaultEditorAction();
				newaction.setTitle(actioninfo.name());
				newaction.setEnabled(true);
				if ( ! actioninfo.icon().equals("none")) {
					//newaction.setIcon();
					}
				newaction.setGroup(1);
				newaction.setActionlistener(
						new ActionListener() {
					    	public void actionPerformed(ActionEvent event) {
					    		try{
					    		tmp.invoke(SmallStepGUI.this);
					    		} catch (Exception e){
					    			e.printStackTrace();
					    			//TODO Add Handling!!
					    		}
					    		}
					    	});
				actions.add(newaction);
			}
		}
	}

	public List<EditorAction> getActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(actions);
		return tmp;
	}


	public String getTitle() {
		return title;
	}

	public Component getDisplay() {
		return this;
	}
}
