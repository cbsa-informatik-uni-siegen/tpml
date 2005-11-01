package smallstep;

public abstract class Expression {

  /**
   */
  public abstract Expression substitute(String id, Expression e);

  /**
   */
  public abstract Expression evaluate(RuleChain ruleChain);

}
