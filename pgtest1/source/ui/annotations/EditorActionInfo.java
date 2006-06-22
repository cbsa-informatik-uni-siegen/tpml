package ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EditorActionInfo {
	/**
	 * Defines if the action is visible in the toolbar within the editorwindow.
	 * 
	 * @return true if visible
	 */
	public boolean visible();

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
	 * @return the keycode to be held for shortcuts.
	 */
	public int accelModifiers();

	/**
	 * @return the keycode to be pressed for shortcuts.
	 */
	public int accelKey();
}
