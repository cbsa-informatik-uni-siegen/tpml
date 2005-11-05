package ui;

import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.StyledEditorKit;

public class SourceFile {

	static private int num = 0;
	private JScrollPane scrollPane;
	private JEditorPane editorPane;
	private String filename;

	
	public SourceFile ()
	{
		filename = "newfile" + num + ".ml";
		num++;
		scrollPane = new JScrollPane ();
		editorPane =  new JEditorPane ();
		editorPane.setName (filename);
		editorPane.setAutoscrolls (false);
		editorPane.setEditorKit (new StyledEditorKit ());
		editorPane.setDocument (new MLStyledDocument ());
		scrollPane.setViewportView (editorPane);
		scrollPane.setName (filename);
	}
	
	public void setName (String name)
	{
		filename = name;
		scrollPane.setName (filename);
	}
	
	public String getFilename ()
	{
		return (filename);
	}
	
	public Component getComponent ()
	{
		return (scrollPane);
	}

	public MLStyledDocument getDocument() {
		return (MLStyledDocument)editorPane.getDocument();
	}
}
