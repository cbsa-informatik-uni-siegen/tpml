package de.unisiegen.tpml.core.bigstepclosure;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.languages.Language;


/**
 * TODO
 *
 */
public class AbstractBigStepClosureProofRuleSet extends AbstractProofRuleSet
{
  public AbstractBigStepClosureProofRuleSet(Language language)
  {
    super(language);
  }
  
  protected void register( final int group, final String name, final Method applyMethod,
      final Method updateMethod)
  {
    register (new AbstractBigStepClosureProofRule (group, name)
    {
      protected void applyInternal( BigStepClosureProofContext context,
          BigStepClosureProofNode node) throws Exception
      {
        applyMethod.invoke ( AbstractBigStepClosureProofRuleSet.this, context, node );
      }
      
      protected void updateInternal(BigStepClosureProofContext context,
          BigStepClosureProofNode node) throws Exception
      {
        updateMethod.invoke(AbstractBigStepClosureProofRuleSet.this, context, node);
      }
    });
  }

  protected void register ( int group, String name, Method applyMethod )
  {
    register ( group, name, applyMethod, null );
  }

  protected void unregister ( String name )
  {
    unregister ( getRuleByName ( name ) );
  }
  
  protected void registerByMethodName ( int group, String name,
      String applyMethodName )
  {
    register ( group, name, getMethodByName ( applyMethodName ) );
  }
  
  protected void registerByMethodName ( int group, String name,
      String applyMethodName, String updateMethodName )
  {
    register ( group, name, getMethodByName ( applyMethodName ),
        getMethodByName ( updateMethodName ) );
  }
  
  private Method getMethodByName ( String methodName )
  {
    try
    {
      // lookup the method with the parameters BigStepProofContext and
      // BigStepProofNode
      return getClass ().getMethod ( methodName, new Class []
      { BigStepProofContext.class, BigStepProofNode.class } );
    }
    catch ( RuntimeException e )
    {
      // just re-throw the exception
      throw e;
    }
    catch ( Exception e )
    {
      // translate the exception to a runtime exception
      throw new RuntimeException ( "Method " + methodName + " not found", e ); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }
}
