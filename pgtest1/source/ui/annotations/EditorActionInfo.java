package ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EditorActionInfo {
	/**
	 * 
	 * @return The name of the action.
	 */
	public String name();
	/**
	 * @return The Path to the icon file relative to the classpath.
	 */
	public String icon();	
}
