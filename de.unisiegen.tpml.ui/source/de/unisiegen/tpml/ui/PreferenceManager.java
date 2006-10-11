package de.unisiegen.tpml.ui;

import java.io.File;
import java.util.LinkedList;
import java.util.prefs.Preferences;

/**
 * Manages the preferences for the user interface.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * 
 * @see java.util.prefs.Preferences
 */
public class PreferenceManager {
	//
	// Attributes
	//
	
	/**
	 * The {@link Preferences} object for the node where the settings are stored and loaded.
	 * 
	 * @see Preferences
	 */
	private Preferences prefs;

	
	
	//
	// Constructor
	//
	
	/**
	 * Allocates a new <code>PreferencesManager</code>.
	 */
	public PreferenceManager() {
		this.prefs = Preferences.userNodeForPackage(this.getClass());
	}

	
	
	//
	// Primitives
	//
	
	/**
	 * TODO Add documentation here.
	 */
	public void setRecentlyUsed(LinkedList<HistoryItem> list) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			this.prefs.put("item" + i, list.get(i).getFile().toURI().normalize().toASCIIString());
		}

	}

	/**
	 * TODO Add documentation here.
	 */
	public LinkedList<HistoryItem> getRecentlyUsed() {
		int count = 0;
		String result = "";
		LinkedList<HistoryItem> list = new LinkedList<HistoryItem>();
		while (true) {
			result = this.prefs.get("item" + count, "end");
			if (result.equals("end")) break;
			
			list.add(new HistoryItem(new File(result)));
			count++;
		}
		return list;
	}
	
	/**
	 * TODO Add documentation here.
	 */
	public void setAdvanced (boolean status){
		this.prefs.putBoolean("advanced", status);
	}
	
	/**
	 * TODO Add documentation here.
	 */
	public boolean getAdvanced (){
		return this.prefs.getBoolean("advanced", false);
	}
}
