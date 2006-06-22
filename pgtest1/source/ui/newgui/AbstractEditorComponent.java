package ui.newgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import ui.EditorWindow;
import ui.SourceFile;
import ui.annotations.EditorActionInfo;

public abstract class AbstractEditorComponent extends JPanel implements EditorComponent{
	private List<DefaultEditorAction>	actions;
	private String title;
	private Hashtable myactions;

	
	protected AbstractEditorComponent(String title){
		this.title = title;
		actions = new LinkedList<DefaultEditorAction>();
		myactions = new Hashtable();
		generateActions();
	}
	public List<EditorAction> getActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(actions);
		return tmp;
	}
	

	public String getTitle() {
		return title;
	}
	
	private void generateActions() {
		Class me = this.getClass();
		Method[] methods = me.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method tmp = methods[i];
			EditorActionInfo actioninfo = tmp
					.getAnnotation(EditorActionInfo.class);
			if (actioninfo != null) {
				DefaultEditorAction newaction = new DefaultEditorAction();
				newaction.setTitle(actioninfo.name());
				newaction.setEnabled(true);
				if (!actioninfo.icon().equals("none")) {
					java.net.URL imgURL = EditorWindow.class
							.getResource(actioninfo.icon());
					if (imgURL != null) {
						newaction.setIcon(new ImageIcon(imgURL));
					} else {
						System.out.println("Imagefile not found!");
						//TODO add handling!
					}
				}
				newaction.setAccelModifiers(actioninfo.accelModifiers());
				newaction.setAccelKey(actioninfo.accelKey());
				newaction.setVisible(actioninfo.visible());
				//TODO enable grouping
				newaction.setGroup(1);
				newaction.setActionlistener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						try {
							tmp.invoke(AbstractEditorComponent.this);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Add Handling!!
						}
					}
				});
				actions.add(newaction);
				myactions.put(tmp.getName().substring(6), newaction);
			}
		}
	}
	
	public void setActionStatus(String name, boolean enabled) {
		((DefaultEditorAction) myactions.get(name)).setEnabled(enabled);
	}
	
	  public EditorAction getAction (String action){
		 return (EditorAction)myactions.get(action);
	  };

}
