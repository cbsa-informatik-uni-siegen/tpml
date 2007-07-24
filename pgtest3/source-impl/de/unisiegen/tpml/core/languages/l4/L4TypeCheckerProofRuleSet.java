package de.unisiegen.tpml.core.languages.l4 ;


import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l3.L3TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.UnitType;


/**
 * The type proof rules for the <code>L4</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Rev: 287 $
 * @see de.unisiegen.tpml.core.languages.l3.L3TypeCheckerProofRuleSet
 */
public class L4TypeCheckerProofRuleSet extends L3TypeCheckerProofRuleSet
{
  /**
   * Allocates a new <code>L4TypecheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L4</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4TypeCheckerProofRuleSet ( L4Language language )
  {
    super ( language ) ;
    // register the additional type rules
    registerByMethodName ( L4Language.L4 , "COND-1" , "applyCond1" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L4Language.L4 , "SEQ" , "applySeq" ) ; //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L4Language.L4 , "WHILE" , "applyWhile" ) ; //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Applies the <b>(COND-1)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyCond1 ( TypeCheckerProofContext context ,
		  TypeCheckerProofNode pNode )
  {
	  TypeCheckerExpressionProofNode node = (TypeCheckerExpressionProofNode) pNode;
    Condition1 condition1 = ( Condition1 ) node.getExpression ( ) ;
    context.addEquation ( node.getType ( ) , new UnitType ( ) ) ;
    context.addProofNode ( node , node.getEnvironment ( ) ,
        condition1.getE0 ( ) , new BooleanType ( ) ) ;
    context.addProofNode ( node , node.getEnvironment ( ) ,
        condition1.getE1 ( ) , node.getType ( ) ) ;
  }


  /**
   * {@inheritDoc} The language <code>L4</code> introduces references and as
   * such, the <b>(P-LET)</b> rule must be adjusted to allow type
   * quantification - and thereby application of <b>(P-LET)</b> - only if the
   * <code>e1</code> is a value, according to
   * {@link de.unisiegen.tpml.core.expressions.Expression#isValue()}.
   * 
   * @see de.unisiegen.tpml.core.languages.l3.L3TypeCheckerProofRuleSet#applyPLet(de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext,
   *      de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode)
   */
  @ Override
  public void applyPLet ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node )
  {
    // for Let, LetRec and MultiLet, e1 must be a value
    Expression expression = node.getExpression ( ) ;
    if ( expression instanceof Let )
    {
      Let let = ( Let ) expression ;
      if ( ! let.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(P-LET) can only be applied if e1 is a value" ) ; //$NON-NLS-1$
      }
    }
    else if ( expression instanceof MultiLet )
    {
      MultiLet multiLet = ( MultiLet ) expression ;
      if ( ! multiLet.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(P-LET) can only be applied if e1 is a value" ) ; //$NON-NLS-1$
      }
    }
    super.applyPLet ( context , node ) ;
  }


  /**
   * Applies the <b>(SEQ)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applySeq ( TypeCheckerProofContext context ,
		  TypeCheckerProofNode pNode )
  {
	  TypeCheckerExpressionProofNode node = (TypeCheckerExpressionProofNode) pNode;
    Sequence sequence = ( Sequence ) node.getExpression ( ) ;
    context.addProofNode ( node , node.getEnvironment ( ) , sequence.getE1 ( ) ,
        context.newTypeVariable ( ) ) ;
    context.addProofNode ( node , node.getEnvironment ( ) , sequence.getE2 ( ) ,
        node.getType ( ) ) ;
  }


  /**
   * Applies the <b>(WHILE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof context.
   */
  public void applyWhile ( TypeCheckerProofContext context ,
		  TypeCheckerProofNode pNode )
  {
	  TypeCheckerExpressionProofNode node = (TypeCheckerExpressionProofNode) pNode;
    While loop = ( While ) node.getExpression ( ) ;
    context.addEquation ( node.getType ( ) , new UnitType ( ) ) ;
    context.addProofNode ( node , node.getEnvironment ( ) , loop.getE1 ( ) ,
        new BooleanType ( ) ) ;
    context.addProofNode ( node , node.getEnvironment ( ) , loop.getE2 ( ) ,
        node.getType ( ) ) ;
  }
}
