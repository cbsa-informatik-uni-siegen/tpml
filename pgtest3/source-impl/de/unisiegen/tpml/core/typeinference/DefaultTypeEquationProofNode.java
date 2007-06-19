package de.unisiegen.tpml.core.typeinference ;


import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * DefaultTypeEquationProofNode is a presentation of a TypeCheckerProofNode used
 * for unification. This node owns a list of subtitutions and a list of
 * equations.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class DefaultTypeEquationProofNode extends DefaultTypeCheckerProofNode

		implements TypeEquationProofNode {

  //
  // Attributes
  //
  /**
   * list of collected type substitutions initialised with empty list
   */
  private final TypeSubstitutionList substitutions = TypeSubstitutionList.EMPTY_LIST ;



  /**
   * list of collected type equations
   */
  private TypeEquationTypeInference equation ;


  /**
   * The choosen mode.
   */
  private boolean mode ;


  //
  // Constructors
  //
  /**
   * Allocates a new <code>DefaultTypeEquationProofNode</code>
   * 
   * @param pEnvironment1 type environment of this node
   * @param pExpression1 expression of this node
   * @param pType1 type of this node
   * @param eqns equations of this node
   * @param pMode signal if we are in advanced or beginner mode
   */
  public DefaultTypeEquationProofNode ( final TypeEnvironment pEnvironment1 ,
      final Expression pExpression1 , final MonoType pType1 ,
      final TypeEquationTypeInference eqns , boolean pMode )
  {
    super ( pEnvironment1 , pExpression1 , pType1 ) ;
    this.equation = eqns ;
    this.mode = pMode ;
  }


  /**
   * extend a new substitution to the list of substitutions for this node
   * 
   * @param s1 type substitution to add to list
   */
  public void addSubstitution ( final DefaultTypeSubstitution s1 )
  {
    this.substitutions.extend ( s1 ) ;
  }


  //
  // Accessors
  //
  /**
   * get the type equation list of this node
   * 
   * @return equations TypeEquationList equations
   */
  public TypeEquationTypeInference getEquation ( )
  {
    return this.equation ;
  }


  /**
   * get the actual choosen mode, true means advanced, false beginner mode
   * 
   * @return boolean mode
   */
  public boolean getMode ( )
  {
    return this.mode ;
  }
}
