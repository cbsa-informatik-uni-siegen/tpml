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
 */
public class PreferenceManager {
	private Preferences prefs;

	public PreferenceManager() {
		prefs = Preferences.userNodeForPackage(this.getClass());
	}

	public void setRecentlyUsed(LinkedList<HistoryItem> list) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			prefs.put("item" + i, list.get(i).getFile().toURI().toASCIIString());
		}

	}

	public LinkedList<HistoryItem> getRecentlyUsed() {
		int count = 0;
		String result = "";
		LinkedList<HistoryItem> list = new LinkedList<HistoryItem>();
		while (true) {
			result = prefs.get("item"+count, "end");
			if (result.equals("end")) break;
			
			list.add(new HistoryItem(new File(result)));
			count++;
		}
		return list;
	}
	
	public void setAdvanced (boolean status){
		prefs.putBoolean("advanced", status);
	}
	
	public boolean getAdvanced (){
		return prefs.getBoolean("advanced", false);
	}
}
