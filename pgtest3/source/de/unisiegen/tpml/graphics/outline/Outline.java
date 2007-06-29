package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This interface is the main interface of the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public interface Outline
{
  /**
   * An interface for all execute enums.
   * 
   * @author Christian Fehler
   */
  public interface Execute
  {
    // Nothing to do here.
  }


  /**
   * Indicates who loads the new Expression or Type.
   * 
   * @author Christian Fehler
   */
  public enum ExecuteAutoChange implements Execute
  {
    /**
     * Auto change from the <code>Editor</code>.
     */
    EDITOR ,
    /**
     * Auto change from the <code>SmallStep</code>.
     */
    SMALLSTEP ,
    /**
     * Auto change from the <code>BigStep</code>.
     */
    BIGSTEP ,
    /**
     * Auto change from the <code>TypeChecker</code>.
     */
    TYPECHECKER ,
    /**
     * Auto change from the <code>TypeInference</code>.
     */
    TYPEINFERENCE ,
    /**
     * Auto change from the <code>Subtyping</code>.
     */
    SUBTYPING ,
    /**
     * Auto change from the <code>MinimalTyping</code>.
     */
    MINIMALTYPING
  }


  /**
   * Indicates who loads the new Expression or Type.
   * 
   * @author Christian Fehler
   */
  public enum ExecuteInit implements Execute
  {
    /**
     * Initialized from the <code>Editor</code>.
     */
    EDITOR ,
    /**
     * Initialized from the <code>SmallStep</code>.
     */
    SMALLSTEP ,
    /**
     * Initialized from the <code>BigStep</code>.
     */
    BIGSTEP ,
    /**
     * Initialized from the <code>TypeChecker</code>.
     */
    TYPECHECKER ,
    /**
     * Initialized from the <code>TypeInference</code>.
     */
    TYPEINFERENCE ,
    /**
     * Initialized from the <code>Subtyping</code>.
     */
    SUBTYPING ,
    /**
     * Initialized from the <code>MinimalTyping</code>.
     */
    MINIMALTYPING
  }


  /**
   * Indicates who loads the new Expression or Type.
   * 
   * @author Christian Fehler
   */
  public enum ExecuteMouseClick implements Execute
  {
    /**
     * Change by mouse cick from the <code>Editor</code>.
     */
    EDITOR ,
    /**
     * Change by mouse cick from the <code>SmallStep</code>.
     */
    SMALLSTEP ,
    /**
     * Change by mouse cick from the <code>BigStep</code>.
     */
    BIGSTEP ,
    /**
     * Change by mouse cick from the <code>TypeChecker</code>.
     */
    TYPECHECKER ,
    /**
     * Change by mouse cick from the <code>TypeInference</code>.
     */
    TYPEINFERENCE ,
    /**
     * Change by mouse cick from the <code>Subtyping</code>.
     */
    SUBTYPING ,
    /**
     * Change by mouse cick from the <code>MinimalTyping</code>.
     */
    MINIMALTYPING
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   */
  public JPanel getPanel ( ) ;


  /**
   * This method loads a new {@link Expression} into the {@link Outline}. It
   * does nothing if the auto update is disabled and the change does not come
   * from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public void loadExpression ( Expression pExpression , Outline.Execute pExecute ) ;


  /**
   * This method loads a new {@link Type} into the {@link Outline}. It does
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>.
   * 
   * @param pType The new {@link Type}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public void loadType ( Type pType , Outline.Execute pExecute ) ;
}
