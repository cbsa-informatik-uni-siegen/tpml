package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.UnifyType;

public class DefaultMinimalTypingTypesProofNode extends
		AbstractMinimalTypingProofNode implements MinimalTypingTypesProofNode{
	private SeenTypes < DefaultSubType > seenTypes;
	DefaultSubType subtype;

	public DefaultMinimalTypingTypesProofNode ( MonoType pType,
			MonoType pType2 ) {
		super ( new Unify() );
		subtype = new DefaultSubType(pType, pType2);
		
	}

	public MonoType getType ( ) {
		return this.subtype.getSubtype ( );
	}

	public MonoType getType2 ( ) {
		return this.subtype.getOvertype ( );
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
    builder.append(this.subtype.getSubtype ( ));
    builder.append(" &#60: ");
    builder.append(this.subtype.getOvertype ( ));
    if (getRule() != null) {
      builder.append(" (" + getRule() + ")");
    }
    return builder.toString();
  }

	/**
	 * {@inheritDoc} 
	 * 
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSeenTypes()
	 */
	public SeenTypes < DefaultSubType > getSeenTypes ( ) {
		return this.seenTypes;
	}

	public DefaultSubType getSubType ( ) {
	return this.subtype;
}

	public DefaultTypeEnvironment getEnvironment(){
		return new DefaultTypeEnvironment();
	}

}
