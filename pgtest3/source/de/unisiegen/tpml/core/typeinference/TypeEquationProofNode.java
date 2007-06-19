package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;

public interface TypeEquationProofNode extends TypeCheckerProofNode {

	public TypeEquationTypeInference getEquation();
	
	public boolean getMode();
}
