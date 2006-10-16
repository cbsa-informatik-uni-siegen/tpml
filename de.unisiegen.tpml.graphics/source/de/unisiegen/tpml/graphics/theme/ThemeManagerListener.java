/**
 * 
 */
package de.unisiegen.tpml.graphics.theme;

import java.util.EventListener;

/**
 * @author marcell
 *
 */
public interface ThemeManagerListener extends EventListener{

	public void currentThemeChanged (Theme theme);
	
}
