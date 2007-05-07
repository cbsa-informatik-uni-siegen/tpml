package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RowType;

public abstract class AbstractSubTyping {


	AbstractSubTyping() {

	}

	public static boolean check(MonoType type1, MonoType type2) {

		if (type1 instanceof ObjectType && type2 instanceof ObjectType)
			return checkObjectType ( type1, type2 );
		if (type1 instanceof RowType && type2 instanceof RowType)
			return checkRowType ( ( RowType ) type1, ( RowType ) type2 );
		if (type1 instanceof ArrowType && type2 instanceof ArrowType)
			return checkArrowType ( ( ArrowType ) type1, ( ArrowType ) type2 );
		return checkType ( type1, type2 );
	}

	private static boolean checkArrowType(ArrowType type, ArrowType type2) {
		MonoType taul = type.getTau1 ( );
		MonoType taur = type.getTau2 ( );

		MonoType tau2l = type2.getTau1 ( );
		MonoType tau2r = type2.getTau2 ( );

		return check ( taul, tau2l ) && check ( taur, tau2r );

	}

	private static boolean checkType(MonoType type1, MonoType type2) {
		return type1.equals ( type2 );

	}

	private static boolean checkObjectType(MonoType type1, MonoType type2) {
		RowType r1 = ( ( ObjectType ) type1 ).getPhi ( );
		RowType r2 = ( ( ObjectType ) type2 ).getPhi ( );
		return checkRowType ( r1, r2 );
	}

	private static boolean checkRowType(RowType type1, RowType type2) {
		boolean goOn = false;

		Identifier[] ids1 = type1.getIdentifiers ( );
		Identifier[] ids2 = type2.getIdentifiers ( );

		MonoType[] types1 = type1.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );

		for (int i = 0; i < ids1.length; i++ ) {
			goOn = false;
			for (int j = 0; j < ids2.length; j++ ) {
				if (ids1[i].equals ( ids2[j] ))
					goOn = check ( types1[i], types2[j] );
			}
			if (goOn)
				continue;
			break;
		}

		return goOn;
	}
}
