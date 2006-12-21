package de.unisiegen.tpml.core.unification;

import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

//TODO Doku
public interface UnifyListItem 
{
	public TypeEquationList getEquationList();
	
	public TypeSubstitution getSubstitution();
	
}
