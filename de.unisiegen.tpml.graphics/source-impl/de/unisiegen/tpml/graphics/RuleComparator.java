package de.unisiegen.tpml.graphics;

import java.util.Comparator;

import de.unisiegen.tpml.core.ProofRule;

public class RuleComparator implements Comparator<ProofRule> {

	public int compare(ProofRule r1, ProofRule r2) {
		
		return r1.getName().compareTo(r2.getName());
	}

}
