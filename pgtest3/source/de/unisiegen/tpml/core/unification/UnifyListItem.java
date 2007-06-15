package de.unisiegen.tpml.core.unification;

import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;


//TODO Doku
public interface UnifyListItem 
{
	public TypeEquationListTypeChecker getEquationList();
	
	public TypeSubstitution getSubstitution();
	
}
