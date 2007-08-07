package de.unisiegen.tpml.core.languages.l2c ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Class ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Inherit ;
import de.unisiegen.tpml.core.expressions.New ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2o.L2OBigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;


/**
 * Big step proof rules for the <b>L2C</b> and derived languages.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 * @see L2OBigStepProofRuleSet
 */
public class L2CBigStepProofRuleSet extends L2OBigStepProofRuleSet
{
  /**
   * Allocates a new <code>L2CBigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L2C</b> or a derived language.
   * 
   * @param pL2CLanguage The language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L2OBigStepProofRuleSet#L2OBigStepProofRuleSet(L2OLanguage)
   */
  public L2CBigStepProofRuleSet ( L2CLanguage pL2CLanguage )
  {
    super ( pL2CLanguage ) ;
    registerByMethodName ( L2CLanguage.L2C , "CLASS" , "applyClass" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateClass" ) ; //$NON-NLS-1$
    registerByMethodName ( L2CLanguage.L2C , "NEW" , "applyNew" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateNew" ) ; //$NON-NLS-1$
    registerByMethodName ( L2CLanguage.L2C , "INHERIT" , "applyInherit" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateInherit" ) ; //$NON-NLS-1$
  }


  /**
   * Applies the <b>(CLASS)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(CLASS)</b> rule to.
   */
  public void applyClass ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Class classExpr = ( Class ) pNode.getExpression ( ) ;
    Expression body = classExpr.getBody ( ) ;
    pContext.addProofNode ( pNode , body ) ;
  }


  /**
   * Applies the <b>(NEW)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(NEW)</b> rule to.
   */
  public void applyNew ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    New newExpr = ( New ) pNode.getExpression ( ) ;
    Expression e = newExpr.getE ( ) ;
    pContext.addProofNode ( pNode , e ) ;
  }


  /**
   * Applies the <b>(INHERIT)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(INHERIT)</b> rule to.
   */
  public void applyInherit ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    Inherit inherit = ( Inherit ) pNode.getExpression ( ) ;
    Expression body = inherit.getBody ( ) ;
    pContext.addProofNode ( pNode , body ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(CLASS)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(CLASS)</b>.
   */
  public void updateClass ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      Class classExpr = ( Class ) pNode.getExpression ( ) ;
      Expression body = pNode.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      pContext.setProofNodeResult ( pNode , new Class ( classExpr.getId ( ) ,
          classExpr.getTau ( ) , body ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(NEW)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(NEW)</b>.
   */
  public void updateNew ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      Class classExpr = ( Class ) pNode.getChildAt ( 0 ).getResult ( )
          .getValue ( ) ;
      Row row = ( Row ) classExpr.getBody ( ) ;
      pContext.setProofNodeResult ( pNode , new ObjectExpr (
          classExpr.getId ( ) , classExpr.getTau ( ) , row ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(INHERIT)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(INHERIT)</b>.
   */
  public void updateInherit ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      Inherit inherit = ( Inherit ) pNode.getExpression ( ) ;
      Expression e = inherit.getE ( ) ;
      pContext.addProofNode ( pNode , e ) ;
    }
    else if ( ( pNode.getChildCount ( ) == 2 )
        && ( pNode.getChildAt ( 1 ).isProven ( ) ) )
    {
      Inherit inherit = ( Inherit ) pNode.getExpression ( ) ;
      Class classExpr = ( Class ) pNode.getChildAt ( 1 ).getResult ( )
          .getValue ( ) ;
      Row r1 = ( Row ) classExpr.getBody ( ) ;
      Row r2 = ( Row ) pNode.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      // dom_a(r1) = A
      ArrayList < Identifier > attributeIdentifierR1 = new ArrayList < Identifier > ( ) ;
      for ( Expression e : r1.getExpressions ( ) )
      {
        if ( e instanceof Attribute )
        {
          attributeIdentifierR1.add ( ( ( Attribute ) e ).getId ( ) ) ;
        }
      }
      if ( attributeIdentifierR1.size ( ) != inherit.getIdentifiers ( ).length )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "L2CBigStepProofRuleSet.0" ) ) ; //$NON-NLS-1$
      }
      for ( Identifier a : inherit.getIdentifiers ( ) )
      {
        boolean found = false ;
        for ( Identifier attributeId : attributeIdentifierR1 )
        {
          if ( a.equals ( attributeId ) )
          {
            found = true ;
            break ;
          }
        }
        if ( ! found )
        {
          throw new IllegalArgumentException ( Messages
              .getString ( "L2CBigStepProofRuleSet.0" ) ) ; //$NON-NLS-1$
        }
      }
      try
      {
        Row row = Row.union ( r1 , r2 ) ;
        pContext.setProofNodeResult ( pNode , row ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "L2CBigStepProofRuleSet.0" ) ) ; //$NON-NLS-1$
      }
    }
  }
}
