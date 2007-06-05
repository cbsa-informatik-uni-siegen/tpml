package de.unisiegen.tpml.core.subtypingrec;

import de.unisiegen.tpml.core.types.MonoType;

public class DefaultSubType {
	
	private MonoType subtype;
	private MonoType overtype;
	
	public DefaultSubType(MonoType type, MonoType type2){
		subtype = type;
		overtype = type2;
	}

	public MonoType getOvertype ( ) {
		return this.overtype;
	}

	public MonoType getSubtype ( ) {
		return this.subtype;
	}
	
  public boolean equals ( Object pObject ){
  	if (pObject instanceof DefaultSubType){
  		DefaultSubType other = (DefaultSubType) pObject;
  		
  		return (this.subtype.equals ( other.subtype ) && this.overtype.equals ( other.overtype ));
  		
  	}
  	return false;
  }
	
	public String toString(){
		final StringBuilder builder = new StringBuilder ( );
		builder.append ( subtype );
		builder.append ( " &#60: " );
		builder.append ( overtype );
		return builder.toString ( );
	}

}
