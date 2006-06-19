package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

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

	private List<DefaultEditorAction> fileactions;

	private List<DefaultEditorAction> runactions;

	private List<DefaultEditorAction> toolbaractions;

	private List<DefaultEditorAction> editactions;

	private Hashtable myactions;

	private boolean modified;

	/**
	 * Constructor.
	 * 
	 * @param ifile
	 *            The file to display.
	 */
	public EditorWindow(SourceFile ifile, MainWindow imywindow) {
		file = ifile;
		file.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("modified")) {
					if ((Boolean) evt.getNewValue()) {
						EditorWindow.this.setModified(true);
						((DefaultEditorAction) myactions.get("Save"))
								.setEnabled(true);
					} else {
						((DefaultEditorAction) myactions.get("Save"))
								.setEnabled(false);
					}
				} else if (evt.getPropertyName().equals("filename")) {
					firePropertyChange("name", evt.getOldValue(), evt
							.getNewValue());
				}
			}
		});
		JPanel menupanel = new JPanel();
		// TODO DONT use BorderLayout!!
		menupanel.setLayout(new BorderLayout());
		menu = new JToolBar();
		actions = new JToolBar();
		// JToggleButton code = new JToggleButton("Source");
		shownComponent = file.getComponent();
		fileactions = new LinkedList<DefaultEditorAction>();
		runactions = new LinkedList<DefaultEditorAction>();
		toolbaractions = new LinkedList<DefaultEditorAction>();
		editactions = new LinkedList<DefaultEditorAction>();
		myactions = new Hashtable();
		modified = false;

		this.setLayout(new BorderLayout());
		menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		addEditorComponent(file);
		add(menupanel, BorderLayout.NORTH);
		menupanel.add(menu, BorderLayout.WEST);
		menupanel.add(actions, BorderLayout.CENTER);
		generateActions();
		//((DefaultEditorAction) myactions.get("Save")).setEnabled(false);
		
	}

	public String getTitle() {
		if (modified) {
			return "*" + file.getFilename();
		} else {
			return file.getFilename();
		}
	}

	private void generateActions() {
		Class me = this.getClass();
		Method[] methods = me.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method tmp = methods[i];
			MainActionInfo actioninfo = tmp.getAnnotation(MainActionInfo.class);
			if (actioninfo != null) {
				DefaultEditorAction newaction = new DefaultEditorAction();
				newaction.setTitle(actioninfo.name());
				newaction.setEnabled(true);
				myactions.put(tmp.getName().substring(6), newaction);
				if (!actioninfo.icon().equals("none")) {
					java.net.URL imgURL = EditorWindow.class
							.getResource(actioninfo.icon());
					if (imgURL != null) {
						newaction.setIcon(new ImageIcon(imgURL));
					} else {
						System.out.println("Imagefile not found!");
					}
				}
				newaction.setGroup(1);
				newaction.setActionlistener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						try {
							tmp.invoke(EditorWindow.this);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Add Handling!!
						}
					}
				});
				if (actioninfo.visibleToolbar() == MainActionInfo.TOOLBAR_VISIBLE) {
					toolbaractions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_FILE) {
					fileactions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_RUN) {
					runactions.add(newaction);
				}
				if (actioninfo.visibleMenu() == MainActionInfo.MENU_EDIT) {
					editactions.add(newaction);
				}
				
			}
		}
	}

	@MainActionInfo(name = "Save", icon = "icons/save.png", visibleMenu = MainActionInfo.MENU_FILE, visibleToolbar = MainActionInfo.TOOLBAR_VISIBLE)
	public void handleSave() {
		if (systemfile == null) {
			handleSaveAs();
		} else {
			writeFile();
		}
	}

	@MainActionInfo(name = "SaveAs", icon = "icons/saveas.png", visibleMenu = MainActionInfo.MENU_FILE, visibleToolbar = MainActionInfo.TOOLBAR_VISIBLE)
	public void handleSaveAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(this);
		File outfile = chooser.getSelectedFile();
		if (outfile != null) {
			try {
				outfile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO Add Handling!!
			}

			systemfile = outfile;
			String oldname = file.getFilename();
			String newname = outfile.getName();
			file.setFilename(newname);
			firePropertyChange("name", oldname, newname);
			writeFile();
		}
	}

	@MainActionInfo(name = "SmallStep", icon = "none", visibleMenu = MainActionInfo.MENU_RUN, visibleToolbar = MainActionInfo.TOOLBAR_HIDDEN)
	public void handleSmallStep() {
		try {
			SmallStepGUI gui = new SmallStepGUI("SmallStep", true, file
					.getDocument().getText(0, file.getDocument().getLength()));
			addEditorComponent(gui);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addEditorComponent(EditorComponent comp) {
		JToggleButton compbutton = new JToggleButton(comp.getTitle());
		Component[] buttons = menu.getComponents();
		for (int i = 0; i < buttons.length; i++) {
			if (((JToggleButton) buttons[i]).getLabel().equals(comp.getTitle())) {
				compbutton = (JToggleButton) buttons[i];
				compbutton
						.removeActionListener(compbutton.getActionListeners()[0]);
				break;
			}
		}
		compbutton.addActionListener(new EditorComponentListener(this, comp,
				compbutton));
		deselectButtons();
		compbutton.setSelected(true);
		menu.add(compbutton);
		remove(getShownComponent());
		add((Component) comp, BorderLayout.CENTER);
		setShownComponent((Component) comp);
		updateActions(comp);
	}

	public void deselectButtons() {
		Component[] buttons = menu.getComponents();
		for (int i = 0; i < buttons.length; i++) {
			((JToggleButton) buttons[i]).setSelected(false);
		}
	}

	private void writeFile() {
		try {
			FileOutputStream out = new FileOutputStream(systemfile);
			out.write(file.getDocument().getText(0,
					file.getDocument().getLength()).getBytes());
			out.flush();
			out.close();
			file.setModified(false);
			if (file.getFilename().startsWith("*"))
				file.setFilename(file.getFilename().substring(1));
		} catch (Exception e) {
			// TODO Add Handling!!
			System.out.println("Error while saving!");
		}
	}

	public void updateActions(EditorComponent comp) {
		actions.removeAll();
		addActions(comp.getActions(), actions);
	}

	private void addActions(List<EditorAction> list, JComponent component) {
		List<EditorAction> actionlist = list;
		for (int i = 0; i < actionlist.size(); i++) {
			final EditorAction action = actionlist.get(i);
			final JButton tmp = new JButton(action.getTitle());
			tmp.setEnabled(action.isEnabled());
			action.addPropertyChangeListener("enabled", new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                  tmp.setEnabled(action.isEnabled());
                }
              });
			component.add(tmp);
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

	public JToolBar getMenu() {
		return menu;
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

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

}