package de.unisiegen.tpml.ui;

import java.util.prefs.Preferences;

/**
 * Manages the preferences for the user interface.
 *
 * @author Christoph Fehling
 * @version $Rev$ 
 *
 */
public class PreferenceManager {
	private Preferences prefs;
	
	public PreferenceManager(){
		prefs = Preferences.userNodeForPackage(this.getClass());
	}
	
	public void putFileName(){
		
	}

}
