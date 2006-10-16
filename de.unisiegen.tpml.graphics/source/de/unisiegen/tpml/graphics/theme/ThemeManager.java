/**
 * 
 */
package de.unisiegen.tpml.graphics.theme;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.event.EventListenerList;

import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;

/**
 * @author marcell
 *
 */
public class ThemeManager {
	
	private LinkedList<Theme>			themes;
	
	private static ThemeManager		themeManager = null;
	
	private Theme									currentTheme = null;

	private String								currentThemeName;
	
	private EventListenerList			listenerList;
	
	private ThemeManager () {
		
		this.themes = new LinkedList<Theme> ();
		
		this.listenerList = new EventListenerList ();
		
		// load the current themes
		loadThemes ();

		
		// check if there is a theme stored with the name default
		boolean containsDefault = false;
		for (Theme theme : this.themes) {
			if (theme.getName() == "Default") {
				containsDefault = true;
				break;
			}
		}
		
		
		// if there is no default theme: generate one
		if (!containsDefault) {
			Theme theme = new Theme ();
			theme.setName("Default");
			
			this.themes.add(theme);
		}
		
		for (Theme theme : this.themes) {
			if (theme.getName () == this.currentThemeName) {
				this.currentTheme = theme;
				break;
			}
		}
		
	}
	
	public void addThemeManagerListener (ThemeManagerListener listener) {
		this.listenerList.add (ThemeManagerListener.class, listener);
	}
	
	public void removeThemeManagerListener (ThemeManagerListener listener) {
		this.listenerList.remove(ThemeManagerListener.class, listener);
	}
	
	private void fireThemeChanged () {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i=0; i<listeners.length; i++) {
			if (listeners [i] == ThemeManagerListener.class) {
				
				((ThemeManagerListener)listeners [i+1]).currentThemeChanged(this.currentTheme);
			}
		}
	}
	
	public static ThemeManager get () {
		if (ThemeManager.themeManager == null) {
			ThemeManager.themeManager = new ThemeManager ();
		}
		return ThemeManager.themeManager;
	}
	
	public Theme createNewTheme () {
		Theme theme = new Theme ();
		
		this.themes.add(theme);
		
		return theme;
	}
	
	public void removeTheme (Theme theme) {
		this.themes.remove(theme);
	}
	
	public Theme getCurrentTheme () {
		return this.currentTheme;
	}
	
	public void setCurrentThem (Theme theme) {
		this.currentTheme = theme;
		if (theme != null) {
			AbstractRenderer.setTheme(theme, new JLabel ());
		}
		fireThemeChanged();
	}
	
	public int getNumberOfThemes () {
		return this.themes.size();
	}
	
	public Theme getTheme (int idx) {
		if (idx < 0 || idx >= this.themes.size()) {
			return null;
		}
		return this.themes.get(idx);
	}
	
	public void saveThemes () {
		
		// first remove all the current stored themes
		try {
			Preferences pref = Preferences.userNodeForPackage(ThemeManager.class);
			pref.removeNode();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Preferences pref = Preferences.userNodeForPackage(ThemeManager.class);
			
			for (Theme theme : this.themes) {
				
				saveTheme (pref, theme);
				
			}
		
			if (this.currentTheme != null) {
				pref.put("Current" ,this.currentTheme.getName());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void saveTheme (Preferences parentPref, Theme theme) {
		
		try {
			Preferences themePref = parentPref.node(theme.getName());
			
			String[] itemNames = theme.getItemNames();
			for (int i=0; i<itemNames.length; i++) {
				
				Preferences itemPref = themePref.node(itemNames [i]);
				
				// save the font of the theme item
				Font font = theme.getItemFont(i);
				if (font != null) {
					itemPref.put("FontName", font.getName());
					itemPref.putInt("FontSize", font.getSize());
					itemPref.putInt("FontStyle", font.getStyle ());
				}				
				// save the color of the 
				Color color = theme.getItemColor(i);
				itemPref.putInt("Red", color.getRed());
				itemPref.putInt("Green", color.getGreen());
				itemPref.putInt("Blue", color.getBlue());
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void loadThemes () {
		this.themes.clear();

		try {
			
			
			Preferences pref = Preferences.userNodeForPackage(ThemeManager.class);
			
			String[] themeNames = pref.childrenNames();
			for (int i=0; i<themeNames.length; i++) {
				// 
				// get the Preferences node for the theme 
				Preferences themePref = pref.node(themeNames [i]);

				// load the theme itself get him a name 
				Theme theme = loadTheme (themePref);
				theme.setName (themeNames [i]);
				
				
				// and store it
				this.themes.add(theme);
				
			}
			
			this.currentThemeName = pref.get("Current", "Default");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private Theme loadTheme (Preferences prefNode) {
		Theme theme = new Theme ();
			
			String[] items = theme.getItemNames();
			for (int i=0; i<items.length; i++) {
				try {
					if (!prefNode.nodeExists(items [i])) {
						continue;
					}
				
					Preferences pref = prefNode.node(items[i]);
					
					//
					// Extract the font from the preferences file
					String fontName 	= pref.get("FontName", "unknown");
					int fontSize 			= pref.getInt("FontSize", -1);
					int fontStyle 		= pref.getInt("FontStyle", -1);
					
					if (fontName != "unknwon" && fontSize != -1 && fontStyle != -1) {
						Font font = new Font (fontName, fontStyle, fontSize);
						theme.setItemFont(i, font);
					}
					
					//
					// Extract the color for the font from the preferences file 
					int red 		= pref.getInt("Red", 0);
					int green		= pref.getInt("Green", 0);
					int blue		= pref.getInt("Blue", 0);
					
					theme.setItemColor(i, new Color (red, green, blue));
				
				}
				catch (Exception e) {
					e.printStackTrace();
				}
		
		}
		return theme;
	}
	
}
