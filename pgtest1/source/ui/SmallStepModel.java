package ui;


import java.util.LinkedList;
import smallstep.*;


public class SmallStepModel {
		
	private Expression 	originExpression;
	private Expression 	currentExpression;
	private RuleChain	currentRuleChain;
	
	
	public SmallStepModel(Expression e) {
		originExpression = currentExpression = e;
		currentRuleChain = null;
	}
	
/*	
	public int getNumberOfSteps() {
		return ruleChaines.size();
	}
	
	public int getNumberOfRules(int step) {
		if (step >= ruleChaines.size())
			return 0;
		return ruleChaines.get(step).getRules().size();
	}
	
	public Rule getRule(int step, int n) {
		if (step >= ruleChaines.size())
			throw new IllegalStateException("Step: " + step + " not in range of ruleChaines");
		
		RuleChain rc = ruleChaines.get(step);
		if (n >= rc.getRules().size())
			throw new IllegalStateException("n: " + n + " not in range of RuleChaing");
		
		return rc.getRules().get(n); 
	}
*/
	
	public Expression getOriginExpression() {
		return (originExpression);
	}
	
	public Expression getCurrentExpression() {
		return (currentExpression);
	}
	
	public RuleChain getCurrentRuleChain() {
		return (currentRuleChain);
	}
	
	public int evaluateNextStep() {
		if ((currentExpression instanceof Value) || (currentExpression instanceof Exn))
			return 1;
		currentRuleChain = new RuleChain();
		currentExpression = currentExpression.evaluate(currentRuleChain);
		
		if (currentRuleChain.isEmpty())
			return -1;
			
		return 0;
	}
}
