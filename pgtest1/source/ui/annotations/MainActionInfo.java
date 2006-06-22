package ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainActionInfo {
	/**
	 * 
	 * @return The name of the action.
	 */
	public String name();
	/**
	 * @return The Path to the icon file relative to the classpath.
	 */
	public String icon();
	/**
	 * Defines into which menu the action shall be put.
	 * @return 
	 */
	public int visibleMenu();
	
	/**
	 * @return the keycode to be held for shortcuts.
	 */
	public int accelModifiers();
	
	/**
	 * @return the keycode to be pressed for shortcuts.
	 */
	public int accelKey();
	
	public static final int MENU_HIDDEN = 0;
	public static final int MENU_FILE = 1;
	public static final int MENU_EDIT = 2;
	public static final int MENU_RUN = 3;
	/**
	 * Defines if an actions is put into the toolbar.
	 * @return
	 */
	public int visibleToolbar();
	
	public static final int TOOLBAR_HIDDEN = 4;
	public static final int TOOLBAR_VISIBLE = 5;
	
	
}
