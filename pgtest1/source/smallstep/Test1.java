package smallstep;

import java.util.ListIterator;

public class Test1 {
  private static Expression simplePlus() {
    return new Application(
        new Application(
            ArithmeticOperator.PLUS,
            new IntegerConstant(1)),
        new IntegerConstant(3));
  }
  
  private static Expression simpleCond() {
    return new Application(
        new Condition(
            new Application(
                new Application(
                    RelationalOperator.EQUALS,
                    new IntegerConstant(1)),
                new IntegerConstant(2)),
            UnitConstant.UNIT,
            new BooleanConstant(true)),
        UnitConstant.UNIT);
  }

  private static void evaluate(Expression e) {
    System.out.println(e);
    while (!(e instanceof Value) && !(e instanceof Exn)) {
      RuleChain ruleChain = new RuleChain();
      e = e.evaluate(ruleChain);
      
      if (ruleChain.isEmpty())
        break;
      
      System.out.print(" -> " + e + " [");
      
      ListIterator<Rule> it = ruleChain.listIterator();
      while (it.hasNext()) {
        if (it.hasPrevious())
          System.out.print(" ");
        System.out.print("(" + it.next() + ")");
      }
      System.out.println("]");
    }
    System.out.println();
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    evaluate(simplePlus());
    
    evaluate(simpleCond());
  }
}
