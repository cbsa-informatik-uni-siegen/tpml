package ui;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JComboBox;

public class Theme {

	private class ThemeItem {
		private String	name;
		
		private Font	font;
		
		private Color	color;
		
		public ThemeItem () {
			
		}
		public ThemeItem (String name, Font font, Color color) {
			this.name = name;
			this.font = font;
			this.color = color;
		}
		
		public void setName (String name) {
			this.name = name;
		}
		
		public String getName () {
			return this.name;
		}
		
		public void setFont (Font font) {
			this.font = font;
		}
		
		public void setColor (Color color) {
			this.color = color;
		}
		
		public Font getFont () {
			return this.font;
		}
		
		public Color getColor () {
			return this.color;
		}
		
	}
	
	private LinkedList<ThemeItem>	items;
	
	private String					name;
	
	public Theme () {
		this.items = new LinkedList<ThemeItem>();
		Font f = new JComboBox().getFont();
		
		this.items.add(new ThemeItem("Expression", f, new Color (0, 0, 0)));
		this.items.add(new ThemeItem("Keyword", f, new Color(0, 0, 0)));
		this.items.add(new ThemeItem("Constant", f, new Color(0, 0, 0)));
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getNumberOfItems () {
		return this.items.size ();
	}
	
	public String getItemName (int idx) {
		return this.items.get(idx).getName();
	}
	
	public Font getItemFont (int idx) {
		return this.items.get(idx).getFont();
	}
	
	public Color getItemColor (int idx) {
		return this.items.get(idx).getColor();
	}
	
	public void setItemName (int idx, String name) {
		this.items.get(idx).setName(name);
	}
	
	public void setItemFont (int idx, Font font) {
		this.items.get(idx).setFont(font);
	}
	
	public void setItemColor (int idx, Color color) {
		this.items.get(idx).setColor (color);
	}
	
	public String[] getItemNames () {
		String [] itemNames = new String [this.items.size ()];
		for (int i=0; i<this.items.size (); i++) {
			itemNames [i] = this.items.get(i).getName ();
		}
		return itemNames;
	}
}
