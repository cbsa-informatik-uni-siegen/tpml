package typer;

import java.util.LinkedList;
import java.util.Stack;

import typer.analysis.DepthFirstAdapter;
import typer.node.AArrowType;
import typer.node.ABoolType;
import typer.node.AIntType;
import typer.node.ATupleType;
import typer.node.AUnitType;

import typing.ArrowType;
import typing.PrimitiveType;
import typing.TupleType;
import typing.MonoType;

public class Translator extends DepthFirstAdapter {

	public Translator() {
		this.types = new Stack<MonoType>();
	}
	
	public void outAArrowType(AArrowType node) {
		MonoType t2 = this.types.pop();
		MonoType t1 = this.types.pop();
		this.types.push(new ArrowType (t1, t2));
	}

	public void outABoolType(ABoolType node) {
		types.push(PrimitiveType.BOOL);
	}

	public void outAIntType(AIntType node) {
		types.push(PrimitiveType.INT);
	}

	public void outATupleType(ATupleType node) {
		LinkedList typeList = node.getType();
		MonoType[] types = new MonoType [typeList.size()];
		for (int i=types.length-1; i>=0; i--) {
			types [i] = this.types.pop();
		}
		this.types.push(new TupleType (types));
	}

	public void outAUnitType(AUnitType node) {
		types.push(PrimitiveType.UNIT);
	}
	
	public MonoType getType () {
	    assert (this.types.size() == 1);
	    return this.types.get(0);
	}

	private Stack<MonoType>		types;

}
