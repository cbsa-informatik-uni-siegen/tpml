package ui;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.prefs.Preferences;

public class ThemeManager {

	private static ThemeManager themeManager = null;

	private LinkedList<Theme> themes;

	private Theme defaultTheme;

	private int currentIdx;

	private ThemeManager() {
		this.themes = new LinkedList<Theme>();
		this.defaultTheme = new Theme();

		initializeThemes();
	}

	public static ThemeManager get() {
		if (ThemeManager.themeManager == null) {
			ThemeManager.themeManager = new ThemeManager();
		}
		return ThemeManager.themeManager;
	}

	public int getNumberOfThemes() {
		return this.themes.size() + 1;
	}

	public Theme getTheme(int idx) {
		if (idx == 0) {
			return defaultTheme;
		}

		--idx;
		return themes.get(idx);
	}

	public Theme getCurrentTheme() {
		if (this.currentIdx == 0) {
			return this.defaultTheme;
		}
		return this.themes.get(this.currentIdx - 1);
	}

	public void setCurrentThemeIndex(int idx) {
		this.currentIdx = idx;
	}

	public int getCurrentThemeIndex() {
		return this.currentIdx;
	}

	public Theme addNewTheme() {
		Theme theme = new Theme();
		theme.setName("<Unnamed>");
		themes.add(theme);
		this.currentIdx = themes.size();
		return theme;
	}

	public void removeCurrentTheme() {
		if (this.currentIdx == 0) {
			return;
		}
		this.themes.remove(this.currentIdx - 1);
		if (this.currentIdx < 0) {
			this.currentIdx = 0;
		}
		if (this.currentIdx > this.themes.size()) {
			this.currentIdx = this.themes.size();
		}
	}

	private void initializeThemes() {
		this.defaultTheme.setName("Default");
		this.currentIdx = 0;
		try {
			Preferences prefs = Preferences
					.userNodeForPackage(SettingsGUI.class);
			Preferences themesPref = prefs.node("themes");
			String[] themeNames = themesPref.childrenNames();
			for (int i = 0; i < themeNames.length; i++) {
				Preferences themePref = themesPref.node(themeNames[i]);
				Theme theme = loadTheme(themePref);
				theme.setName(themeNames[i]);
				if (themeNames[i].equals("Default")) {
					this.defaultTheme = theme;
				} else {
					System.out.println("load user theme");
					this.themes.add(theme);
				}
			}
		} catch (Exception e) {
			System.out.println("Could not open preferences");
		}
	}

	private void storeTheme(Preferences parentPrefs, Theme theme) {
		try {
			Preferences themePref = parentPrefs.node(theme.getName());
			for (int i = 0; i < theme.getNumberOfItems(); i++) {
				Preferences itemPref = themePref.node(theme.getItemName(i));

				Font font = theme.getItemFont(i);
				if (font != null) {
					itemPref.put("family", font.getFamily());
					itemPref.putInt("style", font.getStyle());
					itemPref.putInt("size", font.getSize());
				}

				Color color = theme.getItemColor(i);
				itemPref.putInt("red", color.getRed());
				itemPref.putInt("green", color.getGreen());
				itemPref.putInt("blue", color.getBlue());
				itemPref.flush();
			}
			themePref.flush();
		} catch (Exception e) {
			System.out.println("Error saving theme: " + theme.getName());
		}
	}

	private Theme loadTheme(Preferences themePref) {
		Theme theme = new Theme();
		try {
			String[] itemNames = { "Rule", "Expression", "Keyword", "Constant", "Underline", "RuleExpression"  };
			for (int i = 0; i < itemNames.length; i++) {
				Preferences itemPref = themePref.node(itemNames[i]);
				String fontName = itemPref.get("family", "UnknownFontFamily");
				int fontSize = itemPref.getInt("size", -1);
				int fontStyle = itemPref.getInt("style", -1);

				if (!(fontName.equals ("UnknownFontFamily") ||
						fontSize == -1 ||
						fontStyle == -1)) {
					Font font = new Font(fontName, fontStyle, fontSize);
					theme.setItemFont(i, font);
				}
				int red = itemPref.getInt("red", 0);
				int green = itemPref.getInt("green", 0);
				int blue = itemPref.getInt("blue", 0);

				Color color = new Color(red, green, blue);
				theme.setItemColor(i, color);
				theme.setItemName(i, itemNames[i]);

			}

		} catch (Exception e) {
			System.out.println("Could not load theme");
		}
		return theme;
	}

	public void storeThemes(Preferences prefs) {
		Preferences themesPrefs = prefs.node("themes");
		try {
			themesPrefs.removeNode();
		} catch (Exception e) {
			System.out.println("Error removing the themes data");
		}
		themesPrefs = prefs.node("themes");

		storeTheme(themesPrefs, this.defaultTheme);
		for (int i = 0; i < themes.size(); i++) {
			Theme theme = themes.get(i);
			storeTheme(themesPrefs, theme);
		}
		try {
			themesPrefs.flush();
		} catch (Exception e) {
			System.out.println("Errr flushing themes");
		}
	}
	
	
}
