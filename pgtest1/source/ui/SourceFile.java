package ui;

import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.StyledEditorKit;
import java.awt.event.*;

public class SourceFile {
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
}
