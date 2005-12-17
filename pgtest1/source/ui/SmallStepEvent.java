package ui;

import java.util.EventObject;
import smallstep.*;

public class SmallStepEvent extends EventObject {
	
	private Rule 		rule;
	
	public SmallStepEvent (Rule rule, Object src) {
		super (src);
		this.rule = rule;
	}
	
	public Rule getRule() {
		return this.rule;
	}

}
