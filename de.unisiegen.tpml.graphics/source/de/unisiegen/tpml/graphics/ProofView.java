package de.unisiegen.tpml.graphics;

import de.unisiegen.tpml.core.ProofGuessException;

public interface ProofView {

	public void guess () throws IllegalStateException, ProofGuessException;
	
}
