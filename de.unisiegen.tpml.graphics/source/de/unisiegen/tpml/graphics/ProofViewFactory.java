package de.unisiegen.tpml.graphics;

import javax.swing.JComboBox;

import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.bigstep.BigStepView;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView;
import de.unisiegen.tpml.graphics.theme.Theme;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;

public class ProofViewFactory {

	
	public static ProofView newTypeCheckerView (TypeCheckerProofModel model) {
		AbstractRenderer.setTheme(new Theme (), new JComboBox());
		return new TypeCheckerView (model);
	}
	
	public static ProofView newBigStepView (BigStepProofModel model) {
		AbstractRenderer.setTheme (new Theme(), new JComboBox ());
		return new BigStepView (model);
	}
	
	public static ProofView newSmallStepView (SmallStepProofModel model) {
		AbstractRenderer.setTheme (new Theme() , new JComboBox ());
		return new SmallStepView (model);
	}
	
}
