package de.unisiegen.tpml.graphics;


import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.unify.UnifyProofModel;
import de.unisiegen.tpml.graphics.bigstep.BigStepView;
import de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingView;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView;
import de.unisiegen.tpml.graphics.subtyping.SubTypingView;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceView;
import de.unisiegen.tpml.graphics.unify.UnifyView;


/**
 * The proof view factory.
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @version $Id$
 */
public class ProofViewFactory
{

  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newTypeCheckerView ( TypeCheckerProofModel model )
  {
    return new TypeCheckerView ( model );
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newBigStepView ( BigStepProofModel model )
  {
    return new BigStepView ( model );
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newSmallStepView ( SmallStepProofModel model )
  {
    return new SmallStepView ( model );
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newTypeInferenceView ( TypeInferenceProofModel model )
  {
    return new TypeInferenceView ( model );
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newSubTypingView ( SubTypingProofModel model )
  {
    return new SubTypingView ( model );
    // return new TypeEditorPanel(language);
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newSubTypingRecView ( RecSubTypingProofModel model )
  {
    return new SubTypingView ( model );
    // return new TypeEditorPanel(language);
  }


  /**
   * TODO
   * 
   * @param model
   * @return TODO
   */
  public static ProofView newMinimalTypingView ( MinimalTypingProofModel model )
  {
    return new MinimalTypingView ( model );
  }


  /**
   * TODO
   *
   * @param model
   * @return TODO
   */
  public static ProofView newUnifyView ( UnifyProofModel model )
  {
    return new UnifyView ( model );
  }
}
