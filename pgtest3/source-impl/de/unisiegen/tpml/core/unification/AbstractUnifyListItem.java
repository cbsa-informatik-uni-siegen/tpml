package de.unisiegen.tpml.core.unification;

import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

class AbstractUnifyListItem implements UnifyListItem
{
	private TypeEquationList typeEquationList;

	private TypeSubstitution typeSubstitution;
	
	public TypeEquationList getEquationList()
	{
		// TODO Auto-generated method stub
		return typeEquationList;
	}

	public TypeSubstitution getSubstitution()
	{
		// TODO Auto-generated method stub
		return typeSubstitution;
	}

}
