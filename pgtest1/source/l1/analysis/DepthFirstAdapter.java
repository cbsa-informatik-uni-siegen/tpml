/* This file was generated by SableCC (http://www.sablecc.org/). */

package l1.analysis;

import java.util.*;
import l1.node.*;

public class DepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(Node node)
    {
    }

    public void defaultOut(Node node)
    {
    }

    public void caseStart(Start node)
    {
        inStart(node);
        node.getPExpression().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAApplicationExpression(AApplicationExpression node)
    {
        defaultIn(node);
    }

    public void outAApplicationExpression(AApplicationExpression node)
    {
        defaultOut(node);
    }

    public void caseAApplicationExpression(AApplicationExpression node)
    {
        inAApplicationExpression(node);
        if(node.getE0() != null)
        {
            node.getE0().apply(this);
        }
        if(node.getE1() != null)
        {
            node.getE1().apply(this);
        }
        outAApplicationExpression(node);
    }

    public void inAConditionExpression(AConditionExpression node)
    {
        defaultIn(node);
    }

    public void outAConditionExpression(AConditionExpression node)
    {
        defaultOut(node);
    }

    public void caseAConditionExpression(AConditionExpression node)
    {
        inAConditionExpression(node);
        if(node.getE0() != null)
        {
            node.getE0().apply(this);
        }
        if(node.getE1() != null)
        {
            node.getE1().apply(this);
        }
        if(node.getE2() != null)
        {
            node.getE2().apply(this);
        }
        outAConditionExpression(node);
    }

    public void inALetExpression(ALetExpression node)
    {
        defaultIn(node);
    }

    public void outALetExpression(ALetExpression node)
    {
        defaultOut(node);
    }

    public void caseALetExpression(ALetExpression node)
    {
        inALetExpression(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getE1() != null)
        {
            node.getE1().apply(this);
        }
        if(node.getE2() != null)
        {
            node.getE2().apply(this);
        }
        outALetExpression(node);
    }

    public void inAAbstractionExpression(AAbstractionExpression node)
    {
        defaultIn(node);
    }

    public void outAAbstractionExpression(AAbstractionExpression node)
    {
        defaultOut(node);
    }

    public void caseAAbstractionExpression(AAbstractionExpression node)
    {
        inAAbstractionExpression(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getE() != null)
        {
            node.getE().apply(this);
        }
        outAAbstractionExpression(node);
    }

    public void inARecursionExpression(ARecursionExpression node)
    {
        defaultIn(node);
    }

    public void outARecursionExpression(ARecursionExpression node)
    {
        defaultOut(node);
    }

    public void caseARecursionExpression(ARecursionExpression node)
    {
        inARecursionExpression(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getE() != null)
        {
            node.getE().apply(this);
        }
        outARecursionExpression(node);
    }

    public void inAInfixExpression(AInfixExpression node)
    {
        defaultIn(node);
    }

    public void outAInfixExpression(AInfixExpression node)
    {
        defaultOut(node);
    }

    public void caseAInfixExpression(AInfixExpression node)
    {
        inAInfixExpression(node);
        if(node.getE1() != null)
        {
            node.getE1().apply(this);
        }
        if(node.getOp() != null)
        {
            node.getOp().apply(this);
        }
        if(node.getE2() != null)
        {
            node.getE2().apply(this);
        }
        outAInfixExpression(node);
    }

    public void inAIdentifierExpression(AIdentifierExpression node)
    {
        defaultIn(node);
    }

    public void outAIdentifierExpression(AIdentifierExpression node)
    {
        defaultOut(node);
    }

    public void caseAIdentifierExpression(AIdentifierExpression node)
    {
        inAIdentifierExpression(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        outAIdentifierExpression(node);
    }

    public void inAUnitExpression(AUnitExpression node)
    {
        defaultIn(node);
    }

    public void outAUnitExpression(AUnitExpression node)
    {
        defaultOut(node);
    }

    public void caseAUnitExpression(AUnitExpression node)
    {
        inAUnitExpression(node);
        if(node.getUnit() != null)
        {
            node.getUnit().apply(this);
        }
        outAUnitExpression(node);
    }

    public void inATrueExpression(ATrueExpression node)
    {
        defaultIn(node);
    }

    public void outATrueExpression(ATrueExpression node)
    {
        defaultOut(node);
    }

    public void caseATrueExpression(ATrueExpression node)
    {
        inATrueExpression(node);
        if(node.getTrue() != null)
        {
            node.getTrue().apply(this);
        }
        outATrueExpression(node);
    }

    public void inAFalseExpression(AFalseExpression node)
    {
        defaultIn(node);
    }

    public void outAFalseExpression(AFalseExpression node)
    {
        defaultOut(node);
    }

    public void caseAFalseExpression(AFalseExpression node)
    {
        inAFalseExpression(node);
        if(node.getFalse() != null)
        {
            node.getFalse().apply(this);
        }
        outAFalseExpression(node);
    }

    public void inANumberExpression(ANumberExpression node)
    {
        defaultIn(node);
    }

    public void outANumberExpression(ANumberExpression node)
    {
        defaultOut(node);
    }

    public void caseANumberExpression(ANumberExpression node)
    {
        inANumberExpression(node);
        if(node.getNumber() != null)
        {
            node.getNumber().apply(this);
        }
        outANumberExpression(node);
    }

    public void inAPlusExpression(APlusExpression node)
    {
        defaultIn(node);
    }

    public void outAPlusExpression(APlusExpression node)
    {
        defaultOut(node);
    }

    public void caseAPlusExpression(APlusExpression node)
    {
        inAPlusExpression(node);
        if(node.getPlus() != null)
        {
            node.getPlus().apply(this);
        }
        outAPlusExpression(node);
    }

    public void inAMinusExpression(AMinusExpression node)
    {
        defaultIn(node);
    }

    public void outAMinusExpression(AMinusExpression node)
    {
        defaultOut(node);
    }

    public void caseAMinusExpression(AMinusExpression node)
    {
        inAMinusExpression(node);
        if(node.getMinus() != null)
        {
            node.getMinus().apply(this);
        }
        outAMinusExpression(node);
    }

    public void inAMultiplyExpression(AMultiplyExpression node)
    {
        defaultIn(node);
    }

    public void outAMultiplyExpression(AMultiplyExpression node)
    {
        defaultOut(node);
    }

    public void caseAMultiplyExpression(AMultiplyExpression node)
    {
        inAMultiplyExpression(node);
        if(node.getMultiply() != null)
        {
            node.getMultiply().apply(this);
        }
        outAMultiplyExpression(node);
    }

    public void inADivideExpression(ADivideExpression node)
    {
        defaultIn(node);
    }

    public void outADivideExpression(ADivideExpression node)
    {
        defaultOut(node);
    }

    public void caseADivideExpression(ADivideExpression node)
    {
        inADivideExpression(node);
        if(node.getDivide() != null)
        {
            node.getDivide().apply(this);
        }
        outADivideExpression(node);
    }

    public void inAModuloExpression(AModuloExpression node)
    {
        defaultIn(node);
    }

    public void outAModuloExpression(AModuloExpression node)
    {
        defaultOut(node);
    }

    public void caseAModuloExpression(AModuloExpression node)
    {
        inAModuloExpression(node);
        if(node.getModulo() != null)
        {
            node.getModulo().apply(this);
        }
        outAModuloExpression(node);
    }

    public void inALowerThanExpression(ALowerThanExpression node)
    {
        defaultIn(node);
    }

    public void outALowerThanExpression(ALowerThanExpression node)
    {
        defaultOut(node);
    }

    public void caseALowerThanExpression(ALowerThanExpression node)
    {
        inALowerThanExpression(node);
        if(node.getLowerThan() != null)
        {
            node.getLowerThan().apply(this);
        }
        outALowerThanExpression(node);
    }

    public void inAGreaterThanExpression(AGreaterThanExpression node)
    {
        defaultIn(node);
    }

    public void outAGreaterThanExpression(AGreaterThanExpression node)
    {
        defaultOut(node);
    }

    public void caseAGreaterThanExpression(AGreaterThanExpression node)
    {
        inAGreaterThanExpression(node);
        if(node.getGreaterThan() != null)
        {
            node.getGreaterThan().apply(this);
        }
        outAGreaterThanExpression(node);
    }

    public void inALowerEqualExpression(ALowerEqualExpression node)
    {
        defaultIn(node);
    }

    public void outALowerEqualExpression(ALowerEqualExpression node)
    {
        defaultOut(node);
    }

    public void caseALowerEqualExpression(ALowerEqualExpression node)
    {
        inALowerEqualExpression(node);
        if(node.getLowerEqual() != null)
        {
            node.getLowerEqual().apply(this);
        }
        outALowerEqualExpression(node);
    }

    public void inAGreaterEqualExpression(AGreaterEqualExpression node)
    {
        defaultIn(node);
    }

    public void outAGreaterEqualExpression(AGreaterEqualExpression node)
    {
        defaultOut(node);
    }

    public void caseAGreaterEqualExpression(AGreaterEqualExpression node)
    {
        inAGreaterEqualExpression(node);
        if(node.getGreaterEqual() != null)
        {
            node.getGreaterEqual().apply(this);
        }
        outAGreaterEqualExpression(node);
    }

    public void inAEqualExpression(AEqualExpression node)
    {
        defaultIn(node);
    }

    public void outAEqualExpression(AEqualExpression node)
    {
        defaultOut(node);
    }

    public void caseAEqualExpression(AEqualExpression node)
    {
        inAEqualExpression(node);
        if(node.getEqual() != null)
        {
            node.getEqual().apply(this);
        }
        outAEqualExpression(node);
    }
}
