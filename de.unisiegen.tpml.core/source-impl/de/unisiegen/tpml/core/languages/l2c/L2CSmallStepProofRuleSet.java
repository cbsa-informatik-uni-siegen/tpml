package de.unisiegen.tpml.core.languages.l2c ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Inherit ;
import de.unisiegen.tpml.core.expressions.Class ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.New ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;
import de.unisiegen.tpml.core.languages.l2o.L2OSmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;


/**
 * Small step proof rules for the <code>L2C</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev:1132 $
 * @see L2OSmallStepProofRuleSet
 */
public class L2CSmallStepProofRuleSet extends L2OSmallStepProofRuleSet
{
  /**
   * The string of the <code>CLASS-EVAL</code> rule.
   */
  private static final String CLASS_EVAL = "CLASS-EVAL" ; //$NON-NLS-1$


  /**
   * The string of the <code>NEW-EVAL</code> rule.
   */
  private static final String NEW_EVAL = "NEW-EVAL" ; //$NON-NLS-1$


  /**
   * The string of the <code>NEW-EXEC</code> rule.
   */
  private static final String NEW_EXEC = "NEW-EXEC" ; //$NON-NLS-1$


  /**
   * The string of the <code>INHERIT-RIGHT</code> rule.
   */
  private static final String INHERIT_RIGHT = "INHERIT-RIGHT" ; //$NON-NLS-1$


  /**
   * The string of the <code>INHERIT-LEFT</code> rule.
   */
  private static final String INHERIT_LEFT = "INHERIT-LEFT" ; //$NON-NLS-1$


  /**
   * The string of the <code>INHERIT-EXEC</code> rule.
   */
  private static final String INHERIT_EXEC = "INHERIT-EXEC" ; //$NON-NLS-1$


  /**
   * Allocates a new <code>L2CSmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L2C</tt> or a derived
   * language.
   * 
   * @param pL2CLanguage The {@link Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L2OSmallStepProofRuleSet#L2OSmallStepProofRuleSet(L2OLanguage)
   */
  public L2CSmallStepProofRuleSet ( L2CLanguage pL2CLanguage )
  {
    super ( pL2CLanguage ) ;
    // Class
    register ( L2CLanguage.L2C , CLASS_EVAL , false ) ;
    // New
    register ( L2CLanguage.L2C , NEW_EVAL , false ) ;
    register ( L2CLanguage.L2C , NEW_EXEC , true ) ;
    // Inherit
    register ( L2CLanguage.L2C , INHERIT_RIGHT , false ) ;
    register ( L2CLanguage.L2C , INHERIT_LEFT , false ) ;
    register ( L2CLanguage.L2C , INHERIT_EXEC , true ) ;
  }


  /**
   * Evaluates the {@link Class} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pClass The {@link Class}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateClass ( SmallStepProofContext pContext ,
      Class pClass )
  {
    /*
     * If the Expression is a Class, we can only perform CLASS-EVAL.
     */
    pContext.addProofStep ( getRuleByName ( CLASS_EVAL ) , pClass ) ;
    Expression body = evaluate ( pContext , pClass.getBody ( ) ) ;
    if ( body.isException ( ) )
    {
      return body ;
    }
    return new Class ( pClass.getId ( ) , pClass.getTau ( ) , body ) ;
  }


  /**
   * Evaluates the {@link New} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pNew The {@link New}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateNew ( SmallStepProofContext pContext , New pNew )
  {
    /*
     * If the Expression is a New and the e of the New is not a value, we can
     * perform NEW-EVAL.
     */
    if ( ! pNew.getE ( ).isValue ( ) )
    {
      pContext.addProofStep ( getRuleByName ( NEW_EVAL ) , pNew ) ;
      Expression e = evaluate ( pContext , pNew.getE ( ) ) ;
      if ( e.isException ( ) )
      {
        return e ;
      }
      return new New ( e ) ;
    }
    /*
     * If the Expression is a New and the e of the New is a Class and the e of
     * the Class is a Row, we can perform NEW-EXEC.
     */
    else if ( ( pNew.getE ( ) instanceof Class )
        && ( ( ( Class ) pNew.getE ( ) ).getBody ( ) instanceof Row ) )
    {
      pContext.addProofStep ( getRuleByName ( NEW_EXEC ) , pNew ) ;
      Class tmpClass = ( Class ) pNew.getE ( ) ;
      Row row = ( Row ) tmpClass.getBody ( ) ;
      return new ObjectExpr ( tmpClass.getId ( ) , tmpClass.getTau ( ) , row ) ;
    }
    return pNew ;
  }


  /**
   * Evaluates the {@link Inherit} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pBody The {@link Inherit}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateBody ( SmallStepProofContext pContext , Inherit pBody )
  {
    /*
     * If the Expression is a Inherit and the body of the Inherit is a Inherit, we can
     * perform INHERIT-RIGHT.
     */
    if ( pBody.getBody ( ) instanceof Inherit )
    {
      pContext.addProofStep ( getRuleByName ( INHERIT_RIGHT ) , pBody ) ;
      Expression body = evaluate ( pContext , pBody.getBody ( ) ) ;
      if ( body.isException ( ) )
      {
        return body ;
      }
      return new Inherit ( pBody.getIdentifiers ( ) , pBody.getE ( ) , body ) ;
    }
    /*
     * If the Expression is a Inherit and the body of the Inherit is a Row and the e
     * of the Inherit is not yet a value, we can perform INHERIT-LEFT.
     */
    else if ( ( pBody.getBody ( ) instanceof Row )
        && ( ! pBody.getE ( ).isValue ( ) ) )
    {
      pContext.addProofStep ( getRuleByName ( INHERIT_LEFT ) , pBody ) ;
      Expression e = evaluate ( pContext , pBody.getE ( ) ) ;
      if ( e.isException ( ) )
      {
        return e ;
      }
      return new Inherit ( pBody.getIdentifiers ( ) , e , pBody.getBody ( ) ) ;
    }
    /*
     * If the Expression is a Inherit and the body of the Inherit is a Row and the e
     * of the Inherit is a Class and the e of the Class is a Row, we can perform
     * INHERIT-EXEC.
     */
    else if ( ( pBody.getE ( ) instanceof Class )
        && ( ( ( Class ) pBody.getE ( ) ).getBody ( ) instanceof Row )
        && ( pBody.getBody ( ) instanceof Row ) )
    {
      Class tmpClass = ( Class ) pBody.getE ( ) ;
      Row r1 = ( Row ) tmpClass.getBody ( ) ;
      Row r2 = ( Row ) pBody.getBody ( ) ;
      // dom_a(r1) = A
      ArrayList < Identifier > attributeIdentifierR1 = new ArrayList < Identifier > ( ) ;
      for ( Expression e : r1.getExpressions ( ) )
      {
        if ( e instanceof Attribute )
        {
          attributeIdentifierR1.add ( ( ( Attribute ) e ).getId ( ) ) ;
        }
      }
      if ( attributeIdentifierR1.size ( ) != pBody.getIdentifiers ( ).length )
      {
        return pBody ;
      }
      for ( Identifier a : pBody.getIdentifiers ( ) )
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
          return pBody ;
        }
      }
      try
      {
        Row row = Row.union ( r1 , r2 ) ;
        pContext.addProofStep ( getRuleByName ( INHERIT_EXEC ) , pBody ) ;
        return row ;
      }
      catch ( LanguageParserMultiException e )
      {
        return pBody ;
      }
    }
    return pBody ;
  }
}
