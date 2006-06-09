package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import ui.annotations.MainActionInfo;
import ui.newgui.DefaultEditorAction;
import ui.newgui.EditorAction;
import ui.newgui.EditorComponent;
import ui.newgui.FileWindow;
import ui.newgui.MainWindow;
import ui.smallstep.SmallStepGUI;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;

/**
 * An Editor in the UI. It contains the sourcefile and the excecutions.
 * 
 * @author cfehling
 *
 */
public class EditorWindow extends JPanel implements FileWindow {
	
	private SourceFile file;
	private File systemfile = null;
	private JToolBar menu;
	private JToolBar actions;
	private Component shownComponent;
	//private LinkedList allElements = new LinkedList();
	private List<DefaultEditorAction> fileactions;
	private List<DefaultEditorAction> runactions;
	private List<DefaultEditorAction> toolbaractions;
	private List<DefaultEditorAction> editactions;

	/**
	 * Constructor.
	 * @param ifile The file to display.
	 */
	public EditorWindow (SourceFile ifile, MainWindow imywindow) {
		file = ifile;
		//mywindow =imywindow;
		JPanel menupanel = new JPanel();
		//TODO DONT use BorderLayout!!
		menupanel.setLayout(new BorderLayout());
		menu = new JToolBar();
		actions = new JToolBar();
		JToggleButton code = new JToggleButton("Source");
		shownComponent = file.getComponent();
		fileactions = new LinkedList<DefaultEditorAction>();
		runactions = new LinkedList<DefaultEditorAction>();
		toolbaractions = new LinkedList<DefaultEditorAction>();
		editactions = new LinkedList<DefaultEditorAction>();
		
		this.setLayout(new BorderLayout());
		menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(file.getComponent(), BorderLayout.CENTER);
		menu.add(code);
//	    code.addActionListener(new ActionListener() {
//	    	public void actionPerformed(ActionEvent event) {
//	    		remove(shownComponent);
//	    		add(file.getComponent(), BorderLayout.CENTER);
//	    		shownComponent = file.getComponent();
//	    		repaint();
//	    	}
//	    });
		code.addActionListener(new EditorComponentListener(this, file, code));
		code.setSelected(true);
		add(menupanel, BorderLayout.NORTH);
		menupanel.add(menu, BorderLayout.WEST);
		menupanel.add(actions, BorderLayout.CENTER);
		//initActions();
		generateActions();
	}
	
	public String getTitle()
	{	
		return file.getFilename();
	}
	


	private void generateActions(){
		Class me = this.getClass();
		Method[] methods = me.getDeclaredMethods();
		for (int i = 0; i < methods.length ; i++){
			final Method tmp = methods[i];
			MainActionInfo actioninfo = tmp.getAnnotation(MainActionInfo.class);
			if (actioninfo != null){
				DefaultEditorAction newaction = new DefaultEditorAction();
				newaction.setTitle(actioninfo.name());
				newaction.setEnabled(true);
				if ( ! actioninfo.icon().equals("none")) {
					java.net.URL imgURL = EditorWindow.class.getResource(actioninfo.icon());
				    if (imgURL != null) {
					newaction.setIcon(new ImageIcon(imgURL));
					}
				    else{
				    	System.out.println("Imagefile not found!");
				    }
				}
				newaction.setGroup(1);
				newaction.setActionlistener(
						new ActionListener() {
					    	public void actionPerformed(ActionEvent event) {
					    		try{
					    		tmp.invoke(getMe());
					    		} catch (Exception e){
					    			//TODO Add Handling!!
					    		}
					    		}
					    	});
				if (actioninfo.visibleToolbar() == MainActionInfo.TOOLBAR_VISIBLE){
					toolbaractions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_FILE){
					fileactions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_RUN){
					runactions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_EDIT){
					editactions.add(newaction);
				}
			}
		}
	}
	
	private void initActions(){
		DefaultEditorAction save = new DefaultEditorAction();
		save.setTitle("Save");
		save.setEnabled(true);
		save.setIcon(null);
		save.setGroup(1);
		save.setActionlistener(
				new ActionListener() {
			    	public void actionPerformed(ActionEvent event) {
			    		handleSave();
			    		}
			    	});
		fileactions.add(save);
		toolbaractions.add(save);
		
		DefaultEditorAction saveas = new DefaultEditorAction();
		saveas.setTitle("SaveAs");
		saveas.setEnabled(true);
		saveas.setIcon(null);
		saveas.setGroup(1);
		saveas.setActionlistener(
				new ActionListener() {
			    	public void actionPerformed(ActionEvent event) {
			    		handleSaveAs();
			    		}
			    	});
		fileactions.add(saveas);
		toolbaractions.add(saveas);
		
		DefaultEditorAction smallstep = new DefaultEditorAction();
		smallstep.setTitle("SmallStep");
		smallstep.setEnabled(true);
		smallstep.setIcon(null);
		smallstep.setGroup(1);
		smallstep.setActionlistener(
				new ActionListener() {
			    	public void actionPerformed(ActionEvent event) {
			    		handleSmallStep();
			    	}
			    	});
		runactions.add(smallstep);
	}
	
