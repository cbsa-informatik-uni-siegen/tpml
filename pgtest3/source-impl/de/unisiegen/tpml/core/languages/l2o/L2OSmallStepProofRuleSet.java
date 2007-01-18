package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;


public class L2OSmallStepProofRuleSet extends L2SmallStepProofRuleSet
{
  public L2OSmallStepProofRuleSet ( L2OLanguage language )
  {
    super ( language ) ;
    register ( L2OLanguage.L2O , "OBJECT" , true ) ;
    register ( L2OLanguage.L2O , "METHOD-EVAL" , false ) ;
    register ( L2OLanguage.L2O , "METHOD-EXEC" , true ) ;
  }


  public Expression evaluateObjects ( SmallStepProofContext context ,
      ObjectExpr objects )
  {
    // determine the sub expressions
    Expression [ ] expressions = objects.getExpressions ( ) ;
    // find the first sub expression that is not already a value
    for ( int n = 0 ; n < expressions.length ; n ++ )
    {
      // check if the expression is not already a value
      if ( ! expressions [ n ].isValue ( ) )
      {
        // we're about to perform (OBJECT) or (METHOD-EVAL)
        if ( objects.getMethod ( ) == null )
        {
          context.addProofStep ( getRuleByName ( "OBJECT" ) , objects ) ;
        }
        else
        {
          context.addProofStep ( getRuleByName ( "METHOD-EVAL" ) , objects ) ;
        }
        // try to evaluate the expression
        Expression newExpression = evaluate ( context , expressions [ n ] ) ;
        // check if we need to forward an exception
        if ( newExpression.isException ( ) )
        {
          return newExpression ;
        }
        // otherwise generate a new object with the new expression
        Expression [ ] newExpressions = expressions.clone ( ) ;
        newExpressions [ n ] = newExpression ;
        String [ ] tmpID = new String [ objects.getIdentifiers ( ).length ] ;
        for ( int i = 0 ; i < objects.getIdentifiers ( ).length ; i ++ )
        {
          tmpID [ i ] = objects.getIdentifiers ( i ) ;
        }
        if ( objects.getMethod ( ) == null )
        {
          return new ObjectExpr ( tmpID , newExpressions ) ;
        }
        return new ObjectExpr ( tmpID , newExpressions , new String ( objects
            .getMethod ( ) ) ) ;
      }
    }
    if ( objects.getMethod ( ) == null )
    {
      return objects ;
    }
    context.addProofStep ( getRuleByName ( "METHOD-EXEC" ) , objects ) ;
    for ( int i = 0 ; i < objects.getIdentifiers ( ).length ; i ++ )
    {
      if ( objects.getMethod ( ).equals ( objects.getIdentifiers ( i ) ) )
      {
        return objects.getExpressions ( i ) ;
      }
    }
    return objects ;
  }
}
