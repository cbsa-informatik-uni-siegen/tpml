package ui;

import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.StyledEditorKit;

import ui.newgui.EditorAction;
import ui.newgui.EditorComponent;

import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

public class SourceFile implements EditorComponent{
  static private int num = 0;
  private JScrollPane scrollPane;
  private JEditorPane editorPane;
  private String filename;
  
  public SourceFile() {
    this.filename = "newfile" + num + ".ml";
    num++;
    this.scrollPane = new JScrollPane ();
    this.editorPane =  new JEditorPane ();
    this.editorPane.setName (filename);
    this.editorPane.setAutoscrolls (false);
    this.editorPane.setEditorKit (new StyledEditorKit ());
    this.editorPane.setDocument (new MLStyledDocument ());
    this.scrollPane.setViewportView (editorPane);
    this.scrollPane.setName (filename);
  }
  
  public void setName(String name) {
    this.filename = name;
    this.scrollPane.setName(this.filename);
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public Component getComponent() {
    return this.scrollPane;
  }
  
  public MLStyledDocument getDocument() {
    return (MLStyledDocument)editorPane.getDocument();
  }

public List<EditorAction> getActions() {
	return new LinkedList<EditorAction>();
}

public String getTitle() {

	return filename;
}

public Component getDisplay() {
	return getComponent();
}

public void setFilename(String filename) {
	this.filename = filename;
}

}
