package de.unisiegen.tpml.graphics;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;

public class ProofViewFactory {

	
	public ProofView newTypeCheckerView (TypeCheckerProofModel model) {
		return (ProofView) new TypeCheckerView (model);
	}
	
}
