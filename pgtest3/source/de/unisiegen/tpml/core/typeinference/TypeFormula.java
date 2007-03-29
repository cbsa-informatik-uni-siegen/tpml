package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

public interface TypeFormula {
	
	//
	// Attributes
	//
	
		
	public Expression getExpression();
	
	public DefaultTypeEnvironment getEnvironment();
	
	public MonoType getType();
	
	public String toString();
	
}