	@MainActionInfo(name="Save" ,
						    icon="icons/save.png", 
						    visibleMenu=MainActionInfo.MENU_FILE, 
						    visibleToolbar=MainActionInfo.TOOLBAR_VISIBLE)
	public void handleSave(){
		if (systemfile == null){
			handleSaveAs();
		}
		else {
			writeFile();
		}
	}
	
	@MainActionInfo(name="SaveAs" ,
						    icon="icons/saveas.png", 
						    visibleMenu=MainActionInfo.MENU_FILE, 
						    visibleToolbar=MainActionInfo.TOOLBAR_VISIBLE)
	public void handleSaveAs () {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(getMe());
		File outfile = chooser.getSelectedFile();
		if (outfile != null){
		try {outfile.createNewFile();} catch (Exception e){
			e.printStackTrace();
			//TODO Add Handling!!
		}

		systemfile = outfile;
		String oldname = file.getFilename();
		String newname = outfile.getName();
		file.setName(newname);
		firePropertyChange("name", oldname, newname);
		writeFile();
		}
	}
	
	@MainActionInfo(name="SmallStep" ,
						    icon="none", 
						    visibleMenu=MainActionInfo.MENU_RUN, 
						    visibleToolbar=MainActionInfo.TOOLBAR_HIDDEN)
	public void handleSmallStep(){
		try{
		SmallStepGUI gui = new SmallStepGUI ("SmallStep", true, file.getDocument().getText(0, file.getDocument().getLength()));
		addEditorComponent(gui);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	
	private void addEditorComponent ( EditorComponent comp){
		JToggleButton compbutton = new JToggleButton(comp.getTitle());
		Component[] buttons = menu.getComponents();
		for (int i = 0 ; i < buttons.length ; i++){
			if (((JToggleButton)buttons[i]).getLabel().equals(comp.getTitle())){
				compbutton = (JToggleButton)buttons[i];
				compbutton.removeActionListener(compbutton.getActionListeners()[0]);
				break;
			}
		}
		compbutton.addActionListener(new EditorComponentListener(this, comp, compbutton));
		deselectButtons();
		compbutton.setSelected(true);
		menu.add(compbutton);
		remove(getShownComponent());
		add((Component)comp, BorderLayout.CENTER);
		setShownComponent((Component)comp);
		updateActions(comp);
		repaint();
	}
	
	public void deselectButtons(){
		Component[] buttons = menu.getComponents();
		for (int i = 0 ; i < buttons.length ; i++){
			((JToggleButton)buttons[i]).setSelected(false);
		}
	}
	
	public void Close(){
		
	}
	
	private void writeFile (){
		try{
			FileOutputStream out = new FileOutputStream(systemfile);
			out.write(file.getDocument().getText(0,file.getDocument().getLength()).getBytes());
			out.flush();
			out.close();
			} catch (Exception e){//TODO Add Handling!!
				System.out.println("Error while saving!");
			}
	}
	
	public void updateActions(EditorComponent comp){
		actions.removeAll();
		addActions(comp.getActions(), actions);
	}
	
	private void addActions(List<EditorAction> list, JComponent component){
		List<EditorAction> actionlist = list;
		  for(int i = 0 ; i < actionlist.size() ; i++){
			 EditorAction action = actionlist.get(i);

			//ImageIcon icon = new ImageIcon(action.getIcon());
			 JButton tmp = new JButton (action.getTitle());
			 component.add( tmp);
			 tmp.addActionListener(action.getActionListener());
			}
	}

	public Component getShownComponent() {
		return shownComponent;
	}

	public void setShownComponent(Component shownComponent) {
		this.shownComponent = shownComponent;
	}

	public List<EditorAction> getFileActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(fileactions);
		return tmp;
	}
	

	public List<EditorAction> getToolbarActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(toolbaractions);
		return tmp;
	}

	public List<EditorAction> getEditActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(editactions);
		return tmp;
	}

	public List<EditorAction> getRunActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(runactions);
		return tmp;
	}	

//	public String getTitle() {
//		return getName();
//	}
	
	public JToolBar getMenu(){
		return menu;
	}
	
	private Component getMe (){
		return this;
	}

	public SourceFile getFile() {
		return file;
	}

	public void setFile(SourceFile file) {
		this.file = file;
	}

	public File getSystemfile() {
		return systemfile;
	}

	public void setSystemfile(File systemfile) {
		this.systemfile = systemfile;
	}
	
}