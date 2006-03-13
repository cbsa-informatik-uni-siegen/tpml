package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * An Editor in the UI. It contains the sourcefile and the excecutions.
 * 
 * @author cfehling
 *
 */
public class EditorWindow extends JPanel {
	
	private Mainwindow mywindow;
	private SourceFile file;
	private JPanel menu;
	private Component shownComponent;
	private LinkedList allElements = new LinkedList();
	
	/**
	 * Constructor.
	 * @param ifile The file to display.
	 */
	public EditorWindow (SourceFile ifile, Mainwindow imywindow) {
		file = ifile;
		mywindow =imywindow;
		menu = new JPanel();
		JButton code = new JButton("Code");
		shownComponent = file.getComponent();
		
		this.setLayout(new BorderLayout());
		menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(file.getComponent(), BorderLayout.CENTER);
		allElements.add(file);
		menu.add(code);
	    code.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		remove(shownComponent);
	    		add(file.getComponent(), BorderLayout.CENTER);
	    		shownComponent = file.getComponent();
	    		repaint();
	    	}
	    });
		add(menu, BorderLayout.NORTH);
	}
	
	public String getName()
	{	
		return file.getFilename();
	}
	
	public void handleSmallStep(SmallStepGUI igui){
		SmallStepGUI gui = igui;
		JButton smallstep = new JButton("SmallStep");
		menu.add(smallstep);
		smallstep.addActionListener(new SmallStepListener(this, gui, smallstep));
		remove(getShownComponent());
		add(gui, BorderLayout.CENTER);
		setShownComponent(gui);
//		allElements.add(gui);
//		gui.getCom
		repaint();
	}
	
//	public void removeShownComponent ()
//	{
//		for (int i = 0 ; i < allElements.size() ; i ++)
//		{
//			Component tmp = allElements.get(i);
//			if (tmp.equals(shownComponent))
//			{
//				allElements.remove(i);
//				remove(shownComponent);				
//				add(allElements.get(i--), BorderLayout.CENTER);
//				repaint();
//			}
//		}	
//	}

	public Component getShownComponent() {
		return shownComponent;
	}

	public void setShownComponent(Component shownComponent) {
		this.shownComponent = shownComponent;
	}

	public JPanel getMenu() {
		return menu;
	}	
	
}