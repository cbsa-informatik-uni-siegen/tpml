package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.AbstractProofNode;

class AbstractTypeInferenceProofNode extends AbstractProofNode
		implements
			TypeInferenceProofNode
{



	public boolean isProven()
	{
		
		return (this.getRules().length>0);
	}

}
