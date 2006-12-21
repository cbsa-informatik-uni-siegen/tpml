package de.unisiegen.tpml.core.unification;

import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;


//TODO Doku
public interface UnifyListItem 
{
	public TypeEquationList getEquationList();
	
	public TypeSubstitution getSubstitution();
	
}
