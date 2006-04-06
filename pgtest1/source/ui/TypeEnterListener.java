
package ui;

import java.util.EventListener;

import javax.swing.JComponent;

import typing.MonoType;
import typing.ProofNode;

public interface TypeEnterListener extends EventListener {

	public void typeAccepted (JComponent gui, String typeString, ProofNode node, MonoType type);
	
	public void typeRejected (JComponent gui);
	
}
