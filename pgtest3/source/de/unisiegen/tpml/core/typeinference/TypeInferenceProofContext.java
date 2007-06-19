package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

public interface TypeInferenceProofContext extends TypeCheckerProofContext {

	public void addSubstitution(TypeSubstitution s);
	
	public ArrayList < DefaultTypeSubstitution >  getSubstitution();

	public SeenTypes < TypeEquationTypeInference > getSeenTypes ( );

	public void addSeenType ( TypeEquationTypeInference eqn );
	
	
}
