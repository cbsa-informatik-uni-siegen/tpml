package ui;

import java.util.EventListener;
import java.util.EventObject;
import typing.MonoType;
import typing.ProofNode;
import typing.Rule;

public interface TypeCheckerEventListener extends EventListener {

	public void applyRule (EventObject o, ProofNode node, Rule rule);
	

	public void guessType (EventObject o, ProofNode node, MonoType type);
}
