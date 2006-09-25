package de.unisiegen.tpml.graphics;

import javax.swing.JComboBox;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.theme.Theme;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;

public class ProofViewFactory {

	
	public static ProofView newTypeCheckerView (TypeCheckerProofModel model) {
		AbstractRenderer.setTheme(new Theme (), new JComboBox());
		return (ProofView) new TypeCheckerView (model);
	}
	
}
