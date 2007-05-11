package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TupleType;

/**
 * 
 * This abstract class checks if one type is a subtype of another type.
 *
 * @author Benjamin Mies
 *
 */
public abstract class AbstractSubTyping {

	/**
	 * 
	 * This method awaits two MonoTypes, and returns true if one type is a subtype
	 * of the other one.
	 *
	 * @param type1 MonoType first type
	 * @param type2 MonoType second type
	 * @return boolean is Subtype
	 */
	public static boolean check(MonoType type1, MonoType type2) {

		if (type1 instanceof ObjectType && type2 instanceof ObjectType)
			return checkObjectType ( type1, type2 );
		if (type1 instanceof RowType && type2 instanceof RowType)
			return checkRowType ( ( RowType ) type1, ( RowType ) type2 );
		if (type1 instanceof ArrowType && type2 instanceof ArrowType)
			return checkArrowType ( ( ArrowType ) type1, ( ArrowType ) type2 );
		if (type1 instanceof RefType && type2 instanceof RefType)
			return checkRefType ( ( RefType ) type1, ( RefType ) type2 );
		if (type1 instanceof TupleType && type2 instanceof TupleType)
			return checkTupleType ( ( TupleType ) type1, ( TupleType ) type2 );
		if (type1 instanceof ListType && type2 instanceof ListType)
			return checkListType ( ( ListType ) type1, ( ListType ) type2 );
		return checkType ( type1, type2 );
	}

	private static boolean checkListType(ListType type, ListType type2) {
		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );
		return check ( tau, tau2 );

	}

	private static boolean checkTupleType(TupleType type, TupleType type2) {
		MonoType[] types = type.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );
		if (types.length != types2.length)
			return false;
		for (int i = 0; i < types.length; i++ ) {
			if (!types[i].equals ( types2[i] ))
				;
			return false;
		}
		return true;
	}

	private static boolean checkRefType(RefType type, RefType type2) {
		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );
		return check ( tau, tau2 );
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
		Identifier[] ids1 = null;
		Identifier[] ids2 = null;
		MonoType[] types1 = null;
		MonoType[] types2 = null;

		if (type1.getIdentifiers ( ).length < type2.getIdentifiers ( ).length) {

			ids1 = type1.getIdentifiers ( );
			ids2 = type2.getIdentifiers ( );

			types1 = type1.getTypes ( );
			types2 = type2.getTypes ( );
		} else {
			ids1 = type2.getIdentifiers ( );
			ids2 = type1.getIdentifiers ( );

			types1 = type2.getTypes ( );
			types2 = type1.getTypes ( );
		}

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
