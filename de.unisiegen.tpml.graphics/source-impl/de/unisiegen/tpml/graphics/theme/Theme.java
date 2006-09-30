
package de.unisiegen.tpml.graphics.theme;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JComboBox;


public class Theme {
	
	public static final int	TYPE_RULE						= 0;
	
	public static final int TYPE_EXPRESSION			= 1;
	
	public static final int TYPE_KEYWORD				= 2;
	
	public static final int TYPE_CONSTANT				= 3;
	
	public static final	int TYPE_UNDERLINE			= 4;
	
	public static final int TYPE_RULEEXPRESSION	= 5;
	
	public static final int TYPE_ENVIRONMENT		= 6;
	
	public static final int TYPE_TYPE						= 7;
	
	
	private class ThemeItem {
		private String			itemName;
		
		private Font				font;
		
		private Color				color;
		
		private int					type;
				
		public ThemeItem () {
			
		}
		public ThemeItem (int type, String name, Color color) {
			this.type			= type;
			this.itemName	= name;
			this.color 		= color;
			this.font 		= null;
		}
		public ThemeItem (int type, String name, Font font, Color color) {
			this.type			= type;
			this.itemName	= name;
			this.font 		= font;
			this.color 		= color;
		}
		
		public void setName (String name) {
			this.itemName = name;
		}
		
		public String getName () {
			return this.itemName;
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
		
		public int getType () {
			return this.type;
		}
	}
	
	private LinkedList<ThemeItem>		items;
	
	private String									name;
	
	public Theme () {
		this.items = new LinkedList<ThemeItem>();
		Font f = new JComboBox().getFont();
		
		this.items.add(new ThemeItem(Theme.TYPE_RULE, "Rule", f, new Color(0, 0, 0)));
		this.items.add(new ThemeItem(Theme.TYPE_EXPRESSION, "Expression", f, new Color (0, 0, 0)));
		this.items.add(new ThemeItem(Theme.TYPE_KEYWORD, "Keyword", f, new Color(128, 0, 0)));
		this.items.add(new ThemeItem(Theme.TYPE_CONSTANT, "Constant", f, new Color(0, 0, 128)));
		this.items.add(new ThemeItem(Theme.TYPE_UNDERLINE, "Underline", new Color (255, 0, 0)));
		this.items.add(new ThemeItem(Theme.TYPE_RULEEXPRESSION, "RuleExpression", f, new Color (0, 0, 0)));
		this.items.add(new ThemeItem(Theme.TYPE_ENVIRONMENT, "Environment", f, new Color (128, 128, 128)));
		this.items.add(new ThemeItem(Theme.TYPE_TYPE, "Type", f, new Color (0.0f, 0.6f, 0.0f)));
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
	
	public boolean hasItemFont (int idx) {
		return this.items.get(idx).getFont () != null;
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
