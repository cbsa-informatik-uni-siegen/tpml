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

}
