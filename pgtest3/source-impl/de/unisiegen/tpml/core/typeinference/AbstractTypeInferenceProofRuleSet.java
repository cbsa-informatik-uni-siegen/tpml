/**
 * 
 */
package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;

/**
 * @author benjamin
 *
 */
public abstract class AbstractTypeInferenceProofRuleSet extends
		AbstractProofRuleSet {

	protected AbstractTypeInferenceProofRuleSet(Language language) {
		super(language);
		
	}

}
