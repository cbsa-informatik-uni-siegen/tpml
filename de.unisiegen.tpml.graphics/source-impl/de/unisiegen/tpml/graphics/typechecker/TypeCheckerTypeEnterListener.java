/**
 * 
 */
package de.unisiegen.tpml.graphics.typechecker;

import java.util.EventListener;

/**
 * @author marcell
 *
 */
public interface TypeCheckerTypeEnterListener extends EventListener {
	
	public void typeEntered (String type);
	
	public void canceled ();

}
