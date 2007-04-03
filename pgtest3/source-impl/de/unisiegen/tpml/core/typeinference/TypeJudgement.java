package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.Environment;

public class TypeJudgement implements TypeFormula {
	
	//
	// Attributes
	//
	
	private DefaultTypeEnvironment environment;
	
	private Expression expression;
	
	private MonoType type;
	
	public TypeJudgement (DefaultTypeEnvironment env, Expression expr, MonoType t) {
		environment = env;
		expression = expr;
		type = t;
	}
	
	//
	// Accessors
	//

	public DefaultTypeEnvironment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(DefaultTypeEnvironment environment) {
		this.environment = environment;
	}

	public MonoType getType() {
		return this.type;
	}

	public void setType(MonoType type) {
		this.type = type;
	}

	public Expression getExpression() {
		return this.expression;
	}

	
	  //
	  // Base methods
	  //
	  
	  /**
	   * Returns the string representation of the equations contained in this list. This method is mainly useful
	   * for debugging purposes.
	   * 
	   * @return the string representation.
	   * 
	   * @see TypeEquation#toString()
	   * @see java.lang.Object#toString()
	   */
	  @Override
	  public String toString() {
	    StringBuilder builder = new StringBuilder(128);
	    builder.append(environment);
	    builder.append(" \u22b3 ");
	    builder.append(expression);
	    builder.append(" :: ");
	    builder.append(type);
	    return builder.toString();
	  }

		public TypeEquation substitute(TypeSubstitution s) {
			this.type.substitute(s);
			return null;
		}
}
