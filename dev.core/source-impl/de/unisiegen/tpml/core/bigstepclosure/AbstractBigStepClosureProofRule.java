package de.unisiegen.tpml.core.bigstepclosure;

import java.lang.reflect.InvocationTargetException;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.exceptions.RowSubstitutionException;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.UnificationException;


/**
 * TODO
 *
 */
public abstract class AbstractBigStepClosureProofRule extends AbstractProofRule
implements BigStepClosureProofRule
{
  public AbstractBigStepClosureProofRule(final int group, final String name)
  {
    super (group, name);
  }
  
  public void apply(BigStepClosureProofContext context, BigStepClosureProofNode node) throws ProofRuleException
  {
    try
    {
      applyInternal(context, node);
    }
    catch(ProofRuleException e)
    {
      e.printStackTrace ();
      throw e;
    }
    catch ( InvocationTargetException e )
    {
      if ( ( e.getTargetException () instanceof NotOnlyFreeVariableException )
          || ( e.getTargetException () instanceof RowSubstitutionException ) )
      {
        throw new RuntimeException ( e.getTargetException ().getMessage () );
      }
      throw new ProofRuleException ( node, this, e.getTargetException () );
    }
    catch ( Exception e )
    {
      // check if e contains a usable error message
      for ( Throwable t = e ; t != null ; t = t.getCause () )
      {
        if ( t instanceof IllegalArgumentException
            && t instanceof UnificationException )
        {
          throw new ProofRuleException ( t.getMessage (), node, this, e );
        }
      }
      throw new ProofRuleException ( node, this, e );
    }
  }
  
  protected abstract void applyInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;
  
  
  public void update ( BigStepClosureProofContext context, BigStepClosureProofNode node )
  {
    try
    {
      updateInternal(context, node);
    }
    catch(Exception e)
    {
      // TODO
    }
  }
  
  protected abstract void updateInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;
  
  BigStepClosureProofRule toExnRule ( int n )
  {
    if ( n < 0 )
    {
      throw new IllegalArgumentException ( "n is negative" ); //$NON-NLS-1$
    }
    return newNoopRule ( getName () + "-EXN-" + ( n + 1 ) ); //$NON-NLS-1$
  }
  
  public static BigStepClosureProofRule newNoopRule ( String name )
  {
    return new AbstractBigStepClosureProofRule ( -1, name )
    {

      @Override
      protected void applyInternal ( @SuppressWarnings ( "unused" )
      BigStepClosureProofContext context, @SuppressWarnings ( "unused" )
      BigStepClosureProofNode node ) throws Exception
      {
        throw new IllegalArgumentException ( "Cannot apply noop rules" ); //$NON-NLS-1$
      }


      @Override
      protected void updateInternal ( @SuppressWarnings ( "unused" )
      BigStepClosureProofContext context, @SuppressWarnings ( "unused" )
      BigStepClosureProofNode node ) throws Exception
      {
        // nothing to do here...
      }
    };
  }

  
  public LatexCommandList getLatexCommands ()
  {
    return null;
  }
  
  public LatexInstructionList getLatexInstructions ()
  {
    return null;
  }
  
  public LatexPackageList getLatexPackages ()
  {
    return null;
  }
  
  public final LatexString toLatexString ()
  {
    return null;
  }
  
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    return null;
  }
  
  public final PrettyString toPrettyString ()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    return null;
  }
}
