package de.unisiegen.tpml.graphics;

import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.graphics.bigstep.BigStepView;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceView;

public class ProofViewFactory {

	
	public static ProofView newTypeCheckerView (TypeCheckerProofModel model) {
		return new TypeCheckerView (model);
	}
	
	public static ProofView newBigStepView (BigStepProofModel model) {
		return new BigStepView (model);
	}
	
	public static ProofView newSmallStepView (SmallStepProofModel model) {
		return new SmallStepView (model);
	}
	
	//public static ProofView newTypeInferenceView (TypeCheckerProofModel model) {
	//	return new TypeInferenceView (model);
	//}
	
	public static ProofView newTypeInferenceView (TypeInferenceProofModel model) {
		return new TypeInferenceView (model);
	}
	
}