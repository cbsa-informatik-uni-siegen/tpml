package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.UnifyType;

public class DefaultMinimalTypingTypesProofNode extends
		AbstractMinimalTypingProofNode implements MinimalTypingTypesProofNode{
	MonoType type;
	MonoType type2;

	public DefaultMinimalTypingTypesProofNode ( MonoType pType,
			MonoType pType2 ) {
		super ( new Unify() );
		this.type = pType;
		this.type2 = pType2;
		
	}

	public MonoType getType ( ) {
		return this.type;
	}

	public MonoType getType2 ( ) {
		return this.type2;
	}
	
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   * 
   * Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.type);
    builder.append(" &#60: ");
    builder.append(this.type2);
    if (getRule() != null) {
      builder.append(" (" + getRule() + ")");
    }
    return builder.toString();
  }

}
