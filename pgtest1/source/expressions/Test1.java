package expressions;

import java.util.ListIterator;

public class Test1 {
  private static Expression simplePlus() {
    return new Application(
        new Application(
            ArithmeticOperator.PLUS,
            new IntegerConstant(1)),
        new IntegerConstant(3));
  }
  
  private static Expression simpleInfixPlus() {
    return new InfixOperation(
        ArithmeticOperator.MULT,
        new InfixOperation(
            ArithmeticOperator.PLUS,
            new IntegerConstant(1),
            new IntegerConstant(10)),
        new IntegerConstant(8));
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
            BooleanConstant.TRUE),
        UnitConstant.UNIT);
  }
  
  private static Expression simpleAbstr() {
    return new Application(
        new Application(
            new Abstraction(
                "x",
                new Application(
                    ArithmeticOperator.DIV,
                    new Identifier("x"))),
            new IntegerConstant(4)),
        new IntegerConstant(0));
  }
  
  private static Expression simpleLet() {
    return new Let(
        "f",
        new Condition(
            BooleanConstant.TRUE,
            new Abstraction(
                "x",
                new Application(
                    new Application(
                        ArithmeticOperator.PLUS,
                        new IntegerConstant(1)),
                    new Identifier("x"))),
            UnitConstant.UNIT),
        new Application(
            new Identifier("f"),
            new IntegerConstant(9)));
  }

  private static void evaluate(Expression e) {
    System.out.println(e);
    while (!e.isValue() && !(e instanceof Exn)) {
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
    
    evaluate(simpleAbstr());
    
    evaluate(simpleLet());
    
    evaluate(simpleInfixPlus());
  }
}
